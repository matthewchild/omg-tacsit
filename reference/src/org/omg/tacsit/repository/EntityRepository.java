/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 23, 2011
 */
package org.omg.tacsit.repository;

import java.util.Iterator;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.query.QueryManager;

/**
 * A collection of Entity objects which can be queried for subsets of Entities.
 * @param <E> The type of Entity that's valid in this repository.
 * @author Matthew Child
 */
public interface EntityRepository<E extends Entity> extends QueryManager
{
  /**
   * Gets an Iterator for all of the Entities stored in this repository.  The repository should not be modified
   * while this Iterator is being iterated.
   * @return An iterator which will return every Entity in the Repository.
   */
  public Iterator<E> getEntities();
  
  /**
   * Adds a listener that wants to know when entities are added, removed, or updated.
   * @param listener The listener that wants to receive notification.
   */
  public void addRepositoryListener(RepositoryListener listener);
  
  /**
   * Removes a listener that no longer cares about additions, removal, or updates.
   * @param listener The listener that no longer wants to receive notification.
   */
  public void removeRepositoryListener(RepositoryListener listener);
}
