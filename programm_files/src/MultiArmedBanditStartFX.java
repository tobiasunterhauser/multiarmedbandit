
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MultiArmedBanditStartFX extends Application{
    public void start(Stage primaryStage) throws IOException{
        Scene scene = new Scene(new MultiArmedBanditPane(), 800, 300);
       

        primaryStage.setTitle("Multi Armed Bandit");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    public static void main(String[] args){
        launch(args);
    }
}