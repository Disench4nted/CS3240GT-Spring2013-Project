import java.util.ArrayList;

public class State {

	private String name;

	private ArrayList<Transition> transitions;

	public State(String name) {
		this.transitions = new ArrayList<Transition>();
		this.name = name;
	}

	public void addTransition(Transition t) {
		transitions.add(t);
	}

}
