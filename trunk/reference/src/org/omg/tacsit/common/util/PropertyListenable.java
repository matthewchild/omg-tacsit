/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 1, 2011
 */
package org.omg.tacsit.common.util;

import java.beans.PropertyChangeListener;

/**
 * Fires property changes that client Objects can listen to for notification of state changes.
 * @author Matthew Child
 */
public interface PropertyListenable
{
  /**
   * Adds a new PropertyChangeListener that should receive notification when a change occurs.  The listener
   * will be notified when the value of any property changes.
   * @param listener The listener that should be notified when a property change occurs.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener);
  
  /**
   * Adds a new PropertyChangeListener that should receive notification only when a specific property is changed.
   * @param propertyName The name of the property to receive notification about.
   * @param listener The listener that should be notified when the property changes.
   */
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
  
  /**
   * Removes a listener that was listening of all property changes from being notified about future changes.
   * <p>
   * Note: This will not remove a PropertyChangeListener that was added with the 
   * addPropertyChangeListener(propertyName, listener) method.  Use removePropertyChangeListener(propertyName, 
   * listener) instead.
   * @param listener The listener that should no longer listen to all property changes.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener);
  
  /**
   * Removes a listener that was receiving notification when a specific property was changed.
   * @param propertyName The name of the property that was listened to.
   * @param listener The listener that no longer wants to be notified about property changes.
   */
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
