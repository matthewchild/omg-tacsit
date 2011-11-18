/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 28, 2011
 */
package org.omg.tacsit.worldwind.ui.geometry;

import org.omg.tacsit.common.ui.ComponentUtils;
import org.omg.tacsit.ui.geometry.PositionPanel;
import org.omg.tacsit.geometry.GeodeticPosition;
import org.omg.tacsit.worldwind.geometry.WWGeodeticPosition;
import org.omg.tacsit.worldwind.geometry.WWSurfaceCircle;

/**
 * A panel that can edit a WWSurfaceCircle.
 * @author Matthew Child
 */
public class WWSurfaceCirclePanel extends javax.swing.JPanel
{
  /**
   * The property fired when the value in the panel changes.
   */
  public static final String VALUE_PROPERTY = "value";
  
  private WWSurfaceCircle value;
  
  private boolean editable;
  
  private boolean isInitializingFieldValues;

  /**
   * Creates a new instance.
   */
  public WWSurfaceCirclePanel()
  {
    editable = true;
    isInitializingFieldValues = false;
    initComponents();
  }

  /**
   * Gets the value in the panel.
   * @return The value being displayed in the panel.
   */
  public WWSurfaceCircle getValue()
  {
    return value;
  }

  /**
   * Sets the value in the panel.
   * @param value The value being displayed in the panel.
   */
  public void setValue(WWSurfaceCircle value)
  {
    this.value = value;
    updateFieldValues(this.value);
    checkEditState();
  }
  
  private void updateFieldValues(WWSurfaceCircle circle)
  {
    isInitializingFieldValues = true;
    
    double radiusMeters = 0.0d;
    GeodeticPosition centerPoint = null;

    if (circle != null)
    {
      radiusMeters = circle.getRadius();
      centerPoint = circle.getCenter();
    }
    
    radiusField.setValue(radiusMeters);
    centerPointPanel.setValue(centerPoint);
    
    isInitializingFieldValues = false;
  }
  
  /**
   * Sets whether the value in the panel can be edited.
   * @param editable whether the value should be edited.
   */
  public void setEditable(boolean editable)
  {
    this.editable = editable;
    checkEditState();
  }

  private void checkEditState()
  {
    boolean propertiesEditable = arePropertiesEditable();

    radiusField.setEditable(propertiesEditable);
    centerPointPanel.setEditable(propertiesEditable);
  }

  private boolean arePropertiesEditable()
  {
    return editable && (value != null);
  }
  
  private void setRadius(double radius)
  {
    if (arePropertiesEditable())
    {
      this.value.setRadius(radius);
    }
  }

  private void setCenterPoint(GeodeticPosition centerPoint)
  {
    if (arePropertiesEditable())
    {
      WWGeodeticPosition center = WWGeodeticPosition.toWWGeodeticPosition(centerPoint);
      this.value.setCenter(center);
    }
  }
  
  private double getDoubleValue(Number number)
  {
    double doubleValue = 0.0;
    if(number != null)
    {
      doubleValue = number.doubleValue();
    }
    return doubleValue;
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    javax.swing.JLabel radiusLabel = new javax.swing.JLabel();
    radiusField = new javax.swing.JFormattedTextField();
    javax.swing.JLabel radiusUnitsLabel = new javax.swing.JLabel();
    centerPointPanel = new org.omg.tacsit.ui.geometry.PositionPanel();

    radiusLabel.setText("Radius:");

    radiusField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
    radiusField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
    radiusField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        radiusPropertyChange(evt);
      }
    });

    radiusUnitsLabel.setText("Meters");

    centerPointPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Center Point"));
    centerPointPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        centerPropertyChange(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(radiusLabel)
            .addGap(18, 18, 18)
            .addComponent(radiusField, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(radiusUnitsLabel))
          .addComponent(centerPointPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(radiusLabel)
          .addComponent(radiusField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(radiusUnitsLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(centerPointPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void radiusPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_radiusPropertyChange
  {//GEN-HEADEREND:event_radiusPropertyChange
    if(!isInitializingFieldValues)
    {
      String propertyName = evt.getPropertyName();
      if (ComponentUtils.FORMATTED_FIELD_VALUE_PROPERTY.equals(propertyName))
      {
        Number radius = (Number) radiusField.getValue();
        setRadius(getDoubleValue(radius));
      }
    }
  }//GEN-LAST:event_radiusPropertyChange

  private void centerPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_centerPropertyChange
  {//GEN-HEADEREND:event_centerPropertyChange
    if (!isInitializingFieldValues)
    {
      String propertyName = evt.getPropertyName();
      if (propertyName.equals(PositionPanel.VALUE_PROPERTY))
      {
        GeodeticPosition position = centerPointPanel.getValue();
        setCenterPoint(position);
      }
    }
  }//GEN-LAST:event_centerPropertyChange

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private org.omg.tacsit.ui.geometry.PositionPanel centerPointPanel;
  private javax.swing.JFormattedTextField radiusField;
  // End of variables declaration//GEN-END:variables
}