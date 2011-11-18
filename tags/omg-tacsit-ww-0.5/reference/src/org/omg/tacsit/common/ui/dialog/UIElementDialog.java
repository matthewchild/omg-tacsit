/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 14, 2011
 */
package org.omg.tacsit.common.ui.dialog;

import java.beans.PropertyChangeEvent;
import org.omg.tacsit.common.ui.UIElement;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.omg.tacsit.common.ui.ComponentUtils;

/**
 * A Dialog that displays a UIElement.  The title of the dialog will be the name of the UIElement's component.
 * @author Matthew Child
 */
public class UIElementDialog extends JDialog
{
  private UIElement uiElement;

  /**
   * Creates a new instance.
   * @param owner The window that owns this dialog.
   * @param uiElement The UI Element to display in the dialog.
   */
  public UIElementDialog(Window owner, UIElement uiElement)
  {
    super(owner);
    if(uiElement == null)
    {
      throw new IllegalArgumentException("uiElement may not be null");
    }
    Component component = uiElement.getComponent();
    if(component == null)
    {
      throw new IllegalArgumentException("uiElement's component may not be null.");
    }
    
    this.uiElement = uiElement;
    initGUI(this.uiElement);
    pack();
  }
  
  private void initGUI(UIElement element)
  {
    Component elementComponent = element.getComponent();
    updateDialogTitle(elementComponent.getName());
    elementComponent.addPropertyChangeListener(ComponentUtils.COMPONENT_NAME_PROPERTY, new NameChangeListener());
    
    JComponent contentPanel = createContentPanel(elementComponent);
    setContentPane(contentPanel);
  }
  
  private JComponent createContentPanel(Component content)
  {    
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
    contentPanel.add(content, BorderLayout.CENTER);
    return contentPanel;
  }
  
  private void updateDialogTitle(String title)
  {
    setTitle(title);
  }
  
  private class NameChangeListener implements PropertyChangeListener
  {
    public void propertyChange(PropertyChangeEvent evt)
    {
      Object newName = evt.getNewValue();
      String nameAsString = String.valueOf(newName);
      updateDialogTitle(nameAsString);
    }    
  }
}
