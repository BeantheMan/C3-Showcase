import java.util.ArrayList; //Cameron Allen, 10/24/19, determines where nodes will go on the PST by finding out if the new node is a suffix of other nodes and populates the PST accordingly
import java.util.List;

public class Node<T> {
	ArrayList<T> tokenSequence = new ArrayList<T>();
	ArrayList<Node<T>> children = new ArrayList<Node<T>>(); //is this supposed to be an arraylist of arraylists?
	int count;
	
	
	Node() { //constructor
		count = 1;
	}
	
	void setTokenSequence(ArrayList<T> input) { //sets the token sequence
		tokenSequence = input;
	}
	
	int GetCount() {
		return count;
	}
	
	boolean addNode(Node<T> node) { //adds new node to PST
		boolean found = false;
		
		
		if (tokenSequence.equals(node.getTokenSequence())) {  //sees if token sequence is equal to the current node sequence
			found = true;
			count++; //counts how many times a sequence occurs
			//System.out.println(count);
		}
		else if (amIASuffix(node) || (tokenSequence.size() == 0)) { //if node is a suffix of token sequence or is the first node after the root node
			int i = 0;
			while (!found  && i < children.size()) {  //tries to find out if current node is a suffix of the child nodes
				
				found = children.get(i).addNode(node);
				i++;
				
			}			
			if (!found) { //if isn't a suffix of the child nodes, adds the node as a child
				
				children.add(node);
				found = true;
				
			}
			
		}
		
		return found;
	}
	
	ArrayList<T> getTokenSequence() //gets the token sequence
	{
		return tokenSequence;
	}
	
	
	boolean amIASuffix(Node<T> node) { //determines if token sequence of new node is a suffix of the token sequence being analyzed
		
		boolean found = false;
		 
		ArrayList<T> nodeSequence = node.getTokenSequence();
		
		ArrayList<T> subList = new ArrayList<T>(nodeSequence.subList((nodeSequence.size() ) - tokenSequence.size(), nodeSequence.size())); //populates a new arraylist with the last values of the analyzed token sequence (size of current token sequence)
		
		if (subList.equals(tokenSequence)) { //determines if the token sequences equal eachother
			found = true;
		}
		
		return found; //returns if token sequence is a suffix
	}
	
	void print() { //prints PST
		
		for (int i = 0; i < children.size(); i++) { //prints children
			children.get(i).print(1);
		}
	}
	
	void print(int numSpacesBefore) { //prints PST
		
		for (int i = 1; i <= numSpacesBefore; i++) {
			System.out.print("   ");
		}
		
		System.out.print("-->");
		System.out.print("  " + tokenSequence + "\n"); //prints token sequence of current node
		
		for (int n = 0; n < children.size(); n++) { //prints children of current node
			children.get(n).print(numSpacesBefore + 1);
		}
	 
	}
	
	boolean pMinElimination(int totalTokens, double pMin) {
		
		int numPOccur = totalTokens - (tokenSequence.size() - 1); //numPOccur is the number of times the token can possibly occur
		double empiricalProb = (double) count / numPOccur; //calculates the empirical probability and assigns it to empiricalProb
		boolean shouldRemove = empiricalProb <= pMin; //determines if node should be removed by determining of empiricalProb is less than or equal to pMin
			
		if (tokenSequence.isEmpty()) { //if tokenSequence is empty (aka the root bc the root is an empty string)
			shouldRemove =  false;
		}
		
		if (shouldRemove == false) {
			
			for (int i = children.size() - 1; i >= 0; i--) { //checks if node should be removed and if it should, it is removed. Deletes in reverse to prevent
				
				boolean remove = children.get(i).pMinElimination(totalTokens, pMin);
				
				if (remove == true) {
					
					children.remove(children.get(i)); //removes children
					
				}
			}
		}
	
 		return shouldRemove; //returns whether the node should be removed
	}
}
