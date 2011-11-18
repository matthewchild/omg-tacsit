/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 17, 2011
 */

package org.omg.tacsit.common.ui;

import javax.swing.Icon;

/**
 * An action which has settable components which must be initialized before the action can be performed.
 * <p>
 * This type of action is especially useful for actions who have their state updated externally based on the selection 
 * of a Component.  If the selection state is empty, the action will be disabled.
 * <p>
 * This pattern helps create actions that are "safer" in UIs.  If the action can't be performed, it will be disabled.
 * However, an action can still be programmatically disabled.  The action won't automatically be turned back "enabled"
 * if the associated components are all set properly and would otherwise be performable.  This also helps easily
 * identify actions whose necessary components have not been set.
 * <p>
 * For each <code>set</code> method in a subclass, the user should call checkEnabledState().  This will ensure the 
 * enabled state of the action is updated based on the new variables.
 * <p>
 * <b>Implementation notes:</b> 
 * <li>The default state of a ConfigurableAction is disabled, because it is expected that child implementing classes
 * will have data that needs to be set.</li>
 * <li>Generally, you should not call checkEnabledState() in the constructor.  This can lead to access to partially
 * initialized classes, violating the integrity of parent classes.</li>
 * @author Matthew Child
 */
public abstract class ConfigurableAction extends EnhancedAction
{
  private boolean userEnabled;

  /**
   * Constructs a new ConfigurableAction.  The action will be disabled by default.
   */
  public ConfigurableAction()
  {
    initEnabledState();
  }
  
  /**
   * Constructs a new ConfigurableAction with the specified name.  The action will be disabled by default.
   * @param name The name of the action.
   */
  public ConfigurableAction(String name)
  {
    super(name);
    initEnabledState();
  }
  
  /**
   * Constructs a new ConfigurableAction with the specified name and icon.  The action will be disabled by default.
   * @param name The name of the action.
   * @param icon The icon that represents the action.
   */
  public ConfigurableAction(String name, Icon icon)
  {
    super(name, icon);
    initEnabledState();
  }
  
  private void initEnabledState()
  {
    userEnabled = super.isEnabled();
    super.setEnabled(false);
  }

  /**
   * Checks to see if all the required data for the action is set.  Subclasses should return true only if all
   * associated variables to the action have been set.
   * @return true if all the required data has been set, or false if any is missing.
   */
  public abstract boolean isPerformable();

  /**
   * Checks the enabled state of the action.  If the action has been disabled externally, the action will remain
   * disabled even if the action is otherwise performable.
   */
  protected final void checkEnabledState()
  {
    super.setEnabled(userEnabled && isPerformable());
  }

  @Override
  public final void setEnabled(boolean newValue)
  {
    userEnabled = newValue;
    checkEnabledState();
  }
}
