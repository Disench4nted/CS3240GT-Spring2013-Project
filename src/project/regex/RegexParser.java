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

	public ParseTree tree;

	public RegexParser(String input) {
		this.input = input;
		pointer = 0;
		tree = new ParseTree();
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

	public ParseTree parse() throws SyntaxError {

		/*
		 * tree.addNode("<reg-ex>",'\0'); tree.addNode(label, operation)regex();
		 */
		tree.setRoot(regex());
		System.out.println("Parse Complete!");
		return tree;
	}

	public ParseTreeNode regex() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<reg-ex>");
		node.addChild(rexp());
		return node;
	}

	public ParseTreeNode rexp() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<rexp>");
		node.addChild(rexp1());
		node.addChild(rexpPrime());
		return node;
	}

	public ParseTreeNode rexpPrime() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<rexp'>");
		if (accept('|')) {
			node.addChild("UNION");
			node.addChild(rexp1());
			node.addChild(rexpPrime());
			return node;
		} else {
			return node;
		}
	}

	public ParseTreeNode rexp1() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<rexp1>");
		node.addChild(rexp2());
		node.addChild(rexp1Prime());
		return node;
	}

	public ParseTreeNode rexp1Prime() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<rexp1'>");
		if (symbol == '(') {
			/*
			 * accept(symbol); node.addChild(String.valueOf(symbol));
			 * node.addChild(rexp()); accept(')'); node.addChild(")");
			 * node.addChild(rexp2Tail());
			 */
			node.addChild(rexp2());
			node.addChild(rexp1Prime());
			return node;
		} else if (isReChar()) {
			/*
			 * accept(symbol); node.addChild(String.valueOf(symbol));
			 * node.addChild(rexp2Tail());
			 */
			node.addChild(rexp2());
			node.addChild(rexp1Prime());
			return node;
		} else if (symbol == '.' || symbol == '[' || isDefinedClass()) {
			// node.addChild(rexp3());
			node.addChild(rexp2());
			node.addChild(rexp1Prime());
			return node;
		} else {
			return node;
		}
		/*
		 * if (rexp2()) { rexp1Prime(); } else { return; }
		 */
	}

	public ParseTreeNode rexp2() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<rexp2>");
		if (accept('(')) {
			node.addChild(String.valueOf('('));
			node.addChild(rexp());
			if (accept(')')) {
				node.addChild(")");
			}
			node.addChild(rexp2Tail());
			return node;
		} else if (isReChar()) {
			// accept(symbol);
			node.addChild(String.valueOf(symbol));
			accept(symbol);
			node.addChild(rexp2Tail());
			return node;
		} else if (symbol == '.' || symbol == '[' || isDefinedClass()) {
			node.addChild(rexp3());
			return node;
		} else {
			throw new SyntaxError("Invalid rexp3");
		}
	}

	public ParseTreeNode rexp2Tail() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<rexp2-tail>");
		if (accept('*')) {
			node.addChild("*");
			return node;
		} else if (accept('+')) {
			node.addChild("+");
			return node;
		} else {
			return node;
		}
	}

	public ParseTreeNode rexp3() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<rexp3>");
		if (symbol == '.' || symbol == '[' || isDefinedClass()) {
			node.addChild(charClass());
			return node;
		}
		return node;
	}

	public ParseTreeNode charClass() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<char-class>");
		if (accept('.')) {
			node.addChild(".");
			return node;
		} else if (accept('[')) {
			node.addChild("[");
			node.addChild(charClass1());
			return node;
		} else if (isDefinedClass()) {
			accept(symbol);
			node.addChild(String.valueOf(symbol));
			return node;
		} else {
			return node;
		}
	}

	public ParseTreeNode charClass1() throws SyntaxError {

		ParseTreeNode node = new ParseTreeNode("<char-class1>");
		if (isClsChar() || symbol == ']') {
			node.addChild(charSetList());
			return node;
		} else {
			node.addChild(excludeSet());
			return node;
		}

		/*
		 * if (!charSetList()) { excludeSet(); }
		 */
	}

	public ParseTreeNode charSetList() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<char-set-list>");
		if (accept(']')) {
			node.addChild("]");
			return node;
		} else {
			node.addChild(charSet());
			node.addChild(charSetList());
			return node;
		}
	}

	public ParseTreeNode charSet() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<char-set>");
		if (isClsChar()) {
			node.addChild(String.valueOf(symbol));
			accept(symbol);
			node.addChild(charSetTail());
			return node;
		} else {
			throw new SyntaxError("Invalid char-set");
		}
	}

	public ParseTreeNode charSetTail() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<char-set-tail>");
		if (accept('-')) {
			node.addChild("-");
			if (isClsChar()) {
				node.addChild(String.valueOf(symbol));
				accept(symbol);
			}
			return node;
		} else {
			return node;
		}
	}

	public ParseTreeNode excludeSet() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<exclude-set>");
		if (accept('^')) {
			node.addChild("^");
			node.addChild(charSet());
			if (accept(']')) {
				node.addChild("[");
			}
			if (accept('I')) {
				node.addChild("I");
			}
			if (accept('N')) {
				node.addChild("N");
			}
			node.addChild(excludeSetTail());
			return node;
		} else {
			throw new SyntaxError("Invalid exclude-set");
		}
	}

	public ParseTreeNode excludeSetTail() throws SyntaxError {
		ParseTreeNode node = new ParseTreeNode("<exclude-set-tail>");
		if (accept('[')) {
			node.addChild("[");
			node.addChild(charSet());
			if (accept(']')) {
				node.addChild("]");
			}
			return node;
		} else if (isDefinedClass()) {
			node.addChild(String.valueOf(symbol));
			accept(symbol);
			return node;
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

}
