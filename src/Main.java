import WheelOfFortune.Board;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primary){
        Board gmae = new Board();
        primary.setMinWidth(360);
        primary.setMinHeight(270);

        primary.setScene(new Scene(gmae));
        primary.setTitle("Wheel Of Fortune");
        primary.show();
    }
}
