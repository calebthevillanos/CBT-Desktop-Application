package home.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import home.model.CurrentUser;
import home.model.User;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SignUpController
{

    @FXML
    private Button signUpButton;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private Label firstNameValidationLabel;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private Label lastNameValidationLabel;

    @FXML
    private TextField userNameTextField;

    @FXML
    private Label userNameValidationLabel;

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    @FXML
    private Label genderValidationLabel;

    @FXML
    private Hyperlink existingAccountHyperLink;

    @FXML
    private FontAwesomeIcon backArrowIcon;

    @FXML
    private ImageView quoteImage;

    //EVENT HANDLER FOR BACKARROWICON ON MOUSE CLICKED THAT OPENS A NEW STAGE
    @FXML
    void backArrowIconPressed(MouseEvent event) throws IOException
    {
        Stage stage = (Stage) backArrowIcon.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/Homepage.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    //EVENT HANDLER FOR EXISTINGACCOUNTHYPERLINK ON ACTION THAT OPENS A NEW STAGE
    @FXML
    void existingAccountHyperLinkPressed(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) existingAccountHyperLink.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/ExistingAccounts.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private User user = new User();
    private Alert alert = new Alert(Alert.AlertType.NONE);

    //EVENT HANDLER FOR SIGNUPBUTTON ON ACTION THAT CALLS THE SAVENEWACCOUNT EVENT  
    @FXML
    void signUpButtonPressed(ActionEvent event) throws IOException
    {
        //CREATES A NEW USER ACCOUNT
        saveNewAccount(event);
    }

    //AN EVENTHANDLER THAT CALLS THE VALIDATE FORM METHOD AND SAVES A NEW USER ACCOUNT IF "TRUE"
    void saveNewAccount(ActionEvent event) throws IOException
    {
        if (validateForm())
        {
            //IF VALIDATEFORM METHOD IS CALLED AND IT RETURNS TRUE
            //THE ADDUSER METHOD FROM THE USER CLASS IS CALLED

            //PASS THE VALUE RETURNED FROM THE ADDUSER METHOD TO A INT VARIABLE 
            int returned = user.addUser();

            switch (returned)
            {
                //IF RESULT VARIABLE EQUIVALENT TO "0"
                case User.UserStatus.DATABASE_ERROR:
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("Form Error");
                    alert.setContentText("A database error occurred. Please contact the application admin");
                    break; //EXITS SWITCH
                    
                //IF RESULT VARIABLE EQUIVALENT TO "-1"
                case User.UserStatus.USER_EXISTS:
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setTitle("Form Error");
                    alert.setContentText(String.format(
                            "%s %s %s is already a registered user",
                            user.getFirstName(), user.getLastName(), user.getUserName()));
                    break; //EXITS SWITCH
                default:
                    //PASS THE INPUTED VALUES FROM THE CONTROLS TO THE NEWUSER CONSTRUCTOR THAT CREATES A NEW USER IN THE DB 
                    User newUser = new User(firstNameTextField.getText(), lastNameTextField.getText(), userNameTextField.getText());
                    
                    //CALLS THE OPENSTARTPAGE METHOD 
                    openStartPage(newUser);
                    
                    //PASS VALUES TO THE GLOBAL VARIABLE(FIRSTNAME, LASTNAME, USERNAME) 
                    CurrentUser.firstName = newUser.getFirstName();
                    CurrentUser.lastName = newUser.getLastName();
                    CurrentUser.userName = newUser.getUserName();
                    return;
            }
            alert.showAndWait();
        }
    }
    
    //METHODS THAT VALIDATES ALL THE VALUES PASSED TO ALL TEXTFIELDS AND CHOICE BOX
    boolean validateForm()
    {
        boolean valid = true;

        // VALIDATE FIRST NAME
        if (firstNameTextField.getText().isEmpty())
        {
            firstNameValidationLabel.setText("First name is required");
            valid = false;
        } else
        {
            try
            {
                user.setFirstName(firstNameTextField.getText());
                firstNameValidationLabel.setText(null);
            } catch (IllegalArgumentException e)
            {
                firstNameValidationLabel.setText(e.getMessage());
                valid = false;
            }
        }

        // VALIDATE USER NAME
        if (userNameTextField.getText().isEmpty())
        {
            userNameValidationLabel.setText("Other name is required");
            valid = false;
        } else
        {
            try
            {
                user.setUserName(userNameTextField.getText());
                userNameValidationLabel.setText(null);
            } catch (IllegalArgumentException e)
            {
                userNameValidationLabel.setText(e.getMessage());
                valid = false;
            }
        }
        // VALIDATE LAST NAME
        if (lastNameTextField.getText().isEmpty())
        {
            lastNameValidationLabel.setText("Last name is required");
            valid = false;
        } else
        {
            try
            {
                user.setLastName(lastNameTextField.getText());
                lastNameValidationLabel.setText(null);
            } catch (IllegalArgumentException e)
            {
                lastNameValidationLabel.setText(e.getMessage());
                valid = false;
            }
        }

        //GENDER VALIDATION
        if (genderChoiceBox.getSelectionModel().getSelectedIndex() == -1)
        {
            genderValidationLabel.setText("Gender is required");
            valid = false;
        } else
        {
            user.setGender(genderChoiceBox.getSelectionModel().getSelectedItem());
            genderValidationLabel.setText(null);
        }
        return valid;
    }
    
    //OPENS A NEW SCENE WHEN THE APPROPRIATE PARAMETER IS MET 
    private void openStartPage(User user) throws IOException
    {
        Stage stage = (Stage) signUpButton.getScene().getWindow();

        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/Dashboard.fxml"));

        Parent root = loader.load();

        DashboardController controller = loader.getController();
        
        //PASSES VALUES TO THE INITIALIZEVALUES METHOD IN THE DASHBOARD CONTROLLER
        controller.initializeValues(user);

        stage.setScene(new Scene(root));
        stage.setTitle("1469 CBT System");
        stage.show();
    }

    public void initialize()
    {
        genderChoiceBox.getItems().addAll("Male", "Female");
    }
}
