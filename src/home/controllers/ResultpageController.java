package home.controllers;

import home.model.CurrentUser;
import home.model.Time;
import home.model.User;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ResultpageController
{

    @FXML
    private Label totalScoreLabel;

    @FXML
    private Label totalTimeLabel;

    @FXML
    private Button correctionPageButton;

    @FXML
    private Label nameLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label courseLabel;

    @FXML
    private Label practiceDateLabel;

    @FXML
    private Hyperlink startPageHyperLink;

    public void initialize()
    {
        //SETS LABEL VALUES THROUGH CURRENTUSER GLOBAL VARIABLES
        nameLabel.setText(CurrentUser.firstName + " " + CurrentUser.lastName);
        userNameLabel.setText(CurrentUser.userName);

        //CONVERSION OF LONG VARIABLE TO STRING
        String minute = String.valueOf(Time.minutesSpent);
        String seconds = String.valueOf(Time.secondsSpent);

        //SETS LABEL VALUES THROUGH CURRENTUSER GLOBAL VARIABLES
        totalTimeLabel.setText(minute + "m" + " " + seconds + "s");
    }

    //OPENS A NEW SCENE IN SAME WINDOW
    @FXML
    void correctionPageButtonPressed(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) correctionPageButton.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/Correctionpage.fxml"));

        Parent root = loader.load();

        CorrectionpageController controller = loader.getController();
        controller.initializeValues(questionCount, course);

        stage.setScene(new Scene(root));
        stage.setTitle("1469 CBT System");
        stage.show();
    }
    private User user;

    private int questionCount;
    private String course;

    public void setParameters(int questionCount, String course, int correctAnswers)
    {
        this.questionCount = questionCount;
        this.course = course;

        courseLabel.setText(course);
        practiceDateLabel.setText(LocalDate.now().toString());
        totalScoreLabel.setText(Integer.toString(correctAnswers) + " / " + Integer.toString(questionCount));
    }

    User currentUser = new User(CurrentUser.firstName, CurrentUser.lastName, CurrentUser.userName);

    @FXML
    void startPageHyperLinkPressed(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) startPageHyperLink.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/Dashboard.fxml"));
        Parent root = loader.load();

        DashboardController controller = loader.getController();
        controller.initializeValues(currentUser);
        
        stage.setScene(new Scene(root));
        stage.show();
    }
}
