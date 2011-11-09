/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 21, 2011
 */
package org.omg.tacsit.query;

import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.controller.EntityType;

/**
 * A Query that checks for Entities which match a particular EntityType.
 * <p>
 * If no EntityType is assigned, this query will return true for all values.
 * @author Matthew Child
 */
public class DefaultEntityTypeQuery implements EntityTypeQuery
{
  private EntityType entityType;

  /**
   * Creates a new instance.
   */
  public DefaultEntityTypeQuery()
  {
  }

  /**
   * Creates a new instance.
   * @param entityType The type of entity to find.
   */
  public DefaultEntityTypeQuery(EntityType entityType)
  {
    this.entityType = entityType;
  }

  /**
   * Gets the type of entity this query looks for.
   * @return The entity type to look for
   */
  public EntityType getEntityType()
  {
    return entityType;
  }

  /**
   * Sets the type of entity this query looks for.
   * @param entityType The entity type to look for
   */
  public void setEntityType(EntityType entityType)
  {
    this.entityType = entityType;
  }  

  public boolean satifies(Entity entity)
  {
    return (entityType == null) || entityType.equals(entity.getType());
  }

  public EntityType getEntityTypes()
  {
    return entityType;
  }

  @Override
  public String toString()
  {
    return "DefaultEntityTypeQuery{" + "entityType=" + entityType + '}';
  }
}
