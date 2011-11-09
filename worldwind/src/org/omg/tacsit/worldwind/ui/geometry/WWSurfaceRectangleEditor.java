/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 18, 2011
 */
package org.omg.tacsit.worldwind.ui.geometry;

import java.awt.Component;
import java.beans.PropertyEditorSupport;
import org.omg.tacsit.worldwind.geometry.WWSurfaceRectangle;

/**
 * A PropertyEditor which can edit the properties of a WWRectangle.
 * @author Matthew Child
 */
public class WWSurfaceRectangleEditor extends PropertyEditorSupport
{
  private WWSurfaceRectanglePanel rectangleEditPanel;

  /**
   * Creates a new instance.
   */
  public WWSurfaceRectangleEditor()
  {
    rectangleEditPanel = new WWSurfaceRectanglePanel();
  }

  @Override
  public void setValue(Object value)
  {
    WWSurfaceRectangle rectangle = (WWSurfaceRectangle)value;
    super.setValue(rectangle);
    rectangleEditPanel.setValue(rectangle);
  }

  @Override
  public Component getCustomEditor()
  {
    return rectangleEditPanel;
  }

  @Override
  public boolean supportsCustomEditor()
  {
    return true;
  }
}
