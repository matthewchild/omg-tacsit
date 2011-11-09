/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 1, 2011
 */
package org.omg.tacsit.common.ui.table;

import java.text.Format;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A cell renderer which uses a java.text.Format to convert a value to a String.
 * @author Matthew Child
 */
public class FormattedTextCellRenderer extends DefaultTableCellRenderer
{
  private Format format;
  
  /**
   * Creates a new instance.
   */
  public FormattedTextCellRenderer()
  {
    
  }

  /**
   * Creates a new instance.
   * @param format The format to use to convert rendered values to a String.
   */
  public FormattedTextCellRenderer(Format format)
  {
    this.format = format;
  }

  /**
   * Gets the format to use to convert rendered values to a String.
   * @return The format for converting values to a String.
   */
  public Format getFormat()
  {
    return format;
  }

  /**
   * Sets the format to use to convert rendered values to a String.
   * @param format The format for converting values to String.  May be null.
   */
  public void setFormat(Format format)
  {
    this.format = format;
  }
  
  private String getAsText(Object value)
  {
    String text;
    if(format != null)
    {
      text = format.format(value);
    }
    else if(value == null)
    {
      text = "";
    }
    else
    {
      text = value.toString();
    }
    return text;
  }

  @Override
  protected void setValue(Object value)
  {
    String objectAsText = getAsText(value);
    super.setValue(objectAsText);
  }  
}
