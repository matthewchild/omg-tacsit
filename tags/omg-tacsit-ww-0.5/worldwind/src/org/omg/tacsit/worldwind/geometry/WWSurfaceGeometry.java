/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 8, 2011
 */
package org.omg.tacsit.worldwind.geometry;

import gov.nasa.worldwind.geom.LatLon;
import org.omg.tacsit.common.util.PropertyListenable;
import org.omg.tacsit.geometry.GeodeticPosition;
import org.omg.tacsit.geometry.Geometry;

/**
 * Geometry that exists on the surface of the Globe.  Any altitude values are ignored.
 * @author Matthew Child
 */
public interface WWSurfaceGeometry extends Geometry, PropertyListenable
{
  /**
   * The reference position to use to give a general location of the geometry.
   * @return The reference position of the geometry.
   */
  public GeodeticPosition getReferencePosition();
  
  /**
   * The bounding locations of the surface geometry.
   * @return An Iterable of locations on the surface of a globe that bound this geometry.
   */
  public Iterable<? extends LatLon> getBoundingLocations();
  
  /**
   * Checks to see if another geometry is wholly contained by this geometry.
   * @param geometry The geometry to check containment of.
   * @return true if this geometry completely contains the parameter, or false otherwise.
   */
  public boolean contains(WWSurfaceGeometry geometry);
  
  /**
   * Checks to see if another geometry intersects this geometry.
   * @param geometry The geometry to check for intersection with.
   * @return true if this geometry intersects the parameter, or false otherwise.
   */
  public boolean intersects(WWSurfaceGeometry geometry);
}
