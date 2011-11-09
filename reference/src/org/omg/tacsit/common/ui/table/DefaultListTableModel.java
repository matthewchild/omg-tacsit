/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 21, 2011
 */
package org.omg.tacsit.common.ui.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * A default implementation of a table in which each row is represented by an object.  A column represents a property 
 * of (or relation to) that object.
 * @author Matthew Child
 * @param <O> The type of Object stored in this instance.
 */
public class DefaultListTableModel<O> extends AbstractTableModel implements ListTableModel<O>
{
  private List<O> rows;
  private Class<O> rowClass;
  
  /**
   * Creates a new instance.
   * @param rowClass The class of elements that are valid for this ListTableModel.
   */
  public DefaultListTableModel(Class<O> rowClass)
  {
    if (rowClass == null)
    {      
      throw new IllegalArgumentException("rowClass may not be null");
    }
    
    this.rows = new ArrayList();
    this.rowClass = rowClass;
  }
  
  public Class<O> getRowClass()
  {
    return this.rowClass;
  }
  
  public List<O> getRows()
  {
    return Collections.unmodifiableList(rows);
  }
  
  public O getRow(int row)
  {
    return rows.get(row);
  }

  @Override
  public int getRowCount()
  {
    return rows.size();
  }
  
  public void clear()
  {
    int size = rows.size();
    if(size > 0)
    {
      rows.clear();
      fireTableRowsDeleted(0, size - 1);
    }
  }
  
  /**
   * Inserts an element into the row model.
   * @param element The element to add.
   */
  protected void doAdd(O element)
  {
    rows.add(element);
  }
  
  public void add(O element)
  {
    int addIndex = rows.size();
    doAdd(element);
    fireTableRowsInserted(addIndex, addIndex + 1);
  }
  
  public void addAll(Collection<? extends O> elements)
  {
    if(elements != null)
    {
      int addCount = elements.size();
      if(addCount > 0)
      {
        int addIndex = rows.size();
        int lastAddIndex = addIndex + addCount - 1;
        for (O element : elements)
        {
          doAdd(element);
        }
        fireTableRowsInserted(addIndex, lastAddIndex);
      }
    }
  }
  
  public void removeAll(Collection<? extends O> elements)
  {
    if(elements != null)
    {
      // Need to go through these one at a time, because they could be in
      //  different places in the list.
      for(O element : elements)
      {
        remove(element);
      }
    }
  }
  
  public void remove(O element)
  {
    int index = rows.indexOf(element);
    if(index != -1)
    {
      rows.remove(index);
      fireTableRowsDeleted(index, index);
    }
  }
  
  public void rowChanged(O element)
  {
    int index = rows.indexOf(element);
    if(index != -1)
    {
      fireTableRowsUpdated(index, index);
    }
  }
  
  public O set(int row, O newElement)
  {
    O oldElement = rows.set(row, newElement);
    fireTableRowsUpdated(row, row);
    return oldElement;
  }
  
  public int rowIndex(O element)
  {
    return rows.indexOf(element);
  }

  @Override
  public int getColumnCount()
  {
    return 1;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    return getRow(rowIndex);
  }
}
