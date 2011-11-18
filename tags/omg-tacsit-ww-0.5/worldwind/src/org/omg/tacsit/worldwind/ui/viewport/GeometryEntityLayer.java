/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 23, 2011
 */
package org.omg.tacsit.worldwind.ui.viewport;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfaceCircle;
import gov.nasa.worldwind.render.SurfaceQuad;
import gov.nasa.worldwind.render.SurfaceShape;
import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.tacsit.geometry.GeodeticPosition;
import org.omg.tacsit.worldwind.common.layers.AbstractItemLayer;
import org.omg.tacsit.worldwind.entity.GeometryEntity;
import org.omg.tacsit.worldwind.geometry.WWGeodeticPosition;
import org.omg.tacsit.worldwind.geometry.WWSurfaceCircle;
import org.omg.tacsit.worldwind.geometry.WWSurfaceGeometry;
import org.omg.tacsit.worldwind.geometry.WWSurfaceRectangle;

/**
 * A Layer capable of displaying geometry entities.
 * @param <SEL_TYPE> The selection type that can be displayed in the layer.
 * @author Matthew Child
 */
public class GeometryEntityLayer<SEL_TYPE> extends AbstractItemLayer<GeometryEntity, SEL_TYPE, SurfaceShape>
{

  private RenderableLayer renderableLayer;
  private Map<SEL_TYPE, Material> selectionTypeToMaterial;

  /**
   * Creates a new instance.
   */
  public GeometryEntityLayer()
  {
    this.renderableLayer = new RenderableLayer();
    this.selectionTypeToMaterial = new HashMap();
  }

  /**
   * Sets the selection color for a particular selection type.
   * @param selectionType The selection type to change the color of.
   * @param selectionColor The new color for the selection type.
   */
  public void setSelectionColor(SEL_TYPE selectionType, Color selectionColor)
  {
    selectionTypeToMaterial.put(selectionType, new Material(selectionColor));
  }

  /**
   * Gets the selection color for a particular selection type.
   * @param selectionType The selection type to get the color of.
   * @return The color used for that selection type.
   */
  public Color getSelectionColor(SEL_TYPE selectionType)
  {
    Color colorForType = null;

    Material material = getShapeMaterial(selectionType);
    if (material != null)
    {
      colorForType = material.getDiffuse();
    }
    return colorForType;
  }

  private Material getShapeMaterial(SEL_TYPE selectionType)
  {
    Material shapeMaterial = selectionTypeToMaterial.get(selectionType);
    return shapeMaterial;
  }

  private void reloadShapeMaterial(GeometryEntity item)
  {
    SurfaceShape representation = getRepresentation(item);
    if (representation != null)
    {
      SEL_TYPE selectionType = getDisplaySelectionType(item);
      Material shapeMaterial = getShapeMaterial(selectionType);
      if (shapeMaterial != null)
      {
        ShapeAttributes shapeAttributes = representation.getAttributes();
        if (shapeAttributes == null)
        {
          shapeAttributes = new BasicShapeAttributes();
        }
        shapeAttributes.setInteriorMaterial(shapeMaterial);
        shapeAttributes.setOutlineMaterial(shapeMaterial);
        representation.setAttributes(shapeAttributes);
      }
      else
      {
        // For a surface shape, the default attributes are initially null.  This restores them to their
        // default display method.
        representation.setAttributes(null);
      }
    }
    else
    {
      Logger.getLogger(GeometryEntityLayer.class.getName()).log(Level.FINE,
                                                                "Attempted to update an entity representation which does not exist.");
    }
  }

  private void reloadAllShapeMaterials()
  {
    for (GeometryEntity item : getItems())
    {
      reloadShapeMaterial(item);
    }
  }

  @Override
  public void addItem(GeometryEntity item)
  {
    super.addItem(item);
    // If the item was already added to selection
    if (isSelected(item))
    {
      reloadShapeMaterial(item);
    }
  }

  @Override
  public void addItemToSelection(GeometryEntity item, SEL_TYPE selectionType)
  {
    super.addItemToSelection(item, selectionType);
    if (containsItem(item))
    {
      reloadShapeMaterial(item);
    }
  }

  @Override
  public void clearSelectionType(SEL_TYPE selectionType)
  {
    super.clearSelectionType(selectionType);
    reloadAllShapeMaterials();
  }

  @Override
  public void clearItemSelection(GeometryEntity item)
  {
    super.clearItemSelection(item);
    reloadShapeMaterial(item);
  }

  @Override
  protected SurfaceShape newRepresentation(GeometryEntity item)
  {
    WWSurfaceGeometry itemGeometry = item.getGeometry();
    SurfaceShape representation = toSurfaceShape(itemGeometry);
    return representation;
  }

  /**
   * Imports the attributes from a Geometry object into a displayable representation.
   * @param source The source geometry object.
   * @param target The target displayable representation.
   */
  protected void importCircleAttributes(WWSurfaceCircle source, SurfaceCircle target)
  {
    GeodeticPosition center = source.getCenter();
    Position wwPosition = WWGeodeticPosition.toWWPosition(center);
    target.setCenter(wwPosition);

    double radius = source.getRadius();
    target.setRadius(radius);
  }

  /**
   * Imports the attributes from a Geometry object into a displayable representation.
   * @param source The source geometry object.
   * @param target The target displayable representation.
   */
  protected void importRectangleAttributes(WWSurfaceRectangle source, SurfaceQuad target)
  {
    GeodeticPosition center = source.getCenter();
    Position wwPosition = WWGeodeticPosition.toWWPosition(center);
    target.setCenter(wwPosition);

    double width = source.getWidth();
    target.setWidth(width);

    double height = source.getHeight();
    target.setHeight(height);

    double orientation = source.getOrientation();
    Angle orientationAngle = Angle.fromRadians(orientation);
    target.setHeading(orientationAngle);
  }

  /**
   * Converts a Geometry object into an equivalent displayable representation.
   * @param geometry The geometry to convert to a displayable representation.
   * @return A shape displayable by Worldwind's geometry layer.
   */
  protected SurfaceShape toSurfaceShape(WWSurfaceGeometry geometry)
  {
    if (geometry instanceof WWSurfaceCircle)
    {
      SurfaceCircle circle = new SurfaceCircle();
      importCircleAttributes((WWSurfaceCircle) geometry, circle);
      return circle;
    }
    else if (geometry instanceof WWSurfaceRectangle)
    {
      SurfaceQuad rectangle = new SurfaceQuad();
      importRectangleAttributes((WWSurfaceRectangle) geometry, rectangle);
      return rectangle;
    }
    else
    {
      return null;
    }
  }

  @Override
  protected void addToLayer(SurfaceShape representation)
  {
    if (representation != null)
    {
      this.renderableLayer.addRenderable(representation);
    }
  }

  @Override
  protected void removeFromLayer(SurfaceShape representation)
  {
    if (representation != null)
    {
      renderableLayer.removeRenderable(representation);
    }
  }

  @Override
  protected void updateDisplayAttributes(GeometryEntity item, SurfaceShape representation)
  {
    WWSurfaceGeometry geometry = item.getGeometry();

    // This should be factored into an interface.  If it were, it would remove the dependencies to tacsit implementation
    // items, allowing the classes to be useful in a more general purpose.
    if (geometry instanceof WWSurfaceCircle)
    {
      if (representation instanceof SurfaceCircle)
      {
        SurfaceCircle circle = (SurfaceCircle) representation;
        importCircleAttributes((WWSurfaceCircle) geometry, circle);
      }
      else
      {
        SurfaceShape shape = toSurfaceShape(geometry);
        setRepresentation(item, shape);
      }

    }
    else if (geometry instanceof WWSurfaceRectangle)
    {
      if (representation instanceof SurfaceQuad)
      {
        SurfaceQuad rectangle = (SurfaceQuad) representation;
        importRectangleAttributes((WWSurfaceRectangle) geometry, rectangle);
      }
      else
      {
        SurfaceShape shape = toSurfaceShape(geometry);
        setRepresentation(item, shape);
      }
    }
    else
    {
      Object[] messageArgs = new Object[]
      {
        item, geometry
      };
      Logger.getLogger(GeometryEntityLayer.class.getName()).log(Level.INFO,
                                                                "Unable to update representation for item {0} with geometry {1}; no mapping is defined.",
                                                                messageArgs);
    }
  }

  @Override
  protected void clearRepresentationsFromLayer()
  {
    renderableLayer.removeAllRenderables();
  }

  @Override
  public void pick(DrawContext dc, Point point)
  {
    renderableLayer.pick(dc, point);
  }

  @Override
  public void preRender(DrawContext dc)
  {
    renderableLayer.preRender(dc);
  }

  /**
   * Draws the Geometry Entities onto the draw context
   * @param dc The draw context to draw the entities on.
   */
  @Override
  protected void doRender(DrawContext dc)
  {
    renderableLayer.render(dc);
  }
}
