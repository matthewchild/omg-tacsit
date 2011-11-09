/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 5, 2011
 */
package org.omg.tacsit.common.math;

/**
 * A representation of the space between two points.
 * <p>
 * A Distance can only be constructed by the various static methods fromFeet(), fromMeters(), fromMiles(), etc.  
 * Since distance measurements are typically stored as doubles (regardless of whether it's feet, meters, or any other),
 * this approach was adopted to make it clear which measurement was intended when constructing a Distance.
 * <p>
 * Instances of Distance are immutable.
 * 
 * @author Matthew Child
 */
public class Distance
{
  /**
   * A distance with no extent (all distance values are 0).
   */
  public static final Distance ZERO = Distance.fromMeters(0);
  private static final double METERS_PER_KILOMETER = 1000;
  private static final double METERS_PER_NAUTICAL_MILE = 1852;
  private static final double FEET_PER_METER = 3.2808399;
  private static final double FEET_PER_YARD = 3;
  private static final double FEET_PER_MILE = 5280;
  
  private double meters;
  private double feet;
  
  private Distance(double meters, double feet)
  {
    this.meters = meters;
    this.feet = feet;
  }
  
  /**
   * Constructs a new Distance from the specified feet.
   * @param feet A value in the imperial unit "feet".
   * @return A new Distance that has the specified feet.
   */
  public static Distance fromFeet(double feet)
  {
    double meters = feet / FEET_PER_METER;
    return new Distance(meters, feet);
  }
  
  /**
   * Constructs a new Distance from the specified yards.
   * @param yards A value in the imperial unit "yards".
   * @return A new Distance that has the specified yards.
   */
  public static Distance fromYards(double yards)
  {
    double feet = yards * FEET_PER_YARD;
    return fromFeet(feet);
  }
  
  /**
   * Constructs a new Distance from the specified miles.
   * @param miles A value in the imperial unit "miles".
   * @return A new Distance that has the specified miles.
   */
  public static Distance fromMiles(double miles)
  {
    double feet = miles * FEET_PER_MILE;
    return fromFeet(feet);
  }
  
  /**
   * Constructs a new Distance from the specified meters.
   * @param meters A value in the metric unit "miles".
   * @return A new Distance that has the specified meters.
   */
  public static Distance fromMeters(double meters)
  {
    double feet = meters * FEET_PER_METER;
    return new Distance(meters, feet);
  }
  
  /**
   * Constructs a new Distance from the specified kilometers.
   * @param kilometers A value in the metric unit "kilometers".
   * @return A new Distance that has the specified kilometers.
   */
  public static Distance fromKilometers(double kilometers)
  {
    double meters = kilometers * METERS_PER_KILOMETER;
    return fromMeters(meters);
  }
  
  /**
   * Constructs a new Distance from the specified nauticalMiles.
   * @param nauticalMiles A value in nautical miles.
   * @return A new Distance that has the specified nautical miles.
   */
  public static Distance fromNauticalMiles(double nauticalMiles)
  {
    double meters = nauticalMiles * METERS_PER_NAUTICAL_MILE;
    return fromMeters(meters);
  }
  
  ///////////////////////////////////
  // Imperial units
  
  /**
   * Gets the value of this distance, as expressed in feet.
   * @return The value in feet.
   */
  public double getFeet()
  {
    return feet;
  }
  
  /**
   * Gets the value of this distance, as expressed in yards.
   * @return The value in yards.
   */
  public double getYards()
  {
    return feet / FEET_PER_YARD;
  }
  
  /**
   * Gets the value of this distance, as expressed in miles.
   * @return The value in miles.
   */
  public double getMiles()
  {
    return feet / FEET_PER_MILE;
  }  
  ///////////////////////////////////
  
  ///////////////////////////////////
  // Metric units
  
  /**
   * Gets the value of this distance, as expressed in meters.
   * @return The value in meters.
   */
  public double getMeters()
  {
    return meters;
  }
  
  /**
   * Gets the value of this distance, as expressed in kilometers.
   * @return The value in kilometers.
   */
  public double getKilometers()
  {
    return meters / METERS_PER_KILOMETER;
  }  
  ///////////////////////////////////
  
  /**
   * Gets the value of this distance, as expressed in nautical miles.
   * @return The value in nautical miles.
   */
  public double getNauticalMiles()
  {
    return meters / METERS_PER_NAUTICAL_MILE;
  }
  
  /**
   * Creates a new distance which represents the sum of this distance and the parameter (this + that).
   * <p>
   * Neither this distance nor the parameter are modified.
   * @param distance The distance to add.
   * @return A new distance which represents (this + that).
   */
  public Distance add(Distance distance)
  {
    double result = meters + distance.meters;
    return fromMeters(result);
  }
  
  /**
   * Creates a new distance which represents the difference of this distance and the parameter (this - that).
   * <p>
   * Neither this distance nor the parameter are modified.
   * @param distance The distance to subtract.
   * @return A new distance which represents (this - that).
   */
  public Distance subtract(Distance distance)
  {
    double result = meters - distance.meters;
    return fromMeters(result);
  }
  
  /**
   * Creates a new distance which represents this distance times a scalar value (this * that).
   * <p>
   * This distance is not modified.  Note: No option is given to multiplyBy(Distance), because the logical
   * result of such a function would be an Area value, not a distance.
   * @param value The scalar value to multiply this distance by.
   * @return A new distance which represents (this * value).
   */
  public Distance multipliedBy(double value)
  {
    double result = meters * value;
    return fromMeters(result);
  }
  
  /**
   * Creates a new distance which represents this distance divided by a scalar value (this / that).
   * <p>
   * This distance is not modified.  
   * @param value The scalar value to divide this distance by.
   * @return A new distance which represents (this / value).
   */
  public Distance dividedBy(double value)
  {
    double result = meters / value;
    return fromMeters(result);
  }
  
  /**
   * Divides this distance by another distance (this / that).
   * <p>
   * This distance is not modified.  Note: Dividing a unit of measurement by the same unit of measurement, the
   * units cancel each other out, and the resulting value is scalar.
   * @param distance The scalar value to divide this distance by.
   * @return A scalar value which represents (this / value).
   */
  public double dividedBy(Distance distance)
  {
    double result = meters / distance.meters;
    return result;
  }
  
  /**
   * Checks to see if the value of this distance is exactly zero.
   * @return true if the value is zero, false otherwise.
   */
  public boolean isZero()
  {
    return (meters == 0);
  }

  @Override
  public String toString()
  {
    return meters + "m";
  }
}
