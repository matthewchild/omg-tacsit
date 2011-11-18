/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 1, 2009
 */

package org.omg.tacsit.common.util;

import java.util.List;

/**
 * A collection of utility functions to use on core java Strings.
 * @author Matthew Child
 */
public abstract class StringUtils
{


  /**
   * Concatenates a list of elements into a more printable version of a string,
   * using a delimiter to separate the toString() results of each of the elements.
   * @param elements The list of elements to concatenate.
   * @return A string representing all the elements in the list.
   */
  public static String concatenateElements(List elements)
  {
    return concatenateElements(elements, ", ");
  }

  /**
   * Concatenates a list of elements into a more printable version of a string,
   * using a delimiter parameter to separate the toString() results of each of
   * the elements.
   * @param elements The list of elements to concatenate.
   * @param delimiter The delimiter to use for separation of elements.
   * @return A string representing all the elements in the list.
   */
  public static String concatenateElements(List elements, String delimiter)
  {
    String value = null;
    
    if(elements != null)
    {
      StringBuilder builder = concatenateElements(new StringBuilder(), elements, delimiter);
      value = builder.toString();
    }
    return value;
  }


  /**
   * Concatenates a list of elements into a more printable version of a string,
   * using a delimiter parameter to separate the toString() results of each of
   * the elements.
   * @param builder The element that builder that should be used to put the elements into.  May not be null.
   * @param elements The list of elements to concatenate.
   * @param delimiter The delimiter to use for separation of elements.
   * @return A string representing all the elements in the list.
   */
  public static StringBuilder concatenateElements(StringBuilder builder, List elements, String delimiter)
  {
    if(elements != null)
    {
      for (int i = 0; i < elements.size(); i++)
      {
        Object element = elements.get(i);
        if(i != 0)
        {
          builder.append(delimiter);
        }
        builder.append(String.valueOf(element));
      }
    }
    return builder;
  }
}
