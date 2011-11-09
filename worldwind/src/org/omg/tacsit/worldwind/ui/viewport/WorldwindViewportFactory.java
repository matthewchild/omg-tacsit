/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 20, 2011
 */
package org.omg.tacsit.worldwind.ui.viewport;

import org.omg.tacsit.common.util.Factory;

/**
 * A Factory that creates new Worldwind Viewports.  The Viewports will have a default name.
 * @author Matthew Child
 */
public class WorldwindViewportFactory implements Factory<WorldwindViewport>
{
  private int viewportCount;

  /**
   * Creates a new instance.
   */
  public WorldwindViewportFactory()
  {
    viewportCount = 0;
  }
  
  public WorldwindViewport createObject()
  {    
    WorldwindViewport viewport = new WorldwindViewport();
    viewportCount++;
    viewport.setName("Viewport " + viewportCount);
    return viewport;
  }
}
