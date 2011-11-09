/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 23, 2011
 */
package org.omg.tacsit.worldwind.common.layers;

/**
 * A set of images used to represent items.
 * <p>
 * This allows an application to easily swap in new representations as sets of images.  For example, an application
 * might allow you to change rendering of to tracks between an NTDSImagePack and a MilStd2525bImagePack.
 * @param <T> The type of object that this image pack defines images for.
 * @author Matthew Child
 */
public interface ImagePack<T>
{
  /**
   * Gets the image source for a given item.  The image source can be used as an image source for a WWIcon.
   * @param object The item to get the image for.
   * @return The image source that should be used to represent an item.
   */
  public Object getImageSource(T object);  
}
