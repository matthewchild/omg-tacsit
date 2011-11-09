/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 28, 2011
 */
package org.omg.tacsit.worldwind.geometry;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.util.WWMath;
import java.util.Iterator;
import org.omg.tacsit.geometry.GeodeticPosition;

/**
 * An implementation that provides standard implementations of the WWSurfaceGeometry interface.
 * @author Matthew Child
 */
public abstract class AbstractSurfaceGeometry implements WWSurfaceGeometry
{
  
  private boolean contains(LatLon location)
  {
    boolean contains = false;
    Iterable<? extends LatLon> boundingLocations = getBoundingLocations();
    if(boundingLocations != null)
    {
      contains = WWMath.isLocationInside(location, boundingLocations);
    }
    return contains;
  }

  public boolean contains(GeodeticPosition point)
  {
    Position wwPosition = WWGeodeticPosition.toWWPosition(point);
    return contains(wwPosition);
  }
  
  private boolean containsAll(Iterable<? extends LatLon> theirs)
  {
    boolean containsAll = false;
    if(theirs != null)
    {
      Iterator<? extends LatLon> theirLocations = theirs.iterator();
      if(theirLocations.hasNext())
      {
        containsAll = true;
        while (theirLocations.hasNext())
        {
          LatLon theirLocation = theirLocations.next();
          if(!contains(theirLocation))
          {
            containsAll = false;
            break;
          }
        }
      }
    }
    return containsAll;
  }
  
  private boolean areAnyOfMyLocationsInside(Iterable<? extends LatLon> theirs)
  {
    boolean myLocationsInside = false;
    if(theirs != null)
    {
      Iterable<? extends LatLon> boundingLocations = getBoundingLocations();
      if(boundingLocations != null)
      {    
        Iterator<? extends LatLon> myLocations = boundingLocations.iterator();
        while (myLocations.hasNext())
        {
          LatLon myLocation = myLocations.next();
          if(WWMath.isLocationInside(myLocation, theirs))
          {
            myLocationsInside = true;
            break;
          }
        }
      }
    }
    return myLocationsInside;
  }
  
  public boolean contains(WWSurfaceGeometry geometry)
  {
    boolean contains = false;
    if(geometry != null)
    {
      Iterable<? extends LatLon> otherBoundingLocations = geometry.getBoundingLocations();
      
      boolean containsAllTheirs = containsAll(otherBoundingLocations);
      boolean anyOfMyLocaitonsInside = areAnyOfMyLocationsInside(otherBoundingLocations);
      
      contains = containsAllTheirs && !anyOfMyLocaitonsInside;
    }
    
    return contains;
  }
  
  private boolean boundingBoxesIntersect(WWSurfaceGeometry geometry)
  {
    boolean intersects = false;
    if(geometry != null)
    {
      Iterable<? extends LatLon> theirBoundingLocations = geometry.getBoundingLocations();
      Iterable<? extends LatLon> myBoundingLocations = getBoundingLocations();
      if((theirBoundingLocations != null) && (myBoundingLocations != null))
      {
        Sector theirBoundingSector = Sector.boundingSector(theirBoundingLocations);
        Sector myBoundingSector = Sector.boundingSector(myBoundingLocations);
        intersects = myBoundingSector.intersects(theirBoundingSector);
      }
    }
    return intersects;
  }
  

  public boolean intersects(WWSurfaceGeometry geometry)
  {
    boolean intersects = boundingBoxesIntersect(geometry);
    return intersects;
  }
}
