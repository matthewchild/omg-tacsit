/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Mar 28, 2011
 */
package org.omg.tacsit.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import org.omg.tacsit.ui.viewport.EntityViewport;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.omg.tacsit.common.util.ObjectUtils;
import org.omg.tacsit.repository.EntityRepository;
import org.omg.tacsit.repository.RepositoryChangeEvent;
import org.omg.tacsit.repository.RepositoryListener;

/**
 * A ViewportManager that manages a set of EntityViewports.
 * <p>
 * This also provides the capability to manipulate the SelectionManager of the associated EntityViewports.  If
 * the ViewportManager is operating with a VIEWPORT_INDEPENDENT SelectionMethodology, changes to any Viewport's
 * SelectionManager will affect all other managed Viewport's SelectionManager.  It will also implicitly update
 * the EntityViewportManager's viewportIndependentSelectionManager.
 * @author Matthew Child
 */
public class EntityViewportManager implements ViewportManager
{

  private Set<EntityViewport> viewports;
  private Set<ViewportManagerListener> listeners;
  private Map<EntityViewport, SelectionManager> viewportToOldSelectionManager;
  private RepositoryListener repositoryListener;
  private EntityRepository entityRepository;
  private SelectionManager viewportIndependentSelectionManager;
  private SelectionMethodology selectionMethodology;
  private PropertyChangeListener viewportSelectionPropertyListener;

  /**
   * Creates a new instance.
   */
  public EntityViewportManager()
  {
    repositoryListener = new RepositoryToViewportAdapter();
    viewports = new HashSet();
    listeners = new HashSet();
    viewportToOldSelectionManager = new HashMap();
    viewportIndependentSelectionManager = createDefaultSelectionManager();
    viewportSelectionPropertyListener = new ViewportSelectionPropertyListener();
    selectionMethodology = SelectionMethodology.VIEWPORT_DEPENDENT;
  }

  private SelectionManager createDefaultSelectionManager()
  {
    return new SingleSelectionManager();
  }

  @Override
  public Collection<Viewport> getViewports()
  {
    // This is necessary to prevent compile-time errors.  It is unsafe to pass this as a return value, because
    // the add method could be called to add a Viewport.  From the compiler's perspective, this would be okay,
    // because you're adding a Viewport to a Collection of Viewports.  However, we have a more specific type
    // here, and we don't want them adding Viewports that aren't EntityViewports.
    //
    // This cast is safe because the returned collection is unmodifiable.
    Collection untypedCollection = viewports;
    return Collections.unmodifiableCollection((Collection<Viewport>) untypedCollection);
  }

  /**
   * Notifies the ViewportManagerListeners that a new Viewport has been added.
   * @param viewport The Viewport that was added.
   */
  protected void fireViewAdded(Viewport viewport)
  {
    ViewportManagerEvent event = null;
    for (ViewportManagerListener listener : listeners)
    {
      if (event == null)
      {
        event = new ViewportManagerEvent(viewport, this);
      }
      listener.viewportAdded(event);
    }
  }

  /**
   * Notifies the ViewportManagerListeners that a Viewport has been removed.
   * @param viewport The Viewport that was removed.
   */
  protected void fireViewRemoved(Viewport viewport)
  {
    ViewportManagerEvent event = null;
    for (ViewportManagerListener listener : listeners)
    {
      if (event == null)
      {
        event = new ViewportManagerEvent(viewport, this);
      }
      listener.viewportRemoved(event);
    }
  }

  /**
   * Adds a Viewport to the EntityManager.  Only accepts EntityViewports.
   * @param viewport The Viewport to add to the manager.  Must be an EntityViewport.
   */
  @Override
  public void addViewport(Viewport viewport)
  {
    if (!(viewport instanceof EntityViewport))
    {
      throw new IllegalArgumentException("viewport must be a " + EntityViewport.class.getName());
    }

    EntityViewport entityViewport = (EntityViewport) viewport;
    boolean added = viewports.add(entityViewport);

    if (added)
    {
      addRepositoryEntitiesTo(entityViewport);

      // Update the viewport if we are using a common selection methodology.
      if (selectionMethodology == SelectionMethodology.VIEWPORT_INDEPENDENT)
      {
        convertToCommonSelectionManager(entityViewport);
      }
      fireViewAdded(viewport);
    }
  }

  @Override
  public void removeViewport(Viewport viewport)
  {
    boolean removed = viewports.remove(viewport);
    if (removed)
    {
      EntityViewport entityViewport = (EntityViewport) viewport;
      removeRepositoryEntitiesFrom(entityViewport);
      if (selectionMethodology == SelectionMethodology.VIEWPORT_INDEPENDENT)
      {
        revertViewportToOldSelectionManager(entityViewport);
      }
      fireViewRemoved(viewport);
    }
  }

  /**
   * Gets the SelectionManager that will be used if the ViewportManager is switched to a VIEWPORT_INDEPENDENT
   * SelectionMethodolgy
   * @return The viewport independent SelectionManager.
   */
  public SelectionManager getViewportIndependentSelectionManager()
  {
    return viewportIndependentSelectionManager;
  }

  /**
   * We don't want to update a viewport that's already been updated.  Since we're responding to PropertyChangeEvents,
   * if we try and remove a property change listener on the viewport that fired the event, then the viewport will
   * likely fire a concurrent modification exception.
   * @param selectionManager The new selection manager.
   * @param viewportsToUpdate The viewports that each SelectionManager will be applied to.
   */
  private void setViewportIndependentSelectionManager(SelectionManager selectionManager,
                                                      Collection<EntityViewport> viewportsToUpdate)
  {
    SelectionManager newIndependentSelectionManager;
    // If the argument is null, create a new default selection manager so it won't be empty.
    if (selectionManager == null)
    {
      newIndependentSelectionManager = createDefaultSelectionManager();
    }
    else
    {
      newIndependentSelectionManager = selectionManager;
    }

    this.viewportIndependentSelectionManager = newIndependentSelectionManager;

    // If we're using a selection manager that's independent of the viewport, update the associated viewports.
    if (this.selectionMethodology == SelectionMethodology.VIEWPORT_INDEPENDENT)
    {
      for (EntityViewport entityViewport : viewportsToUpdate)
      {
        entityViewport.removePropertyChangeListener(SELECTION_MANAGER_PROPERTY, viewportSelectionPropertyListener);
        entityViewport.setSelectionManager(this.viewportIndependentSelectionManager);
        entityViewport.addPropertyChangeListener(SELECTION_MANAGER_PROPERTY, viewportSelectionPropertyListener);
      }
    }
  }

  /**
   * Sets the SelectionManager that should be used if the ViewportManager is using a VIEWPORT_INDEPENDENT 
   * SelectionMethodology.  If the ViewportManager is currently using the VIEWPORT_INDEPENDENT methodology,
   * all of the associated Viewports will be updated.
   * @param selectionManager The SelectionManager that handles selection when the ViewportManager is operating in
   * VIEWPORT_INDEPENDENT mode.
   */
  public void setViewportIndependentSelectionManager(SelectionManager selectionManager)
  {
    setViewportIndependentSelectionManager(selectionManager, viewports);
  }

  @Override
  public void addViewportManagerListener(ViewportManagerListener listener)
  {
    listeners.add(listener);
  }

  @Override
  public void removeViewportManagerListener(ViewportManagerListener listener)
  {
    listeners.remove(listener);
  }

  @Override
  public EntityViewport getViewport(String name)
  {
    EntityViewport viewportWithName = null;

    for (EntityViewport viewport : viewports)
    {
      String viewportName = viewport.getName();
      if (ObjectUtils.areEqual(name, viewportName))
      {
        viewportWithName = viewport;
        break;
      }
    }

    return viewportWithName;
  }

  /**
   * Sets the EntityRepository that houses the entities that should be displayed in all Viewports.
   * @return The entity repository displayed in all Viewports.
   */
  public EntityRepository getEntityRepository()
  {
    return entityRepository;
  }

  /**
   * Sets the EntityRepository that houses the entities that should be displayed in all Viewports.
   * @param entityRepository The entity repository displayed in all Viewports.
   */
  public void setEntityRepository(EntityRepository entityRepository)
  {
    if (this.entityRepository != null)
    {
      this.entityRepository.removeRepositoryListener(repositoryListener);
    }
    clearViewportEntities();
    this.entityRepository = entityRepository;
    if (this.entityRepository != null)
    {
      this.entityRepository.addRepositoryListener(repositoryListener);
      for (EntityViewport viewport : viewports)
      {
        addRepositoryEntitiesTo(viewport);
      }
    }
  }

  private synchronized void addRepositoryEntitiesTo(EntityViewport viewport)
  {
    if (entityRepository != null)
    {
      Iterator<Entity> entities = entityRepository.getEntities();
      while (entities.hasNext())
      {
        Entity entity = entities.next();
        viewport.addEntity(entity);
      }
    }
  }

  private synchronized void removeRepositoryEntitiesFrom(EntityViewport viewport)
  {
    if (entityRepository != null)
    {
      Iterator<Entity> entities = entityRepository.getEntities();
      while (entities.hasNext())
      {
        Entity entity = entities.next();
        viewport.removeEntity(entity);
      }
    }
  }

  private synchronized void clearViewportEntities()
  {
    for (EntityViewport viewport : viewports)
    {
      viewport.clearEntities();
    }
  }

  private synchronized void addToAllViewports(Iterator<Entity> entities)
  {
    while (entities.hasNext())
    {
      Entity entity = entities.next();
      for (EntityViewport viewport : viewports)
      {
        viewport.addEntity(entity);
      }
    }
  }

  private synchronized void removeFromAllViewports(Iterator<Entity> entities)
  {
    while (entities.hasNext())
    {
      Entity entity = entities.next();
      for (EntityViewport viewport : viewports)
      {
        viewport.removeEntity(entity);
      }
    }
  }

  private synchronized void updateForAllViewports(Iterator<Entity> entities)
  {
    while (entities.hasNext())
    {
      Entity entity = entities.next();
      for (EntityViewport viewport : viewports)
      {
        viewport.updateEntity(entity);
      }
    }
  }
  private static final String SELECTION_MANAGER_PROPERTY = "selectionManager";

  /**
   * Note: this should only be called on viewports which are using the common selection manager
   * @param viewport 
   */
  private void revertViewportToOldSelectionManager(EntityViewport viewport)
  {
    // Safety check to ensure this function is only be called on viewports using the common selection manager.
    if (!viewportIndependentSelectionManager.equals(viewport.getSelectionManager()))
    {
      throw new IllegalArgumentException(
          "Unable to revert viewport to old selection manager; it's not using the common selection manager");
    }
    viewport.removePropertyChangeListener(SELECTION_MANAGER_PROPERTY, viewportSelectionPropertyListener);

    SelectionManager oldSelectionManager = this.viewportToOldSelectionManager.remove(viewport);
    importSelectionState(viewportIndependentSelectionManager, oldSelectionManager);
    viewport.setSelectionManager(oldSelectionManager);
  }

  private void revertViewportsToOldSelectionManager()
  {
    for (EntityViewport viewport : viewports)
    {
      revertViewportToOldSelectionManager(viewport);
    }
  }

  /**
   * Note: this should only be called on viewports which are using their own selection manager.
   * @param viewport 
   */
  private void convertToCommonSelectionManager(EntityViewport viewport)
  {
    // Safety check to ensure this function is only be called on viewports that are NOT using 
    //   the common selection manager.
    if (viewportIndependentSelectionManager.equals(viewport.getSelectionManager()))
    {
      throw new IllegalArgumentException(
          "Unable to convert viewport to use the common selection manager; it's already using it.");
    }
    SelectionManager oldSelectionManager = viewport.getSelectionManager();
    viewportToOldSelectionManager.put(viewport, oldSelectionManager);

    viewport.setSelectionManager(viewportIndependentSelectionManager);
    viewport.addPropertyChangeListener(SELECTION_MANAGER_PROPERTY, viewportSelectionPropertyListener);
  }

  private void convertViewportsToCommonSelectionManager()
  {
    // If we have at least 1 viewport
    if (!viewports.isEmpty())
    {
      // import the view state into the common selection model.
      EntityViewport viewport = viewports.iterator().next();
      SelectionManager firstSelectionManager = viewport.getSelectionManager();
      importSelectionState(firstSelectionManager, viewportIndependentSelectionManager);
    }

    for (EntityViewport viewport : viewports)
    {
      convertToCommonSelectionManager(viewport);
    }
  }

  /**
   * Do a best-effort import of the selection state from one manager to another.
   * @param source
   * @param target 
   */
  private void importSelectionState(SelectionManager source, SelectionManager target)
  {
    target.clearAllSelections();

    for (SelectionType selectionType : SelectionType.values())
    {
      List<Entity> selectedEntity = source.getSelection(selectionType);
      target.addToSelection(selectedEntity, selectionType);
    }
  }

  /**
   * Gets the SelectionMethodolgy used by this ViewportManager.
   * @return The selection methodology this viewport manager uses.
   */
  public SelectionMethodology getSelectionMethodology()
  {
    return selectionMethodology;
  }

  /**
   * Sets the SelectionMethodolgy used by this ViewportManager.
   * <p>
   * If switching to a VIEWPORT_INDEPENDENT SelectionMethodolgy, the current SelectionManager of all Viewports will
   * be archived, and all Viewports will be assigned to use the ViewportManager's viewportIndependentSelectionManager.
   * Any subsequent changes to any SelectionManager in any Viewport will cause all other Viewports and the
   * viewportIndependentSelectionManager to be updated.
   * <p>
   * A best-effort guess will be made to import the selection state from one of the from an old SelectionManager into
   * the new viewport-independent SelectionManager.  Any changes to the Selection state will cause all Viewports
   * to be updated.
   * <p>
   * If switching to a VIEWPORT_DEPENDENT SelectionMethodology, each Viewport will be restored to its original
   * SelectionManager.  A best-effort guess will be made to import the selection state from the viewport independent
   * SelectionManager into each Viewport's SelectionManager.
   * 
   * @param selectionMethodology The selectionMethodology that describes whether or not Viewports share selection state.
   */
  public void setSelectionMethodology(SelectionMethodology selectionMethodology)
  {
    if (selectionMethodology == null)
    {
      throw new IllegalArgumentException("selectionMethodology may not be null");
    }
    if (this.selectionMethodology != selectionMethodology)
    {
      this.selectionMethodology = selectionMethodology;
      switch (this.selectionMethodology)
      {
        case VIEWPORT_DEPENDENT:
          this.selectionMethodology = selectionMethodology;
          revertViewportsToOldSelectionManager();
          break;
        case VIEWPORT_INDEPENDENT:
          this.selectionMethodology = selectionMethodology;
          convertViewportsToCommonSelectionManager();
          break;
        default:
          throw new IllegalArgumentException("selectionMethodology " + selectionMethodology + " not yet implemented.");
      }
    }
  }

  /**
   * Gets the SelectionManager used by the first Viewport with a particular name
   * @param viewportName The name of the Viewport to get the SelectionManager for.
   * @return The SelectionManager that Viewport uses.
   */
  public SelectionManager getSelectionManager(String viewportName)
  {
    EntityViewport viewportWithName = getViewport(viewportName);
    return viewportWithName.getSelectionManager();
  }

  private class RepositoryToViewportAdapter implements RepositoryListener
  {

    public void entitiesAdded(RepositoryChangeEvent event)
    {
      Collection<Entity> addedEntities = event.getEntities();
      addToAllViewports(addedEntities.iterator());
    }

    public void entitiesRemoved(RepositoryChangeEvent event)
    {
      Collection<Entity> removedEntities = event.getEntities();
      removeFromAllViewports(removedEntities.iterator());
    }

    public void entitiesCleared(RepositoryChangeEvent event)
    {
      clearViewportEntities();
    }

    public void entitiesUpdated(RepositoryChangeEvent event)
    {
      Collection<Entity> updatedEntities = event.getEntities();
      updateForAllViewports(updatedEntities.iterator());
    }
  }

  private class ViewportSelectionPropertyListener implements PropertyChangeListener
  {

    private List<EntityViewport> getOtherViewports(Object eventSource)
    {
      EntityViewport changedViewport = (EntityViewport) eventSource;
      List<EntityViewport> viewportListCopy = new ArrayList(viewports);
      viewportListCopy.remove(changedViewport);
      return viewportListCopy;
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
      List<EntityViewport> otherViewports = getOtherViewports(evt.getSource());

      SelectionManager newSelectionManager = (SelectionManager) evt.getNewValue();
      setViewportIndependentSelectionManager(newSelectionManager, otherViewports);
    }
  }
}
