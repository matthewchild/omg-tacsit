/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 30, 2011
 */
package org.omg.tacsit.entity;

import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.geometry.GeodeticPosition;

/**
 * An Entity which has a single defining reference position.
 * <p>
 * The Entity may be a single point, or cover a large area bounded by many points.  The reference position provides 
 * client objects some general clue about where it is located.  A PointEntity might return its only point; a surface
 * shape might return its center point.
 * 
 * @author Matthew Child
 */
public interface PositionedEntity extends Entity
{
  /**
   * Gets the reference position of the entity.  This is a general marker of approximately where the entity is located.
   * @return The reference geodetic position.
   */
  GeodeticPosition getReferencePosition();
}
