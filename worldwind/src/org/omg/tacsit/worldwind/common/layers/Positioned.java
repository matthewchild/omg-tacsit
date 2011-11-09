/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 2, 2011
 */
package org.omg.tacsit.worldwind.common.layers;

import gov.nasa.worldwind.geom.Position;

/**
 * An object which has a position.
 * @author Matthew Child
 */
public interface Positioned
{
  /**
   * Gets the position of the object.
   * @return The object's Position.
   */
  public Position getPosition();
}
