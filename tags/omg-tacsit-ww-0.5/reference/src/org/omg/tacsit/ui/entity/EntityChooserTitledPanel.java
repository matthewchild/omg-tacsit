/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 22, 2011
 */
package org.omg.tacsit.ui.entity;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.omg.tacsit.common.ui.ComponentUtils;

/**
 * A panel which display an EntityChooser with a titled border.
 * @author Matthew Child
 */
public class EntityChooserTitledPanel extends JPanel
{
  private static final String EMPTY_TITLE = "Untitled";
  
  private EntityChooserUI entityViewer;
  
  private PropertyChangeListener nameChangeListener;
  
  private TitledBorder titledBorder;

  /**
   * Creates a new instance.
   */
  public EntityChooserTitledPanel()
  {
    super(new BorderLayout(5, 5));
    nameChangeListener = new NameChangeListener();
    initGUI();
  }
  
  private void initGUI()
  {
    titledBorder = new TitledBorder(EMPTY_TITLE);
    setBorder(titledBorder);
  }  

  /**
   * Gets the EntityChooser that's displayed within the titled border.
   * @return The displayed EntityChooser
   */
  public EntityChooserUI getEntityChooser()
  {
    return entityViewer;
  }

  /**
   * Sets the EntityChooser that's displayed within the titled border.
   * @param entityChooser The EntityChooser to display 
   */
  public void setEntityChooser(EntityChooserUI entityChooser)
  {
    if(this.entityViewer != null)
    {
      Component existingComponent = this.entityViewer.getComponent();
      remove(existingComponent);
      existingComponent.removePropertyChangeListener(ComponentUtils.COMPONENT_NAME_PROPERTY, nameChangeListener);
    }
    
    String titleText = EMPTY_TITLE;
    this.entityViewer = entityChooser;
    if(this.entityViewer != null)
    {
      Component newComponent = this.entityViewer.getComponent();
      add(newComponent, BorderLayout.CENTER);
      titleText = newComponent.getName();      
      newComponent.addPropertyChangeListener(ComponentUtils.COMPONENT_NAME_PROPERTY, nameChangeListener);
    }
    updateBorderTitle(titleText);
  }
  
  private void updateBorderTitle(String nameAsString)
  {
    titledBorder.setTitle(nameAsString);
    invalidate();
    validate();
    repaint();
  }
  
  private class NameChangeListener implements PropertyChangeListener
  {
    public void propertyChange(PropertyChangeEvent evt)
    {
      Object newName = evt.getNewValue();
      String nameAsString = String.valueOf(newName);
      updateBorderTitle(nameAsString);
    }    
  }
}
