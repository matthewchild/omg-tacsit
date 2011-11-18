/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Mar 28, 2011
 */

package org.omg.tacsit.controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.omg.tacsit.common.util.CollectionUtils;
import org.omg.tacsit.common.util.PropertyListenable;
import org.omg.tacsit.query.QueryManager;
import org.omg.tacsit.repository.EntityRepository;

/**
 * A default implementation of a TacsitController.
 * <p>
 * The TacsitController makes use of the EntityRepository interface for entity management.  That EntityRepository
 * is associated with the ViewportManager, which is responsible for distributing the entities to each Viewport
 * for display.  An implementation of EntityRepository is provided 
 * {@link org.omg.tacsit.repository.PolledEntityRepository}.
 * <p>
 * This Projections and EntityTypes this controller supports are configurable.   Adapting this for an implementation
 * of the standard may be as easy as the following steps:
 * <p>
 * <li>Import the data from your data source to a {@link org.omg.tacsit.repository.PolledEntityRepository}.  Associate
 * that repository with the DefaultTacsitController.</li>
 * <li>Create an enumeration that implements ProjectionType, and associate that with the DefaultTacsitController.</li>
 * <li>Create an enumeration that implements EntityType, and associated that with the DefaultTacsitController.</li>
 * <li>Create a Class that implements {@link org.omg.tacsit.ui.EntityViewport}, and add it to the
 * DefaultTacsitController's ViewportManager.</li>
 * <p>
 * For testing purposes, {@link org.omg.tacsit.ui.TacsitFrame} exercises many of the default features of the
 * standard implementation.
 * @author Matthew Child
 */
public class DefaultTacsitController implements TacsitController, PropertyListenable
{
  /**
   * The property event fired when the entity repository is changed.
   */
  public static final String PROPERTY_ENTITY_REPOSITORY = "entityRepository";
  
  /**
   * The property event fired when the selection methodology is changed.
   */
  public static final String PROPERTY_SELECTION_METHODOLOGY = "selectionMethodology";
  
  /**
   * The property event fired when the projections are changed.
   */
  public static final String PROPERTY_PROJECTIONS = "projections";
  
  /**
   * The property event fired when the entity types are changed.
   */
  public static final String PROPERTY_ENTITY_TYPES = "entityTypes";
  
  private EntityRepository entityRepository;
  private EntityViewportManager viewportManager;
    
  private Collection<Projection> projections;
  private Collection<EntityType> entityTypes;
  
  private PropertyChangeSupport changeSupport;

  /**
   * Creates a new instance.
   */
  public DefaultTacsitController()
  {
    viewportManager = new EntityViewportManager();
    projections = Collections.emptyList();
    entityTypes = Collections.emptyList();
    changeSupport = new PropertyChangeSupport(this);
  }

  /**
   * Sets the EntityRepository used by this TacsitController.  All viewports managed by this TacsitController's
   * ViewportManager will have the same data stored in this EntityRepository.
   * @param entityRepository The entity repository that contains all of the entities that fall under the purview of
   * this TacsitController.
   */
  public void setEntityRepository(EntityRepository entityRepository)
  {
    if(this.entityRepository != entityRepository)
    {
      EntityRepository oldRepository = this.entityRepository;
      this.entityRepository = entityRepository;
      viewportManager.setEntityRepository(entityRepository);
      changeSupport.firePropertyChange(PROPERTY_ENTITY_REPOSITORY, oldRepository, this.entityRepository);
    }
  }
  
  /**
   * Gets the EntityRepository used by this TacsitController.  All viewports managed by this TacsitController's
   * ViewportManager will have the same data stored in this EntityRepository.
   * @return The entity repository that contains all of the entities that fall under the purview of
   * this TacsitController.
   */
  public EntityRepository getEntityRepository()
  {
    return viewportManager.getEntityRepository();
  }

  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    changeSupport.removePropertyChangeListener(propertyName, listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    changeSupport.removePropertyChangeListener(listener);
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    changeSupport.addPropertyChangeListener(propertyName, listener);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    changeSupport.addPropertyChangeListener(listener);
  }

  @Override
  public ViewportManager getViewportManager()
  {
    return viewportManager;
  }

  /**
   * Sets the SelectionMethodology used by the TacsitController.  This is used for linking or de-linking the selections
   * in the Viewports under the ViewportManager's control.
   * @param selectionMethodology The new SelectionMethodology to use.
   */
  public void setSelectionMethodology(SelectionMethodology selectionMethodology)
  {
    SelectionMethodology oldMethodology = viewportManager.getSelectionMethodology();
    if(oldMethodology != selectionMethodology)
    {
      viewportManager.setSelectionMethodology(selectionMethodology);
      changeSupport.firePropertyChange(PROPERTY_SELECTION_METHODOLOGY, oldMethodology, selectionMethodology);
    }
  }

  @Override
  public SelectionMethodology getSelectionMethodology()
  {
    return viewportManager.getSelectionMethodology();
  }
  

  @Override
  public SelectionManager getSelectionManager(String viewportName)
  {
    return viewportManager.getSelectionManager(viewportName);
  }
  
  /**
   * Sets the Projection options that are supported by Viewports which this TacsitController maintains.
   * <p>
   * A common implementation mechanism is to create a standalone enumeration class which implements Projection.
   * @param projections A collection of supported Projections.
   */
  public void setProjections(Collection<Projection> projections)
  {
    Collection<EntityType> oldProjections = new ArrayList(this.projections);
    this.projections = CollectionUtils.copyToUnmodifiableCollection(projections);
    changeSupport.firePropertyChange(PROPERTY_PROJECTIONS, oldProjections, this.projections);
  }

  public Collection<Projection> getProjections()
  {
    return projections;
  }
  
  /**
   * Sets the EntityTypes that can exist in the EntityRepository for this TacsitController.
   * <p>
   * A common implementation mechanism is to create a standalone enumeration class which implements EntityType.
   * @param entityTypes A collection of supported EntityTypes.
   */
  public void setEntityTypes(Collection<EntityType> entityTypes)
  {
    Collection<EntityType> oldTypes = this.entityTypes;
    this.entityTypes = CollectionUtils.copyToUnmodifiableCollection(entityTypes);
    changeSupport.firePropertyChange(PROPERTY_ENTITY_TYPES, oldTypes, this.entityTypes);
  }

  public Collection<EntityType> getEntityTypes()
  {
    return entityTypes;
  }
  
  /**
   * Sets the QueryManager used by this TacsitController.  This implicitly sets the TacsitController's
   * EntityRepository.
   * @param repository The repository that will serve as the QueryManager.
   */
  public void setQueryManager(EntityRepository repository)
  {
    setEntityRepository(repository);
  }

  public QueryManager getQueryManager()
  {
    return entityRepository;
  }  
}
