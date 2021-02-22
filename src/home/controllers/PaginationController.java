package home.controllers;

import home.model.Question;
import home.model.Time;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PaginationController
{

    @FXML
    private Pagination pagination;

    @FXML
    private Button submitButton;

    @FXML
    private Label timerLabel;

    private Boolean stopped = false;

    int previousScore = 0;


    private Map<Integer, String> chosenOptions = new HashMap<>();

    private int questionCount;
    private String course;
    private LocalTime startTime;

    public void initialize()
    {
        startTime = LocalTime.now();
        //STARTS THE TIMER AS THE SCENE OPENS
        startTimer();
    }

    private Thread thread;

    public void startTimer()
    {
        //THREAD THAT PERFORMS THE TIMER
        thread = new Thread()
        {
            @Override
            public void run()
            {
                while (!stopped)
                {
                    if (Time.minute == 0 && Time.second == 0)
                    {
                        Platform.runLater(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    resultPage();
                                } catch (IOException e)
                                {
                                    System.out.println("an error occured" + e.getMessage());
                                }
                            }
                        });
                        break;
                    }
                    if (Time.second == 0)
                    {
                        Time.minute--;
                        Time.second = 59;
                    }
                    updateTimerLabel();
                    try
                    {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex)
                    {
                        System.err.println("An error occured: " + ex.getMessage());
                    }
                    Time.second--;
                }
            }
        };
        thread.start();
    }

    public void updateTimerLabel()
    {
        //WHAT CHANGES THE VALUES ON THE TIMERLABEL
        Platform.runLater(() ->
        {
            timerLabel.setText(String.format("%02d:%02d:%02d", Time.hour, Time.minute, Time.second));
        });
    }
    
    ArrayList<Question> questions;
    
    public void initializeValues(int questionCount, String course)
    {
        this.course = course;
        this.questionCount = questionCount;

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

            //DECLARED CONTROLS
            Label questionLabel = new Label(questions.get(index).getQuestion());
            RadioButton optA = new RadioButton(questions.get(index).getOptionA());
            RadioButton optB = new RadioButton(questions.get(index).getOptionB());
            RadioButton optC = new RadioButton(questions.get(index).getOptionC());
            RadioButton optD = new RadioButton(questions.get(index).getOptionD());

            ToggleGroup optionToogleGroup = new ToggleGroup();
            optA.setToggleGroup(optionToogleGroup);
            optB.setToggleGroup(optionToogleGroup);
            optC.setToggleGroup(optionToogleGroup);
            optD.setToggleGroup(optionToogleGroup);

            //SAVES THE VALUE OF WHICH OF THE RADIOBUTTON WAS CLICKED ON
            if (chosenOptions.containsKey(index))
            {
                for (Toggle toggle : optionToogleGroup.getToggles())
                {
                    RadioButton radioButton = (RadioButton) toggle;
                    if (radioButton.getText().equals(chosenOptions.get(index)))
                    {
                        radioButton.setSelected(true);
                    }
                }
            }

            //SETS ON ACTION EVENTHANDLERS FOR ALL OPTIONRADIOBUTTON
            optA.setOnAction((event) -> radioButtonPressed(event, index));

            optB.setOnAction((event) -> radioButtonPressed(event, index));

            optC.setOnAction((event) -> radioButtonPressed(event, index));

            optD.setOnAction((event) -> radioButtonPressed(event, index));

            questionLabel.setFont(new Font(16));
            questionLabel.setWrapText(true);
            questionPane.setSpacing(10);
            questionPane.getChildren().addAll(questionLabel, optA, optB, optC, optD);

            return questionPane;

        });

    }

    //PUTS THE VALUE OF THE OPTION SELECTED IN THE HASHMAP
    public void radioButtonPressed(ActionEvent e, int index)
    {
        String value = ((RadioButton) e.getSource()).getText();
        chosenOptions.put(index, value);
    }

    public void resultPage() throws IOException
    {
        stopped = true;
        Duration timeSpent = Duration.between(startTime, LocalTime.now());
        long minutes = timeSpent.getSeconds() / 60;
        long seconds = timeSpent.getSeconds() % 60;

        Time.minutesSpent = minutes;
        Time.secondsSpent = seconds;
        
        //CHECKS IF CHOSENOPTION SELECTED BY USER IS THE SAME AS CORRECTOPTION IF TRUE ADDS +1 TO THE CORRETANSWERS VARIABLE
        int correctAnswers = 0;
        for (int i = 0; i < chosenOptions.size(); i++)
        {
            String chosenOption = chosenOptions.get(i);
            String correctOption = questions.get(i).getCorrectOption();
//            System.out.println(chosenOption + correctOption);
            if (correctOption.equals(chosenOption))
            {
                correctAnswers++;
            }

        }

        Stage stage = (Stage) submitButton.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/Resultpage.fxml"));

        Parent root = loader.load();

        ResultpageController controller = loader.getController();
        controller.setParameters(questionCount, course, correctAnswers);

        stage.setScene(new Scene(root));
        stage.setTitle("1469 CBT System");
        stage.show();
    }

    @FXML
    void submitButtonPressed(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Submit Test");
        alert.setContentText(
                "Are you sure you want to Submit?");

        Optional<ButtonType> buttonSelected = alert.showAndWait();
        if (buttonSelected.get() == ButtonType.OK)
        {
            resultPage();
        }
    }

}
