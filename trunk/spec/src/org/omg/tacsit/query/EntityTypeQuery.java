/**
 * @(#) EntityTypeQuery.java
 */

package org.omg.tacsit.query;

import org.omg.tacsit.controller.EntityType;

/**
 * The EntityTypeQuery is used to determine whether or not a given Entity
 * belongs to a particular EntityType.
 */
public interface EntityTypeQuery extends EntityQuery
{
	/**
	 * The EntityType to compare with
	 * 
	 * @return 
	 */
	EntityType getEntityTypes( );
	
	
}
