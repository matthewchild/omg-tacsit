/**
 * @(#) IntersectionQuery.java
 */

package org.omg.tacsit.query;

/**
 * The IntersectionQuery is used to determine if an Entity intersects
 * geometrically with a Geometry. Intersection is determined by using the
 * Entity's Geometry as an argument to the intersects() method on the Geometry
 * specified by this Query, i.e., this.satisfies( entity ) =
 * this.getGeometry().intersects( entity.getGeometry() )
 */
public interface IntersectionQuery extends GeometryQuery
{
	
}
