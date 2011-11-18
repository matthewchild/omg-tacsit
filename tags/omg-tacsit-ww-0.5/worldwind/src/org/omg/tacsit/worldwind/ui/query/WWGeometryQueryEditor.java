/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 21, 2011
 */
package org.omg.tacsit.worldwind.ui.query;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyEditorSupport;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.omg.tacsit.common.ui.panel.PropertyEditorPanel;
import org.omg.tacsit.worldwind.geometry.GeometryFactory;
import org.omg.tacsit.worldwind.geometry.WWSurfaceGeometry;
import org.omg.tacsit.worldwind.geometry.WWSurfaceShape;
import org.omg.tacsit.worldwind.query.WWGeometryQuery;

/**
 * A PropertyEditor for a WWGeometryQuery.
 * @author Matthew Child
 */
public class WWGeometryQueryEditor extends PropertyEditorSupport
{
  private DefaultComboBoxModel shapeOptionsModel;
  private JComboBox shapeOptionsCombo;
  private ItemListener comboListener;
  
  private Component customEditor;
  
  private WWGeometryQuery geometryQuery;
  
  private GeometryFactory geometryFactory;
  
  private PropertyEditorPanel propertyEditorPanel;  

  /**
   * Creates a new instance.
   */
  public WWGeometryQueryEditor()
  {
    geometryFactory = new GeometryFactory();
    customEditor = initCustomEditor();
  }
  
  private Component initCustomEditor()
  {
    JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        
    JComponent shapeChooserComponent = initShapeChooserComponent();
    mainPanel.add(shapeChooserComponent, BorderLayout.NORTH);
        
    JComponent shapeDetailComponent = initShapeDetailComponent();
    mainPanel.add(shapeDetailComponent, BorderLayout.CENTER);
    
    return mainPanel;
  }
  
  private JComponent initShapeChooserComponent()
  {
    JPanel shapeChooserPanel = new JPanel(new BorderLayout(5, 5));
    
    JLabel comboLabel = new JLabel("Choose Geometry Shape:");
    shapeChooserPanel.add(comboLabel, BorderLayout.NORTH);
    
    shapeChooserPanel.add(Box.createHorizontalStrut(15), BorderLayout.WEST);
    
    shapeOptionsModel = new DefaultComboBoxModel(WWSurfaceShape.values());
    shapeOptionsCombo = new JComboBox(shapeOptionsModel);
    comboListener = new ComboListener();
    shapeOptionsCombo.addItemListener(comboListener);
            
    shapeChooserPanel.add(shapeOptionsCombo, BorderLayout.CENTER);
    
    return shapeChooserPanel;
  }
  
  private JComponent initShapeDetailComponent()
  {
    propertyEditorPanel = new PropertyEditorPanel();
    JScrollPane scrollPane = new JScrollPane(propertyEditorPanel);
    
    return scrollPane;
  }

  @Override
  public void setValue(Object value)
  {
    super.setValue(value);
    
    geometryQuery = (WWGeometryQuery)value;
    shapeOptionsCombo.removeItemListener(comboListener);
    if(geometryQuery != null)
    {
      WWSurfaceGeometry existingGeometry = (WWSurfaceGeometry)geometryQuery.getGeometry();
      
      WWSurfaceShape existingShape = WWSurfaceShape.RECTANGLE;
      if(existingGeometry != null)
      {
        existingShape = WWSurfaceShape.forClass(existingGeometry.getClass());        
      }
      shapeOptionsCombo.setSelectedItem(existingShape);
      
      propertyEditorPanel.setValue(existingGeometry);
    }
    else
    {
      shapeOptionsCombo.setSelectedItem(null);
    }
    shapeOptionsCombo.addItemListener(comboListener);
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
  
  private WWSurfaceShape getSelectedSurfaceShape()
  {
    Object selectedItem = shapeOptionsCombo.getSelectedItem();
    return ((WWSurfaceShape)selectedItem);
  }
  
  private void setSurfaceShapeToSelection()
  {
    if(geometryQuery != null)
    {
      WWSurfaceShape selectedShape = getSelectedSurfaceShape();
      WWSurfaceGeometry newGeometry = geometryFactory.createGeometry(selectedShape);      
      geometryQuery.setGeometry(newGeometry);
      propertyEditorPanel.setValue(newGeometry);
      this.firePropertyChange();
    }
    else
    {
      throw new IllegalStateException("No geometryQuery set");
    }
  }
  
  private class ComboListener implements ItemListener
  {
    public void itemStateChanged(ItemEvent e)
    {
      setSurfaceShapeToSelection();
    }
  }
}
