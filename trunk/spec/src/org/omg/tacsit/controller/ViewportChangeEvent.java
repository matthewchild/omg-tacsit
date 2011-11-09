/**
 * @(#) ViewportChangeEvent.java
 */

package org.omg.tacsit.controller;

/**
 * ViewportChangeEvent is the type of event that is passed to a
 * ViewportChangeListener each time the Viewport changes.
 * 
 * Through the ViewportChangeEvent it is possible to know which Viewport has
 * been changed and details of the change.
 * 
 * If there are properties other than the view eye that the client is interested
 * in, they can query the associated Viewport Object contained in the event
 * available in the getSource() method.
 */
public class ViewportChangeEvent
{
	private final Viewport viewport;
	
	private final ViewEyeProperties viewEyeProperties;
	
	public ViewportChangeEvent( Viewport viewport, ViewEyeProperties viewEyeProperties )
	{
		this.viewport = viewport;
    this.viewEyeProperties = viewEyeProperties;
	}
	
	/**
	 * Returns a copy of the ViewEyeProperties of the Viewport that has changed.
	 * 
	 * @return 
	 */
	public ViewEyeProperties getViewEyeProperties( )
	{
		return viewEyeProperties;
	}
	
	/**
	 * Returns the Viewport that has changed
	 * 
	 * @return 
	 */
	public Viewport getSource( )
	{
		return viewport;
	}
	
	
}
