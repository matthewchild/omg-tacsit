/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 16, 2011
 */
package org.omg.tacsit.worldwind.entity;

import gov.nasa.worldwind.geom.Position;
import org.omg.tacsit.controller.EntityType;
import org.omg.tacsit.geometry.GeodeticPosition;
import org.omg.tacsit.entity.PointEntity;
import org.omg.tacsit.worldwind.common.layers.Positioned;
import org.omg.tacsit.worldwind.geometry.WWGeodeticPosition;

/**
 * A point entity that provides geodetic location information in Worldwind's format.
 * @author Matthew Child
 */
public class WWPointEntity extends PointEntity implements Positioned
{
  /**
   * The position property of the entity.
   */
  public static final String PROPERTY_POSITION = "Position";
  
  /**
   * Creates a new instance.
   * @param entityType The type of entity. must be either a TRACK or LANDMARK.
   */
  public WWPointEntity(WWEntityType entityType)
  {
    super(entityType);
    validateEntityType(entityType);
    super.setReferencePosition(WWGeodeticPosition.ZERO);
  }
  
  private void validateEntityType(EntityType entityType)
  {
    if((!WWEntityType.TRACK.equals(entityType)) && (!WWEntityType.LANDMARK.equals(entityType)))
    {
      throw new UnsupportedOperationException("entityType " + entityType + " is not a valid type for an EntityItem.");
    }
  }

  @Override
  public WWGeodeticPosition getReferencePosition()
  {
    return (WWGeodeticPosition)super.getReferencePosition();
  }
  
  @Override
  public void setReferencePosition(GeodeticPosition geodeticPosition)
  {
    WWGeodeticPosition wwGeodeticPosition = WWGeodeticPosition.toWWGeodeticPosition(geodeticPosition);
    super.setReferencePosition(wwGeodeticPosition);
  }
  
  /**
   * Sets the reference position using Worldwind's native location objects.
   * @param position The new reference position of the entity.
   */
  public void setReferencePosition(Position position)
  {
    setReferencePosition(new WWGeodeticPosition(position));
  }
  
  public Position getPosition()
  {
    return getReferencePosition().getPosition();
  }
}
