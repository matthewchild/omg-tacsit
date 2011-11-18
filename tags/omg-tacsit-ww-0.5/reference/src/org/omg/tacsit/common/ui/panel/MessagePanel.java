/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 28, 2011
 */
package org.omg.tacsit.common.ui.panel;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A panel that displays a message.  The message will be centered vertically
 * and horizontally.
 * @author Matthew Child
 */
public class MessagePanel extends JPanel
{
  private JLabel messageComponent;

  /**
   * Creates a new instance.
   */
  public MessagePanel()
  {
    this(null);
  }
  
  /**
   * Creates a new instance.
   * @param message The message to display.
   */
  public MessagePanel(String message)
  {
    super(new BorderLayout());
    initGUI(message);
  }

  private void initGUI(String message)
  {
    messageComponent = new JLabel(message);
    messageComponent.setHorizontalAlignment(JLabel.CENTER);
    add(messageComponent, BorderLayout.CENTER);

    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
  }

  /**
   * Sets the message that should be displayed.
   * @param message The message to display.
   */
  public void setMessage(String message)
  {
    messageComponent.setText(message);
  }

  /**
   * Gets the message that should be displayed.
   * @return The displayed message.
   */
  public String getMessage()
  {
    return messageComponent.getText();
  }
}
