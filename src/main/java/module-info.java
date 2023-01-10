module com.example.berky_konyvtarasztali {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.berky_konyvtarasztali to javafx.fxml;
    exports com.example.berky_konyvtarasztali;
}