/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 30, 2011
 */
package org.omg.tacsit.ui.viewport;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.omg.tacsit.common.math.Angle;
import org.omg.tacsit.common.ui.ComponentUtils;
import org.omg.tacsit.common.ui.layouts.FatStackedLayout;
import org.omg.tacsit.common.ui.table.AddToTableAction;
import org.omg.tacsit.common.ui.table.ClearTableAction;
import org.omg.tacsit.common.ui.table.RemoveFromTableAction;
import org.omg.tacsit.common.util.Factory;
import org.omg.tacsit.controller.Viewport;
import org.omg.tacsit.geometry.DefaultGeodeticPosition;
import org.omg.tacsit.geometry.GeodeticPosition;
import org.omg.tacsit.ui.resources.ActionIcons;
import org.omg.tacsit.ui.resources.DecorationIcons;
import org.omg.tacsit.ui.geometry.GeodeticPositionTableModel;
import org.omg.tacsit.ui.TableDefaults;

/**
 * A panel that allows the user to select a set of points to scale a viewport to.
 * @author Matthew Child
 */
public class ScaleToPositionsPanel extends JPanel
{ 
  private GeodeticPositionTableModel positionTableModel;
  private JFormattedTextField marginField;
  private JTable positionTable;
  private RemoveFromTableAction<GeodeticPosition> deleteFromTableAction;
  private ScaleToPointsAction scaleToPointsAction;

  /**
   * Creates a new instnace.
   */
  public ScaleToPositionsPanel()
  {
    initGUI();
    addDefaultTableData();
  }
  
  private void addDefaultTableData()
  {
    positionTableModel.add(DefaultGeodeticPosition.ZERO);
    positionTableModel.add(DefaultGeodeticPosition.fromDegrees(0.0, 1.0, 0.0));
  }

  private void initGUI()
  {
    setLayout(new BorderLayout());
    
    JComponent descriptionComponent = createDescriptionComponent();
    descriptionComponent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(descriptionComponent, BorderLayout.NORTH);
    
    JComponent pointEntryPanel = initPointEntryPanel();
    Border pointEntryBorder = BorderFactory.createTitledBorder("Scale Points");
    pointEntryPanel.setBorder(pointEntryBorder);    
    add(pointEntryPanel, BorderLayout.CENTER);
    
    JComponent scaleAndMarginComponent = initDoScaleAndMarginComponent();
    Insets pointEntryInsets = pointEntryPanel.getInsets();
    scaleAndMarginComponent.setBorder(BorderFactory.createEmptyBorder(pointEntryInsets.top, 30, 0, 30));
    add(scaleAndMarginComponent, BorderLayout.EAST);
  }
  
  private JComponent initDoScaleAndMarginComponent()
  {
    JPanel scaleAndMarginPanel = new JPanel(new BorderLayout(5, 5));
    
    final double INITIAL_MARGIN_VALUE = 0;
    
    JComponent marginComponent = initMarginComponent(INITIAL_MARGIN_VALUE);
    scaleAndMarginPanel.add(marginComponent, BorderLayout.NORTH);
    
    JComponent scaleComponent = initDoScaleComponent(INITIAL_MARGIN_VALUE);
    scaleAndMarginPanel.add(scaleComponent, BorderLayout.CENTER);
    
    return scaleAndMarginPanel;
  }
  
  private JComponent initDoScaleComponent(double initialMarginValue)
  {
    JPanel scalePanel = new JPanel(new FatStackedLayout(5));
    scalePanel.add(new JLabel(DecorationIcons.WORLD_ZOOM_128));
    
    scaleToPointsAction = new ScaleToPointsAction("Scale Viewport", null);
    scaleToPointsAction.setMargin(initialMarginValue);    
    JButton scaleToPointsButton = new JButton(scaleToPointsAction);   
    scaleToPointsButton.setVerticalTextPosition(JButton.BOTTOM);
    scaleToPointsButton.setHorizontalTextPosition(JButton.CENTER);
    scalePanel.add(scaleToPointsButton);
    
    // The default behavior of grid bag layout is to center the component in the panel.
    JPanel doScaleComponent = new JPanel(new GridBagLayout());
    doScaleComponent.add(scalePanel);
    
    return doScaleComponent;
  }
  
  private JComponent initMarginComponent(double initialMarginValue)
  {        
    JPanel marginPanel = new JPanel(new BorderLayout(5, 5));
    marginPanel.add(new JLabel("Margin:"), BorderLayout.NORTH);
        
    marginPanel.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
    
    marginField = new JFormattedTextField(NumberFormat.getNumberInstance());
    marginField.setColumns(12);
    marginField.setHorizontalAlignment(JFormattedTextField.RIGHT);
    
    // Set value must be performed before the property change listener is added,
    // because the scale to points action may not have been initialized.
    marginField.setValue(initialMarginValue);
    marginField.addPropertyChangeListener(ComponentUtils.FORMATTED_FIELD_VALUE_PROPERTY, new MarginValueListener());    
    marginPanel.add(marginField, BorderLayout.CENTER);
    
    marginPanel.add(new JLabel("m"), BorderLayout.EAST);
    
    return marginPanel;
  }
  
  private JComponent createDescriptionComponent()
  {
    JPanel descriptionPanel = new JPanel(new BorderLayout());
    
    JTextArea textArea = new JTextArea();
    ComponentUtils.configureAsLabel(textArea);
    textArea.setColumns(30);
    textArea.setText("Choose a list of points (minimum of 1) to use as a basis for scaling the viewport.  The viewport"
        + " will reorient to fit as tightly as possible around the associated points, while keeping the given margin"
        + " of distance around them.");
    descriptionPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
    
    return textArea;
  }
  
  private JComponent initPointEntryPanel()
  {
    JPanel pointEntryPanel = new JPanel(new BorderLayout(5, 5));
    
    positionTableModel = new GeodeticPositionTableModel();
    positionTableModel.addTableModelListener(new PositionModelListener());
    
    JComponent tableComponent = initTableComponent(positionTableModel);
    pointEntryPanel.add(tableComponent, BorderLayout.CENTER);
    
    JComponent tableButtons = initTableButtons(positionTableModel);
    pointEntryPanel.add(tableButtons, BorderLayout.EAST);
    
    return pointEntryPanel;
  }
  
  private JComponent initTableButtons(GeodeticPositionTableModel tableModel)
  {
    JPanel tableButtons = new JPanel(new FatStackedLayout(5));
    
    AddToTableAction addToTableAction = new AddToTableAction("Add", ActionIcons.ADD_24);
    addToTableAction.setTableModel(tableModel);
    addToTableAction.setNewObjectFactory(GEO_POSITION_FACTORY);
    JButton addToTableButton = new JButton(addToTableAction);
    addToTableButton.setHorizontalAlignment(SwingConstants.LEFT);
    tableButtons.add(addToTableButton);
    
    deleteFromTableAction = new RemoveFromTableAction("Delete", ActionIcons.DELETE_24);
    deleteFromTableAction.setTableModel(tableModel);
    JButton deleteFromTableButton = new JButton(deleteFromTableAction);
    deleteFromTableButton.setHorizontalAlignment(SwingConstants.LEFT);
    tableButtons.add(deleteFromTableButton);
    
    tableButtons.add(Box.createVerticalStrut(50));
    
    ClearTableAction clearTableAction = new ClearTableAction("Clear", ActionIcons.CLEAR_24);
    clearTableAction.setTableModel(tableModel);
    JButton clearTableButton = new JButton(clearTableAction);
    clearTableButton.setHorizontalAlignment(SwingConstants.LEFT);
    tableButtons.add(clearTableButton);
    
    return tableButtons;
  }

  private JComponent initTableComponent(TableModel model)
  {
    positionTable = new JTable(model);
    TableDefaults.initializeEditorAndRendererForTable(positionTable, Angle.class);
    positionTable.getSelectionModel().addListSelectionListener(new SelectedPointsListener());
    JScrollPane tableScroll = new JScrollPane(positionTable);
    tableScroll.setPreferredSize(new Dimension(300, 300));
    return tableScroll;
  }
  
  /**
   * Gets the viewport that should be scaled.
   * @return The viewport to scale.
   */
  public Viewport getViewportToScale()
  {
    return scaleToPointsAction.getViewportToScale();
  }

  /**
   * Sets the viewport that should be scaled.
   * @param viewport The viewport to scale.
   */
  public void setViewportToScale(Viewport viewport)
  {
    scaleToPointsAction.setViewportToScale(viewport);
  }
  
  private List<GeodeticPosition> getSelectedRowsInTable()
  {      
    int[] selectedRowViewIndices = positionTable.getSelectedRows();
    if(selectedRowViewIndices == null)
    {
      return Collections.emptyList();
    }
    if(selectedRowViewIndices.length == 0)
    {
      return Collections.emptyList();
    }

    List<GeodeticPosition> selectedRows = new ArrayList(); 
    for (int selectedRowViewIndex : selectedRowViewIndices)
    {
      int modelIndex = positionTable.convertRowIndexToModel(selectedRowViewIndex);
      GeodeticPosition positionForRow = positionTableModel.getRow(modelIndex);
      selectedRows.add(positionForRow);        
    }
    return selectedRows;
  }
  
  private static GeodeticPositionFactory GEO_POSITION_FACTORY = new GeodeticPositionFactory();
  
  private static class GeodeticPositionFactory implements Factory<GeodeticPosition>
  {

    public GeodeticPosition createObject()
    {
      // Since instances of DefaultGeodeticPosition are immutable, we can always return the same one
      // and it will make no difference.
      return DefaultGeodeticPosition.ZERO;
    }
  }
  
  private class PositionModelListener implements TableModelListener
  {

    public void tableChanged(TableModelEvent e)
    {
      List<? extends GeodeticPosition> rows = positionTableModel.getRows();
      scaleToPointsAction.setScalePoints(rows);
    }    
  }
  
  private class SelectedPointsListener implements ListSelectionListener
  {

    public void valueChanged(ListSelectionEvent e)
    {
      List<GeodeticPosition> selectedRows = getSelectedRowsInTable();
      deleteFromTableAction.setObjectsToRemove(selectedRows);
    }
  }
  
  private class MarginValueListener implements PropertyChangeListener
  {
    public void propertyChange(PropertyChangeEvent evt)
    {
      Number fieldValue = (Number)marginField.getValue();
      
      double margin; 
      if(fieldValue != null)
      {
        margin = fieldValue.doubleValue();
      }
      else
      {
        margin = 0;
      }
      scaleToPointsAction.setMargin(margin);
    }
  }
}
