import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class MultiArmedBanditPane extends GridPane {

	private Quotes authorA; 
	private Quotes authorB;
	private Alert infoBox; //contains the instructions
	private Button info, start, like, dislike;
	private ArrayList<Integer> drawPot = new ArrayList<Integer>(); // arrayList from which the the getRandomPostion() method fetches a position
	private Label overallSuccessScoreLabel, authorAScoreLabel, authorBScoreLabel; // Labels which show the different success scores 
	private Text quote; // text field to show the quote and result
	private int lastCallIndex; //integer which stores the INDEXNUMBER of the author of the last quote
	private boolean running = false;

	public MultiArmedBanditPane() throws IOException {

		// Info Box opens immediately after application gets started
		infoBox = new Alert(AlertType.INFORMATION);
		infoBox.setTitle("Multi Armed Bandit");
		infoBox.setHeaderText("Information");
		infoBox.setContentText("The goal of this application is to test a multi-armed bandit strategy.\n\n"
				+ "After you click the \"start\" button the program will show you different quotes of two different "
				+ "authors without showing you from which author it is."
				+ " This procedure gets repeated until the program can be certain which author you prefere, or you click \"Stop\". "
				+ "Afterwords it shows you the result and a few more quotes of your favourite author.\n\n Have fun!");
		infoBox.showAndWait();

		//creates two Quotes objects which get filled with the quotes of teh txt files
		authorA = new Quotes("King.txt", 0, "Martin Luther King Jr.");
		authorB = new Quotes("Trump.txt", 1, "Donald Trump");
		
		// prefilling the drawPot with the INDEXNUMBERS of the authors
		drawPot.add(authorA.getIndexNumber());
		drawPot.add(authorB.getIndexNumber());

		// Button which opens Info Box
		info = new Button("Info");
		info.setOnAction(this::infoClick);
		info.prefWidth(70);
		
		start = new Button("Start");
		start.setOnAction(this::startClick);
		start.setPrefWidth(110);

		quote = new Text("Click start to see the first quote!");
		quote.maxWidth(500);
		quote.setWrappingWidth(480);
		quote.setStyle("-fx-font-size: 14px; -fx-text-fill: blue;");
		quote.prefHeight(210);
		quote.setTextAlignment(TextAlignment.CENTER);

		like = new Button("Like");
		like.setOnAction(this::likeClick);
		like.setStyle("-fx-text-fill: green");
		like.setPrefWidth(80);

		dislike = new Button("Dislike");
		dislike.setOnAction(this::dislikeClick);
		dislike.setStyle("-fx-text-fill: red");
		dislike.setPrefWidth(80);

		Label title = new Label("Multi armed bandit\nApplication");
		title.setStyle("-fx-font-size: 18px; -fx-text-fill: Black;");
		title.prefHeight(50);
		title.setTextAlignment(TextAlignment.CENTER);

		overallSuccessScoreLabel = new Label("  Overall success Score: ");
		authorAScoreLabel = new Label("  Success score Author A: ");
		authorBScoreLabel = new Label("  Success score Author B: ");
		

		setAlignment(Pos.CENTER);
		setHgap(40);
		setVgap(10);

		// HBox quotes = new HBox(quote);

		HBox firstR = new HBox(start, info);
		firstR.setAlignment(Pos.BOTTOM_LEFT);
		firstR.setSpacing(35);

		HBox secondR = new HBox(like, dislike);
		secondR.setAlignment(Pos.CENTER);
		secondR.setSpacing(35);

		HBox thirdR = new HBox(overallSuccessScoreLabel);
		thirdR.setAlignment(Pos.CENTER);
		thirdR.setSpacing(35);

		VBox v3 = new VBox(overallSuccessScoreLabel, authorAScoreLabel, authorBScoreLabel);
		v3.setAlignment(Pos.CENTER_LEFT);
		v3.setSpacing(15);
		v3.setStyle(" -fx-font-size: 12px; -fx-border-color: gray; -fx-background-color: white;");
		v3.setPrefSize(100, 100);

		VBox left = new VBox(title, firstR, v3);
		left.setPrefWidth(200);
		left.setAlignment(Pos.CENTER);
		left.setSpacing(30);
		left.setPrefHeight(REMAINING);

		VBox right = new VBox(quote, secondR);
		right.setPrefWidth(500);
		right.setAlignment(Pos.CENTER);
		right.setSpacing(30);

		HBox bottom = new HBox(left, right);
		bottom.setSpacing(30);

		add(bottom, 0, 0);
	}

	public void infoClick(ActionEvent event) {
		infoBox.showAndWait();
	}

	public void startClick(ActionEvent event) {
		if (running == false) {
			showQuote();
			start.setText("Stop");
			running = true;
		} else {
			showResult();
			running = false;
			start.setText("Continue");
		}
	}

	public void likeClick(ActionEvent event) { 
		if (running) {
			addLike();
			refreshSuccessScores();
			showQuote();
		}
	}

	public void dislikeClick(ActionEvent event) {
		if (running) {
			refreshSuccessScores();
			showQuote();
		}
	}

	public void refreshSuccessScores() { // refreshes the success score labels
		DecimalFormat fmt = new DecimalFormat("0.##");

		overallSuccessScoreLabel
				.setText("  Overall success Score: " + fmt.format(Quotes.getSuccessScore(authorA, authorB) * 100) + "%");
		authorAScoreLabel.setText("  Success Score author A: " + fmt.format(authorA.getSuccessScore() * 100) + "%");
		authorBScoreLabel.setText("  Success Score author B: " + fmt.format(authorB.getSuccessScore() * 100) + "%");
		
	}
	// checks if program can already tell which is the preferred author with chechSuccessScore() method, if not shows next quote
	public void showQuote() { 
		if (checkSuccessScore() == false) {
			if (getRandomPosition() == authorA.getIndexNumber()) {
				quote.setText(authorA.showQuote());
				lastCallIndex = authorA.getIndexNumber();
			} else {
				quote.setText(authorB.showQuote());
				lastCallIndex = authorB.getIndexNumber();
			}
		} else {
			showResult();
			running = false;
			start.setText("Continue");
		}
	}

	public void addLike() {
		if (lastCallIndex == authorB.getIndexNumber()) {
			authorB.addLike();
			drawPot.add(authorB.getIndexNumber());
		}

		else {
			authorA.addLike();
			drawPot.add(authorA.getIndexNumber());
		}
	}
	//gets random position of ArrayList drawPot to decide which author to show next
	private int getRandomPosition() {
		Random generator = new Random();
		int index = generator.nextInt(drawPot.size());
		return drawPot.get(index);
	}

	// gets called if user clicks on stop button or checkSuccessScore() method == true
	public void showResult() {
		DecimalFormat fmt = new DecimalFormat("0.##");

		if ((authorA.getNumberOfCalls() + authorB.getNumberOfCalls()) > 6 && authorA.getSuccessScore() != authorB.getSuccessScore()) { // method only shows results if these criteria are fulfilled
			quote.setText(" Program stopped\n Your preferred author is:\t\t" + getPreferredAuthor().getAuthorName()
				+ "\n\n" + getPreferredAuthor().showQuote() + "\n\n" + getPreferredAuthor().showQuote() + "\n");
			
			overallSuccessScoreLabel.setText("  Overall success Score: " + fmt.format(Quotes.getSuccessScore(authorA, authorB) * 100) + "%");
			authorAScoreLabel.setText("  " + authorA.getAuthorName() + ": " + fmt.format(authorA.getSuccessScore() * 100) + "%");
			authorBScoreLabel.setText("  " + authorB.getAuthorName() + ": " + fmt.format(authorB.getSuccessScore() * 100) + "%");
		
		} else
			quote.setText("Sorry, we don't have enough data yet.\n\nPlease continue!");
	}

	public Quotes getPreferredAuthor() {
		if (authorA.getSuccessScore() < authorB.getSuccessScore())
			return authorB;
		else
			return authorA;
	}
	
	
	public double getAuthorComparison() {
		if (authorA.getSuccessScore() < authorB.getSuccessScore())
			return authorB.getSuccessScore() - authorA.getSuccessScore();
		else
			return authorA.getSuccessScore() - authorB.getSuccessScore();

	}

	//gets called by showQuote() method 
	public boolean checkSuccessScore() {
		if ((authorA.getNumberOfCalls() + (authorB.getNumberOfCalls()) > 8 && getAuthorComparison() > 0.35)) {
			return true;
		} else
			return false;
	}

}
