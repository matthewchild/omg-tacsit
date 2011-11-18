/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 17, 2011
 */
package org.omg.tacsit.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A Collection of utility functions which operate on a core Java <code>Collection</code>
 * @author Matthew Child
 */
public abstract class CollectionUtils
{

  /**
   * Iterates over a set of elements and creates a List that contains all of them.  The order of the elements in
   * the List will be the order returned by <code>iterator</code> parameter.
   * @param <T> The type of element iterated over.
   * @param iterator The set of elements to iterate over
   * @return A new Collection that contains all the elements returned by the iterator.
   */
  public static <T> List<T> toList(Iterator<T> iterator)
  {
    List<T> newList = new ArrayList();
    addToCollection(newList, iterator);
    return newList;
  }

  /**
   * Adds the contents of an iterator to an existing Collection.
   * @param <T> The type of elements returned by the iterator.
   * @param collection The receptacle to put the elements into.
   * @param elementsToAdd The elements to add to the Collection.
   */
  public static <T> void addToCollection(Collection<T> collection, Iterator<T> elementsToAdd)
  {
    while (elementsToAdd.hasNext())
    {
      T next = elementsToAdd.next();
      collection.add(next);
    }
  }

  /**
   * Copies the contents of a List to a new, unmodifiable List.  If the List is null, an empty List will be returned.
   * <p>
   * This is especially useful for methods which take List parameters.  Directly assigning such a List to a member
   * variable can allow external classes to modify internal state.  Classes may have different state depending on the
   * size of a List; for example, a button might be disabled if the the List size is zero.  If an external object
   * can change the List, the containing class would have no way have knowing a modification occurred.
   * 
   * @param <T> The type of element in the List.
   * @param list The List to copy.
   * @return A new, unmodifiable List which contains all of the elements of the List parameter.  Will never be null.
   */
  public static <T> List<T> copyToUnmodifiableList(List<T> list)
  {
    if (list != null)
    {
      List<T> listCopy = new ArrayList(list);
      return Collections.unmodifiableList(listCopy);
    }
    else
    {
      return Collections.emptyList();
    }
  }

  /**
   * Copies the contents of a Collection to a new, unmodifiable Collection.  If the Collection is null, an empty 
   * Collection will  be returned.
   * <p>
   * This is especially useful for methods which take Collection parameters.  Directly assigning such a Collection
   * to a member variable can allow external classes to modify internal state.  Classes may have different state 
   * depending on the size of a Collection; for example, a button might be disabled if the the Collection size is zero.  
   * If an external object can change the Collection, the containing class would have no way have knowing a 
   * modification occurred.
   * 
   * @param <T> The type of element in the Collection.
   * @param collection The Collection to copy.
   * @return A new, unmodifiable Collection which contains all of the elements of the List parameter.  Will never be 
   * null.
   */
  public static <T> Collection<T> copyToUnmodifiableCollection(Collection<T> collection)
  {
    if (collection != null)
    {
      Collection<T> collectionCopy = new ArrayList(collection);
      return Collections.unmodifiableCollection(collectionCopy);
    }
    else
    {
      return Collections.emptyList();
    }
  }
}
