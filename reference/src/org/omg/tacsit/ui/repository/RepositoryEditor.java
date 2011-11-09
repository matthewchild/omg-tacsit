/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jul 17, 2011
 */
package org.omg.tacsit.ui.repository;

import org.omg.tacsit.common.ui.UIElement;
import org.omg.tacsit.repository.MutableEntityRepository;

/**
 * A UI Component that edits a repository.
 * @author Matthew Child
 */
public interface RepositoryEditor extends UIElement
{
  /**
   * Gets the repository that's being edited by the component.
   * @return The repository being edited.
   */
  public MutableEntityRepository getEntityRepository();
  
  /**
   * Sets the repository that's being edited by the component.
   * @param repository The repository to edit.
   */
  public void setEntityRepository(MutableEntityRepository repository);
}
