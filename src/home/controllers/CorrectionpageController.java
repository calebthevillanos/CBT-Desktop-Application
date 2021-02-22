package home.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import home.model.CurrentUser;
import home.model.Question;
import home.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CorrectionpageController
{

    @FXML
    private Pagination pagination;

    @FXML
    private FontAwesomeIcon backArrowIcon;

    @FXML
    private Button returnStartPageButton;

    @FXML
    private Button quitButton;

    User currentUser = new User(CurrentUser.firstName, CurrentUser.lastName, CurrentUser.userName);

    ArrayList<Question> questions;

    public void initializeValues(int questionCount, String course)
    {
        int courseId = 1;
        if (course.equalsIgnoreCase("Java"))
        {
            courseId = 1;
        } else
        {
            if (course.equalsIgnoreCase("Sql"))
            {
                courseId = 2;
            }
        }
        
        //GETS THE QUESTIONS FROM THE DATABASE AND STORES IT IN A DECLARED ARRAYLIST
        questions = Question.getQuestions(questionCount, courseId);

        pagination.setPageCount(questionCount);
        pagination.setPageFactory((Integer index)
                ->
        {
            VBox questionPane = new VBox();

            
            Label questionLabel = new Label(questions.get(index).getQuestion());

            questionPane.setSpacing(10);
            questionLabel.setFont(new Font(16));
            questionLabel.setWrapText(true);

            questionPane.getChildren().add(questionLabel);

            //STRING ARRAY OF OPTIONS THAT GETS OPTIONS FROM THE DB
            String[] options =
            {
                questions.get(index).getOptionA(), questions.get(index).getOptionB(),
                questions.get(index).getOptionC(), questions.get(index).getOptionD()
            };

            //LABEL ARRAY
            Label[] optionLabels = new Label[options.length];

            
            for (int i = 0; i < options.length; i++)
            {
                optionLabels[i] = new Label(options[i]);

                String correctOption = questions.get(index).getCorrectOption();

                if (options[i].equals(correctOption))
                {
                    optionLabels[i].setTextFill(Color.GREEN);
                } else
                {
                    optionLabels[i].setTextFill(Color.RED);
                }

                optionLabels[i].setFont(new Font(14));
                questionPane.getChildren().add(optionLabels[i]);
            }

            return questionPane;

        });

    }

    //CLOSE THE WINDOW
    @FXML
    void quitButtonPressed(ActionEvent event)
    {
        System.exit(0);
    }

    @FXML
    void returnStartPageButtonPressed(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) returnStartPageButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/Dashboard.fxml"));
        Parent root = loader.load();

        DashboardController controller = loader.getController();
        controller.initializeValues(currentUser);
        stage.setScene(new Scene(root));
        stage.setTitle("1469 CBT System");
        stage.show();

    }
}
