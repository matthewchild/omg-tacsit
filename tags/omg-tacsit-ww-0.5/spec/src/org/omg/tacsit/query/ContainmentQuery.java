/**
 * @(#) ContainmentQuery.java
 */

package org.omg.tacsit.query;

/**
 * The ContainmentQuery is used to determine whether or not an Entity is
 * completely contained within a Geometry. Containment is determined by using
 * the Entity's Geometry as an argument to the contains() method on the Geometry
 * specified by this Query, i.e., this.satisfies( entity ) =
 * this.getGeometry().contains( entity.getGeometry() )
 */
public interface ContainmentQuery extends GeometryQuery
{
	
}
