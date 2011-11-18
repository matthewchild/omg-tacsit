/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 15, 2011
 */
package org.omg.tacsit.ui.resources;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 * Provides static access to the icons used in the as decoration for the application.
 * @author MattChild
 */
public class DecorationIcons
{
  /**
   * A 16x16 icon of a world 
   */
  public static final ImageIcon WORLD_16 =
      getIcon("icons/world_16.png");
  /**
   * A 24x24 icon of a world 
   */
  public static final ImageIcon WORLD_24 =
      getIcon("icons/world_24.png");
  /**
   * A 32x32 icon of a world 
   */
  public static final ImageIcon WORLD_32 =
      getIcon("icons/world_32.png");
  
  /**
   * A 128x128 icon of a world with a zoom picture.
   */
  public static final ImageIcon WORLD_ZOOM_128 =
      getIcon("icons/world_zoom_128.png");
  
  private static ImageIcon getIcon(String path)
  {
    try
    {
      URL loc = DecorationIcons.class.getResource(path);
      return new ImageIcon(loc);
    }
    catch (Exception ex)
    {
      Logger.getLogger(DecorationIcons.class.getName()).log(Level.WARNING, "Path " + path + " not found.", ex);
      ImageIcon icon = new ImageIcon();
      return icon;
    }
  }
}
