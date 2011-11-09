/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 29, 2011
 */

package org.omg.tacsit.common.ui;

import java.awt.event.ActionEvent;

/**
 * An action that triggers an exit from the application.
 * @author Matthew Child
 */
public class ExitAction extends EnhancedAction
{

  private int exitCode;

  /**
   * Creates a new instance.
   */
  public ExitAction()
  {
    this("Exit Application", 0);
  }

  /**
   * Creates a new instance.
   * @param name The name of the action.
   * @param exitCode The exit code the application should terminate with.
   */
  public ExitAction(String name, int exitCode)
  {
    super("Exit Application");
    this.exitCode = exitCode;
  }

  /**
   * Gets the exit code the application should terminate with.
   * @return the exit code the application should terminate with
   */
  public int getExitCode()
  {
    return exitCode;
  }

  /**
   * Sets the exit code the application should terminate with.
   * @param exitCode The exit code the application should terminate with.
   */
  public void setExitCode(int exitCode)
  {
    this.exitCode = exitCode;
  }

  public void actionPerformed(ActionEvent e)
  {
    System.exit(exitCode);
  }
}
