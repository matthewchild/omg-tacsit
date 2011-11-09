/**
 * @(#) EntityQuery.java
 */

package org.omg.tacsit.query;

import org.omg.tacsit.controller.Entity;

/**
 * An EntityQuery encapsulates the criteria against which an Entity may be
 * evaluated for membership.
 */
public interface EntityQuery
{
	/**
	 * Returns true if the specified Entity satisfies the criteria represented
	 * by this EntityQuery; false otherwise.
	 * 
	 * @return 
	 */
	boolean satifies( Entity entity );
	
	
}
