/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 29, 2011
 */
package org.omg.tacsit.common.ui.panel;

import org.omg.tacsit.common.ui.ToggleComponentShownAction;
import org.omg.tacsit.common.ui.ComponentUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * A message panel that has a user-selectable option to display details of the message.
 * <p>
 * The details of the message is displayed in a configurable component.  The typical usage would be to have a container
 * with this class, and a component to display details (for example, a {@link org.omg.swing.ThrowablePanel}.  For a good 
 * example of usage, check out {@link org.omg.swing.ErrorPanel}.
 * @see org.omg.swing.ErrorPanel
 * @see org.omg.swing.ThrowablePanel
 * @author Matthew Child
 */
public class MessageWithDetailsPanel extends JPanel
{
  /**
   * The property that's fired when the the details are shown or hidden.
   */
  public static final String DETAILS_VISIBLE_PROPERTY = "detailsVisible";
  
  private ToggleComponentShownAction toggleDetailsAction;

  private JTextArea messageArea;
  
  private ComponentListener componentVisibilityListener;

  /**
   * Creates a new instance.
   */
  public MessageWithDetailsPanel()
  {
    componentVisibilityListener = new ComponentVisibilityListener();
    initGUI();
  }

  private void initGUI()
  {
    super.setLayout(new BorderLayout(10, 15));
    
    JComponent messageComponent = createMessageComponent();
    add(messageComponent, BorderLayout.NORTH);
  }

  private JComponent createMessageComponent()
  {
    JPanel topPanel = new JPanel(new BorderLayout(10, 0));
    messageArea = new JTextArea("");
    ComponentUtils.configureAsLabel(messageArea);
    ComponentUtils.changeFontStyle(messageArea, Font.BOLD);
    topPanel.add(messageArea, BorderLayout.CENTER);
    
    JPanel toggleContainer = new JPanel(new BorderLayout());
    toggleDetailsAction = new ToggleComponentShownAction("Less", "More", null);
    JButton toggleDetailsButton = new JButton(toggleDetailsAction);
    toggleContainer.add(toggleDetailsButton, BorderLayout.NORTH);
    topPanel.add(toggleContainer, BorderLayout.EAST);
    
    return topPanel;
  }
  
  /**
   * Sets the number of columns in the summary message area.
   * @param columns The number of columns in the summary message area.
   */
  public void setSummaryMessageColumns(int columns)
  {
    messageArea.setColumns(columns);
  }
  
  /**
   * Gets the number of columns in the summary message area.
   * @return The number of columns in the summary message area.
   */
  public int getSummaryMessageColumns()
  {
    return messageArea.getColumns();
  }

  /**
   * Sets the summary message that is always displayed.
   * @param summaryMessage The message summary
   */
  public void setSummaryMessage(String summaryMessage)
  {
    messageArea.setText(summaryMessage);
  }
  
  /**
   * Gets the summary message that is always displayed.
   * @return The message summary
   */
  public String getSummaryMessage()
  {
    return messageArea.getText();
  }
  
  /**
   * Sets the component that displays the details of the message.  This component will only be displayed if
   * the user chooses to show the details.
   * @param component The component that displays the details of the message.
   */
  public void setMessageDetailsComponent(Component component)
  {
    Component existingComponent = toggleDetailsAction.getComponent();
    if(existingComponent != null)
    {
      existingComponent.removeComponentListener(componentVisibilityListener);
      remove(existingComponent);
    }
    toggleDetailsAction.setComponent(component);
    if(component != null)
    {
      component.addComponentListener(componentVisibilityListener);
      add(component, BorderLayout.CENTER);
    }
    invalidate();
    validate();
    repaint();
  }
  
  /**
   * Gets the component that displays the details of the message.  This component will only be displayed if
   * the user chooses to show the details.
   * @return The component that displays the details of the message.
   */
  public Component getMessageDetailsComponent()
  {
    return toggleDetailsAction.getComponent();
  }
  
  private class ComponentVisibilityListener extends ComponentAdapter
  {

    @Override
    public void componentHidden(ComponentEvent e)
    {
      firePropertyChange(DETAILS_VISIBLE_PROPERTY, true, false);
    }

    @Override
    public void componentShown(ComponentEvent e)
    {
      firePropertyChange(DETAILS_VISIBLE_PROPERTY, false, true);
    }
  }
}
