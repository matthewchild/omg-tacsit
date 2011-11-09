/**
 * @(#) Viewport.java
 */

package org.omg.tacsit.controller;

import java.awt.Point;
import java.util.List;
import org.omg.tacsit.geometry.GeodeticPosition;

/**
 * The Viewport class enables a TacSit client to interact with the basic
 * properties of a TacSit view. In particular the view's name, where it is
 * centered, how it is scaled, projected and oriented, and the selections it has
 * (through its SelectionManager instance).
 */
public interface Viewport
{
	/**
	 * Returns the name of the Viewport
	 * 
	 * @return 
	 */
	String getName( );
	
	/**
	 * Set the ViewEye properties of the Viewport to the given
	 * ViewEyeProperties.
	 * 
	 * If any of the attributes of the given ViewEyeProperties object are not
	 * set, the current value of the Viewport for that attribute will not be
	 * affected.
	 * 
	 * Later changes to the given ViewEyeProperties object do not have an effect
	 * on the Viewport.
	 * 
	 */
	void setViewEye( ViewEyeProperties viewEyeProps );
	
	/**
	 * Returns a copy of the current ViewEyeProperties of the Viewport.
	 * Modification to the returned ViewEyeProperties object will have no effect
	 * on the Viewport. To effect change, call the setViewEye method with the
	 * desired properties.
	 * 
	 * @return 
	 */
	ViewEyeProperties getViewEye( );
	
	/**
	 * Registers the given ViewportChangeListener to this Viewport.
	 * 
	 * After registration the ViewportChangeListener will be notified through
	 * its only method each time that a viewport is changed.
	 * 
	 * Registering a ViewportChangeListener that was already registered with
	 * this Viewport does not have any effect.
	 * 
	 */
	void addViewportChangeListener( ViewportChangeListener listener );
	
	/**
	 * Unregisters the givenViewportChangeListener from this Viewport.
	 * 
	 * Afterwards the ViewportChangeListener will not be notified of Viewport
	 * changes.
	 * 
	 * Unregistering a ViewportChangeListener that was not registered does not
	 * have any effect.
	 * 
	 */
	void removeViewportChangeListener( ViewportChangeListener listener );
	
	/**
	 * Set the name of the Viewport to the given name
	 * 
	 */
	void setName( String name );
	
	/**
	 * Converts the passed screen position into a geo position (pixels). Note
	 * that the conversion may not be valid.
	 * 
	 * These conversion methods are useful if you are trying to do anything
	 * special with OS GUI libraries (e.g. mouse controllers, popup windows, or
	 * other things of that sort).
	 * 
	 * @return 
	 */
	GeodeticPosition convertScreenPosition( Point screenPos );
	
	/**
	 * Converts the given GeoPosition into a ScreenPosition (pixels). Note that
	 * the conversion may not be valid.
	 * 
	 * These conversion methods are useful if you are trying to do anything
	 * special with OS GUI libraries (e.g. mouse controllers, popup windows, or
	 * other things of that sort).
	 * 
	 * @return 
	 */
	Point convertGeoPosition( GeodeticPosition geoPos );
	
	/**
	 * Offset and scale the viewport to contain all points (as possible) passed
	 * in the given GeoPositions array. The margin Distance parameter specifies
	 * an additional space that needs to be visible around the broadest points
	 * in the points list.
	 * 
	 */
	void scaleToPoints( List<GeodeticPosition> points, double margin );
	
	
}
