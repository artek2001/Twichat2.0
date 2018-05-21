package com.artek;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import me.philippheuer.util.desktop.WebsiteUtils;
import org.springframework.web.util.WebUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    @FXML
    Hyperlink aboutLink;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void onAboutClicked(MouseEvent event) {
        WebsiteUtils.openWebpage(aboutLink.getText());
    }
}
