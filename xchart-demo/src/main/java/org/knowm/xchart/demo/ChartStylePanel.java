package org.knowm.xchart.demo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Stroke;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.GGPlot2Theme;
import org.knowm.xchart.style.MatlabTheme;
import org.knowm.xchart.style.Theme;
import org.knowm.xchart.style.XChartTheme;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.BaseSeriesMarkers;
import org.knowm.xchart.style.markers.Marker;

public class ChartStylePanel extends JPanel {

  public static final class LabelValue {
    String label;
    Object value;

    public LabelValue(String label, Object value) {
      this.label = label;
      this.value = value;
    }

    @Override
    public String toString() {

      return label;
    }
  }

  public static final class EditableProperty {
    String name;
    Method readMethod;
    Method writeMethod;
    Object obj;
    PropertyEditor editor;
    TableCellEditor cellEditor;
    ChartStylePanel csp;
    Object additionalParameter;

    static HashMap<Class, TableCellEditor> editorMap;
    static Class[] assignableClasses = {
      Theme.class, BasicStroke.class, Marker.class, TimeZone.class
    };

    static {
      editorMap = new HashMap<Class, TableCellEditor>();
      {
        JComboBox comboBox = new JComboBox(new Boolean[] {Boolean.TRUE, Boolean.FALSE});
        TableCellEditor cellEditor = new DefaultCellEditor(comboBox);
        editorMap.put(Boolean.class, cellEditor);
        editorMap.put(Boolean.TYPE, cellEditor);
      }

      {
        Class[][] clsArr = {
          {int.class, Integer.class},
          {byte.class, Byte.class},
          {short.class, Short.class},
          {long.class, Long.class},
          {float.class, Float.class},
          {double.class, Double.class, Number.class},
          {String.class, String.class}
        };

        for (Class[] classes : clsArr) {
          GenericEditorWithClass editor = new GenericEditorWithClass(classes[1]);
          for (Class class1 : classes) {
            editorMap.put(class1, editor);
          }
        }
      }

      {
        JComboBox comboBox =
            new JComboBox(new Theme[] {new XChartTheme(), new GGPlot2Theme(), new MatlabTheme()});
        editorMap.put(Theme.class, new DefaultCellEditor(comboBox));
      }

      {
        LabelValue[] values = {
          new LabelValue("NONE", SeriesLines.NONE),
          new LabelValue("SOLID", SeriesLines.SOLID),
          new LabelValue("DOT_DOT", SeriesLines.DOT_DOT),
          new LabelValue("DASH_DASH", SeriesLines.DASH_DASH),
          new LabelValue("DASH_DOT", SeriesLines.DASH_DOT)
        };
        JComboBox comboBox = new JComboBox(values);
        editorMap.put(BasicStroke.class, new DefaultCellEditor(comboBox));
      }
      {
        LabelValue[] values = { //
          // styler.plotGridLinesStroke java.awt.Stroke
          new LabelValue(
              "Base Grid Line",
              new BasicStroke(
                  1.0f,
                  BasicStroke.CAP_BUTT,
                  BasicStroke.JOIN_BEVEL,
                  10.0f,
                  new float[] {3.0f, 5.0f},
                  0.0f)), //
          new LabelValue("GGPlot2 Grid Line", new BasicStroke(1.0f)), //
          new LabelValue(
              "Matlab Grid Line",
              new BasicStroke(
                  .5f,
                  BasicStroke.CAP_BUTT,
                  BasicStroke.JOIN_ROUND,
                  10.0f,
                  new float[] {1f, 3.0f},
                  0.0f)), //

          // styler.axisTickMarksStroke java.awt.Stroke
          new LabelValue("Base Tick Marks", new BasicStroke(1.0f)), //
          new LabelValue("GGPlot2 Tick Marks", new BasicStroke(1.5f)), //
          new LabelValue("Matlab Tick Marks", new BasicStroke(.5f)), //
        };
        JComboBox comboBox = new JComboBox(values);
        editorMap.put(Stroke.class, new DefaultCellEditor(comboBox));
      }

      {
        Marker[] seriesMarkers = new BaseSeriesMarkers().getSeriesMarkers();
        JComboBox comboBox = new JComboBox(seriesMarkers);
        editorMap.put(Marker.class, new DefaultCellEditor(comboBox));
      }

      {
        Locale[] values =
            new Locale[] {
              Locale.ENGLISH,
              Locale.US,
              Locale.UK,
              Locale.FRANCE,
              Locale.FRANCE,
              Locale.ITALIAN,
              Locale.GERMAN,
              new Locale("tr", "tr")
            };
        JComboBox comboBox = new JComboBox(values);
        editorMap.put(Locale.class, new DefaultCellEditor(comboBox));
      }

      {
        String[] availableIDs = TimeZone.getAvailableIDs();
        TimeZone[] values = new TimeZone[availableIDs.length];
        for (int i = 0; i < values.length; i++) {
          values[i] = TimeZone.getTimeZone(availableIDs[i]);
        }
        JComboBox comboBox = new JComboBox(values);
        editorMap.put(TimeZone.class, new DefaultCellEditor(comboBox));
      }
    }

    public EditableProperty(
        ChartStylePanel csp, String name, Object obj, Method readMethod, Method writeMethod) {
      this(csp, name, obj, readMethod, writeMethod, null);
    }

    public EditableProperty(
        ChartStylePanel csp,
        String name,
        Object obj,
        Method readMethod,
        Method writeMethod,
        Object additionalParameter) {

      this.csp = csp;
      this.readMethod = readMethod;
      this.writeMethod = writeMethod;
      this.name = name;
      this.obj = obj;
      this.additionalParameter = additionalParameter;

      initEditor();
    }

    private void initEditor() {

      try {
        Object val = getValue();
        Class cls = val == null ? getValueClass() : val.getClass();
        cellEditor = editorMap.get(cls);
        if (cellEditor != null) {
          return;
        }

        if (cls.isEnum()) {
          JComboBox comboBox = new JComboBox(cls.getEnumConstants());
          cellEditor = new DefaultCellEditor(comboBox);
          return;
        }

        for (Class class1 : assignableClasses) {
          if (class1.isAssignableFrom(cls)) {
            cellEditor = editorMap.get(class1);
            return;
          }
        }

        editor = java.beans.PropertyEditorManager.findEditor(cls);
        if (editor != null && editor.supportsCustomEditor()) {
          cellEditor = new PropertyEditorAdapter(editor, this);
          return;
        }

        System.out.println("Warning no editor found for property '" + name + "' with class " + cls);

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    @Override
    public String toString() {

      return name;
    }

    public void setValue(Object aValue) {

      if (aValue != null && aValue instanceof LabelValue) {
        aValue = ((LabelValue) aValue).value;
      }
      try {
        if (additionalParameter == null) {
          writeMethod.invoke(obj, aValue);
        } else {
          if (writeMethod == null) {
            Array.set(obj, (Integer) additionalParameter, aValue);
          } else {
            writeMethod.invoke(obj, additionalParameter, aValue);
          }
        }
        csp.repaintChart();
      } catch (Exception e) {
        Class<?>[] parameterTypes = writeMethod.getParameterTypes();
        if (aValue.getClass() != parameterTypes[0]) {
          System.out.println(
              name
                  + " "
                  + writeMethod.getName()
                  + " requires "
                  + parameterTypes[0]
                  + " but got "
                  + aValue
                  + " ("
                  + (aValue == null ? null : aValue.getClass())
                  + ")");
        }
        e.printStackTrace();
      }
    }

    public Object getValue() {

      try {
        if (additionalParameter == null) {
          return readMethod.invoke(obj);
        } else {
          if (readMethod == null) {
            return Array.get(obj, (Integer) additionalParameter);
          }
          return readMethod.invoke(obj, additionalParameter);
        }
      } catch (Exception e) {
        System.out.println("Error reading " + name);
        e.printStackTrace();
      }
      return null;
    }

    public TableCellEditor getTableCellEditor() {

      return cellEditor;
    }

    public Class getValueClass() {

      if (readMethod == null) {
        // obj is array
        return obj.getClass().getComponentType();
      }
      return readMethod.getReturnType();
    }
  }

  public static class PropertyEditorAdapter extends AbstractCellEditor implements TableCellEditor {

    PropertyEditor editor;
    EditableProperty se;

    public PropertyEditorAdapter(PropertyEditor editor, EditableProperty se) {

      this.editor = editor;
      this.se = se;
    }

    @Override
    public Object getCellEditorValue() {

      return editor.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(
        JTable table, Object value, boolean isSelected, int row, int column) {

      editor.setValue(value);
      return editor.getCustomEditor();
    }
  }

  static class GenericEditorWithClass extends DefaultCellEditor {

    Class[] argTypes = new Class[] {String.class};
    java.lang.reflect.Constructor constructor;
    Object value;

    public GenericEditorWithClass(Class cls) {
      super(new JTextField());
      getComponent().setName("Table.editor");
      try {
        constructor = cls.getConstructor(argTypes);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public boolean stopCellEditing() {

      String s = (String) super.getCellEditorValue();
      // Here we are dealing with the case where a user
      // has deleted the string value in a cell, possibly
      // after a failed validation. Return null, so that
      // they have the option to replace the value with
      // null or use escape to restore the original.
      // For Strings, return "" for backward compatibility.
      try {
        if ("".equals(s)) {
          if (constructor.getDeclaringClass() == String.class) {
            value = s;
          }
          return super.stopCellEditing();
        }

        value = constructor.newInstance(new Object[] {s});
      } catch (Exception e) {
        ((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
        return false;
      }
      return super.stopCellEditing();
    }

    public Component getTableCellEditorComponent(
        JTable table, Object value, boolean isSelected, int row, int column) {

      this.value = null;
      ((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
      return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    public Object getCellEditorValue() {

      return value;
    }
  }

  public static class EditorTableModel extends DefaultTableModel {
    ArrayList<EditableProperty> properties;
    Chart chart;
    int rowCount;
    ChartStylePanel csp;

    public EditorTableModel(ChartStylePanel csp, Chart chart) {
      this.csp = csp;
      addColumn("Name");
      addColumn("Type");
      addColumn("Value");
      changeChart(chart);
    }

    public void changeChart(Chart chart) {

      this.chart = chart;
      properties = getProperties(csp, chart);
      rowCount = properties.size();
      fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

      switch (columnIndex) {
        case 0:
          return String.class;
        case 1:
          return Class.class;

        default:
          return Object.class;
      }
    }

    public EditableProperty getValueAt(int row) {

      return properties.get(row);
    }

    @Override
    public Object getValueAt(int row, int column) {

      EditableProperty prop = properties.get(row);
      switch (column) {
        case 0:
          return prop.name;
        case 1:
          return prop.getValueClass().getName();
        case 2:
          return prop.getValue();
        default:
          return null;
      }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

      EditableProperty prop = properties.get(rowIndex);
      prop.setValue(aValue);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

      return columnIndex == 2;
    }

    @Override
    public int getRowCount() {

      return rowCount;
    }
  }

  public static class EditorTable extends JTable {
    EditorTableModel tableModel;

    public EditorTable(ChartStylePanel csp, Chart chart) {
      tableModel = new EditorTableModel(csp, chart);

      setModel(tableModel);
      setRowHeight(getRowHeight() * 2);
      setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

      TableColumnModel colmodel = getColumnModel();

      colmodel.getColumn(0).setPreferredWidth(200);
      colmodel.getColumn(1).setPreferredWidth(150);
      colmodel.getColumn(2).setPreferredWidth(400);

      setAutoCreateRowSorter(true);
    }

    public void changeChart(Chart chart) {

      tableModel.changeChart(chart);
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {

      int modelRowIndex = convertRowIndexToModel(row);
      EditableProperty se = tableModel.getValueAt(modelRowIndex);
      TableCellEditor editor = se.getTableCellEditor();
      if (editor != null) {
        return editor;
      }
      Class valueClass = se.getValueClass();
      TableCellEditor defaultEditor = getDefaultEditor(valueClass);

      // System.out.println(valueClass + "=>" + defaultEditor);
      return defaultEditor;
    }
  }

  private EditorTable table;
  private XChartPanel chartPanel;

  public ChartStylePanel(XChartPanel chartPanel) {
    this.chartPanel = chartPanel;
    table = new EditorTable(this, chartPanel.getChart());
    JScrollPane scrollpane = new JScrollPane(table);
    GridLayout layout = new GridLayout(1, 1);
    setLayout(layout);
    add(scrollpane);
    setPreferredSize(new Dimension(800, 600));
  }

  public void changeChart(XChartPanel chartPanel) {

    this.chartPanel = chartPanel;
    table.changeChart(chartPanel.getChart());
  }

  protected void repaintChart() {

    chartPanel.repaint();
  }

  static HashSet<String> skipSet =
      new HashSet<String>(
          Arrays.asList(
              "class",
              // chart
              "styler",
              "toolTips",
              "height",
              "width",
              "seriesMap",
              "YAxisGroupTitle",
              // series
              "XMax",
              "XMin",
              "YMax",
              "YMin",
              "extraValues",
              "XData",
              "YData",
              "xAxisDataType",
              "yAxisDataType",
              "legendRenderType",
              "name",
              "YAxisAlignment",
              "YAxisGroupPosition"));

  public static ArrayList<EditableProperty> getProperties(ChartStylePanel csp, Chart chart) {

    if (chart == null) {
      return new ArrayList<EditableProperty>();
    }
    ArrayList<EditableProperty> list = getObjectProperties(csp, chart, "chart.", skipSet);
    ArrayList<EditableProperty> list2 =
        getObjectProperties(csp, chart.getStyler(), "styler.", skipSet);
    list.addAll(list2);

    Map<String, Series> seriesMap = chart.getSeriesMap();
    int ind = 0;
    TreeSet<Integer> seriesIndSet = new TreeSet<Integer>();
    for (Entry<String, Series> e : seriesMap.entrySet()) {
      Series series = e.getValue();
      list2 = getObjectProperties(csp, series, "series[" + e.getKey() + "].", skipSet);
      list.addAll(list2);
      seriesIndSet.add(series.getYAxisGroup());
      seriesIndSet.add(ind);
      ind++;
    }

    try {
      MethodDescriptor[] methodDescriptors =
          Introspector.getBeanInfo(chart.getClass()).getMethodDescriptors();
      HashMap<String, Method> chartMethodMap = new HashMap<String, Method>();
      for (MethodDescriptor methodDescriptor : methodDescriptors) {
        chartMethodMap.put(
            methodDescriptor.getName().toLowerCase(Locale.ENGLISH), methodDescriptor.getMethod());
      }

      methodDescriptors =
          Introspector.getBeanInfo(chart.getStyler().getClass()).getMethodDescriptors();
      HashMap<String, Method> stylerMethodMap = new HashMap<String, Method>();
      for (MethodDescriptor methodDescriptor : methodDescriptors) {
        stylerMethodMap.put(
            methodDescriptor.getName().toLowerCase(Locale.ENGLISH), methodDescriptor.getMethod());
      }

      // Skipping property (no read method): styler.YAxisAlignment null
      // chart.getStyler().getYAxisGroupPosistion(yAxisGroup)
      // chart.getStyler().setYAxisGroupPosition(yAxisGroup)

      for (Integer i : seriesIndSet) {
        EditableProperty styleEditor =
            new EditableProperty(
                csp,
                "chart.YAxisGroupTitle[" + i + "]",
                chart,
                chartMethodMap.get("getyaxisgrouptitle"),
                chartMethodMap.get("setyaxisgrouptitle"),
                i);
        list.add(styleEditor);
        Method readMethod = stylerMethodMap.get("getyaxisgroupposition");
        styleEditor =
            new EditableProperty(
                csp,
                "chart.YAxisGroupPosition[" + i + "]",
                chart.getStyler(),
                readMethod,
                stylerMethodMap.get("setyaxisgroupposition"),
                i);
        list.add(styleEditor);
      }
    } catch (IntrospectionException e1) {
      e1.printStackTrace();
    }

    return list;
  }

  public static Method getMethod(
      PropertyDescriptor pd, boolean read, HashMap<String, Method> methodMap) {

    Method method = read ? pd.getReadMethod() : pd.getWriteMethod();

    if (method != null) {
      return method;
    }

    String lowerCaseName = pd.getName().toLowerCase(Locale.ENGLISH);
    String[] prefixes = read ? new String[] {"get", "is"} : new String[] {"set"};

    for (String pre : prefixes) {
      String name = pre + lowerCaseName;
      method = methodMap.get(name);
      if (method != null) {
        if (method.getParameterCount() != (read ? 0 : 1)) {
          method = null;
          continue;
        }
        return method;
      }
    }
    return method;
  }

  public static ArrayList<EditableProperty> getObjectProperties(
      ChartStylePanel csp, Object obj, String prefix, Set<String> skipSet) {

    ArrayList<EditableProperty> list = new ArrayList<EditableProperty>();
    try {
      BeanInfo info = Introspector.getBeanInfo(obj.getClass());
      PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();

      Arrays.sort(
          propertyDescriptors,
          new Comparator<PropertyDescriptor>() {
            public int compare(PropertyDescriptor a, PropertyDescriptor b) {

              return a.getName().compareToIgnoreCase(b.getName());
            }
          });

      MethodDescriptor[] methodDescriptors = info.getMethodDescriptors();
      HashMap<String, Method> methodMap = new HashMap<String, Method>();
      for (MethodDescriptor methodDescriptor : methodDescriptors) {
        methodMap.put(
            methodDescriptor.getName().toLowerCase(Locale.ENGLISH), methodDescriptor.getMethod());
      }
      for (PropertyDescriptor pd : propertyDescriptors) {
        try {
          if (skipSet.contains(pd.getName())) {
            continue;
          }

          Method readMethod = getMethod(pd, true, methodMap);

          if (readMethod == null) {

            // System.out.println("Skipping property (no read method): " +
            // pd.getName() + " " + pd.getPropertyType());
            System.out.println(
                "Skipping property (no read method): "
                    + prefix
                    + pd.getName()
                    + " "
                    + pd.getPropertyType());
            continue;
          }
          Method writeMethod = getMethod(pd, false, methodMap);
          if (writeMethod == null) {

            System.out.println(
                "Skipping property (no write method): "
                    + prefix
                    + pd.getName()
                    + " "
                    + pd.getPropertyType());
            // System.out.println("Skipping property (no write method): " +
            // pd.getName() + " " + pd.getPropertyType());
            continue;
          }

          if (pd.getReadMethod().getReturnType().isArray()) {
            Object arr = readMethod.invoke(obj);
            int size = Array.getLength(arr);
            Method rm = null;
            Method wm = null;
            for (int i = 0; i < size; i++) {

              EditableProperty styleEditor =
                  new EditableProperty(csp, prefix + pd.getName() + "[" + i + "]", arr, rm, wm, i);
              list.add(styleEditor);
            }
            continue;
          }

          EditableProperty styleEditor =
              new EditableProperty(csp, prefix + pd.getName(), obj, readMethod, writeMethod);
          list.add(styleEditor);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return list;
  }
}
