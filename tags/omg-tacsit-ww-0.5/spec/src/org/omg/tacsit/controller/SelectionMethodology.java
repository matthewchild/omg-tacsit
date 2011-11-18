/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Mar 28, 2011
 */

package org.omg.tacsit.controller;

/**
 * The methodology that should be used for selection in a TacSit controller.
 * <p>
 * For VIEWPORT_DEPENDENT, the selection is unique to each Viewport - which allows them to vary independently of one
 * another.  For VIEWPORT_INDEPENDENT, the selection is shared between all viewports; a change in one of them
 * affects all the others.
 * @author Matthew Child
 */
public enum SelectionMethodology
{
  VIEWPORT_DEPENDENT,
  VIEWPORT_INDEPENDENT;
}
