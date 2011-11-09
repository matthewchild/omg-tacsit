/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Sep 3, 2011
 */
package org.omg.tacsit.worldwind.ui.repository;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;
import javax.swing.Icon;
import org.omg.tacsit.common.ui.dialog.ShowDialogAction;
import org.omg.tacsit.common.ui.dialog.UIElementDialog;
import org.omg.tacsit.common.util.DefaultFactory;
import org.omg.tacsit.common.util.Factory;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.repository.MutableEntityRepository;
import org.omg.tacsit.ui.entity.PointEntityTableModel;
import org.omg.tacsit.ui.entity.TableEntityChooserUI;
import org.omg.tacsit.ui.repository.EntityChooserToRepositoryEditorAdapter;
import org.omg.tacsit.ui.repository.RepositoryEditor;
import org.omg.tacsit.ui.repository.TabbedRepositoryEditors;
import org.omg.tacsit.worldwind.entity.GeometryEntityFactory;
import org.omg.tacsit.worldwind.entity.WWEntityType;
import org.omg.tacsit.worldwind.entity.WWPointEntity;
import org.omg.tacsit.worldwind.geometry.GeometryFactory;
import org.omg.tacsit.worldwind.geometry.WWSurfaceShape;
import org.omg.tacsit.worldwind.ui.geometry.GeometryEntityChooserUI;

/**
 * Shows a dialog that allows user modification of an EntityRepository. 
 * @author Matthew Child
 */
public class ShowEntityRepositoryDialogAction extends ShowDialogAction
{
  private MutableEntityRepository entityRepository;
  private GeometryFactory geometryFactory;

  /**
   * Creates a new instance.
   * @param name The name of the action.
   * @param icon The icon to use for the action.
   * @param entityRepository The entity repository to modify.  May not be null.
   * @param geometryFactory The geometry factory that can be used to create new geometry.
   */
  public ShowEntityRepositoryDialogAction(String name, Icon icon, MutableEntityRepository entityRepository,
                                          GeometryFactory geometryFactory)
  {
    super(name, icon);
    if (entityRepository == null)
    {      
      throw new IllegalArgumentException("entityRepository may not be null");
    }    
    this.entityRepository = entityRepository;
    if (geometryFactory == null)
    {      
      throw new IllegalArgumentException("geometryFactory may not be null");
    }    
    this.geometryFactory = geometryFactory;
  }

  private Factory<? extends Entity> createEntityFactory(Class<? extends Entity> entityClass, Object... arguments)
  {
    try
    {
      Factory<? extends Entity> entityFactory = new DefaultFactory(entityClass, arguments);
      return entityFactory;
    }
    catch (NoSuchMethodException ex)
    {
      throw new IllegalArgumentException("Unable to create factory for class " + entityClass + " with arguments " +
                                         arguments, ex);
    }
  }

  private RepositoryEditor createEntityItemEditor(WWEntityType entityType, String name)
  {
    Factory<? extends Entity> entityFactory = createEntityFactory(WWPointEntity.class, entityType);
    TableEntityChooserUI tableViewer = new TableEntityChooserUI(new PointEntityTableModel(), entityType);
    tableViewer.setName(name);
    EntityChooserToRepositoryEditorAdapter editor =
                                           new EntityChooserToRepositoryEditorAdapter(tableViewer, entityFactory);
    return editor;
  }

  private RepositoryEditor createRepositoryEditor(GeometryFactory geometryFactory)
  {
    TabbedRepositoryEditors repositoryEditors = new TabbedRepositoryEditors();
    repositoryEditors.setName("Entity Repository Editor");
    RepositoryEditor trackEditor = createEntityItemEditor(WWEntityType.TRACK, "Tracks");
    repositoryEditors.addEditor(trackEditor);
    RepositoryEditor landmarkEditor = createEntityItemEditor(WWEntityType.LANDMARK, "Landmarks");
    repositoryEditors.addEditor(landmarkEditor);
    GeometryEntityChooserUI surfaceGeometryCollection = new GeometryEntityChooserUI(geometryFactory);
    surfaceGeometryCollection.setName("Surface Geometry");
    GeometryEntityFactory geometryEntityFactory =
                          new GeometryEntityFactory("Surface Shape", geometryFactory, WWSurfaceShape.RECTANGLE);
    EntityChooserToRepositoryEditorAdapter surfaceGeometryEditor =
                                           new EntityChooserToRepositoryEditorAdapter(surfaceGeometryCollection,
                                                                                      geometryEntityFactory);
    repositoryEditors.addEditor(surfaceGeometryEditor);
    return repositoryEditors;
  }

  protected Dialog createDialog(Window owner)
  {
    RepositoryEditor repositoryEditor = createRepositoryEditor(geometryFactory);
    repositoryEditor.setEntityRepository(entityRepository);
    UIElementDialog editorDialog = new UIElementDialog(owner, repositoryEditor);
    editorDialog.setSize(new Dimension(550, 450));
    return editorDialog;
  }
  
}
