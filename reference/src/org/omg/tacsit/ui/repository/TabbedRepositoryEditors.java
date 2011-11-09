/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 22, 2011
 */
package org.omg.tacsit.ui.repository;

import java.awt.Component;
import org.omg.tacsit.common.ui.UIElementTabbedPane;
import org.omg.tacsit.repository.MutableEntityRepository;

/**
 * A Tabbed Pane that displays a RepositoryEditor in each tab.
 * <p>
 * All repository editors will be editing a single repository.
 * @author Matthew Child
 */
public class TabbedRepositoryEditors implements RepositoryEditor
{
  private UIElementTabbedPane<RepositoryEditor> repositoryEditorTabs;
  
  private MutableEntityRepository entityRepository;

  /**
   * Creates a new instance.
   */
  public TabbedRepositoryEditors()
  {
    repositoryEditorTabs = new UIElementTabbedPane();
    repositoryEditorTabs.setName("Repository Editors");
  }
  
  /**
   * Adds a new repository editor.  It will be displayed in its own tab.
   * @param editor The editor to add.
   */
  public void addEditor(RepositoryEditor editor)
  {
    editor.setEntityRepository(entityRepository);
    repositoryEditorTabs.addUIElement(editor);
  }
  
  /**
   * Removes a repository editor from the tabs.
   * @param editor The editor to remove.
   */
  public void removeEditor(RepositoryEditor editor)
  {
    repositoryEditorTabs.removeUIElement(editor);
    editor.setEntityRepository(null);
  }
  
  /**
   * Clears all editors from the tabs.
   */
  public void clearEditors()
  {
    repositoryEditorTabs.clearUIElements();
  }

  public MutableEntityRepository getEntityRepository()
  {
    return this.entityRepository;
  }

  public void setEntityRepository(MutableEntityRepository repository)
  {
    this.entityRepository = repository;
    for(RepositoryEditor editor : repositoryEditorTabs.getUIElements())
    {
      editor.setEntityRepository(repository);
    }
  }
  
  /**
   * Gets the name of the RepositoryEditor.
   * @return The name of the repository editor.
   */
  public String getName()
  {
    return getComponent().getName();
  }
  
  /**
   * Sets the name of the repository editor.
   * @param name 
   */
  public void setName(String name)
  {
    getComponent().setName(name);
  }

  public Component getComponent()
  {
    return repositoryEditorTabs;
  }
}
