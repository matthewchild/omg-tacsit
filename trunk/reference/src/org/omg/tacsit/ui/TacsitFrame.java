/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 12, 2011
 */
package org.omg.tacsit.ui;

import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.omg.tacsit.common.util.Factory;
import org.omg.tacsit.ui.viewport.ViewportManagementPanel;
import org.omg.tacsit.controller.Projection;
import org.omg.tacsit.controller.TacsitController;
import org.omg.tacsit.controller.Viewport;
import org.omg.tacsit.controller.ViewportManager;
import org.omg.tacsit.query.QueryManager;
import org.omg.tacsit.ui.resources.DecorationIcons;
import org.omg.tacsit.ui.viewport.EntityViewport;

/**
 * A Frame that allows drop-in use of a TacsitController implementation.
 * <p>
 * In addition to setting a TacsitController, it is also recommended to set the ViewportFactory property.  While
 * this isn't strictly required, this property allows Viewports to be created by the assorted actions in the Frame.  
 * If viewports will be created and managed without user interaction, this does not pose an issue.
 * @author Matthew Child
 */
public class TacsitFrame extends JFrame
{    
  private TacsitMenuBar tacsitMenuBar;
  private ViewportManagementPanel viewportManagementPanel;
  
  private TacsitController tacsitController;

  /**
   * Creates a new instance.
   */
  public TacsitFrame()
  {
    setTitle("Tacsit Controller Frame");
    initGUI();
  }
    
  private void initGUI()
  {
    tacsitMenuBar = new TacsitMenuBar();
    setJMenuBar(tacsitMenuBar);
    
    JPanel contentPane = new JPanel(new BorderLayout());
    
    viewportManagementPanel = new ViewportManagementPanel();
    contentPane.add(viewportManagementPanel, BorderLayout.CENTER);
    
    setContentPane(contentPane);
    setIconImage(DecorationIcons.WORLD_24.getImage());
  }

  /**
   * Gets the tacsit controller that provides the data for this Frame.
   * @return The tacsit controller being viewed.
   */
  public TacsitController getTacsitController()
  {
    return tacsitController;
  }

  /**
   * Sets the tacsit controller that provides data for this Frame.
   * @param tacsitController The tacsit controller to view.
   */
  public void setTacsitController(TacsitController tacsitController)
  {
    this.tacsitController = tacsitController;
    if(this.tacsitController != null)
    {
      ViewportManager viewportManager = this.tacsitController.getViewportManager();
      viewportManagementPanel.setViewportManager(viewportManager);
      
      Collection<Projection> availableProjections = tacsitController.getProjections();
      viewportManagementPanel.setProjectionOptions(availableProjections);
      
      QueryManager queryManager = tacsitController.getQueryManager();
      viewportManagementPanel.setQueryManager(queryManager);
    }
    
    tacsitMenuBar.setTacsitController(tacsitController);
    ensureOneViewportExists();
  }
  
  private void ensureOneViewportExists()
  {
    Factory<? extends Viewport> viewportFactory = getViewportFactory();
    if(viewportFactory == null)
    {
      return;
    }
    
    ViewportManager viewportManager = getViewportManager();
    if(viewportManager == null)
    {
      return;
    }
    
    Collection<Viewport> currentViewports = viewportManager.getViewports();
    if(currentViewports.isEmpty())
    {
      Viewport newViewport = viewportFactory.createObject();
      viewportManager.addViewport(newViewport);
    }
  }
  
  private ViewportManager getViewportManager()
  {
    ViewportManager viewportManager = null;
    if(this.tacsitController != null)
    {
      viewportManager = this.tacsitController.getViewportManager();
    }
    return viewportManager;
  }

  /**
   * Sets the factory used to create new Viewports.  New Viewports will be added to the tacsit controller's
   * ViewportManager.
   * @param viewportFactory The factory that creates new viewports.
   */
  public void setViewportFactory(Factory<? extends EntityViewport> viewportFactory)
  {
    tacsitMenuBar.setViewportFactory(viewportFactory);
    ensureOneViewportExists();
  }
  
  /**
   * Gets the factory used to create new viewports.  New viewports will be added to the tacsit controller's
   * ViewportManager.
   * @return The factory that creates new viewports.
   */
  public Factory<? extends EntityViewport> getViewportFactory()
  {
    return tacsitMenuBar.getViewportFactory();
  }

  /**
   * Gets the MenuBar for this Frame.
   * @return The tacsit menu bar for the frame.
   */
  public TacsitMenuBar getTacsitMenuBar()
  {
    return tacsitMenuBar;
  }
}
