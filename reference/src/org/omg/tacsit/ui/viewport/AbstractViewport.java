/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Mar 28, 2011
 */

package org.omg.tacsit.ui.viewport;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;
import org.omg.tacsit.controller.ViewEyeProperties;
import org.omg.tacsit.controller.ViewportChangeEvent;
import org.omg.tacsit.controller.ViewportChangeListener;

/**
 * An implementation that provides some of the common default behavior Viewports will use.
 * @author Matthew Child
 */
public abstract class AbstractViewport implements ComponentViewport
{
  private String name;
  private Set<ViewportChangeListener> viewportChangeListeners;
  private PropertyChangeSupport propertyChangeSupport;

  /**
   * Creates a new instance.
   */
  public AbstractViewport()
  {
    viewportChangeListeners = new HashSet();
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  @Override
  public String getName()
  {
    return this.name;
  }

  @Override
  public void setName(String name)
  {
    String oldName = this.name;
    this.name = name;
    propertyChanged(NAME_PROPERTY, oldName, this.name);
  }

  @Override
  public void addViewportChangeListener(ViewportChangeListener listener)
  {
    viewportChangeListeners.add(listener);
  }

  @Override
  public void removeViewportChangeListener(ViewportChangeListener listener)
  {
    viewportChangeListeners.remove(listener);
  }
  
  /**
   * Notifies ViewportChangeListeners that the view eye has changed.
   * @param viewEyeProperties The new view eye properties for the viewport.
   */
  protected void fireViewEyeChanged(ViewEyeProperties viewEyeProperties)
  {
    if(viewportChangeListeners.size() > 0)
    {
      ViewportChangeEvent evt = new ViewportChangeEvent(this, viewEyeProperties);
      for(ViewportChangeListener changeListener : viewportChangeListeners)
      {
        changeListener.viewportChanged(evt);
      }
    }
  }
  
  /**
   * Notifies the PropertyChangeListeners that a property has been updated.
   * @param propertyName The property that was updated.
   * @param oldValue The old value of the property.
   * @param newValue The new value of the property.
   */
  protected void propertyChanged(String propertyName, Object oldValue, Object newValue)
  {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
  }
}
