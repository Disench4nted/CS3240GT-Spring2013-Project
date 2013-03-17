package project.automata;
import java.util.ArrayList;

public class FiniteAutomata {

	private ArrayList<State> states;

	public FiniteAutomata() {
		this.states = new ArrayList<State>();
	}

	public void addState(State s) {
		states.add(s);
	}

}
