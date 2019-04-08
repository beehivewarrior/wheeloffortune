package WheelOfFortune;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Board extends BorderPane {
    private final String ColorBlue = "#0B2637";
    private Button spin = new Button("Spin");
    private TextField letterGuesser = new TextField();
    private ArrayList<String> goodGuesses = new ArrayList<>();
    private ArrayList<String> badGuesses = new ArrayList<>();
    private Text listOfLetters = new Text();
    private Wheel gameWheel = new Wheel();
    private Text phraseBox = new Text("");
    private String phaseUsed = "";
    private ArrayList<String> phrases = new ArrayList<>();
    private int userScore = 0;
    private Text score = new Text(String.format("Score: %d", this.userScore));


    public Board() {
        this.phraseBox.setFill(Color.WHITE);
        this.resetBoard();
        char spaceChar = ' ';
        this.goodGuesses.add(String.valueOf(spaceChar));

    }

    private void resetBoard() {
        this.userScore = 0;
        this.score = new Text(String.format("Score: %d", this.userScore));
        this.gameWheel = new Wheel();
        this.listOfLetters.setText("");
        this.setTop(addTop());
        this.setBottom(addBottom());
        try {
            this.setPhraseBox();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setRight(addRight());
        this.setLeft(addLeft());

        this.setCenter(this.gameWheel);
    }

    private void nextTurn() {
        this.spin.setDisable(true);
        this.updateScore(this.gameWheel.rotateWheel());
        this.letterGuesser.setDisable(false);
    }

    private void updateScore(int newScore) {
        this.userScore += newScore;
        this.score.setText(String.format("Score: %d", this.userScore));
    }

    private HBox basicHBpx() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle(String.format("-fx-background-color: %s", ColorBlue));
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }

    private HBox addTop() {
        HBox hbox = basicHBpx();

        Text gameMessage = new Text("Welcome to the Wheel of Fortune");
        gameMessage.setTextAlignment(TextAlignment.CENTER);
        gameMessage.setFont(Font.font("Helvetica", FontWeight.BOLD, 32));
        gameMessage.setFill(Color.WHITE);

        hbox.getChildren().add(gameMessage);
        return hbox;
    }

    private VBox addBottom() {
        HBox buttonContainer = basicHBpx();
        VBox masterContainer = new VBox();

        masterContainer.setPadding(new Insets(15, 0, 0, 0));
        masterContainer.setSpacing(10);
        masterContainer.setAlignment(Pos.CENTER);


        spin.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        spin.setPrefSize(100, 20);
        spin.setOnAction(event -> {
            nextTurn();
            letterGuesser.requestFocus();
        });

        String[] options = new String[]{
                "Reset Game"
        };

        ComboBox<String> comboMenu = new ComboBox<>(FXCollections.observableArrayList(options));
        EventHandler<ActionEvent> selected = event -> {
            if (comboMenu.getValue().equals("Reset Game")) {
                resetBoard();
                spin.setDisable(false);
                spin.requestFocus();
            }
        };

        comboMenu.setOnAction(selected);


        buttonContainer.getChildren().add(spin);
        buttonContainer.getChildren().add(comboMenu);
        masterContainer.setStyle(String.format("-fx-background-color: %s", ColorBlue));


        masterContainer.getChildren().add(this.phraseBox);
        masterContainer.getChildren().add(buttonContainer);

        return masterContainer;
    }

    private VBox addRight() {
        VBox letterContainer = basicVBox();

        Text guessed_letters = new Text("Guessed Letters");
        setVBoxHeader(letterContainer, guessed_letters);

        listOfLetters.setFont(new Font("Helvetica", 14));
        listOfLetters.setFill(Color.WHITE);

        letterContainer.getChildren().add(this.listOfLetters);
        letterContainer.setMinWidth(150);
        letterContainer.setMaxWidth(150);
        return letterContainer;
    }

    private void setVBoxHeader(VBox letterContainer, Text guessed_letters) {
        guessed_letters.setFont(new Font("Helvetica", 18));
        guessed_letters.setFill(Color.WHITE);
        guessed_letters.setTextAlignment(TextAlignment.CENTER);
        letterContainer.getChildren().add(guessed_letters);
    }

    private VBox basicVBox() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(0, 15, 0, 15));
        vbox.setStyle(String.format("-fx-background-color: %s", ColorBlue));
        return vbox;
    }

    private void guessLetter(String letter) {

        if (this.phaseUsed.contains(letter) && !this.phraseBox.getText().equals(this.phaseUsed) && !this.phraseBox.getText().contains(letter)) {
            this.goodGuesses.add(letter);
            this.listOfLetters.setText(this.listOfLetters.getText() + " " + letter);

            String[] ary = this.phaseUsed.split("");

            StringBuilder newLine = new StringBuilder();
            for (int i = 0; i < this.phaseUsed.length(); i++) {
                if (this.goodGuesses.contains(ary[i])) {
                    newLine.append(ary[i]);
                } else {
                    newLine.append("-");
                }
            }
            this.phraseBox.setText(newLine.toString());
            this.phraseBox.setFill(Color.WHITE);
            this.spin.setDisable(false);

        } else {
            if (!this.phraseBox.getText().equals(this.phaseUsed) & !this.badGuesses.contains(letter)) {
                this.badGuesses.add(letter);
                this.listOfLetters.setText(this.listOfLetters.getText() + " " + letter);
                this.spin.setDisable(false);
                this.userScore = 0;
                this.score.setText(String.format("Score: %d", this.userScore));
            }
        }
    }

    private VBox addLeft() {
        VBox scoreBoard = basicVBox();

        Text scoreBoarTitle = new Text("Score Board");
        setVBoxHeader(scoreBoard, scoreBoarTitle);

        letterGuesser.setPrefColumnCount(1);
        letterGuesser.setOnKeyReleased(event -> {
            guessLetter(event.getText().toUpperCase());
            letterGuesser.setText("");
            letterGuesser.setDisable(true);
        });
        letterGuesser.setDisable(true);


        score.setFill(Color.WHITE);
        score.setFont(new Font("Helvetica", 24));
        score.setTextAlignment(TextAlignment.CENTER);
        scoreBoard.getChildren().add(score);
        scoreBoard.getChildren().add(letterGuesser);
        scoreBoard.setMaxWidth(150);
        scoreBoard.setMinWidth(150);
        return scoreBoard;
    }

    private void setPhraseBox() throws IOException {
        this.phraseBox.setFont(new Font("Helvetica", 32));
        this.phraseBox.setFill(Color.BLACK);
        this.phraseBox.setTextAlignment(TextAlignment.CENTER);

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("./data/phrases.txt")));

        String inputLine;

        while ((inputLine = reader.readLine()) != null) {
            phrases.add(inputLine.trim());
        }
        reader.close();

        String phaseToUser = phrases.get(new Random().nextInt(phrases.size())).toUpperCase();
        this.phaseUsed = phaseToUser;
        StringBuilder message = new StringBuilder("_");
        for (int i = 1; i < phaseToUser.length(); i++) {
            message.append(" _");
        }

        this.phraseBox.setText(message.toString());
    }
}
