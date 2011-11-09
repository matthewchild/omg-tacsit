/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 23, 2011
 */
package org.omg.tacsit.worldwind.entity;

import java.util.Arrays;
import java.util.Collection;
import org.omg.tacsit.controller.EntityType;

/**
 * An enumeration of EntityTypes that are supported by the Worldwind Tacsit Implementation.
 * @author Matthew Child
 */
public enum WWEntityType implements EntityType
{
  /**
   * Entities of interest to a tactical display.  Usually consists of ships and planes.
   */
  TRACK("Track"),
  
  /**
   * Entities that are represents static points of interest.
   */
  LANDMARK("Landmark"),
  
  /**
   * An entity that consists of a portion of surface area on the globe.
   */
  SURFACE_GEOMETRY("Surface Geometry");
  
  private String typeName;
  
  /**
   * Creates a new instance.
   * @param typeName The printable name of the entity type.
   */
  WWEntityType(String typeName)
  {
    this.typeName = typeName;
  }

  /**
   * Gets the name of this EntityType.
   * @return The name of the EntityType.
   */
  public String getTypeName()
  {
    return typeName;
  }
  
  /**
   * Converts the Worldwind entity enumeration to a Collection of values.
   * @return A Collection of all values of EntityTypes support by this Tacsit implementation.
   */
  public static Collection<WWEntityType> asCollection()
  {
    WWEntityType[] values = values();
    return Arrays.asList(values);
  }
}
