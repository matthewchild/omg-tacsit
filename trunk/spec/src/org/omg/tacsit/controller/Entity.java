/**
 * @(#) Entity.java
 */

package org.omg.tacsit.controller;

/**
 * The Entity class is an abstraction of the tactical objects that can be
 * displayed on a Tactical Situation display (TacSit).
 * 
 * Entities are modeled such that it is possible for clients to establish there
 * specific type and geometry and hence proceed to interact with them in an
 * application specific manner.
 * 
 * Entities need to be able to indicate if they are still valid: even though
 * they may have been part of a selection at a certain point in time, by the
 * time that the entities of that selection are accessed the entities may not
 * exist any more in reality.
 */
public interface Entity
{
	/**
	 * Returns true if the given Entity corresponds to this Entity (i.e. the
	 * Tactical Objects are the same).
	 * 
	 * @return 
	 */
	boolean equals( Entity entity );
	
	/**
	 * Returns true if this instance is valid
	 * 
	 * @return 
	 */
	boolean isValid( );
	
	/**
	 * Returns the type of the Entity. This corresponds to the application
	 * specific class of the tactical object that has been added to the TacSit.
	 * 
	 * @return 
	 */
	EntityType getType( );
	
	/**
	 * This is a convenience method for indicating whether the entity is to be
	 * handled as though it is a single point rather than a complex geometry.
	 * This is useful for containment and other queries.
	 * 
	 * @return 
	 */
	boolean isPointEntity( );
	
	
}
