/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 21, 2011
 */
package org.omg.tacsit.ui.entity;

import org.omg.tacsit.common.math.Angle;
import org.omg.tacsit.common.ui.table.DefaultListTableModel;
import org.omg.tacsit.entity.PositionedEntity;
import org.omg.tacsit.geometry.GeodeticPosition;

/**
 * A ListTableModel that displays and edits PositionedEntitys.
 * @author Matthew Child
 */
public class PositionedEntityTableModel extends DefaultListTableModel<PositionedEntity>
{

  /**
   * Creates a new instance.
   */
  public PositionedEntityTableModel()
  {
    super(PositionedEntity.class);
  }
  
  @Override
  public String getColumnName(int index)
  {
    Columns column = Columns.atIndex(index);
    return column.getPrintableName();
  }

  @Override
  public int getColumnCount()
  {
    return Columns.count();
  }

  @Override
  public Class<?> getColumnClass(int columnIndex)
  {
    Columns column = Columns.atIndex(columnIndex);
    return column.getValueClass();
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex)
  {
    return false;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex)
  {
    // Do nothing.  Entities are not editable at the PositionEntity interface level.
  }
  
  @Override
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    PositionedEntity entity = getRow(rowIndex);
    
    Columns column = Columns.atIndex(columnIndex);
    Object value = column.getValue(entity);
    return value;  
  }

  private enum Columns
  {

    LATITUDE("Latitude", Angle.class)
    {
      public Object getValue(PositionedEntity entity)
      {
        Double latitude = 0.0;
        GeodeticPosition position = getPosition(entity);
        if(position != null)
        {
          latitude = position.getLatitude();
        }
        return Angle.fromRadians(latitude);
      }
    },
    LONGITUDE("Longitude", Angle.class)
    {
      public Object getValue(PositionedEntity entity)
      {
        Double longitude = 0.0;
        GeodeticPosition position = getPosition(entity);
        if(position != null)
        {
          longitude = position.getLongitude();
        }
        return Angle.fromRadians(longitude);
      }
    },
    ALTITUDE("Altitude", Double.class)
    {
      public Object getValue(PositionedEntity entity)
      {
        Double altitude = 0.0;
        GeodeticPosition position = getPosition(entity);
        if(position != null)
        {
          altitude = position.getAltitude();
        }
        return altitude;
      }
    };

    private String printableName;
    private Class valueClass;
    
    Columns(String printableName, Class valueClass)
    {
      this.printableName = printableName;
      this.valueClass = valueClass;
    }
    
    private static GeodeticPosition getPosition(PositionedEntity item)
    {
      GeodeticPosition position = null;
      if(item != null)
      {
        position = item.getReferencePosition();
      }
      return position;
    }

    public abstract Object getValue(PositionedEntity entity);
    
    public Class getValueClass()
    {
      return valueClass;
    }
    
    public static Columns atIndex(int index)
    {
      Columns[] columns = Columns.values();
      return columns[index];
    }
    
    public static int count()
    {
      return values().length;
    }

    public String getPrintableName()
    {
      return printableName;
    }
  }

  @Override
  public String toString()
  {
    return "Track Entities";
  }
}
