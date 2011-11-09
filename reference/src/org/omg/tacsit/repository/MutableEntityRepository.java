/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 14, 2011
 */
package org.omg.tacsit.repository;

import java.util.Collection;
import org.omg.tacsit.controller.Entity;

/**
 * An EntityRepository that can be modified.
 * <p>
 * This provides a simpler version of the core Java Collection interface to make it simpler to implement.
 * @param <E> The type of Entity that's valid in this repository.
 * @author Matthew Child
 */
public interface MutableEntityRepository<E extends Entity> extends EntityRepository<E>
{
  /**
   * Removes an Entity from the repository.
   * @param entity The entity to remove from the repository.
   * @return true if the entity was removed, false otherwise.
   */
  public boolean remove(E entity);
  
  /**
   * Removes all the Entities contained in the Collection from the EntityRepository.
   * @param c The Collection of Entities that should be removed from the repository.
   * @return true if the repository was modified, false otherwise.
   */
  public boolean removeAll(Collection<? extends E> c);
  
  /**
   * Adds an Entity to the repository.
   * @param entity The entity to add to the repository.
   * @return true if the entity was added, false otherwise.
   */
  public boolean add(E entity);
  
  /**
   * Adds all the Entities contained in the Collection to the EntityRepository.
   * @param entities The Collection of entities to add to the repository.
   * @return true if the repository was modified, false otherwise.
   */
  public boolean addAll(Collection<? extends E> entities);
  
  /**
   * Clears the repository of all contained entities.
   */
  public void clear();
}
