/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 12, 2011
 */
package org.omg.tacsit.worldwind.ui.viewport;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwind.view.orbit.FlatOrbitView;
import org.omg.tacsit.controller.Projection;

/**
 * An enumeration consisting of the valid projection types for a Worldwind Viewport.
 * @author Matthew Child
 */
public enum WorldwindProjection implements Projection
{
  /**
   * The Basic Orbit View.
   */
  BASIC_ORBIT_VIEW("Basic Orbit View", BasicOrbitView.class),
  
  /**
   * The Flat Orbit View.
   */
  FLAT_ORBIT_VIEW("Flat Orbit View", FlatOrbitView.class);
    
  private String name;
  private Class<? extends View> viewClass;
  
  /**
   * Creates a new instance.
   * @param name The displayable name of the projection.
   * @param viewClass The Worldwind View class represented by this projection.
   */
  WorldwindProjection(String name, Class<? extends View> viewClass)
  {
    this.name = name;
    this.viewClass = viewClass;
  }
  
  /**
   * Gets the displayable name of this projection.
   * @return 
   */
  public String getName()
  {
    return name;
  }

  /**
   * Gets the Worldwind View class represented by this projection.
   * @return The Worldwind View class represented by this projection.
   */
  public Class<? extends View> getViewClass()
  {
    return viewClass;
  }
  
  /**
   * Gets the Projection value that represents a particular Worldwind View class.
   * @param viewClass The Worldwind View class to get the Projection value of.
   * @return The Projection value that represents the parameter's viewClass.
   */
  public static WorldwindProjection getProjection(Class<? extends View> viewClass)
  {
    WorldwindProjection projectionForClass = null;
    for(WorldwindProjection projection : values())
    {
      Class<? extends View> projectionClass = projection.getViewClass();
      if(projectionClass.equals(viewClass))
      {
        projectionForClass = projection;
        break;        
      }
    }
    
    if(projectionForClass == null)
    {
      throw new IllegalArgumentException("No WorldwindProjection defined for " + viewClass);
    }
    
    return projectionForClass;
  }
}
