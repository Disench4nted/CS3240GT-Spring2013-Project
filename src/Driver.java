
import project.regex.*;

public class Driver {

	public static void main(String[] args) throws SyntaxError{
		RegexParser parser = new RegexParser("["+(char)92+"*a-z]");
		parser.parse();
	}
	
}
