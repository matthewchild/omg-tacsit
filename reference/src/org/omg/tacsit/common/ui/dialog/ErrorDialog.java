/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 29, 2011
 */
package org.omg.tacsit.common.ui.dialog;

import org.omg.tacsit.common.ui.ToggleComponentShownAction;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.omg.tacsit.common.ui.ExitAction;
import org.omg.tacsit.common.ui.panel.MessageWithDetailsPanel;
import org.omg.tacsit.common.ui.panel.ThrowablePanel;

/**
 * A dialog used to display errors and exceptions.
 * <p>
 * The dialog will not present the users with the exception in the initial dialog, but provides a UI feature to
 * show the stack trace if desired.
 * @author Matthew Child
 */
public class ErrorDialog extends JDialog
{

  private MessageWithDetailsPanel errorPanel;
  private DetailPanel detailPanel;

  /**
   * Creates a new instance.
   * @param owner The owning window.
   */
  public ErrorDialog(Window owner)
  {
    super(owner);
    initGUI();
  }

  private void initGUI()
  {
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    setContentPane(contentPanel);

    errorPanel = createErrorPanel();
    contentPanel.add(errorPanel, BorderLayout.CENTER);

    JComponent buttonPanel = createButtonPanel();
    contentPanel.add(buttonPanel, BorderLayout.SOUTH);
    pack();
  }

  private MessageWithDetailsPanel createErrorPanel()
  {
    MessageWithDetailsPanel panel = new MessageWithDetailsPanel();
    panel.setSummaryMessageColumns(66);
    detailPanel = new DetailPanel();
    detailPanel.setVisible(false);
    panel.setMessageDetailsComponent(detailPanel);
    panel.addPropertyChangeListener(MessageWithDetailsPanel.DETAILS_VISIBLE_PROPERTY,
                                    new DescriptionVisibilityListener());
    return panel;
  }

  private JComponent createButtonPanel()
  {
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));

    JButton closeButton = new JButton(new ToggleComponentShownAction("Close", "Show", this));
    buttonPanel.add(closeButton);

    JButton exitButton = new JButton(new ExitAction("Exit Application", -1));
    buttonPanel.add(exitButton);

    return buttonPanel;
  }

  /**
   * Sets the summary message that is always displayed in the dialog.
   * @param summaryMessage A summary of the exception/error.
   */
  public void setSummaryMessage(String summaryMessage)
  {
    errorPanel.setSummaryMessage(summaryMessage);
    pack();
  }
  
  /**
   * Gets the summary message that is always displayed in the dialog.
   * @return  A summary of the exception/error.
   */
  public String getSummaryMessage()
  {
    return errorPanel.getSummaryMessage();
  }

  /**
   * Sets the thread that the exception/error occurred in.  This will be a part of the (initially hidden) message 
   * details.
   * @param thread The thread the exception/error occurred in.
   */
  public void setThread(Thread thread)
  {
    detailPanel.setThread(thread);
  }
  
  /**
   * Gets the thread that the exception/error occurred in.  This will be a part of the (initially hidden) message 
   * details.
   * @return The thread the exception/error occurred in.
   */
  public Thread getThread()
  {
    return detailPanel.getThread();
  }

  /**
   * Sets the exception/error that occurred.  This will be a part of the (initially hidden) message details.
   * @param throwable The exception/error that occurred.
   */
  public void setError(Throwable throwable)
  {
    detailPanel.setThrowable(throwable);
  }
  
  /**
   * Gets the exception/error that occurred.  This will be a part of the (initially hidden) message details.
   * @return The exception/error that occurred.
   */
  public Throwable getError()
  {
    return detailPanel.getThrowable();
  }

  private class DetailPanel extends JPanel
  {
    private Thread thread;

    private JLabel threadLabel;
    private ThrowablePanel throwablePanel;

    public DetailPanel()
    {
      initGUI();
    }
    
    private void initGUI()
    {
      super.setLayout(new BorderLayout());
      
      threadLabel = new JLabel();
      add(threadLabel, BorderLayout.NORTH);
      
      throwablePanel = new ThrowablePanel();
      throwablePanel.setColumns(90);
      throwablePanel.setRows(10);
      add(throwablePanel, BorderLayout.CENTER);
      
      setBorder(BorderFactory.createTitledBorder("Error Details"));
    }

    public void setThread(Thread thread)
    {
      this.thread = thread;
      String newMessageText;
      if (this.thread != null)
      {
        StringBuilder builder = new StringBuilder("Unhandled exception in Thread \"");
        builder.append(this.thread.getName());
        builder.append('\"');
        newMessageText = builder.toString();
      }
      else
      {
        newMessageText = null;
      }
      threadLabel.setText(newMessageText);
    }
    
    public Thread getThread()
    {
      return thread;
    }
    
    public void setThrowable(Throwable throwable)
    {
      throwablePanel.setThrowable(throwable);
    }
    
    public Throwable getThrowable()
    {
      return throwablePanel.getThrowable();
    }
  }

  private class DescriptionVisibilityListener implements PropertyChangeListener
  {

    public void propertyChange(PropertyChangeEvent evt)
    {
      pack();
    }
  }
}
