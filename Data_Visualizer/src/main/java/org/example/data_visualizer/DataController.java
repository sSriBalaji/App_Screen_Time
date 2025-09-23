package org.example.data_visualizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class DataController {

    @FXML
    private TableColumn<DataModel, Long> list_id;

    @FXML
    private TableColumn<DataModel, String> list_appName;

    @FXML
    private TableColumn<DataModel, String> list_appDate; // Adjust to Date if needed

    @FXML
    private TableColumn<DataModel, Long> list_totalTime;

    @FXML
    private TableView<DataModel> list_inTable;

    private ObservableList<DataModel> usageData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Bind ObservableList to TableView
        list_inTable.setItems(usageData);

        // Configure cell value factories for columns
        list_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        list_appName.setCellValueFactory(new PropertyValueFactory<>("appName"));
//        list_appDate.setCellValueFactory(new PropertyValueFactory<>("usageDate")); // Note: May need formatting
        list_appDate.setCellValueFactory(cellData -> {
            Date date = cellData.getValue().getUsageDate();
            return date != null ? new SimpleStringProperty(new SimpleDateFormat("EEE dd MMM yyyy").format(date)) : new SimpleStringProperty("N/A");
        });
        list_totalTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));

        // Fetch data on startup
        fetchDataAsync();
    }

    private void fetchDataAsync() {
        new Thread(() -> {
            String url = "http://localhost:8080/app/screen/all"; // Verify this endpoint
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
                    Platform.runLater(() -> usageData.setAll(usageList));
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to fetch data: " + e.getMessage());
                // Clear or show error on UI thread
                Platform.runLater(() -> usageData.setAll());
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
        usageData.clear(); // Clear current data
        fetchDataAsync();
    }
}