/**
 * @(#) TacsitController.java
 */

package org.omg.tacsit.controller;

import java.util.Collection;
import org.omg.tacsit.query.QueryManager;

/**
 * The top level interface for the Tactical Situation display (TacSit) Controller
 * interface, which enables clients to interact with Viewports and Query TacSit
 * content in terms of the Entity class.
 * 
 * The TacSit Controller interface does not provide the mechanism for adding
 * Entities to a TacSit. When a client receives an Entity from a Query, the
 * client of the TacSit controller will interpret the Entity according to
 * interfaces provided by other components than the Tacsit Controller (for
 * instance an interaction with another component, which is known to have added
 * the Entity to the TacSit by some other interface).
 */
public interface TacsitController
{
	/**
	 * Returns the projections which are supported by the TACSIT
	 * 
	 * @return 
	 */
	Collection<Projection> getProjections( );
	
	/**
	 * Return the Entity Types which are supported by the TACSIT. This will
	 * return a list of all Entity Types currently available for Selection and
	 * Query by this TACSITController
	 * 
	 * @return 
	 */
	Collection<EntityType> getEntityTypes( );
	
	/**
	 * Return the ViewportManager for this Tacsit Controller.
	 * 
	 * @return 
	 */
	ViewportManager getViewportManager( );
	
	/**
	 * Return the QueryManager for this Tacsit Controller
	 * 
	 * @return 
	 */
	QueryManager getQueryManager( );
	
	/**
	 * Returns the SelectionMethodology of the Tacsit controller
	 * 
	 * @return 
	 */
	SelectionMethodology getSelectionMethodology( );
	
	/**
	 * Return the SelectionManager that handles the selection on the viewport
	 * with name viewportName.
	 * 
	 * Depending on the SelectionMethodology of the Tacsit Controller the
	 * returned SelectionManagers for multiple viewports will be identical or
	 * different.
	 * 
	 * @return 
	 */
	SelectionManager getSelectionManager( String viewportName );
	
	
}
