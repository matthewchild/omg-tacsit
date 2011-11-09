/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 11, 2011
 */
package org.omg.tacsit.worldwind.ui.viewport;

import org.omg.tacsit.worldwind.entity.WWEntityType;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.controller.EntityType;
import org.omg.tacsit.controller.SelectionType;
import org.omg.tacsit.ui.entity.EntityCollectionUI;
import org.omg.tacsit.worldwind.common.layers.AbstractItemLayer;
import org.omg.tacsit.worldwind.entity.WWPointEntity;
import org.omg.tacsit.worldwind.common.layers.ImagePack;
import org.omg.tacsit.worldwind.common.layers.ItemIconLayer;
import org.omg.tacsit.worldwind.entity.GeometryEntity;

/**
 * A LayerList which displays all the EntityTypes that are defined in the Worldwind Tacsit implementation.
 * @author Matthew Child
 */
public class EntityLayerList
{

  private LayerList layerList;
  private Map<EntityType, EntityToItemLayerAdapter> typeToLayer;
  private ItemIconLayer trackLayer;

  /**
   * Creates a new instance.
   */
  public EntityLayerList()
  { 
    initLayers();
  }

  private void initLayers()
  {
    layerList = new LayerList();
    typeToLayer = new HashMap();
    
    Map<SelectionType, Color> selectionTypeToColor = createSelectionColorMap();
    List<SelectionType> selectionPriorityList = createSelectionPriorityList();

    AbstractItemLayer geometryLayer = createGeometryLayer(selectionTypeToColor, selectionPriorityList);
    typeToLayer.put(WWEntityType.SURFACE_GEOMETRY, new EntityToItemLayerAdapter(geometryLayer, GeometryEntity.class));
    layerList.add(geometryLayer);

    AbstractItemLayer landmarkLayer = createLandmarkLayer(selectionTypeToColor, selectionPriorityList);
    typeToLayer.put(WWEntityType.LANDMARK, new EntityToItemLayerAdapter(landmarkLayer, WWPointEntity.class));
    layerList.add(landmarkLayer);

    trackLayer = createTrackLayer(selectionTypeToColor, selectionPriorityList);
    typeToLayer.put(WWEntityType.TRACK, new EntityToItemLayerAdapter(trackLayer, WWPointEntity.class));
    layerList.add(trackLayer);
  }
  
  private AbstractItemLayer createGeometryLayer(Map<SelectionType, Color> selectionTypeToColor, List<SelectionType> selectionPriorityList)
  {
    GeometryEntityLayer<SelectionType> geometryLayer = new GeometryEntityLayer<SelectionType>();
    for (Map.Entry<SelectionType, Color> entry : selectionTypeToColor.entrySet())
    {
      geometryLayer.setSelectionColor(entry.getKey(), entry.getValue());
    }
    geometryLayer.setSelectionTypePriority(selectionPriorityList);
    return geometryLayer;
  }
  
  private AbstractItemLayer createLandmarkLayer(Map<SelectionType, Color> selectionTypeToColor, List<SelectionType> selectionPriorityList)
  {
    ItemIconLayer landmarkLayer = new ItemIconLayer();
    for (Map.Entry<SelectionType, Color> entry : selectionTypeToColor.entrySet())
    {
      landmarkLayer.setSelectionColor(entry.getKey(), entry.getValue());
    }
    landmarkLayer.setSelectionTypePriority(selectionPriorityList);
    return landmarkLayer;
  }
  
  private ItemIconLayer createTrackLayer(Map<SelectionType, Color> selectionTypeToColor, List<SelectionType> selectionPriorityList)
  {
    ItemIconLayer layer = new ItemIconLayer();
    for (Map.Entry<SelectionType, Color> entry : selectionTypeToColor.entrySet())
    {
      layer.setSelectionColor(entry.getKey(), entry.getValue());
    }
    layer.setSelectionTypePriority(selectionPriorityList);
    return layer;
  }
  
  
  private Map<SelectionType, Color> createSelectionColorMap()
  {
    Map<SelectionType, Color> colorMap = new HashMap();
    colorMap.put(SelectionType.PRIMARY, Color.CYAN);
    colorMap.put(SelectionType.SECONDARY, Color.WHITE);
    colorMap.put(SelectionType.OTHER, Color.GRAY);
    return colorMap;
  }
  
  private List<SelectionType> createSelectionPriorityList()
  {
    List<SelectionType> selectionPriorityList = new ArrayList();
    selectionPriorityList.add(SelectionType.PRIMARY);
    selectionPriorityList.add(SelectionType.SECONDARY);
    selectionPriorityList.add(SelectionType.OTHER);
    return selectionPriorityList;
  }

  /**
   * Gets all of the Layers in this layer list.
   * @return An Iterator of all the layers that make up this EntityLayerList.
   */
  public Iterator<Layer> getLayers()
  {
    return Collections.unmodifiableCollection(layerList).iterator();
  }

  /**
   * Sets the image pack that's used to determine which icons should be displayed for tracks.
   * @param iconSet The image pack that contains the icons for tracks.
   */
  public void setTrackImagePack(ImagePack iconSet)
  {
    trackLayer.setImagePack(iconSet);
  }

  /**
   * Gets the image pack that's used to determine which icons should be displayed for tracks.
   * @return The image pack that contains the icons for tracks.
   */
  public ImagePack getTrackImagePack()
  {
    return trackLayer.getImagePack();
  }

  private EntityToItemLayerAdapter getEntityLayerSafely(EntityType entityType)
  {
    EntityToItemLayerAdapter entityLayer = typeToLayer.get(entityType);
    if (entityLayer == null)
    {
      throw new IllegalArgumentException("entityType has no corresponding layer");
    }
    return entityLayer;
  }

  private EntityToItemLayerAdapter checkedGetEntityLayer(Entity entity)
  {
    if (entity == null)
    {
      throw new IllegalArgumentException("null entity has no associated entity layerr");
    }
    EntityType entityType = entity.getType();
    return getEntityLayerSafely(entityType);
  }

  /**
   * Sets whether or not an entity type should be displayed.
   * @param entityType The entity type to enable or disable
   * @param enabled whether or not the entity type should be displayed.
   */
  public void setEnabled(WWEntityType entityType, boolean enabled)
  {
    EntityToItemLayerAdapter entityLayer = getEntityLayerSafely(entityType);
    entityLayer.setEnabled(enabled);
  }

  /**
   * Adds an entity to be displayed.
   * @param entity The entity to be added to the layer.
   */
  public void addEntity(Entity entity)
  {
    EntityToItemLayerAdapter entityLayer = checkedGetEntityLayer(entity);
    entityLayer.addEntity(entity);
  }

  /**
   * removes an entity to be displayed.
   * @param entity The entity to remove from the layer.
   */
  public void removeEntity(Entity entity)
  {
    EntityToItemLayerAdapter entityLayer = checkedGetEntityLayer(entity);
    entityLayer.removeEntity(entity);
  }

  /**
   * Clears all entities from the layer.
   */
  public void clearEntities()
  {
    for (EntityToItemLayerAdapter entityLayer : typeToLayer.values())
    {
      entityLayer.clearEntities();
    }
  }

  /**
   * Adds a List of entities to the current selection for a selection type.
   * @param selectedEntities The entities to add to the selection.
   * @param selectionType The selection type to be added to.
   */
  public void addToSelection(List<Entity> selectedEntities, SelectionType selectionType)
  {
    for (Entity selectedEntity : selectedEntities)
    {
      EntityToItemLayerAdapter entityLayer = checkedGetEntityLayer(selectedEntity);
      entityLayer.addToSelection(selectedEntity, selectionType);
    }
  }

  /**
   * Clears the selected items for a given selection type.
   * @param selectionType The selection type to clear.
   */
  public void clearSelection(SelectionType selectionType)
  {
    for (EntityToItemLayerAdapter layer : typeToLayer.values())
    {
      layer.clearSelection(selectionType);
    }
  }

  /**
   * Notifies the EntityLayerList that an entity has been updated, and needs to be redisplayed.
   * @param entity The entity that's been updated.
   */
  public void updateEntity(Entity entity)
  {
    EntityToItemLayerAdapter entityLayer = checkedGetEntityLayer(entity);
    entityLayer.updateEntity(entity);
  }

  /**
   * Gets the Entity that's associated with a particular picked Object.
   * <p>
   * Implementation note:  It would be much preferable to use Worldwind's Pick support for
   * choosing the entity as the picked item, rather than going back and getting the correct item.  However, this
   * was non trivial to do for the icon layer due to the method of its having the icons be what is actually "picked".
   * @param pickedObject The pickedObject to get the entity for.
   * @return The Entity that is represented by the pickedObject.
   */
  public Entity getEntityForObject(Object pickedObject)
  {
    // Entities can only exist in a single layer.
    Entity entityForObject = null;
    for (EntityToItemLayerAdapter layer : typeToLayer.values())
    {
      Entity entityInLayer = layer.getEntityForDisplayObject(pickedObject);
      if (entityInLayer != null)
      {
        entityForObject = entityInLayer;
        break;
      }
    }
    return entityForObject;
  }
  
  private class EntityToItemLayerAdapter implements EntityCollectionUI
  {
    private AbstractItemLayer layer;
    private Class<? extends Entity> allowedEntityClass;

    public EntityToItemLayerAdapter(AbstractItemLayer itemLayer, Class<? extends Entity> allowedEntityClass)
    {
      if (itemLayer == null)
      {        
        throw new IllegalArgumentException("itemLayer may not be null");
      }
      
      if (allowedEntityClass == null)
      {        
        throw new IllegalArgumentException("allowedEntityClass may not be null");
      }
      this.layer = itemLayer;
      this.allowedEntityClass = allowedEntityClass;
    }

    public void addEntity(Entity entity)
    {
      layer.addItem(entity);
    }

    public void removeEntity(Entity entity)
    {
      layer.removeItem(entity);
    }

    public void updateEntity(Entity entity)
    {
      layer.updateItem(entity);
    }

    public void clearEntities()
    {
      layer.clearItems();
    }

    public boolean isEntityAllowed(Entity entity)
    {
      return allowedEntityClass.isInstance(entity);
    }

    public void addToSelection(Entity entity, SelectionType selectionType)
    {
      layer.addItemToSelection(entity, selectionType);
    }

    public void clearSelection(SelectionType selectionType)
    {
      layer.clearSelectionType(selectionType);
    }

    public Entity getEntityForDisplayObject(Object displayObject)
    {
      Object item = layer.getItem(displayObject);
      Entity entityForDisplayObject = (Entity) item;
      return entityForDisplayObject;
    }
    
    public void setEnabled(boolean enabled)
    {
      layer.setEnabled(enabled);
    } 
  }
}
