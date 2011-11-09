/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 5, 2011
 */
package org.omg.tacsit.common.math;

/**
 * A position on the surface of a globe.  This position is described by its latitude and longitude.  Latitude
 * represents the distance from the equator of the globe (typically expressed as -90 <= latitude <= 90).  Longitude
 * represents the distance from the prime meridian of the globe (typically expressed as -180 < longitude <= 180).
 * <p>
 * A LatLon can be constructed in multiple methods.  It can be constructed directly with an Angle for Latitude and
 * Longitude, or by the static factory methods fromDegrees(latitude, longitude) and fromRadians(latitude, longitude).
 * <p>
 * Instances of LatLon are immutable.  This class is based on <code>LatLon</code> from Nasa's Worldwind, originally
 * authored by Tom Gaskins.
 * 
 * @author Matthew Child
 */
public class SurfacePosition
{

  /**
   * The surface position at (latitude = 0, longitude = 0).
   */
  public static final SurfacePosition ZERO = SurfacePosition.fromDegrees(0, 0);    
  
  private Angle latitude;
  private Angle longitude;

  /**
   * Constructs a new SurfacePosition, with the specified latitude and longitude.
   * @param latitude The distance from the equator of the globe.
   * @param longitude The distance from the prime meridian.
   */
  public SurfacePosition(Angle latitude, Angle longitude)
  {
    if (latitude == null)
    {
      throw new IllegalArgumentException("latitude may not be null.");
    }
    if (longitude == null)
    {
      throw new IllegalArgumentException("longitude may not be null.");
    }
    this.latitude = latitude;
    this.longitude = longitude;
  }

  /**
   * Constructs a new SurfacePosition with the specified latitude and longitude degree values.
   * @param latitudeDegrees The degrees latitude (distance from the equator of the globe).
   * @param longitudeDegrees The degrees longitude (distance from the prime meridian of the globe).
   * @return A new instance with the specified latitude and longitude degree values.
   */
  public static SurfacePosition fromDegrees(double latitudeDegrees, double longitudeDegrees)
  {
    return new SurfacePosition(Angle.fromDegrees(latitudeDegrees), Angle.fromDegrees(longitudeDegrees));
  }

  /**
   * Constructs a new SurfacePosition with the specified latitude and longitude radian values.
   * @param latitudeRadians The radians latitude (distance from the equator of the globe).
   * @param longitudeRadians The radians longitude (distance from the prime meridian of the globe).
   * @return A new instance with the specified latitude and longitude radian values.
   */
  public static SurfacePosition fromRadians(double latitudeRadians, double longitudeRadians)
  {
    return new SurfacePosition(Angle.fromRadians(latitudeRadians), Angle.fromRadians(longitudeRadians));
  }

  /**
   * Gets the latitude of this SurfacePosition.
   * @return The latitude Angle.
   */
  public Angle getLatitude()
  {
    return latitude;
  }

  /**
   * Gets the longitude of this SurfacePosition.
   * @return The longitude Angle.
   */
  public Angle getLongitude()
  {
    return longitude;
  }

  /**
   * Checks to see whether or not this surface position is normal.  The position is considered normal if both of
   * the following are true:
   * <p>
   * <li>-90 <= latitude <= 90</li>
   * <li>-180 <= longitude <= 180</li>
   * 
   * @return true if the angle is normal, or false otherwise.
   */
  public boolean isNormal()
  {
    return isNormalizedDegreesLatitude(latitude.getDegrees()) && isNormalizedDegreesLongitude(longitude.getDegrees());
  }

  /**
   * Returns a SurfacePosition that is normalized.  A normalized position will guarantee the following:
   * <p>
   * <li>-90 <= latitude <= 90</li>
   * <li>-180 <= longitude <= 180</li>
   * <p>
   * If the SurfacePosition is already normal, this object will be returned.  If the SurfacePosition is not normal,
   * a new SurfacePosition will be returned with the normalized version of the latitude and longitude.  In either
   * case, this SurfacePosition will never be modified.
   * @return A normalized version of this SurfacePosition.
   */
  public SurfacePosition normalized()
  {
    if (isNormal())
    {
      return this;
    }
    else
    {
      Angle normalizedLatitude = normalizedLatitude();
      Angle normalizedLongitude = normalizedLongitude();
      return new SurfacePosition(normalizedLatitude, normalizedLongitude);
    }
  }

  /**
   * Gets the Latitude Angle, normalized such that -90 <= latitude <= 90.
   * <p>
   * If the Latitude is already normalized, it will be returned.  If it is not normal, a new Angle representing
   * the normalized Latitude will be returned.  In either case, neither the SurfacePosition nor its Latitude are 
   * modified.
   * @return A normalized version of the Latitude Angle.
   */
  public Angle normalizedLatitude()
  {
    double degreesLatitude = latitude.getDegrees();
    if (isNormalizedDegreesLatitude(degreesLatitude))
    {
      return latitude;
    }
    else
    {
      double normalizedLatitude = normalizedDegreesLatitude(degreesLatitude);
      return Angle.fromDegrees(normalizedLatitude);
    }
  }

  /**
   * Gets the Longitude Angle, normalized such that -180 <= longitude <= 180.
   * <p>
   * If the Longitude is already normalized, it will be returned.  If it is not normal, a new Angle representing
   * the normalized Longitude will be returned.  In either case, neither the SurfacePosition nor its Longitude are 
   * modified.
   * @return A normalized version of the Longitude Angle.
   */
  public Angle normalizedLongitude()
  {
    double degreesLongitude = longitude.getDegrees();
    if(isNormalizedDegreesLongitude(degreesLongitude))
    {
      return longitude;
    }
    else
    {
      double normalizedLongitude = normalizedDegreesLongitude(degreesLongitude);
      return Angle.fromDegrees(normalizedLongitude);
    }
  }

  private static boolean isNormalizedDegreesLatitude(double degrees)
  {
    return (-90 <= degrees) && (degrees <= 90);
  }

  private static double normalizedDegreesLatitude(double degrees)
  {
    double lat = degrees % 180;
    return lat > 90 ? 180 - lat : lat < -90 ? -180 - lat : lat;
  }

  private static boolean isNormalizedDegreesLongitude(double degrees)
  {
    return (-180 <= degrees) && (degrees <= 180);
  }

  private static double normalizedDegreesLongitude(double degrees)
  {
    double lon = degrees % 360;
    return lon > 180 ? lon - 360 : lon < -180 ? 360 + lon : lon;
  }

  @Override
  public String toString()
  {
    return "SurfacePosition{" + "latitude=" + latitude + ", longitude=" + longitude + '}';
  }
}
