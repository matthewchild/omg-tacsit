/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Jun 21, 2011
 */
package org.omg.tacsit.common.ui.panel;

import org.omg.tacsit.common.ui.layouts.EnhancedCardLayout;
import java.awt.BorderLayout;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Displays an appropriate PropertyEditor for varying types of value Objects.
 * <p>
 * This class integrates with Java's PropertyEditor functionality.  PropertyEditor are looked up via the core Java
 * class PropertyEditorManager.
 * <p>
 * Some additional enhancements to the core lookup functionality have been added to simplify usage.  
 * PropertyEditorPanel will not just look up a PropertyEditor based on the Object class, but also by parent classes 
 * and implemented interfaces.  This allows you the option of working at a higher level of abstraction than 
 * PropertyEditorManager usually allows.
 * 
 * @author Matthew Child
 */
public class PropertyEditorPanel extends JPanel
{
  private static final String NULL_VALUE_COMPONENT = "";
  
  private Object value;
  private EnhancedCardLayout cardLayout;
  private Map<Class, PropertyEditor> classToEditor;
  private JComponent nullValueComponent;
  private Object currentConstraint;

  /**
   * Creates a new instance.
   */
  public PropertyEditorPanel()
  {
    cardLayout = new EnhancedCardLayout();
    classToEditor = new HashMap();
    nullValueComponent = createDefaultNullValueComponent();
    setLayout(cardLayout);
    add(nullValueComponent, NULL_VALUE_COMPONENT);
    currentConstraint = NULL_VALUE_COMPONENT;
  }

  /**
   * The component that's displayed when the value is null.
   * @return The component that's displayed when the value is null.
   */
  public JComponent getNullValueComponent()
  {
    return nullValueComponent;
  }

  /**
   * Sets the component that's displayed when the value is null.
   * @param nullValueComponent The component to display when the value is null.
   */
  public void setNullValueComponent(JComponent nullValueComponent)
  {
    if(this.nullValueComponent != null)
    {
      remove(nullValueComponent);
    }
    if(nullValueComponent == null)
    {
      this.nullValueComponent = createDefaultNullValueComponent();
    }
    else
    {
      this.nullValueComponent = nullValueComponent;
    }
    add(this.nullValueComponent, NULL_VALUE_COMPONENT);
    if(currentConstraint.equals(NULL_VALUE_COMPONENT))
    {
      cardLayout.show(this, NULL_VALUE_COMPONENT);
    }
  }
  
  private JComponent createDefaultNullValueComponent()
  {
    JPanel defaultComponent = new JPanel(new BorderLayout());
    
    JLabel textLabel = new JLabel("No Data Selected", JLabel.CENTER);
    defaultComponent.add(textLabel, BorderLayout.CENTER);
    
    return defaultComponent;
  }

  /**
   * The value who's properties are currently being edited.
   * @return The value being edited.
   */
  public Object getValue()
  {
    return value;
  }

  private PropertyEditor getEditor(Class clazz)
  {
    return PropertyEditorManager.findEditor(clazz);
  }
  
  private PropertyEditor getInterfaceEditor(Class clazz)
  {
    PropertyEditor editor = null;
    Class[] interfaces = clazz.getInterfaces();
    for (Class clazzInterface : interfaces)
    {
      editor = getEditor(clazzInterface);
      if (editor != null)
      {
        break;
      }
    }

    return editor;
  }

  private PropertyEditor getEditorForClass(Class clazz)
  {
    PropertyEditor editor = null;
    if(clazz != null)
    {
      editor = getEditor(clazz);
      
      if(editor == null)
      {
        editor = getInterfaceEditor(clazz);
      }
      
      if(editor == null)
      {
        Class parentClass = clazz.getSuperclass();
        editor = getEditorForClass(parentClass);
      }
    }
    return editor;
  }

  private PropertyEditor getOrDefineEditor(Class valueClass)
  {
    PropertyEditor editor;
    if (classToEditor.containsKey(valueClass))
    {
      editor = classToEditor.get(valueClass);
    }
    else
    {
      editor = getEditorForClass(valueClass);
      add(editor.getCustomEditor(), valueClass);
      classToEditor.put(valueClass, editor);
    }
    return editor;
  }
  
  private void showEditorFor(Object value)
  {
    Object constraint;
    if(value != null)
    {
      Class valueClass = value.getClass();
      PropertyEditor editor = getOrDefineEditor(valueClass);

      if (editor != null)
      {
        editor.setValue(value);
      }
      constraint = valueClass;
    }
    else
    {
      constraint = NULL_VALUE_COMPONENT;
    }
    currentConstraint = constraint;
    cardLayout.show(this, currentConstraint);
  }

  /**
   * Sets the value currently being edited.  The panel will be updated to display an appropriate editor
   * for the value.
   * @param value The value to edit.
   */
  public void setValue(Object value)
  {
    this.value = value;
    showEditorFor(this.value);
  }
}
