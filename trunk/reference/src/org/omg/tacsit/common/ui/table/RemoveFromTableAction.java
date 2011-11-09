/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 27, 2011
 */
package org.omg.tacsit.common.ui.table;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import org.omg.tacsit.common.ui.ConfigurableAction;
import org.omg.tacsit.common.util.CollectionUtils;

/**
 * An action that removes a set of objects from an ObjectTableModel.
 * @param <O> The type of Object that can be removed from the ListTableModel.
 * @author Matthew Child
 */
public class RemoveFromTableAction<O> extends ConfigurableAction
{
  private ListTableModel<O> tableModel;
  private List<O> objectsToRemove;

  /**
   * Creates a new instance.
   * @param name The name of the action.
   * @param icon The icon to use for the action.
   */
  public RemoveFromTableAction(String name, Icon icon)
  {
    super(name, icon);
    objectsToRemove = Collections.emptyList();
  }

  /**
   * Gets the List of objects that will be removed from the ObjectTableModel when the action is performed.  This is
   * guaranteed not to be null.
   * @return A List of objects that will be removed from the ObjectTableModel.  This List is unmodifiable.  
   */
  public List<O> getObjectsToRemove()
  {
    return objectsToRemove;
  }

  /**
   * Sets the List of objects that will be removed from the ObjectTableModel when the action is performed.  The List
   * is copied, so later changes to the list parameter will not affect which objects this action will remove.
   * @param objectsToRemove The List of objects to remove.
   */
  public void setObjectsToRemove(List<O> objectsToRemove)
  {
    this.objectsToRemove = CollectionUtils.copyToUnmodifiableList(objectsToRemove);
    checkEnabledState();
  }

  /**
   * Gets the table model that will have the objects removed from when the action is performed.
   * @return The ObjectTableModel that will be removed from.
   */
  public ListTableModel<O> getTableModel()
  {
    return tableModel;
  }

  /**
   * Sets the table model that will have the objects removed from when the action is performed.
   * @param tableModel The ObjectTableModel that will be removed from.
   */
  public void setTableModel(ListTableModel<O> tableModel)
  {
    this.tableModel = tableModel;
    checkEnabledState();
  }
  
  private boolean hasObjectsToRemove()
  {
    return !objectsToRemove.isEmpty();
  }

  @Override
  public boolean isPerformable()
  {
    return ((tableModel != null) && hasObjectsToRemove());
  }
  
  public void actionPerformed(ActionEvent e)
  {
    tableModel.removeAll(objectsToRemove);
  }
}
