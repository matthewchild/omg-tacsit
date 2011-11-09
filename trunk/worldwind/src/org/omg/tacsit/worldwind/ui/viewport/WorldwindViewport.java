/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Mar 28, 2011
 */
package org.omg.tacsit.worldwind.ui.viewport;

import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.controller.SelectionEvent;
import org.omg.tacsit.worldwind.common.layers.ImagePack;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.RenderingEvent;
import gov.nasa.worldwind.event.RenderingListener;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.examples.ClickAndGoSelectListener;
import gov.nasa.worldwind.examples.util.ToolTipController;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.WorldMapLayer;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.tacsit.common.math.Distance;
import org.omg.tacsit.common.util.Disposable;
import org.omg.tacsit.controller.Projection;
import org.omg.tacsit.controller.SelectionListener;
import org.omg.tacsit.controller.SelectionManager;
import org.omg.tacsit.controller.SelectionType;
import org.omg.tacsit.controller.SingleSelectionManager;
import org.omg.tacsit.controller.ViewEyeProperties;
import org.omg.tacsit.geometry.GeodeticPosition;
import org.omg.tacsit.geometry.GeometryUtils;
import org.omg.tacsit.ui.viewport.AbstractViewport;
import org.omg.tacsit.ui.viewport.EntityViewport;
import org.omg.tacsit.worldwind.geometry.WWGeodeticPosition;

/**
 * An implementation of the Tacsit Viewport interface using Worldwind.
 * @author Matthew Child
 */
public class WorldwindViewport extends AbstractViewport implements EntityViewport, Disposable
{
  private static final int WORLD_MAP_LAYER_INDEX = 10;
  
  private ViewUpdateListener viewUpdateListener;
  private WorldWindowGLCanvas worldWindow;
  private EntityLayerList entityLayerList;
  private ToolTipController toolTipController;
  private SelectionManager selectionManager;
  private SelectionListener selectionListener;
  private SelectListener viewportSelectionListener;

  /**
   * Creates a new instance.
   */
  public WorldwindViewport()
  {
    initGUI();
    
    selectionManager = createDefaultSelectionManager();
    
    selectionListener = new SelectionHandler();
    selectionManager.addSelectionListener(selectionListener);    
  }
  
  private SelectionManager createDefaultSelectionManager()
  {
    return new SingleSelectionManager();
  }

  private void initGUI()
  {
    this.worldWindow = new WorldWindowGLCanvas();
    this.worldWindow.setPreferredSize(new Dimension(640, 480));

    // Create the default model as described in the current worldwind properties.
    entityLayerList = new EntityLayerList();
    Model windowModel = createModel(entityLayerList);
    this.worldWindow.setModel(windowModel);

    // Setup a select listener for the worldmap click-and-go feature
    this.worldWindow.addSelectListener(new ClickAndGoSelectListener(worldWindow, WorldMapLayer.class));

    // Add controllers to manage highlighting and tool tips.
    this.toolTipController = new ToolTipController(worldWindow, AVKey.DISPLAY_NAME, null);
    
    viewportSelectionListener  = new ViewportSelectionListener();
    worldWindow.addSelectListener(viewportSelectionListener);
    
    viewUpdateListener = new ViewUpdateListener();
    View view = worldWindow.getView();
    view.addPropertyChangeListener(viewUpdateListener);
    worldWindow.addPropertyChangeListener(viewUpdateListener);
    worldWindow.addRenderingListener(viewUpdateListener);    
  }

  private Model createModel(EntityLayerList entityLayers)
  {
    Model windowModel = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
    LayerList layerList = windowModel.getLayers();
    
    int layerStartIndex = WORLD_MAP_LAYER_INDEX;
    Iterator<Layer> layers = entityLayers.getLayers();
    while (layers.hasNext())
    {
      Layer layer = layers.next();
      layerList.add(layerStartIndex, layer);
      layerStartIndex++;
    }
    return windowModel;
  }
  
  public Component getComponent()
  {
    return worldWindow;
  }

  /**
   * Gets the Worldwind View for this Viewport.
   * @return The View of this Viewport.
   */
  protected View getView()
  {
    return worldWindow.getView();
  }

  /**
   * Sets the image pack used to pick icons for tracks.
   * @param imagePack The image pack to use for picking icons for tracks.
   */
  public void setTrackImagePack(ImagePack imagePack)
  {
    entityLayerList.setTrackImagePack(imagePack);
  }

  /**
   * Gets the image pack used to pick icons for track.
   * @return The image pack to use for picking icons for tracks.
   */
  public ImagePack getTrackImagePack()
  {
    return entityLayerList.getTrackImagePack();
  }
  
  /**
   * WorldWind's View interface has a copyViewState function.  However, this does not work in many cases.
   * The functions it uses to calculate the new view state sometimes return a pitch that is negative, but very
   * close to zero (Example: -3.13445 x 10^-15).  The copyViewState function will fail if the pitch is less than 0.
   * The implemented function works more often, but not always.
   * <p>
   * If this gets fixed, this function can be replaced by simply doing newView.copyViewState(oldView);
   * 
   * Potential better way to do this:
   * Configuration.setValue(AVKey.INITIAL_LATITUDE, lat;
   * Configuration.setValue(AVKey.INITIAL_LONGITUDE, lon);
   * <p>
   * This sets the initial values that are loaded when a Viewport is displayed.
   * <p>
   * wwd = new WorldWindowGLCanvas();
   * @param oldView The view that contains the data to be copied
   * @param newView The view where the copied data will be placed.
   */
  protected void copyViewState(View oldView, View newView)
  {
    Vec4 oldCenterPoint = oldView.getCenterPoint();
    Vec4 oldEyePoint = oldView.getCurrentEyePoint();   
    Angle oldHeading = oldView.getHeading();
    Angle oldPitch = oldView.getPitch();
        
    Globe newGlobe = newView.getGlobe();
    
    Position newCenterPosition = newGlobe.computePositionFromPoint(oldCenterPoint);
    Position newEyePosition = newGlobe.computePositionFromPoint(oldEyePoint);
    try
    {
      newView.setOrientation(newEyePosition, newCenterPosition);
    }
    catch(IllegalArgumentException ex)
    {
      Logger.getLogger(WorldwindViewport.class.getName()).log(Level.INFO, "Unable to copy view state; ignoring old orientation.");
    }
    
    newView.setHeading(oldHeading);
    newView.setPitch(oldPitch);
  }
  
  /**
   * Sets the View that's used for this Viewport.
   * @param newView The new View to use in this Viewport.
   */
  protected void setView(View newView)
  {
    if(newView == null)
    {
      throw new IllegalArgumentException("newView may not be null.");
    }
    
    View oldView = worldWindow.getView();
    
    
    oldView.removePropertyChangeListener(viewUpdateListener);
    worldWindow.setView(newView);
    newView.addPropertyChangeListener(viewUpdateListener);
    
    // We must trigger a redraw immediately, because the View's globe will not exist until apply(DrawContext)
    //  is invoked.  If the globe does not exist, that causes certain View functions that utilize the globe
    //  to throw a NullPointerException.
    this.worldWindow.redrawNow();
    
    copyViewState(oldView, newView);
  }
  
  private View updateViewFromProjection(Projection projection)
  {
    if(!(projection instanceof WorldwindProjection))
    {
      throw new IllegalArgumentException("projection must be a WorldwindProjection");
    }
    Class<? extends View> viewClass = ((WorldwindProjection)projection).getViewClass();
    View currentView = worldWindow.getView();
    Class currentViewClass = currentView.getClass();
    if(!currentViewClass.equals(viewClass))
    {
      try
      {
        View newView = viewClass.newInstance();
        setView(newView);
        currentView = newView;
      }
      catch(Exception ex)
      {
        Logger.getLogger(WorldwindViewport.class.getName()).log(Level.SEVERE, "Couldn't instantiate view.", ex);
      }
    }
    return currentView;
  }
  
  /**
   * Sets the projection used by this viewport.  
   * @param projection The projection to change to.  Must be a WorldwindProjection.
   */
  public void setProjection(Projection projection)
  {
    updateViewFromProjection(projection);
  }

  /**
   * Gets the projection used by this viewport.
   * @return The projection used by this viewport.
   */
  public Projection getProjection()
  {
    View view = getView();
    
    Class<? extends View> viewClass = view.getClass();
    WorldwindProjection projection = WorldwindProjection.getProjection(viewClass);
    
    return projection;
  }
  
  @Override
  public void setViewEye(ViewEyeProperties viewEyeProps)
  {
    Projection projection = viewEyeProps.getProjection();
        
    View newView = updateViewFromProjection(projection);
            
    double orientation = viewEyeProps.getOrientation();
    Angle orientationAngle = Angle.fromRadians(orientation);
    newView.setHeading(orientationAngle);
    
    GeodeticPosition centerPosition = viewEyeProps.getGeoCenter();
    Position worldwindCenterPosition = WWGeodeticPosition.toWWPosition(centerPosition);
    
    double rangeScale = viewEyeProps.getRangeScale();
    
    newView.goTo(worldwindCenterPosition, rangeScale);
       
    if(viewEyeProps instanceof WorldwindViewEyeProperties)
    {
      Angle pitch = ((WorldwindViewEyeProperties)viewEyeProps).getPitch();
      if(pitch != null)
      {
        newView.setPitch(pitch);
      }
    }
    this.worldWindow.redraw();
  }
  
  /**
   * Checks to see whether or not the view and globe has been fully initialized.
   * @return true if it's intialized, false otherwise.
   */
  protected boolean isInitialized()
  {
    boolean initialized = false;
    View view = getView();
    if(view != null)
    {
      initialized = view.getGlobe() != null;
    }
    return initialized;
  }

  /**
   * Gets the center point of the Viewport.
   * @return The geodetic center point of the Viewport.
   */
  public GeodeticPosition getGeoCenter()
  {
    GeodeticPosition geoCenter = null;
    
    if(isInitialized())
    {
      View view = getView();

      Globe globe = view.getGlobe();
      Vec4 centerPoint = view.getCenterPoint();
      Position centerPosition = globe.computePositionFromPoint(centerPoint);
      geoCenter = new WWGeodeticPosition(centerPosition);
    }
    
    return geoCenter;
  }
  
  /**
   * Calculates the range from the camera to the center of the view.
   * @return range to the eye point, in meters.
   */
  public double getRangeToEye()
  {
    double rangeInMeters = 0;
    if(isInitialized())
    {
      View view = getView();

      Vec4 centerPoint = view.getCenterPoint();
      Vec4 eyePoint = view.getEyePoint();
      rangeInMeters = eyePoint.distanceTo3(centerPoint);
    }
    
    return rangeInMeters;
  }
  
  /**
   * Gets the pitch of the Viewport.
   * @return The pitch of the Viewport.
   */
  public Angle getPitch()
  {
    Angle pitch = null;
    if(isInitialized())
    {
      View view = getView();
      pitch = view.getPitch();
    }
    return pitch;
  }
  
  /**
   * Gets the orientation of the Viewport.
   * @return The orientation of the viewport.
   */
  public Angle getOrientation()
  {
    Angle heading = null;
    if(isInitialized())
    {
      View view = getView();    
      heading = view.getHeading();
    }
    return heading;
  }
  
  @Override
  public ViewEyeProperties getViewEye()
  {    
    WorldwindViewEyeProperties viewEye = new WorldwindViewEyeProperties();
        
    Projection currentProjection = getProjection();
    viewEye.setProjection(currentProjection);
    
    GeodeticPosition geoCenter = getGeoCenter();
    viewEye.setGeoCenter(geoCenter);
        
    double rangeMeters = getRangeToEye();
    viewEye.setRangeScale(rangeMeters);
    
    Angle orientation = getOrientation();
    viewEye.setOrientation(orientation);
    
    Angle pitch = getPitch();
    viewEye.setPitch(pitch);
    
    return viewEye;
  }
  
  /**
   * Fires to associated listeners that the view eye has changed.
   */
  protected void fireViewEyeChanged()
  {
    ViewEyeProperties viewEye = getViewEye();
    super.fireViewEyeChanged(viewEye);
  }

  @Override
  public GeodeticPosition convertScreenPosition(Point screenPos)
  {
    View view = getView();    
    Position position = view.computePositionFromScreenPoint(screenPos.getX(), screenPos.getY());
    GeodeticPosition geodeticPosition = new WWGeodeticPosition(position);
    return geodeticPosition;
  }
  
  /**
   * Gets the screen point for a position defined by a Worldwind native location.  If the position is not on the
   * screen, the results are undefined.
   * @param position The position to get the point of.
   * @return The point on the screen that position is located at.
   */
  public Point getScreenPoint(Position position)
  {
    View view = getView();
    Globe viewGlobe = view.getGlobe();
    Vec4 globeVector = viewGlobe.computePointFromPosition(position);
    
    Vec4 screenVector = view.project(globeVector);
    
    Point screenPoint = new Point((int)screenVector.x, (int)screenVector.y);
    return screenPoint;
  }

  @Override
  public Point convertGeoPosition(GeodeticPosition geoPos)
  {
    Position worldwindPosition = WWGeodeticPosition.toWWPosition(geoPos);
    Point screenPoint = getScreenPoint(worldwindPosition);
    return screenPoint;
  }
  
  private WWGeodeticPosition padMinimumPosition(GeodeticPosition minimumPosition, Globe globe, Distance margin)
  {    
    // Convert the minimum position into a worldwind position.
    WWGeodeticPosition wwMinimumPosition = WWGeodeticPosition.toWWGeodeticPosition(minimumPosition);
    
    // Move the minimum position west by the margin amount.   This must be done before moving south, because
    // the angular measurement of the distance west will be larger (and incorrect) than if you move west before.
    // The distance of 1 degree longitude at the equator is 111.2 KM
    // The distance of 1 degree longitude at 45 degrees latitude is 78.63 KM
    WWGeodeticPosition minimumWithWestMargin = wwMinimumPosition.west(margin, globe);
    
    // Move the minimum position south by the margin amount.
    WWGeodeticPosition minimumWithSouthAndWestMargin = minimumWithWestMargin.south(margin, globe);        
    return minimumWithSouthAndWestMargin;
  }
  
  private WWGeodeticPosition padMaximumPosition(GeodeticPosition maximumPosition, Globe globe, Distance margin)
  {
    // Convert the maximum position into a worldwind position.
    WWGeodeticPosition wwMaximumPosition = WWGeodeticPosition.toWWGeodeticPosition(maximumPosition) ;
        
    // Move the maximum position east by the margin amount.  This must be done before moving north, for the same
    // reason mentioned above (west before south).
    WWGeodeticPosition maximumWithEastMargin = wwMaximumPosition.east(margin, globe);
    
    // Move the maximum position south by the margin amount.
    WWGeodeticPosition maximumWithNorthAndEastMargin = maximumWithEastMargin.north(margin, globe);
    return maximumWithNorthAndEastMargin;
  }
  
  private Distance getLongitudeDifference(Position min, Position max, double equatorialRadius, double polarRadius)
  {
    LatLon pointEastOfMin = new LatLon(min.getLatitude(), max.getLongitude());
    double distanceMeters = LatLon.ellipsoidalDistance(min, pointEastOfMin, equatorialRadius, polarRadius);
    if(!Double.isNaN(distanceMeters))
    {
      return Distance.fromMeters(distanceMeters);
    }
    else
    {
      return Distance.ZERO;
    }
  }
  
  private Distance getLatitudeDifference(Position min, Position max, double equatorialRadius, double polarRadius)
  {
    LatLon pointAboveMin = new LatLon(max.getLatitude(), min.getLongitude());
    double distanceMeters = LatLon.ellipsoidalDistance(min, pointAboveMin, equatorialRadius, polarRadius);
    if(!Double.isNaN(distanceMeters))
    {
      return Distance.fromMeters(distanceMeters);
    }
    else
    {
      return Distance.ZERO;
    }
  }
  
  private double getViewportAspectRatio()
  {
    View view = worldWindow.getView();
    if(view == null)
    {
      return 0;
    }
    
    Rectangle viewport = view.getViewport();
    if(viewport.width == 0)
    {
      return 0;
    }
    
    double viewportAspectRatio = (double)viewport.height / (double)viewport.width;
    return viewportAspectRatio;
  }
  
  private Angle getVerticalFieldOfView()
  {
    View view = worldWindow.getView();
    if(view == null)
    {
      return Angle.ZERO;
    }
    Angle horizontalFieldOfView = getHorizontalFieldOfView();
    
    double viewportAspectRatio = getViewportAspectRatio();
    double verticalFieldOfViewRadians = horizontalFieldOfView.getRadians() * viewportAspectRatio;
    Angle verticalFieldOfView = Angle.fromRadians(verticalFieldOfViewRadians);
    return verticalFieldOfView;
  }
  
  private Angle getHorizontalFieldOfView()
  {
    View view = worldWindow.getView();
    if(view == null)
    {
      return Angle.ZERO;
    }
    Angle horizontalFieldOfView = view.getFieldOfView();
    return horizontalFieldOfView;
  }
  
  private Distance getMinimumElevation(Distance horizontalDistance, Distance verticalDistance)
  {
    double angularDifferenceRatio = verticalDistance.getMeters() / horizontalDistance.getMeters();
    
    Distance elevation;
    // If the aspect ratio of distances is greater than the viewport ratio; there's more "rise" than "run".
    double viewportAspectRatio = getViewportAspectRatio();
    if(angularDifferenceRatio > viewportAspectRatio)
    {
      // The height will be on the edge of the screen.  Use the latitude coordinate to calculate it based on
      // the view eye.
      Distance halfHeight = verticalDistance.dividedBy(2);
      
      // Calculate the eye vertical view angle      
      Angle verticalFieldOfView = getVerticalFieldOfView();
      elevation = halfHeight.dividedBy(verticalFieldOfView.tanHalfAngle());
    }
    // Otherwise, the aspect ratio of distances is less than or equal to the viewport ratio; there's more "run"
    // than "rise".
    else
    {
      // The width will be on the edge of the screen.  Use the longitude coordinate to calculate it based on
      // the view eye.      
      Distance halfWidth = horizontalDistance.dividedBy(2);
      
      // Calculate the eye horizontal view angle.
      Angle horizontalFieldOfView = getHorizontalFieldOfView();
      elevation =  halfWidth.dividedBy(horizontalFieldOfView.tanHalfAngle());
    }
    return elevation;
  }
  
  private void setViewportBoundaries(WWGeodeticPosition minimumPoint, WWGeodeticPosition maximumPoint)
  {
    Position minimumPosition = minimumPoint.getPosition();
    Position maximumPosition = maximumPoint.getPosition();
    Position newViewCenter = Position.interpolate(0.5, minimumPosition, maximumPosition);
    View view = worldWindow.getView();        
        
    // Calculate the distance of the horizontal and vertical rectangle formed by the two points.
    Globe globe = view.getGlobe();
    double equatorialRadius = globe.getEquatorialRadius();
    double polarRadius = globe.getPolarRadius();
    Distance longitudeDistance = getLongitudeDifference(minimumPosition, maximumPosition, equatorialRadius, polarRadius);
    Distance latitudeDistance = getLatitudeDifference(minimumPosition, maximumPosition, equatorialRadius, polarRadius);    
    
    // Get the minimum elevation the view eye must be away to fully view the longitude and latitude distance
    Distance newElevation = getMinimumElevation(longitudeDistance, latitudeDistance);
    
    // Move the view to the new position.
    view.setHeading(Angle.ZERO);
    view.goTo(newViewCenter, newElevation.getMeters());    
  }
  
  
  private void scaleToPoints(GeodeticPosition minimumPoint, GeodeticPosition maximumPoint, double marginMeters)
  {
    // Check to make sure the necessary resources have been initialized to perform a scaling operation.
    View view = worldWindow.getView();
    if(view == null)
    {
      return;
    }
    
    Globe globe = view.getGlobe();
    if(globe == null)
    {
      return;
    }
    
    // Calculate the margin values we'll need to compute the offsets
    double absoluteMargin = Math.abs(marginMeters);
    Distance marginDistance = Distance.fromMeters(absoluteMargin);
    
    // Pad the minimum position by the margin amount.
    WWGeodeticPosition paddedPosition1 = padMinimumPosition(minimumPoint, globe, marginDistance);
        
    // Pad the maximum position by the margin amount.
    WWGeodeticPosition paddedPosition2 = padMaximumPosition(maximumPoint, globe, marginDistance);    
        
    // Set the viewport boundaries so that the new offset minimum and maximum positions are on the edge of the screen.
    setViewportBoundaries(paddedPosition1, paddedPosition2);
  }
  
  /**
	 * Offset and scale the viewport to contain all points (as possible) passed
	 * in the given GeoPositions array. The margin Distance parameter specifies
	 * an additional space that needs to be visible around the broadest points
	 * in the points list.
	 * <p>
   * This implementation does not function correctly when the smallest viewport crosses the dateline.  There are
   * 3 points of failure:
   * <ol>
   * <li>The correct points are not chosen.  This chooses the viewport border points based on their maximum and minimum
   * values.  This algorithm is not correct if the smallest viewport crosses the dateline.  If this is the case (and
   * the total viewing angle is under 180 degrees), the correct points are the longitude values closest to zero.</li>
   * <li>The algorithm needs to apply padding in the opposite longitude direction in the event of a dateline cross.
   * Normally, the algorithm moves the point minimum point west and south, and the maximum point east and north.  When
   * the minimum scaled view crosses the dateline, the minimum point should be moved east and south, and the maximum
   * point should move west and north.</li>
   * <li>The zoom out distance is incorrect.  The underlying function that serves as a basis for the zoom out
   * distance is LatLon.ellipsoidalDistance().  This function appears to always calculate the distance from normalized
   * maximum to normalized minimum.  This throws off the correct value, since the shortest distance between 
   * two points at -179 and 179 is obviously 2 degrees apart, not 358 degrees</li>
   * </ol>
   * @param points The list of points to scale the viewport to.
   * @param margin The distance (in meters) to
   */
  public void scaleToPoints(List<GeodeticPosition> points, double margin)
  {
    GeodeticPosition minimumPosition = GeometryUtils.getMinimumPosition(points);
    GeodeticPosition maximumPosition = GeometryUtils.getMaximumPosition(points);
    if((minimumPosition != null) && (maximumPosition != null))
    {
      scaleToPoints(minimumPosition, maximumPosition, margin);
    }
  }

  public void addEntity(Entity entity)
  {
    entityLayerList.addEntity(entity);
  }

  public void removeEntity(Entity entity)
  {
    entityLayerList.removeEntity(entity);
  }

  public void clearEntities()
  {
    entityLayerList.clearEntities();
  }

  public void updateEntity(Entity entity)
  {
    entityLayerList.updateEntity(entity);
  }

  public boolean isEntityAllowed(Entity entity)
  {
    return true;
  }
  
  public void setSelectionManager(SelectionManager selectionManager)
  {
    // Create a selection manager if null is used.
    SelectionManager newSelectionManager;
    if(selectionManager == null)
    {
      newSelectionManager = createDefaultSelectionManager();
    }
    else
    {
      newSelectionManager = selectionManager;
    }
    
    this.selectionManager.removeSelectionListener(selectionListener);
    clearAllViewportSelections();
    SelectionManager oldSelectionManager = this.selectionManager;
    this.selectionManager = newSelectionManager;
    loadSelectionFromManager();
    this.selectionManager.addSelectionListener(selectionListener);
    propertyChanged("selectionManager", oldSelectionManager, this.selectionManager);
  }

  public SelectionManager getSelectionManager()
  {
    return this.selectionManager;
  }
  
  private void loadSelectionFromManager()
  {
    for(SelectionType type : SelectionType.values())
    {
      List<Entity> selectedEntities = selectionManager.getSelection(type);
      updateViewportSelection(selectedEntities, type);
    }
  }
  
  private void clearAllViewportSelections()
  {
    for (SelectionType selectionType : SelectionType.values())
    {
      clearViewportSelection(selectionType);
    }
  }
  
  private void clearViewportSelection(SelectionType type)
  {    
    entityLayerList.clearSelection(type);
  }
  
  private void updateViewportSelection(List<Entity> selectedEntities, SelectionType type)
  {
    worldWindow.removeSelectListener(viewportSelectionListener);
    clearViewportSelection(type);
    if(selectedEntities != null)
    {
      entityLayerList.addToSelection(selectedEntities, type);
    }
    worldWindow.addSelectListener(viewportSelectionListener);
  }
  
  private class SelectionHandler implements SelectionListener
  {
    public void selectionChanged(SelectionEvent event)
    {
      updateViewportSelection(event.getEntities(), event.getSelectionType());
    }
  }
  
  public void dispose()
  {
    worldWindow.shutdown();
  }
      
  private class ViewUpdateListener implements PropertyChangeListener, RenderingListener
  {
    boolean firstRendering;
    
    public ViewUpdateListener()
    {
      firstRendering = true;
    }
    
    /**
     * Only general events are fired when the view is changed.  Specific events like "Zoom level changed" and
     * "Center point changed" do not exist.
     * @param evt 
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
      String propertyName = evt.getPropertyName();
      if(propertyName.equals(AVKey.VIEW))
      {
        fireViewEyeChanged();
      }
    }

    /**
     * The current location of the view is not available when the object is constructed.  A <code>view</code>
     * object's Globe does not appear to be initialized until it is rendered on screen.  This means that objects
     * that query the state before it is rendered will receive a null value, and no PropertyChangeEvent will
     * be fired because the view hasn't changed.  After the view has been initialized, we don't need to worry
     * about rendering state any more, so the firstRendering flag turns this listener off.
     * @param event 
     */
    public void stageChanged(RenderingEvent event)
    {
      String stage = event.getStage();
      if(firstRendering && RenderingEvent.BEFORE_BUFFER_SWAP.equals(stage))
      {
        firstRendering = false;
        fireViewEyeChanged();
      }
    }
  }
  
  private class ViewportSelectionListener implements SelectListener
  {
    public ViewportSelectionListener()
    {
    }
    
    private SelectionType getSelectionTypeForEvent(SelectEvent event)
    {
      SelectionType selectionType = null;
      if(SelectEvent.LEFT_CLICK.equals(event.getEventAction()))
      {
        MouseEvent mouseEvent = event.getMouseEvent();
        if(mouseEvent.isControlDown())
        {
          selectionType = SelectionType.SECONDARY;
        }
        else if(mouseEvent.isAltDown())
        {
          selectionType = SelectionType.OTHER;          
        }
        else
        {
          selectionType = SelectionType.PRIMARY;
        }
      }
      
      return selectionType;
    }

    public void selected(SelectEvent event)
    {
      SelectionType selectionType = getSelectionTypeForEvent(event);
      if(selectionType != null)
      {
        Object topObject = event.getTopObject();
        Entity selectedEntity = entityLayerList.getEntityForObject(topObject);
        if(selectedEntity != null)
        {
          List<Entity> selectionAsList = Collections.singletonList(selectedEntity);
          selectionManager.setSelection(selectionAsList, selectionType);
        }
      }
    }
  }
}
