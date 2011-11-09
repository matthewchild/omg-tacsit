/**
 * @(#) Rectangle.java
 */

package org.omg.tacsit.geometry;

public interface Rectangle extends Centered, Geometry
{
	/**
	 * @return the height of the rectangular shape in meters
	 */
	double getHeight( );
	
	/**
	 * @return the azimuth of the y axis of the rectangle shape, where zero is
	 * north in radians.
	 */
	double getOrientation( );
	
	/**
	 * @return the width of the rectangle shape in meters
	 */
	double getWidth( );
	
	
}
