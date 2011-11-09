/**
 * @(#) Geometry.java
 */

package org.omg.tacsit.geometry;

import java.io.Serializable;

/**
 * @author jtruss
 */
public interface Geometry extends Serializable, Cloneable
{
	/**
	 * If the Geometry is has an altitude component this method will ignore it
	 * and only test against the ground projection of the shape.
	 * 
	 * @return whether the given point is contained within the shape.
	 */
	boolean contains( GeodeticPosition point );
	
	
}
