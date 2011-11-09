/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 15, 2011
 */
package org.omg.tacsit.worldwind.geometry;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Globe;
import org.omg.tacsit.common.math.Distance;
import org.omg.tacsit.geometry.GeodeticPosition;

/**
 * An implementation of GeodeticPosition that allows easy conversion to Worldwind's native location format.
 * @author Matthew Child
 */
public class WWGeodeticPosition implements GeodeticPosition
{
  private static final double MIN_LATITUDE_DEGREES = -90.0;
  private static final double MAX_LATITUDE_DEGREES = 90;
  
  /**
   * The surface position at zero degrees latitude, longitude, and 0 meters from the surface.
   */
  public static final WWGeodeticPosition ZERO = fromRadians(0, 0, 0);
  
  private Position position;
  
  /**
   * Creates a new instance.
   * @param latitude The latitude angle, as an offset from the equator.
   * @param longitude The longitude angle, as an offset from the prime meridian.
   * @param altitudeMeters The distance (in meters) from the surface of the earth.
   */
  public WWGeodeticPosition(Angle latitude, Angle longitude, double altitudeMeters)
  {
    this.position = new Position(latitude, longitude, altitudeMeters);
  }
  
  /**
   * Creates a new position.
   * @param position The Worldwind native location.
   */
  public WWGeodeticPosition(Position position)
  {
    if (position == null)
    {
      throw new IllegalArgumentException("position may not be null");
    }      
    this.position = position;
  }
  
  /**
   * Creates a new WWGeodeticPosition from latitude and longitude measured in angular degree.
   * @param latitudeDegrees The degrees latitude, as an offset from the equator.
   * @param longitudeDegrees The degrees longitude, as an offset from the prime meridian.
   * @param altitudeMeters The distance (in meters) from the surface of the earth.
   * @return A new geodetic position with the relevant position values.
   */
  public static WWGeodeticPosition fromDegrees(double latitudeDegrees, double longitudeDegrees, double altitudeMeters)
  {
    Position degreePosition = Position.fromDegrees(latitudeDegrees, longitudeDegrees, altitudeMeters);
    return new WWGeodeticPosition(degreePosition);
  }
  
  /**
   * Creates a new WWGeodeticPosition from latitude and longitude measured in angular radians.
   * @param latitudeRadians The radians latitude, as an offset from the equator.
   * @param longitudeRadians The radians longitude, as an offset from the prime meridian.
   * @param altitudeMeters The distance (in meters) from the surface of the earth.
   * @return A new geodetic position with the relevant position values.
   */
  public static WWGeodeticPosition fromRadians(double latitudeRadians, double longitudeRadians, double altitudeMeters)
  {
    Position radianPosition = Position.fromRadians(latitudeRadians, longitudeRadians, altitudeMeters);
    return new WWGeodeticPosition(radianPosition);    
  }
  
  /**
   * Converts a Tacsit specified GeodeticPosition to a WWGeodeticPosition.  If the GeodeticPosition already is a 
   * WWGeodeticPosition, it will be returned and no new object will be created.
   * <p>
   * This is a convenience operation for Tacsit components which must interact directly with Worldwind.
   * @param geodeticPosition The position to convert to a WWGeodeticPosition.
   * @return A position that represents the same latitude, longitude, and altitude.
   */
  public static WWGeodeticPosition toWWGeodeticPosition(GeodeticPosition geodeticPosition)
  {
    if(geodeticPosition == null)
    {
      return null;
    }
    
    WWGeodeticPosition wwGeoPosition;    
    if(geodeticPosition instanceof WWGeodeticPosition)
    {
      wwGeoPosition = (WWGeodeticPosition)geodeticPosition;
    }
    else
    {
      Position wwPosition = convertPosition(geodeticPosition);
      wwGeoPosition = new WWGeodeticPosition(wwPosition);
    }
    return wwGeoPosition;
  }
  
  private static Position convertPosition(GeodeticPosition geodeticPosition)
  {
    double latitudeRadians = geodeticPosition.getLatitude();
    double longitudeRadians = geodeticPosition.getLongitude();
    double altitudeMeters = geodeticPosition.getAltitude();

    Position wwPosition = Position.fromRadians(latitudeRadians, longitudeRadians, altitudeMeters);
    return wwPosition;
  }
  
  /**
   * Converts a Tacsit position to Worldwind's native location format.
   * @param geodeticPosition The position to convert.
   * @return A Position that has the same values for latitude, longitude, and altitude.
   */
  public static Position toWWPosition(GeodeticPosition geodeticPosition)
  {
    Position wwPosition = null;
    
    if(geodeticPosition instanceof WWGeodeticPosition)
    {
      wwPosition = ((WWGeodeticPosition)geodeticPosition).getPosition();
    }
    else if(geodeticPosition != null)
    {
      wwPosition = convertPosition(geodeticPosition);
    }
    return wwPosition;
  }

  public double getLongitude()
  {
    return getLongitudeAngle().getRadians();
  }
  
  /**
   * Gets the Worldwind native angle format for longitude.
   * @return The Worldwind native angle format for longitude.
   */
  public Angle getLongitudeAngle()
  {
    return position.getLongitude();
  }

  public double getLatitude()
  {
    return getLatitudeAngle().getRadians();
  }
  
  /**
   * Gets the Worldwind native angle format for latitude.
   * @return The Worldwind native angle format for latitude.
   */
  public Angle getLatitudeAngle()
  {
    return position.getLatitude();
  }

  public double getAltitude()
  {
    return position.getAltitude();
  }
  
  /**
   * Gets the position in the Worldwind native format.
   * @return The position represented in Worldwind's native format.
   */
  public Position getPosition()
  {
    return position;
  }

  public boolean contains(GeodeticPosition point)
  {
    return ((this.getLatitude() == point.getLatitude())   &&
            (this.getLongitude() == point.getLongitude()) &&
            (this.getAltitude() == point.getAltitude()));
  }
  
  /**
   * Creates a new position with the same longitude and altitude, but a new latitude.  
   * <p>
   * This WWGeodeticPosition will not be modified.
   * @param newLatitude The latitude for the new position.
   * @return A new position with the same longitude and altitude, and a new latitude.
   */
  public WWGeodeticPosition newLatitude(Angle newLatitude)
  {
    WWGeodeticPosition newPosition = new WWGeodeticPosition(newLatitude, getLongitudeAngle(), getAltitude());
    return newPosition;
  }

  /**
   * Creates a new position with the same latitude and altitude, but a new longitude.
   * <p>
   * This WWGeodeticPosition will not be modified.
   * @param newLongitude The longitude for the new position.
   * @return A new position with the same latitude and altitude, but a new longitude.
   */
  public WWGeodeticPosition newLongitude(Angle newLongitude)
  {
    WWGeodeticPosition newPosition = new WWGeodeticPosition(getLatitudeAngle(), newLongitude, getAltitude());
    return newPosition;
  }

  /**
   * Creates a new position with the same latitude and longitude, but a new altitude.
   * <p>
   * This WWGeodeticPosition will not be modified.
   * @param newAltitude The altitude for the new position.
   * @return A new position with the same latitude and longitude, but a different altitude.
   */
  public WWGeodeticPosition newAltitude(double newAltitude)
  {
    WWGeodeticPosition newPosition = new WWGeodeticPosition(getLatitudeAngle(), getLongitudeAngle(), newAltitude);
    return newPosition;
  }
  
  private Distance getLatitudeCircleRadius(Distance ellipsoidRadiusAtPosition)
  {
    // Get the interior angle of the triangle which is formed by a line crossing through both poles, and
    // the radius line from the center of the ellipsoid at the position.  
    // This value is identical to the value which describes the latitude.
    Angle interiorAngle = position.getLatitude();
    
    // If we're at either pole, the latitude circle is a point; it has no distance.
    Angle normalizedAngle = interiorAngle.normalizedLatitude();
    if(normalizedAngle.equals(Angle.POS90) || normalizedAngle.equals(Angle.NEG90))
    {
      return Distance.ZERO;
    }
    else
    {    
      //  Since cos(angle) = Adjacent / Hypotenuse, we solve for Adjacent.  Adjecent = Hypotenuse * sin(angle)
      Distance latitudeCircleRadius = ellipsoidRadiusAtPosition.multipliedBy(interiorAngle.cos());
      return latitudeCircleRadius;      
    }
  }
  
  private Angle getLongitude(Distance longitudeOffset, Distance ellipsoidRadiusAtPosition)
  {
    if(ellipsoidRadiusAtPosition.isZero())
    {
      throw new IllegalArgumentException("radiusAtPosition may not be zero.");
    }
    
    // When moving east/west along a fixed latitude coordinate, the radius of 1 degree is a longer distance in meters 
    // can very greatly.  At the equator, 1 degree west is much larger than it would be near one of the poles.
    
    // Find the radius of the latitude circle at the given point.
    Distance latitudeRingRadius = getLatitudeCircleRadius(ellipsoidRadiusAtPosition);
    
    if(!latitudeRingRadius.isZero())
    {
      double longitudeRadians = longitudeOffset.dividedBy(latitudeRingRadius);
      return Angle.fromRadiansLongitude(longitudeRadians);    
    }
    else
    {
      return position.getLongitude();
    }
  }
  
  private Angle getLatitude(Distance latitudeOffset, Distance ellipsoidRadiusAtPosition)
  {
    if(ellipsoidRadiusAtPosition.isZero())
    {
      throw new IllegalArgumentException("sphereRadiusAtPosition may not be zero.");
    }
    
    double latitudeRadians = latitudeOffset.dividedBy(ellipsoidRadiusAtPosition);
    return Angle.fromRadians(latitudeRadians);    
  }
  
  /**
   * Creates a new point that is west of this point by the given distance.  The latitude and altitude coordinates 
   * of the new point will be identical to that of this point.  This point's position is not modified.
   * <p>
   * NOTE:  Each latitude ring (a full circle that goes from pole to pole described by CONSTANT latitude,
   * x longitude) does not go through the center of the globe; it "slices" the globe like a loaf of bread.  
   * Thus, the distance traveled in angular degrees is greater the closer to one of the "heels" of the loaf of bread.  
   * For example, traveling 1 degree west at the equator is roughly 111.2km.  However, traveling 1 degree west at 
   * 45 degrees latitude is only 78.63km.
   * <p>
   * Distance Value Source:  http://www.movable-type.co.uk/scripts/latlong.html
   * 
   * @param westDistance The distance to travel west of this point.
   * @param globe The globe to travel along the surface of.
   * @return A new point that is west of this point by the specified distance
   */
  public WWGeodeticPosition west(Distance westDistance, Globe globe)
  {
    double globeRadiusAtPositionMeters = globe.getRadiusAt(position);
    Distance globeRadiusAtPosition = Distance.fromMeters(globeRadiusAtPositionMeters);
    
    Angle longitudeWest = getLongitude(westDistance, globeRadiusAtPosition);
    Angle currentLongitude = position.getLongitude();
    Angle newLongitude = currentLongitude.subtract(longitudeWest);
    
    return newLongitude(newLongitude);
  }
  
  /**
   * Creates a new point that is east of this point by the given distance.  The latitude and altitude coordinates 
   * of the new point will be identical to that of this point.  This point's position is not modified.
   * <p>
   * NOTE:  Each latitude ring (a full circle that goes from pole to pole described by CONSTANT latitude,
   * x longitude) does not go through the center of the globe; it "slices" the globe like a loaf of bread.  
   * Thus, the distance traveled in angular degrees is greater the closer to one of the "heels" of the loaf of bread.  
   * For example, traveling 1 degree east at the equator is roughly 111.2km.  However, traveling 1 degree east 
   * at 45 degrees latitude is only 78.63km.
   * <p>
   * Distance Value Source:  http://www.movable-type.co.uk/scripts/latlong.html
   * 
   * @param eastDistance The distance to travel east of this point.
   * @param globe The globe to travel along the surface of.
   * @return A new point that is east of this point by the specified distance
   */
  public WWGeodeticPosition east(Distance eastDistance, Globe globe)
  {
    double globeRadiusAtPositionMeters = globe.getRadiusAt(position);
    Distance globeRadiusAtPosition = Distance.fromMeters(globeRadiusAtPositionMeters);
    
    Angle longitudeEast = getLongitude(eastDistance, globeRadiusAtPosition);
    Angle currentLongitude = position.getLongitude();
    Angle newLongitude = currentLongitude.add(longitudeEast);
    
    return newLongitude(newLongitude);
  }
  
  /**
   * Creates a new point that is south of this point by the given distance.  The longitude and altitude coordinates 
   * of the new point will be identical to that of this point.  This point's position is not modified.
   * <p> 
   * Moving south at the south pole (or south a distance amount that would take you past the south pole) will not 
   * wrap around the globe; instead, it will always end at the south pole.
   * <p>
   * NOTE:  Each longitude ring (a half circle that goes from pole to pole described by (x) latitude,
   * CONSTANT longitude) is part of a great circle; it goes through the center of the globe.  Thus, the distance 
   * traveled is always the same angular degrees, regardless of the latitude and longitude of this point.
   * 
   * @param southDistance The distance to travel south of this point.
   * @param globe The globe to travel along the surface of.
   * @return A new point that is south of this point by the specified distance
   */
  public WWGeodeticPosition south(Distance southDistance, Globe globe)
  {
    double globeRadiusAtPositionMeters = globe.getRadiusAt(position);
    Distance globeRadiusAtPosition = Distance.fromMeters(globeRadiusAtPositionMeters);
    
    Angle latitudeSouth = getLatitude(southDistance, globeRadiusAtPosition);
    Angle currentLatitude = position.getLatitude();
    Angle newLatitude = currentLatitude.subtract(latitudeSouth);
    if(MIN_LATITUDE_DEGREES <= newLatitude.getDegrees())
    {    
      return newLatitude(newLatitude);
    }
    else
    {
      return newLatitude(Angle.fromDegrees(MIN_LATITUDE_DEGREES));      
    }
  }
  
  /**
   * Creates a new point that is north of this point by the given distance.  The longitude and altitude coordinates 
   * of the new point will be identical to that of this point.  This point's position is not modified.  
   * <p>
   * Moving north at the north pole (or north a distance amount that would take you past the north pole) will not 
   * wrap around the globe; instead, it will always end at the north pole.
   * <p>
   * NOTE:  Each longitude ring (a half circle that goes from pole to pole described by (x) latitude,
   * CONSTANT longitude) is part of a great circle; it goes through the center of the globe.  Thus, the distance 
   * traveled is always the same angular degrees, regardless of the latitude and longitude of this point.
   * 
   * @param northDistance The distance to travel north of this point.
   * @param globe The globe to travel along the surface of.
   * @return A new point that is north of this point by the specified distance
   */
  public WWGeodeticPosition north(Distance northDistance, Globe globe)
  {
    double globeRadiusAtPositionMeters = globe.getRadiusAt(position);
    Distance globeRadiusAtPosition = Distance.fromMeters(globeRadiusAtPositionMeters);
    
    Angle latitudeNorth = getLatitude(northDistance, globeRadiusAtPosition);
    Angle currentLatitude = position.getLatitude();
    Angle newLatitude = currentLatitude.add(latitudeNorth);
    
    if(newLatitude.getDegrees() <= MAX_LATITUDE_DEGREES)
    {    
      return newLatitude(newLatitude);
    }
    else
    {
      return newLatitude(Angle.fromDegrees(MAX_LATITUDE_DEGREES));      
    }
  }

  @Override
  public String toString()
  {
    return "WWGeodeticPosition{" + position + '}';
  }
}
