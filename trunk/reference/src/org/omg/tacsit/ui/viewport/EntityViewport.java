/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 2, 2011
 */
package org.omg.tacsit.ui.viewport;

import org.omg.tacsit.controller.SelectionManager;
import org.omg.tacsit.ui.entity.EntityCollectionUI;

/**
 * A Viewport which is capable of displaying and selecting Entities.
 * <p>
 * Implementation note:  This interface definition goes outside an important abstraction level.  Rather than
 * being an EntityCollectionUI, it should be changed to have a set/getEntityRepository.  The interface was originally
 * designed to allow other types of entity stores to use an EntityViewport, but the EntityRepository interface
 * is fairly central to the reference implementation, and thus it is logical for the Viewport to work on the
 * EntityRepository itself.
 * @author Matthew Child
 */
public interface EntityViewport extends ComponentViewport, EntityCollectionUI
{
  /**
   * Sets the selection manager that handles the selection state for the Viewport.
   * @param selectionManager The selection manager that handles entity selection state.
   */
  public void setSelectionManager(SelectionManager selectionManager);
  
  /**
   * Gets the selection manager that handles the selection state for the Viewport.
   * @return The selection manager that handles entity selection state.
   */
  public SelectionManager getSelectionManager();
}
