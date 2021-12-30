It was necessary to implement a strategy in order that based on previous likes/dislikes the program chooses the next quote to show. 

In order to do so I created an ArrayList<Integer> called drawPot which gets pre-filled with two values; 1 and 0. 
By constructing the two Quotes objects (authorA and authorB) I assigned one of the two numbers as an INDEXNUMBER to authorA (0) and the other to authorB (1). 
So when the program gets started the getRandomPosition() method fetches a random position of the drawPot and shows a quote of the author whose INDEXNUMBER corresponds to the fetched number.
For this first“Fetch” the probability of getting authorA or authorB is 50/50. 
If the user know clicks on the Like button either a 0 or a 1 gets added to the drawPot based from which author the quote was. 
So if the user gives a Like to a quote of authorA  a 0 gets added to the drawPot and if the quote was from authorB a  gets added.
Numbers which were already fetched by the getRandomPosition() method remain in the drawPot. 
Hence the probability of getting one author or the other is not 50/50 anymore, after the user clicked on like for the first time. 

Each time before showing a quote the checkSuccessScore() method gets called which verifies if the program can already tell which of the two is the preferred author. 
The method does so by verifying if the user has already seen at least 8 quotes AND if the difference of the Success Score between the leading author and the following author is at least 0,35. 
If both of these conditions are fulfilled the process gets stopped and the results are shown to the user. 
