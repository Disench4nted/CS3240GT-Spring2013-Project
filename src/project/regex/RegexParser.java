package project.regex;

/**
 * The Recursive Descent Parser for the grammar specified in Project1-Regex.doc
 * 
 * NOTE: doesn't yet work with escaped characters yet because Java automatically
 * inserts a backslash in front of a backslash. >:|
 * 
 * @author nirav
 * 
 */
public class RegexParser {

	/**
	 * This holds the next symbol to process
	 */
	public char symbol;

	public int pointer;

	public String input;

	public RegexParser(String input) {
		this.input = input;
		pointer = 0;
		getSymbol();
	}

	public boolean isDigit() {
		if (symbol >= 48 && symbol <= 57) {
			return true;
		}
		return false;
	}

	public boolean isNonZero() {
		if (symbol >= 49 && symbol <= 57) {
			return true;
		}
		return false;
	}

	public boolean isChar() {
		char c = symbol;
		if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
			return true;
		}
		return false;
	}

	public boolean isDefinedClass() throws SyntaxError {
		if (isDigit()) {
			return true;
		} else if (isNonZero()) {
			return true;
		} else if (isChar()) {
			return true;
		} else {
			// throw new SyntaxError("Invalid defined-class");
			return false;
		}
	}

	public boolean isReChar() throws SyntaxError {
		if (symbol == 92) {
			getSymbol();
			if (symbol == 92 || symbol == '*' || symbol == '+' || symbol == '?'
					|| symbol == '|' || symbol == '[' || symbol == ']'
					|| symbol == '(' || symbol == '.' || symbol == 39
					|| symbol == 34 || symbol == 41) {
				return true;
			} else {
				throw new SyntaxError("Invalid RE_CHAR");
			}
		} else {
			if (symbol != 92 && symbol != '*' && symbol != '+' && symbol != '?'
					&& symbol != '|' && symbol != '[' && symbol != ']'
					&& symbol != '(' && symbol != '.' && symbol != 39
					&& symbol != 34 && symbol != 41) {
				return true;
			}
			return false;
		}
	}

	public boolean isClsChar() throws SyntaxError {
		if (symbol == 92) {
			getSymbol();
			if (symbol == 92 || symbol == '^' || symbol == '-' || symbol == '['
					|| symbol == ']') {
				return true;
			} else {
				throw new SyntaxError("Invalid RE_CHAR");
			}
		} else {
			if (symbol != 92 && symbol != '^' && symbol != '-' && symbol != '['
					&& symbol != ']') {
				return true;
			}
			return false;
		}
	}

	public void regex() throws SyntaxError {
		rexp();
	}

	public void rexp() throws SyntaxError {
		rexp1();
		rexpPrime();
	}

	public void rexpPrime() throws SyntaxError {
		if (accept('|')) {
			rexp1();
			rexpPrime();
		} else {
			return;
		}
	}

	public void rexp1() throws SyntaxError {
		rexp2();
		rexp1Prime();
	}

	public void rexp1Prime() throws SyntaxError {

		if (rexp2()) {
			rexp1Prime();
		} else {
			return;
		}
	}

	public boolean rexp2() throws SyntaxError {
		if (accept('(')) {
			rexp();
			accept(')');
			rexp2Tail();
			return true;
		} else if (isReChar()) {
			// accept(symbol);
			accept(symbol);
			rexp2Tail();
			return true;
		} else if (rexp3()) {
			return true;
		} else {
			// throw new SyntaxError("Invalid rexp3");
			return false;
		}
	}

	public void rexp2Tail() throws SyntaxError {
		if (accept('*')) {
			;
		} else if (accept('+')) {
			;
		} else {
			return;
		}
	}

	public boolean rexp3() throws SyntaxError {
		if (charClass()) {
			return true;
		}
		return false;
	}

	public boolean charClass() throws SyntaxError {
		if (accept('.')) {
			return true;
		} else if (accept('[')) {
			charClass1();
			return true;
		} else if (isDefinedClass()) {
			accept(symbol);
			return true;
		} else {
			return false;
		}
	}

	public void charClass1() throws SyntaxError {
		if (!charSetList()) {
			excludeSet();
		}
	}

	public boolean charSetList() throws SyntaxError {
		if (accept(']')) {
			return true;
		} else {
			charSet();
			charSetList();
		}
		return true;
	}

	public boolean charSet() throws SyntaxError {
		if (isClsChar()) {
			accept(symbol);
			charSetTail();
			return true;
		}
		return false;
	}

	public boolean charSetTail() throws SyntaxError {
		if (accept('-')) {
			if (isClsChar()) {
				accept(symbol);
				return true;
			}
		}
		return true;
	}

	public void excludeSet() throws SyntaxError {
		if (accept('^')) {
			charSet();
			accept(']');
			accept('I');
			accept('N');
			excludeSetTail();
		} else {
			throw new SyntaxError("Invalid exclude-set");
		}
	}

	public void excludeSetTail() throws SyntaxError {
		if (accept('[')) {
			charSet();
			accept(']');
		} else if (isDefinedClass()) {
			accept(symbol);
		} else {
			throw new SyntaxError("Invalid excludeSetTail");
		}
	}

	public boolean accept(char someSymbol) {
		if (someSymbol == symbol) {
			getSymbol();
			return true;
		}
		return false;
	}

	public void getSymbol() {
		if (pointer < input.length()) {
			this.symbol = input.charAt(pointer);
			pointer++;
		}
	}

	public void parse() throws SyntaxError {
		regex();
		System.out.println("Parse Complete!");
		return;
	}

}
