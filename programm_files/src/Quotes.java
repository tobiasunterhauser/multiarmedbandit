import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Quotes {

	private String[] quotesList;
	private String fileName;
	private int showQuoteCount = 0;
	private int numberOfCalls = 0;
	private int numberOfLikes = 0;
	private final int INDEXNUMBER;
	private String authorName;
	

	public Quotes(String fileName, int TOMBOLANUMBER, String authorName) throws IOException {
		this.fileName = fileName;
		this.INDEXNUMBER = TOMBOLANUMBER;
		this.authorName = authorName;
		quotesList = new String[getLengthOfList()];

		Scanner fileScan = new Scanner(new File(fileName));
		int position = 0;
		while (fileScan.hasNext()) {
			quotesList[position] = fileScan.nextLine();
			position++;
		}
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public int getNumberOfCalls() {
		return numberOfCalls;
	}

	public void setNumberOfCalls(int numberOfCalls) {
		this.numberOfCalls = numberOfCalls;
	}

	public int getNumberOfLikes() {
		return numberOfLikes;
	}

	public void setNumberOfLikes(int numberOfLikes) {
		this.numberOfLikes = numberOfLikes;
	}

	public int getIndexNumber() {
		return INDEXNUMBER;
	}

	private int getLengthOfList() throws IOException {
		int numberOfLines = 0;
		Scanner scanFile = new Scanner(new File(fileName));

		while (scanFile.hasNext()) {
			scanFile.nextLine();
			numberOfLines++;
		}
		scanFile.close();

		return numberOfLines;

	}
	/*
	 * every time this method is called it shows the next quote which is stored in
	 * the array. If arrived at the last element in the list it restarts with the
	 * first
	 */

	public String showQuote() {
		if (showQuoteCount == quotesList.length)
			showQuoteCount = 0;
		String res = "\"" + quotesList[showQuoteCount] + "\"";
		showQuoteCount++;
		numberOfCalls++;
		
		return res;

	}

	public void addLike() {
		numberOfLikes++;
	}

	public void addCall() {
		numberOfCalls++;
	}

	public double getSuccessScore() {
		if (numberOfCalls == 0 || numberOfLikes == 0)
			return 0;
		else
			return (double) numberOfLikes / numberOfCalls;
	}

	public static double getSuccessScore(Quotes input1, Quotes input2) {
		return ((double) input1.getNumberOfLikes() + (double) input2.getNumberOfLikes())
				/ (input1.getNumberOfCalls() + input2.getNumberOfCalls());
	}

	

}
