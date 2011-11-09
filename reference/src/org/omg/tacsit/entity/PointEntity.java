/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 15, 2011
 */
package org.omg.tacsit.entity;

import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.controller.EntityType;
import org.omg.tacsit.geometry.GeodeticPosition;
import org.omg.tacsit.geometry.DefaultGeodeticPosition;

/**
 * An Entity who's location is defined by a reference point.
 * @author Matthew Child
 */
public class PointEntity extends AbstractEntity
{
    
  private GeodeticPosition referencePosition;

  /**
   * Creates a new instance.
   * @param entityType The EntityType that this PointEntity is.
   */
  public PointEntity(EntityType entityType)
  {
    super(entityType);
    this.referencePosition = DefaultGeodeticPosition.ZERO;
  }
    
  /**
   * Sets the geodetic reference position of where this entity is located.
   * @param geodeticPosition The new position of the entity.
   */
  public void setReferencePosition(GeodeticPosition geodeticPosition)
  {    
    GeodeticPosition oldPosition = this.referencePosition;
    this.referencePosition = geodeticPosition;
    propertyChanged(PROPERTY_REFERENCE_POSITION, oldPosition, this.referencePosition);
  }

  @Override
  public boolean equals(Entity entity)
  {
    boolean isEqual = false;
    
    if(super.equals(entity) && (entity instanceof PointEntity))
    {
      GeodeticPosition otherPoint = ((PointEntity)entity).getReferencePosition();
      isEqual = (otherPoint == null) ? this.referencePosition == null
                                     : otherPoint.equals(referencePosition);
    }
    return isEqual;
  }
  
  public GeodeticPosition getReferencePosition()
  {
    return this.referencePosition;
  }

  public boolean isPointEntity()
  {
    return true;
  }
}
