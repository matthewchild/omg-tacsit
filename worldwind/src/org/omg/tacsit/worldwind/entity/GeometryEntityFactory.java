/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 26, 2011
 */
package org.omg.tacsit.worldwind.entity;

import org.omg.tacsit.worldwind.geometry.GeometryFactory;
import org.omg.tacsit.worldwind.geometry.WWSurfaceGeometry;
import org.omg.tacsit.worldwind.geometry.WWSurfaceShape;
import org.omg.tacsit.common.util.Factory;

/**
 * A Factory which creates GeometryEntities.
 * @author Matthew Child
 */
public class GeometryEntityFactory implements Factory<GeometryEntity>
{
  private String baseName;
  
  private GeometryFactory geometryFactory;
  private int creationCount;  
  private WWSurfaceShape defaultShape;

  /**
   * Creates a new instance.
   * @param baseName The base name that new objects will be have.
   * @param geometryFactory The factory that will supply new entities will create the default geometry.
   * @param defaultShape The default shape new geometry entities will have.
   */
  public GeometryEntityFactory(String baseName, GeometryFactory geometryFactory, WWSurfaceShape defaultShape)
  {
    creationCount = 0;
    this.baseName = baseName;
    this.geometryFactory = geometryFactory;
    this.defaultShape = defaultShape;
  }
  
  private WWSurfaceGeometry createDefaultGeometry()
  {
    return geometryFactory.createGeometry(defaultShape);
  }
  
  private String getNewObjectName()
  { 
    creationCount++;
    return baseName + " " + creationCount;
  }

  public GeometryEntity createObject()
  {    
    GeometryEntity geometryEntity = new GeometryEntity();   
    
    String name = getNewObjectName();
    geometryEntity.setName(name);
    
    WWSurfaceGeometry defaultGeometry = createDefaultGeometry();
    geometryEntity.setGeometry(defaultGeometry);
    
    return geometryEntity;
  }
}
