/**
 * @(#) ViewportManagerListener.java
 */

package org.omg.tacsit.controller;

/**
 * ViewportManagerListener is the type of object that is notified by a
 * ViewportManager where it is registered in case a Viewport is added or removed
 * from that ViewportManager.
 */
public interface ViewportManagerListener
{
	/**
	 * This method is called by the ViewportManager where this
	 * ViewportManagerListener is registered in case a Viewport is added.
	 * 
	 * The details of the addition can be obtained through the given
	 * ViewportManagerEvent.
	 * 
	 */
	void viewportAdded( ViewportManagerEvent event );
	
	/**
	 * This method is called by the ViewportManager where this
	 * ViewportManagerListener is registered in case a Viewport is removed.
	 * 
	 * The details of the removal can be obtained through the given
	 * ViewportManagerEvent.
	 * 
	 */
	void viewportRemoved( ViewportManagerEvent event );
	
	
}
