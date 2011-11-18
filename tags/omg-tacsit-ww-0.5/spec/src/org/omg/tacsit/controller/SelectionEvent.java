/**
 * @(#) SelectionEvent.java
 */

package org.omg.tacsit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SelectionEvent is the type of event that is passed to a SelectionListener
 * each time there is a change in one of the selections of a SelectionManager.
 */
public class SelectionEvent
{
	private SelectionManager source;
	
	private SelectionType type;
	
	private List<Entity> entities;
	
	public SelectionEvent(List<Entity> entities, SelectionManager source, SelectionType type )
	{
    if(entities == null)
    {
      this.entities = Collections.emptyList();
    }
    else
    {
      // Copy the list to ensure no external modifications are made to the event after its creation.
      List<Entity> listCopy = new ArrayList(entities);
      this.entities = Collections.unmodifiableList(listCopy);
    }
    this.source = source;
    this.type = type;
		
	}
	
	public List<Entity> getEntities( )
	{
		return this.entities;
	}
	
	/**
	 * Returns the type of selection that has changed.
	 * 
	 * @return 
	 */
	public SelectionType getSelectionType( )
	{
		return this.type;
	}
	
	/**
	 * Returns the SelectionManager in which the selection change occurred.
	 * 
	 * @return 
	 */
	public SelectionManager getSource( )
	{
		return this.source;
	}
	
	
}
