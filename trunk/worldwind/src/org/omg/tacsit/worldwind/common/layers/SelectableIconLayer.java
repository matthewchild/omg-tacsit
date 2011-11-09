/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 16, 2011
 */
package org.omg.tacsit.worldwind.common.layers;

import com.sun.opengl.util.texture.TextureCoords;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.IconLayer;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.IconRenderer;
import gov.nasa.worldwind.render.WWIcon;
import gov.nasa.worldwind.util.Logging;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import javax.media.opengl.GL;

/**
 * A layer that renders icons with selection state shown as boxes around the icons.
 * @param <SEL_TYPE> The type of selections this layer can render.
 * @author Matthew Child
 */
public class SelectableIconLayer<SEL_TYPE> extends IconLayer
{

  private SelectableIconRenderer selectedIconRenderer;

  /**
   * Creates a new instance.
   */
  public SelectableIconLayer()
  {
    this.selectedIconRenderer = new SelectableIconRenderer();
    this.iconRenderer = selectedIconRenderer;
  }

  /**
   * Sets the selection model that has the selection state for icons.
   * @param selectionModel The selection model to use.
   */
  public void setSelectionModel(IconSelectionModel selectionModel)
  {
    selectedIconRenderer.setSelectionModel(selectionModel);
  }

  /**
   * Gets the selection model that has the selection state for icons.
   * @return The selection model that contains the selection state.
   */
  public IconSelectionModel getSelectionModel()
  {
    return selectedIconRenderer.getSelectionModel();
  }

  /**
   * Sets the rendering color of the box for a particular selection type.
   * @param selectionType The type of selection to change the color of.
   * @param selectionColor The color for that selection type.
   */
  public void setSelectionColor(SEL_TYPE selectionType, Color selectionColor)
  {
    selectedIconRenderer.setSelectionColor(selectionType, selectionColor);
  }

  /**
   * Gets the selection color for a particular selection type.
   * @param selectionType The type of selection to get the color of.
   * @return The color for that selection type.
   */
  public Color getSelectionColor(SEL_TYPE selectionType)
  {
    return selectedIconRenderer.getSelectionColor(selectionType);
  }

  private class SelectableIconRenderer extends IconRenderer
  {

    private IconSelectionModel selectionModel;
    private Map<Object, Color> selectionTypeToColor;
    private float selectionLineWidth;

    public SelectableIconRenderer()
    {
      selectionTypeToColor = new HashMap();
      selectionLineWidth = 3.0f;
    }

    public IconSelectionModel getSelectionModel()
    {
      return selectionModel;
    }

    public void setSelectionModel(IconSelectionModel selectionModel)
    {
      this.selectionModel = selectionModel;
    }
    
    public void setSelectionColor(Object selectionType, Color selectionColor)
    {
      selectionTypeToColor.put(selectionType, selectionColor);
    }
    
    public Color getSelectionColor(Object selectionType)
    {
      return selectionTypeToColor.get(selectionType);
    }

    protected Color safelyGetSelectionColor(Object selectionType)
    {
      Color selectionColor = getSelectionColor(selectionType);
      if (selectionColor == null)
      {
        selectionColor = Color.CYAN;
      }
      return selectionColor;
    }
    // This is only used in the computation of drawing the icon selection.
    private float[] selectionColorTempArray = new float[3];

    protected void drawIconSelection(GL gl, WWIcon icon, Object selectionType, Vec4 screenPoint, double width,
                                     double height)
    {
      Color selectionColor = safelyGetSelectionColor(selectionType);
      gl.glPushAttrib(GL.GL_ENABLE_BIT | GL.GL_CURRENT_BIT | GL.GL_POLYGON_BIT);
      gl.glDisable(GL.GL_TEXTURE_2D);
      gl.glPushMatrix();
      gl.glLoadIdentity();
      gl.glLineWidth(selectionLineWidth);
      selectionColorTempArray = selectionColor.getColorComponents(selectionColorTempArray);
      gl.glColor3f(selectionColorTempArray[0], selectionColorTempArray[1], selectionColorTempArray[2]);
      gl.glBegin(GL.GL_LINE_LOOP);
      double halfSelectionLineWidth = selectionLineWidth / 2;
      double startX = screenPoint.x - (width / 2) - halfSelectionLineWidth;
      double startY = screenPoint.y - halfSelectionLineWidth;
      gl.glVertex2d(startX, startY);
      double endY = startY + height + selectionLineWidth;
      gl.glVertex2d(startX, endY);
      double endX = startX + width + selectionLineWidth;
      gl.glVertex2d(endX, endY);
      gl.glVertex2d(endX, startY);
      gl.glEnd();
      gl.glPopMatrix();
      gl.glPopAttrib();
    }
    
    protected Object getSelectionType(WWIcon icon)
    {
      Object selectionType = null;
      if(this.selectionModel != null)
      {
        selectionType = this.selectionModel.getSelectionType(icon);
      }
      return selectionType;
    }

    @Override
    protected Vec4 drawIcon(DrawContext dc, OrderedIcon uIcon)
    {
      Vec4 iconPoint = uIcon.getPoint();
      WWIcon icon = uIcon.getIcon();
      if (iconPoint == null)
      {
        String msg = Logging.getMessage("nullValue.PointIsNull");
        Logging.logger().severe(msg);

        // Record feedback data for this WWIcon if feedback is enabled.
        if (iconPoint != null)
        {
          this.recordFeedback(dc, icon, null, null);
        }

        return null;
      }

      if (dc.getView().getFrustumInModelCoordinates().getNear().distanceTo(iconPoint) < 0)
      {
        // Record feedback data for this WWIcon if feedback is enabled.
        this.recordFeedback(dc, icon, iconPoint, null);

        return null;
      }

      final Vec4 screenPoint = dc.getView().project(iconPoint);
      if (screenPoint == null)
      {
        // Record feedback data for this WWIcon if feedback is enabled.
        this.recordFeedback(dc, icon, iconPoint, null);

        return null;
      }

      double pedestalScale;
      double pedestalSpacing;
      if (this.pedestal != null)
      {
        pedestalScale = this.pedestal.getScale();
        pedestalSpacing = pedestal.getSpacingPixels();
      }
      else
      {
        pedestalScale = 0d;
        pedestalSpacing = 0d;
      }

      javax.media.opengl.GL gl = dc.getGL();

      this.setDepthFunc(dc, uIcon, screenPoint);

      gl.glMatrixMode(GL.GL_MODELVIEW);
      gl.glLoadIdentity();

      Dimension size = icon.getSize();
      double width = size != null ? size.getWidth() : icon.getImageTexture().getWidth(dc);
      double height = size != null ? size.getHeight() : icon.getImageTexture().getHeight(dc);
      gl.glTranslated(screenPoint.x - width / 2, screenPoint.y + (pedestalScale * height) + pedestalSpacing, 0d);

      if (icon.isHighlighted())
      {
          double heightDelta = this.pedestal != null ? 0 : height / 2; // expand only above the pedestal
          gl.glTranslated(width / 2, heightDelta, 0);
          gl.glScaled(icon.getHighlightScale(), icon.getHighlightScale(), icon.getHighlightScale());
          gl.glTranslated(-width / 2, -heightDelta, 0);
      }

      Rectangle rect = new Rectangle((int) (screenPoint.x - width / 2), (int) (screenPoint.y), (int) width,
                                     (int) (height + (pedestalScale * height) + pedestalSpacing));

      if (dc.isPickingMode())
      {
        //If in picking mode and pick clipping is enabled, check to see if the icon is within the pick volume.
        if (this.isPickFrustumClippingEnabled() && !dc.getPickFrustums().intersectsAny(rect))
        {
          // Record feedback data for this WWIcon if feedback is enabled.
          this.recordFeedback(dc, icon, iconPoint, rect);

          return screenPoint;
        }
        else
        {
          java.awt.Color color = dc.getUniquePickColor();
          int colorCode = color.getRGB();
          this.pickSupport.addPickableObject(colorCode, icon, uIcon.getPosition(), false);
          gl.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
        }
      }


      Object selectionType = getSelectionType(icon);
      if (selectionType != null)
      {
        drawIconSelection(gl, icon, selectionType, screenPoint, width, height);
      }

      if (icon.getBackgroundTexture() != null)
      {
        this.applyBackground(dc, icon, screenPoint, width, height, pedestalSpacing, pedestalScale);
      }

      if (icon.getImageTexture().bind(dc))
      {
        TextureCoords texCoords = icon.getImageTexture().getTexCoords();
        gl.glScaled(width, height, 1d);
        dc.drawUnitQuad(texCoords);
      }

      if (this.pedestal != null && this.pedestal.getImageTexture() != null)
      {
        gl.glLoadIdentity();
        gl.glTranslated(screenPoint.x - (pedestalScale * (width / 2)), screenPoint.y, 0d);
        gl.glScaled(width * pedestalScale, height * pedestalScale, 1d);

        if (this.pedestal.getImageTexture().bind(dc))
        {
          TextureCoords texCoords = this.pedestal.getImageTexture().getTexCoords();
          dc.drawUnitQuad(texCoords);
        }
      }

      // Record feedback data for this WWIcon if feedback is enabled.
      this.recordFeedback(dc, icon, iconPoint, rect);

      return screenPoint;
    }
  }
}
