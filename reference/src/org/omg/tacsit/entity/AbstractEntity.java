/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 16, 2011
 */
package org.omg.tacsit.entity;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.controller.EntityType;
import org.omg.tacsit.common.util.ObjectUtils;
import org.omg.tacsit.common.util.PropertyListenable;

/**
 * An Entity which provides the standard services most implementations will need.
 * @author Matthew Child
 */
public abstract class AbstractEntity implements PollableEntity, PropertyListenable, PositionedEntity
{
  /**
   * The property event fired when the validity of the entity has changed.
   */
  public static final String PROPERTY_VALID = "valid";
  
  /**
   * The property event fired when the reference position has changed..
   */
  public static final String PROPERTY_REFERENCE_POSITION = "referencePosition";
    
  private long lastModified;
  
  private EntityType entityType;
  private boolean valid;
  
  private PropertyChangeSupport changeSupport;

  /**
   * Creates a new instnace.
   * @param entityType The type of Entity this is.
   */
  public AbstractEntity(EntityType entityType)
  {
    this.entityType = entityType;
    lastModified = 0;
    valid = true;
  }  

  public EntityType getType()
  {
    return entityType;
  }
  
  private PropertyChangeSupport lazyGetChangeSupport()
  {
    if(changeSupport == null)
    {
      changeSupport = new PropertyChangeSupport(this);
    }
    return changeSupport;
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    PropertyChangeSupport support = lazyGetChangeSupport();
    support.addPropertyChangeListener(propertyName, listener);
  }

  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    if(changeSupport != null)
    {
      changeSupport.removePropertyChangeListener(propertyName, listener);
    }
  }
  
  public void addPropertyChangeListener(PropertyChangeListener l)
  {
    PropertyChangeSupport support = lazyGetChangeSupport();
    support.addPropertyChangeListener(l);
  }
  
  public void removePropertyChangeListener(PropertyChangeListener l)
  {
    if(changeSupport != null)
    {
      changeSupport.removePropertyChangeListener(l);
    }
  }
    
  public boolean equals(Entity entity)
  {    
    if(!(entity instanceof AbstractEntity))
    {
      return false;
    }
    
    AbstractEntity otherEntity = (AbstractEntity)entity;
    
    boolean typesEqual = ObjectUtils.areEqual(otherEntity.getType(), getType());    
    boolean validityEqual = otherEntity.isValid() && isValid();    
    boolean lastModifiedEqual = (otherEntity.getLastModified() == getLastModified());
    boolean referencePositionsEqual = ObjectUtils.areEqual(otherEntity.getReferencePosition(), getReferencePosition());
    boolean pointEntityEquality = otherEntity.isPointEntity() == isPointEntity();
    return typesEqual && validityEqual && lastModifiedEqual && referencePositionsEqual && pointEntityEquality;
  }
  
  private void markModifiedTime()
  {
    lastModified = System.currentTimeMillis();
  }
  
  /**
   * Fires a property change to all associated listeners, and marks the entity as having been modified.
   * @param property The name of the property that was changed.
   * @param oldValue The old value of the property.
   * @param newValue The new value of the property.
   */
  protected void propertyChanged(String property, Object oldValue, Object newValue)
  {
    markModifiedTime();
    if(changeSupport != null)
    {
      changeSupport.firePropertyChange(property, oldValue, newValue);
    }
  }  
  
  /**
   * Sets whether or not the entity is valid.  Invalid entities should not be displayed or used.
   * @param valid true if the entity is valid, or false otherwise.
   */
  public void setValid(boolean valid)
  {
    boolean oldValue = this.valid;
    this.valid = valid;
    propertyChanged(PROPERTY_VALID, oldValue, this.valid);
  }

  public final boolean isValid()
  {
    return valid;
  }

  public long getLastModified()
  {
    return lastModified;
  }
}
