/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 16, 2011
 */
package org.omg.tacsit.common.util;

/**
 * A standard development pattern that allows the repeated creation of new objects.
 * @author Matthew Child
 * @param <T> The type of object the data factory creates.
 */
public interface Factory<T>
{
  /**
   * Returns a new object.
   * @return newObject A newly created data object.
   */
  public T createObject();
}
