/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Mar 28, 2011
 */

package org.omg.tacsit.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A SelectionManager which supports only one selection at a time for a given SelectionType.
 * <p>
 * Whenever an entity is added to the selection, the previous selection is removed.  If multiple entities are
 * selected at the same time, 1 entity will be chosen for selection.
 * <p>
 * The same entity may be selected under multiple SelectionTypes, however.  For example, the same track may be both
 * the SelectionType.PRIMARY and SelectionType.SECONDARY.
 * @author Matthew Child
 */
public class SingleSelectionManager implements SelectionManager
{
  private SelectionSupport selectionSupport;
  
  private Map<SelectionType, Entity> typeToSelection;

  /**
   * Creates a new instance.
   */
  public SingleSelectionManager()
  {
    selectionSupport = new SelectionSupport(this);
    typeToSelection = new HashMap();
  }

  @Override
  public void addSelectionListener(SelectionListener listener)
  {
    selectionSupport.addSelectionListener(listener);
  }

  @Override
  public void removeSelectionListener(SelectionListener listener)
  {
    selectionSupport.removeSelectionListener(listener);
  }
  
  @Override
  public void removeFromSelection(List<Entity> entities)
  {
    if(entities != null)
    {
      Iterator<Map.Entry<SelectionType, Entity>> selectionIterator = typeToSelection.entrySet().iterator();
      while (selectionIterator.hasNext())
      {
        Map.Entry<SelectionType, Entity> selection = selectionIterator.next();
        Entity selectedEntity = selection.getValue();
        if(selectedEntity == null)
        {
          continue;
        }
        
        if(entities.contains(selectedEntity))
        {
          selectionIterator.remove();
          SelectionType selectionType = selection.getKey();
          selectionSupport.fireSelectionCleared(selectionType);
        }
      }
    }
  }

  @Override
  public void clearAllSelections()
  {
    for(SelectionType selectionType : SelectionType.values())
    {
      clearSelection(selectionType);
    }
  }

  /**
   * Sets the selection for a given SelectionType.  One entity in the List will be chosen to be selected.
   * @param entitiesToSelect The entities to select
   * @param type The type of selection
   */
  @Override
  public void setSelection(List<Entity> entitiesToSelect, SelectionType type)
  {
    clearSelection(type);
    
    if(!entitiesToSelect.isEmpty())
    {
      Entity entity = entitiesToSelect.iterator().next();
      typeToSelection.put(type, entity);
      selectionSupport.fireSelectionChanged(entity, type);      
    }
  }

  @Override
  public List<Entity> getSelection(SelectionType type)
  {
    Entity selection = typeToSelection.get(type);
    if(selection != null)
    {
      return Collections.singletonList(selection);
    }
    else
    {
      return Collections.emptyList();
    }
  }

  /**
   * Adds entities to the selection.  Because the SingleSelectionManager only supports 1 selected entity for a given
   * EntityType at a time, this will clear anything out of the existing selection.  Likewise, 1 entity will
   * be chosen from the List to represent the new selection.
   * @param entities The entities to add to the selection.
   * @param type The selection type
   */
  @Override
  public void addToSelection(List<Entity> entities, SelectionType type)
  {
    setSelection(entities, type);
  }

  @Override
  public void clearSelection(SelectionType type)
  {
    Entity oldSelection = typeToSelection.remove(type);
    if(oldSelection != null)
    {
      selectionSupport.fireSelectionCleared(type);
    }
  }
}
