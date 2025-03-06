package aads.labs.lab1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ExpListApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ExpListApplication.class.getResource("explist_interface.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 720);
        stage.setTitle("ExpList");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {launch();}
}