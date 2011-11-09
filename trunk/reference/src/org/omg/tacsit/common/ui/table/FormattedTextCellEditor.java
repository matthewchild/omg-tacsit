/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 1, 2011
 */
package org.omg.tacsit.common.ui.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.Format;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

/**
 * A cell editor which uses a JFormattedTextField to convert values to and from Strings.
 * @author Matthew Child
 */
public class FormattedTextCellEditor extends AbstractCellEditor implements TableCellEditor, TreeCellEditor
{
  private int clickCountToStart;

  private ActionListener textEntryListener;
  private JFormattedTextField formattedTextField;

  /**
   * Creates a new instance.
   * @param format The format to use for converting the value to and from a String.
   */
  public FormattedTextCellEditor(Format format)
  {
    this(new JFormattedTextField(format));
  }

  /**
   * Creates a new instance.
   * @param formattedTextField The field to use for editing value objects.
   */
  public FormattedTextCellEditor(JFormattedTextField formattedTextField)
  {
    if(formattedTextField == null)
    {
      throw new IllegalArgumentException("formattedTextField may not be null");
    }
    this.formattedTextField = formattedTextField;
    this.clickCountToStart = 2;
    this.textEntryListener = new TextEntryListener();
    this.formattedTextField.addActionListener(textEntryListener);
  }

  /**
   * Sets the horizontal alignment of the text field.  As standard practice, numerical values should be right aligned.
   * 
   * @param alignment The new horizontal alignment.  Valid values are SwingConstants.LEFT, SwingConstants.CENTER, 
   * or SwingConstants.RIGHT.
   */
  public void setHorizontalAlignment(int alignment)
  {
    formattedTextField.setHorizontalAlignment(alignment);
  }

  /**
   * Gets the horizontal alignment of the text field.
   * @return The horizontal alignment.  May be SwingConstants.LEFT, SwingConstants.CENTER,  or SwingConstants.RIGHT.
   */
  public int getHorizontalAlignment()
  {
    return formattedTextField.getHorizontalAlignment();
  }

  @Override
  public Object getCellEditorValue()
  {
    return formattedTextField.getValue();
  }

  @Override
  public boolean isCellEditable(EventObject anEvent)
  {
    if (anEvent instanceof MouseEvent)
    {
      return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
    }
    return true;
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
  {
    formattedTextField.setValue(value);
    return formattedTextField;
  }

  @Override
  public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
                                              boolean leaf, int row)
  {
    formattedTextField.setValue(value);
    return formattedTextField;
  }

  private class TextEntryListener implements ActionListener
  {

    @Override
    public void actionPerformed(ActionEvent e)
    {
      stopCellEditing();
    }
  }
}
