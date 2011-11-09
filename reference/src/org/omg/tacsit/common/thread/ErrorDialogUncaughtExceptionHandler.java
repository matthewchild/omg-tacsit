/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 29, 2011
 */
package org.omg.tacsit.common.thread;

import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.omg.tacsit.common.ui.dialog.ErrorDialog;

/**
 * Handles exceptions and errors that aren't caught by any thread by displaying an error dialog.  The error dialog
 * will allow you to optionally see the exception/error and the thread it occurred in.
 * <p>
 * This handler is meant to be used as a parameter for the method Thread.setDefaultUncaughtExceptionHandler.  
 * <p>
 * Inspiration for this class was taken from guidance suggested here:
 * http://stuffthathappens.com/blog/2007/10/07/programmers-notebook-uncaught-exception-handlers/
 * @author Matthew Child
 */
public class ErrorDialogUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler
{
  private JDialog dialog;
  
  private String dialogTitle;
  private String summaryMessage;

  /**
   * Creates a new ErrorDialogUncaughtExceptionHandler
   */
  public ErrorDialogUncaughtExceptionHandler()
  {
    dialog = null;
    dialogTitle = "Unexpected Error";
    summaryMessage = "A serious error has occurred within the application.  This may cause display problems," +
                     " and some controls to be unresponsive.  You may be able to continue using it," +
                     " but it is recommended that you quit the application and restart it.";
  }

  /**
   * Gets the title of the dialog that is displayed.
   * @return The title of the dialog.
   */
  public String getDialogTitle()
  {
    return dialogTitle;
  }

  /**
   * Sets the title of the dialog that is displayed.
   * @param dialogTitle The new dialog title.
   */
  public void setDialogTitle(String dialogTitle)
  {
    this.dialogTitle = dialogTitle;
  }

  /**
   * Gets the summary message that will always be visible to the user.  This should be a general purpose message, as it
   * will be displayed for any exception that occurs.
   * @return The summary message that will be displayed within the dialog.
   */
  public String getSummaryMessage()
  {
    return summaryMessage;
  }

  /**
   * Sets the summary message that will always be visible to the user.  This should be a general purpose message, as it
   * will be displayed for any exception that occurs.
   * @param summaryMessage The summary message that will be displayed within the dialog.
   */
  public void setSummaryMessage(String summaryMessage)
  {
    this.summaryMessage = summaryMessage;
  }

  public void uncaughtException(final Thread t, final Throwable e)
  {
    if (SwingUtilities.isEventDispatchThread())
    {
      showException(t, e);
    }
    else
    {
      SwingUtilities.invokeLater(new Runnable()
      {

        public void run()
        {
          showException(t, e);
        }
      });
    }
  }

  private Window getActiveWindow(Window[] windows)
  {
    Window result = null;
    for (int i = 0; i < windows.length; i++)
    {
      Window window = windows[i];
      if (window.isActive())
      {
        result = window;
      }
      else
      {
        Window[] ownedWindows = window.getOwnedWindows();
        if (ownedWindows != null)
        {
          result = getActiveWindow(ownedWindows);
        }
      }
    }
    return result;
  }
  
  private Window getDialogParentWindow()
  {
    Window[] windows = JFrame.getWindows();
    Window activeWindow = getActiveWindow(windows);
    return activeWindow;
  }
  
  private boolean isDialogVisible()
  {
    boolean visible = false;
    if(dialog != null)
    {
      visible = dialog.isVisible();
    }
    return visible;
  }

  private void showException(Thread t, Throwable e)
  {
    // This will prevent errors from being rethrown
    if(!isDialogVisible())
    {
      Window dialogParentWindow = getDialogParentWindow();
      ErrorDialog errorDialog = errorDialog = new ErrorDialog(dialogParentWindow);
      errorDialog.setTitle(dialogTitle);
      errorDialog.setSummaryMessage(summaryMessage);
      errorDialog.setThread(t);
      errorDialog.setError(e);
      errorDialog.setLocationRelativeTo(null);
      errorDialog.setVisible(true);      
      errorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);      
    }
  }
}
