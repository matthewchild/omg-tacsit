/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 27, 2011
 */
package org.omg.tacsit.common.ui.table;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import org.omg.tacsit.common.ui.ConfigurableAction;
import org.omg.tacsit.common.util.Factory;

/**
 * Adds a new Object to an ObjectTableModel. 
 * @author Matthew Child
 * @param <O> The type of Object to be created.
 */
public class AddToTableAction<O> extends ConfigurableAction
{
  private ListTableModel<O> tableModel;
  private Factory<O> newObjectFactory;

  /**
   * Creates a new instance.
   * @param name The name of the action
   * @param icon The icon to use for the action.
   */
  public AddToTableAction(String name, Icon icon)
  {
    super(name, icon);
  }

  /**
   * The factory which will create new Objects to add to the table.
   * @return The factory that creates new Objects.
   */
  public Factory<O> getNewObjectFactory()
  {
    return newObjectFactory;
  }

  /**
   * Sets the factory which will create new Objects to add to the table.
   * @param newObjectFactory The factory that will create new Objects.
   */
  public void setNewObjectFactory(Factory<O> newObjectFactory)
  {
    this.newObjectFactory = newObjectFactory;
    checkEnabledState();
  }

  /**
   * Gets the table model that new Objects will be added to when the action is performed.
   * @return The table model that new Objects will be added to.
   */
  public ListTableModel<O> getTableModel()
  {
    return tableModel;
  }

  /**
   * Sets the table model that new Objects will be added to when the action is performed.
   * @param tableModel The table model that will create new Objects.
   */
  public void setTableModel(ListTableModel<O> tableModel)
  {
    this.tableModel = tableModel;
    checkEnabledState();
  }

  @Override
  public boolean isPerformable()
  {
    return ((tableModel != null) && (newObjectFactory != null));
  }
  
  public void actionPerformed(ActionEvent e)
  {
    O newObject = newObjectFactory.createObject();
    tableModel.add(newObject);
  }
}
