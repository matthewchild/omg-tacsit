/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 22, 2011
 */
package org.omg.tacsit.worldwind.ui.query;

import org.omg.tacsit.ui.query.DefaultEntityTypeQueryEditor;
import org.omg.tacsit.worldwind.entity.WWEntityType;

/**
 * A PropertyEditor that edits a EntityTypeQuery with the valid Worldiwnd EntityTypes.
 * @author Matthew Child
 */
public class WorldwindEntityTypeQueryEditor extends DefaultEntityTypeQueryEditor
{

  /**
   * Creates a new instance.
   */
  public WorldwindEntityTypeQueryEditor()
  {
    super.setEntityTypeOptions(WWEntityType.asCollection());
  }
}
