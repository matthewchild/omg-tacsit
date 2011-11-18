/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 16, 2011
 */
package org.omg.tacsit.worldwind.common.layers;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.omg.tacsit.worldwind.common.util.ImageUtils;

/**
 * A collection of utilities that operate on WWIcons
 * @author Matthew Child
 */
public abstract class WWIconUtils
{
  /**
   * Transforms an image file on the classpath to an image source that can be used for display with a WWicon.
   * @param fullClasspathImageLocation The fully qualified path in the classpath of the image file.
   * @return A buffered image that can be used as a WWIcon's image source.
   */
  public static BufferedImage toImageSource(String fullClasspathImageLocation)
  {
    URL imageFileURL = Object.class.getResource(fullClasspathImageLocation);
    ImageIcon missingImageIcon = new ImageIcon(imageFileURL);
    BufferedImage transformedImage = toImageSource(missingImageIcon);
    return transformedImage;
  }
  
  /**
   * Transforms an image to an image source that can be used for display with a WWIcon.
   * @param image The image to transform to an image source.
   * @return A buffered image that can be used as a WWIcon's image source.
   */
  public static BufferedImage toImageSource(Image image)
  {
    BufferedImage imageSource;

    if (image instanceof BufferedImage)
    {
      imageSource = (BufferedImage) image;
    }
    else
    {
      imageSource = ImageUtils.createBufferedImage(image);
    }
    return imageSource;
  }
  
  /**
   * Transforms an icon to an image source that can be used for display with a WWIcon.
   * @param icon The icon to transform to an image source
   * @return A buffered image that can be used as a WWIcon's image source.
   */
  public static BufferedImage toImageSource(Icon icon)
  {
    BufferedImage imageSource;
    
    // By checking the case of ImageIcon, we may opt out of having to repaint the image,
    // if the underlying image is already a buffered image.
    if(icon instanceof ImageIcon)
    {
      Image image = ((ImageIcon)icon).getImage();
      imageSource = toImageSource(image);
    }
    else
    {
      imageSource = ImageUtils.createBufferedImage(icon);
    }
    return imageSource;
  }
}
