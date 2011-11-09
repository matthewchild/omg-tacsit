/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Mar 30, 2011
 */
package org.omg.tacsit.worldwind;

import org.omg.tacsit.worldwind.entity.WWEntityType;
import org.omg.tacsit.worldwind.entity.WWPointEntity;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import java.beans.PropertyEditorManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import org.omg.tacsit.common.thread.ErrorDialogUncaughtExceptionHandler;
import org.omg.tacsit.ui.TacsitFrame;
import org.omg.tacsit.query.DefaultEntityTypeQuery;
import org.omg.tacsit.repository.MutableEntityRepository;
import org.omg.tacsit.repository.PolledEntityRepository;
import org.omg.tacsit.worldwind.ui.geometry.WWPointEntityEditor;
import org.omg.tacsit.worldwind.ui.geometry.WWSurfaceRectangleEditor;
import org.omg.tacsit.worldwind.ui.geometry.WWSurfaceCircleEditor;
import org.omg.tacsit.worldwind.ui.query.WorldwindEntityTypeQueryEditor;
import org.omg.tacsit.worldwind.geometry.WWSurfaceCircle;
import org.omg.tacsit.worldwind.geometry.WWSurfaceRectangle;
import org.omg.tacsit.worldwind.query.WWContainmentQuery;
import org.omg.tacsit.controller.DefaultTacsitController;
import org.omg.tacsit.worldwind.ui.query.WWGeometryQueryEditor;
import org.omg.tacsit.worldwind.query.WWIntersectionQuery;

/**
 * Main entry point for the example application to demonstrate the Worldwind implementation of the Tacsit 
 * specification.
 * @author Matthew Child
 */
public class Main
{

  private static TacsitFrame createWorldwindTacsitFrame(MutableEntityRepository entityRepo)
  {
    DefaultTacsitController tacsitController = WorldwindTacsitToolkit.createTacsitController(entityRepo);
    TacsitFrame tacsitFrame = WorldwindTacsitToolkit.createWorldwindTacsitFrame(tacsitController);
    WorldwindTacsitToolkit.addViewportMenuItems(tacsitFrame, tacsitController);
    WorldwindTacsitToolkit.addEntityMenuItems(tacsitFrame, entityRepo);

    return tacsitFrame;
  }

  private static MutableEntityRepository createEntityRepository(WWPointEntity mobileEntity)
  {
    PolledEntityRepository entityRepo = new PolledEntityRepository();
    entityRepo.add(mobileEntity);
    return entityRepo;
  }

  private static void initPropertyEditors()
  {
    PropertyEditorManager.registerEditor(DefaultEntityTypeQuery.class, WorldwindEntityTypeQueryEditor.class);
    PropertyEditorManager.registerEditor(WWPointEntity.class, WWPointEntityEditor.class);
    PropertyEditorManager.registerEditor(WWSurfaceRectangle.class, WWSurfaceRectangleEditor.class);
    PropertyEditorManager.registerEditor(WWSurfaceCircle.class, WWSurfaceCircleEditor.class);
    PropertyEditorManager.registerEditor(WWContainmentQuery.class, WWGeometryQueryEditor.class);
    PropertyEditorManager.registerEditor(WWIntersectionQuery.class, WWGeometryQueryEditor.class);
  }

  private static void setToSystemLookAndFeel()
  {
    try
    {
      String laf = UIManager.getSystemLookAndFeelClassName();
      UIManager.setLookAndFeel(laf);
    }
    catch (Exception ex)
    {
      Logger.getLogger(Main.class.getName()).log(Level.INFO, "Couldn't load the system look and feel.", ex);
    }
  }

  private static void initWorldwindJVMProperties()
  {
    System.setProperty("java.net.useSystemProxies", "true");
    if (Configuration.isMacOS())
    {
      System.setProperty("apple.laf.useScreenMenuBar", "true");
      System.setProperty("com.apple.mrj.application.apple.menu.about.name", "World Wind Application");
      System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
      System.setProperty("apple.awt.brushMetalLook", "true");
    }
    else if (Configuration.isWindowsOS())
    {
      System.setProperty("sun.awt.noerasebackground", "true"); // prevents flashing during window resizing
    }
    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
  }

  /**
   * Main entry point.
   * @param args Program arguments.  These are unused.
   */
  public static void main(String[] args)
  {
    initWorldwindJVMProperties();
    setToSystemLookAndFeel();
    initPropertyEditors();
    Thread.setDefaultUncaughtExceptionHandler(new ErrorDialogUncaughtExceptionHandler());

    WWPointEntity mobileEntity = new WWPointEntity(WWEntityType.TRACK);
    mobileEntity.setReferencePosition(Position.fromDegrees(13, 20, 1000));
    Thread moveThread = new Thread(new TrackMoveRunnable(mobileEntity));
    moveThread.start();

    MutableEntityRepository entityRepo = createEntityRepository(mobileEntity);

    TacsitFrame frame = createWorldwindTacsitFrame(entityRepo);

    frame.setVisible(true);
  }


  private static class TrackMoveRunnable implements Runnable
  {

    private WWPointEntity mobileEntity;

    public TrackMoveRunnable(WWPointEntity mobileEntity)
    {
      this.mobileEntity = mobileEntity;
    }

    public void run()
    {
      while (true)
      {
        Position currentPosition = this.mobileEntity.getPosition();
        LatLon newLocation = currentPosition.add(LatLon.fromDegrees(0.001, 0));
        Position newPosition = new Position(newLocation, currentPosition.getElevation());
        mobileEntity.setReferencePosition(newPosition);
        try
        {
          Thread.sleep(10);
        }
        catch (InterruptedException ex)
        {
        }
      }
    }
  }
}
