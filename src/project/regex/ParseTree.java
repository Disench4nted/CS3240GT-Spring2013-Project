package project.regex;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

public class ParseTree {

	private ParseTreeNode root;
	
	private static int count = 0;

	public ParseTree() {
		this.root = null;
	}

	public void setRoot(ParseTreeNode root) {
		this.root = root;
	}

	public void printGraphViz() {
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedWriter writer = Files.newBufferedWriter(
				Paths.get("graph.gv"), charset, StandardOpenOption.CREATE,
				StandardOpenOption.APPEND)) {
			writer.write("digraph ParseTree {");
			LinkedList<String> arrows = printGraphViz(this.root);
			for (String arrow : arrows) {
				writer.write(arrow);
			}
			writer.write("}");
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}

	private LinkedList<String> printGraphViz(ParseTreeNode root) {

		LinkedList<String> arrows = new LinkedList<String>();
		if (root.getChildren().size() == 0) {
			return arrows;
		} else {
			LinkedList<ParseTreeNode> children = root.getChildren();
			for (ParseTreeNode node : children) {
				arrows.add("\""+root.getLabel() +"\"->\"" + node.getLabel()+"\"\n");
				arrows.addAll(printGraphViz(node));
				count++;
			}
			return arrows;
		}
	}

}
