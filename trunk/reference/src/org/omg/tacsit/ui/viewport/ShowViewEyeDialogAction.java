/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 29, 2011
 */
package org.omg.tacsit.ui.viewport;

import java.awt.Window;
import java.util.Collection;
import javax.swing.Icon;
import org.omg.tacsit.common.ui.dialog.ShowDialogAction;
import org.omg.tacsit.common.util.CollectionUtils;
import org.omg.tacsit.controller.Projection;
import org.omg.tacsit.controller.Viewport;

/**
 * Shows a dialog for manipulating a Viewport's ViewEyeProperties.
 * @author Matthew Child
 */
public class ShowViewEyeDialogAction extends ShowDialogAction<ViewEyeDialog>
{
  private Viewport viewport;
  Collection<Projection> projectionOptions;

  /**
   * Creates a new instance.
   * @param name The name of the action.
   * @param icon The icon to use for the action.
   */
  public ShowViewEyeDialogAction(String name, Icon icon)
  {
    super(name, icon);
  }

  /**
   * Gets the Viewport who's ViewEyeProperties will be viewed and edited.
   * @return The Viewport to use.
   */
  public Viewport getViewport()
  {
    return viewport;
  }

  /**
   * Sets the Viewport who's ViewEyeProperties will be viewed and edited.
   * @param viewport The Viewport to use.
   */
  public void setViewport(Viewport viewport)
  {
    this.viewport = viewport;
    ViewEyeDialog currentDialog = getCurrentDialog();
    if (currentDialog != null)
    {
      currentDialog.setViewport(viewport);
    }
  }

  /**
   * Sets the Projection options that are valid for this Viewport's ViewEyeProperties
   * @param projectionOptions The valid projection options
   */
  public void setProjectionOptions(Collection<Projection> projectionOptions)
  {
    this.projectionOptions = CollectionUtils.copyToUnmodifiableCollection(projectionOptions);
    ViewEyeDialog currentDialog = getCurrentDialog();
    if (currentDialog != null)
    {
      currentDialog.setProjectionOptions(this.projectionOptions);
    }
  }

  /**
   * Gets the Projection options that are valid for this Viewport's ViewEyeProperties
   * @return The valid projection options.
   */
  public Collection<Projection> getProjectionOptions()
  {
    return projectionOptions;
  }

  protected ViewEyeDialog createDialog(Window owner)
  {
    ViewEyeDialog viewEyeDialog = new ViewEyeDialog(owner);
    viewEyeDialog.setProjectionOptions(this.projectionOptions);
    viewEyeDialog.setViewport(this.viewport);
    viewEyeDialog.pack();
    return viewEyeDialog;
  }  
}
