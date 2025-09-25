ScreenTimeTracker:

ScreenTimeTracker is a JavaFX application that monitors and visualizes the time spent on different applications on your computer. It uses JNA to track the active window, logs usage data to an SQLite database, and displays the data in a table and interactive charts (Pie Chart and Bar Chart). The application supports light and dark themes, styled with BootstrapFX, and allows users to toggle between chart types and themes via ComboBoxes.
Features

Real-time Tracking: Monitors the active window using JNA (Windows-specific) and logs time spent per application.
Data Storage: Stores usage data (app name, time spent, date/time) in an SQLite database (screentime.db).
Data Visualization:
Displays raw data in a JavaFX TableView.
Visualizes total time per app in a PieChart or BarChart, toggled via a ComboBox.
Charts are overlaid in a StackPane and updated every 5 seconds.


Theming: Supports light and dark themes using BootstrapFX, with theme selection via a ComboBox.
Modular Design: Built with Java 20 and JavaFX 20.0.1, using the Java Module System for encapsulation.
Cross-dependency Support: Integrates JNA, SQLite-JDBC, and BootstrapFX for functionality and styling.



Java Development Kit (JDK): Version 20 or higher.
Maven: For dependency management and building the project.
Operating System: Windows (for JNA-based window tracking; macOS/Linux support requires modifications).
IDE (Optional): IntelliJ IDEA, Eclipse, or any IDE with JavaFX support.

Installation

Clone the Repository:
git clone https://github.com/sSriBalaji/ScreenTimeTracker.git
cd ScreenTimeTracker


Install Dependencies:Ensure Maven is installed, then build the project:
mvn clean install

This downloads all dependencies specified in pom.xml, including JavaFX, JNA, SQLite-JDBC, and BootstrapFX.

Project Structure:
ScreenTimeTracker/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── org/example/javafx/
│   │   │   │   ├── ScreenTimeTracker.java
│   │   │   │   ├── ScreenTimeData.java
│   │   │   │   ├── ActiveWindowTracker.java
│   │   │   │   ├── ScreenTimeLogger.java
│   │   │   ├── module-info.java
│   │   ├── resources/
│   │       ├── styles.css (optional for custom CSS)
├── screentime.db (generated at runtime)
├── pom.xml
├── README.md
├── screenshots/


Verify Dependencies:Ensure pom.xml includes:
<dependencies>
    <dependency>
        <groupId>net.java.dev.jna</groupId>
        <artifactId>jna</artifactId>
        <version>5.14.0</version>
    </dependency>
    <dependency>
        <groupId>net.java.dev.jna</groupId>
        <artifactId>jna-platform</artifactId>
        <version>5.14.0</version>
    </dependency>
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.50.3.0</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>20.0.1</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>20.0.1</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-web</artifactId>
        <version>20.0.1</version>
    </dependency>
    <dependency>
        <groupId>org.controlsfx</groupId>
        <artifactId>controlsfx</artifactId>
        <version>11.2.0</version>
    </dependency>
    <dependency>
        <groupId>com.dlsc.formsfx</groupId>
        <artifactId>formsfx</artifactId>
        <version>11.6.0</version>
    </dependency>
    <dependency>
        <groupId>net.synedra</groupId>
        <artifactId>validatorfx</artifactId>
        <version>0.4.0</version>
    </dependency>
    <dependency>
        <groupId>org.kordamp.ikonli</groupId>
        <artifactId>ikonli-javafx</artifactId>
        <version>12.3.1</version>
    </dependency>
    <dependency>
        <groupId>org.kordamp.bootstrapfx</groupId>
        <artifactId>bootstrapfx-core</artifactId>
        <version>0.4.0</version>
    </dependency>
    <dependency>
        <groupId>com.almasb</groupId>
        <artifactId>fxgl</artifactId>
        <version>17.3</version>
    </dependency>
</dependencies>


Module Configuration:Ensure module-info.java is set up:
module org.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires java.sql;

    opens org.example.javafx to javafx.fxml, javafx.base;
    exports org.example.javafx;
}



Usage

Run the Application:

Using Maven:mvn javafx:run


Using an IDE:
Open the project in IntelliJ IDEA, Eclipse, or another IDE.
Run ScreenTimeTracker.java as a Java application.




Interact with the Application:

The application starts tracking active windows immediately (Windows only).
View raw data in the TableView (app name, time spent, date/time).
Use the Chart ComboBox to switch between Pie Chart and Bar Chart visualizations of total time per app.
Use the Theme ComboBox to toggle between light and dark themes (styled with BootstrapFX).
Data updates every 5 seconds, and logs are stored in screentime.db.


View Database:

Open screentime.db with an SQLite client (e.g., DB Browser for SQLite).
Query data:SELECT app_name, SUM(time_spent) as total_time FROM screentime GROUP BY app_name;





Features in Detail

Active Window Tracking: Uses JNA (com.sun.jna.platform) to monitor the active window title on Windows and logs time spent per application.
SQLite Integration: Stores data in screentime.db with columns id, app_name, time_spent, and date_time.
Data Visualization:
TableView: Displays raw data with columns for app name, time spent (seconds), and timestamp.
PieChart: Shows total time per app as slices.
BarChart: Shows total time per app as bars.
Charts are overlaid in a StackPane and toggled via a ComboBox.


Theming: Supports light and dark themes using BootstrapFX, with dynamic switching via a ComboBox.
Automatic Refresh: Updates table and charts every 5 seconds to reflect new data.

Limitations

Platform: Currently Windows-only due to JNA’s User32 usage. macOS/Linux support requires platform-specific JNA code.
Dependencies: Requires Java 20 and JavaFX 20.0.1, which may need specific JDK configuration.
Chart Styling: BootstrapFX has limited support for chart styling; custom CSS can enhance chart appearance.

Contributing
Contributions are welcome! To contribute:

Fork the repository.
Create a new branch (git checkout -b feature/your-feature).
Make changes and commit (git commit -m "Add your feature").
Push to your branch (git push origin feature/your-feature).
Open a Pull Request.

Please ensure code follows the project structure and includes tests where applicable.
Future Improvements

Add macOS/Linux support for window tracking.
Implement a LineChart for time-based trends.
Add date filtering for data visualization.
Enhance chart styling with custom CSS.
Save theme preferences to a configuration file.


Contact
For questions or feedback, open an issue on GitHub or contact sribalaji.work07@email.com.
