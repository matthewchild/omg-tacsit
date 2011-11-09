/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 21, 2011
 */
package org.omg.tacsit.common.ui.table;

import java.util.Collection;
import java.util.List;
import javax.swing.table.TableModel;

/**
 * A table in which each row is represented by an object.  A column represents a property of (or relation to) that
 * object.
 * @author Matthew Child
 * @param <O> The type of Object that make up the rows in this instance.
 */
public interface ListTableModel<O> extends TableModel
{ 
  /**
   * Gets the Class of Object that is stored as rows in this TableModel.
   * @return The Class of Object that comprises the rows.
   */
  public Class<O> getRowClass();
  
  /**
   * Gets a list of the objects which make up the rows of this table model.
   * @return An unmodifiable List of the objects which make up the rows.  
   */
  public List<O> getRows();
  
  /**
   * Gets the object which underlies a given row in the TableModel
   * @param row The row of the object
   * @return The Object which underlies that row.
   */
  public O getRow(int row);
  
  /**
   * Clears all objects from the table model.
   */
  public void clear();
  
  /**
   * Adds a new row to the model.
   * @param element The element to add.
   */
  public void add(O element);
  
  /**
   * Adds a collection of elements as rows in the model.
   * @param elements The collection of elements to add.
   */
  public void addAll(Collection<? extends O> elements);
  
  /**
   * Removes all the elements as rows in the model.
   * @param elements The collection of elements to remove.
   */
  public void removeAll(Collection<? extends O> elements);
  
  /**
   * Removes a single element from the model.
   * @param element The element to remove from the model.
   */
  public void remove(O element);
  
  /**
   * Notifies the model that external change has happened to this Object, and existing state needs to be updated.
   * @param element The element that has been changed.
   */
  public void rowChanged(O element);
  
  /**
   * Sets the object that underlies a given row to a new object.
   * @param row The row to change the underlying object
   * @param newElement The new object that should reside in the given row
   * @return The old object that was at the row previously.
   */
  public O set(int row, O newElement);
  
  /**
   * Gets the row index of a given element.
   * @param element The element to get the index of.
   * @return The index of the row, or -1 if the element is not found in the model.
   */
  public int rowIndex(O element);
}
