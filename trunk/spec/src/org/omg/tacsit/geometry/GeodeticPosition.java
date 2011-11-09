/**
 * @(#) GeodeticPosition.java
 */

package org.omg.tacsit.geometry;

/**
 * GeoPosition represent a geographical position, i.e., a position on the earth
 * using the WS84 model.
 */
public interface GeodeticPosition extends Geometry
{
	/**
	 * @return the Longitude in radians.
	 */
	double getLongitude( );
	
	/**
	 * @return the Latitude in radians.
	 */
	double getLatitude( );
	
	/**
	 * @return the altitude in meters or {@link Double.NAN} if the altitude is
	 * not valid.
	 */
	double getAltitude( );
	
	
}
