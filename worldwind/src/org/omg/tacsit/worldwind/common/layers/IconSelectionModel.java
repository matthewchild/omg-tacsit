/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 2, 2011
 */
package org.omg.tacsit.worldwind.common.layers;

import gov.nasa.worldwind.render.WWIcon;

/**
 * A selection model which determines whether an icon is selected.
 * @author Matthew Child
 */
public interface IconSelectionModel
{
  /**
   * Gets the selection type for an icon.
   * @param icon The icon to check the selection type of.
   * @return The selection type for the item.
   */
  public Object getSelectionType(WWIcon icon);
}
