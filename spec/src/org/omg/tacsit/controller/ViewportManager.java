/**
 * @(#) ViewportManager.java
 */

package org.omg.tacsit.controller;

import java.util.Collection;

/**
 * The ViewportManager enables a TacSit client to manage Viewport instances and
 * track changes to the set of Viewports. Construction of a Viewport is outside
 * of the scope of this interface.
 */
public interface ViewportManager
{
	/**
	 * Return all Viewports managed by this ViewportManager
	 * 
	 * @return 
	 */
	Collection<Viewport> getViewports( );
	
	/**
	 * Add a Viewport to this ViewportManager.
	 * 
	 * If the Viewport is already added to the ViewportManager before, this
	 * operation has no effect.
	 * 
	 */
	void addViewport( Viewport viewport );
	
	/**
	 * Remove the given Viewport from this ViewportManager.
	 * 
	 * If the given Viewport is not managed by this ViewportManager, this
	 * operation as no effect.
	 * 
	 */
	void removeViewport( Viewport viewport );
	
	/**
	 * Registers the given ViewportManagerListener to this ViewportManager.
	 * 
	 * After registration the ViewportManagerListener will be notified through
	 * each time that a viewport is added or removed through its corresponding
	 * method.
	 * 
	 * Registering a ViewportManagerListener that was already registered with
	 * this ViewportManager does not have any effect.
	 * 
	 */
	void addViewportManagerListener( ViewportManagerListener listener );
	
	/**
	 * Unregisters the givenViewportManagerListener from this ViewportManager.
	 * 
	 * Afterwards the ViewportManagerListener will not be notified of added or
	 * removed Viewports.
	 * 
	 * Unregistering a ViewportManagerListener that was not registered does not
	 * have any effect.
	 * 
	 */
	void removeViewportManagerListener( ViewportManagerListener listener );
	
	/**
	 * Return the viewport with the given name.
	 * 
	 * If there is no such viewport, null is returned.
	 * 
	 * @return 
	 */
	Viewport getViewport( String name );
	
	
}
