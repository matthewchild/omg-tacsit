/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 22, 2011
 */
package org.omg.tacsit.ui.entity;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JPanel;
import org.omg.tacsit.common.ui.UIElementTabbedPane;
import org.omg.tacsit.common.ui.panel.MessagePanel;
import org.omg.tacsit.controller.Entity;

/**
 * A panel which can display 0, 1, or multiple EntityChoosers.
 * <p>
 * The panel starts off displaying an initial message.  After Entities have been set on the panel, the number of 
 * EntityChoosers displayed will be dependent on the Collection of Entities being viewed.  Each EntityChooser that 
 * is capable of displaying an Entity will be made visible.
 * <p>
 * <li>If no EntityChoosers can view any Entity, the empty viewer will be displayed.</li>
 * <li>If only 1 EntityChooser can view any (or all) of the Entities, only that EntityChooser will be displayed.</li>
 * <li>If multiple EntityChoosers can view the Entities, all the ones capable of viewing them will be displayed.</li>
 * 
 * @author Matthew Child
 */
public class EntityChooserAggregatorPanel extends JPanel
{
  private static final String INITIAL_VIEWER_COMPONENT = "InitialViewer";
  private static final String EMPTY_VIEWER_COMPONENT = "EmptyViewer";
  private static final String SINGLE_VIEWER_COMPONENT = "SingleViewer";
  private static final String MULTI_VIEWER_COMPONENT = "MultiViewer";
  
  private List<EntityChooserUI> entityChoosers;
  
  private Collection<Entity> entities;
  
  private List<EntityChooserUI> activeViewers;
    
  private EntityChooserTitledPanel titleEntityChooserPanel;
  private UIElementTabbedPane<EntityChooserUI> tabbedEntityChooser;  
  private MessagePanel initialMessagePanel;
  private MessagePanel noChooserPanel;
  
  private CardLayout cardLayout;
    
  /**
   * Creates a new instance.
   */
  public EntityChooserAggregatorPanel()
  {
    cardLayout = new CardLayout();
    setLayout(cardLayout);
    initGUI();
    this.entities = new ArrayList();
    this.entityChoosers = new ArrayList();
    this.activeViewers = new ArrayList();
  }
  
  private void initGUI()
  {
    initialMessagePanel = new MessagePanel();    
    add(initialMessagePanel, INITIAL_VIEWER_COMPONENT);
    
    noChooserPanel = new MessagePanel("No Entities.");
    add(noChooserPanel, EMPTY_VIEWER_COMPONENT);
    
    titleEntityChooserPanel = new EntityChooserTitledPanel();
    add(titleEntityChooserPanel, SINGLE_VIEWER_COMPONENT);
    
    tabbedEntityChooser = new UIElementTabbedPane();
    add(tabbedEntityChooser, MULTI_VIEWER_COMPONENT);
  }
  
  /**
   * Adds an EntityChooser to the List of Choosers that can view entities.  If the chooser can display the Entities
   * that are currently being viewed, the chooser will be made visible.
   * @param chooser The Chooser to add.
   */
  public void addEntityChooser(EntityChooserUI chooser)
  {
    this.entityChoosers.add(chooser);
    maybeActivateChoosers(chooser);
  }
  
  private void maybeActivateChoosers(EntityChooserUI chooser)
  {
    boolean entitiesAdded = reloadEntitiesForChooser(chooser);      
    if(entitiesAdded)
    {
      activateChooser(chooser);
    }
  }
  
  private boolean reloadEntitiesForChooser(EntityChooserUI chooser)
  {
    boolean viewsEntities = false;
    chooser.clearEntities();
    for(Entity entity : this.entities)
    {
      if(chooser.isEntityAllowed(entity))
      {
        chooser.addEntity(entity);
        viewsEntities = true;
      }
    }
    return viewsEntities;
  }
  
  private void activateChooser(EntityChooserUI chooser)
  {
    activeViewers.add(chooser);
    int size = activeViewers.size();
    if(size == 1)
    {
      titleEntityChooserPanel.setEntityChooser(chooser);
      cardLayout.show(this, SINGLE_VIEWER_COMPONENT);
    }
    else if(size == 2)
    {
      EntityChooserUI existingViewer = titleEntityChooserPanel.getEntityChooser();
      titleEntityChooserPanel.setEntityChooser(null);
      
      tabbedEntityChooser.addUIElement(existingViewer);
      tabbedEntityChooser.addUIElement(chooser);
      cardLayout.show(this, MULTI_VIEWER_COMPONENT);
    }
    else
    {
      tabbedEntityChooser.addUIElement(chooser);
    }
  }
  
  private void clearCurrentChoosers()
  {
    activeViewers.clear();
    titleEntityChooserPanel.setEntityChooser(null);
    tabbedEntityChooser.clearUIElements();
    
    cardLayout.show(this, EMPTY_VIEWER_COMPONENT);
  }
  
  /**
   * Sets the entities this panel is viewing.  All EntityChoosers that can view any of the Entities will be displayed,
   * and the ones that can't will be hidden.
   * @param entities The entities that should be viewed.
   */
  public void setEntities(Collection<Entity> entities)
  {
    this.entities = new ArrayList(entities);
    
    clearCurrentChoosers();
    if(!entities.isEmpty())
    {
      for(EntityChooserUI viewer : entityChoosers)
      {
        maybeActivateChoosers(viewer);
      }
    }
    invalidate();
    validate();
    repaint();
  }
  
  /**
   * Gets the initial message displayed by the panel until entities have been set.
   * @return The initial message.
   */
  public String getInitialMessage()
  {
    return this.initialMessagePanel.getMessage();
  }
  
  /**
   * Sets the initial message displayed by the panel until entities have been set.
   * @param message The initial message.
   */
  public void setInitialMessage(String message)
  {
    this.initialMessagePanel.setMessage(message);
  }

  /**
   * Gets the message that's displayed by the panel when no EntityChoosers can be displayed.
   * @return The text that's displayed.
   */
  public String getEmptyEntitiesText()
  {
    return this.noChooserPanel.getMessage();
  }

  /**
   * Sets the message that's displayed by the panel when no EntityChoosers can be displayed.
   * @param emptyEntitiesText The text that should be displayed.
   */
  public void setEmptyEntitiesText(String emptyEntitiesText)
  {
    this.noChooserPanel.setMessage(emptyEntitiesText);
  }
}
