package home.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import home.model.CurrentUser;
import home.model.User;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ExistingAccountsController
{

    @FXML
    private TableView<User> usersTableView;

    @FXML
    private TableColumn<User, String> firstNameTableColumn;

    @FXML
    private TableColumn<User, String> lastNameTableColumn;

    @FXML
    private TableColumn<User, String> userNameTableColumn;

    @FXML
    private TableColumn<User, String> genderTableColumn;

    @FXML
    private TableColumn<User, String> dateCreatedTableColumn;

    @FXML
    private FontAwesomeIcon backArrowIcon;

    public void initialize()
    {
        //POPULATE TABLE
        ObservableList<User> users
                = FXCollections.observableList(User.getUsers());

        usersTableView.setItems(users);

        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameTableColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameTableColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        userNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userNameTableColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        genderTableColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        genderTableColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        dateCreatedTableColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        dateCreatedTableColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        //OPEN USER DASHBOARD AFTER DOUBLE CLICKING
        usersTableView.setRowFactory(tv ->
        {
            TableRow<User> row = new TableRow<>();
            //EVENT HANDLER THAT OCCURS IF A ROW IN THE TABLE IS CLICKED
            row.setOnMouseClicked(event ->
            {
                //VALIDATES IF NUMBER OF CLICKS EQUALS 2
                if (event.getClickCount() == 2 && (!row.isEmpty()))
                {
                    User selectedUser = row.getItem();
                    try
                    {
                        openStartPage(selectedUser);

                        //PASS VALUES TO THE GLOBAL VARIABLE(FIRSTNAME, LASTNAME, USERNAME) 
                        CurrentUser.firstName = selectedUser.getFirstName();
                        CurrentUser.lastName = selectedUser.getLastName();
                        CurrentUser.userName = selectedUser.getUserName();
                    } catch (IOException e)
                    {
                        throw new IllegalArgumentException(e.getMessage());
                    }
                }
            });
            return row;
        });
    }

    //OPENS A NEW SCENE WHEN THE APPROPRIATE PARAMETER IS MET 
    private void openStartPage(User user) throws IOException
    {
        Stage stage = (Stage) usersTableView.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/Dashboard.fxml"));

        Parent root = loader.load();

        DashboardController controller = loader.getController();
        
        //PASSES VALUES TO THE INITIALIZEVALUES METHOD IN THE DASHBOARD CONTROLLER
        controller.initializeValues(user);

        stage.setScene(new Scene(root));
        stage.setTitle("1469 CBT System");
        stage.show();
    }

    //RETURNS TO THE LAST SCENE 
    @FXML
    void backArrowIconPressed(MouseEvent event) throws IOException
    {

        Stage stage = (Stage) usersTableView.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/Homepage.fxml"));

        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("1469 CBT System");
        stage.show();
    }
}
