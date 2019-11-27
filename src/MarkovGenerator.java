import java.util.ArrayList;

public class MarkovGenerator<T> extends ProbabilityGenerator<T> {
	
	
	ArrayList<T> alphabet = new ArrayList<T>();
	ArrayList<ArrayList<Integer>> transitionTable = new ArrayList<ArrayList<Integer>>();
	ArrayList<Double> prob = new ArrayList<Double>();
	
	
	
	void train(ArrayList<T> input) {
		alphabet.clear();
		transitionTable.clear();
		prob.clear();

		int lastIndex = -1;
		int tokenIndex = 0;
		
		for (int i = 0; i < input.size(); i++) {
			
			tokenIndex = alphabet.indexOf(input.get(i));
			
			if (tokenIndex == -1) {

				tokenIndex = alphabet.size();
				ArrayList<Integer> row = new ArrayList<Integer>();
				
				for (int n = 0; n < alphabet.size(); n++) {
					row.add(0);
				}

				transitionTable.add(row); //adds a new row to the transition table
				
				for (int p = 0; p < transitionTable.size(); p++) { //adds a new column to the transition table by adding a 0 to every row
					transitionTable.get(p).add(0);
				}
				
				alphabet.add(input.get(i)); //adds new token to alphabet
				
			}
			
			if (lastIndex > -1) { //updates row by adding one to it
				
				ArrayList<Integer> row = transitionTable.get(lastIndex);
				row.set(tokenIndex, row.get(tokenIndex) + 1);
				transitionTable.set(lastIndex, row);
				
			}
			
			lastIndex = tokenIndex; //sets lastIndex to dataIndex which is the current index
		
		}
		
		//PrintProb();
	
	}
	
	void PrintProb() {
		
		if (alphabet.get(0) instanceof Integer) {
			System.out.println("Pitches:\n");
		}
		else {
			System.out.println("Rhythm:\n");
		}
		
		System.out.println("-----Transition Table-----\n");
		
		System.out.println("Columns: " + alphabet);
		
		for (int i = 0; i < alphabet.size(); i++) {
			
			ArrayList<Integer> row = transitionTable.get(i);
			
			double sum = 0;
			double prob = 0;
			
			System.out.print(alphabet.get(i));
			
			for (int n = 0; n < row.size(); n++) {
				
				sum += row.get(n);
				
			}
			
			for (int m = 0; m < row.size(); m++) {
				
				prob = row.get(m) / sum;
				System.out.print(" " + prob + " ");
				
			}
			System.out.println("");
		}
		System.out.print("\n------------");
	}	
	
	void display() { //used this to look at the different tokens in the alphabet
		
		for (int i = 0; i < alphabet.size(); i++) {
			System.out.println(alphabet.get(i) + " ");
		}
		//System.out.println(prob.size());
		
	}
}
