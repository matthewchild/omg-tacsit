/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Mar 31, 2011
 */

package org.omg.tacsit.geometry;

import org.omg.tacsit.common.math.Angle;
import org.omg.tacsit.common.math.Distance;
import org.omg.tacsit.common.math.SurfacePosition;

/**
 * A GeodeticPosition that is described using Angles for lat/lon, and a Distance for altitude.
 * <p>
 * DefaultGeodeticPositions are immutable.  However, several helper methods have been defined for creating
 * new DefaultGeodeticPositions that are partial copies of the existing one: newAltitude, newLatitude, and newLongitude.
 * @author Matthew Child
 */
public class DefaultGeodeticPosition implements GeodeticPosition
{
  /**
   * The position representing 0 latitude, 0 longitude, and 0 altitude.
   */
  public static final DefaultGeodeticPosition ZERO = fromRadians(0, 0, 0);
  
  private SurfacePosition surfacePosition;
  private Distance altitude;
  
  /**
   * Creates a new instance.
   * @param latitude The latitude offset (from the equator)
   * @param longitude The longitude offset (from the prime meridian).
   * @param altitude The distance from the surface of the earth.
   */
  public DefaultGeodeticPosition(Angle latitude, Angle longitude, Distance altitude)
  {
    this(new SurfacePosition(latitude, longitude), altitude);
  }
  
  /**
   * Creates a new instance.
   * @param surfacePosition The latitude and longitude of the new position.
   * @param altitude The distance from the surface of the earth.
   */
  public DefaultGeodeticPosition(SurfacePosition surfacePosition, Distance altitude)
  {
    if(surfacePosition == null)
    {
      throw new IllegalArgumentException("surfacePosition may not be null.");
    }
    this.surfacePosition = surfacePosition;
    this.altitude = altitude;
  }
  
  /**
   * Factory method that creates a new position based on degree angular measurements of latitude and longitude.
   * @param latitudeDegrees The latitude offset from the equator (in degrees)
   * @param longitudeDegrees The longitude offset from the prime meridian (in degrees)
   * @param altitudeMeters The altitude offset from the surface of the earth (in meters).
   * @return A new DefaultGeodeticPosition with the given latitude, longitude, and altitude.
   */
  public static DefaultGeodeticPosition fromDegrees(double latitudeDegrees, double longitudeDegrees, double altitudeMeters)
  {
    SurfacePosition surfacePosition = SurfacePosition.fromDegrees(latitudeDegrees, longitudeDegrees);
    Distance altitude = Distance.fromMeters(altitudeMeters);
    return new DefaultGeodeticPosition(surfacePosition, altitude);    
  }
  
  /**
   * Factory method that creates a new position based on radian angular measurements of latitude and longitude.
   * @param latitudeRadians The latitude offset from the equator (in radians)
   * @param longitudeRadians The longitude offset from the equator (in radians)
   * @param altitudeMeters The altitude offset from the surface of the earth (in meters).
   * @return A new DefaultGeodeticPosition with the given latitude, longitude, and altitude.
   */
  public static DefaultGeodeticPosition fromRadians(double latitudeRadians, double longitudeRadians, double altitudeMeters)
  {
    SurfacePosition surfacePosition = SurfacePosition.fromRadians(latitudeRadians, longitudeRadians);
    Distance altitude = Distance.fromMeters(altitudeMeters);
    return new DefaultGeodeticPosition(surfacePosition, altitude);    
  }
  
  /**
   * Converts a Tacsit standard compliant GeodeticPosition to a DefaultGeodeticPosition.  This can be useful to
   * take advantage of some of the additional functionality that DefaultGeodeticPosition provides, like 
   * optional normalization or unit-insensitive measurement storage.
   * 
   * @param geodeticPosition The position to convert to a DefaultGeodeticPosition
   * @return A DefaultGeodeticPosition which contains the same latitude, longitude, and altitude as the parameter.
   */
  public static DefaultGeodeticPosition toDefaultGeodeticPosition(GeodeticPosition geodeticPosition)
  {
    if(geodeticPosition == null)
    {
      return null;
    }
    
    DefaultGeodeticPosition defaultGeodeticPosition;    
    if(geodeticPosition instanceof DefaultGeodeticPosition)
    {
      defaultGeodeticPosition = (DefaultGeodeticPosition)geodeticPosition;
    }
    else
    {
      defaultGeodeticPosition = DefaultGeodeticPosition.fromRadians(geodeticPosition.getLatitude(), geodeticPosition.getLongitude(), geodeticPosition.getAltitude());
    }
    return defaultGeodeticPosition;
  }
  
  /**
   * Returns a DefaultGeodeticPosition that represents a normalized version of the Angles represented by this
   * DefaultGeodeticPosition.  A normalized latitude represents -90 <= latitude <= 90.  A normalized longitude
   * represents -180 <= longitude <= 180.
   * <p>
   * This DefaultGeodeticPosition is not modified.
   * @return A DefaultGeodeticPosition in which the Latitude and Longitude Angles have been normalized.
   */
  public DefaultGeodeticPosition normalized()
  {
    if(surfacePosition.isNormal())
    {
      return this;
    }
    else
    {
      SurfacePosition normalizedPosition = this.surfacePosition.normalized();
      return new DefaultGeodeticPosition(normalizedPosition, this.altitude);
    }
  }
  
  /**
   * Gets the point on the surface this position represents (Latitude & Longitude).
   * @return The SurfacePosition of this GeodeticPosition
   */
  public SurfacePosition getSurfacePosition()
  {
    return this.surfacePosition;
  }
  
  /**
   * Gets the Angle measurement of Latitude from the equator.
   * @return The latitude angle.
   */
  public Angle getLatitudeAngle()
  {
    return this.surfacePosition.getLatitude();
  }

  public double getLatitude()
  {
    return getLatitudeAngle().getRadians();
  }

  /**
   * Gets the Angle measurement of Longitude from the equator.
   * @return The longitude angle.
   */
  public Angle getLongitudeAngle()
  {
    return this.surfacePosition.getLongitude();
  }
  
  public double getLongitude()
  {
    return getLongitudeAngle().getRadians();
  }

  public double getAltitude()
  {
    return getAltitudeDistance().getMeters();
  }
  
  /**
   * Gets the distance measurement of this point from the surface of the earth.
   * @return The distance from the surface of the earth.
   */
  public Distance getAltitudeDistance()
  {
    return this.altitude;
  }
  
  /**
   * Creates a new DefaultGeodeticPosition that has the same Longitude and Altitude, but with a different Latitude.
   * @param newLatitude The Latitude of the new position.
   * @return A new DefaultGeodeticPosition with the same Longitude and Altitude, with a new Latitude.
   */
  public DefaultGeodeticPosition newLatitude(Angle newLatitude)
  {
    DefaultGeodeticPosition newPosition = new DefaultGeodeticPosition(newLatitude, getLongitudeAngle(), getAltitudeDistance());
    return newPosition;
  }

  /**
   * Creates a new DefaultGeodeticPosition that has the same Latitude and Altitude, but with a different Longitude.
   * @param newLongitude The Longitude of the new position.
   * @return A new DefaultGeodeticPosition with the same Latitude and Altitude, with a new Longitude.
   */
  public DefaultGeodeticPosition newLongitude(Angle newLongitude)
  {
    DefaultGeodeticPosition newPosition = new DefaultGeodeticPosition(getLatitudeAngle(), newLongitude, getAltitudeDistance());
    return newPosition;
  }

  /**
   * Creates a new DefaultGeodeticPosition that has the same Latitude and Longitude, but with a different altitude.
   * @param newAltitude The Altitude of the new position.
   * @return A new DefaultGeodeticPosition with the same Latitude and Longitude, with a new Altitude.
   */
  public DefaultGeodeticPosition newAltitude(Distance newAltitude)
  {
    DefaultGeodeticPosition newPosition = new DefaultGeodeticPosition(surfacePosition, newAltitude);
    return newPosition;
  }

  public boolean contains(GeodeticPosition point)
  {
    return ((this.getLatitude() == point.getLatitude())   &&
            (this.getLongitude() == point.getLongitude()) &&
            (this.getAltitude() == point.getAltitude()));
  }

  @Override
  public String toString()
  {
    return "DefaultGeodeticPosition{" + "location=" + surfacePosition + ", altitude=" + altitude + '}';
  }
}
