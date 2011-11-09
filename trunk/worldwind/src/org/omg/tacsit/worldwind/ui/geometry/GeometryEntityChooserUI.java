/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 26, 2011
 */
package org.omg.tacsit.worldwind.ui.geometry;

import java.awt.Component;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.event.ListSelectionListener;
import org.omg.tacsit.common.ui.EditorComponent;
import org.omg.tacsit.common.ui.panel.MessagePanel;
import org.omg.tacsit.common.ui.panel.PropertyEditorPanel;
import org.omg.tacsit.common.ui.table.ListTableModel;
import org.omg.tacsit.ui.entity.EntityChooserUI;
import org.omg.tacsit.ui.entity.TableWithDetailsEntityChooserUI;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.geometry.Geometry;
import org.omg.tacsit.worldwind.entity.GeometryEntity;
import org.omg.tacsit.worldwind.entity.WWEntityType;
import org.omg.tacsit.worldwind.geometry.GeometryFactory;
import org.omg.tacsit.worldwind.geometry.WWSurfaceShape;

/**
 * An EntityChooser that allows selection of GeometryEntities.
 * <p>
 * The chooser will display geometry entities in a table.  The specific attributes of the shape will be editable
 * in an adjacent property editor.
 * @author Matthew Child
 */
public class GeometryEntityChooserUI implements EntityChooserUI
{
  private TableWithDetailsEntityChooserUI delegate;

  /**
   * Creates a new instance.
   */
  public GeometryEntityChooserUI()
  {
    this(null);
  }

  /**
   * Creates a new instance.
   * @param geometryFactory The factory that will be used to create new shapes for a GeometryEntity, if the user
   * elects to change them.
   */
  public GeometryEntityChooserUI(GeometryFactory geometryFactory)
  {
    ListTableModel<? extends Entity> tableModel = new GeometryEntityTableModel(geometryFactory);
    delegate = new TableWithDetailsEntityChooserUI(tableModel, WWEntityType.SURFACE_GEOMETRY);
    delegate.setDefaultTableCellEditor(WWSurfaceShape.class, new DefaultCellEditor(new JComboBox(WWSurfaceShape.values())));
    delegate.setSelectedComponentEditor(new GeometryEntityEditor());    
  }
  
  /**
   * Gets the name of the EntityChooser.
   * @return The name of the entity chooser.
   */
  public String getName()
  {
    return getComponent().getName();
  }
  
  /**
   * Sets the name of the EntityChooser.
   * @param name The new name of the entity chooser.
   */
  public void setName(String name)
  {
    getComponent().setName(name);
  }

  public Component getComponent()
  {
    return delegate.getComponent();
  }

  public Collection<Entity> getSelectedEntities()
  {
    return delegate.getSelectedEntities();
  }

  public void addListSelectionListener(ListSelectionListener listener)
  {
    delegate.addListSelectionListener(listener);
  }

  public void removeListSelectionListener(ListSelectionListener listener)
  {
    delegate.removeListSelectionListener(listener);
  }

  public void addEntity(Entity entity)
  {
    delegate.addEntity(entity);
  }

  public void removeEntity(Entity entity)
  {
    delegate.removeEntity(entity);
  }

  public void updateEntity(Entity entity)
  {
    delegate.updateEntity(entity);
  }

  public void clearEntities()
  {
    delegate.clearEntities();
  }

  public boolean isEntityAllowed(Entity entity)
  {
    return delegate.isEntityAllowed(entity);
  }
  
  /**
   * This is not made public since it's fairly internal to the way GeometryEntityChooserUI works.  It extracts
   * the geometry from the selected item, and views that instead.
   */
  private static class GeometryEntityEditor implements EditorComponent
  {
    private PropertyEditorPanel editorPanel;
    private GeometryEntity geometryEntity;
    private String name;
    private Object value;

    public GeometryEntityEditor()
    {
      this.name = "Geometry Editor";
      this.editorPanel = new PropertyEditorPanel();
      this.editorPanel.setNullValueComponent(createNullValueComponent());
    }
    
    private JComponent createNullValueComponent()
    {
      MessagePanel nullValuePanel = new MessagePanel("<html><center>Select a value in the left-hand table to edit the shape properties");      
      return nullValuePanel;
    }
    
    private GeometryEntity getGeometryEntity(Object value)
    {
      GeometryEntity entity;
      if(value instanceof Collection)
      {
        Iterator valueIterator = ((Collection)value).iterator();
        if(valueIterator.hasNext())
        {
          Object firstValue = valueIterator.next();
          entity = (GeometryEntity)firstValue;
        }
        else
        {
          entity = null;
        }
      }
      else
      {
        entity = (GeometryEntity)value;
      }
      return entity;
    }

    public void setValue(Object value)
    {
      this.value = value;
      geometryEntity = getGeometryEntity(value);
      
      Geometry geometry = null;
      if(geometryEntity != null)
      {
        geometry = geometryEntity.getGeometry();
      }
      
      this.editorPanel.setValue(geometry);
    }

    public Object getValue()
    {
      return value;
    }

    public String getName()
    {
      return name;
    }

    public JComponent getComponent()
    {
      return editorPanel;
    }
  }
}
