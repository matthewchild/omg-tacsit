/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 15, 2011
 */
package org.omg.tacsit.ui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import org.omg.tacsit.controller.DefaultTacsitController;
import org.omg.tacsit.controller.SelectionMethodology;

/**
 * A MenuItem for setting the selection methodology of a DefaultTacsitController.
 * @author Matthew Child
 */
public class SetSelectionMethodologyMenuItem extends JCheckBoxMenuItem
{
  private SelectionMethodology methodology;
  private DefaultTacsitController tacsitController;

  /**
   * Creates a new instance.
   * @param methodologyToSelect The SelectionMethodology that should be chosen when this item is selected.
   * @param tacsitController The tacsit controller who's selection methodology should be changed.
   */
  public SetSelectionMethodologyMenuItem(SelectionMethodology methodologyToSelect, DefaultTacsitController tacsitController)
  {
    if (methodologyToSelect == null)
    {      
      throw new IllegalArgumentException("methodologyToSelect may not be null");
    }
    if (tacsitController == null)
    {      
      throw new IllegalArgumentException("tacsitController may not be null");
    }
    
    this.methodology = methodologyToSelect;
    this.tacsitController = tacsitController;
    this.tacsitController.addPropertyChangeListener(DefaultTacsitController.PROPERTY_SELECTION_METHODOLOGY, new MethodologyChangeListener());
    setCheckedIfMethodologyMatches(tacsitController.getSelectionMethodology());
    this.setAction(new ChooseSelectionMethodologyAction(methodology.toString()));
  }
  
  private void setCheckedIfMethodologyMatches(SelectionMethodology currentState)
  {
    boolean matches = methodology == currentState;
    setSelected(matches);
  }
  
  private class MethodologyChangeListener implements PropertyChangeListener
  {

    public void propertyChange(PropertyChangeEvent evt)
    {
      SelectionMethodology newValue = (SelectionMethodology)evt.getNewValue();
      setCheckedIfMethodologyMatches(newValue);
    }
  }
  
  private class ChooseSelectionMethodologyAction extends AbstractAction
  {

    public ChooseSelectionMethodologyAction(String name)
    {
      super(name);
    }

    public void actionPerformed(ActionEvent e)
    {
      tacsitController.setSelectionMethodology(methodology);
    }
  }
}
