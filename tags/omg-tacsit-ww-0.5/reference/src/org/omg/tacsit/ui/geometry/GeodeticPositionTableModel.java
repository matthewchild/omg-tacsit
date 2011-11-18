/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 26, 2011
 */
package org.omg.tacsit.ui.geometry;

import org.omg.tacsit.common.math.Angle;
import org.omg.tacsit.common.math.Distance;
import org.omg.tacsit.common.ui.table.DefaultListTableModel;
import org.omg.tacsit.geometry.DefaultGeodeticPosition;
import org.omg.tacsit.geometry.GeodeticPosition;

/**
 * A ListTableModel that displays and edits GeodeticPositions.
 * <p>
 * GeodeticPosition provides no public setter methods.  In order to editing a particular row, a new GeodeticPosition
 * is created with the appropriate edited value.
 * @author Matthew Child
 */
public class GeodeticPositionTableModel extends DefaultListTableModel<GeodeticPosition>
{

  /**
   * Creates a new instance.
   */
  public GeodeticPositionTableModel()
  {
    super(GeodeticPosition.class);
  }
  
  private DefaultGeodeticPosition getRowAsDefaultPosition(int row)
  {
    GeodeticPosition positionAtRow = getRow(row);
    return DefaultGeodeticPosition.toDefaultGeodeticPosition(positionAtRow);
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
    DefaultGeodeticPosition positionAtRow = getRowAsDefaultPosition(rowIndex);

    Column column = Column.atIndex(columnIndex);
    GeodeticPosition newPosition;
    switch (column)
    {
      case LATITUDE:
        newPosition = positionAtRow.newLatitude((Angle) aValue);
        break;
      case LONGITUDE:
        newPosition = positionAtRow.newLongitude((Angle) aValue);
        break;
      case ALTITUDE:
        double newValue = ((Number)aValue).doubleValue();
        Distance newAltitude = Distance.fromMeters(newValue);
        newPosition = positionAtRow.newAltitude(newAltitude);
        break;
      default:
        throw new IllegalArgumentException("Column " + column + " attribute not defined");
    }
    set(rowIndex, newPosition);
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    Object value;
    DefaultGeodeticPosition position = getRowAsDefaultPosition(rowIndex);

    Column column = Column.atIndex(columnIndex);
    switch (column)
    {
      case LATITUDE:
        value = position.getLatitudeAngle();
        break;
      case LONGITUDE:
        value = position.getLongitudeAngle();
        break;
      case ALTITUDE:
        value = position.getAltitude();
        break;
      default:
        throw new IllegalArgumentException("Column " + column + " attribute not defined");

    }
    return value;
  }

  private enum Column
  {

    LATITUDE("Latitude", Angle.class),
    LONGITUDE("Longitude", Angle.class),
    ALTITUDE("Altitude", Double.class);
    private String printableName;
    private Class valueClass;

    Column(String printableName, Class valueClass)
    {
      this.printableName = printableName;
      this.valueClass = valueClass;
    }

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
    return "Point Table";
  }
}
