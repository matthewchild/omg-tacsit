/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 22, 2011
 */
package org.omg.tacsit.ui.entity;

import java.util.Collection;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.omg.tacsit.common.ui.panel.PropertyEditorPanel;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.controller.EntityType;

/**
 * An EntityChooser component that displays the entities in a JList.
 * @author Matthew Child
 */
public class ListEntityChooserUI implements EntityChooserUI
{
  private DefaultListModel entityListModel;
  private JList entityList;
  private PropertyEditorPanel propertyEditorPanel;
  
  private JPanel componentPanel;
  
  /**
   * Creates a new instance.
   */
  public ListEntityChooserUI()
  {
    initGUI();
  }  
  
  private void initGUI()
  {
    componentPanel = new JPanel(new BorderLayout());
    componentPanel.setName("Entity List");
    JComponent listComponent = initListComponent();
    JComponent editorComponent = initEditorComponent();
    
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listComponent, editorComponent);
    splitPane.setDividerSize(4);
    splitPane.setDividerLocation(100);
    componentPanel.add(splitPane, BorderLayout.CENTER);
  }
  
  private JComponent initListComponent()
  {
    entityListModel = new DefaultListModel();
    entityList = new JList(entityListModel);
    entityList.addListSelectionListener(new EntitySelectionListener());
    entityList.setCellRenderer(new EntityCellRenderer());
    JScrollPane scrollPane = new JScrollPane(entityList);
    
    return scrollPane;
  }
  
  private JComponent initEditorComponent()
  {
    propertyEditorPanel = new PropertyEditorPanel();
    
    return propertyEditorPanel;
  }

  @Override
  public boolean isEntityAllowed(Entity entity)
  {
    return true;
  }

  @Override
  public void addEntity(Entity entity)
  {
    entityListModel.addElement(entity);
    ensureSelectionExists();
  }
  
  private void ensureSelectionExists()
  {
    if((!entityListModel.isEmpty()) && (entityList.getSelectedIndex() == -1))
    {      
      entityList.setSelectedIndex(0);
    }
  }

  @Override
  public void removeEntity(Entity entity)
  {
    entityListModel.removeElement(entity);
  }
  

  public void updateEntity(Entity entity)
  {
    int index = entityListModel.indexOf(entity);
    entityListModel.setElementAt(entity, index);
  }

  @Override
  public void clearEntities()
  {
    entityListModel.clear();
  }
  
  /**
   * Sets the CellRenderer the List uses for rendering the entities.
   * @param renderer The renderer that draws the entities.
   */
  public void setCellRenderer(ListCellRenderer renderer)
  {
    entityList.setCellRenderer(renderer);
  }
  
  private void reloadEntitySelection()
  {
    Object value = entityList.getSelectedValue();
    propertyEditorPanel.setBorder(BorderFactory.createEmptyBorder());
    propertyEditorPanel.setValue(value);
  }
  
  /**
   * Gets the name of the EntityChooser.
   * @return The name of the EntityChooser
   */
  public String getName()
  {
    return getComponent().getName();
  }
  
  /**
   * Sets the name of the EntityChooser
   * @param name The name of the EntityChooser
   */
  public void setName(String name)
  {
    getComponent().setName(name);
  }

  public Component getComponent()
  {
    return componentPanel;
  }

  public Collection<Entity> getSelectedEntities()
  {
    Object[] values = entityList.getSelectedValues();
    Collection selectedValues = Arrays.asList(values);
    return (Collection<Entity>)selectedValues;
  }

  public void addListSelectionListener(ListSelectionListener listener)
  {
    entityList.getSelectionModel().addListSelectionListener(listener);
  }

  public void removeListSelectionListener(ListSelectionListener listener)
  {
    entityList.getSelectionModel().removeListSelectionListener(listener);
  }
  
  private static class EntityCellRenderer extends DefaultListCellRenderer 
  {
    private static final String INVALID_TEXT = "[invalid]";
    
    private String getAsString(Entity entity)
    {
      StringBuilder builder = new StringBuilder();
      EntityType type = entity.getType();
      builder.append(type);
      if(!entity.isValid())
      {
        builder.append(INVALID_TEXT);
      }
      return builder.toString();
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus)
    {      
      Entity entity = (Entity)value;
      String entityAsText = getAsString(entity);
      return super.getListCellRendererComponent(list, entityAsText, index, isSelected, cellHasFocus);
    }
  }
  
  private class EntitySelectionListener implements ListSelectionListener
  {

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
      reloadEntitySelection();
    }
  }
}
