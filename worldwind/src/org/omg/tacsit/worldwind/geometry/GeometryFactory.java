/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 11, 2011
 */
package org.omg.tacsit.worldwind.geometry;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.Globe;

/**
 * A Factory that creates new shapes that meet the Tacsit standard.
 * <p>
 * All shapes created by the factory share the same Globe.
 * @author Matthew Child
 */
public class GeometryFactory
{
  private static final Globe DEFAULT_GLOBE = new Earth();
  
  private Globe globe;
  
  /**
   * Creates a new instance.
   */
  public GeometryFactory()
  {
    this(DEFAULT_GLOBE);
  }

  /**
   * Creates a new instance.
   * @param globe The globe that new shapes use to calculate functions that are a part of the WWSurfaceGeometry
   * interface.
   */
  public GeometryFactory(Globe globe)
  {
    if(globe == null)
    {
      throw new IllegalArgumentException("globe may not be null");
    }
    this.globe = globe;
  }
  
  private WWSurfaceGeometry newInstance(WWSurfaceShape shape)
  {
    return shape.createShape(globe);
  }
  
  /**
   * Creates a new rectangle.  The rectangle will be populated with some default values.
   * @return A new rectangle.
   */
  public WWSurfaceRectangle createRectangle()
  {
    WWSurfaceRectangle rectangle = (WWSurfaceRectangle)newInstance(WWSurfaceShape.RECTANGLE);
    rectangle.setWidth(1000000);
    rectangle.setHeight(1000000);
    rectangle.setCenter(Position.ZERO);    
    return rectangle;
  }
  
  /**
   * Creates a new Circle.  The circle will be populate with some default values.
   * @return A new Circle.
   */
  public WWSurfaceCircle createCircle()
  {
    WWSurfaceCircle circle = (WWSurfaceCircle)newInstance(WWSurfaceShape.CIRCLE);
    circle.setRadius(1000000);
    circle.setCenter(Position.ZERO);
    return circle;
  }
  
  /**
   * Creates a geometry for a specific type of SurfaceShape.
   * @param shape The shape to create
   * @return A new WWSurfaceGeometry of the shape type.
   */
  public WWSurfaceGeometry createGeometry(WWSurfaceShape shape)
  {
    WWSurfaceGeometry geometry;
    switch(shape)
    {
      case CIRCLE:
        geometry = createCircle();
        break;
      case RECTANGLE:
        geometry = createRectangle();
        break;
      default:
        throw new IllegalArgumentException("Can't handle shape " + shape);
    }
    
    return geometry;
  }
}
