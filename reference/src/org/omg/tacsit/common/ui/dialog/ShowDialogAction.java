/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 21, 2011
 */
package org.omg.tacsit.common.ui.dialog;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import org.omg.tacsit.common.ui.ComponentUtils;
import org.omg.tacsit.common.ui.EnhancedAction;

/**
 * An action that shows a dialog.  The dialog is not created into it is ready to be used.
 * <p>
 * This class serves as the parent class that incorporates best practices for display dialogs.  It allows the
 * dialog's owner to be inferred through the Action that invoked it, which simplifies associated the parent Window
 * to the dialog.  In a simple implementation, a child class will simply implement the <code>createDialog</code> 
 * method, and be finished.
 * <p>
 * A more complex implementation might have state associated with the action; for example, a Color that
 * is used by the dialog.  In this case, subclasses should keep the attributes stored as member variables, and
 * update the current dialog <i>only if it is already initialized</i>.  In the <code>createDialog</code> method,
 * you would use this member variable to initialize the state of the dialog before returning it.  This way, the 
 * creation of dialogs can still put off being created until they are used - but you still gain the benefit of being 
 * able to configure the dialogs through the action.  
 * <p>
 * For stateful dialogs, encapsulating the state information into a custom Dialog class, and subclassing with the 
 * concrete template parameter of that custom dialog action will simplify interaction significantly.  For example:
 * <p>
 * <code>
 * public class ShowMyDialogAction extends ShowDialogAction<MyDialog>{...}
 * </code>
 * 
 * @author Matthew Child
 * @param <D> The concrete class of Dialog this action shows.
 */
public abstract class ShowDialogAction<D extends Dialog> extends EnhancedAction
{  
  private D currentDialog;

  /**
   * Creates a new instance.
   * @param name The name of the action.
   * @param icon The icon to use for the action.
   */
  public ShowDialogAction(String name, Icon icon)
  {
    super(name, icon);
  }
  
  /**
   * Constructs a new dialog.
   * @param owner The owner of the dialog
   * @return A new dialog that is owned by <code>owner</code>
   */
  protected abstract D createDialog(Window owner);
  
  private D setupDialog(Window owner)
  {
    D dialog = createDialog(owner);
    dialog.setLocationRelativeTo(owner);
    return dialog;
  }
  
  private boolean areOwnersEqual(Window owner1, Window owner2)
  {
    return (owner1 == owner2) || 
           ((owner1 != null) && owner1.equals(owner2));
  }
  
  /**
   * Gets the current dialog being displayed, or last displayed by this action.  May be null.
   * @return The current dialog.
   */
  protected D getCurrentDialog()
  {
    return currentDialog;
  }
  
  private Dialog getOrCreateCurrentDialog(Window owner)
  {
    if (currentDialog == null)
    {
      currentDialog = setupDialog(owner);
    }
    else
    {
      Window lastCreatedOwner = currentDialog.getOwner();
      if(!areOwnersEqual(lastCreatedOwner, owner))
      {
        currentDialog = setupDialog(owner);
      }
    }
    return currentDialog;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Window ancestor = ComponentUtils.getWindowAncestor(e.getSource());
    Dialog dialog = getOrCreateCurrentDialog(ancestor);
    dialog.setVisible(true);
  }
}
