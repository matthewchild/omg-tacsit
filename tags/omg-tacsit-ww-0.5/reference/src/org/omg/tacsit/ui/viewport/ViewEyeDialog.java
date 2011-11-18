/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 25, 2011
 */
package org.omg.tacsit.ui.viewport;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.omg.tacsit.controller.Projection;
import org.omg.tacsit.controller.ViewEyeProperties;
import org.omg.tacsit.controller.Viewport;
import org.omg.tacsit.controller.ViewportChangeEvent;
import org.omg.tacsit.controller.ViewportChangeListener;

/**
 * A dialog that enables manipulation of a Viewport's ViewEyeProperties.
 * @author Matthew Child
 */
public class ViewEyeDialog extends ViewportDialog
{
  
  private ViewEyePropertiesPanel viewEyePanel;
  
  private ViewportChangeListener viewportChangeListener;

  /**
   * Creates a new instance.
   * @param owner The window that owns this dialog.
   */
  public ViewEyeDialog(Window owner)
  {
    super(owner);
    setBaseTitle("View Eye");
    viewportChangeListener = new ViewportListener();
    initGUI();
    pack();
  }
  
  private void initGUI()
  {
    JComponent contentPane = createContentPane();
    setContentPane(contentPane);
  }

  private JComponent createContentPane()
  {
    JPanel contentPane = new JPanel(new BorderLayout());

    viewEyePanel = new ViewEyePropertiesPanel();
    contentPane.add(viewEyePanel, BorderLayout.CENTER);

    JComponent buttonPanel = initButtonPanel();
    contentPane.add(buttonPanel, BorderLayout.EAST);

    return contentPane;
  }

  private JComponent initButtonPanel()
  {
    JPanel buttonPanel = new JPanel(new BorderLayout());

    JButton editButton = new JButton(new EditAction());
    buttonPanel.add(editButton, BorderLayout.NORTH);

    return buttonPanel;
  }

  @Override
  public void setViewport(Viewport viewport)
  {
    Viewport oldViewport = getViewport();
    if(oldViewport != null)
    {
      oldViewport.removeViewportChangeListener(viewportChangeListener);
    }
    
    super.setViewport(viewport);
    
    ViewEyeProperties viewEye = null;
    if(viewport != null)
    {
      viewport.addViewportChangeListener(viewportChangeListener);
      viewEye = viewport.getViewEye();
    }
    updateViewedLocation(viewEye);
  }
  
  /**
   * Sets the projection options that are valid for this Viewport's ViewEyeProperties.
   * @param projectionOptions The valid projection options.
   */
  public void setProjectionOptions(Collection<Projection> projectionOptions)
  {
    viewEyePanel.setProjectionOptions(projectionOptions);
  }

  /**
   * Gets the projection options that are valid for this Viewport's ViewEyeProperties.
   * @return The valid projection options.
   */
  public Collection<Projection> getProjectionOptions()
  {
    return viewEyePanel.getProjectionOptions();
  }
  
  private void updateViewedLocation(ViewEyeProperties viewEye)
  {
    viewEyePanel.setViewEye(viewEye);
  }

  private class EditAction extends AbstractAction
  {

    private boolean isEditing;

    public EditAction()
    {
      isEditing = false;
      viewEyePanel.setEditable(isEditing);
      reloadActionProperties();
    }

    private void reloadActionProperties()
    {
      if (isEditing)
      {
        putValue(Action.NAME, "Goto");
      }
      else
      {
        putValue(Action.NAME, "Edit");
      }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
      Viewport currentViewport = getViewport();
      if (isEditing)
      {
        currentViewport.addViewportChangeListener(viewportChangeListener);

        ViewEyeProperties viewEye = viewEyePanel.getViewEye();
        currentViewport.setViewEye(viewEye);
      }
      else
      {
        currentViewport.removeViewportChangeListener(viewportChangeListener);
      }
      isEditing = !isEditing;
      reloadActionProperties();
      viewEyePanel.setEditable(isEditing);
    }
  }
  
  private class ViewportListener implements ViewportChangeListener
  {
    @Override
    public void viewportChanged(ViewportChangeEvent event)
    {
      updateViewedLocation(event.getViewEyeProperties());
    }    
  }
}
