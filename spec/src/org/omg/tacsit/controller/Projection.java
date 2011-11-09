/**
 * @(#) Projection.java
 */

package org.omg.tacsit.controller;

/**
 * A projection is a method of displaying entities the surface of the earth on a
 * TacSit Viewport (i.e., a Map Projection).
 * 
 * For the purpose of the Tacsit Controller interface projections are only
 * needed to refer to by name.
 * 
 * Each Toolkit may support whatever projections it supports, this is a method
 * for the users to get a list of supported projections without having to
 * specify any details of how they are implemented.
 */
public interface Projection
{
	/**
	 * @return 
	 */
	String getName( );
	
	
}
