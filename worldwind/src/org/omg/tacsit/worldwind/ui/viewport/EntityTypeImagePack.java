/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 23, 2011
 */
package org.omg.tacsit.worldwind.ui.viewport;

import org.omg.tacsit.worldwind.common.layers.KeyedImagePack;
import org.omg.tacsit.worldwind.common.layers.ImagePack;
import java.awt.Image;
import javax.swing.Icon;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.controller.EntityType;

/**
 * An ImagePack that provides assigns an icon based on an EntityType.
 * @param <T> The type of entity this pack contains images for.
 * @author Matthew Child
 */
public class EntityTypeImagePack<T extends Entity> implements ImagePack<T>
{  
  private KeyedImagePack<EntityType> delegate;

  /**
   * Creates a new instance.
   */
  public EntityTypeImagePack()
  {
    delegate = new KeyedImagePack();
  }
  
  public Object getImageSource(T entity)
  {
    EntityType entityType = null;
    if(entity != null)
    {
      entityType = entity.getType();
    }
    Object imageSource = delegate.getImageSource(entityType);
    return imageSource;
  }

  /**
   * Sets the image source that's used if no icon is defined for a particular entity type.
   * @param missingIconPath The file system path to the icon location.
   */
  public void setMissingImageSource(String missingIconPath)
  {
    delegate.setMissingImageSource(missingIconPath);
  }

  /**
   * Sets the image source that's used if no icon is defined for a particular entity type.
   * @param icon The icon to use for the image source.
   */
  public void setMissingImageSource(Icon icon)
  {
    delegate.setMissingImageSource(icon);
  }

  /**
   * Sets the image source that's used if no icon is defined for a particular entity type.
   * @param image The image to use for the image source.
   */
  public void setMissingImageSource(Image image)
  {
    delegate.setMissingImageSource(image);
  }

  /**
   * Sets the image source that's used for entities with a particular EntityType.
   * @param key The EntityType to define the image source for.
   * @param imagePath The file system path to the image source.
   */
  public void setImageSource(EntityType key, String imagePath)
  {
    delegate.setImageSource(key, imagePath);
  }

  /**
   * Sets the image source that's used for entities with a particular EntityType.
   * @param key The EntityType to define the image source for.
   * @param image The image to use as the image source.
   */
  public void setImageSource(EntityType key, Image image)
  {
    delegate.setImageSource(key, image);
  }

  /**
   * Sets the image source that's used for entities with a particular EntityType.
   * @param key The EntityType to define the image source for.
   * @param icon The icon to use as the image source.
   */
  public void setImageSource(EntityType key, Icon icon)
  {
    delegate.setImageSource(key, icon);
  }
}
