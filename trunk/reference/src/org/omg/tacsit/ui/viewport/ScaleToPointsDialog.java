/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 26, 2011
 */
package org.omg.tacsit.ui.viewport;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.omg.tacsit.common.ui.ToggleComponentShownAction;
import org.omg.tacsit.controller.Viewport;
import org.omg.tacsit.query.QueryManager;

/**
 * A dialog that demonstrates a Viewport's ScaleToPoints functionality.
 * @author Matthew Child
 */
public class ScaleToPointsDialog extends ViewportDialog
{
  private ScaleToEntitiesPanel scaleToEntitiesPanel;
  private ScaleToPositionsPanel scaleToPositionsPanel;

  /**
   * Creates a new instance
   * @param owner The window that owns the dialog.
   */
  public ScaleToPointsDialog(Window owner)
  {
    super(owner);
    super.setBaseTitle("Scale Viewport");
    initGUI();
  }

  private void initGUI()
  {
    JPanel contentPanel = new JPanel(new BorderLayout());    
    contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
    JComponent contentTabs = createContentTabs();
    contentPanel.add(contentTabs, BorderLayout.CENTER);
    
    JComponent dialogCloseComponent = createDialogCloseComponent();
    contentPanel.add(dialogCloseComponent, BorderLayout.SOUTH);
    
    setContentPane(contentPanel);    
  }
  
  private JComponent createContentTabs()
  {    
    JTabbedPane tabPane = new JTabbedPane();
    
    scaleToEntitiesPanel = new ScaleToEntitiesPanel();
    scaleToEntitiesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    tabPane.addTab("Scale To Tracks", scaleToEntitiesPanel);
    
    scaleToPositionsPanel = new ScaleToPositionsPanel();
    scaleToPositionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    tabPane.addTab("Scale To Positions", scaleToPositionsPanel);
    
    return tabPane;
  }
  
  private JComponent createDialogCloseComponent()
  {
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

    ToggleComponentShownAction hideDialogAction = new ToggleComponentShownAction("Close",
                                                                                 "Show" /* this will never be visible.*/,        
                                                                                 this);
    JButton hideDialogButton = new JButton(hideDialogAction);
    bottomPanel.add(hideDialogButton);

    return bottomPanel;
  }

  /**
   * Sets the QueryManager that is used for selecting Entities to scale to.
   * @param queryManager the QueryManager to use.
   */
  public void setQueryManager(QueryManager queryManager)
  {
    scaleToEntitiesPanel.setQueryManager(queryManager);
  }

  /**
   * Gets the QueryManager that is used for selecting Entities to scale to.
   * @return The QueryManager to use
   */
  public QueryManager getQueryManager()
  {
    return scaleToEntitiesPanel.getQueryManager();
  }

  @Override
  public void setViewport(Viewport viewport)
  {
    super.setViewport(viewport);
    scaleToPositionsPanel.setViewportToScale(viewport);
    scaleToEntitiesPanel.setViewportToScale(viewport);
  }
}
