/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 30, 2011
 */
package org.omg.tacsit.common.ui.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.event.TableModelListener;

/**
 * A ListTableModel that narrows from a general class type to a specific implementation.
 * <p>
 * At a general ListTableModel level, it is useful to implement the interface with template parameters.  That
 * way, you always know which types of data you are dealing with.  At a higher level though, you may frequently
 * have generic data models which get plugged into adaptable components.  This provides you a filter to discard
 * the types of data that a specific model can't work with.
 * 
 * @author Matthew Child
 * @param <O> The type of general data that should be narrowed into a more specific version.
 */
public class NarrowingListTableModel<O> implements ListTableModel<O>
{
  private ListTableModel delegate;
  private Class<O> rowClass;

  /**
   * Creates a new instance.
   * @param rowClass The class of general data that should be accepted by this model and narrowed.
   * @param delegate The TableModel that functions on the narrowed data.
   */
  public NarrowingListTableModel(Class<O> rowClass, ListTableModel<? extends O> delegate)
  {
    if (rowClass == null)
    {      
      throw new IllegalArgumentException("rowClass may not be null");
    }
    
    if (delegate == null)
    {      
      throw new IllegalArgumentException("delegate may not be null");
    }
    
    this.rowClass = rowClass;
    this.delegate = delegate;
  }

  public Class<O> getRowClass()
  {
    return rowClass;
  }

  public List<O> getRows()
  {
    return delegate.getRows();
  }

  public O getRow(int row)
  {
    return (O)delegate.getRow(row);
  }

  public void clear()
  {
    delegate.clear();
  }
  
  private boolean isElementValid(O element)
  {
    if(element == null)
    {
      return true;
    }
    else
    {
      Class delegateRowClass = delegate.getRowClass();
      return delegateRowClass.isInstance(element);
    }
  }

  public void add(O element)
  {
    if(isElementValid(element))
    {
      delegate.add(element);
    }
  }
  
  private Collection<O> getValidElements(Collection<? extends O> elements)
  {
    Collection<O> validElements = new ArrayList();
    for (O element : elements)
    {
      if(isElementValid(element))
      {
        validElements.add(element);
      }
    }
    return validElements;
  }

  public void addAll(Collection<? extends O> elements)
  {
    Collection<O> validElements = getValidElements(elements);
    delegate.addAll(validElements);
  }

  public void removeAll(Collection<? extends O> elements)
  {
    Collection<O> validElements = getValidElements(elements);
    delegate.removeAll(validElements);
  }

  public void remove(O element)
  {
    if(isElementValid(element))
    {
      delegate.remove(element);
    }
  }

  public void rowChanged(O element)
  {
    if(isElementValid(element))
    {
      delegate.rowChanged(element);
    }
  }

  public O set(int row, O newElement)
  {
    if(isElementValid(newElement))
    {
      return set(row, newElement);
    }
    else
    {
      return null;
    }
  }

  public int rowIndex(O element)
  {
    if(isElementValid(element))
    {
      return delegate.rowIndex(element);
    }
    else
    {
      return -1;
    }
  }

  public int getRowCount()
  {
    return delegate.getRowCount();
  }

  public int getColumnCount()
  {
    return delegate.getColumnCount();
  }

  public String getColumnName(int columnIndex)
  {
    return delegate.getColumnName(columnIndex);
  }

  public Class<?> getColumnClass(int columnIndex)
  {
    return delegate.getColumnClass(columnIndex);
  }

  public boolean isCellEditable(int rowIndex, int columnIndex)
  {
    return delegate.isCellEditable(rowIndex, columnIndex);
  }

  public Object getValueAt(int rowIndex, int columnIndex)
  {
    return delegate.getValueAt(rowIndex, columnIndex);
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex)
  {
    delegate.setValueAt(aValue, rowIndex, columnIndex);
  }

  public void addTableModelListener(TableModelListener l)
  {
    delegate.addTableModelListener(l);
  }

  public void removeTableModelListener(TableModelListener l)
  {
    delegate.removeTableModelListener(l);
  }
}
