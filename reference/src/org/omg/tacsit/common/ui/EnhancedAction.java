/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 1, 2009
 */

package org.omg.tacsit.common.ui;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

/**
 * A class that provides direct access method to some of Action's default properties.
 * <p>
 * Normally, setting properties of an action involves calls to <code>putValue</code>.   Compare the following 
 * examples:
 * <p>
 * <code>action.putValue(Action.NAME, actionName);</code><br>
 * <br>
 * <code>action.setName(actionName);</code><br>
 * <p>
 * The first example (from the Action interface) requires you to know 2 additional pieces of information:  That
 * the key in <code>putValue</code> is a member of the action interface, and that actionName is supposed to be
 * a String value.  The lack of compile time code checking means you could easily pass objects that don't make
 * sense.
 * <p>
 * having directly settable properties increases the readability of the code.  There is less information
 * that must be understood.  In addition, clients using an EnhancedAction class looking to set the name of the Action
 * will immediately recognize that <code>setName</code> performs that function.  The fact that <code>putValue</code>
 * can be used to set the name is not immediately obvious from the method name.
 * 
 * @author Matthew Child
 */
public abstract class EnhancedAction extends AbstractAction
{
  /**
   * Constructs a new EnhancedAction.
   */
  public EnhancedAction()
  {
  }
  
  /**
   * Constructs a new EnhancedAction with the specified name.
   * @param name The name of the action.
   */
  public EnhancedAction(String name)
  {
    super(name);
  }

  /**
   * Constructs a new EnhancedAction with the specified name and icon.
   * @param name The name of the action
   * @param icon The icon that represents the action.
   */
  public EnhancedAction(String name, Icon icon)
  {
    super(name, icon);
  }

  /**
   * Sets the name of the Action.
   * @param name The new name of the action.
   */
  public void setName(String name)
  {
    this.putValue(Action.NAME, name);
  }
  
  /**
   * Gets the name of the action.
   * @return The name of the action.
   */
  public String getName()
  {
    return (String)this.getValue(Action.NAME);
  }
  
  /**
   * Sets the icon of the action.
   * @param icon The new icon for the action.
   */
  public void setIcon(Icon icon)
  {
    this.putValue(Action.SMALL_ICON, icon);
  }
  
  /**
   * Gets the icon of the action.
   * @return The icon for the action.
   */
  public Icon getIcon()
  {
    return (Icon)this.getValue(Action.SMALL_ICON);
  }
  
  /**
   * Sets the description of the action.
   * <p>
   * For most components (include JButtons), this will be represented as a Tooltip.
   * @param descrip The description of the action.
   */
  public void setDescription(String descrip)
  {
    this.putValue(Action.SHORT_DESCRIPTION, descrip);
  }

  /**
   * Gets the description of the action.
   * <p>
   * For most components (include JButtons), this will be represented as a Tooltip.
   * @return The description of the action.
   */
  public String getDescription()
  {
    return (String)this.getValue(Action.SHORT_DESCRIPTION);
  }
}
