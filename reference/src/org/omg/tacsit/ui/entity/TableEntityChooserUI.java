/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 21, 2011
 */
package org.omg.tacsit.ui.entity;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.omg.tacsit.common.math.Angle;
import org.omg.tacsit.common.ui.table.ListTableModel;
import org.omg.tacsit.common.ui.table.NarrowingListTableModel;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.controller.EntityType;
import org.omg.tacsit.query.EntityQuery;
import org.omg.tacsit.query.DefaultEntityTypeQuery;
import org.omg.tacsit.ui.TableDefaults;

/**
 * An EntityChooser that displays its Entities in a JTable.
 * <p>
 * This will only display entities of a kind that are permitted by its underlying model.
 * @author Matthew Child
 */
public class TableEntityChooserUI implements EntityChooserUI
{
  private ListTableModel<Entity> tableModel;
  
  private EntityQuery entityQuery;
  
  private JPanel componentPanel;
  
  private JTable table;
  
  /**
   * Creates a new instance.
   * @param model The table model to use that can display a specific entity type.
   * @param entityType The EntityType that should be displayed in the table.
   */
  public TableEntityChooserUI(ListTableModel<? extends Entity> model, EntityType entityType)
  {
    this(model, new DefaultEntityTypeQuery(entityType));
  }
  
  /**
   * Creates a new instance.
   * @param model The table model to use that can display a specific entity type.
   * @param query A query that filters which entities are inserted into the table.
   */
  public TableEntityChooserUI(ListTableModel<? extends Entity> model, EntityQuery query)
  {
    if (model == null)
    {
      throw new IllegalArgumentException("model may not be null");
    }
    this.tableModel = new NarrowingListTableModel(Entity.class, model);
    this.entityQuery = query;
    initGUI(this.tableModel);
  }
   
  private void initGUI(TableModel model)
  {
    componentPanel = new JPanel(new BorderLayout());
    componentPanel.setName("Entity Table");
    JComponent tableComponent = initTableComponent(model);
    componentPanel.add(tableComponent, BorderLayout.CENTER);
  }  
  
  private JComponent initTableComponent(TableModel model)
  {
    table = new JTable(model);
    TableDefaults.initializeEditorAndRendererForTable(table, Angle.class);
    JScrollPane tableScroll = new JScrollPane(table);
    return tableScroll;
  }
  
  private boolean passesFilter(Entity entity)
  {
    boolean passes = true;
    if(entityQuery != null)
    {
      passes = entityQuery.satifies(entity);
    }
    return passes;
  }
  
  /**
   * Gets the name of the EntityChooser
   * @return The name of the EntityChooser.
   */
  public String getName()
  {
    return getComponent().getName();
  }
  
  /**
   * Sets the name of the EntityChooser
   * @param name The new name of the EntityChooser 
   */
  public void setName(String name)
  {
    getComponent().setName(name);
  }
  
  /**
   * Sets the default editor for a particular Class of data.
   * @param dataClass The class of data to edit
   * @param editor The editor to use to edit that type of data.
   */
  public void setDefaultTableCellEditor(Class dataClass, TableCellEditor editor)
  {
    table.setDefaultEditor(dataClass, editor);
  }
  
  /**
   * Sets the default renderer for a particular Class of data.
   * @param dataClass The class of data to render
   * @param renderer The renderer to use to show that type of data.
   */
  public void setDefaultTableCellRenderer(Class dataClass, TableCellRenderer renderer)
  {
    table.setDefaultRenderer(dataClass, renderer);
  }
  
  public void addListSelectionListener(ListSelectionListener listener)
  {
    this.table.getSelectionModel().addListSelectionListener(listener);
  }

  public void removeListSelectionListener(ListSelectionListener listener)
  {
    this.table.getSelectionModel().removeListSelectionListener(listener);
  }
  
  public Collection<Entity> getSelectedEntities()
  {
    int[] selectedViewRows = this.table.getSelectedRows();
    return getEntitiesAtViewRows(selectedViewRows);
  }
  
  /**
   * Gets the Entity at a particular model row in the table.
   * @param modelRow The row of the Entity
   * @return The Entity at that row.
   */
  public Entity getEntityAtModelRow(int modelRow)
  {
    return tableModel.getRow(modelRow);
  }
  
  private Collection<Entity> getEntitiesAtViewRows(int[] viewRowIndices)
  {
    Collection<Entity> entities = new ArrayList();
    for (int viewRowIndex : viewRowIndices)
    {
      Entity entity = getEntityAtViewRow(viewRowIndex);
      entities.add(entity);
    }
    return entities;
  }
  
  private Entity getEntityAtViewRow(int viewRowIndex)
  {
    int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
    return this.tableModel.getRow(modelRowIndex);
  }

  public void clearEntities()
  {
    tableModel.clear();
  }

  public boolean isEntityAllowed(Entity entity)
  {
    return passesFilter(entity);
  }
  
  private void validityCheck(Entity entity)
  {
    if(!passesFilter(entity))
    {
      throw new IllegalArgumentException("entity " + entity + " cannot be viewed by viewer " + this);
    }
  }

  public void addEntity(Entity entity)
  {
    validityCheck(entity);
    tableModel.add(entity);
  }

  public void removeEntity(Entity entity)
  {
    validityCheck(entity);
    tableModel.remove(entity);
  }
  
  public void updateEntity(Entity entity)
  {
    validityCheck(entity);
    tableModel.rowChanged(entity);
  }

  public Component getComponent()
  {
    return componentPanel;
  }
}
