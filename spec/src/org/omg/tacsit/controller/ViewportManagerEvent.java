/**
 * @(#) ViewportManagerEvent.java
 */

package org.omg.tacsit.controller;

/**
 * ViewportManagerEvent is the type of event that is passed to a
 * ViewportManagerListener each time there is a Viewport is added or removed
 * from the ViewportManager.
 * 
 * Through the ViewportManagerEvent it is possible to know which Viewport has
 * been added to which ViewportManager
 */
public class ViewportManagerEvent
{
  private Viewport viewport;
  private ViewportManager source;

  public ViewportManagerEvent(Viewport viewport, ViewportManager source)
  {
    this.viewport = viewport;
    this.source = source;
  }

	/**
	 * Returns the viewport that has been added or removed by this event.
	 * 
	 * @return 
	 */
	public Viewport getViewport( )
  {
    return this.viewport;
  }
	
	/**
	 * Returns the ViewportManager to which a Viewport has been added or
	 * removed.
	 * 
	 * @return 
	 */
	ViewportManager getSource( )
  {
    return this.source;
  }
}
