/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 23, 2007
 */
package org.omg.tacsit.common.ui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 * A utility class for enhancing and manipulating core Java components.
 * 
 * @author Matthew Child
 */
public abstract class ComponentUtils
{

  /**
   * The property identifier that is fired when the JFormattedTextField's value changes.
   */
  public static final String FORMATTED_FIELD_VALUE_PROPERTY = "value";
  /**
   * The property identifier that is fired when the Component's name changes.
   */
  public static final String COMPONENT_NAME_PROPERTY = "name";

  /**
   * Gets the window ancestor of an event object.
   * <p>
   * This presumes that eventSrc is some type of component.  This is provided as a convenience because the event source
   * for an Action is an Object, but is almost exclusively a component.
   * <p>
   * This differs from the SwingUtilities method, in that it will continue following the invoker of a JPopupMenu.
   * This is very useful since there are typically a significant number of actions in the Menu bar.
   * @param eventSrc The object to get the ancestor of.
   * @return The ancestor window of the eventSrc object, or null if no ancestor could be found.
   */
  public static Window getWindowAncestor(Object eventSrc)
  {
    if ((eventSrc == null) || (eventSrc instanceof Window))
    {
      return (Window) eventSrc;
    }
    else
    {
      Component parent = null;

      if (eventSrc instanceof JPopupMenu)
      {
        parent = ((JPopupMenu) eventSrc).getInvoker();
      }
      else if (eventSrc instanceof JComponent)
      {
        parent = ((JComponent) eventSrc).getParent();
      }

      return getWindowAncestor(parent);
    }
  }

  /**
   * Change a component's Font style.
   * <p>
   * The Font family and Font size are preserved in the updated Font.
   * @param component The component to change the Font presentation of.
   * @param style The new style to use (Font.BOLD, Font.ITALIC, Font.PLAIN)
   * @return The new Font installed to the component.
   */
  public static Font changeFontStyle(Component component, int style)
  {
    Font existingFont = component.getFont();
    Font newFont = existingFont.deriveFont(style);
    component.setFont(newFont);
    return newFont;
  }

  /**
   * Change a component's Font size.
   * <p>
   * The Font family and Font style are preserved in the updated Font.
   * @param component The component to change the Font presentation of.
   * @param size The new size of Font.
   * @return The new Font installed to the component.
   */
  public static Font changeFontSize(Component component, float size)
  {
    Font existingFont = component.getFont();
    Font newFont = existingFont.deriveFont(size);
    component.setFont(newFont);
    return newFont;
  }

  /**
   * Configures a text area to display as if it were a label.  This can be useful if you know how many columns
   * you want a label to be.
   * @param textArea The text area to configure as a JLabel.
   */
  public static void configureAsLabel(JTextArea textArea)
  {
    textArea.setOpaque(false);
    Font textFont = UIManager.getFont("Label.font");
    textArea.setFont(textFont);
    textArea.setBorder(null);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setEditable(false);
  }
}
