/**
 * Organization:  SimVentions, Inc.
 * Creation Date: Aug 27, 2011
 */
package org.omg.tacsit.ui;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.omg.tacsit.common.math.Angle;
import org.omg.tacsit.common.text.DegreeMinuteSecondFormat;
import org.omg.tacsit.common.ui.table.FormattedTextCellEditor;
import org.omg.tacsit.common.ui.table.FormattedTextCellRenderer;
import org.omg.tacsit.common.util.Factory;

/**
 * Provides default values for Tables used to edit Tacsit data.
 * @author Matthew Child
 */
public class TableDefaults
{
  private static EditorDefaultsFactory editorDefaultsFactory = new EditorDefaultsFactory();
  private static RendererDefaultsFactory rendererDefaultsFactory = new RendererDefaultsFactory();
  
  /**
   * Initializes the editors and renderers for a table for display of a certain Class of data.
   * @param table The table to initialize
   * @param dataClass The class of data to use the default editor and renderers for.
   */
  public static void initializeEditorAndRendererForTable(JTable table, Class dataClass)
  {
     TableCellEditor newEditor = newEditor(dataClass);
     if(newEditor != null)
     {
       table.setDefaultEditor(dataClass, newEditor);
     }
     
     TableCellRenderer newRenderer = newRenderer(dataClass);
     if(newRenderer != null)
     {
       table.setDefaultRenderer(dataClass, newRenderer);
     }
  }
  
  private static TableCellEditor newEditor(Class dataClass)
  {
    return editorDefaultsFactory.newEditor(dataClass);
  }
  
  private static TableCellRenderer newRenderer(Class dataClass)
  {
    return rendererDefaultsFactory.newRenderer(dataClass);
  }
  
  private static class EditorDefaultsFactory
  {
    private Map<Class, Factory<TableCellEditor>> classToEditorFactory;

    public EditorDefaultsFactory()
    {
      classToEditorFactory = createEditorMap();
    }    
  
    private Map<Class, Factory<TableCellEditor>> createEditorMap()
    {
      Map<Class, Factory<TableCellEditor>> editorMap = new HashMap();
      editorMap.put(Angle.class, new AngleEditorFactory());
      return editorMap;
    }
    
    public TableCellEditor newEditor(Class dataClass)
    {
      Factory<TableCellEditor> editorFactory = classToEditorFactory.get(dataClass);
      if(editorFactory != null)
      {
        return editorFactory.createObject();
      }
      else
      {
        return null;
      }
    }
  }
  
  private static class AngleEditorFactory implements Factory<TableCellEditor>
  {
    public TableCellEditor createObject()
    {
      FormattedTextCellEditor angleEditor = new FormattedTextCellEditor(new DegreeMinuteSecondFormat());
      angleEditor.setHorizontalAlignment(SwingConstants.RIGHT);
      return angleEditor;
    }
  }
  
  private static class RendererDefaultsFactory
  {
    private Map<Class, Factory<TableCellRenderer>> classToRendererFactory;

    public RendererDefaultsFactory()
    {
      classToRendererFactory = createRendererMap();
    }
    
    private Map<Class, Factory<TableCellRenderer>> createRendererMap()
    {
      Map<Class, Factory<TableCellRenderer>> rendererMap = new HashMap();
      rendererMap.put(Angle.class, new AngleRendererFactory());
      return rendererMap;
    }
    
    public TableCellRenderer newRenderer(Class dataClass)
    {
      Factory<TableCellRenderer> rendererFactory = classToRendererFactory.get(dataClass);
      if(rendererFactory != null)
      {
        return rendererFactory.createObject();
      }
      else
      {
        return null;
      }
    }
  }
  
  private static class AngleRendererFactory implements Factory<TableCellRenderer>
  {

    public TableCellRenderer createObject()
    {
      FormattedTextCellRenderer angleRenderer = new FormattedTextCellRenderer(new DegreeMinuteSecondFormat());
      angleRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
      return angleRenderer;
    }
  }
}
