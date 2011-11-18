/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 23, 2011
 */
package org.omg.tacsit.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.query.EntityQuery;
import org.omg.tacsit.common.util.CollectionUtils;

/**
 * An EntityRepository that notifies listeners of additions to, or removal from, the Repository.
 * <p>
 * Since there is no explicit interface on Entity to observe changes, this EntityRepository does
 * not broadcast change events when entities are modified.  See {@link org.omg.tacsit.repository.PolledEntityRepository}
 * for an implementation which has this capability.
 * 
 * @param <E> The type of Entity valid in this repository.
 * @author Matthew Child
 */
public class DefaultEntityRepository<E extends Entity> implements MutableEntityRepository<E>
{
  private Collection<E> entities;
  
  private RepositoryChangeSupport changeSupport;

  /**
   * Creates a new instance.
   */
  public DefaultEntityRepository()
  {
    entities = new ArrayList();
    changeSupport = new RepositoryChangeSupport();
  }

  public void clear()
  {
    if(!entities.isEmpty())
    {
      entities.clear();
      changeSupport.fireEntitiesCleared();
    }
  }  

  /**
   * Performs a remove from the entity repository.  No listeners are notified.
   * @param entity The entity being removed.
   * @return true if the entity was removed, or false otherwise.
   */
  protected boolean doRemove(E entity)
  {
    return entities.remove(entity);
  }
  
  public boolean remove(E entity)
  {
    boolean removed = doRemove(entity);
    if(removed)
    {
      changeSupport.fireEntityRemoved(entity);
    }
    return removed;
  }
  
  public boolean removeAll(Collection<? extends E> c)
  {
    Collection<Entity> removedEntities = new ArrayList();
    Iterator<? extends E> itor = c.iterator();
    while (itor.hasNext())
    {
      E entity = itor.next();
      boolean removed = doRemove(entity);
      if(removed)
      {
        removedEntities.add(entity);
      }
    }
    changeSupport.fireEntitiesRemoved(removedEntities);
    return !removedEntities.isEmpty();
  }
  
  /**
   * Notifies all RepositoryListeners that a group of entities were updated.
   * @param entities The group of entities that were updated.
   */
  protected void fireEntitiesUpdated(Collection<E> entities)
  {
    changeSupport.fireEntitiesUpdated(entities);
  }

  /**
   * Performs the insert of the entity into the repository.  No listeners are notified.
   * @param entity The entity to insert into the repository.
   * @return true if the entity was inserted, false otherwise.
   */
  protected boolean doAdd(E entity)
  {
    return entities.add(entity);
  }

  public boolean add(E entity)
  {
    boolean added = doAdd(entity);
    if(added)
    {
      changeSupport.fireEntityAdded(entity);
    }
    return added;
  }

  public boolean addAll(Collection<? extends E> entities)
  {
    Collection<E> addedEntities = new ArrayList();
    for (E entity : entities)
    {
      boolean added = doAdd(entity);
      if(added)
      {
        addedEntities.add(entity);
      }
    }
    changeSupport.fireEntitiesAdded(entities);
    return !addedEntities.isEmpty();
  }
  
  public void addRepositoryListener(RepositoryListener listener)
  {
    changeSupport.addRepositoryListener(listener);
  }
  
  public void removeRepositoryListener(RepositoryListener listener)
  {
    changeSupport.removeRepositoryListener(listener);
  }
  
  public Iterator<E> getEntities()
  {
    Collection<E> unmodifiableEntities = Collections.unmodifiableCollection(entities);
    return unmodifiableEntities.iterator();
  }

  public Collection<Entity> submitEntityQuery(EntityQuery query)
  {
    EntityQueryIterator queryIterator = new EntityQueryIterator(query);
    Collection<Entity> satisfiedEntities = CollectionUtils.toList(queryIterator);
    return satisfiedEntities;
  }
  
  private class EntityQueryIterator implements Iterator<Entity>
  {
    private EntityQuery entityQuery;
    private Iterator<E> delegate;
    
    private Entity next;

    public EntityQueryIterator(EntityQuery entityQuery)
    {
      this.entityQuery = entityQuery;
      this.delegate = entities.iterator();
    }

    public boolean hasNext()
    {
      return getNext() != null;
    }

    public Entity next()
    {
      Entity nextEntity = getNext();
      if(nextEntity == null)
      {
        throw new NoSuchElementException("No more elements that match the query " + entityQuery + " exist.");
      }
      next = null;
      return nextEntity;
    }
    
    private boolean satisfiesQuery(Entity entity)
    {
      boolean satisfies = true;
      if(entityQuery != null)
      {
        satisfies = entityQuery.satifies(entity);
      }
      return satisfies;
    }
    
    private Entity getNext()
    {
      if(next == null)
      {
        while(delegate.hasNext())
        {
          Entity entity = delegate.next();
          if(satisfiesQuery(entity))
          {
            next = entity;
            break;
          }
        }
      }
      return next;
    }

    public void remove()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
