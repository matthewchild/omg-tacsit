/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Sep 3, 2011
 */
package org.omg.tacsit.worldwind.ui.query;

import java.awt.Dialog;
import java.awt.Window;
import javax.swing.Icon;
import javax.swing.JDialog;
import org.omg.tacsit.common.ui.dialog.ShowDialogAction;
import org.omg.tacsit.query.DefaultEntityTypeQuery;
import org.omg.tacsit.query.QueryManager;
import org.omg.tacsit.ui.entity.PointEntityTableModel;
import org.omg.tacsit.ui.entity.TableEntityChooserUI;
import org.omg.tacsit.ui.query.QueryPanel;
import org.omg.tacsit.worldwind.entity.WWEntityType;
import org.omg.tacsit.worldwind.geometry.GeometryFactory;
import org.omg.tacsit.worldwind.query.WWContainmentQuery;
import org.omg.tacsit.worldwind.query.WWIntersectionQuery;
import org.omg.tacsit.worldwind.ui.geometry.GeometryEntityChooserUI;

/**
 * Shows a query dialog that allows users to run queries on a particular QueryManager.
 * @author Matthew Child
 */
public class ShowQueryDialogAction extends ShowDialogAction
{
  private QueryManager queryManager;
  private GeometryFactory geometryFactory;

  /**
   * Creates a new instance.
   * @param name The name of the action.
   * @param icon The icon to use for the action.
   * @param queryManager The QueryManager to execute Queries against.
   * @param geometryFactory The geometry factory that creates new shapes for GeometryQuerys.
   */
  public ShowQueryDialogAction(String name, Icon icon, QueryManager queryManager, GeometryFactory geometryFactory)
  {
    super(name, icon);
    this.queryManager = queryManager;
    this.geometryFactory = geometryFactory;
  }

  protected Dialog createDialog(Window owner)
  {
    JDialog dialog = new JDialog(owner);
    dialog.setModal(false);
    dialog.setTitle("Query Entity Database");
    QueryPanel editPanel = new QueryPanel();
    TableEntityChooserUI trackViewer = new TableEntityChooserUI(new PointEntityTableModel(), WWEntityType.TRACK);
    trackViewer.setName("Tracks");
    editPanel.addEntityChooser(trackViewer);
    TableEntityChooserUI landmarkViewer = new TableEntityChooserUI(new PointEntityTableModel(), WWEntityType.LANDMARK);
    landmarkViewer.setName("Landmarks");
    editPanel.addEntityChooser(landmarkViewer);
    GeometryEntityChooserUI surfaceGeometryViewer = new GeometryEntityChooserUI();
    surfaceGeometryViewer.setName("Surface Geometry");
    editPanel.addEntityChooser(surfaceGeometryViewer);
    editPanel.setQueryManager(queryManager);
    editPanel.addQuery(new DefaultEntityTypeQuery(WWEntityType.TRACK));
    editPanel.addQuery(new WWContainmentQuery(geometryFactory.createRectangle()));
    editPanel.addQuery(new WWIntersectionQuery(geometryFactory.createRectangle()));
    dialog.add(editPanel);
    dialog.setSize(800, 480);
    return dialog;
  }
  
}
