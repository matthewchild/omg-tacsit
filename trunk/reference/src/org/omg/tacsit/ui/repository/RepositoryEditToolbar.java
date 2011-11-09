/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 13, 2011
 */
package org.omg.tacsit.ui.repository;

import java.util.Collection;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToolBar;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.repository.MutableEntityRepository;
import org.omg.tacsit.common.util.Factory;

/**
 * A Toolbar containing actions to manipulate an MutableEntityRepository.
 * @author Matthew Child
 */
public class RepositoryEditToolbar extends JToolBar
{  
  private AddEntityAction addAction;
  
  private RemoveEntityAction deleteAction;
  private MutableEntityRepository repository;

  /**
   * Creates a new instance.
   */
  public RepositoryEditToolbar()
  {
    super("Edit Repository");
    initGUI();
  }
  
  private void initGUI()
  {
    addAction = new AddEntityAction();
    JButton addButton = new JButton(addAction);
    addButton.setBorder(null);
    add(addButton);
    
    add(Box.createHorizontalStrut(10));
    
    deleteAction = new RemoveEntityAction();
    JButton deleteButton = new JButton(deleteAction);
    deleteButton.setBorder(null);
    add(deleteButton);
  }
  
  /**
   * Gets the repository that this toolbar modifies.
   * @return The repository that's modified.
   */
  public MutableEntityRepository getRepository()
  {
    return this.repository;
  }

  /**
   * Sets the repository that this toolbar modifies.
   * @param repository The repository to modify.
   */
  public void setRepository(MutableEntityRepository repository)
  {
    this.repository = repository;
    addAction.setRepository(repository);
    deleteAction.setRepository(repository);
  }

  /**
   * Sets the entity factory used to create new entities.
   * @param entityFactory The factory that creates new entities.
   */
  public void setEntityFactory(Factory<? extends Entity> entityFactory)
  {
    addAction.setEntityFactory(entityFactory);    
  }
  
  /**
   * Sets the entities that the toolbar acts on.
   * @param entities The entities the toolbar is currently acting on.
   */
  public void setEntitiesToActOn(Collection<Entity> entities)
  {
    deleteAction.setEntitiesToRemove(entities);
  }
  
  /**
   * Gets the entities that the toolbar is acting on.
   * @return The entities the toolbar is currently acting on.
   */
  public Collection<Entity> getEntitiesToActOn()
  {
    return deleteAction.getEntitiesToRemove();
  }
}
