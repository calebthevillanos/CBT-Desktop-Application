package home.controllers;

import home.model.User;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DashboardController
{

    @FXML
    private BorderPane parentPane;

    @FXML
    private Label nameLabel;

    @FXML
    private Label userNameLabel;

    private User user;

    private String name;

    private String userName;

    //OPENS A NEW SCENE IN THE SAME WINDOW
    @FXML
    void logOutLinkClicked(MouseEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Return Home");
        alert.setContentText(
                "Are you sure you want to Log out?");

        Optional<ButtonType> buttonSelected = alert.showAndWait();
        if (buttonSelected.get() == ButtonType.OK)
        {
            Stage stage = (Stage) nameLabel.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/Homepage.fxml"));

            Parent root = loader.load();

            stage.setScene(new Scene(root));
            stage.setTitle("1469 CBT System");
            stage.show();
        }
    }

    //METHOD THAT UPDATE VALUES PASSED FROM THE LAST SCENE
    public void initializeValues(User user)
    {
        this.user = user;
        name = user.getFirstName() + " " + user.getLastName();

        userName = user.getUserName();

        nameLabel.setText(name);

        userNameLabel.setText(userName);
    }

    //METHOD THAT LOADS UP AND SETS THE STARTPAGE ON TO THE DASHBOARD
    public void initialize() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/Startpage.fxml"));
        Parent root = loader.load();
        parentPane.setCenter(root);
    }
}
