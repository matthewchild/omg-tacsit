/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 30, 2011
 */
package org.omg.tacsit.ui.viewport;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.Icon;
import org.omg.tacsit.common.ui.ConfigurableAction;
import org.omg.tacsit.common.util.CollectionUtils;
import org.omg.tacsit.controller.Viewport;
import org.omg.tacsit.geometry.GeodeticPosition;

/**
 * An action which scales a Viewport to a set of Points.  The viewport will be scaled to be the smallest
 * possible, while containing all of the points, with a given margin of boundary as a border.
 * @author Matthew Child
 */
public class ScaleToPointsAction extends ConfigurableAction
{
  private Viewport viewportToScale;
  private List<? extends GeodeticPosition> scalePoints;
  private double margin;
  private int minimumScalePointCount;

  /**
   * Creates a new instance.
   * @param name The name of the action.
   * @param icon The icon to use for the action.
   */
  public ScaleToPointsAction(String name, Icon icon)
  {
    super(name, icon);
    minimumScalePointCount = 1;
  }

  /**
   * Gets the viewport that should be scaled.
   * @return The viewport to scale.
   */
  public Viewport getViewportToScale()
  {
    return viewportToScale;
  }

  /**
   * Sets the viewport that should be scaled.
   * @param viewportToScale The viewport that should be scaled.
   */
  public void setViewportToScale(Viewport viewportToScale)
  {
    this.viewportToScale = viewportToScale;
    checkEnabledState();
  }

  /**
   * Gets the list of points that should be scaled to.
   * @return The list of points.
   */
  public List<? extends GeodeticPosition> getScalePoints()
  {
    return scalePoints;
  }

  /**
   * Sets the list of points that should be scaled to.
   * @param scalePoints The list of points
   */
  public void setScalePoints(List<? extends GeodeticPosition> scalePoints)
  {
    this.scalePoints = CollectionUtils.copyToUnmodifiableList(scalePoints);
    checkEnabledState();
  }

  /**
   * Gets the margin border distance around the points that should be visible.
   * @return The distance (in meters)
   */
  public double getMargin()
  {
    return margin;
  }

  /**
   * Sets the margin border distance (in meters) around the points that should be visible.
   * @param margin The distance (in meters)
   */
  public void setMargin(double margin)
  {
    this.margin = margin;
  }
  
  /**
   * Gets the minimum number of scale points that are required for this action to be performed.
   * @return The minimum scale point count.
   */
  public int getMinimumScalePointCount()
  {
    return minimumScalePointCount;
  }

  /**
   * Sets the minimum number of scale points that are required for this action to be performed.
   * @param minimumScalePointCount The minimum scale point count.  May not be negative.
   */
  public void setMinimumScalePointCount(int minimumScalePointCount)
  {
    if (minimumScalePointCount < 0)
    {
      throw new IllegalArgumentException("minimumScalePointCount may not be null");
    }
    this.minimumScalePointCount = minimumScalePointCount;
  }

  private boolean hasMinimumScalePointCount()
  {
    if (scalePoints == null)
    {
      return false;
    }
    else
    {
      return minimumScalePointCount <= scalePoints.size();
    }
  }

  @Override
  public boolean isPerformable()
  {
    return (viewportToScale != null) && hasMinimumScalePointCount();
  }

  public void actionPerformed(ActionEvent e)
  {
    viewportToScale.scaleToPoints((List<GeodeticPosition>) scalePoints, margin);
  }  
}
