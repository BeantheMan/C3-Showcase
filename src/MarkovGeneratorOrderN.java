import java.util.ArrayList;

public class MarkovGeneratorOrderN<T> extends MarkovGenerator<T> {
	
	ArrayList<T> alphabet = new ArrayList<T>();
	ArrayList<ArrayList<T>> uniqueAlphabetSequence = new ArrayList<ArrayList<T>>(); 
	ArrayList<ArrayList<Integer>> transitionTable = new ArrayList<ArrayList<Integer>>();
	int orderN = 0;
	//ArrayList<T> prevSequence = new ArrayList<T>();
	ArrayList<ArrayList<Double>> probabilities = new ArrayList<ArrayList<Double>>();
	ArrayList<T> generation = new ArrayList<T>();
	
	
	void SetOrder(int input) {
		orderN = input;
	}
	
	void train(ArrayList<T> input) {
		
		alphabet.clear();
		
		uniqueAlphabetSequence.clear();
		
		transitionTable.clear();

		int tokenIndex = 0;
		
		int rowIndex = 0;
		
		for (int i = orderN - 1; i < input.size() - 1; i++) { //loops starts at the end of the first uniqueAlphabetSequence
			
			ArrayList<T> curSequence = new ArrayList<T>();
			
			boolean found = false;
			
			for (int p = 0; p < orderN; p++) {
				
				curSequence.add(input.get((i - (orderN - 1)) + p)); //makes curSequence equal to the values of input the length of orderN. Each time the first for loop iterates, curSequence moves up by one index.
			
			}
			
			//System.out.println(curSequence);
			
			if (uniqueAlphabetSequence.contains(curSequence)) { //determines if curSequence is in uniqueAlphabetSequence
				
				found = true;
				rowIndex = uniqueAlphabetSequence.indexOf(curSequence);
			
			}

			if (!found) { //if curSequence is not in uniqueAlphabetSequence
				
				rowIndex = uniqueAlphabetSequence.size();
				
				uniqueAlphabetSequence.add(curSequence);
				
				ArrayList<Integer> row = new ArrayList<Integer>();
				
				
				for (int n = 0; n < alphabet.size(); n++) { //populates row with zeroes for each column of the transitionTable
					
					
					row.add(0);
				
				}
					

				transitionTable.add(row); //adds a new row to the transition table
				
			}
			
			tokenIndex = alphabet.indexOf(input.get(i + 1)); //tokenIndex stands for the index of the token after curSequence
		
			
			if (tokenIndex == -1) { //if the an instance of the token is not already found in the alphabet 

				tokenIndex = alphabet.size();
				
				//T test = input.get(i + 1);
				
				alphabet.add(input.get(i + 1)); //adds token to the alphabet
				
				for (int p = 0; p < transitionTable.size(); p++) { //adds an extra column to the transitionTable
					transitionTable.get(p).add(0);
				}
				
			}
			
			ArrayList<Integer> row = transitionTable.get(rowIndex); 
			
			row.set(tokenIndex, row.get(tokenIndex) + 1); //increases the value of the transition table at token index.
			
			transitionTable.set(rowIndex, row);
		}
		PrintProb();
		ArrayList<T> temporary = new ArrayList<T>();
		temporary = (ArrayList<T>) uniqueAlphabetSequence.get(0).clone();
		generation = generate(temporary, 20);
	}
	
	T generate(ArrayList<T> initSeq) {
		T token;
		
		boolean found = uniqueAlphabetSequence.contains(initSeq);
		if (!found) {
			token = generate(uniqueAlphabetSequence.get(0));
		}
		else {
			int curSeqIndex = uniqueAlphabetSequence.indexOf(initSeq);
			ArrayList<Double> row = new ArrayList<Double>();
			row = probabilities.get(curSeqIndex);
			token = probGen(row);
		}
		
		return token; 
	}
	
	ArrayList<T> getGeneration() {
		return generation;
	}
	
	ArrayList<T> generate(ArrayList<T> initSeq, int numTokensToGen) {
		ArrayList<T> outputMelody = new ArrayList<T>();
		for (int i = 0; i < numTokensToGen; i++) {
			T token = generate(initSeq);
			initSeq.remove(0);
			initSeq.add(token);
			outputMelody.add(token);
		}
		return outputMelody;
	}
	
	
	T probGen(ArrayList<Double> prob) {
		float randIndex = (float) Math.random(); //generates a random number between 0 and 1 and sets it to randIndex
		boolean found = false; //turns to true if the random number is less than or equal to 1. This is done so the while loop doesn't continue to run while the answer is already found.
		int index = 0;
		double total = 0;
		
		while (!found && index < prob.size() - 1) { //repeats until randIndex is less than the total
			total += prob.get(index);
			found = randIndex <= total;
			index++;
		}
		
		if (found) {return alphabet.get(index - 1);} //returns a single token to add to the new song
		else {return alphabet.get(index);}
	}
	
	
	
	void PrintProb() {
		
		ArrayList<Double> probs = new ArrayList<Double>();
		
		if (alphabet.get(0) instanceof Integer) {
			System.out.println("\nPitches for order: " + orderN + "\n");
		}
		else if (alphabet.get(0) instanceof Double) {
			System.out.println("Rhythms for order: " + orderN + "\n");
		}
		
		System.out.println("-----Transition Table-----\n");
		
//		for (int n = 0; n < alphabet.size(); n++) {
//			System.out.print(alphabet.get(n) + " ");
//		}
		
		System.out.println("Columns: " + alphabet); //prints all tokens (which are the columns of the transitionTable) in alphabet
		
		for (int i = 0; i < uniqueAlphabetSequence.size(); i++) {
			probs.clear();
			
			ArrayList<Integer> row = transitionTable.get(i);
			
			double sum = 0;
			
			
			
			System.out.print(uniqueAlphabetSequence.get(i));
			
			for (int n = 0; n < row.size(); n++) { //adds up all of the values in a row of the transition table and sets value to sum
				
				sum += row.get(n);
				
			}
			
			double prob = 0;
			
			for (int m = 0; m < row.size(); m++) { //divides each value in the row by the sum to calculate the probability and prints the probability
				
				prob = row.get(m) / sum;
				double temp1 = prob;
				System.out.print(" " + temp1 + " "); 
				probs.add(temp1);
				
			}
			ArrayList<Double> temp = new ArrayList<Double>();
			for(int m = 0; m < probs.size(); m++)
			{
				temp.add(probs.get(m));
			}
			//System.out.println("Adding: " + temp);
			probabilities.add(temp);
			System.out.println("");
		}
		//System.out.println(probabilities.get(0));
//		System.out.print("\n------------\n");
//		getProbs();

	}	
	
}

	

