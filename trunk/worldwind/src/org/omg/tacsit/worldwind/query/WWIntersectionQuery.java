/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 28, 2011
 */
package org.omg.tacsit.worldwind.query;

import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.geometry.GeodeticPosition;
import org.omg.tacsit.entity.PointEntity;
import org.omg.tacsit.query.IntersectionQuery;
import org.omg.tacsit.worldwind.entity.GeometryEntity;
import org.omg.tacsit.worldwind.geometry.WWSurfaceGeometry;

/**
 * A query that determines whether an entity is intersects a the surface geometry.
 * <p>
 * This provides an implementation of the Tacsit standard's IntersectionQuery for Worldwind.
 * @author Matthew Child
 */
public class WWIntersectionQuery extends WWGeometryQuery implements IntersectionQuery
{
  /**
   * Creates a new instance.
   */
  public WWIntersectionQuery()
  {
  }

  /**
   * Creates a new instance.
   * @param geometry The geometry that will be used to test whether entities intersect it.
   */
  public WWIntersectionQuery(WWSurfaceGeometry geometry)
  {
    super(geometry);
  }
  
  private GeodeticPosition getPointEntityPosition(Entity entity)
  {
    GeodeticPosition position;
    if(entity instanceof PointEntity)
    {
      position = ((PointEntity)entity).getReferencePosition();
    }
    else
    {
      throw new IllegalArgumentException("Unable to get position from point entity " + entity);
    }
    return position;
  }

  public boolean satifies(Entity entity)
  {
    boolean satisfies = false;
    WWSurfaceGeometry geometry = getGeometry();
    if(geometry != null)
    {
      if(entity.isPointEntity())
      {
        GeodeticPosition entityPosition = getPointEntityPosition(entity);
        satisfies = geometry.contains(entityPosition);
      }
      else if(entity instanceof GeometryEntity)
      {
        GeometryEntity geometryEntity = (GeometryEntity)entity;
        satisfies = geometry.intersects(geometryEntity.getGeometry());
      }
    }
    
    return satisfies;
  }
}
