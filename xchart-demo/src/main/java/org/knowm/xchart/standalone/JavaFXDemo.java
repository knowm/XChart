package org.knowm.xchart.standalone;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.SwingUtilities;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.demo.charts.area.AreaChart01;

/**
 * Class showing how to integrate a chart into a JavaFX Stage, embedded in a layout container
 * (VBox) alongside other controls so that everything resizes with the window.
 */
public class JavaFXDemo extends Application {

  public static void main(String[] args) {

    launch(args);
  }

  @Override
  public void start(Stage stage) {

    final SwingNode swingNode = new SwingNode();
    // Swing content must be created on the Swing Event Dispatch Thread
    SwingUtilities.invokeLater(
        () -> swingNode.setContent(new XChartPanel<>(new AreaChart01().getChart())));

    // A VBox gives each child its preferred height only; wrap the SwingNode in a resizable pane
    // and mark it to grow so the chart fills the space left over by the other controls.
    StackPane chartHolder = new StackPane(swingNode);
    VBox.setVgrow(chartHolder, Priority.ALWAYS);

    Button closeButton = new Button("Close");
    closeButton.setOnAction(event -> stage.close());
    ButtonBar buttonBar = new ButtonBar();
    buttonBar.getButtons().add(closeButton);

    VBox root = new VBox(10, chartHolder, buttonBar);
    root.setPadding(new Insets(10));

    stage.setScene(new Scene(root, 640, 480));
    stage.show();
  }
}
