/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 27, 2011
 */
package org.omg.tacsit.common.ui.table;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import org.omg.tacsit.common.ui.ConfigurableAction;

/**
 * An Action that clears an ObjectTableModel of all its contents.
 * @param <O> The type of element contained in the table model.
 * @author Matthew Child
 */
public class ClearTableAction<O> extends ConfigurableAction
{
  private ListTableModel<O> tableModel;

  /**
   * Creates a new instance.
   * @param name The name of the action.
   * @param icon The icon for the action.
   */
  public ClearTableAction(String name, Icon icon)
  {
    super(name, icon);
  }

  /**
   * Gets the table model to be cleared when the action is performed.
   * @return The table model to clear.
   */
  public ListTableModel<O> getTableModel()
  {
    return tableModel;
  }

  /**
   * Sets the table model to be cleared when the action is performed.
   * @param tableModel The table model to clear.
   */
  public void setTableModel(ListTableModel<O> tableModel)
  {
    this.tableModel = tableModel;
    checkEnabledState();
  }

  @Override
  public boolean isPerformable()
  {
    return (tableModel != null);
  }
  
  public void actionPerformed(ActionEvent e)
  {
    tableModel.clear();
  }
}
