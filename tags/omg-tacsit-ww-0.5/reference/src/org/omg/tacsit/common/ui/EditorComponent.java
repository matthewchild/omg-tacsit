/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 25, 2011
 */
package org.omg.tacsit.common.ui;

/**
 * An editor capable of modifying objects.
 * <p>
 * Typically, an EditorComponent will have special handling for Collection objects.
 * @author Matthew Child
 */
public interface EditorComponent extends UIElement
{
  /**
   * Sets the value the editor component should modify.
   * 
   * @param value The new value to allow a user to modify.
   */
  public void setValue(Object value);
  
  /**
   * Gets the value the editor component is modifying.
   * @return The value currently being edited.
   */
  public Object getValue();
}
