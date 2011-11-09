/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 29, 2011
 */
package org.omg.tacsit.ui.viewport;

import java.awt.Window;
import javax.swing.Icon;
import org.omg.tacsit.common.ui.dialog.ShowDialogAction;
import org.omg.tacsit.controller.Viewport;
import org.omg.tacsit.query.QueryManager;

/**
 * Shows a dialog for scaling a Viewport to a set of points.
 * @author Matthew Child
 */
public class ShowScaleToPointsDialogAction extends ShowDialogAction<ScaleToPointsDialog>
{
  private Viewport viewport;
  private QueryManager queryManager;

  /**
   * Creates a new instance.
   * @param name The name of the action
   * @param icon The icon to use for the action
   */
  public ShowScaleToPointsDialogAction(String name, Icon icon)
  {
    super(name, icon);
  }

  /**
   * Gets the Viewport that will be scaled.
   * @return The Viewport that will be scaled.
   */
  public Viewport getViewport()
  {
    return viewport;
  }

  /**
   * Sets the Viewport that will be scaled.
   * @param viewport The Viewport that will be scaled.
   */
  public void setViewport(Viewport viewport)
  {
    this.viewport = viewport;
    ScaleToPointsDialog currentDialog = getCurrentDialog();
    if (currentDialog != null)
    {
      currentDialog.setViewport(this.viewport);
    }
  }

  /**
   * Gets the QueryManager that will be used if the user wants to scale the Viewport to Entity positions.
   * @return The QueryManager that is used.
   */
  public QueryManager getQueryManager()
  {
    return queryManager;
  }

  /**
   * Sets the QueryManager that will be used if the user wants to scale the Viewport to Entity positions.
   * @param queryManager The QueryManager that will be used.
   */
  public void setQueryManager(QueryManager queryManager)
  {
    this.queryManager = queryManager;
    ScaleToPointsDialog currentDialog = getCurrentDialog();
    if (currentDialog != null)
    {
      currentDialog.setQueryManager(this.queryManager);
    }
  }

  protected ScaleToPointsDialog createDialog(Window owner)
  {
    ScaleToPointsDialog dialog = new ScaleToPointsDialog(owner);
    dialog.setViewport(this.viewport);
    dialog.setQueryManager(this.queryManager);
    dialog.pack();
    return dialog;
  }
  
}
