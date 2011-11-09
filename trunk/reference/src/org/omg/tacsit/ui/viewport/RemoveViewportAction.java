/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 20, 2011
 */
package org.omg.tacsit.ui.viewport;

import org.omg.tacsit.common.ui.ConfigurableAction;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import org.omg.tacsit.common.util.Disposable;
import org.omg.tacsit.controller.Viewport;
import org.omg.tacsit.controller.ViewportManager;

/**
 * An Action which removes a Viewport from a ViewportManager.
 * @author Matthew Child
 */
public class RemoveViewportAction extends ConfigurableAction
{
  private Viewport viewport;
  
  private ViewportManager viewportManager;

  /**
   * Creates a new instance.
   * @param name The name of the action.
   * @param icon The icon to use for the action.
   */
  public RemoveViewportAction(String name, Icon icon)
  {
    super(name, icon);
  }

  /**
   * Gets the ViewportManager that the Viewport will be removed from.
   * @return The ViewportManager to remove from.
   */
  public ViewportManager getViewportManager()
  {
    return viewportManager;
  }

  /**
   * Sets the ViewportManager that the Viewport will be removed from.
   * @param viewportManager The ViewportManager to remove from.
   */
  public void setViewportManager(ViewportManager viewportManager)
  {
    this.viewportManager = viewportManager;
    checkEnabledState();
  }

  /**
   * Gets the Viewport that will be removed.
   * @return The viewport to remove.
   */
  public Viewport getViewport()
  {
    return viewport;
  }

  /**
   * Sets the Viewport that will be removed.
   * @param viewport The viewport to remove.
   */
  public void setViewport(Viewport viewport)
  {
    this.viewport = viewport;
    checkEnabledState();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    viewportManager.removeViewport(viewport);
    if(viewport instanceof Disposable)
    {
      ((Disposable)viewport).dispose();
    }
  }

  @Override
  public boolean isPerformable()
  {
    return (this.viewportManager != null) && (this.viewport != null);
  }
}
