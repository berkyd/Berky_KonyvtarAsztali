package com.example.berky_konyvtarasztali;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Optional;

public class KonyvtarController {
    public TableColumn<Konyv, String> author;
    @FXML
    private Button torlesButton;
    @FXML
    private TableColumn<Konyv, Integer> page_count;
    @FXML
    private TableColumn<Konyv, String> title;
    @FXML
    private TableView<Konyv> konyvek;
    @FXML
    private TableColumn<Konyv, Integer> publish_year;
    private KonyvtarDB db;
    @FXML

    @Deprecated
    private void initialize() {
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        publish_year.setCellValueFactory(new PropertyValueFactory<>("publish_year"));
        page_count.setCellValueFactory(new PropertyValueFactory<>("page_count"));
        try {
            db = new KonyvtarDB();
            konyvek.getItems().addAll(db.getKonyvek());
        } catch (SQLException e) {
            Platform.runLater(() -> {
                sqlAlert(e);
                Platform.exit();
            });
        }
    }

    private void sqlAlert(SQLException e) {
        alert(Alert.AlertType.ERROR,
                "Hiba történt az adatbázis kapcsolat kialakításakor",
                e.getMessage());
    }

    private Optional<ButtonType> alert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    @FXML
    public void torlesClick(ActionEvent actionEvent) {
        Konyv selected = getSelectedKonyv();
        if (selected == null){
            alert(Alert.AlertType.WARNING, "Törléshez előbb válasszon ki könyvet", "");
            return;
        }
        Konyv delete = konyvek.getSelectionModel().getSelectedItem();
        Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION,
                "Biztos szeretné törölni a kiválasztott könyvet?", "");
        if (optionalButtonType.isEmpty() ||
                (!optionalButtonType.get().equals(ButtonType.OK) &&
                        !optionalButtonType.get().equals(ButtonType.YES))) {
            return;
        }

        try {
            if (db.deleteKonyv(delete)) {
                alert(Alert.AlertType.WARNING, "Sikeres törlés", "");
            } else {
                alert(Alert.AlertType.WARNING, "Sikertelen törlés", "");
            }
            konyvek.setItems(FXCollections.observableList(db.getKonyvek()));
        } catch (SQLException e) {
            sqlAlert(e);
        }
    }

    private Konyv getSelectedKonyv() {
        int selectedIndex = konyvek.getSelectionModel().getSelectedIndex();
        return konyvek.getSelectionModel().getSelectedItem();
    }
}