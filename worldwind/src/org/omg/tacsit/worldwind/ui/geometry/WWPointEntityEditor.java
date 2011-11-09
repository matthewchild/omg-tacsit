/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 24, 2011
 */
package org.omg.tacsit.worldwind.ui.geometry;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyEditorSupport;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A PropertyEditor which can edit the properties of a WWPointEntity.
 * @author Matthew Child
 */
public class WWPointEntityEditor extends PropertyEditorSupport
{
  private JLabel entityTypeLabel;
  
  private JComponent customEditor;

  /**
   * Creates a new instance.
   */
  public WWPointEntityEditor()
  {
    customEditor = initCustomEditor();
  }

  private JComponent initCustomEditor()
  {
    JPanel customEditorPanel = new JPanel(new BorderLayout(10, 10));
    
    JComponent typeComponent = initTypeComponent();
    customEditorPanel.add(typeComponent, BorderLayout.NORTH);
    
    customEditorPanel.add(new JLabel("Not yet implemented"), BorderLayout.CENTER);
    
    return customEditorPanel;
  }
  
  private JComponent initTypeComponent()
  {
    JPanel typePanel = new JPanel(new BorderLayout(15, 0));
    
    typePanel.add(new JLabel("Entity Type:"), BorderLayout.WEST);
    
    entityTypeLabel = new JLabel();
    typePanel.add(entityTypeLabel, BorderLayout.CENTER);
    
    return typePanel;    
  }
    
  @Override
  public void setValue(Object value)
  {
    super.setValue(value);
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
}
