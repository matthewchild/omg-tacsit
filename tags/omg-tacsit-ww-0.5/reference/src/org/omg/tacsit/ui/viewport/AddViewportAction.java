/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 20, 2011
 */
package org.omg.tacsit.ui.viewport;

import org.omg.tacsit.common.ui.ConfigurableAction;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import org.omg.tacsit.common.util.Factory;
import org.omg.tacsit.controller.Viewport;
import org.omg.tacsit.controller.ViewportManager;

/**
 * An Action that adds a new Viewport to a ViewportManager.
 * @author Matthew Child
 */
public class AddViewportAction extends ConfigurableAction
{
  private Factory<? extends EntityViewport> viewportFactory;
  
  private ViewportManager viewportManager;

  /**
   * Creates a new instance.
   * @param name The name of the action.
   * @param icon The icon to use for the action.
   */
  public AddViewportAction(String name, Icon icon)
  {
    super(name, icon);
  }

  /**
   * Gets the ViewportManager that new Viewports are added to.
   * @return The ViewportManager that will receive new Viewports.
   */
  public ViewportManager getViewportManager()
  {
    return viewportManager;
  }

  /**
   * Sets the ViewportManager that new viewports will be added to.
   * @param viewportManager The ViewportManager that will receive new Viewports
   */
  public void setViewportManager(ViewportManager viewportManager)
  {
    this.viewportManager = viewportManager;
    checkEnabledState();
  }

  /**
   * Gets the factory that's will be used to create new Viewports.
   * @return The factory that makes new Viewports.
   */
  public Factory<? extends EntityViewport> getViewportFactory()
  {
    return viewportFactory;
  }

  /**
   * Sets the factory that will be used to create new Viewports.
   * @param viewportFactory The factory that makes new Viewports.
   */
  public void setViewportFactory(Factory<? extends EntityViewport> viewportFactory)
  {
    this.viewportFactory = viewportFactory;
    checkEnabledState();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Viewport viewport = viewportFactory.createObject();
    viewportManager.addViewport(viewport);
  }

  @Override
  public boolean isPerformable()
  {
    return (this.viewportManager != null) && (this.viewportFactory != null);
  }
}
