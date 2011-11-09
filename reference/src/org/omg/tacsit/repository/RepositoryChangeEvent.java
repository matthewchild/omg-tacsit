/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 17, 2011
 */
package org.omg.tacsit.repository;

import java.util.Collection;
import java.util.Collections;
import org.omg.tacsit.common.util.CollectionUtils;
import org.omg.tacsit.controller.Entity;

/**
 * A Event signifying changes to the entities contained in an EntityRepository.
 * @author Matthew Child
 */
public class RepositoryChangeEvent
{
  /**
   * The type of change an event represents.
   */
  public static enum Type
  {

    /**
     * Indicates Entities were added to the repository.
     */
    ADDED, 
    /**
     * Indicates Entities were removed from the repository.
     */
    REMOVED, 
    /**
     * Indicates all Entities were cleared from the repository.
     */
    CLEARED, 
    /**
     * Indicates all Entities were updated in the repository.
     */
    UPDATED
  };
  
  private Type type;
  private Collection<Entity> entities;
  
  /**
   * Creates a new instance.
   * @param type The type of change
   * @param entity The entity that was changed (added, removed, or updated) in the repository.
   */
  public RepositoryChangeEvent(Type type, Entity entity)
  {
    this.type = type;
    this.entities = Collections.singleton(entity);
  }
  
  /**
   * Creates a new instance.
   * @param type The type of change
   * @param entities The entities that were changed (added, removed, or updated) in the repository.
   */
  public RepositoryChangeEvent(Type type, Collection<? extends Entity> entities)
  {
    this.type = type;
    // We need to copy the list, to ensure that external modifications to the passed list do not affect the state
    // of the event.
    this.entities = (Collection<Entity>)CollectionUtils.copyToUnmodifiableCollection(entities);
  }

  /**
   * Gets the type of change.
   * @return The event type.
   */
  public Type getType()
  {
    return type;
  }

  /**
   * Gets the entities that were changed (added, removed, or updated).
   * @return An unmodifiable Collection of entities that were changed.
   */
  public Collection<Entity> getEntities()
  {
    return entities;
  }
}
