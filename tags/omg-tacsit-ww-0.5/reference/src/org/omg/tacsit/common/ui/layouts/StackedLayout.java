/**
 * Organization:  SimVentions, Inc.
 * Creation Date: May 29, 2007
 */

package org.omg.tacsit.common.ui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * A layout manager that stacks components vertically.  The vertical size a component takes up is based on its
 * preferred size.
 * <p>
 * This layout differs from BoxLayout in that it allows components to have a consistent vertical spacing.
 * <p>
 * This layout differs from GridLayout in two ways:<br>
 * <li>It does not resize the height of the components.</li>
 * <li>The layout may not take up all of the available vertical or horizontal space allocated to it.
 * 
 * @author Matthew Child
 */
public class StackedLayout implements LayoutManager
{
  private int vgap;
  
  /**
   * Creates a new instance.
   */
  public StackedLayout()
  {
    this(5);
  }
  
  /**
   * Creates a new instance.
   * @param vgap The vertical gap between components in the layout.
   */
  public StackedLayout(int vgap)
  {
    this.vgap = vgap;
  }
  
  /**
   * Sets the vertical gap between components in the layout.
   * @param vgap The vertical gap between components in the layout.
   */
  public void setVgap(int vgap)
  {
    this.vgap = vgap;
  }
  
  /**
   * Gets the vertical gap between components in the layout.
   * @return The vertical gap between components in the layout.
   */
  public int getVgap()
  {
    return vgap;
  }

  @Override
  public void addLayoutComponent(String name, Component comp)
  {
    // We don't need any constraints for a component, so we can ignore them.
  }

  @Override
  public void removeLayoutComponent(Component comp)
  {
    // Since no constraints are needed, we don't have to worry about maintaining a list of layout components.
  }

  @Override
  public Dimension preferredLayoutSize(Container parent)
  {
    return minimumLayoutSize(parent);
  }

  @Override
  public Dimension minimumLayoutSize(Container parent)
  {
    Dimension minSize = new Dimension(0, 0);
    
    boolean first = true;
    for(Component comp: parent.getComponents())
    {
      Dimension prefSize = comp.getPreferredSize();
      if(prefSize.width > minSize.width)
      {
        minSize.width = prefSize.width;
      }
      minSize.height += prefSize.height;
      if(first)
      {
        first = false;
      }
      else
      {
        minSize.height += vgap;
      }
    }
    Insets insets = parent.getInsets();
    minSize.height += insets.top + insets.bottom;
    minSize.width += insets.left + insets.right;
    return minSize;
  }

  @Override
  public void layoutContainer(Container parent)
  {
    Dimension size = minimumLayoutSize(parent);
    
    Insets insets = parent.getInsets();
    int currHeight = insets.top;
    int insetWidth = insets.left + insets.right;
    boolean first = true;
    for(Component comp: parent.getComponents())
    {
      Dimension compSize = comp.getPreferredSize();
      if(first)
      {
        first = false;
      }
      else
      {
        currHeight += vgap;
      }
      comp.setBounds(insets.left, currHeight, size.width - insetWidth, compSize.height);
      currHeight += compSize.height;
    }
  }
}
