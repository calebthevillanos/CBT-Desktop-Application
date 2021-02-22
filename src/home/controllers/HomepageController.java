package home.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class HomepageController
{

    //DECLARED CONTROLS
    @FXML
    private Button newAccountButton;

    @FXML
    private Button existingAccountButton;

    @FXML
    private ImageView quoteImage;

    @FXML
    private Button  quitutton;

    //EVENT HANDLER FOR EXISTINGACCOUNTBUTTON ON ACTION THAT OPENS A NEW SCENE ON THE SAME WINDOW
    @FXML
    void existingAccountButtonPressed(ActionEvent event) throws IOException
    {

        Stage stage = (Stage) existingAccountButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/ExistingAccounts.fxml"));
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("1469 CBT System");

        stage.show();
    }

    //EVENT HANDLER FOR EXISTINGACCOUNTBUTTON ON ACTION THAT OPENS A NEW SCENE ON THE SAME WINDOW
    @FXML
    void newAccountButtonPressed(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) newAccountButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/SignUp.fxml"));
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("1469 CBT System");

        stage.show();
    }

    //CLOSE PROGRAM 
    @FXML
    void quitButtonPressed(ActionEvent event)
    {
        System.exit(0);
    }
}
