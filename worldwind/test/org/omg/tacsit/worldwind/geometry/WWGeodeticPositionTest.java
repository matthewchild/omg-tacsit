/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.omg.tacsit.worldwind.geometry;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.Globe;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.omg.tacsit.common.math.Distance;

/**
 *
 * @author MattChild
 */
public class WWGeodeticPositionTest
{
  
  /**
   * Creates a new instance.
   */
  public WWGeodeticPositionTest()
  {
  }
  
  private static final double ACCEPTABLE_MOVEMENT_AXIS_DELTA = Angle.MINUTE.getDegrees();
  private static final double ACCEPTABLE_NON_MOVEMENT_AXIS_DELTA = 0.00000001;
  
  private void testLongitudeMovement(WWGeodeticPosition basePosition, WWGeodeticPosition movedPosition, double expectedLongitude)
  {
    double newLongitudeDegrees = movedPosition.getLongitudeAngle().getDegrees();
    assertEquals(basePosition.getLatitude(), movedPosition.getLatitude(), ACCEPTABLE_NON_MOVEMENT_AXIS_DELTA);
    assertEquals(expectedLongitude, newLongitudeDegrees, ACCEPTABLE_MOVEMENT_AXIS_DELTA);
    assertEquals(basePosition.getAltitude(), movedPosition.getAltitude(), ACCEPTABLE_NON_MOVEMENT_AXIS_DELTA);
  }
  
  private void testMoveWest(WWGeodeticPosition basePosition, Globe globe, double expectedLongitude, double metersFromExpectedLongitude)
  {        
    Distance moveDistance = Distance.fromMeters(metersFromExpectedLongitude);
    WWGeodeticPosition westOfBasePosition = basePosition.west(moveDistance, globe);
    testLongitudeMovement(basePosition, westOfBasePosition, expectedLongitude);
  }
  
  private void testMoveEast(WWGeodeticPosition basePosition, Globe globe, double expectedLongitude, double metersFromExpectedLongitude)
  {        
    Distance moveDistance = Distance.fromMeters(metersFromExpectedLongitude);
    WWGeodeticPosition eastOfBasePosition = basePosition.east(moveDistance, globe);
    testLongitudeMovement(basePosition, eastOfBasePosition, expectedLongitude);
  }
  
  /**
   * Test of west method, of class WWGeodeticPosition.
   * The distance values for 1 degree at various latitudes comes from:
   * http://www.movable-type.co.uk/scripts/latlong.html
   * 
   */
  @Test
  public void testWest()
  {
    System.out.println("west");
    Globe globe = new Earth();
    
    // This is the calculation for miles for 1 degree west at the equatorial latitude from:
    
    WWGeodeticPosition zeroPosition = new WWGeodeticPosition(Angle.ZERO, Angle.ZERO, 0);
    testMoveWest(zeroPosition, globe, -1.0, 111200);
    
    WWGeodeticPosition halfwayNorthPosition = new WWGeodeticPosition(Angle.fromDegrees(45.0), Angle.ZERO, 0);
    testMoveWest(halfwayNorthPosition, globe, -1.0, 78630);    
    
    WWGeodeticPosition nearNorthPolePosition = new WWGeodeticPosition(Angle.fromDegrees(80.0), Angle.ZERO, 0);
    testMoveWest(nearNorthPolePosition, globe, -1.0, 19310);
    
    WWGeodeticPosition northPolePosition = new WWGeodeticPosition(Angle.fromDegrees(90.0), Angle.ZERO, 0);    
    // At the north pole, moving west should not move you at all.
    testMoveWest(northPolePosition, globe, 0.0, 19310);
    
    WWGeodeticPosition halfwaySouthPosition = new WWGeodeticPosition(Angle.fromDegrees(-45.0), Angle.ZERO, 0);
    testMoveWest(halfwaySouthPosition, globe, -1.0, 78630);
    
    WWGeodeticPosition nearSouthPolePosition = new WWGeodeticPosition(Angle.fromDegrees(-80.0), Angle.ZERO, 0);
    testMoveWest(nearSouthPolePosition, globe, -1.0, 19310);
    
    WWGeodeticPosition southPolePosition = new WWGeodeticPosition(Angle.fromDegrees(-90.0), Angle.ZERO, 0);    
    // At the south pole, moving west should not move you at all.
    testMoveWest(southPolePosition, globe, 0.0, 19310);
  }
  
  /**
   * Test of east method, of class WWGeodeticPosition.
   * The distance values for 1 degree at various latitudes comes from:
   * http://www.movable-type.co.uk/scripts/latlong.html
   * 
   */
  @Test
  public void testEast()
  {
    System.out.println("east");
    Globe globe = new Earth();
    
    // This is the calculation for miles for 1 degree west at the equatorial latitude from:
    
    WWGeodeticPosition zeroPosition = new WWGeodeticPosition(Angle.ZERO, Angle.ZERO, 0);
    testMoveEast(zeroPosition, globe, 1.0, 111200);
    
    WWGeodeticPosition halfwayNorthPosition = new WWGeodeticPosition(Angle.fromDegrees(45.0), Angle.ZERO, 0);
    testMoveEast(halfwayNorthPosition, globe, 1.0, 78630);    
    
    WWGeodeticPosition nearNorthPolePosition = new WWGeodeticPosition(Angle.fromDegrees(80.0), Angle.ZERO, 0);
    testMoveEast(nearNorthPolePosition, globe, 1.0, 19310);
    
    WWGeodeticPosition northPolePosition = new WWGeodeticPosition(Angle.fromDegrees(90.0), Angle.ZERO, 0);    
    // At the north pole, moving west should not move you at all.
    testMoveEast(northPolePosition, globe, 0.0, 19310);
    
    WWGeodeticPosition halfwaySouthPosition = new WWGeodeticPosition(Angle.fromDegrees(-45.0), Angle.ZERO, 0);
    testMoveEast(halfwaySouthPosition, globe, 1.0, 78630);
    
    WWGeodeticPosition nearSouthPolePosition = new WWGeodeticPosition(Angle.fromDegrees(-80.0), Angle.ZERO, 0);
    testMoveEast(nearSouthPolePosition, globe, 1.0, 19310);
    
    WWGeodeticPosition southPolePosition = new WWGeodeticPosition(Angle.fromDegrees(-90.0), Angle.ZERO, 0);    
    // At the south pole, moving west should not move you at all.
    testMoveEast(southPolePosition, globe, 0.0, 19310);
  }
  
  private void testLatitudeMovement(WWGeodeticPosition basePosition, WWGeodeticPosition movedPosition, double expectedLatitude)
  {
    double newLatitudeDegrees = movedPosition.getLatitudeAngle().getDegrees();
    assertEquals(basePosition.getLongitude(), movedPosition.getLongitude(), ACCEPTABLE_NON_MOVEMENT_AXIS_DELTA);
    assertEquals(expectedLatitude, newLatitudeDegrees, ACCEPTABLE_MOVEMENT_AXIS_DELTA);
    assertEquals(basePosition.getAltitude(), movedPosition.getAltitude(), ACCEPTABLE_NON_MOVEMENT_AXIS_DELTA);
  }
  
  private void testMoveSouth(WWGeodeticPosition basePosition, Globe globe, double expectedLatitude, double metersFromExpectedLatitude)
  {        
    Distance moveDistance = Distance.fromMeters(metersFromExpectedLatitude);
    WWGeodeticPosition southOfBasePosition = basePosition.south(moveDistance, globe);
    testLatitudeMovement(basePosition, southOfBasePosition, expectedLatitude);
  }
  
  private void testMoveNorth(WWGeodeticPosition basePosition, Globe globe, double expectedLatitude, double metersFromExpectedLatitude)
  {        
    Distance moveDistance = Distance.fromMeters(metersFromExpectedLatitude);
    WWGeodeticPosition northOfBasePosition = basePosition.north(moveDistance, globe);
    testLatitudeMovement(basePosition, northOfBasePosition, expectedLatitude);
  }
  
  /**
   * Test of north method, of class WWGeodeticPosition.
   * The distance values for 1 degree at various longitudes comes from:
   * http://www.movable-type.co.uk/scripts/latlong.html
   * 
   */
  @Test
  public void testNorth()
  {
    System.out.println("north");
    Globe globe = new Earth();
    
    // This is the calculation for miles for 1 degree west at the equatorial latitude from:
    
    WWGeodeticPosition zeroPosition = new WWGeodeticPosition(Angle.ZERO, Angle.ZERO, 0);
    testMoveNorth(zeroPosition, globe, 1.0, 111200);
    
    WWGeodeticPosition halfwayNorthPosition = new WWGeodeticPosition(Angle.fromDegrees(45.0), Angle.ZERO, 0);
    testMoveNorth(halfwayNorthPosition, globe, 46.0, 111200);    
    
    WWGeodeticPosition nearNorthPolePosition = new WWGeodeticPosition(Angle.fromDegrees(80.0), Angle.ZERO, 0);
    testMoveNorth(nearNorthPolePosition, globe, 81.0, 111200);
    
    WWGeodeticPosition northPolePosition = new WWGeodeticPosition(Angle.fromDegrees(90.0), Angle.ZERO, 0);    
    // At the north pole, moving north should not move you at all.
    testMoveNorth(northPolePosition, globe, 90.0, 111200);
    
    WWGeodeticPosition halfwaySouthPosition = new WWGeodeticPosition(Angle.fromDegrees(-45.0), Angle.ZERO, 0);
    testMoveNorth(halfwaySouthPosition, globe, -44.0, 111200);
    
    WWGeodeticPosition nearSouthPolePosition = new WWGeodeticPosition(Angle.fromDegrees(-80.0), Angle.ZERO, 0);
    testMoveNorth(nearSouthPolePosition, globe, -79.0, 111200);
    
    WWGeodeticPosition southPolePosition = new WWGeodeticPosition(Angle.fromDegrees(-90.0), Angle.ZERO, 0);    
    testMoveNorth(southPolePosition, globe, -89.0, 111200);
  }
  
  /**
   * Test of south method, of class WWGeodeticPosition.
   * The distance values for 1 degree at various longitudes comes from:
   * http://www.movable-type.co.uk/scripts/latlong.html
   * 
   */
  @Test
  public void testSouth()
  {
    System.out.println("south");
    Globe globe = new Earth();
    
    // This is the calculation for miles for 1 degree west at the equatorial latitude from:
    
    WWGeodeticPosition zeroPosition = new WWGeodeticPosition(Angle.ZERO, Angle.ZERO, 0);
    testMoveSouth(zeroPosition, globe, -1.0, 111200);
    
    WWGeodeticPosition halfwayNorthPosition = new WWGeodeticPosition(Angle.fromDegrees(45.0), Angle.ZERO, 0);
    testMoveSouth(halfwayNorthPosition, globe, 44.0, 111200);    
    
    WWGeodeticPosition nearNorthPolePosition = new WWGeodeticPosition(Angle.fromDegrees(80.0), Angle.ZERO, 0);
    testMoveSouth(nearNorthPolePosition, globe, 79.0, 111200);
    
    WWGeodeticPosition northPolePosition = new WWGeodeticPosition(Angle.fromDegrees(90.0), Angle.ZERO, 0);    
    // At the north pole, moving north should not move you at all.
    testMoveSouth(northPolePosition, globe, 89.0, 111200);
    
    WWGeodeticPosition halfwaySouthPosition = new WWGeodeticPosition(Angle.fromDegrees(-45.0), Angle.ZERO, 0);
    testMoveSouth(halfwaySouthPosition, globe, -46.0, 111200);
    
    WWGeodeticPosition nearSouthPolePosition = new WWGeodeticPosition(Angle.fromDegrees(-80.0), Angle.ZERO, 0);
    testMoveSouth(nearSouthPolePosition, globe, -81.0, 111200);
    
    WWGeodeticPosition southPolePosition = new WWGeodeticPosition(Angle.fromDegrees(-90.0), Angle.ZERO, 0);    
    testMoveSouth(southPolePosition, globe, -90.0, 111200);
  }
}
