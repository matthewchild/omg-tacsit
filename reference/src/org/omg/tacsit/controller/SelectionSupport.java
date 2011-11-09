/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 9, 2011
 */
package org.omg.tacsit.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class for implementing the SelectionManager interface.  Client classes should declare a
 * SelectionSupport member variable, and delegate Add/Remove listener methods to it.
 * 
 * @author Matthew Child
 */
public class SelectionSupport
{
  private SelectionManager source;
  
  private Set<SelectionListener> selectionListeners;

  /**
   * Creates a new instance.
   * @param source The SelectionManager that will be the source of the SelectionEvents that are fired.
   */
  public SelectionSupport(SelectionManager source)
  {
    if (source == null)
    {        
      throw new IllegalArgumentException("source may not be null");
    }
    this.source = source;
    selectionListeners = new HashSet();
  }
  
  /**
   * Notifies all associated listeners that a single Entity is now the new Selection for a particular selectionType.
   * @param entity The entity that is now selected.
   * @param selectionType The type of selection
   */
  public void fireSelectionChanged(Entity entity, SelectionType selectionType)
  {
    List<Entity> entityAsList = Collections.singletonList(entity);
    fireSelectionChanged(entityAsList, selectionType);
  }
  
  /**
   * Notifies all associated listeners that a List of Entities is now the new Selection for a particular selectionType.
   * @param entities The entities that are now selected.
   * @param selectionType The type of selection
   */
  public void fireSelectionChanged(List<Entity> entities, SelectionType selectionType)
  {
    SelectionEvent selectionEvent = new SelectionEvent(entities, source, selectionType);
    for(SelectionListener selectionListener : selectionListeners)
    {
      selectionListener.selectionChanged(selectionEvent);
    }
  }
  
  /**
   * Notifies all listeners that the selection has been cleared for a particular SelectionType.
   * @param selectionType The SelectionType that has been cleared.
   */
  public void fireSelectionCleared(SelectionType selectionType)
  {
    SelectionEvent selectionEvent = new SelectionEvent(null, source, selectionType);
    for(SelectionListener selectionListener : selectionListeners)
    {
      selectionListener.selectionChanged(selectionEvent);
    }
  }
  
  /**
   * Adds a listener that wants notification when SelectionState changes.
   * @param listener The listener that wants to be notified.
   */
  public void addSelectionListener(SelectionListener listener)
  {
    selectionListeners.add(listener);
  }
  
  /**
   * Removes a listener that no longer wants notification when SelectionState changes.
   * @param listener The listener that no longer wants to be notified.
   */
  public void removeSelectionListener(SelectionListener listener)
  {
    selectionListeners.remove(listener);
  }
}
