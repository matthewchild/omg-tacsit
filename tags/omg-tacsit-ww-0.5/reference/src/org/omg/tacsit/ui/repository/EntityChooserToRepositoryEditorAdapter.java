/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 13, 2011
 */
package org.omg.tacsit.ui.repository;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.omg.tacsit.common.ui.ComponentUtils;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.repository.MutableEntityRepository;
import org.omg.tacsit.repository.RepositoryChangeEvent;
import org.omg.tacsit.repository.RepositoryListener;
import org.omg.tacsit.common.util.Factory;
import org.omg.tacsit.ui.entity.EntityChooserUI;

/**
 * Adapter that allows EntityChoosers to meet the RepositoryEditor interface.
 * @author Matthew Child
 */
public class EntityChooserToRepositoryEditorAdapter implements RepositoryEditor
{
  private EntityChooserUI entityChooser;
  private RepositoryListener repositoryListener;  
  private MutableEntityRepository entityRepository;  
  
  private RepositoryEditToolbar editToolbar;
  
  private JPanel tablePanel;
  
  /**
   * Creates a new instance.
   * @param chooser The chooser to adapt to the RepositoryEditor interface.
   * @param entityFactory The Factory used to create new Entities.
   */
  public EntityChooserToRepositoryEditorAdapter(EntityChooserUI chooser, Factory<? extends Entity> entityFactory)
  {
    this.entityChooser = chooser;
    repositoryListener = new EntityRepoListener();
        
    initGUI(entityFactory);
    this.entityChooser.addListSelectionListener(new ComponentSelectionListener());
  }
  
  private void initGUI(Factory<? extends Entity> entityFactory)
  {
    tablePanel = new JPanel(new BorderLayout());
    
    Component component = entityChooser.getComponent();
    updateComponentTitle(component.getName());
    component.addPropertyChangeListener(ComponentUtils.COMPONENT_NAME_PROPERTY, new NameChangeListener());
    tablePanel.add(component, BorderLayout.CENTER);
    
    editToolbar = new RepositoryEditToolbar();
    editToolbar.setFloatable(false);
    editToolbar.setEntityFactory(entityFactory);
    tablePanel.add(editToolbar, BorderLayout.NORTH);
  }
  
  /**
   * Gets the name of the EntityChooser.
   * @return The name of the chooser.
   */
  public String getName()
  {
    return getComponent().getName();
  }
  
  /**
   * Sets the name of the EntityChooser.
   * @param name The new name of the chooser.
   */
  public void setName(String name)
  {
    getComponent().setName(name);
  }

  public Component getComponent()
  {
    return tablePanel;
  }
    
  private void setEditedEntities(Collection<Entity> entities)
  {
    editToolbar.setEntitiesToActOn(entities);
  }
  
  private Collection<Entity> getSelectedEntities()
  {
    return entityChooser.getSelectedEntities();
  }
  
  public MutableEntityRepository getEntityRepository()
  {
    return this.entityRepository;
  }
  
  public void setEntityRepository(MutableEntityRepository entityRepository)
  {
    if(this.entityRepository != null)
    {
      this.entityRepository.removeRepositoryListener(repositoryListener);
    }
    this.entityRepository = entityRepository;
    editToolbar.setRepository(this.entityRepository);
    reloadEntitiesFromRepository();
    if(this.entityRepository != null)
    {
      this.entityRepository.addRepositoryListener(repositoryListener);
    }
  }
  
  private void reloadEntitiesFromRepository()
  {
    clearListContents();
    if(entityRepository != null)
    {
      Iterator<Entity> entityIterator = entityRepository.getEntities();
      addEntities(entityIterator);
    }
  }
  
  private void clearListContents()
  {
    entityChooser.clearEntities();
  }
  
  private void addEntities(Iterator<Entity> entities)
  {
    while (entities.hasNext())
    {
      Entity entity = entities.next();
      
      if(entityChooser.isEntityAllowed(entity))
      {
        entityChooser.addEntity(entity);
      }
    }
  }
  
  private void removeEntities(Iterator<Entity> entities)
  {
    while (entities.hasNext())
    {
      Entity entity = entities.next();
      if(entityChooser.isEntityAllowed(entity))
      {
        entityChooser.removeEntity(entity);
      }
    }
  }
  
  private void updateEntities(Iterator<Entity> entities)
  {
    while (entities.hasNext())
    {
      Entity entity = entities.next();
      if(entityChooser.isEntityAllowed(entity))
      {
        entityChooser.updateEntity(entity);
      }
    }
  }
  
  private void updateComponentTitle(String nameAsString)
  {
    tablePanel.setName(nameAsString);
  }
  
  private class NameChangeListener implements PropertyChangeListener
  {
    public void propertyChange(PropertyChangeEvent evt)
    {
      Object newName = evt.getNewValue();
      String nameAsString = String.valueOf(newName);
      updateComponentTitle(nameAsString);
    }    
  }
  
  private class ComponentSelectionListener implements ListSelectionListener
  {
    private void reloadEditedEntitiesFromTableSelection()
    {
      Collection<Entity> selectedEntities = getSelectedEntities();
      setEditedEntities(selectedEntities);
    }
    
    public void valueChanged(ListSelectionEvent e)
    {
      reloadEditedEntitiesFromTableSelection();
    }
  }    
  
  private class EntityRepoListener implements RepositoryListener
  {
    @Override
    public void entitiesAdded(RepositoryChangeEvent event)
    {
      Collection<Entity> entities = event.getEntities();
      addEntities(entities.iterator());
    }

    @Override
    public void entitiesRemoved(RepositoryChangeEvent event)
    {
      Collection<Entity> entities = event.getEntities();
      removeEntities(entities.iterator());
    }

    @Override
    public void entitiesCleared(RepositoryChangeEvent event)
    {
      clearListContents();
    }

    public void entitiesUpdated(RepositoryChangeEvent event)
    {
      Collection<Entity> entities = event.getEntities();
      updateEntities(entities.iterator());
    }
  }
}
