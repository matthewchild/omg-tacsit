/**
 * @(#) ViewEyeProperties.java
 */
package org.omg.tacsit.controller;

import org.omg.tacsit.geometry.GeodeticPosition;

/**
 * ViewEyeProperties groups the attributes of a ViewPort that have an impact on
 * the position where Entities are displayed in the Viewport.
 */
public class ViewEyeProperties
{

  /**
   * The GeoCenter of the Viewport.
   *
   * The geoCenter of the Viewport is the Geoposition that is displayed in the
   * center of the Viewport (i.e., on a Viewport of w pixels wide and h pixels
   * high, it is displayed on pixel (w/2, h/2) ).
   */
  private GeodeticPosition geoCenter;
  /**
   * The rangescale of the Viewport in meters.
   *
   * The rangescale is the Distance from the center of the viewport to the
   * nearest viewport edge.
   */
  private double rangeScale;
  /**
   * The orientation of the Viewport in radians.
   *
   * The orientation is applied on the Viewport as a rotation of the Viewport
   * in clockwise direction around the Viewport's center. See the definition
   * of Angle for precision guidance.
   */
  private double orientation;
  /**
   * The projection of the Viewport.
   *
   * The projection object here is assumed to be one of the options provided
   * by the high level TACSIT Controller method "getProjections()". This
   * standard does not address any further details of how to handle
   * projections.
   */
  private Projection projection;

  /**
   * @return
   */
  public GeodeticPosition getGeoCenter()
  {
    return geoCenter;
  }

  /**
   * @return meters
   */
  public double getRangeScale()
  {
    return rangeScale;
  }

  /**
   * @return radians
   */
  public double getOrientation()
  {
    return orientation;
  }

  /**
   * @return
   */
  public Projection getProjection()
  {
    return projection;
  }

  public void setProjection(Projection projection)
  {
    this.projection = projection;
  }

  public void setGeoCenter(GeodeticPosition geoCenter)
  {
    this.geoCenter = geoCenter;
  }

  public void setRangeScale(double rangeScale)
  {
    this.rangeScale = rangeScale;
  }

  public void setOrientation(double orientation)
  {
    this.orientation = orientation;
  }
}
