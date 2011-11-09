/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 12, 2011
 */
package org.omg.tacsit.ui.viewport;

import java.awt.GridLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import org.omg.tacsit.common.util.CollectionUtils;
import org.omg.tacsit.controller.Projection;
import org.omg.tacsit.controller.Viewport;
import org.omg.tacsit.controller.ViewportManager;
import org.omg.tacsit.controller.ViewportManagerEvent;
import org.omg.tacsit.controller.ViewportManagerListener;
import org.omg.tacsit.query.QueryManager;

/**
 * A panel which displays the Viewports that are managed by a ViewportManager.
 * <p>
 * The Viewports are tiled in a roughly square shape.
 * @author Matthew Child
 */
public class ViewportManagementPanel extends JPanel
{  
  private Map<Viewport, ViewportPanel> viewportToPanel;
  
  private ViewportManager viewportManager;
  
  private ViewportManagerListener viewportManagerListener;
  
  private QueryManager queryManager;
  
  /**
   * The available Projections that viewports can use.
   */
  private Collection<Projection> projectionOptions;
  
  /**
   * Creates a new instance.
   */
  public ViewportManagementPanel()
  {
    viewportToPanel = new HashMap();
  }

  /**
   * Gets the ViewportManager who's contents are displayed.
   * @return The ViewportManager who's contents are displayed.
   */
  public ViewportManager getViewportManager()
  {
    return viewportManager;
  }

  /**
   * Sets the ViewportManager who's contents are displayed.
   * @param viewportManager The ViewportManager who's contents should be displayed.
   */
  public void setViewportManager(ViewportManager viewportManager)
  {
    ViewportManagerListener updateListener = lazyGetViewportManagerListener();
    if(this.viewportManager != null)
    {
      this.viewportManager.removeViewportManagerListener(updateListener);
    }
    
    clearViewports();
    this.viewportManager = viewportManager;
    
    if(this.viewportManager != null)
    {
      this.viewportManager.addViewportManagerListener(updateListener);
      addViewports(viewportManager.getViewports());
    }
  }

  /**
   * Gets the QueryManager used for finding entities that actions may use.
   * @return The query manager that retrieves entities.
   */
  public QueryManager getQueryManager()
  {
    return queryManager;
  }

  /**
   * Sets the QueryManager used for finding entities that actions may use.
   * @param queryManager The query manager that will retrieve entities.
   */
  public void setQueryManager(QueryManager queryManager)
  {
    this.queryManager = queryManager;
    for (ViewportPanel viewportPanel : viewportToPanel.values())
    {
      viewportPanel.setQueryManager(queryManager);
    }
  }
  
  /**
   * Gets (or initializes) the listener that responds to the ViewportManager.
   * @return This object's listener.
   */
  protected ViewportManagerListener lazyGetViewportManagerListener()
  {
    if(viewportManagerListener == null)
    {
      viewportManagerListener = new ViewportUpdateListener();
    }
    return viewportManagerListener;
  }
  
  private void clearViewports()
  {
    super.removeAll();
    viewportToPanel.clear();
    invalidate();
    validate();
    repaint();
  }  
  
  private void rebuildLayout()
  {
    int rows;
    int columns;
    int componentCount = getComponentCount();
    if(componentCount == 0)
    {
      rows = 1;
      columns = 1;
    }
    else
    {
      double squareRoot = Math.sqrt(componentCount);
      rows = (int)Math.floor(squareRoot);
      columns = componentCount / rows;
      if((componentCount % rows) != 0)
      {
        columns++;
      }
    }
    
    setLayout(new GridLayout(rows, columns));    
  }
  
  private void addViewports(Collection<Viewport> viewports)
  {
    for (Viewport viewport : viewports)
    {
      addViewportToLayout(viewport);
    }
  }
  
  private ViewportPanel createViewportPanel(Viewport viewport)
  {
    ViewportPanel viewportPanel = new ViewportPanel();
    viewportPanel.setProjectionOptions(projectionOptions);
    viewportPanel.setQueryManager(queryManager);
    viewportPanel.setViewport(viewport);
    viewportPanel.setViewportManager(viewportManager);
    return viewportPanel;
  }
  
  private void addViewportToLayout(Viewport viewport)
  {
    ViewportPanel viewportPanel = createViewportPanel(viewport);
    viewportToPanel.put(viewport, viewportPanel);
    super.add(viewportPanel);
    rebuildLayout();
    this.invalidate();
    this.validate();
    repaint();
  }
  
  private void removeViewport(Viewport viewport)
  {
    ViewportPanel viewportPanel = viewportToPanel.remove(viewport);
    viewportPanel.setViewportManager(null);
    remove(viewportPanel);
    rebuildLayout();
    this.invalidate();
    this.validate();
    repaint();
  }

  /**
   * Gets the Projection options that Viewports have available for use.
   * @return An unmodifiable Collection of projection options.
   */
  public Collection<Projection> getProjectionOptions()
  {
    return projectionOptions;
  }

  /**
   * Sets the Projection options that Viewports have available for use.
   * @param projectionOptions The valid projection options.
   */
  public void setProjectionOptions(Collection<Projection> projectionOptions)
  {
    this.projectionOptions = CollectionUtils.copyToUnmodifiableCollection(projectionOptions);
    for(ViewportPanel viewportPanel : viewportToPanel.values())
    {
      viewportPanel.setProjectionOptions(projectionOptions);
    }
  }
  
  private class ViewportUpdateListener implements ViewportManagerListener
  {
    @Override
    public void viewportAdded(ViewportManagerEvent event)
    {
      Viewport viewport = event.getViewport();
      addViewportToLayout(viewport);
    }

    @Override
    public void viewportRemoved(ViewportManagerEvent event)
    {
      Viewport viewport = event.getViewport();
      removeViewport(viewport);
    }    
  }
}
