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
 * Serializes Angle objects to and from degree, minute, second Strings.
 * <p>
 * These strings will appear as $(degrees) $(minutes)' $(seconds)".  Note, the actual strings will not have
 * parenthesis.
 * @author Matthew Child
 */
public class DegreeMinuteSecondFormat extends Format
{
  private static final char DEGREE_SYMBOL = '\u00B0';  
  private static final char MINUTE_SYMBOL = '\u2019';
  private static final char SECOND_SYMBOL = '\u201d';
  
  private static final char NEGATIVE_SIGN = '-';
  
  private static final String FORMAT_PATTERN = "%d" + DEGREE_SYMBOL + 
                                               " %2d" + MINUTE_SYMBOL + 
                                               " %5.2f" + SECOND_SYMBOL;
    
  private static final String NEGATIVE_FORMAT_PATTERN = NEGATIVE_SIGN + FORMAT_PATTERN;
  
  
  private NumberFormat numberFormat;
  
  /**
   * Constructs a new instance.
   */
  public DegreeMinuteSecondFormat()
  {
    numberFormat = NumberFormat.getNumberInstance();
  }

  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    Angle angle = (Angle) obj;
    double angleAsDegrees = angle.getDegrees();

    double absAngleDegrees = Math.abs(angleAsDegrees);  // For display purposes, we only want to display a negative
                                                        // sign by the degree measurement.
    int degrees = (int) Math.floor(absAngleDegrees);
    double angleMinutes = (absAngleDegrees - degrees) * 60d;
    int minutes = (int) Math.floor(angleMinutes);
    double angleSeconds = (angleMinutes - minutes) * 60d;
    double seconds = Math.rint(angleSeconds * 100) / 100;  // keep two decimals for seconds

    if (seconds == 60)
    {
      minutes++;
      seconds = 0;
    } // Fix rounding errors
    if (minutes == 60)
    {
      degrees++;
      minutes = 0;
    }

    // We can't multiply the sign to the degrees, because degrees might be 0.  This would prevent you from
    // representing negative minute and second values with 0 degrees.
    int sign = (int) Math.signum(angleAsDegrees);
    if(sign >= 0)
    {
      String formattedString = String.format(FORMAT_PATTERN, degrees, minutes, seconds);
      toAppendTo.append(formattedString);
    }
    else
    {
      String formattedString = String.format(NEGATIVE_FORMAT_PATTERN, degrees, minutes, seconds);
      toAppendTo.append(formattedString);
    }
    

    return toAppendTo;
  }
  
  private void skipWhitespace(String source, ParsePosition pos)
  {
    int index = pos.getIndex();
    char charAtIndex = source.charAt(index);
    while(Character.isWhitespace(charAtIndex))
    {
      index++;
      charAtIndex = source.charAt(index);
    }
    pos.setIndex(index);
  }
  
  private void skipSymbol(String source, ParsePosition pos, char symbol)
  {
    int currentIndex = pos.getIndex();
    char degreeChar = source.charAt(currentIndex);
    if(degreeChar != symbol)
    {
      pos.setErrorIndex(currentIndex);
    }
    else
    {
      pos.setIndex(currentIndex + 1);
    }
  }
  
  private double parseNumber(String source, ParsePosition pos, char symbol)
  {
    double numberAsDouble = 0.0d;
    skipWhitespace(source, pos);    
    Number number = numberFormat.parse(source, pos);
    if(number != null)
    {
      numberAsDouble = number.doubleValue();
      skipSymbol(source, pos, symbol);
    }
    return numberAsDouble;
  }
  
  private double parseSign(String source, ParsePosition pos)
  {
    skipWhitespace(source, pos);
    int maybeSignIndex = pos.getIndex();
    char signChar = source.charAt(maybeSignIndex);
    if(signChar == NEGATIVE_SIGN)
    {
      pos.setIndex(maybeSignIndex + 1);
      return -1;
    }
    else
    {
      return 1;
    }
  }

  @Override
  public Object parseObject(String source, ParsePosition pos)
  {
    double signValue = parseSign(source, pos);
    
    double angle;
    
    double degrees = parseNumber(source, pos, DEGREE_SYMBOL);
    if(pos.getErrorIndex() != -1)
    {
      return null;
    }
    angle = degrees;
    
    double minutes = parseNumber(source, pos, MINUTE_SYMBOL);
    if(pos.getErrorIndex() != -1)
    {
      return null;
    }
    angle += minutes / 60d;
    
    double seconds = parseNumber(source, pos, SECOND_SYMBOL);
    if(pos.getErrorIndex() != -1)
    {
      return null;
    }
    angle += seconds / 3600d;
    
    // This has to be performed last, in case degrees and minutes are zero.  Multiplying them earlier
    // would cause the sign value to be ignored.
    angle = angle * signValue;
    
    return Angle.fromDegrees(angle);
  }
}
