/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 31, 2011
 */
package org.omg.tacsit.query;

import org.omg.tacsit.controller.Entity;

/**
 * A Query that returns all entities which are instances of a particular class.  This is especially useful if you are 
 * trying to get all entities of a particular type that you know how to display.
 * <p>
 * If the matchingClass is not set, all Entities will be returned.
 * @author Matthew Child
 */
public class InstanceOfQuery implements EntityQuery
{
  private Class<? extends Entity> matchingClass;

  /**
   * Creates a new instance.
   */
  public InstanceOfQuery()
  {
  }

  /**
   * Creates a new instance.
   * @param matchingClass The class that Entities must be an instance of to pass the query.
   */
  public InstanceOfQuery(Class<? extends Entity> matchingClass)
  {    
    this.matchingClass = matchingClass;
  }

  /**
   * Gets the class that Entities must be an instance of to pass the query.
   * @return The matching class.
   */
  public Class<? extends Entity> getMatchingClass()
  {
    return matchingClass;
  }

  /**
   * Sets the class that Entities must be an instance of to pass the query.
   * @param matchingClass The matching class.
   */
  public void setMatchingClass(Class<? extends Entity> matchingClass)
  {    
    this.matchingClass = matchingClass;
  }

  public boolean satifies(Entity entity)
  {
    if(matchingClass == null)
    {
      return true;
    }
    else
    {
      return matchingClass.isInstance(entity);
    }
  }
  
}
