/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 21, 2011
 */
package org.omg.tacsit.ui.query;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyEditorSupport;
import java.util.Collection;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.omg.tacsit.controller.EntityType;
import org.omg.tacsit.query.DefaultEntityTypeQuery;

/**
 * A PropertyEditor which edits DefaultEntityTypeQuerys.
 * @author Matthew Child
 */
public class DefaultEntityTypeQueryEditor extends PropertyEditorSupport
{
  private DefaultComboBoxModel entityOptionsModel;
  private JComboBox entityOptionsCombo;
  private ItemListener comboListener;
  
  private Component customEditor;
  
  private DefaultEntityTypeQuery typeQuery;
  
  /**
   * Creates a new instance.
   */
  public DefaultEntityTypeQueryEditor()
  {
    customEditor = initCustomEditor();
  }
  
  private Component initCustomEditor()
  {
    JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        
    JPanel entryPanel = new JPanel(new BorderLayout(5, 5));
    mainPanel.add(entryPanel);
    
    JLabel comboLabel = new JLabel("Choose Entity Type:");
    entryPanel.add(comboLabel, BorderLayout.NORTH);
    
    entryPanel.add(Box.createHorizontalStrut(15), BorderLayout.WEST);
    
    entityOptionsModel = new DefaultComboBoxModel();
    entityOptionsCombo = new JComboBox(entityOptionsModel);
    comboListener = new ComboListener();
    entityOptionsCombo.addItemListener(comboListener);
    
    entryPanel.add(entityOptionsCombo, BorderLayout.CENTER);
    
    return mainPanel;
  }
  
  /**
   * Sets the EntityTypes that are selectable in the Query Editor.
   * @param entityTypes The selectable EntityTypes.
   */
  public void setEntityTypeOptions(Collection<? extends EntityType> entityTypes)
  {
    entityOptionsModel.removeAllElements();
    for (EntityType entityType : entityTypes)
    {
      entityOptionsModel.addElement(entityType);
    }
  }

  @Override
  public void setValue(Object value)
  {
    super.setValue(value);
    
    typeQuery = (DefaultEntityTypeQuery)value;
    entityOptionsCombo.removeItemListener(comboListener);
    if(typeQuery != null)
    {
      EntityType entityType = typeQuery.getEntityType();      
      entityOptionsCombo.setSelectedItem(entityType);      
    }
    else
    {
      entityOptionsCombo.setSelectedItem(null);
    }
    entityOptionsCombo.addItemListener(comboListener);
  }

  @Override
  public boolean supportsCustomEditor()
  {
    return true;
  }

  @Override
  public Component getCustomEditor()
  {
    return customEditor;
  }
  
  private EntityType getSelectedEntityType()
  {
    Object selectedItem = entityOptionsCombo.getSelectedItem();
    return ((EntityType)selectedItem);
  }
  
  private void setEntityTypeToSelection()
  {
    if(typeQuery != null)
    {
      EntityType selectedType = getSelectedEntityType();
      typeQuery.setEntityType(selectedType);
      this.firePropertyChange();
    }
  }
  
  private class ComboListener implements ItemListener
  {
    public void itemStateChanged(ItemEvent e)
    {
      setEntityTypeToSelection();
    }
  }
}
