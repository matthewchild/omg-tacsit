/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 26, 2011
 */
package org.omg.tacsit.entity;

import java.util.Collection;
import java.util.Iterator;
import org.omg.tacsit.controller.Entity;

/**
 * A Collection of utilities which operate on Entities.
 * @author Matthew Child
 */
public abstract class EntityUtils
{
  /**
   * Checks to see if a collection contains a particular entity.
   * <p>
   * The entities will be compared using the method from the <code>Entity</code> interface's 
   * <code>equals(Entity)</code> method.
   * @param entityCollection The collection of entities to search.
   * @param entity The entity being searched for in the collection.
   * @return true if the entity is in the collection, false otherwise.
   */
  public static boolean containsEntity(Collection<Entity> entityCollection, Entity entity)
  {
    boolean containsEntity = false;
    if(entity == null)
    {
      containsEntity = entityCollection.contains(null);
    }
    else if(entityCollection != null)
    {
      Iterator<Entity> entitySetIterator = entityCollection.iterator();
      while (entitySetIterator.hasNext())
      {
        Entity entitySetElement = entitySetIterator.next();
        if(entity.equals(entitySetElement))
        {
          containsEntity = true;
          break;
        }
      }
    }
    return containsEntity;
  }
  
  /**
   * Checks to see if two collections contain the same set of entities.
   * <p>
   * The entities will be compared using the method from the <code>Entity</code> interface's 
   * <code>equals(Entity)</code> method.
   * @param entitySet1 The first collection of entities.
   * @param entitySet2 The second collection of entities.
   * @return true if entitySet1 and entitySet2 both contain the same entities, or false otherwise.
   */
  public static boolean areEntityCollectionsEqual(Collection<Entity> entitySet1, Collection<Entity> entitySet2)
  {
    boolean entityCollectionsEqual = false;
    // This handles both the null case, and the case where the two
    // collections are the same object.
    if((entitySet1 == entitySet2))
    {
      entityCollectionsEqual = true;
    }
    else
    {
      if((entitySet1 != null) && (entitySet2 != null) && (entitySet1.size() == entitySet2.size()))
      {
        entityCollectionsEqual = true;
        Iterator<Entity> entitySet1Iterator = entitySet1.iterator();
        while (entitySet1Iterator.hasNext())
        {
          Entity entity1 = entitySet1Iterator.next();
          if(!containsEntity(entitySet2, entity1))
          {
            entityCollectionsEqual = false;
            break;
          }
        }
      }
    }
    return entityCollectionsEqual;
  }
}
