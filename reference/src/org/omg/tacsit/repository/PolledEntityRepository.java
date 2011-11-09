/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 14, 2011
 */
package org.omg.tacsit.repository;

import org.omg.tacsit.entity.PollableEntity;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.Timer;

/**
 * A repository that observes when a PollableEntity has been modified to notify listeners that Entities have been 
 * updated.  The update rate can be configured for granularity of how frequently the Entities are polled for changes
 * in their modification time.
 * <p>
 * Clients are always guaranteed that an add event will fired before an update event for any Entity, and that if an
 * Entity is removed, no subsequent updates will be fired.
 * <p>
 * Update notifications will always occur in the Event Dispatch (Swing) thread, so it may be safely used to update
 * visual components.
 * @author Matthew Child
 */
public class PolledEntityRepository extends DefaultEntityRepository<PollableEntity>
{  
  private Timer refreshTimer;
  private long lastUpdateTime;
   
  /**
   * Creates a new instance.
   */
  public PolledEntityRepository()
  {
    this(100);
  }
  
  /**
   * Creates a new instance.
   * @param updateRate How frequently (in milliseconds) Entities should be polled for changes.
   */
  public PolledEntityRepository(int updateRate)
  {
    this.lastUpdateTime = 0;
    refreshTimer = new Timer(updateRate, new RefreshAction());
    refreshTimer.start();
  }

  @Override
  public synchronized boolean add(PollableEntity entity)
  {
    return super.add(entity);
  }

  @Override
  public synchronized boolean addAll(Collection<? extends PollableEntity> entities)
  {
    return super.addAll(entities);
  }

  @Override
  public synchronized boolean remove(PollableEntity entity)
  {
    return super.remove(entity);
  }

  @Override
  public synchronized boolean removeAll(Collection<? extends PollableEntity> c)
  {
    return super.removeAll(c);
  }

  private synchronized void updateChangedItems()
  {
    Collection<PollableEntity> changedItems = null;
    long newUpdateTime = System.currentTimeMillis();
    Iterator<PollableEntity> entityIterator = getEntities();
    while (entityIterator.hasNext())
    {
      PollableEntity pollableEntity = entityIterator.next();
      long itemModTime = pollableEntity.getLastModified();
      // The system clock is updated in small chunks.  For example, the millis portion of the clock could go
      // from 503 to 507, while never returning the values 504, 505, and 506.  As such, it's entirely possible that
      // an item get updated and a poll event occurs during the same "clock" time when using very fast update rates.  
      // Hence, we need to update the representation even if it should have been updated during the previous
      // update cycle.  The only safe case to ignore an update is if the lastUpdate time is later than the itemModTime.
      if (lastUpdateTime <= itemModTime)
      {
        if(changedItems == null)
        {
          changedItems = new ArrayList();
        }
        changedItems.add(pollableEntity);
      }
    }
    lastUpdateTime = newUpdateTime;
    if(changedItems != null)
    {
      fireEntitiesUpdated(changedItems);
    }
  }

  /**
   * Gets the rate at which this repository polls the entities for changes.
   * @return The update rate, in milliseconds.
   */
  public int getUpdateRate()
  {
    return refreshTimer.getDelay();
  }

  /**
   * Sets the rate at which this repository polls the entities for changes.
   * @param updateRate The new update rate, in milliseconds.
   */
  public void setUpdateRate(int updateRate)
  {
    refreshTimer.setDelay(updateRate);
  }
  
  private class RefreshAction implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      updateChangedItems();
    }
  }
}
