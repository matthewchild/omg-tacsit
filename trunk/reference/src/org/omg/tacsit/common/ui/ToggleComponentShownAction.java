/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 1, 2009
 */

package org.omg.tacsit.common.ui;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.Icon;

/**
 * Toggles whether a component is shown.
 * @author Matthew Child
 */
public class ToggleComponentShownAction extends AbstractToggleAction
{
  private Component component;
  private ComponentListener visibilityListener;

  /**
   * Creates a new action.
   */
  public ToggleComponentShownAction()
  {
    this("Hide", "Show", null);
  }

  /**
   * Creates a new action with the specified toggleOn/toggleOff text
   * @param toggleOnText The text to use when the toggle is on.
   * @param toggleOffText The text to use when the toggle is off.
   * @param component The component who's visibility will be toggled.
   */
  public ToggleComponentShownAction(String toggleOnText, String toggleOffText, Component component)
  {
    super(toggleOnText, toggleOffText);
    visibilityListener = new VisibilityListener();
    setComponent(component);
  }

  /**
   * Creates a new action with the specified toggleOn/toggleOff text
   * @param toggleOnIcon The icon that represents the toggle on state.
   * @param toggleOffIcon The icon that represents the toggle off state.
   * @param component The component who's visibility will be toggled.
   */
  public ToggleComponentShownAction(Icon toggleOnIcon, Icon toggleOffIcon, Component component)
  {
    super(toggleOnIcon, toggleOffIcon);
    visibilityListener = new VisibilityListener();
    setComponent(component);
  }

  /**
   * Gets whether or not the action is performable.
   * @return true if the action can be performed, or false otherwise.
   */
  protected boolean isPerformable()
  {
    return this.component != null;
  }

  /**
   * Checks whether or not the action should be enabled based on its state.
   */
  protected void checkEnabledState()
  {
    setEnabled(isPerformable());
  }

  /**
   * Gets the component who's visibility state will be toggled.
   * @return The component who's visibility state will be toggled.
   */
  public Component getComponent()
  {
    return component;
  }

  /**
   * Sets the component who's visibility state will be toggled.
   * @param component  The component who's visibility state will be toggled.
   */
  public void setComponent(Component component)
  {
    if(this.component != null)
    {
      this.component.removeComponentListener(visibilityListener);
    }
    this.component = component;
    if(this.component != null)
    {
      this.component.addComponentListener(visibilityListener);
    }
    checkEnabledState();
    reloadToggleText();
  }

  @Override
  public boolean isToggleOn()
  {
    return (component != null) ? component.isVisible()
                               : false;
  }

  @Override
  protected void setToggleState(boolean toggleOn)
  {
    if(component != null)
    {
      component.setVisible(toggleOn);
    }
  }
  
  private class VisibilityListener extends ComponentAdapter
  {

    @Override
    public void componentHidden(ComponentEvent e)
    {
      loadToggleText(false);
      super.componentHidden(e);
    }

    @Override
    public void componentShown(ComponentEvent e)
    {
      loadToggleText(true);
    }
  }
}
