module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.demo to javafx.fxml, java.base;
    opens entity to javafx.base;
    exports com.example.demo;
}

