/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 11, 2011
 */
package org.omg.tacsit.worldwind.entity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.entity.AbstractEntity;
import org.omg.tacsit.geometry.GeodeticPosition;
import org.omg.tacsit.geometry.Geometry;
import org.omg.tacsit.worldwind.geometry.WWSurfaceGeometry;

/**
 * An entity which consists of an area on the surface of the globe.
 * @author Matthew Child
 */
public class GeometryEntity extends AbstractEntity
{

  private static final String PROPERTY_GEOMETRY = "geometry";
  private WWSurfaceGeometry geometry;
  private String name;
  private PropertyChangeListener geometryPropertyListener;

  /**
   * Creates a new instance.
   */
  public GeometryEntity()
  {
    super(WWEntityType.SURFACE_GEOMETRY);
    geometryPropertyListener = new GeometryPropertyListener();
  }

  /**
   * Gets the geometry represented by this entity.
   * @return The geometry of this entity.
   */
  public WWSurfaceGeometry getGeometry()
  {
    return geometry;
  }

  /**
   * Sets the geometry represented by this entity.
   * @param geometry The new geometry of this entity.
   */
  public void setGeometry(WWSurfaceGeometry geometry)
  {
    if (this.geometry != null)
    {
      this.geometry.removePropertyChangeListener(geometryPropertyListener);
    }
    Geometry oldGeometry = this.geometry;
    this.geometry = geometry;
    if (this.geometry != null)
    {
      this.geometry.addPropertyChangeListener(geometryPropertyListener);
    }
    propertyChanged(PROPERTY_GEOMETRY, oldGeometry, geometry);
  }

  /**
   * Checks to see if the entity contains a surface geometry.
   * @param geometry The surface geometry to see if it contains.
   * @return true if this entity contains the geometry, false otherwise.
   */
  public boolean contains(WWSurfaceGeometry geometry)
  {
    boolean contains = false;
    if (this.geometry != null)
    {
      contains = this.geometry.contains(geometry);
    }
    return contains;
  }

  /**
   * Checks to see if an entity intersects a surface geometry.
   * @param geometry The surface geometry to see if it intersects with
   * @return true if this entity intersects with the geometry, false otherwise.
   */
  public boolean intersects(WWSurfaceGeometry geometry)
  {
    boolean contains = false;
    if (this.geometry != null)
    {
      contains = this.geometry.intersects(geometry);
    }
    return contains;
  }

  @Override
  public boolean equals(Entity entity)
  {
    if (!super.equals(entity) || !(entity instanceof GeometryEntity))
    {
      return false;
    }

    GeometryEntity other = (GeometryEntity) entity;

    WWSurfaceGeometry otherGeometry = other.getGeometry();
    boolean geometryEqual = this.geometry == null ? (otherGeometry == null)
                            : otherGeometry.equals(geometry);

    String otherName = other.getName();
    boolean namesEqual = (name == null) ? otherName == null
                                        : name.equals(otherName);
    return geometryEqual && namesEqual;
  }

  public boolean isPointEntity()
  {
    return false;
  }

  /**
   * Gets the name of the entity.
   * @return 
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the name of the entity.
   * @param name The new name of the entity.
   */
  public void setName(String name)
  {
    String oldName = this.name;
    this.name = name;
    propertyChanged("name", oldName, this.name);
  }

  public GeodeticPosition getReferencePosition()
  {
    if(this.geometry != null)
    {
      return this.geometry.getReferencePosition();
    }
    else
    {
      return null;
    }
  }

  private class GeometryPropertyListener implements PropertyChangeListener
  {

    public void propertyChange(PropertyChangeEvent evt)
    {
      propertyChanged(PROPERTY_GEOMETRY, null, geometry);
    }
  }
}
