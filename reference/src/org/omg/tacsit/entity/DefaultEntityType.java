/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 24, 2011
 */
package org.omg.tacsit.entity;

import org.omg.tacsit.controller.EntityType;

/**
 * An EntityType that is defined by an Object identifier.
 * @author Matthew Child
 */
public class DefaultEntityType implements EntityType
{
  private Object identifier;
  private String typeName;

  /**
   * Creates a new instance.
   * @param identifier The identifier that is unique to this EntityType.
   */
  public DefaultEntityType(Object identifier)
  {
    this.identifier = identifier;
    this.typeName = String.valueOf(identifier);
  }

  /**
   * Gets the name for the EntityType.
   * @return The name of the EntityType.
   */
  public String getTypeName()
  {
    return typeName;
  }
  
}
