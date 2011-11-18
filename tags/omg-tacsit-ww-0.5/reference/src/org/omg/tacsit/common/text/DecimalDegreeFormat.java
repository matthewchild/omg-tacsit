/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 14, 2011
 */
package org.omg.tacsit.common.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.omg.tacsit.common.math.Angle;

/**
 * Serializes Angle objects to and from decimal degree Strings.
 * 
 * @author Matthew Child
 */
public class DecimalDegreeFormat extends Format
{  
  private NumberFormat degreeFormat;

  /**
   * Constructs a new instance.
   */
  public DecimalDegreeFormat()
  {
    degreeFormat = NumberFormat.getNumberInstance();
    degreeFormat.setMaximumFractionDigits(1);
  }

  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    Angle angle = (Angle)obj;
    Double degrees = angle.getDegrees();
    StringBuffer buffer = degreeFormat.format(degrees, toAppendTo, pos);
    return buffer;    
  }

  @Override
  public Object parseObject(String source, ParsePosition pos)
  {
    Angle angle = null;
    Number degrees = degreeFormat.parse(source, pos);
    if(degrees != null)
    {
      double degreeValue = degrees.doubleValue();
      angle = Angle.fromDegrees(degreeValue);
    }
    return angle;
  }
}
