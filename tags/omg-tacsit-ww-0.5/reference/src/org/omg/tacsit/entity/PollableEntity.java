/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 19, 2011
 */
package org.omg.tacsit.entity;

import org.omg.tacsit.controller.Entity;

/**
 * An Entity which keeps track of the last time it was modified.
 * @author Matthew Child
 */
public interface PollableEntity extends Entity
{
  /**
   * Gets the system time the Entity was last modified.  The time returned is in same reference as 
   * System.currentTimeInMillis() (millis from epoch).
   * @return The system time the entity was modified.
   */
  public long getLastModified();
}
