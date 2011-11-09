/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 13, 2011
 */
package org.omg.tacsit.worldwind.ui.viewport;

import gov.nasa.worldwind.geom.Angle;
import org.omg.tacsit.controller.ViewEyeProperties;

/**
 * A ViewEyeProperties that has additional display attributes for a Worldwind ViewEye.
 * @author Matthew Child
 */
public class WorldwindViewEyeProperties extends ViewEyeProperties
{
  private Angle pitch;

  /**
   * Gets the pitch of the view eye.
   * @return The view eye's pitch.
   */
  public Angle getPitch()
  {
    return pitch;
  }

  /**
   * Sets the pitch of the view eye.
   * @param pitch The view eye's pitch.
   */
  public void setPitch(Angle pitch)
  {
    this.pitch = pitch;
  }
  
  /**
   * Sets the orientation of the view eye.
   * @param orientation The new orientation of the view eye.
   */
  public void setOrientation(Angle orientation)
  {
    double orientationAsRadians = 0.0d;
    if(orientation != null)
    {
      orientationAsRadians = orientation.getRadians();
      
    }
    setOrientation(orientationAsRadians);
  }
}
