package org.knowm.xchart.standalone;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.swing.JPanel;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.demo.charts.area.AreaChart01;

/**
 * Class showing how to integrate a chart into a JavaFX Stage
 *
 * @author timmolter
 */
public class JavaFXDemo extends Application {

  public static void main(String[] args) {

    launch(args);
  }

  @Override
  public void start(Stage stage) {

    final SwingNode swingNode = new SwingNode();
    JPanel chartPanel = new XChartPanel(new AreaChart01().getChart());
    swingNode.setContent(chartPanel);
    Scene scene = new Scene(new StackPane(swingNode), 640, 480);
    stage.setScene(scene);
    stage.show();
  }
}
