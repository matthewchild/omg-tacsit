/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 8, 2011
 */
package org.omg.tacsit.worldwind.query;

import org.omg.tacsit.query.GeometryQuery;
import org.omg.tacsit.worldwind.geometry.WWSurfaceGeometry;

/**
 * A query which has Geometry associated with it.
 * <p>
 * This provides an abstract implementation of the Tacsit standard's GeometryQuery interface.
 * @author Matthew Child
 */
public abstract class WWGeometryQuery implements GeometryQuery
{
  private WWSurfaceGeometry geometry;

  /**
   * Creates a new instance.
   */
  public WWGeometryQuery()
  {
  }

  /**
   * Creates a new instance.
   * @param geometry The geometry that will be used for query calculations.
   */
  public WWGeometryQuery(WWSurfaceGeometry geometry)
  {
    this.geometry = geometry;
  }
  
  /**
   * Sets the geometry that will be used for query calculations.
   * @param geometry The geometry that will be used for query calculations.
   */
  public void setGeometry(WWSurfaceGeometry geometry)
  {
    this.geometry = geometry;
  }

  public WWSurfaceGeometry getGeometry()
  {
    return geometry;
  }
}
