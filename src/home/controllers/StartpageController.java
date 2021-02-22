package home.controllers;

import home.model.Time;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StartpageController
{

    @FXML
    private Button getStartedButtonPressed;

    @FXML
    private RadioButton javaRadioButton;

    @FXML
    private RadioButton sqlRadioButton;

    @FXML
    private ToggleGroup courseToogleGroup;

    @FXML
    private ComboBox<String> noOfQuestionComboBox;

    @FXML
    private TextField totalTimeTextField;

    @FXML
    private Label courseLabel;

    @FXML
    private Label questionsLabel;

    @FXML
    private Label courseValidationLabel;

    @FXML
    private Label noOfQuestionValidationLabel;

    private String course;

    @FXML
    void courseRadioButton(ActionEvent event)
    {
        course = courseToogleGroup.getSelectedToggle().getUserData().toString();
        courseLabel.setText("Practice: " + course);
    }

    public void initialize()
    {
        //ADDS ITEMS TO THE NOOFQUESTIONS COMBO BOX
        noOfQuestionComboBox.getItems().addAll("5", "10");
        //LISTENER THAT CHECKS FOR WHICH OF THE ITEMS IN THE COMBOBOX WAS SELECTED
        noOfQuestionComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) ->
        {
            if (noOfQuestionComboBox.getValue().equals("5"))
            {
                //PASSES VALUES TO THE TIME GLOBAL VARIABLES
                Time.minute = 1;
                Time.second = 30;
                //SETS TEXT VALUE IN TEXTFIELD
                totalTimeTextField.setText("00:01:30");
                //SETS TEXT VALUE IN LABEL
                questionsLabel.setText("No of Questions: 5");
            } else
            {
                if (noOfQuestionComboBox.getValue().equals("10"))
                {
                    //PASSES VALUES TO THE TIME GLOBAL VARIABLES
                    Time.minute = 3;
                    Time.second = 0;
                    //SETS TEXT VALUE IN TEXTFIELD
                    totalTimeTextField.setText("00:03:00");
                    //SETS TEXT VALUE IN LABEL
                    questionsLabel.setText("No of Questions: 10");

                }
            }
        });
        javaRadioButton.setUserData("Java");
        sqlRadioButton.setUserData("SQL");
    }

    boolean validateFields()
    {
        boolean valid = true;

        // VALIDATE COURSE RADIO BUTTON
        if (courseToogleGroup.getSelectedToggle() == null)
        {
            courseValidationLabel.setText("Please, pick a course");
            valid = false;
        } else
        {
            courseValidationLabel.setText(null);
        }

        // VALIDATE NO OF QUESTIONS COMBO BOX
        if (noOfQuestionComboBox.getSelectionModel().getSelectedIndex() == -1)
        {
            noOfQuestionValidationLabel.setText("a valid number of question is required");
            valid = false;
        } else
        {
            noOfQuestionValidationLabel.setText(null);
        }
        return valid;
    }

    //LOADS UP A NEW SCENE IN THE SAME WINDOW
    @FXML
    void getStartedButtonPressed(ActionEvent event) throws IOException
    {
        if (validateFields())
        {
            Stage stage = (Stage) noOfQuestionComboBox.getScene().getWindow();
            BorderPane rootPane = (BorderPane) stage.getScene().getRoot();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/Pagination.fxml"));
            Parent root = loader.load();

            PaginationController controller = loader.getController();
            controller.initializeValues(Integer.parseInt(noOfQuestionComboBox.getValue()), course);
            rootPane.setCenter(root);
            stage.setTitle("1469 CBT System");
        }
    }
}
