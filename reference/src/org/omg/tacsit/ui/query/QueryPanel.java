/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 21, 2011
 */
package org.omg.tacsit.ui.query;

import org.omg.tacsit.ui.entity.EntityChooserAggregatorPanel;
import org.omg.tacsit.ui.entity.EntityChooserUI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import org.omg.tacsit.common.ui.panel.PropertyEditorPanel;
import org.omg.tacsit.common.ui.ConfigurableAction;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.query.EntityQuery;
import org.omg.tacsit.query.QueryManager;

/**
 * A panel used to execute queries, and display the results.
 * @author Matthew Child
 */
public class QueryPanel extends JPanel
{
  
  private DefaultComboBoxModel queryComboModel;
  private JComboBox queryCombo;
    
  private PropertyEditorPanel queryEditPanel;
  
  private RunQueryAction runQueryAction;
  
  private EntityChooserAggregatorPanel queryResultsPanel;

  /**
   * Creates a new instance.
   */
  public QueryPanel()
  {
    super(new BorderLayout(5, 5));
    initGUI();
  }
    
  private void initGUI()
  {
    JComponent queryChooserPanel = initQueryChooserPanel();
    
    JComponent queryViewPanel = initQueryViewPanel();
    
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, queryChooserPanel, queryViewPanel);
    splitPane.setDividerLocation(300);
    add(splitPane, BorderLayout.CENTER);
  }
  
  private JComponent initQueryChooserPanel()
  {
    JPanel queryChooserPanel = new JPanel(new BorderLayout(5, 15));
    
    JComponent listSelector = initListSelector();
    queryChooserPanel.add(listSelector, BorderLayout.NORTH);
    
    JComponent queryEditor = initQueryEditor();
    queryChooserPanel.add(queryEditor, BorderLayout.CENTER);
    
    JComponent queryButtons = initQueryButtons();
    queryChooserPanel.add(queryButtons, BorderLayout.SOUTH);
    
    queryChooserPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
    return queryChooserPanel;
  }  

  private JComponent initListSelector()
  {
    JPanel queryPanel = new JPanel(new BorderLayout(0, 5));
    
    JLabel selectorLabel = new JLabel("Choose a query type:");
    queryPanel.add(selectorLabel, BorderLayout.NORTH);
    
    queryPanel.add(Box.createHorizontalStrut(15), BorderLayout.WEST);
    
    queryComboModel = new DefaultComboBoxModel();    
    queryCombo = new JComboBox(queryComboModel);
    queryCombo.addItemListener(new QuerySelectionListener());
    queryCombo.setRenderer(new QueryRenderer());
    queryPanel.add(queryCombo, BorderLayout.CENTER);
    
    return queryPanel;
  }
  
  private JComponent initQueryEditor()
  {
    queryEditPanel = new PropertyEditorPanel();
    Border editPanelBorder = BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Configure Query"), 
                                                           BorderFactory.createEmptyBorder(5, 5, 5, 5));
    queryEditPanel.setBorder(editPanelBorder);
    return queryEditPanel;
  }
  
  private JComponent initQueryButtons()
  {
    JPanel queryButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    
    runQueryAction = new RunQueryAction();
    JButton runQueryButton = new JButton(runQueryAction);
    queryButtonPanel.add(runQueryButton);
    
    return queryButtonPanel;    
  }

  private JComponent initQueryViewPanel()
  {
    queryResultsPanel = new EntityChooserAggregatorPanel();
    queryResultsPanel.setInitialMessage("<html><center>Choose a query from the left hand panel to see a matching list of entitites.");
    queryResultsPanel.setEmptyEntitiesText("Query returned no results.");
    return queryResultsPanel;
  }

  /**
   * Adds an EntityChooser that may be used to display results of the query.
   * @param chooser The chooser to add.
   */
  public void addEntityChooser(EntityChooserUI chooser)
  {
    queryResultsPanel.addEntityChooser(chooser);
  }
  
  /**
   * Adds a query that should be selectable to be executed.
   * @param query The new query to add.
   */
  public void addQuery(EntityQuery query)
  {
    queryComboModel.addElement(query);
  }
  
  private void refreshSelectedQuery()
  {
    Object selectedItem = queryCombo.getSelectedItem();
    EntityQuery selectedQuery = (EntityQuery)selectedItem;
    queryEditPanel.setValue(selectedQuery);
    runQueryAction.setQuery(selectedQuery);
  }

  /**
   * Sets the QueryManager that queries are executed against.
   * @param queryManager The QueryManager to execute queries on.
   */
  public void setQueryManager(QueryManager queryManager)
  {
    runQueryAction.setQueryManager(queryManager);
  }

  /**
   * Gets the QueryManager that queries are executed against.
   * @return The QueryManager that executes queries.
   */
  public QueryManager getQueryManager()
  {
    return runQueryAction.getQueryManager();
  }
  
  private class QuerySelectionListener implements ItemListener
  {

    @Override
    public void itemStateChanged(ItemEvent e)
    {
      refreshSelectedQuery();
    }
  }
  
  private class RunQueryAction extends ConfigurableAction
  {
    private EntityQuery query;
    private QueryManager queryManager;

    public RunQueryAction()
    {
      super("Run Query");
    }

    public EntityQuery getQuery()
    {
      return query;
    }

    public void setQuery(EntityQuery query)
    {
      this.query = query;
      checkEnabledState();
    }

    public QueryManager getQueryManager()
    {
      return queryManager;
    }

    public void setQueryManager(QueryManager queryManager)
    {
      this.queryManager = queryManager;
      checkEnabledState();
    }

    @Override
    public boolean isPerformable()
    {
      return (this.query != null) && (this.queryManager != null);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
      Collection<Entity> results = queryManager.submitEntityQuery(query);
      queryResultsPanel.setEntities(results);
    }    
  }
  
  private class QueryRenderer extends DefaultListCellRenderer
  {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus)
    {
      Class valueClass = value.getClass();
      String simpleName = valueClass.getSimpleName();
      return super.getListCellRendererComponent(list, simpleName, index, isSelected, cellHasFocus);
    }
  }
}
