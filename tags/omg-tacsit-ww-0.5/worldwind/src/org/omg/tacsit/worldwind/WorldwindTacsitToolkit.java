/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 12, 2011
 */
package org.omg.tacsit.worldwind;

import org.omg.tacsit.worldwind.ui.repository.ShowEntityRepositoryDialogAction;
import org.omg.tacsit.worldwind.ui.query.ShowQueryDialogAction;
import org.omg.tacsit.worldwind.entity.WWEntityType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import org.omg.tacsit.controller.EntityType;
import org.omg.tacsit.controller.Projection;
import org.omg.tacsit.controller.DefaultTacsitController;
import org.omg.tacsit.controller.SelectionMethodology;
import org.omg.tacsit.entity.EntityTypeRegistry;
import org.omg.tacsit.repository.EntityRepository;
import org.omg.tacsit.repository.MutableEntityRepository;
import org.omg.tacsit.ui.SetSelectionMethodologyMenuItem;
import org.omg.tacsit.ui.TacsitFrame;
import org.omg.tacsit.ui.TacsitMenuBar;
import org.omg.tacsit.worldwind.geometry.GeometryFactory;
import org.omg.tacsit.worldwind.ui.viewport.WorldwindProjection;
import org.omg.tacsit.worldwind.ui.viewport.WorldwindViewportFactory;

/**
 * A Toolkit that creates Worldwind implementations of the Tacsit standard.
 * @author Matthew Child
 */
public abstract class WorldwindTacsitToolkit
{

  private static final Collection<Projection> PROJECTION_OPTIONS;
  private static final Collection<EntityType> ENTITY_TYPES;

  static
  {
    Collection c = Collections.unmodifiableCollection(Arrays.asList(WorldwindProjection.values()));
    PROJECTION_OPTIONS = (Collection<Projection>) c;

    EntityTypeRegistry entityTypeRegistry = new EntityTypeRegistry();
    entityTypeRegistry.registerEntityTypes(WWEntityType.values());
    ENTITY_TYPES = Collections.unmodifiableCollection(entityTypeRegistry.getEntityTypes());
  }

  private static Collection<Projection> getProjections()
  {
    return PROJECTION_OPTIONS;
  }

  private static Collection<EntityType> getEntityTypes()
  {
    return ENTITY_TYPES;
  }

  /**
   * Creates a new TacsitController configured with the appropriate values for Worldwind.
   * @param entityRepository The repository that contains all of the entities controlled by the TacsitController.
   * @return A new TacsitController that's ready to be used and manipulated.
   */
  public static DefaultTacsitController createTacsitController(EntityRepository entityRepository)
  {
    DefaultTacsitController controller = new DefaultTacsitController();
    controller.setEntityRepository(entityRepository);
    controller.setProjections(getProjections());
    controller.setEntityTypes(getEntityTypes());
    return controller;
  }

  /**
   * Adds the default entity menu items to a frame's menu.
   * @param frame The frame to add the menu items to.
   * @param entityRepo The entity repository that will be manipulated by the entity actions.
   */
  public static void addEntityMenuItems(TacsitFrame frame, MutableEntityRepository entityRepo)
  {
    final String MENU_NAME = "Entity";

    GeometryFactory geometryFactory = new GeometryFactory();
    Action queryAction = new ShowQueryDialogAction("Query Entities", null, entityRepo, geometryFactory);
    TacsitMenuBar menuBar = frame.getTacsitMenuBar();
    menuBar.addAction(MENU_NAME, queryAction);

    Action editEntitiesAction = new ShowEntityRepositoryDialogAction("Edit Entities", null, entityRepo, geometryFactory);
    menuBar.addAction(MENU_NAME, editEntitiesAction);
  }

  /**
   * Adds the default viewport menu items to a frame's menu.
   * @param frame The frame to add the menu items to.
   * @param tacsitController The controller that manages the Viewports.
   */
  public static void addViewportMenuItems(TacsitFrame frame, DefaultTacsitController tacsitController)
  {
    final String MENU_NAME = "Viewport";
    JMenu selectionMethodologyMenu = new JMenu("Selection Methodology");

    SetSelectionMethodologyMenuItem viewportDependentItem = new SetSelectionMethodologyMenuItem(
        SelectionMethodology.VIEWPORT_DEPENDENT, tacsitController);
    selectionMethodologyMenu.add(viewportDependentItem);
    SetSelectionMethodologyMenuItem viewportIndependentItem = new SetSelectionMethodologyMenuItem(
        SelectionMethodology.VIEWPORT_INDEPENDENT, tacsitController);
    selectionMethodologyMenu.add(viewportIndependentItem);

    TacsitMenuBar menuBar = frame.getTacsitMenuBar();
    menuBar.addMenuItem(MENU_NAME, selectionMethodologyMenu);
  }

  /**
   * Creates a standard TacsitFrame with only an "Exit" menu action.
   * @param tacsitController The controller that serves as the data model for the frame.
   * @return A new TacsitFrame that views and manipulates the TacsitController parameter.
   */
  public static TacsitFrame createWorldwindTacsitFrame(DefaultTacsitController tacsitController)
  {
    TacsitFrame tacsitFrame = new TacsitFrame();
    tacsitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    tacsitFrame.setSize(1024, 768);
    tacsitFrame.setLocationRelativeTo(null);

    tacsitController.setSelectionMethodology(SelectionMethodology.VIEWPORT_INDEPENDENT);

    WorldwindViewportFactory viewportFactory = new WorldwindViewportFactory();
    tacsitFrame.setViewportFactory(viewportFactory);

    tacsitFrame.setTacsitController(tacsitController);

    return tacsitFrame;
  }
}
