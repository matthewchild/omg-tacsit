/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 1, 2011
 */
package org.omg.tacsit.common.ui;

import java.awt.Component;

/**
 * An object which has a visual display component.
 * <p>
 * Since Java's core windowing implementations (AWT and Swing) all inherit from Component directly, it is difficult
 * to create interface contracts for components.  For example, you might want to create a component which uses another
 * component, as long as you can add and remove Strings to that component.  Such an interface might look like this:
 * <p>
 * <code>
 * public interface WidgetListElement implements UIElement<br>
 * {<br>
 *   void addWidget(Widget widget);<br>
 *   void removeWidget(Widget widget);<br>
 * }<br>
 * </code>
 * <p>
 * This allows your container class to work with multiple types of components that can contain Strings.  You might
 * initially implement this as a JList, for simplicitly.  However, as the project grows, Widgets become more complex,
 * and it becomes more appropriate to display them as a JTable of values with their properties.
 * <p>
 * Special concerns for implementers:<br>
 * <li>The component returned by getComponent() must never change.  As such, if the component is likely to change,
 * it should be wrapped in a JPanel, and that JPanel should be returned as the UIElement's component.</li>
 * <li>Clients of UIElement will frequently use the Component's name property for displaying a title.  In general,
 * you should always set the name of the component returned by getComponent() to a reasonable default.</li>
 * <p>
 * 
 * @author Matthew Child
 */
public interface UIElement
{

  /**
   * Gets the component used to display this UIElement.
   * <p>
   * The component returned should not be modified.  The type of Component that is returned varies greatly depending
   * on the UIElement, and setting properties of that Component may have unintended consequences.  For example, if the
   * component is a JScrollPane, setting the Layout property of the component will cause the scroll pane to be 
   * displayed incorrectly.
   * <p>
   * If you need a title for the UIElement, use the name property of the component.  If you do so, you should also 
   * add a propertyChangeListener to the component to receive updates for when the name is modified.
   * @return The Component that displays this UIElement.
   */
  public Component getComponent();
}
