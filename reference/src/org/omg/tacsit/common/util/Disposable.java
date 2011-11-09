/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 24, 2011
 */
package org.omg.tacsit.common.util;

/**
 * An object which needs to be cleaned up when it is no longer used.
 * @author Matthew Child
 */
public interface Disposable
{
  /**
   * Notifies the object that it will no longer be used.  Any open resources should be closed.
   */
  public void dispose();
}
