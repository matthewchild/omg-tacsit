/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 1, 2009
 */

package org.omg.tacsit.common.ui;

import java.awt.event.ActionEvent;
import javax.swing.Icon;

/**
 * An Action which toggles between an On and an Off state.
 * @author Matthew Child
 */
public abstract class AbstractToggleAction extends EnhancedAction
{
  private String toggleOnText;
  private String toggleOffText;
  
  private Icon toggleOnIcon;
  private Icon toggleOffIcon;

  private String toggleOnDescription;
  private String toggleOffDescription;

  /**
   * Constructs a new action.
   * @param toggleOnText The text that should be used when the toggle is on.
   * @param toggleOffText The text that should be used when the toggle is off.
   */
  public AbstractToggleAction(String toggleOnText, String toggleOffText)
  {
    this.toggleOnText = toggleOnText;
    this.toggleOffText = toggleOffText;
  }

  /**
   * Constructs a new action.
   * @param toggleOnIcon The icon that should be used when the toggle is on.
   * @param toggleOffIcon  The icon that should be used when the toggle is off.
   */
  public AbstractToggleAction(Icon toggleOnIcon, Icon toggleOffIcon)
  {
    this.toggleOnIcon = toggleOnIcon;
    this.toggleOffIcon = toggleOffIcon;
  }

  /**
   * Reloads the toggle text based on the current toggle state.
   */
  protected void reloadToggleText()
  {
    loadToggleText(isToggleOn());
  }

  /**
   * Loads the toggle text from a given toggle state.
   * @param isOn True if the toggle is on, false if the toggle is off.
   */
  protected void loadToggleText(boolean isOn)
  {
    String name;
    Icon icon;
    String description;
    if(isOn)
    {
      name = toggleOnText;
      icon = toggleOnIcon;
      description = toggleOnDescription;
    }
    else
    {
      name = toggleOffText;
      icon = toggleOffIcon;
      description = toggleOffDescription;
    }

    setName(name);
    setIcon(icon);
    setDescription(description);
  }

  /**
   * Sets the toggle state.
   * @param isOn true if the state should be set to on, false otherwise.
   */
  public void setToggleOn(boolean isOn)
  {
    loadToggleText(isOn);
    setToggleState(isOn);
  }

  /**
   * Gets the toggle off description.
   * @return The description used when the toggle is off.
   */
  public String getToggleOffDescription()
  {
    return toggleOffDescription;
  }

  /**
   * Sets the description used when the toggle is off.
   * @param toggleOffDescription The description to use when the toggle is off.
   */
  public void setToggleOffDescription(String toggleOffDescription)
  {
    this.toggleOffDescription = toggleOffDescription;
    if(!isToggleOn())
    {
      setDescription(this.toggleOffDescription);
    }
  }

  /**
   * Gets the icon used when the toggle is off.
   * @return The icon used when the toggle is off.
   */
  public Icon getToggleOffIcon()
  {
    return toggleOffIcon;
  }

  /**
   * Sets the icon used when the toggle is off.
   * @param toggleOffIcon  The icon to use when the toggle is off.
   */
  public void setToggleOffIcon(Icon toggleOffIcon)
  {
    this.toggleOffIcon = toggleOffIcon;
    if(!isToggleOn())
    {
      setIcon(this.toggleOffIcon);
    }
  }

  /**
   * Gets the name used when the toggle is off.
   * @return The name used when the toggle is off.
   */
  public String getToggleOffText()
  {
    return toggleOffText;
  }

  /**
   * Sets the name used when the toggle is off.
   * @param toggleOffText The name to use when the toggle is off. 
   */
  public void setToggleOffText(String toggleOffText)
  {
    this.toggleOffText = toggleOffText;
    if(!isToggleOn())
    {
      setName(this.toggleOffText);
    }
  }

  /**
   * Gets the description used when the toggle is on.
   * @return The description used when the toggle is on.
   */
  public String getToggleOnDescription()
  {
    return toggleOnDescription;
  }

  /**
   * Sets the description used when the toggle is on.
   * @param toggleOnDescription  The description used when the toggle is on.
   */
  public void setToggleOnDescription(String toggleOnDescription)
  {
    this.toggleOnDescription = toggleOnDescription;
    if(isToggleOn())
    {
      setDescription(this.toggleOnDescription);
    }
  }

  /**
   * Gets the icon used when the toggle is on.
   * @return The icon used when the toggle is on.
   */
  public Icon getToggleOnIcon()
  {
    return toggleOnIcon;
  }

  /**
   * Sets the icon used when the toggle is on.
   * @param toggleOnIcon  The icon used when the toggle is on.
   */
  public void setToggleOnIcon(Icon toggleOnIcon)
  {
    this.toggleOnIcon = toggleOnIcon;
    if(isToggleOn())
    {
      setIcon(this.toggleOnIcon);
    }
  }

  /**
   * Gets the name used when the toggle is on.
   * @return The name used when the toggle is on.
   */
  public String getToggleOnText()
  {
    return toggleOnText;
  }

  /**
   * Sets the name used when the toggle is on.
   * @param toggleOnText The name to use when the toggle is on.
   */
  public void setToggleOnText(String toggleOnText)
  {
    this.toggleOnText = toggleOnText;
    if(isToggleOn())
    {
      setName(this.toggleOnText);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    setToggleOn(!isToggleOn());
  }
  
  /**
   * Checks if the action is currently toggled on.
   * @return true if the toggle is currently on, or false otherwise.
   */
  public abstract boolean isToggleOn();
  
  /**
   * Sets the state of the toggled action to on.
   * @param toggleOn whether or not the toggle should be on.
   */
  protected abstract void setToggleState(boolean toggleOn);
}
