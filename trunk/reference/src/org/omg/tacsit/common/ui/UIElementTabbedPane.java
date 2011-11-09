/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 22, 2011
 */
package org.omg.tacsit.common.ui;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JTabbedPane;

/**
 * A Tabbed Pane that displays multiple UIElements.
 * @param <E> The type of UIElement that this tabbed pane contains.
 * @author Matthew Child
 */
public class UIElementTabbedPane<E extends UIElement> extends JTabbedPane
{
  private List<E> uiElements;
  private PropertyChangeListener nameChangeListener;

  /**
   * Creates a new instance.
   */
  public UIElementTabbedPane()
  {
    this.uiElements = new ArrayList();
    nameChangeListener = new NameChangeListener();
    initGUI();
  }
  
  private void initGUI()
  {
  }
  
  /**
   * Adds a UIElement to the tab pane.  The title of the tab will be the name of the UIElement's Component.
   * @param uiElement The element to add to the tab pane.
   */
  public void addUIElement(E uiElement)
  {
    this.uiElements.add(uiElement);
    Component component = uiElement.getComponent();
    String componentName = component.getName();
    addTab(componentName, component);
    component.addPropertyChangeListener(ComponentUtils.COMPONENT_NAME_PROPERTY, nameChangeListener);
  }
  
  /**
   * Removes a UIElement from the tab pane.
   * @param uiElement The element to remove from the tab pane.
   */
  public void removeUIElement(E uiElement)
  {
    int index = this.uiElements.indexOf(uiElement);
    UIElement removedElement = this.uiElements.remove(index);
    
    Component removedComponent = removedElement.getComponent();
    removedComponent.removePropertyChangeListener(ComponentUtils.COMPONENT_NAME_PROPERTY, nameChangeListener);
    
    removeTabAt(index);
  }
  
  /**
   * Clears all UIElements from the tab pane.
   */
  public void clearUIElements()
  {
    uiElements.clear();
    removeAll();
  }
  
  /**
   * Gets the List of UIElements displayed by this tab pane.
   * @return An unmodifiable List of the UIElements this tab pane displays.
   */
  public List<E> getUIElements()
  {
    return Collections.unmodifiableList(uiElements);
  }  
  
  private void updateTabTitle(String newName, Component forComponent)
  {
    int componentIndex = indexOfComponent(forComponent);
    super.setTitleAt(componentIndex, newName);
  }
  
  private class NameChangeListener implements PropertyChangeListener
  {
    public void propertyChange(PropertyChangeEvent evt)
    {
      Object newName = evt.getNewValue();
      String nameAsString = String.valueOf(newName);
      Object source = evt.getSource();
      Component sourceComponent = (Component)source;
      updateTabTitle(nameAsString, sourceComponent);
    }    
  }
}
