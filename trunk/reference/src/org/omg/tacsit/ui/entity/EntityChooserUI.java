/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 22, 2011
 */
package org.omg.tacsit.ui.entity;

import java.util.Collection;
import javax.swing.event.ListSelectionListener;
import org.omg.tacsit.common.ui.UIElement;
import org.omg.tacsit.controller.Entity;

/**
 * An Entity list component which allows the viewed Entities to be selected and chosen.
 * <p>
 * The immediate requirement was satisfied using a List model.  However, it might be advantageous
 * to use the SelectionManager interface to generalize this interface.  This would allow the interface to
 * also describe Components with other types of selection models, like a Tree.
 * 
 * @author Matthew Child
 */
public interface EntityChooserUI extends UIElement, EntityCollectionUI
{
  /**
   * Gets the entities that are currently selected in the EntityChooser.
   * @return The currently selected entities.
   */
  public Collection<Entity> getSelectedEntities();
  
  /**
   * Adds a listener that wants to receive notification when the selection changes.
   * @param listener The listener that wants notification.
   */
  public void addListSelectionListener(ListSelectionListener listener);
  
  /**
   * Removes a listener that no longer wants to receive notification when the selection changes.
   * @param listener The listener that no longer wants notification.
   */
  public void removeListSelectionListener(ListSelectionListener listener);
}
