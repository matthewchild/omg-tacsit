/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 29, 2011
 */
package org.omg.tacsit.ui.repository;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.omg.tacsit.common.ui.ConfigurableAction;
import org.omg.tacsit.ui.resources.ActionIcons;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.repository.MutableEntityRepository;
import org.omg.tacsit.common.util.Factory;

/**
 * An Action that adds a new Entity to an EntityReository.
 * @author Matthew Child
 */
public class AddEntityAction extends ConfigurableAction
{
  private Factory<? extends Entity> entityFactory;
  private MutableEntityRepository repository;

  /**
   * Creates a new instance.
   */
  public AddEntityAction()
  {
    super(null, ActionIcons.ADD_24);
    this.putValue(Action.SHORT_DESCRIPTION, "Add Entity");
  }

  /**
   * Gets the factory that is used to create the entities to add.
   * @return The factory that creates entities.
   */
  public Factory<? extends Entity> getEntityFactory()
  {
    return entityFactory;
  }

  /**
   * Sets the factory that's used to create the entities to add.
   * @param entityFactory The factory that creates entities.
   */
  public void setEntityFactory(Factory<? extends Entity> entityFactory)
  {
    this.entityFactory = entityFactory;
    checkEnabledState();
  }

  /**
   * Gets the repository that new entities will be added to.
   * @return The repository to add to.
   */
  public MutableEntityRepository getRepository()
  {
    return repository;
  }

  /**
   * Sets the repository that new entities will be added to.
   * @param repository The repository to add to.
   */
  public void setRepository(MutableEntityRepository repository)
  {
    this.repository = repository;
    checkEnabledState();
  }
  
  @Override
  public boolean isPerformable()
  {
    return ((entityFactory != null) && (repository != null));
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Entity entity = entityFactory.createObject();
    repository.add(entity);
  }
}
