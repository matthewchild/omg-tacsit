/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 15, 2011
 */
package org.omg.tacsit.worldwind.common.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.Icon;

/**
 * A collection of utility functions for working with Images.
 * @author Matthew Child
 */
public abstract class ImageUtils
{
  
  /**
   * Creates a new buffered image with the contents of the image parameter.
   * @param image The image to use as the basis for the new image.
   * @return A new buffered image with the same contents that the image has.
   */
  public static BufferedImage createBufferedImage(Image image)
  {
    int width = image.getWidth(null);
    if(width == -1)
    {
      throw new IllegalArgumentException("width of image " + image + " is not yet known.");
    }
    int height = image.getHeight(null);
    if(height == -1)
    {
      throw new IllegalArgumentException("height of image " + image + " is not yet known.");
    }
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics imageGraphics = bufferedImage.getGraphics();
    imageGraphics.drawImage(image, 0, 0, null);
    return bufferedImage;
  }
  
  /**
   * Creates a new buffered image with the contents of the icon parameter.
   * @param icon The icon to use as the basis for the new image.
   * @return A new buffered image with the same contents that the image has.
   */
  public static BufferedImage createBufferedImage(Icon icon)
  {
    int width = icon.getIconWidth();
    int height = icon.getIconHeight();
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics imageGraphics = bufferedImage.getGraphics();
    icon.paintIcon(null, imageGraphics, 0, 0);
    return bufferedImage;
  }
}
