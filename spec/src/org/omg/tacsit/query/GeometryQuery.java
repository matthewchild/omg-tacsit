/**
 * @(#) GeometryQuery.java
 */

package org.omg.tacsit.query;

import org.omg.tacsit.geometry.Geometry;

/**
 * The EntityTypeQuery is used to determine whether or not a given Entity has a
 * geometrical relationship with a particular Entity.
 */
public interface GeometryQuery extends EntityQuery
{
	/**
	 * Returns the Geometry against which an Entity will be evaluated.
	 * 
	 * @return 
	 */
	Geometry getGeometry( );
	
	
}
