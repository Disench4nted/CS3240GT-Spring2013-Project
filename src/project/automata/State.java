package project.automata;
import java.util.ArrayList;

public class State {

	private String name;


	public State() {
		this.name = "default";
	}
	
	public State(String name) {
		this.name = name;
	}


	//prints out name
	public void print() {
		System.out.println(name);
	}
	
	//setter for name
	public void setName(String nam) {
		this.name = nam;
	}

}
