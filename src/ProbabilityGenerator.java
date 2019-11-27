import java.util.ArrayList;

public class ProbabilityGenerator<T> {
	
	ArrayList<T> alphabet = new ArrayList<T>();
	ArrayList<T> alphabet_variation = new ArrayList<T>();
	ArrayList<Integer> alphabet_counts = new ArrayList<Integer>();
	ArrayList<Double> prob = new ArrayList<Double>();
	float sizeTokens = 0;

	
	ArrayList<T> GetToken() {
		return alphabet;
	}
	
	
	
	void train(ArrayList<T> newTokens) { //ArrayList input will look like this in markov generator train
		alphabet = newTokens;
		int tempIndex = 0;
		for (int i = 0; i < newTokens.size(); i++) {
				newTokens.get(i);
			
			if (alphabet_variation.contains(newTokens.get(i)) == false) { //if pitch occurs for first time
				alphabet_variation.add(newTokens.get(i)); //adds a new pitch
				alphabet_counts.add(1); //adds a new frequency with the value of 1 
			}
			
			else { //if pitch has already occurred
				tempIndex = alphabet_variation.indexOf(newTokens.get(i)); //finds the index of where this pitch is stored in the token ArrayList and stores it in tempIndex
				alphabet_counts.set(tempIndex, alphabet_counts.get(tempIndex) + 1); //since the frequency has the same index as the corresponding pitch, I increase the frequency of the pitch using tempIndex as frequency's index.  
			}
		
		}
		prob.clear();
		for (int k = 0; k < alphabet_variation.size(); k++) { //finds the probability of each pitch and adds it to the prob ArrayList
			sizeTokens += newTokens.size();
			prob.add((double) alphabet_counts.get(k) / (double) newTokens.size());
		}
	}
	
	
	
	void printAll() {
		if (alphabet.get(0) instanceof Integer) { //prints the probability if the ArrayList token is of type Integer 
			System.out.print("\nPitches:\n\n-----Probability Distribution-----\n\n");
		}
		
		if (alphabet.get(0) instanceof Double) { //prints the probability if the ArrayList token is of type Double
			System.out.print("\nRhythms:\n\n-----Probability Distribution-----\n\n");
		}
		
		for (int i = 0; i < alphabet_variation.size(); i++) { //prints the different tokens and their probabilities
				System.out.print("Token: " + alphabet_variation.get(i) + " | Probability: " + prob.get(i) + "\n");
		}
		
		System.out.print("------------\n");
	}
	
	
	
	void generateMelody() {
		ProbabilityGenerator<T> probDistGen = new ProbabilityGenerator<T>();
		ProbabilityGenerator<T> melodyGen = new ProbabilityGenerator<T>();
		
		melodyGen.train(alphabet); //trains 
		
		ArrayList<T> newSong = new ArrayList<T>();
		
		for (int i = 0; i < 10000; i++) {
			newSong = melodyGen.generate(20); //generates a 20 note song from the probabilities 
			probDistGen.train(newSong);
		}
		
		probDistGen.PrintProbabilityDistribution();
	}
	
	
	
	void PrintProbabilityDistribution() {
		System.out.println("\n10000 iterations");
		for (int i = 0; i <prob.size(); i++) {
			prob.set(i, prob.get(i) / (double) 10000);
		}
		printAll();
	}
	
	
	
	void printMelody(ArrayList<T> input) {
		System.out.println("");
		for (int i = 0; i < input.size(); i++) {
			System.out.print(input.get(i) + " ");
		}
		System.out.println("");
	}
	
	
	
	ArrayList<T> generate(int size) {
		ArrayList<T> answer = new ArrayList<T>();
		
		for (int i = 0; i < size; i++) {
			T token = generate(); //gets a new token based off of the probabilities stored in prob
			answer.add(token); //adds new token to song
		}
		
		return answer; //returns new arraylist of tokens
	}
	
	
	
	void Test() { //use this to check the tokens of the generated song for debugging purposes
		for (int i = 0; i < alphabet.size(); i++) {
			System.out.print(alphabet.get(i) + " ");
		}
		System.out.println("");
	}
	
	
	
	T generate () {
		float randIndex = (float) Math.random(); //generates a random number between 0 and 1 and sets it to randIndex
		boolean found = false; //turns to true if the random number is less than or equal to 1. This is done so the while loop doesn't continue to run while the answer is already found.
		int index = 0;
		double total = 0;
		
		while (!found && index < prob.size() - 1) { //repeats until randIndex is less than the total
			total += prob.get(index);
			found = randIndex <= total;
			index++;
		}
		
		if (found) {return alphabet_variation.get(index - 1);} //returns a single token to add to the new song
		else {return alphabet_variation.get(index);}
	}
}
