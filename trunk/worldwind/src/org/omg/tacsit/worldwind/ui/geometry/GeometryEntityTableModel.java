/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 12, 2011
 */
package org.omg.tacsit.worldwind.ui.geometry;

import org.omg.tacsit.common.ui.table.DefaultListTableModel;
import org.omg.tacsit.worldwind.entity.GeometryEntity;
import org.omg.tacsit.worldwind.geometry.GeometryFactory;
import org.omg.tacsit.worldwind.geometry.WWSurfaceGeometry;
import org.omg.tacsit.worldwind.geometry.WWSurfaceShape;

/**
 * A table model that allows the editing of common attributes of a GeometryEntity.
 * @author Matthew Child
 */
public class GeometryEntityTableModel extends DefaultListTableModel<GeometryEntity>
{
  private GeometryFactory geometryFactory;

  /**
   * Creates a new instance.
   */
  public GeometryEntityTableModel()
  {
    this(null);
  }
  
  /**
   * Creates a new instance.
   * @param geometryFactory The geometry factory that will be used to set new values for a GeometryEntity's
   * <code>geometry</code> property.
   */
  public GeometryEntityTableModel(GeometryFactory geometryFactory)
  {
    super(GeometryEntity.class);
    this.geometryFactory = geometryFactory;
  }
  
  @Override
  public String getColumnName(int index)
  {
    Column column = Column.atIndex(index);
    return column.getPrintableName();
  }

  @Override
  public int getColumnCount()
  {
    return Column.values().length;
  }

  @Override
  public Class<?> getColumnClass(int columnIndex)
  {
    Column column = Column.atIndex(columnIndex);
    return column.getValueClass();
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex)
  {
    boolean isEditable = false;
    if(getRow(rowIndex) != null)
    {
      if(columnIndex == Column.GEOMETRY.getColumn())
      {
        isEditable = geometryFactory != null;
      }
      else
      {
        isEditable = true;
      }
    }
    return isEditable;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex)
  {
    GeometryEntity entity = getRow(rowIndex);
    Column column = Column.atIndex(columnIndex);
    column.setValue(entity, geometryFactory, aValue);
  }
  
  @Override
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    GeometryEntity entity = getRow(rowIndex);
    
    Column column = Column.atIndex(columnIndex);
    Object value = column.getValue(entity);
    return value;  
  }

  private enum Column
  {

    NAME("Name", String.class)
    {
      public Object getValue(GeometryEntity entity)
      {
        String name = null;
        if(entity != null)
        {
          name = entity.getName();
        }
        return name;
      }
      
      public void setValue(GeometryEntity entity, GeometryFactory geometryFactory, Object value)
      {
        entity.setName((String)value);
      }
    },
    GEOMETRY("Geometry", WWSurfaceShape.class)
    {
      public Object getValue(GeometryEntity entity)
      {
        WWSurfaceShape shape = null; 
        if(entity != null)
        {
          WWSurfaceGeometry geometry = entity.getGeometry();
          if(geometry != null)
          {
            shape = WWSurfaceShape.forClass(geometry.getClass());
          }
        }
        
        return shape;
      }
      
      public void setValue(GeometryEntity entity, GeometryFactory geometryFactory, Object value)
      {
        WWSurfaceGeometry newGeometry = null;
        WWSurfaceShape shape = (WWSurfaceShape)value;
        if(shape != null)
        {
          newGeometry = geometryFactory.createGeometry(shape);
        }
        entity.setGeometry(newGeometry);
      }
    };

    private String printableName;
    private Class valueClass;
    
    Column(String printableName, Class valueClass)
    {
      this.printableName = printableName;
      this.valueClass = valueClass;
    }

    public abstract Object getValue(GeometryEntity entity);
    public abstract void setValue(GeometryEntity entity, GeometryFactory geometryFactory, Object value);
    
    public Class getValueClass()
    {
      return valueClass;
    }
    
    public int getColumn()
    {
      int myIndex = -1;
      Column[] columns = Column.values();
      for (int index = 0; index < columns.length; index++)
      {
        Column column = columns[index];
        if(column.equals(this))
        {
          myIndex = index;
          break;
        }
      }
      if(myIndex == -1)
      {
        throw new IllegalStateException("Couldn't find column index in enumeration.");
      }
      return myIndex;      
    }
    
    public static Column atIndex(int index)
    {
      Column[] columns = Column.values();
      return columns[index];
    }

    public String getPrintableName()
    {
      return printableName;
    }
  }

  @Override
  public String toString()
  {
    return "Surface Geometry Entities";
  }
}
