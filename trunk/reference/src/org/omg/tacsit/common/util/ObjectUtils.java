/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 16, 2011
 */
package org.omg.tacsit.common.util;

/**
 * A collection of utilities that function on basic Objects.
 * @author Matthew Child
 */
public abstract class ObjectUtils
{
  /**
   * Tests the equality of two objects.  This differs from obj1.equals(obj2) method
   * in Object because if obj1 is null, you will get a null pointer exception.  Thus,
   * obj1 and obj2 may be equal (both null), but you can't find that out with a
   * single method call.  This method hides the basic equality check (obj1 == obj2)
   * to allow this case to pass successfully, while maintaining readability for
   * implementers who need null equality.
   * @param obj1 The object to test for equality against obj2
   * @param obj2 The object to test for equality against obj1
   * @return true if the two objects are equal.
   */
  public static boolean areEqual(Object obj1, Object obj2)
  {
    return (obj1 == obj2) ||  // This handles the null equality check.
           ((obj1 != null) && (obj1.equals(obj2)));
  }
}
