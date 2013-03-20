package project.regex;

import java.util.LinkedList;

public class ParseTreeNode {

	private String label;

	private ParseTreeNode left;

	private ParseTreeNode right;

	private LinkedList<ParseTreeNode> children;

	public ParseTreeNode(String label) {
		this.label = label;
		this.children = new LinkedList<ParseTreeNode>();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ParseTreeNode getLeft() {
		return left;
	}

	public ParseTreeNode getRight() {
		return right;
	}

	public void setLeft(ParseTreeNode left) {
		this.left = left;
	}

	public void setRight(ParseTreeNode right) {
		this.right = right;
	}

	public void addChild(String label) {
		ParseTreeNode node = new ParseTreeNode(label);
		this.children.add(node);
	}
	
	public void addChild(ParseTreeNode node){
		this.children.add(node);
	}

	public LinkedList<ParseTreeNode> getChildren() {
		return children;
	}

	public void setChildren(LinkedList<ParseTreeNode> children) {
		this.children = children;
	}

}
