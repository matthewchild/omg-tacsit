/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 8, 2011
 */
package org.omg.tacsit.worldwind.geometry;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.render.SurfaceQuad;
import java.beans.PropertyChangeListener;
import org.omg.tacsit.geometry.GeodeticPosition;
import org.omg.tacsit.geometry.Rectangle;

/**
 * An implementation of the Tacsit specification's <code>Rectangle</code> interface.
 * 
 * @author Matthew Child
 */
public class WWSurfaceRectangle extends AbstractSurfaceGeometry implements Rectangle, WWSurfaceGeometry
{
  private SurfaceQuad quadDelegate;
  
  private WWGeodeticPosition geodeticCenter;
  
  private Globe globe;

  /**
   * Creates a new instance.
   * @param globe The globe to use to define the bounding locations.
   */
  public WWSurfaceRectangle(Globe globe)
  {
    // Passing in a globe is necessary here to satisfy the interface contract of entity's contains method.
    if(globe == null)
    {
      throw new IllegalArgumentException("Globe may not be null.");
    }
    this.globe = globe;
    this.quadDelegate = new SurfaceQuad();
    this.geodeticCenter = WWGeodeticPosition.ZERO;
    quadDelegate.setCenter(geodeticCenter.getPosition());
  }
  
  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    quadDelegate.addPropertyChangeListener(listener);
  }

  @Override
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    quadDelegate.addPropertyChangeListener(propertyName, listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    quadDelegate.removePropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    quadDelegate.removePropertyChangeListener(propertyName, listener);
  }

  /**
   * Sets the orientation Angle (as an offset from true north) of the rectangle.
   * @param orientation The orientation that the rectangle should face.
   */
  public void setOrientation(Angle orientation)
  {
    Angle oldHeading = quadDelegate.getHeading();
    quadDelegate.setHeading(orientation);
    quadDelegate.firePropertyChange("heading", oldHeading, orientation);
  }

  public double getOrientation()
  {
    Angle heading = quadDelegate.getHeading();
    return (heading == null) ? 0 : heading.getRadians();
  }

  /**
   * Sets the height of the rectangle.  If the rectangle were facing directly north, the height would affect
   * the minimum and maximum latitude.
   * @param height The new height (in meters).
   */
  public void setHeight(double height)
  {
    double oldHeight = quadDelegate.getHeight();
    quadDelegate.setHeight(height);
    quadDelegate.firePropertyChange("height", oldHeight, height);
  }
  
  public double getHeight()
  {
    return quadDelegate.getHeight();
  }
  
  /**
   * Sets the width of the rectangle.  If the rectangle were facing directly north, the width would affect
   * the minimum and maximum longitude.
   * @param width The new width (in meters).
   */
  public void setWidth(double width)
  {
    double oldWidth = quadDelegate.getWidth();
    quadDelegate.setWidth(width);
    quadDelegate.firePropertyChange("width", oldWidth, width);
  }

  public double getWidth()
  {
    return quadDelegate.getWidth();
  }
  
  /**
   * Sets the center of this rectangle.
   * @param center The new rectangle center.
   */
  public void setCenter(WWGeodeticPosition center)
  {
    WWGeodeticPosition oldCenter = this.geodeticCenter;
    this.geodeticCenter = center;
    quadDelegate.setCenter(this.geodeticCenter.getPosition());
    quadDelegate.firePropertyChange("center", oldCenter, this.geodeticCenter);
  }
  
  /**
   * Sets the center of this rectangle.
   * @param center The rectangle center in worldwind's native location format.
   */
  public void setCenter(Position center)
  {
    setCenter(new WWGeodeticPosition(center));
  }

  public GeodeticPosition getCenter()
  {
    return geodeticCenter;
  }
  
  public Iterable<? extends LatLon> getBoundingLocations()
  {
    return quadDelegate.getLocations(globe);
  }

  public GeodeticPosition getReferencePosition()
  {
    return getCenter();
  }
}
