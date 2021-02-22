/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author laptop
 */
public class MainApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
 
         Parent root = FXMLLoader.load(getClass().getResource("fxml/Homepage.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("1469 CBT System");
        
        Image image = new Image("/images/Application icon.PNG");
        
        stage.getIcons().add(image);
        
        stage.setResizable(false);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
                launch(args);
    }
    
}
