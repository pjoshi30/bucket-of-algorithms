package practise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prerequisites {
	public String[] orderClasses(String[] inp) {
		TopologicalGraph g = null;
		try {
			g = new TopologicalGraph(inp);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return new String[0];
		}
		assert (null != g);
		return g.getOrder();
	}

	public static void main(String... args) {
		String[] classes = { "CSE258: CSE244 CSE243 INTR100",
				"CSE221: CSE254 INTR100", "CSE254: CSE111 MATH210 INTR100",
				"CSE244: CSE243 MATH210 INTR100", "MATH210: INTR100",
				"CSE101: INTR100", "CSE111: INTR100", "ECE201: CSE111 INTR100",
				"ECE111: INTR100", "CSE243: CSE254", "INTR100:", };
		String[] out = new Prerequisites().orderClasses(classes);
		for (String s : out)
			System.out.println(" " + s);
	}
}

class TopologicalGraph {
	private boolean[] onStack; // To detect cycle
	private boolean hasCycle;
	private boolean[] marked;
	private Stack<Integer> reversePost;
	private Digraph G;

	public TopologicalGraph(String[] inp) {
		reversePost = new Stack<Integer>();
		G = new Digraph();
		G.constructDiGraph(inp);
		onStack = new boolean[G.V()];
		marked = new boolean[G.V()];
		for (int v = 0; v < G.V(); v++) {
			if (!marked[v])
				dfs(G, v);
		}
	}

	private void dfs(Digraph G, int v) {
		onStack[v] = true;
		marked[v] = true;
		for (int w : G.adj(v)) {
			if (hasCycle)
				throw new IllegalArgumentException();
			else if (!marked[w])
				dfs(G, w);
			else if (onStack[w])
				hasCycle = true;
		}
		onStack[v] = false;
		reversePost.push(v);
	}

	public String[] getOrder() {
		ArrayList<String> order = new ArrayList<String>();
		while (!reversePost.empty())
			order.add(G.getString(reversePost.pop()));
		String[] ret = new String[order.size()];
		for (int i = 0; i < order.size(); i++) {
			ret[i] = order.get(i);
		}
		return ret;
	}

	class Digraph {

		private int count;
		private int V;
		private List<Integer>[] adj;

		private Map<String, Integer> index; // String==>index mapping

		private Vector<String> invertedIndex;

		@SuppressWarnings("unchecked")
		public Digraph() {
			invertedIndex = new Vector<String>();
			index = new HashMap<String, Integer>();
			V = 0;
			count = 0;
			int initV = 20;
			adj = (ArrayList<Integer>[]) new ArrayList[initV];
			for (int i = 0; i < initV; i++) {
				adj[i] = new ArrayList<Integer>();
			}
		}

		public String getString(int v) {
			return invertedIndex.get(v);
		}

		public int V() {
			return V;
		}

		public Iterable<Integer> adj(int v) {
			return adj[v];
		}

		public void constructDiGraph(String[] inp) {
			for (String s : inp) {
				processString(s);
			}
		}

		private void processString(String s) {
			String[] splitString = s.split(":");
			if (splitString.length == 0)
				throw new IllegalArgumentException("Split on : on " + s
						+ " was empty");
			validateStringAndBuildIndex(splitString);
			for (String atomic : splitString) {
				String[] anotherSplit = atomic.split("\\s");
				for (String s2 : anotherSplit) {
					if(null == s2 || s2.length() ==0 )
						continue;
					if (!(splitString[0].compareTo(s2) == 0))
						addEdge(splitString[0], s2);
				}
			}

		}

		private void addEdge(String from, String to) {
			adj[index.get(from.trim())].add(index.get(to.trim()));
		}

		private void validateStringAndBuildIndex(String[] splitString) {

			for (String atomic : splitString) {
				if (null == atomic || atomic.length() == 0)
					continue;
				String[] anotherSplit = atomic.split("\\s+");
				if (anotherSplit.length > 1 || anotherSplit.length == 1
						&& anotherSplit[0].compareTo(atomic) != 0) {
					validateStringAndBuildIndex(anotherSplit);
					continue;
				}
				checkTrailingWhiteSpace(atomic);
				checkDeptNameAndNumber(atomic);
			    //All is good, hence build indexes
				if (!index.containsKey(atomic.trim())) {
					index.put(atomic.trim(), count);
					invertedIndex.add(count++, atomic.trim()); 
					V++;
				}
			}
		}

		private void checkTrailingWhiteSpace(String s) {
			int len = s.length();
			char[] arr = s.toCharArray();

			// Even if the String has one space at the end, throw an
			// exception
			if (len > 0 && Character.isSpaceChar(arr[len - 1]))
				throw new IllegalArgumentException(
						"Error in checkTrailingWhiteSpace");
		}

		private void checkDeptNameAndNumber(String s) {
			int len = s.length();
			char[] arr = s.toCharArray();

			int i = 0;
			// Skip over the leading whitespace
			while (i < len && Character.isSpaceChar(arr[i]))
				i++;
			s = s.substring(i);
			Pattern pattern = Pattern.compile("\\s*[A-Z]{3,4}[0-9]{3}");
			Matcher matcher = pattern.matcher(s);
			if (!matcher.matches())
				throw new IllegalArgumentException(
						"Error in checkDeptNameAndNumber: Does not match pattern");
		}
	}

}
