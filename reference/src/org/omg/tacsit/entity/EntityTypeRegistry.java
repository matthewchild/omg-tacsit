/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 20, 2011
 */
package org.omg.tacsit.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.tacsit.controller.EntityType;

/**
 * A registry that stores associates a particular identifier with an EntityType instance.
 * @param <I> The identifier that's associated with an entity type.
 * @author Matthew Child
 */
public class EntityTypeRegistry<I>
{
  private Map<I, EntityType> identifierToType;

  /**
   * Creates a new instance.
   */
  public EntityTypeRegistry()
  {
    identifierToType = new HashMap();
  }
  
  /**
   * Registers a set of identifiers as new entity types.
   * @param identifiers The identifiers to register as EntityTypes.
   */
  public void registerEntityTypes(I[] identifiers)
  {
    if(identifiers != null)
    {
      for (I identifier : identifiers)
      {
        registerEntityType(identifier);
      }
    }
  }
  
  /**
   * Registers an identifier as a new EntityType.
   * @param identifier The identifier that marks an EntityType.
   */
  public void registerEntityType(I identifier)
  {
    if(identifierToType.containsKey(identifier))
    {
      Logger.getLogger(EntityTypeRegistry.class.getName()).log(Level.INFO, "identifier {0} is already registered; replacing it.", identifier);
    }
    
    EntityType entityType = new DefaultEntityType(identifier);
    identifierToType.put(identifier, entityType);
  }
  
  /**
   * Gets the EntityType that's associated with a particular identifier.
   * @param identifier The identifier that describes the EntityType.
   * @return The EntityType associated with the identifier, or null if the identifier has not been registered.
   */
  public EntityType getEntityType(I identifier)
  {
    EntityType entityType = identifierToType.get(identifier);
    if(entityType == null)
    {
      entityType = new DefaultEntityType(identifier);
      identifierToType.put(identifier, entityType);
    }
    return entityType;
  }
  
  /**
   * Gets all EntityTypes that have been registered.
   * @return An unmodifiable Collection of all of the registered EntityTypes.
   */
  public Collection<EntityType> getEntityTypes()
  {
    return Collections.unmodifiableCollection(identifierToType.values());
  }
}
