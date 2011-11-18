/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 29, 2011
 */
package org.omg.tacsit.ui.viewport;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import org.omg.tacsit.controller.Viewport;

/**
 * A dialog that uses or manipulates a Viewport.
 * <p>
 * This class is intended to be a parent class of dialogs which use a Viewport.  It provides an appropriate
 * title based on the Viewport's name.
 * @author Matthew Child
 */
public abstract class ViewportDialog extends JDialog
{

  private Viewport viewport;
  
  private PropertyChangeListener nameChangeListener;
  
  private String baseTitle;

  /**
   * Creates a new instance.
   * @param owner The window that owns the dialog.
   */
  public ViewportDialog(Window owner)
  {
    super(owner);
    nameChangeListener = new NameChangeListener();
  }

  /**
   * Gets the base title that will be used in conjuction with the Viewport's name to determine the dialog's title.
   * @return The base title
   */
  public String getBaseTitle()
  {
    return baseTitle;
  }

  /**
   * Sets the base title that will be used in conjuction with the Viewport's name to determine the dialog's title.
   * @param baseTitle The base title
   */
  public void setBaseTitle(String baseTitle)
  {
    this.baseTitle = baseTitle;
    updateTitleFromName(this.viewport);
  }

  /**
   * Gets the Viewport that this dialog is using or manipulating.
   * @return The viewport used
   */
  public Viewport getViewport()
  {
    return viewport;
  }

  /**
   * Sets the Viewport that this dialog is using or manipulating.
   * @param viewport The viewport used
   */
  public void setViewport(Viewport viewport)
  {
    if(this.viewport instanceof ComponentViewport)
    {
      ((ComponentViewport)this.viewport).removePropertyChangeListener(ComponentViewport.NAME_PROPERTY, nameChangeListener);
    }    
    this.viewport = viewport;
    updateTitleFromName(viewport);
    if(this.viewport instanceof ComponentViewport)
    {
      ((ComponentViewport)this.viewport).addPropertyChangeListener(ComponentViewport.NAME_PROPERTY, nameChangeListener);
    }
  }

  private void updateTitleFromName(Viewport viewport)
  {
    if (viewport != null)
    {
      updateTitle(viewport.getName());
    }
    else
    {
      updateTitle((String) null);
    }
  }

  private void updateTitle(String viewportName)
  {
    StringBuilder title = new StringBuilder();
    if (viewportName != null)
    {
      title.append(viewportName);
      title.append(" - ");
    }
    title.append(baseTitle);
    setTitle(title.toString());
  }

  private class NameChangeListener implements PropertyChangeListener
  {

    public void propertyChange(PropertyChangeEvent evt)
    {
      Object newName = evt.getNewValue();
      String nameAsString = String.valueOf(newName);
      updateTitle(nameAsString);
    }
  }
}
