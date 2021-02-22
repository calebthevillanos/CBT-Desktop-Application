package home.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Question
{
    //DECLARED QUESTION CLASS VARIABLES
    private int id;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;

    //NO-ARGUMENT CONSTRUCTOR
    public Question()
    {

    }

    //CONSTRUCTOR OVERLOADING
    public Question(String question, String correctOption)
    {
        this(0, question, null, null, null, null, correctOption);
    }
    
    //CONSTRUCTOR OVERLOADING
    public Question(String question, String optionA, String optionB, String optionC, String optionD, String correctOption)
    {
        this(0, question, optionA, optionB, optionC, optionD, correctOption);

    }

    //FULL-ARGUMENT CONSTRUCTOR
    public Question(int id, String question, String optionA, String optionB, String optionC, String optionD, String correctOption)
    {
        this.id = id;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
    }

    //SETTER AND GETTER METHODS FOR THE DECLARED VARIABLE
    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String getOptionA()
    {
        return optionA;
    }

    public void setOptionA(String optionA)
    {
        this.optionA = optionA;
    }

    public String getOptionB()
    {
        return optionB;
    }

    public void setOptionB(String optionB)
    {
        this.optionB = optionB;
    }

    public String getOptionC()
    {
        return optionC;
    }

    public void setOptionC(String optionC)
    {
        this.optionC = optionC;
    }

    public String getOptionD()
    {
        return optionD;
    }

    public void setOptionD(String optionD)
    {
        this.optionD = optionD;
    }

    public String getCorrectOption()
    {
        return correctOption;
    }

    public void setCorrectOption(String correctOption)
    {
        this.correctOption = correctOption;
    }

    //DATABASE METHOS THAT GETS ALL QUESTIONS WHERE SPECIFIED PARAMETERS ARE MET
    public static ArrayList<Question> getQuestions(int questionCount, int courseId)
    {
        ArrayList<Question> questions = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT TOP " + questionCount + " * FROM Questions WHERE CourseId = " + courseId))
        {
            while (resultSet.next())
            {
                Question question = new Question(resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
                        resultSet.getString(6), resultSet.getString(7), resultSet.getString(8));
                questions.add(question);
            }
        } catch (SQLException e)
        {
            System.err.println("Could not retrieve users: " + e.getMessage());
        }
        return questions;
    }

}
