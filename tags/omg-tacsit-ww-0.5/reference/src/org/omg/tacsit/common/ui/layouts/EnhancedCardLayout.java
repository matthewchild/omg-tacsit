/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 24, 2011
 */
package org.omg.tacsit.common.ui.layouts;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;

/**
 * A card layout that allows you to use Object constraints, instead of just strings.
 * @author Matthew Child
 */
public class EnhancedCardLayout extends CardLayout
{

  /**
   * Flips to the component that was added to this layout with the
   * specified <code>name</code>, using <code>addLayoutComponent</code>.
   * If no such component exists, then nothing happens.
   * @param parent the parent container in which to do the layout
   * @param constraint the component name
   * @see java.awt.CardLayout#addLayoutComponent(java.awt.Component, java.lang.Object)
   */
  public void show(Container parent, Object constraint)
  {
    show(parent, String.valueOf(constraint));
  }

  @Override
  public void addLayoutComponent(Component comp, Object constraints)
  {
    super.addLayoutComponent(comp, String.valueOf(constraints));
  }
}
