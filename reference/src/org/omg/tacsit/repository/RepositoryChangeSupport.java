/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 17, 2011
 */
package org.omg.tacsit.repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.repository.RepositoryChangeEvent.Type;

/**
 * A helper class for implementing the EntityRepository interface.   Client classes should declare a
 * RepositoryChangeSupport member variable, and delegate Add/Remove listener methods to it.
 * @author Matthew Child
 */
public class RepositoryChangeSupport
{ 
  private Set<RepositoryListener> repositoryListeners;

  /**
   * Creates a new instance.
   */
  public RepositoryChangeSupport()
  {
    repositoryListeners = new HashSet();
  }
  
  /**
   * Notifies all registered listeners that the repository has been cleared.
   */
  public void fireEntitiesCleared()
  {
    if(!repositoryListeners.isEmpty())
    {
      RepositoryChangeEvent event = new RepositoryChangeEvent(Type.CLEARED, (Entity)null);
      for (RepositoryListener repositoryListener : repositoryListeners)
      {
        repositoryListener.entitiesCleared(event);
      }
    }
  }  
  
  /**
   * Notifies all registered listeners that an entity was added.
   * @param addedEntity The entity that was added.
   */
  public void fireEntityAdded(Entity addedEntity)
  {
    if(!repositoryListeners.isEmpty())
    {
      RepositoryChangeEvent event = new RepositoryChangeEvent(Type.ADDED, addedEntity);
      for (RepositoryListener repositoryListener : repositoryListeners)
      {
        repositoryListener.entitiesAdded(event);
      }
    }
  }  
  
  /**
   * Notifies all registered listeners that several entities were added to the repository.
   * @param addedEntities The entities that were added to the repository.
   */
  public void fireEntitiesAdded(Collection<? extends Entity> addedEntities)
  {
    if(!repositoryListeners.isEmpty() && !addedEntities.isEmpty())
    {
      RepositoryChangeEvent event = new RepositoryChangeEvent(Type.ADDED, addedEntities);
      for (RepositoryListener repositoryListener : repositoryListeners)
      {
        repositoryListener.entitiesAdded(event);
      }
    }
  }
  
  /**
   * Notifies all registered listeners that an entity was removed from the repository.
   * @param removedEntity The entity that was removed from the repository.
   */
  public void fireEntityRemoved(Entity removedEntity)
  {
    if(!repositoryListeners.isEmpty())
    {
      RepositoryChangeEvent event = new RepositoryChangeEvent(Type.REMOVED, removedEntity);
      for (RepositoryListener repositoryListener : repositoryListeners)
      {
        repositoryListener.entitiesRemoved(event);
      }
    }
  }
  
  /**
   * Notifies all registered listeners that several entities were removed from the repository.
   * @param removedEntities The entities that were removed from the repository.
   */
  public void fireEntitiesRemoved(Collection<? extends Entity> removedEntities)
  {
    if(!repositoryListeners.isEmpty() && !removedEntities.isEmpty())
    {
      RepositoryChangeEvent event = new RepositoryChangeEvent(Type.REMOVED, removedEntities);
      for (RepositoryListener repositoryListener : repositoryListeners)
      {
        repositoryListener.entitiesRemoved(event);
      }
    }
  }
  
  /**
   * Notifies all registered listeners that an entity contained in the repository was updated.
   * @param updatedEntity The entity that was updated.
   */
  public void fireEntityUpdated(Entity updatedEntity)
  {
    if(!repositoryListeners.isEmpty())
    {
      RepositoryChangeEvent event = new RepositoryChangeEvent(Type.UPDATED, updatedEntity);
      for (RepositoryListener repositoryListener : repositoryListeners)
      {
        repositoryListener.entitiesUpdated(event);
      }
    }
  }
  
  /**
   * notifies all registered listeners that a several entities contained in the repository were updated.
   * @param updatedEntities The entities that were updated.
   */
  public void fireEntitiesUpdated(Collection<? extends Entity> updatedEntities)
  {
    if(!repositoryListeners.isEmpty() && !updatedEntities.isEmpty())
    {
      RepositoryChangeEvent event = new RepositoryChangeEvent(Type.ADDED, updatedEntities);
      for (RepositoryListener repositoryListener : repositoryListeners)
      {
        repositoryListener.entitiesUpdated(event);
      }
    }
  }
  
  /**
   * Adds a listener that wants to receive notification about changes to the contents of an EntityRepository.
   * @param listener The listener to notify
   */
  public void addRepositoryListener(RepositoryListener listener)
  {
    repositoryListeners.add(listener);
  }
  
  /**
   * Removes a listener that no longer wants to receive notification about changes to the contents of an
   * EntityRepository.
   * @param listener The listener that no longer wants to be notified.
   */
  public void removeRepositoryListener(RepositoryListener listener)
  {
    repositoryListeners.remove(listener);
  }
}
