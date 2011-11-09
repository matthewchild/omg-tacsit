/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 12, 2011
 */
package org.omg.tacsit.ui.viewport;

import org.omg.tacsit.query.QueryManager;
import java.beans.PropertyChangeEvent;
import org.omg.tacsit.controller.ViewportManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.omg.tacsit.ui.resources.ActionIcons;
import org.omg.tacsit.controller.Projection;
import org.omg.tacsit.controller.Viewport;

/**
 * A panel which displays a Viewport, with some actions that support interacting with the Viewport through the
 * specification interface.
 * <p>
 * An implementation note:  The specification does not provide any mechanism for getting a Component to make
 * displayable.  As such, in order to be displayed correctly, the Viewport must either be a subclass of Component,
 * or implement ComponentViewport.  If the Viewport meets neither requirement, the panel will show up empty.  However,
 * the actions can still be used to invoke the functions defined in the Viewport interface.
 * @author Matthew Child
 */
public class ViewportPanel extends JPanel
{

  private JTextField nameField;
  private Viewport viewport;
  private ShowScaleToPointsDialogAction showScaleAction;
  private ShowViewEyeDialogAction showViewEyeAction;
  private PropertyChangeListener namePropertyListener;
  private RemoveViewportAction removeViewportAction;
  private DocumentListener nameFieldListener;
  private static final Border TOOLBAR_BUTTON_BORDER = BorderFactory.createEmptyBorder(2, 2, 2, 2);

  /**
   * Creates a new instance.
   */
  public ViewportPanel()
  {
    super(new BorderLayout());
    namePropertyListener = new NamePropertyListener();
    initGUI();
  }

  private void initGUI()
  {
    JComponent titleBar = initTitleBar();
    titleBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    add(titleBar, BorderLayout.NORTH);
  }

  private JComponent initTitleBar()
  {
    JPanel titleBar = new JPanel(new BorderLayout());

    JComponent leftButtonBar = initLeftButtonBar();
    titleBar.add(leftButtonBar, BorderLayout.WEST);

    JComponent nameComponent = initNameComponent();
    titleBar.add(nameComponent, BorderLayout.CENTER);

    JComponent rightButtonBar = initRightButtonBar();
    titleBar.add(rightButtonBar, BorderLayout.EAST);

    return titleBar;
  }

  private JComponent initLeftButtonBar()
  {
    JToolBar toolbar = new JToolBar("View Settings");
    toolbar.setFloatable(false);

    showViewEyeAction = new ShowViewEyeDialogAction(null, ActionIcons.VIEW_EYE_24);
    JButton showSettingsButton = new JButton(showViewEyeAction);
    showSettingsButton.setFocusable(false);
    showSettingsButton.setBorder(TOOLBAR_BUTTON_BORDER);
    toolbar.add(showSettingsButton);
    toolbar.add(Box.createHorizontalStrut(5));

    showScaleAction = new ShowScaleToPointsDialogAction(null, ActionIcons.SCALE_24);
    JButton showScaleToPointsButton = new JButton(showScaleAction);
    showScaleToPointsButton.setFocusable(false);
    showScaleToPointsButton.setBorder(TOOLBAR_BUTTON_BORDER);
    toolbar.add(showScaleToPointsButton);

    return toolbar;
  }

  private JComponent initRightButtonBar()
  {
    JToolBar toolbar = new JToolBar("Close Bar");
    toolbar.setFloatable(false);

    removeViewportAction = new RemoveViewportAction(null, ActionIcons.CLOSE_24);
    JButton removeViewportButton = new JButton(removeViewportAction);
    removeViewportButton.setFocusable(false);
    removeViewportButton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    toolbar.add(removeViewportButton);

    return toolbar;
  }

  private JComponent initNameComponent()
  {
    JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

    JLabel nameLabel = new JLabel("Name:", JLabel.TRAILING);
    namePanel.add(nameLabel, BorderLayout.WEST);

    nameField = new JTextField();
    nameField.setHorizontalAlignment(JLabel.CENTER);
    nameField.setColumns(20);
    nameFieldListener = new NameListener();
    nameField.getDocument().addDocumentListener(nameFieldListener);
    namePanel.add(nameField, BorderLayout.CENTER);

    return namePanel;
  }

  private Component createNonComponentViewport()
  {
    JPanel panel = new JPanel(new BorderLayout());

    JLabel label = new JLabel("Viewport is not a<br>ComponentViewport or a Component");
    panel.add(label, BorderLayout.CENTER);

    return panel;
  }

  /**
   * Sets the ViewportManager that contains the currently viewed viewport.
   * @param viewportManager The viewport manager that contains the current viewport.
   */
  public void setViewportManager(ViewportManager viewportManager)
  {
    removeViewportAction.setViewportManager(viewportManager);
  }
  
  /**
   * Gets the ViewportManager that contains the currently viewed viewport.
   * @return The viewport manager that contains the current viewport
   */
  public ViewportManager getViewportManager()
  {
    return removeViewportAction.getViewportManager();
  }

  /**
   * Sets the QueryManager that can be used to look up Entities.
   * @param queryManager The query manager that can find entities.
   */
  public void setQueryManager(QueryManager queryManager)
  {
    showScaleAction.setQueryManager(queryManager);
  }

  /**
   * Gets the QueryManager that can be used to look up Entities.
   * @return The query manager that can find entities.
   */
  public QueryManager getQueryManager()
  {
    return showScaleAction.getQueryManager();
  }
  
  private Component addViewportComponent(Viewport viewport)
  {
    Component component;
    if (viewport instanceof Component)
    {
      component = (Component) viewport;
    }
    else if (viewport instanceof ComponentViewport)
    {
      ComponentViewport componentViewport = (ComponentViewport) viewport;
      component = componentViewport.getComponent();
    }
    else
    {
      component = createNonComponentViewport();
    }
    add(component, BorderLayout.CENTER);
    return component;
  }
  
  /**
   * Gets the Viewport being viewed.
   * @return The viewport being viewed.
   */
  public Viewport getViewport()
  {
    return viewport;
  }

  /**
   * Sets the Viewport being viewed.
   * @param viewport The viewport to view.
   */
  public void setViewport(Viewport viewport)
  {
    if (this.viewport instanceof ComponentViewport)
    {
      ((ComponentViewport) this.viewport).removePropertyChangeListener(ComponentViewport.NAME_PROPERTY, namePropertyListener);
    }
    this.viewport = viewport;
    if (this.viewport instanceof ComponentViewport)
    {
      ((ComponentViewport) this.viewport).addPropertyChangeListener(ComponentViewport.NAME_PROPERTY, namePropertyListener);
    }

    reloadNameLabel();

    addViewportComponent(this.viewport);

    showViewEyeAction.setViewport(this.viewport);
    showScaleAction.setViewport(this.viewport);
    removeViewportAction.setViewport(this.viewport);
  }

  private void reloadNameLabel()
  {
    String name = null;
    if (this.viewport != null)
    {
      name = this.viewport.getName();
    }
    updateDisplayedViewportName(name);
  }

  /**
   * Sets the projection options that are valid for use with this viewport.
   * @param projectionOptions The valid projection options.
   */
  public void setProjectionOptions(Collection<Projection> projectionOptions)
  {
    showViewEyeAction.setProjectionOptions(projectionOptions);
  }

  /**
   * Gets the projection options that are valid for use with this viewport.
   * @return The valid projection options.
   */
  public Collection<Projection> getProjectionOptions()
  {
    return showViewEyeAction.getProjectionOptions();
  }

  private void setViewportName(String name)
  {
    if (viewport != null)
    {
      if (viewport instanceof ComponentViewport)
      {
        ((ComponentViewport) viewport).removePropertyChangeListener(ComponentViewport.NAME_PROPERTY, namePropertyListener);
      }
      viewport.setName(name);
      if (viewport instanceof ComponentViewport)
      {
        ((ComponentViewport) viewport).addPropertyChangeListener(ComponentViewport.NAME_PROPERTY, namePropertyListener);
      }
    }
  }

  private void updateDisplayedViewportName(String newName)
  {
    nameField.getDocument().removeDocumentListener(nameFieldListener);
    nameField.setText(newName);
    nameField.getDocument().addDocumentListener(nameFieldListener);
  }

  private class NameListener implements DocumentListener
  {

    private void updateViewportName()
    {
      String nameText = nameField.getText();
      setViewportName(nameText);
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
      updateViewportName();
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
      updateViewportName();
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
      updateViewportName();
    }
  }

  private class NamePropertyListener implements PropertyChangeListener
  {

    public void propertyChange(PropertyChangeEvent evt)
    {
      String newName = (String) evt.getNewValue();
      updateDisplayedViewportName(newName);
    }
  }
}
