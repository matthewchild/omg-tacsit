/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 13, 2011
 */
package org.omg.tacsit.ui.viewport;

import org.omg.tacsit.common.ui.UIElement;
import org.omg.tacsit.common.util.PropertyListenable;
import org.omg.tacsit.controller.Viewport;

/**
 * A Viewport which that provides simple, additional common UI capabilities.
 * @author Matthew Child
 */
public interface ComponentViewport extends Viewport, UIElement, PropertyListenable
{
  /**
   * The property that's fired when the name changes.
   */
  public static final String NAME_PROPERTY = "name";
}
