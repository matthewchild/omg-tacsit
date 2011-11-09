/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 24, 2011
 */
package org.omg.tacsit.geometry;

import java.util.List;

/**
 * A Collection of utility functions which operate on Geometry objects defined by the Tacsit spec.
 * @author Matthew Child
 */
public abstract class GeometryUtils
{

  private static final double MAX_DOUBLE = Double.MAX_VALUE;
  private static final double MIN_DOUBLE = -1 * Double.MAX_VALUE;

  /**
   * Returns a normalized point (between -90 to 90 latitude, -180 to 180 longitude) which is the minimum latitude,
   * minimum longitude, and minimum altitude of any point contained in the List.  As such, the value may be a
   * combination of latitude, longitude, and altitude from 3 different points (if each point contained a minimum value
   * for only 1 axis).
   * <p>
   * If the list is empty or null, null is returned.
   * @param positions A List of GeodeticPositions to find the minimum values of.
   * @return A position which has the minimum latitude, longitude, and altitude of any point in the List.
   */
  public static GeodeticPosition getMinimumPosition(List<GeodeticPosition> positions)
  {
    GeodeticPosition minimumPosition = null;
    if ((positions != null) && !positions.isEmpty())
    {
      double minimumLatitudeDegrees = MAX_DOUBLE;
      double minimumLongitudeDegrees = MAX_DOUBLE;
      double minimumAltitudeMeters = MAX_DOUBLE;
      for (GeodeticPosition geodeticPosition : positions)
      {
        DefaultGeodeticPosition position = DefaultGeodeticPosition.toDefaultGeodeticPosition(geodeticPosition);
        DefaultGeodeticPosition normalizedPosition = position.normalized();
        
        double normalizedLatitudeDegrees = normalizedPosition.getLatitudeAngle().getDegrees();
        minimumLatitudeDegrees = Math.min(normalizedLatitudeDegrees, minimumLatitudeDegrees);

        double normalizedLongitudeDegrees = normalizedPosition.getLongitudeAngle().getDegrees();
        minimumLongitudeDegrees = Math.min(normalizedLongitudeDegrees, minimumLongitudeDegrees);

        double altitudeMeters = geodeticPosition.getAltitude();
        minimumAltitudeMeters = Math.min(altitudeMeters, minimumAltitudeMeters);
      }
      minimumPosition = DefaultGeodeticPosition.fromDegrees(minimumLatitudeDegrees, minimumLongitudeDegrees,
                                                            minimumAltitudeMeters);
    }
    return minimumPosition;
  }

  /**
   * Returns a normalized point (between -90 to 90 latitude, -180 to 180 longitude) which is the maximum latitude,
   * maximum longitude, and maximum altitude of any point contained in the List.  As such, the value may be a
   * combination of latitude, longitude, and altitude from 3 different points (if each point contained a maximum value
   * for only 1 axis).
   * @param positions A List of GeodeticPositions to find the maximum values of.
   * @return A position which has the maximum latitude, longitude, and altitude of any point in the List.
   */
  public static GeodeticPosition getMaximumPosition(List<GeodeticPosition> positions)
  {
    GeodeticPosition maximumPosition = null;
    if ((positions != null) && !positions.isEmpty())
    {
      double maximumLatitudeDegrees = MIN_DOUBLE;
      double maximumLongitudeDegrees = MIN_DOUBLE;
      double maximumAltitudeMeters = MIN_DOUBLE;
      for (GeodeticPosition geodeticPosition : positions)
      {
        DefaultGeodeticPosition position = DefaultGeodeticPosition.toDefaultGeodeticPosition(geodeticPosition);
        DefaultGeodeticPosition normalizedPosition = position.normalized();
        
        double normalizedLatitude = normalizedPosition.getLatitudeAngle().getDegrees();
        maximumLatitudeDegrees = Math.max(normalizedLatitude, maximumLatitudeDegrees);

        double normalizedLongitude = normalizedPosition.getLongitudeAngle().getDegrees();
        maximumLongitudeDegrees = Math.max(normalizedLongitude, maximumLongitudeDegrees);

        double altitudeMeters = geodeticPosition.getAltitude();
        maximumAltitudeMeters = Math.max(altitudeMeters, maximumAltitudeMeters);
      }
      maximumPosition = DefaultGeodeticPosition.fromDegrees(maximumLatitudeDegrees, maximumLongitudeDegrees,
                                                            maximumAltitudeMeters);
    }
    return maximumPosition;
  }
}
