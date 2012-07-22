package practise;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
 Hello!


 8
 /\
 9   7
 / \   /\
 6  5  2 3

 8 
 9 7
 6 5 2 3
 */
public class BFSBinary {

	private Node root;

	class Node {
		Node left, right;
		Integer val;
		boolean marked;
	}

	public BFSBinary() {
		this.root = new Node();
		root.val = 8;
		root.marked = false;
		Node node = new Node();
		node.val = 9;
		node.marked = false;
		root.left = node;
		Node node1 = new Node();
		node1.val = 7;
		node1.marked = false;
		root.right = node1;
		Node node2 = new Node();
		node2.val = 6;
		node2.marked = false;
		node.left = node2;
		Node node3 = new Node();
		node3.val = 5;
		node3.marked = false;
		node.right = node3;

		Node node4 = new Node();
		node4.val = 2;
		node4.marked = false;
		node1.left = node4;
		Node node5 = new Node();
		node5.val = 3;
		node5.marked = false;
		node1.right = node5;
	}

	// Assuming that all node have intially marked == false
	public void bfs() {
		Queue<Node> queue = new ConcurrentLinkedQueue<BFSBinary.Node>();
		root.marked = true;
		System.out.println(root.val + " ");
		queue.add(root);
		int nodeInCurrentLevel = 1;
		int nodeInNextLevel = 0;
		while (!queue.isEmpty()) {
			Node node = queue.remove();
			nodeInCurrentLevel--;
			if (node.left != null) {
				Node left = node.left;
				if (!left.marked) {
					System.out.print(left.val + " ");
					left.marked = true;
					queue.add(left);
					nodeInNextLevel++;
				}
			}
			if (node.right != null) {
				Node right = node.right;
				if (!right.marked) {
					System.out.print(right.val +" ");
					right.marked = true;
					queue.add(right);
					nodeInNextLevel++;
				}
			}
			if(nodeInCurrentLevel == 0){
				System.out.println(" ");
				nodeInCurrentLevel = nodeInNextLevel;
				nodeInNextLevel = 0;
			}
		}
	}
	
	public static void main(String... args){
		BFSBinary bfs = new BFSBinary();
		bfs.bfs();
	}
}
