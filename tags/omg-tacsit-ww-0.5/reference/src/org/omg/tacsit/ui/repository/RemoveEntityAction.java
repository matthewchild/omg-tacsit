/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 29, 2011
 */
package org.omg.tacsit.ui.repository;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.swing.Action;
import org.omg.tacsit.common.ui.ConfigurableAction;
import org.omg.tacsit.common.util.CollectionUtils;
import org.omg.tacsit.ui.resources.ActionIcons;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.repository.MutableEntityRepository;

/**
 * An action which removes a set of Entities from a repository.
 * @author Matthew Child
 */
public class RemoveEntityAction extends ConfigurableAction
{
  private Collection<Entity> entitiesToRemove;
  private MutableEntityRepository repository;

  /**
   * Creates a new instance.
   */
  public RemoveEntityAction()
  {
    super(null, ActionIcons.DELETE_24);
    this.putValue(Action.SHORT_DESCRIPTION, "Delete Entity");
    entitiesToRemove = Collections.emptyList();
  }
  
  /**
   * Sets the entities that will be removed from the repository.
   * @param entities The entities to remove.
   */
  public void setEntitiesToRemove(Collection<Entity> entities)
  {
    entitiesToRemove = CollectionUtils.copyToUnmodifiableCollection(entities);
    checkEnabledState();
  }
  
  /**
   * Gets the entities that will be removed from the repository.
   * @return The entities to remove.
   */
  public Collection<Entity> getEntitiesToRemove()
  {
    return entitiesToRemove;
  }

  /**
   * Gets the repository that entities should be removed from.
   * @return The repository to remove entities from.
   */
  public MutableEntityRepository getRepository()
  {
    return repository;
  }

  /**
   * Sets the repository that entities should be removed from
   * @param repository The repository to remove entities from.
   */
  public void setRepository(MutableEntityRepository repository)
  {
    this.repository = repository;
    checkEnabledState();
  }
  
  @Override
  public boolean isPerformable()
  {
    return ((!entitiesToRemove.isEmpty()) && (repository != null));
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Collection<Entity> listCopy = new ArrayList(entitiesToRemove);
    for (Entity entityToRemove : listCopy)
    {
      repository.remove(entityToRemove);      
    }
  }
}
