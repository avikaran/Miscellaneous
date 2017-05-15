package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    //@FXML
    //Image image = new Image("user.jpg");
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
        primaryStage.setTitle("Client 1 Chat Room");
        primaryStage.setScene(new Scene(root, 428, 721));
        primaryStage.show();
        //image = new Image("user.jpg");

    }

    public static void main(String[] args) {
        launch(args);
    }
}
