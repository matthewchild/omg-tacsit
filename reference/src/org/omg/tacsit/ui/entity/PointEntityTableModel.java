/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 21, 2011
 */
package org.omg.tacsit.ui.entity;

import org.omg.tacsit.common.math.Angle;
import org.omg.tacsit.common.ui.table.DefaultListTableModel;
import org.omg.tacsit.entity.PointEntity;
import org.omg.tacsit.geometry.DefaultGeodeticPosition;
import org.omg.tacsit.geometry.GeodeticPosition;

/**
 * A ListTableModel that displays and edits PointEntities.
 * @author Matthew Child
 */
public class PointEntityTableModel extends DefaultListTableModel<PointEntity>
{

  /**
   * Creates a new instance.
   */
  public PointEntityTableModel()
  {
    super(PointEntity.class);
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
    return Column.count();
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
    return getRow(rowIndex) != null;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex)
  {
    PointEntity entity = getRow(rowIndex);
    Column column = Column.atIndex(columnIndex);
    column.setValue(entity, aValue);
  }
  
  @Override
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    PointEntity entity = getRow(rowIndex);
    
    Column column = Column.atIndex(columnIndex);
    Object value = column.getValue(entity);
    return value;  
  }

  private enum Column
  {

    LATITUDE("Latitude", Angle.class)
    {
      public Object getValue(PointEntity entity)
      {
        Double latitude = 0.0;
        GeodeticPosition position = getPosition(entity);
        if(position != null)
        {
          latitude = position.getLatitude();
        }
        return Angle.fromRadians(latitude);
      }
      
      public void setValue(PointEntity entity, Object newLatitude)
      {
        GeodeticPosition oldPosition = getPosition(entity);
        Angle latitude = (Angle)newLatitude;
        GeodeticPosition newPosition = DefaultGeodeticPosition.fromRadians(latitude.getRadians(), oldPosition.getLongitude(), oldPosition.getAltitude());
        setPosition(entity, newPosition);
      }
    },
    LONGITUDE("Longitude", Angle.class)
    {
      public Object getValue(PointEntity entity)
      {
        Double longitude = 0.0;
        GeodeticPosition position = getPosition(entity);
        if(position != null)
        {
          longitude = position.getLongitude();
        }
        return Angle.fromRadians(longitude);
      }
      
      public void setValue(PointEntity entity, Object newLongitude)
      {
        GeodeticPosition oldPosition = getPosition(entity);
        Angle longitude = (Angle)newLongitude;
        GeodeticPosition newPosition = DefaultGeodeticPosition.fromRadians(oldPosition.getLatitude(), longitude.getRadians(), oldPosition.getAltitude());
        setPosition(entity, newPosition);
      }
    },
    ALTITUDE("Altitude", Double.class)
    {
      public Object getValue(PointEntity entity)
      {
        Double altitude = 0.0;
        GeodeticPosition position = getPosition(entity);
        if(position != null)
        {
          altitude = position.getAltitude();
        }
        return altitude;
      }
      
      public void setValue(PointEntity entity, Object newAltitude)
      {
        GeodeticPosition oldPosition = getPosition(entity);
        Double altitude = ((Number)newAltitude).doubleValue();
        GeodeticPosition newPosition = DefaultGeodeticPosition.fromRadians(oldPosition.getLatitude(), 
                                                                           oldPosition.getLongitude(), 
                                                                           altitude);
        setPosition(entity, newPosition);
      }
    };

    private String printableName;
    private Class valueClass;
    
    Column(String printableName, Class valueClass)
    {
      this.printableName = printableName;
      this.valueClass = valueClass;
    }
    
    private static GeodeticPosition getPosition(PointEntity item)
    {
      GeodeticPosition position = null;
      if(item != null)
      {
        position = item.getReferencePosition();
      }
      return position;
    }
    
    private static void setPosition(PointEntity item, GeodeticPosition position)
    {
      item.setReferencePosition(position);
    }

    public abstract Object getValue(PointEntity entity);
    public abstract void setValue(PointEntity entity, Object value);
    
    public Class getValueClass()
    {
      return valueClass;
    }
    
    public static Column atIndex(int index)
    {
      Column[] columns = Column.values();
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
    return "Point Entities";
  }
}
