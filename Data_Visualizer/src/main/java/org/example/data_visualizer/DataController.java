package org.example.data_visualizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import org.apache.http.client.methods.CloseableHttpResponse;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DataController {

    @FXML
    private TableColumn<DataModel, Long> list_id;

    @FXML
    private TableColumn<DataModel, String> list_appName;

    @FXML
    private TableColumn<DataModel, String> list_appDate; // Adjust to Date if needed

    @FXML
    private TableColumn<DataModel, String> list_totalTime;

    @FXML
    private TableView<DataModel> list_inTable;

    private ObservableList<DataModel> usageData = FXCollections.observableArrayList();

    private ObservableList<DailyDataModel> DailyUsageData = FXCollections.observableArrayList();

    @FXML
    private PieChart pieChartAnalysis;

    @FXML
    private BarChart<String,Number> dailyScreenTimeLineChart;

    @FXML
    private ComboBox<String> chartChoice;

    @FXML
    private BarChart<String,Number> barChartAnalysis;

    @FXML
    private LineChart<String,Number> lineChartAnalysis;






    private void updateDailyBarChart(ObservableList<DailyDataModel> DailyUsageData){


        if(DailyUsageData != null && !DailyUsageData.isEmpty()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd");
            XYChart.Series<String,Number> series = new XYChart.Series<>();
            series.setName("Daily Total Screen Time");

            for(DailyDataModel ddm : DailyUsageData){
                String formattedDate = dateFormat.format(ddm.getUsageDate());
                Number totalTime = ddm.getTotalTime();
                XYChart.Data<String,Number> data = new XYChart.Data<>(formattedDate,totalTime);
                series.getData().add(data);

            }
            Platform.runLater(() ->{
                dailyScreenTimeLineChart.getData().clear();
                dailyScreenTimeLineChart.getData().add(series);
                for (XYChart.Series<String, Number> s : dailyScreenTimeLineChart.getData()) {
                    for (XYChart.Data<String, Number> data : s.getData()) {
                        if (data.getNode() != null) {
                            long time = data.getYValue().longValue();
                            String value = ConvertSecondsToHMS(time);
                            Tooltip tooltip = new Tooltip(value);
                            System.out.println(data.getXValue());
                            Tooltip.install(data.getNode(), tooltip);
                        }
                    }
                }
            });
        }
    }

    private void updateLineChart(ObservableList<DataModel> usageData){
        System.out.println("Updating LineChart with usageData");

        if(usageData != null && !usageData.isEmpty()){
            XYChart.Series<String ,Number> series = new XYChart.Series<>();
            series.setName("APPS");

            for(DataModel dm : usageData){
                String xAxisData = dm.getAppName();
                long yAxisData = dm.getTotalTime();
                XYChart.Data<String,Number> data = new XYChart.Data<>(xAxisData,yAxisData);
                series.getData().add(data);
            }
            lineChartAnalysis.getData().clear();
            lineChartAnalysis.getData().add(series);
        }
        else{
            lineChartAnalysis.getData().clear();
        }
    }

    private void updateBarChart(ObservableList<DataModel> usageData){
        System.out.println("Updating BarChart with usageData");

        if(usageData != null && !usageData.isEmpty()){
            XYChart.Series<String,Number> series = new XYChart.Series<>();
            series.setName("APPS");
            for(DataModel dm: usageData){
                String xAxisData = dm.getAppName();
                long yAxisData = dm.getTotalTime();
                XYChart.Data<String,Number> data = new XYChart.Data<>(xAxisData,yAxisData);
                series.getData().add(data);
            }
            barChartAnalysis.getData().clear();
            barChartAnalysis.getData().add(series);
        }
        else{
            barChartAnalysis.getData().clear();
        }
    }

    private void updatePieChart(ObservableList<DataModel> usageData) {
        System.out.println("Updating PieChart with usageData size: " + (usageData != null ? usageData.size() : "null"));

        if (usageData != null && !usageData.isEmpty()) {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            for (DataModel dm : usageData) {
                if (dm.getTotalTime() > 0) {
                    pieChartData.add(new PieChart.Data(dm.getAppName(), dm.getTotalTime()));
                    System.out.println("Added to PieChart: " + dm.getAppName() + " - " + dm.getTotalTime());
                } else {
                    System.out.println("Skipped invalid data: " + dm.getAppName() + " - " + dm.getTotalTime());
                }
            }
            pieChartAnalysis.getData().setAll(pieChartData);
            System.out.println("PieChart data size set to: " + pieChartData.size());
        } else {
            pieChartAnalysis.getData().clear();
            System.out.println("PieChart cleared due to empty or null usageData");
        }
    }

    private void updateComboBox(){
        ObservableList<String> choices = FXCollections.observableArrayList(
                "Pie Chart","Line Chart","Bar Chart"
        );

        chartChoice.setItems(choices);
//        chartChoice.setValue("Pie Chart");

        chartChoice.setOnAction((event) -> {
            int selectedIndex = chartChoice.getSelectionModel().getSelectedIndex();

            System.out.println("value is "+selectedIndex);
            handleChoiceSelection(selectedIndex);
        });
    }

    private void handleChoiceSelection(int index){
        switch (index) {
            case 0:
                fetchDataAsync(0);
                break;
            case 1:
                fetchDataAsync(1);
                break;
            case 2:
                fetchDataAsync(2);
                break;
            default:
                System.out.println("Pie chart selected");
        }
    }

    @FXML
    private void initialize() {
        handleChoiceSelection(0);
        updateComboBox();


        // Fetch data on startup
//        fetchDataAsync();
        fetchDailyDataAsync();

        // Bind ObservableList to TableView
        System.out.println("the size before using in function : "+usageData);
        list_inTable.setItems(usageData);

        // Configure cell value factories for columns
        list_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        list_appName.setCellValueFactory(new PropertyValueFactory<>("appName"));
//        list_appDate.setCellValueFactory(new PropertyValueFactory<>("usageDate")); // Note: May need formatting
        list_appDate.setCellValueFactory(cellData -> {
            Date date = cellData.getValue().getUsageDate();
            return date != null ? new SimpleStringProperty(new SimpleDateFormat("EEE dd MMM yyyy").format(date)) : new SimpleStringProperty("N/A");
        });
//        list_totalTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        list_totalTime.setCellValueFactory(cellData ->{
            long totalTime = cellData.getValue().getTotalTime();
            String timeSpend = ConvertSecondsToHMS(totalTime);
            return new SimpleStringProperty(timeSpend);
        });

        dailyScreenTimeLineChart.getData().clear();
        //pie chart
//        updatePieChart(usageData);
//        updateDailyBarChart(DailyUsageData);

    }

    private String ConvertSecondsToHMS(long totalTime){
        long hours = totalTime/3600;
        long minutes = (totalTime%3600) / 60;
        long seconds = (totalTime%60);

        return String.format("%02dhr : %02dmin : %02dsec",hours,minutes,seconds);

    }

    private void fetchDailyDataAsync(){
        new Thread(() -> {
            String url = "http://localhost:8080/app/screen/daily";
            CloseableHttpClient httpClient = null;
            try {
                httpClient = HttpClients.createDefault();
                HttpGet request = new HttpGet(url);
                try(CloseableHttpResponse response = httpClient.execute(request)){
                    String json = EntityUtils.toString((response.getEntity()));
                    ObjectMapper mapper = new ObjectMapper();
                    List<DailyDataModel> dailyUsageList = mapper.readValue(json,mapper.getTypeFactory().constructCollectionType(List.class, DailyDataModel.class));
                    Platform.runLater(() ->{
                        DailyUsageData.setAll(dailyUsageList);
                        dailyScreenTimeLineChart.getData().clear();
                        updateDailyBarChart(DailyUsageData);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("can't reach the daily api end point : "+e.getMessage());
                Platform.runLater(() -> {
                    DailyUsageData.setAll();
                    dailyScreenTimeLineChart.getData().clear();
                    updateDailyBarChart(DailyUsageData);
                });
            } finally {
                if(httpClient != null){
                    try {
                        httpClient.close();
                    }catch (IOException e){
                        System.err.println("can't close the httpclient: "+e.getMessage());
                    }
                }
            }
        }).start();
    }

    private void fetchDataAsync(int idx) {
        new Thread(() -> {
            String url = "http://localhost:8080/app/screen/today"; // Verify this endpoint
            CloseableHttpClient httpClient = null;
            try {
                httpClient = HttpClients.createDefault();
                HttpGet request = new HttpGet(url);

                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    String json = EntityUtils.toString(response.getEntity());
                    System.out.println("API Response: " + json);
                    ObjectMapper mapper = new ObjectMapper();
                    List<DataModel> usageList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, DataModel.class));
                    for (DataModel db : usageList) {
                        System.out.println("Fetched: " + db.getAppName());
                    }
                    // Update UI on JavaFX thread
                    Platform.runLater(() -> {
                        usageData.setAll(usageList);
                        if(idx==0) {
                            barChartAnalysis.setVisible(false);
                            lineChartAnalysis.setVisible(false);
                            pieChartAnalysis.setVisible(true);
                            updatePieChart(usageData);
                        }
                        else if(idx==1){
                            barChartAnalysis.setVisible(false);
                            pieChartAnalysis.setVisible(false);
                            lineChartAnalysis.setVisible(true);
                            updateLineChart(usageData);
                        }
                        else if(idx==2) {
                            pieChartAnalysis.setVisible(false);
                            lineChartAnalysis.setVisible(false);
                            barChartAnalysis.setVisible(true);
                            updateBarChart(usageData);
                        }


                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to fetch data: " + e.getMessage());
                // Clear or show error on UI thread
                Platform.runLater(() -> {
                    usageData.setAll();
                });
            } finally {
                if (httpClient != null) {
                    try {
                        httpClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Error closing HttpClient: " + e.getMessage());
                    }
                }
            }
        }).start();
    }

    // Optional: Add refresh button action
    @FXML
    private void refreshData() {
        usageData.clear();// Clear current data
        pieChartAnalysis.getData().clear();
        DailyUsageData.clear();
        dailyScreenTimeLineChart.getData().clear();
        updateComboBox();
        fetchDailyDataAsync();
    }
}