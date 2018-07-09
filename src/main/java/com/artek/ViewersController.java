package com.artek;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewersController implements Initializable {
    @FXML
    ListView listView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("INITIALIZED VIEWERS CONTROLLER");
        System.out.println(MainApp.twitchClient.getTMIEndpoint().getChatters(MainApp.channelName));
        List<String> adminsAndMods = new ArrayList<String>(
                MainApp.twitchClient.getTMIEndpoint().getChatters(MainApp.channelName).getAdmins());
                adminsAndMods.addAll(MainApp.twitchClient.getTMIEndpoint().getChatters(MainApp.channelName).getModerators());
        List<String> viewers = new ArrayList<String>(MainController.getViewers());

        ObservableList<String> viewersObs = FXCollections.observableList(viewers);
        ObservableList<String> adminModsObs = FXCollections.observableList(adminsAndMods);

        listView.setItems(adminModsObs);
        listView.setItems(viewersObs);

        listView.setCellFactory(l -> {

            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();


            MenuItem editItem = new MenuItem();

            editItem.textProperty().bind(Bindings.format("Edit \"%s\"", cell.itemProperty()));
            editItem.setOnAction(event -> {
                String item = cell.getItem();
                // code to edit item...
            });




            MenuItem deleteItem = new MenuItem();

            deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));

            deleteItem.setOnAction(event -> listView.getItems().remove(cell.getIndex()));


            contextMenu.getItems().addAll(editItem, deleteItem);

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell ;
        });
    }


}
