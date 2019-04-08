package WheelOfFortune;


import javafx.animation.RotateTransition;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Random;

public class Wheel extends StackPane {
    RotateTransition rotator;
    int[] values = new int[19];
    private StackPane wheel = new StackPane();

    public Wheel() {
        createRotatory();
        Group base = new Group();
        Group numbers = new Group();
        Color[] colors = new Color[]{
                Color.web("#B6DCB6"), Color.web("#FCB6D0"), Color.web("#A26A5F"),
                Color.web("#FAF1D6"), Color.web("#ACBFEA"), Color.web("#FEB78D")};
        Random r = new Random();

        for (int j = 1; j <= 18; j++) {
            Arc newArc = new Arc();

            newArc.setRadiusX(240);
            newArc.setRadiusY(240);
            newArc.setLength(-20);
            newArc.setStartAngle((j * 20) - 270);
            newArc.setFill(colors[j % colors.length]);

            newArc.setType(ArcType.ROUND);


            base.getChildren().add(newArc);
        }

        Circle a = new Circle(36, Color.BLACK);
        base.getChildren().add(a);

        for (int j = 1; j <= 18; j++) {
            int cellValue = ((r.nextInt((1000 - 100) + 1) + 49) / 50) * 50;
            values[j] = cellValue;
            HBox n = new HBox();
            n.setRotate(((j * 20) - 310) + 10);
            n.setMinHeight(20);
            n.setMinWidth(360);
            Text cell = new Text(100, 100, String.format("%d", cellValue));
            cell.setFont(new Font("Helvetica", 14));
            cell.setRotate(180);
            n.getChildren().add(cell);
            numbers.getChildren().add(n);
        }


        wheel.getChildren().add(base);
        wheel.getChildren().add(numbers);

        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(20.0, 10.0, 0.0, 0.0,
                10.0, 20.0);
        arrow.setFill(Color.WHITE);
        arrow.setRotate(45);
        arrow.setTranslateY(-35);

        this.getChildren().add(wheel);
        this.getChildren().add(arrow);

    }

    private void createRotatory() {
        RotateTransition rotator = new RotateTransition(Duration.millis(5000), wheel);
        rotator.setAxis(Rotate.Z_AXIS);
        rotator.setFromAngle(0);
        rotator.setCycleCount(1);

        this.rotator = rotator;
    }

    public int rotateWheel() {
        int degrees = Math.abs(new Random().nextInt(360));

        this.rotator.setFromAngle(this.wheel.getRotate());
        this.rotator.setToAngle(degrees);
        this.rotator.play();
        int a = (degrees % 360) / 20;
        int b = a == 0 ? 18 : 19-a;

        return values[b];
    }
}
