/**
 * @(#) SelectionListener.java
 */

package org.omg.tacsit.controller;

/**
 * SelectionListener is the type of object that is notified by a
 * SelectionManager where it is registered in case of a change in one of the
 * selections of that SelectionManager.
 */
public interface SelectionListener
{
	/**
	 * This method is called by a SelectionManager when this SelectionListener
	 * is registered for selection changes for that SelectionManager. The
	 * details of which selection changed and which elements are still in the
	 * selection can be obtained through the given SelectionEvent.
	 * 
	 */
	void selectionChanged( SelectionEvent event );
	
	
}
