/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 30, 2011
 */
package org.omg.tacsit.ui.resources;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 * Provides static access to the icons used in the various actions in the application.
 * @author Matthew Child
 */
public class ActionIcons
{ 
  /**
   * A 16x16 icon for an add action.
   */
  public static final ImageIcon ADD_16 =
      getIcon("icons/add_16.png");
  /**
   * A 24x24 icon for an add action.
   */
  public static final ImageIcon ADD_24 =
      getIcon("icons/add_24.png");
  /**
   * A 32x32 icon for an add action.
   */
  public static final ImageIcon ADD_32 =
      getIcon("icons/add_32.png");
  
  /**
   * A 16x16 icon for a clear action.
   */
  public static final ImageIcon CLEAR_16 =
      getIcon("icons/broom_16.png");
  /**
   * A 24x24 icon for a clear action
   */
  public static final ImageIcon CLEAR_24 =
      getIcon("icons/broom_24.png");
  /**
   * A 32x32 icon for a clear action.
   */
  public static final ImageIcon CLEAR_32 =
      getIcon("icons/broom_32.png");
    
  /**
   * A 16x16 icon for a close action.
   */
  public static final ImageIcon CLOSE_16 =
      getIcon("icons/close_b_16.png");  
  /**
   * A 24x24 icon for a close action
   */
  public static final ImageIcon CLOSE_24 =
      getIcon("icons/close_b_24.png");
  /**
   * A 32x32 icon for a close action.
   */
  public static final ImageIcon CLOSE_32 =
      getIcon("icons/close_b_32.png");

  /**
   * A 16x16 icon for a delete action.
   */
  public static final ImageIcon DELETE_16 =
      getIcon("icons/delete_16.png");  
  /**
   * A 24x24 icon for a delete action
   */
  public static final ImageIcon DELETE_24 =
      getIcon("icons/delete_24.png");
  /**
   * A 32x32 icon for a delete action.
   */
  public static final ImageIcon DELETE_32 =
      getIcon("icons/delete_32.png");
  
  /**
   * A 16x16 icon for a refresh action.
   */
  public static final ImageIcon REFRESH_16 =
      getIcon("icons/refresh_16.png");  
  /**
   * A 24x24 icon for a refresh action
   */
  public static final ImageIcon REFRESH_24 =
      getIcon("icons/refresh_24.png");
  /**
   * A 32x32 icon for a refresh action.
   */
  public static final ImageIcon REFRESH_32 =
      getIcon("icons/refresh_32.png");
  
  /**
   * A 16x16 icon for a scaling action.
   */
  public static final ImageIcon SCALE_16 =
      getIcon("icons/scale_16.png");  
  /**
   * A 24x24 icon for a scaling action
   */
  public static final ImageIcon SCALE_24 =
      getIcon("icons/scale_24.png");
  /**
   * A 32x32 icon for a scaling action.
   */
  public static final ImageIcon SCALE_32 =
      getIcon("icons/scale_32.png");
  
  /**
   * A 16x16 icon for a view eye action.
   */
  public static final ImageIcon VIEW_EYE_16 =
      getIcon("icons/visual_warning_16.png");  
  /**
   * A 24x24 icon for a view eye action
   */
  public static final ImageIcon VIEW_EYE_24 =
      getIcon("icons/visual_warning_24.png");
  /**
   * A 32x32 icon for a view eye action.
   */
  public static final ImageIcon VIEW_EYE_32 =
      getIcon("icons/visual_warning_32.png");
  
  
  private static ImageIcon getIcon(String path)
  {
    try
    {
      URL loc = ActionIcons.class.getResource(path);
      return new ImageIcon(loc);
    }
    catch (Exception ex)
    {
      Logger.getLogger(ActionIcons.class.getName()).log(Level.WARNING, "Path " + path + " not found.", ex);
      ImageIcon icon = new ImageIcon();
      return icon;
    }
  }
}
