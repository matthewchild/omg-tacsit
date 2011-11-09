/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 29, 2011
 */
package org.omg.tacsit.common.ui.panel;

import org.omg.tacsit.common.ui.ComponentUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A panel used for displaying an exception or error.
 * @author Matthew Child
 */
public class ThrowablePanel extends JPanel
{
  private Throwable throwable;
  
  private JTextArea throwableArea;

  /**
   * Creates a new instance.
   */
  public ThrowablePanel()
  {
    super(new BorderLayout(0, 10));
    initGUI();
  }

  private void initGUI()
  {
    JComponent descriptionComponent = initDescriptionPanel();
    add(descriptionComponent, BorderLayout.CENTER);
  }

  private JComponent initDescriptionPanel()
  {
    throwableArea = new JTextArea();
    throwableArea.setBackground(new Color(235, 230, 228));
    throwableArea.setEditable(false);
    ComponentUtils.changeFontSize(throwableArea, 11);
    JScrollPane scroll = new JScrollPane(throwableArea);
    return scroll;
  }
  
  /**
   * Sets the number of columns in the associated text area.
   * @param columns The number of columns.
   */
  public void setColumns(int columns)
  {
    throwableArea.setColumns(columns);
  }
  
  /**
   * Sets the number of rows in the associated text area.
   * @param rows The number of rows.
   */
  public void setRows(int rows)
  {
    throwableArea.setRows(rows);
  }

  private String createStackTraceString(Throwable ex)
  {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    ex.printStackTrace(printWriter);
    return stringWriter.toString();
  }

  /**
   * Sets the exception or error to display in the text area.
   * @param throwable The exception or error that occurred.
   */
  public void setThrowable(Throwable throwable)
  {
    this.throwable = throwable;
    throwableArea.setText(null);
    if (this.throwable != null)
    {
      String exceptionTrace = createStackTraceString(this.throwable);
      throwableArea.append(exceptionTrace);
    }
  }
  
  /**
   * Gets the exception or error displayed in the text area.
   * @return The exception or error that occurred.
   */
  public Throwable getThrowable()
  {
    return throwable;
  }
}
