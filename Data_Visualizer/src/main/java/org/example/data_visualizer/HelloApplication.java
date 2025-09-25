package org.example.data_visualizer;

import com.almasb.fxgl.net.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Home_Screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        Scene scene = new Scene(LineChart(),400,400);
        scene.getStylesheets().add(String.valueOf(getClass().getResource("application.css")));
        stage.setTitle("Screen Time");

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private Region LineChart(){
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("APP");
        yAxis.setLabel("SECONDS");

        final LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setTitle("Screen Time");

        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("Screen Time of Each App");
        series.getData().add(new XYChart.Data<>("Brave",500));
        series.getData().add(new XYChart.Data<>("Chrome",600));
        series.getData().add(new XYChart.Data<>("Idea64",7000));

        lineChart.getData().add(series);
        VBox results =  new VBox(lineChart);
        results.setAlignment(Pos.CENTER);
        results.setFillWidth(false);
        results.setMaxSize(500,500);
        results.setMinSize(400,400);

        return results;
    }

    public static void main(String[] args) {
        launch();
    }
}