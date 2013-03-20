
import project.regex.*;

public class Driver {

	public static void main(String[] args) throws SyntaxError{
		RegexParser parser = new RegexParser("[a-z]+");
		ParseTree x = parser.parse();
		x.printGraphViz();
	}
	
}
