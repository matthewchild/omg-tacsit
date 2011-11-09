/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 12, 2011
 */
package org.omg.tacsit.worldwind.geometry;

import gov.nasa.worldwind.globes.Globe;

/**
 * An enumeration of surface shapes supported by the Tacsit implementation for Worldwind.
 * @author Matthew Child
 */
public enum WWSurfaceShape
{
  /**
   * A surface rectangle.
   */
  RECTANGLE(WWSurfaceRectangle.class)
  {
    @Override
    public WWSurfaceGeometry createShape(Globe globe)
    {
      return new WWSurfaceRectangle(globe);
    }
  },
  
  /**
   * A surface circle.
   */
  CIRCLE(WWSurfaceCircle.class)
  {
    @Override
    public WWSurfaceGeometry createShape(Globe globe)
    {
      return new WWSurfaceCircle(globe);
    }
  };
  
  private Class<? extends WWSurfaceGeometry> geometryClass;
  
  /**
   * Creates a new instance.
   * @param geometryClass The associated class that meets the WWSurfaceGeometry interface.
   */
  WWSurfaceShape(Class<? extends WWSurfaceGeometry> geometryClass)
  {
    this.geometryClass = geometryClass;
  }

  /**
   * Gets the associated Geometry class that meets the WWSurfaceGeometry interface.
   * @return The geometry class.
   */
  public Class<? extends WWSurfaceGeometry> getGeometryClass()
  {
    return geometryClass;
  }
  
  /**
   * Creates a new shape based on the enumeration's value.
   * @param globe The globe to use for calculating information about the shape.
   * @return A new WWSurfaceGeometry of the type described by the enumeration value.
   */
  public abstract WWSurfaceGeometry createShape(Globe globe);
  
  /**
   * Gets the WWSurfaceShape for a particular class of WWSurfaceGeometry.
   * @param geometryClass The class to get the shape enumeration value of.
   * @return The surface shape that describes that geometry class.
   */
  public static WWSurfaceShape forClass(Class<? extends WWSurfaceGeometry> geometryClass)
  {
    if(geometryClass == null)
    {
      throw new IllegalArgumentException("geometryClass may not be null");
    }
    WWSurfaceShape matchingShape = null;
    for(WWSurfaceShape shape : WWSurfaceShape.values())
    {
      if(geometryClass.equals(shape.getGeometryClass()))
      {
        matchingShape = shape;
        break;
      }
    }
    
    if(matchingShape == null)
    {
      throw new IllegalArgumentException("geometryClass " + geometryClass + " has no matching WWShape");
    }
    return matchingShape;
  }
}
