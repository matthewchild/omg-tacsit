/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 23, 2011
 */
package org.omg.tacsit.worldwind.common.layers;

import gov.nasa.worldwind.layers.AbstractLayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.omg.tacsit.common.util.CollectionUtils;

/**
 * A layer which has a store of items that are represented as display objects.
 * <p>
 * This has a great opportunity to be factored into something more general purpose.  Rather than having subclasses of 
 * the layer, it would be preferable to have a single class called "ItemLayer".  This is similar in general concept to 
 * a blend of Java's ListCellRenderer and Worldwind's IconRenderer.  
 * 
 * Worldwind's IconRenderer relies on each icon having all of the display attributes cached into the display object 
 * (an instance of WWIcon).  If those attributes are centralized in external objects (selection state in a 
 * SelectionModel, for example), they could be passed to the ItemRenderer interface.  A tentative implementation
 * would look like this:
 * <p>
 * <code>
 * public class ItemRenderer<br>
 * {<br>
 *   public void renderItem(DrawContext dc, Object item, boolean isSelected);<br>
 *   public void pick(DrawContext dc, List items);<br>
 * }
 * </code>
 * <p>
 * Potential issues for this include the DrawContext's use of orderered renderables.  It might be necessary to have
 * look through all of them to determine order, which would preclude a separation that only renders a single item.
 * <p> 
 * In the case of rendering icons, the ImagePack could then be associated with the ItemRenderer, so all of the logic 
 * for choosing an display representation for an item would be in one place.
 * <p>
 * This approach provides several advantages:
 * <li>It centralizes the drawing logic.</li>
 * <li>It allows state control objects (SelectionModels, etc) to be reused across items which are rendered in differing
 * methods.</li>
 * <li>It allows client classes of ItemLayer to work with data objects exclusively.  They can implement the data object,
 * and the renderer</li>
 * <li>May possibly be more efficient.  Since the renderer pattern is a "stamp" style pattern, each renderer would
 * only need 1 intermediate display item (WWIcon, for instance).</li>
 * @param <ITEM> The type of item contained in the item layer.
 * @param <SEL_TYPE> The selection type.
 * @param <REP> The display representation type.
 * @author Matthew Child
 */
public abstract class AbstractItemLayer<ITEM, SEL_TYPE, REP> extends AbstractLayer
{

  private Map<ITEM, REP> itemToRepresentation;
  private Map<REP, ITEM> representationToItem;
  private ItemSelectionModel<SEL_TYPE> selectionModel;
  private List<SEL_TYPE> selectionTypePriority;

  /**
   * Creates a new instance.
   */
  public AbstractItemLayer()
  {
    this.itemToRepresentation = new HashMap();
    this.representationToItem = new HashMap();
    this.selectionModel = new ItemSelectionModel();
  }

  /**
   * Gets the items visualized in this set.
   * @return The set of visualized items.
   */
  public Set<ITEM> getItems()
  {
    return Collections.unmodifiableSet(itemToRepresentation.keySet());
  }

  /**
   * Sets the priority ordering of selection types.  Higher priority selection types will override lower priority
   * selection types for display purposes.  The first element in the list is the highest priority.
   * @param selectionTypePriority An ordered list representing the priority of the selection types.
   */
  public void setSelectionTypePriority(List<SEL_TYPE> selectionTypePriority)
  {
    this.selectionTypePriority = CollectionUtils.copyToUnmodifiableList(selectionTypePriority);
  }
  
  /**
   * Gets the priority ordering of selection types.  Higher priority selection types will override lower priority
   * selection types for display purposes.  The first element in the list is the highest priority.
   * @return An unmodifiable, ordered list representing the priority of the selection types.
   */
  public List<SEL_TYPE> getSelectionTypePriority()
  {
    return selectionTypePriority;
  }
  
  /**
   * Checks to see if this item is being displayed in the layer.
   * @param item The item to check and see if it's identified in the layer.
   * @return true if the item is displayed in the layer, or false otherwise.
   */
  public boolean containsItem(ITEM item)
  {
    return itemToRepresentation.containsKey(item);
  }
  
  /**
   * Checks to see if an item is selected.
   * @param item The item to check and see if it's selected.
   * @return true if the item is selected, false otherwise.
   */
  public boolean isSelected(ITEM item)
  {
    List<SEL_TYPE> selectionTypes = getSelectionTypes(item);
    if(selectionTypes == null)
    {
      return false;
    }
    else
    {
      return !selectionTypes.isEmpty();
    }
  }
  
  /**
   * Gets all of the selection types for a particular item.
   * @param item The item to find the selection types of.
   * @return The list of selection types for the item.
   */
  public List<SEL_TYPE> getSelectionTypes(ITEM item)
  {
    return selectionModel.getSelectionTypes(item);
  }

  /**
   * Gets the selection type that should be displayed for an item.
   * @param item The item to get the display selection type for.
   * @return The selection type that should be displayed.
   */
  protected SEL_TYPE getDisplaySelectionType(ITEM item)
  {
    final SEL_TYPE DEFAULT_SELECTION_TYPE = null;
    if (this.selectionModel == null)
    {
      return DEFAULT_SELECTION_TYPE;
    }

    Collection selectionTypes = getSelectionTypes(item);
    if ((selectionTypes == null) || (selectionTypes.isEmpty()))
    {
      return DEFAULT_SELECTION_TYPE;
    }

    if (selectionTypePriority != null)
    {
      for (SEL_TYPE selectionType : selectionTypePriority)
      {
        if (selectionTypes.contains(selectionType))
        {
          return selectionType;
        }
      }
    }

    Iterator<SEL_TYPE> selectionTypeIterator = selectionTypes.iterator();
    SEL_TYPE firstSelectionType = selectionTypeIterator.next();
    return firstSelectionType;
  }

  /**
   * Gets the visual display representation for an item.
   * <p>
   * This has protected access because external classes shouldn't modify the representation directly; they should
   * be working with the ITEMs.
   * @param item The item to get the display representation of.
   * @return The visual display representation for an item.
   */
  protected REP getRepresentation(ITEM item)
  {
    REP representation = itemToRepresentation.get(item);
    return representation;
  }

  /**
   * Gets the ITEM associated with this display representation.
   * <p>
   * Currently, implementation of picking in the AbstractItemLayer causes a display object to be "picked".  This
   * method allows you to turn that display object into the original item it was representing.
   * @param representation The visual display representation to get the associated item for.
   * @return The item associated with the display representation, or null if none exists.
   */
  public ITEM getItem(REP representation)
  {
    ITEM item = representationToItem.get(representation);
    return item;
  }

  /**
   * Creates a new representation for an item.
   * @param item An item to create the new representation for.
   * @return A new representation for the item.
   */
  protected abstract REP newRepresentation(ITEM item);

  /**
   * Adds a visual representation to the layer.
   * @param representation The representation to a add to the layer.
   */
  protected abstract void addToLayer(REP representation);

  /**
   * Adds an item to the layer.
   * @param item The item to add to the layer. May not be null.
   */
  public void addItem(ITEM item)
  {
    if (item == null)
    {
      throw new IllegalArgumentException("item may not be null");
    }

    REP representation = newRepresentation(item);
    this.itemToRepresentation.put(item, representation);
    this.representationToItem.put(representation, item);
    addToLayer(representation);
  }

  /**
   * Adds a collection of items to the layer.
   * @param items The items to add to the layer.  May not have null elements.
   */
  public void addItems(Collection<? extends ITEM> items)
  {
    for (ITEM item : items)
    {
      addItem(item);
    }
  }

  /**
   * Removes a visual representation from the layer.
   * @param representation  The representation to remove from the layer.
   */
  protected abstract void removeFromLayer(REP representation);

  /**
   * Removes an item from the layer.
   * @param item The item to remove from the layer.
   */
  public void removeItem(ITEM item)
  {
    if (item != null)
    {
      REP representation = itemToRepresentation.remove(item);
      representationToItem.remove(representation);
      removeFromLayer(representation);
    }
  }

  /**
   * Removes a collection of items from the layer.
   * @param items The items to remove from the layer.
   */
  public void removeItems(Collection<? extends ITEM> items)
  {
    for (ITEM item : items)
    {
      removeItem(item);
    }
  }

  /**
   * Clears all representations from the layer.
   */
  protected abstract void clearRepresentationsFromLayer();

  /**
   * Clears all items from the layer.
   */
  public void clearItems()
  {
    itemToRepresentation.clear();
    selectionModel.clearAll();
    clearRepresentationsFromLayer();
  }

  /**
   * Changes the representation of an item to a new representation.
   * @param item The item to change the representation of.
   * @param representation The new representation for the item.
   */
  protected void setRepresentation(ITEM item, REP representation)
  {
    REP existingRepresentation = itemToRepresentation.remove(item);
    representationToItem.remove(existingRepresentation);

    removeFromLayer(existingRepresentation);

    itemToRepresentation.put(item, representation);
    representationToItem.put(representation, item);

    addToLayer(representation);
  }

  /**
   * Updates the display attributes of a representation for an item.
   * @param item The item being represented.
   * @param representation The representation that should be updated.
   */
  protected abstract void updateDisplayAttributes(ITEM item, REP representation);

  /**
   * Updates all representations that are displayed in this layer.
   */
  protected void updateAllRepresentations()
  {
    updateItems(itemToRepresentation.keySet());
  }

  /**
   * Notifies the layer that the item has changed, and should be updated.  The item that has been changed must
   * already be added to the layer.
   * @param item The item that has been changed.
   */
  public void updateItem(ITEM item)
  {
    REP representation = getRepresentation(item);
    updateDisplayAttributes(item, representation);
    fireLayerChanged();
  }

  /**
   * Notifies the layer that a collection of items have been changed, and should be updated.  The items that were
   * changed must already be added to the layer.
   * @param items The items that have been updated.
   */
  public void updateItems(Collection<? extends ITEM> items)
  {
    for (ITEM item : items)
    {
      updateItem(item);
    }
    fireLayerChanged();
  }

  /**
   * Adds an item to the selection.
   * @param item The item that's selected.
   * @param selectionType The type of selection.
   */
  public void addItemToSelection(ITEM item, SEL_TYPE selectionType)
  {
    selectionModel.addToSelection(item, selectionType);
    fireLayerChanged();
  }

  /**
   * Clears all selections for an item.
   * @param item The item to clear the selection of.
   */
  public void clearItemSelection(ITEM item)
  {
    selectionModel.clearSelection(item);
    fireLayerChanged();
  }

  /**
   * Clears all the selections for a given selection type.
   * @param selectionType The selection type to clear.
   */
  public void clearSelectionType(SEL_TYPE selectionType)
  {
    selectionModel.clearSelectionType(selectionType);
  }

  /**
   * Fires notification that the layer has changed, and needs to be repainted.
   */
  protected void fireLayerChanged()
  {
    firePropertyChange("layer", null, this);
  }

  private class ItemSelectionModel<SEL_TYPE>
  {
    private Map<SEL_TYPE, Set<ITEM>> selectionTypeToItems;

    public ItemSelectionModel()
    {
      selectionTypeToItems = new HashMap();
    }

    private Set<ITEM> lazyGetSelections(SEL_TYPE selectionType)
    {
      Set<ITEM> selectionSet;
      if (selectionTypeToItems.containsKey(selectionType))
      {
        selectionSet = selectionTypeToItems.get(selectionType);
      }
      else
      {
        selectionSet = new HashSet();
        selectionTypeToItems.put(selectionType, selectionSet);
      }
      return selectionSet;
    }

    public void addToSelection(ITEM item, SEL_TYPE selectionType)
    {
      Set<ITEM> itemsForSelectionType = lazyGetSelections(selectionType);
      itemsForSelectionType.add(item);
    }

    public void clearAll()
    {
      selectionTypeToItems.clear();
    }

    public List<SEL_TYPE> getSelectionTypes(ITEM item)
    {
      List<SEL_TYPE> selectionsForIcon = null;
      for (Map.Entry<SEL_TYPE, Set<ITEM>> entry : selectionTypeToItems.entrySet())
      {
        Set selectionsForType = entry.getValue();
        if (selectionsForType.contains(item))
        {
          if (selectionsForIcon == null)
          {
            selectionsForIcon = new ArrayList();
          }
          SEL_TYPE selectionType = entry.getKey();
          selectionsForIcon.add(selectionType);
        }
      }
      return selectionsForIcon;
    }

    public void clearSelection(ITEM item)
    {
      for (Map.Entry<SEL_TYPE, Set<ITEM>> entry : selectionTypeToItems.entrySet())
      {
        Set<ITEM> selectionsForType = entry.getValue();
        selectionsForType.remove(item);
      }
    }

    public void clearSelectionType(SEL_TYPE selectionType)
    {
      selectionTypeToItems.remove(selectionType);
    }
  }
}
