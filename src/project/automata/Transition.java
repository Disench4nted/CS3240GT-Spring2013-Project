package project.automata;
public class Transition {

	private char label;
	private State to, from;
	
	private boolean isEpsilon;

	/*
	 * sets a label, whether it is epsilon, and a to and from state
	 */
	public Transition(char label, boolean isEps, State from_, State to_) {
		this.label = label;
		this.isEpsilon = isEps;
		this.to = to_;
		this.from = from_;
	}

	//prints out label
	public void print() {
		System.out.println(label);
		if(isEpsilon)
			System.out.println("Epsilon");
		if(!isEpsilon)
			System.out.println("NOT Epsilon");
	}
	
	/*
	 * prints out the names of to and from states
	 */
	public void printStates() {
		to.print();
		from.print();
	}
}
