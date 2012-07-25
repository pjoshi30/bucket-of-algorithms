package practise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
//		String[] classes = { "ECE101: ECE201", "ECE201: ECE101" };
//		 String[] classes = {"ECE101: ECE201"};
		String[] out = new Prerequisites().orderClasses(classes);
		for (String s : out)
			System.out.println(" " + s);
				
				String test = "CSE244: CSE243 MATH210 INTR100";
				String[] testsplit = test.split("[: ]");
				System.out.println("*******");
				for(String s: testsplit)
					System.out.print(s+"|");
	}
}

class TopologicalGraph {
	private boolean[] onStack; // To detect cycle
	private boolean[] marked;
	private Stack<Integer> reversePost;
	private Digraph G;

	public TopologicalGraph(String[] inp) {
		reversePost = new Stack<Integer>();
		G = new Digraph();
		G.constructDiGraph(inp);
		if (!G.isValid())
			throw new IllegalArgumentException(
					"Dependencies not fully defined.");
		onStack = new boolean[G.V()];
		marked = new boolean[G.V()];
		for (int w : G.getZeroIndegreeList()) {
			if (!marked[w])
				dfs(G, w);
		}
		for (int v = 0; v < G.V(); v++) {
			if (!marked[v])
				dfs(G, v);
		}
	}

	private void dfs(Digraph G, int v) {
		onStack[v] = true;
		marked[v] = true;

		for (int w : G.adj(v)) {
			if (!marked[w])
				dfs(G, w);
			else if (onStack[w]) {
				throw new IllegalArgumentException("Cycle Detected!");
			}
		}
		onStack[v] = false;
		reversePost.push(v);
	}

	public String[] getOrder() {

		String[] ret = new String[reversePost.size()];
		int i = 0;
		for (int v : reversePost)
			ret[i++] = G.getString(v);
		return ret;
	}

	class Digraph {

		private int count;
		private int V;
		private List<Integer>[] adj;
		private List<Integer> zeroIndegree;
		private Set<Integer> nonZeroIndegree;
		private Set<Integer> dependencies;

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
			nonZeroIndegree = new HashSet<Integer>();
			dependencies = new HashSet<Integer>();
			zeroIndegree = new ArrayList<Integer>();
		}

		public String getString(int v) {
			return invertedIndex.get(v);
		}

		public int V() {
			return V;
		}

		public Iterable<Integer> getZeroIndegreeList() {
			Collections.sort(zeroIndegree, new PrereqComparator());
			return zeroIndegree;
		}

		public boolean isValid() {
			if (V > dependencies.size())
				return false;
			return true;
		}

		public Iterable<Integer> adj(int v) {
			Collections.sort(adj[v], new PrereqComparator());
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
			if (splitString.length > 1)
				checkTrailingWhiteSpace(splitString[1]);
			validateStringAndBuildIndex(splitString[0]);
			dependencies.add(index.get(splitString[0]));
			if (!nonZeroIndegree.contains(index.get(s.trim()))) {
				zeroIndegree.add(index.get(splitString[0].trim()));
			}
			if (splitString.length < 2)
				return;
			String[] anotherSplit = splitString[1].split("\\s");
			for (String s2 : anotherSplit) {
				if (null == s2 || s2.length() == 0)
					continue;
				checkTrailingWhiteSpace(s2);
				validateStringAndBuildIndex(s2);
				// if (!(splitString[0].trim().compareTo(s2.trim()) == 0))
				addEdge(splitString[0], s2);
				nonZeroIndegree.add(index.get(s2.trim()));
			}
		}

		private void addEdge(String from, String to) {
			adj[index.get(from.trim())].add(index.get(to.trim()));
		}

		private void validateStringAndBuildIndex(String atomic) {

			checkDeptNameAndNumber(atomic);
			// All is good, hence build indexes
			if (!index.containsKey(atomic.trim())) {
				index.put(atomic.trim(), count);
				invertedIndex.add(count++, atomic.trim());
				V++;
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

		private int compareCourses(String a, String b) {
			if (a.compareTo(b) == 0)
				return 0;
			Pattern pattern = Pattern.compile("\\s*[A-Z]{3,4}[0-9]{3}");
			Matcher matcher = pattern.matcher(a);
			if (!matcher.matches())
				throw new IllegalArgumentException(
						"Error in compare: Does not match pattern");
			matcher = pattern.matcher(b);
			if (!matcher.matches())
				throw new IllegalArgumentException(
						"Error in compare: Does not match pattern");
			pattern = Pattern.compile("[0-9]{3}");
			matcher = pattern.matcher(a);
			matcher.find();
			int deptNumberA = Integer.valueOf(matcher.group());
			matcher = pattern.matcher(b);
			matcher.find();
			int deptNumberB = Integer.valueOf(matcher.group());
			if (deptNumberA < deptNumberB)
				return -1;
			else if (deptNumberA > deptNumberB)
				return 1;
			// Dept numbers are equal, check for dept name case
			pattern = Pattern.compile("\\s*[A-Z]{3,4}");
			matcher = pattern.matcher(a);
			matcher.find();
			String deptA = matcher.group().trim();
			matcher = pattern.matcher(b);
			matcher.find();
			String deptB = matcher.group().trim();

			return (deptA.compareTo(deptB));
		}

		class PrereqComparator implements Comparator<Integer> {

			public int compare(Integer o1, Integer o2) {
				return compareCourses(invertedIndex.get(o1),
						invertedIndex.get(o2));
			}

		}
	}
}
// {"INTR100","CSE101","CSE111","ECE111","ECE201","MATH210","CSE254","CSE221","CSE243","CSE244","CSE258"}