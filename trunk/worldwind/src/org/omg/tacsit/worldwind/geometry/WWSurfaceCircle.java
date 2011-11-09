/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 8, 2011
 */
package org.omg.tacsit.worldwind.geometry;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.render.SurfaceCircle;
import java.beans.PropertyChangeListener;
import org.omg.tacsit.geometry.Circle;
import org.omg.tacsit.geometry.GeodeticPosition;

/**
 * An implementation of the Tacsit specification's <code>Circle</code> interface.
 * 
 * @author Matthew Child
 */
public class WWSurfaceCircle extends AbstractSurfaceGeometry implements Circle, WWSurfaceGeometry
{
  private SurfaceCircle circleDelegate;
  
  private WWGeodeticPosition geodeticCenter;
  
  private Globe globe;

  /**
   * Creates a new instance.
   * @param globe The globe to use to define the bounding locations.
   */
  public WWSurfaceCircle(Globe globe)
  {
    // Passing in a globe is necessary here to satisfy the interface contract of entity's contains method.
    if(globe == null)
    {
      throw new IllegalArgumentException("Globe may not be null.");
    }
    this.globe = globe;
    this.circleDelegate = new SurfaceCircle();
    this.geodeticCenter = WWGeodeticPosition.ZERO;
    circleDelegate.setCenter(geodeticCenter.getPosition());
  }
  
  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    circleDelegate.addPropertyChangeListener(listener);
  }

  @Override
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    circleDelegate.addPropertyChangeListener(propertyName, listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    circleDelegate.removePropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    circleDelegate.removePropertyChangeListener(propertyName, listener);
  }
  
  /**
   * Sets the center of this circle.
   * @param center The new circle center.
   */
  public void setCenter(WWGeodeticPosition center)
  {
    WWGeodeticPosition oldCenter = this.geodeticCenter;
    this.geodeticCenter = center;
    circleDelegate.setCenter(this.geodeticCenter.getPosition());
    circleDelegate.firePropertyChange("center", oldCenter, this.geodeticCenter);
  }
  
  /**
   * Sets the center of this circle.
   * @param center The circle center in worldwind's native location format.
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
    return circleDelegate.getLocations(globe);
  }
  
  /**
   * Sets the radius of the circle.
   * @param radiusMeters The radius of the circle, in meters.
   */
  public void setRadius(double radiusMeters)
  {
    double oldRadius = circleDelegate.getRadius();
    circleDelegate.setRadius(radiusMeters);
    circleDelegate.firePropertyChange("radius", oldRadius, radiusMeters);
  }

  public double getRadius()
  {
    return circleDelegate.getRadius();
  }

  public GeodeticPosition getReferencePosition()
  {
    return getCenter();
  }
}
