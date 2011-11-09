/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 3, 2011
 */
package org.omg.tacsit.ui.entity;

import java.awt.Component;
import java.util.Collection;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.omg.tacsit.common.ui.EditorComponent;
import org.omg.tacsit.common.ui.table.ListTableModel;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.controller.EntityType;
import org.omg.tacsit.entity.EntityUtils;

/**
 * An EntityChooser that displays entities in a Table, but has a detail panel for showing more complete data.
 * <p>
 * This type of EntityChooser is useful if you are displaying data that has some attributes in common, but some
 * attributes that differ.  This type of data is awkward to fully display in a table, because it invariably leads
 * to non-editable empty columns.
 * <p>
 * By joining a table and a property editor, this enable editing of common information in the table, and specific
 * information editing capabilities when an entry in that table is selected.
 * @author Matthew Child
 */
public class TableWithDetailsEntityChooserUI implements EntityChooserUI
{
  private JSplitPane tableEditorSplit;
  private EditorComponent selectedComponentEditor;
  
  private TableEntityChooserUI tableViewer;
      
  private Collection<Entity> editedEntities;
  
  /**
   * Creates a new instance
   * @param tableModel The tableModel that will display the entities.
   * @param allowedEntityType The EntityType that is permitted to be viewed.
   */
  public TableWithDetailsEntityChooserUI(ListTableModel<? extends Entity> tableModel, EntityType allowedEntityType)
  {
    this.tableViewer = new TableEntityChooserUI(tableModel, allowedEntityType);
    
    TableUpdateListener updateListener = new TableUpdateListener();
    tableModel.addTableModelListener(updateListener);
    tableViewer.addListSelectionListener(updateListener);
    
    initGUI();
  }
  
  private void initGUI()
  {
    tableEditorSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    tableEditorSplit.setName("Detail Table");
    tableEditorSplit.setDividerLocation(200);
    tableEditorSplit.setLeftComponent(tableViewer.getComponent());
  }
  
  /**
   * Sets the default editor for a particular Class of data.
   * @param dataClass The class of data to edit
   * @param editor The editor to use to edit that type of data.
   */
  public void setDefaultTableCellEditor(Class dataClass, TableCellEditor editor)
  {
    tableViewer.setDefaultTableCellEditor(dataClass, editor);
  }
  
  /**
   * Sets the default renderer for a particular Class of data.
   * @param dataClass The class of data to render
   * @param renderer The renderer to use to show that type of data.
   */
  public void setDefaultTableCellRenderer(Class dataClass, TableCellRenderer renderer)
  {
    tableViewer.setDefaultTableCellRenderer(dataClass, renderer);
  }

  /**
   * Gets the editor that's used for values that are selected in the table.
   * @return The editor component.
   */
  public EditorComponent getSelectedComponentEditor()
  {
    return selectedComponentEditor;
  }

  /**
   * Sets the editor that's used for values that are selected in the table.
   * @param selectedComponentEditor The editor component to use.
   */
  public void setSelectedComponentEditor(EditorComponent selectedComponentEditor)
  {
    if(this.selectedComponentEditor != null)
    {
      selectedComponentEditor.setValue(null);
      tableEditorSplit.remove(this.selectedComponentEditor.getComponent());
    }
    this.selectedComponentEditor = selectedComponentEditor;
    if(this.selectedComponentEditor != null)
    {
      Collection<Entity> entities = getEditedEntities();
      this.selectedComponentEditor.setValue(entities);
      tableEditorSplit.setRightComponent(this.selectedComponentEditor.getComponent());
    }
    tableEditorSplit.invalidate();
    tableEditorSplit.validate();
    tableEditorSplit.repaint();
  }
  
  /**
   * Gets the name of the EntityChooser.
   * @return The current name of the entity chooser
   */
  public String getName()
  {
    return getComponent().getName();
  }
  
  /**
   * Sets the name of the EntityChooser
   * @param name The current name of the entity chooser
   */
  public void setName(String name)
  {
    getComponent().setName(name);
  }

  public Component getComponent()
  {
    return tableEditorSplit;
  }  
    
  private void setEditedEntities(Collection<Entity> entities)
  {
    this.editedEntities = entities;
    if(this.selectedComponentEditor != null)
    {
      this.selectedComponentEditor.setValue(editedEntities);
    }
  }
  
  private Collection<Entity> getEditedEntities()
  {
    return editedEntities;
  }
  
  private Collection<Entity> getTableSelection()
  {
    return tableViewer.getSelectedEntities();
  }
  
  private Entity getEntityAtModelRow(int modelRow)
  {
    return tableViewer.getEntityAtModelRow(modelRow);
  }

  public void addEntity(Entity entity)
  {
    tableViewer.addEntity(entity);
  }

  public void removeEntity(Entity entity)
  {
    tableViewer.removeEntity(entity);
  }

  public void updateEntity(Entity entity)
  {
    tableViewer.updateEntity(entity);
  }

  public void clearEntities()
  {
    tableViewer.clearEntities();
  }

  public boolean isEntityAllowed(Entity entity)
  {
    return tableViewer.isEntityAllowed(entity);
  }

  public Collection<Entity> getSelectedEntities()
  {
    return tableViewer.getSelectedEntities();
  }

  public void addListSelectionListener(ListSelectionListener listener)
  {
    tableViewer.addListSelectionListener(listener);
  }

  public void removeListSelectionListener(ListSelectionListener listener)
  {
    tableViewer.removeListSelectionListener(listener);
  }
  
  private class TableUpdateListener implements ListSelectionListener, TableModelListener
  {
    private void reloadEditedEntitiesFromTableSelection()
    {
      Collection<Entity> selectedEntities = getTableSelection();
      setEditedEntities(selectedEntities);
    }
    
    public void valueChanged(ListSelectionEvent e)
    {
      reloadEditedEntitiesFromTableSelection();
    }
    
    private void updateEditedEntitiesIfChanged(int firstModelRow, int lastModelRow)
    {
      Collection<Entity> editedEntities = getEditedEntities();
      for(int modelRow = firstModelRow; modelRow <= lastModelRow; modelRow++)
      {
        Entity entity = getEntityAtModelRow(modelRow);
        if(EntityUtils.containsEntity(editedEntities, entity))
        {
          reloadEditedEntitiesFromTableSelection();
        }
      }
    }

    public void tableChanged(TableModelEvent e)
    {
      int eventType = e.getType();
      switch(eventType)
      {
        case TableModelEvent.UPDATE:
          int firstRow = e.getFirstRow();
          int lastRow = e.getLastRow();
          if(firstRow == TableModelEvent.HEADER_ROW)
          {
            // Structure of the table has changed; need to refresh everything.
            reloadEditedEntitiesFromTableSelection();
          }
          else
          {
            // only update the editor / buttons if this a change to an entity we're acting on.
            updateEditedEntitiesIfChanged(firstRow, lastRow);
          }
          break;
      }
    }
  }
}
