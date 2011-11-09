/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 18, 2011
 */
package org.omg.tacsit.worldwind.ui.geometry;

import java.awt.Component;
import java.beans.PropertyEditorSupport;
import org.omg.tacsit.worldwind.geometry.WWSurfaceCircle;

/**
 * A PropertyEditor which can edit WWSurfaceCircles.
 * @author Matthew Child
 */
public class WWSurfaceCircleEditor extends PropertyEditorSupport
{
  private WWSurfaceCirclePanel rectangleEditPanel;

  /**
   * Creates a new instance.
   */
  public WWSurfaceCircleEditor()
  {
    rectangleEditPanel = new WWSurfaceCirclePanel();
  }

  @Override
  public void setValue(Object value)
  {
    WWSurfaceCircle circle = (WWSurfaceCircle)value;
    super.setValue(circle);
    rectangleEditPanel.setValue(circle);
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
