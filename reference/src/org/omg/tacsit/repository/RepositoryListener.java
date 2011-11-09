/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 17, 2011
 */
package org.omg.tacsit.repository;

/**
 * A listener which wants to receive notification about changes to the state of an EntityRepository.
 * 
 * @see org.omg.tacsit.repository.EntityRepository
 * @author Matthew Child
 */
public interface RepositoryListener
{
  /**
   * Notifies the listener that entities were added to the EntityRepository being listened to.
   * @param event The event describing the addition change.
   */
  public void entitiesAdded(RepositoryChangeEvent event);
  
  /**
   * Notifies the listener that entities were removed from the EntityRepository being listened to.
   * @param event The event describing the removal change.
   */
  public void entitiesRemoved(RepositoryChangeEvent event);
  
  /**
   * Notifies the listener that entities were updated in the EntityRepository being listened to.
   * @param event The event describing the update change.
   */
  public void entitiesUpdated(RepositoryChangeEvent event);
  
  /**
   * Notifies the listener that all the entities were cleared out of the EntityRepository.
   * @param event The event which describes the clear change.
   */
  public void entitiesCleared(RepositoryChangeEvent event);
}
