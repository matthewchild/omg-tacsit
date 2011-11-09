/**
 * @(#) SelectionManager.java
 */

package org.omg.tacsit.controller;

import java.util.List;

/**
 * The selection manager is the entry point for managing the selections of the
 * Tacsit controller.
 * 
 * Multiple selections can be managed using the selection manager. Each
 * selection has a specific selection type. Selection types are predefined. Each
 * selection is a list of the type Entity. This means that each element can only
 * occur once in the selection and the order of the elements in the selection is
 * not determined.
 * 
 * Managing a selection through the selection manager means that the selection
 * manager allows retrieving and modifying each of the selections. It also
 * allows users to register and unregister selection listeners which will be
 * notified in case of a change in a selection.
 * 
 * The scope of the SelectionManager is determined by the SelectionMethodology
 * of the Tacsit controller.
 */
public interface SelectionManager
{
	/**
	 * Registers the given SelectionListener to this SelectionManager.
	 * 
	 * After registration the SelectionListener will be notified through its
	 * only method each time that a change occurs in one of the selections
	 * managed by this SelectionManager. Registering a SelectionListener that
	 * was already registered with this SelectionManager therefore does not have
	 * any effect.
	 * 
	 */
	void addSelectionListener( SelectionListener listener );
	
	/**
	 * Unregisters the given SelectionListener from this SelectionManager.
	 * 
	 * Afterwards the SelectionListener will not be notified of changes in the
	 * selections of this SelectionManager. Unregistering a SelectionListener
	 * that was not registered therefore does not have any effect.
	 * 
	 */
	void removeSelectionListener( SelectionListener listener );
	
	/**
	 * Removes all Entities from the given EntitySet from the selection from the
	 * selection to which they belong.
	 * 
	 * Afterwards, these entities will not be contained in any selection until
	 * added (where contains is based on equals).
	 * 
	 * Alll SelectionListeners registered at the time of the removeFromSelection
	 * are notified by a call of their method selectionChanged for each
	 * SelectionType from which at least one Entity was removed.
	 * 
	 */
	void removeFromSelection(List<Entity> entities );
	
	/**
	 * Clears all selections of this SelectionManager. Afterwards for any
	 * SelectionType, the selection of that type contains no Entities.
	 * 
	 * All SelectionListeners registered at the time of the clearAllSelections
	 * are notified by a call of their method selectionChanged.
	 */
	void clearAllSelections( );
	
	/**
	 * Changes the selection of the given SelectionType to the given EntitySet.
	 * The given EntitySet should not be empty (see clearSelection and
	 * clearAllSelections).
	 * 
	 * Afterwards the set of Entities of getSelection( type ) equals the set of
	 * Entities of the given EntitySet.
	 * 
	 * All SelectionListeners registered at the time of the setSelection are
	 * notified by a call of their method selectionChanged.
	 * 
	 */
	void setSelection(List<Entity> entities, SelectionType type );
	
	/**
	 * Returns the selection of the given SelectionType of this
	 * SelectionManager.
	 * 
	 * Note that editing the returned EntitySet does not have an impact on the
	 * selection of the given SelectionType of this SelectionManager.
	 * 
	 * @return 
	 */
	List<Entity> getSelection( SelectionType type );
	
	/**
	 * Adds all elements of the given array of Entities to the selection of the
	 * given SelectionType of this SelectionManager. The given EntityList should
	 * not be empty.
	 * 
	 * Afterwards, getSelection( type ) contains all elements of the given array
	 * of Entities as well as any entities previously selected for that type.
	 * 
	 * Each Entity can be part of only one selection at a time. Therefore, all
	 * entities of the given array of Entities that were part of a selection of
	 * another SelectionType than the given type are removed from those
	 * selections.
	 * 
	 * All SelectionListeners registered at the time of the addToSelection are
	 * notified by a call of their method selectionChanged.
	 * 
	 */
	void addToSelection(List<Entity> entities, SelectionType type );
	
	/**
	 * Clears the selection of the given SelectionType of this SelectionManager.
	 * Afterwards the selection of the given SelectionType of this
	 * SelectionManager does not contain any Entities. All other selections of
	 * this SelectionManager are unchanged.
	 * 
	 * All SelectionListeners registered at the time of the clearSelection are
	 * notified by a call of their method selectionChanged.
	 * 
	 */
	void clearSelection( SelectionType type );
	
	
}
