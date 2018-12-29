package Searching;
import stdlib.*;
/* ***********************************************************************
 *  Compilation:  javac RedBlackBST.java
 *  Execution:    java RedBlackBST < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/33balanced/tinyST.txt
 *
 *  A symbol table implemented using a left-leaning red-black BST.
 *  This is the 2-3 version.
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *
 *  % java RedBlackBST < tinyST.txt
 *  A 8
 *  C 4
 *  E 12
 *  H 5
 *  L 11
 *  M 9
 *  P 10
 *  R 3
 *  S 0
 *  X 7
 *
 *************************************************************************/
public class RedBlackBST<K extends Comparable<? super K>, V> {

	private static final boolean RED   = true;
	private static final boolean BLACK = false;

	private Node<K,V> root;     // root of the BST

	// BST helper node data type
	private static class Node<K,V> {
		public K key;         // key
		public V val;         // associated data
		public Node<K,V> left, right;  // links to left and right subtrees
		public boolean color;     // color of parent link
		public int N;             // subtree count

		public Node(K key, V val, boolean color, int N) {
			this.key = key;
			this.val = val;
			this.color = color;
			this.N = N;
		}
	}

	/* ***********************************************************************
	 *  Node<K,V> helper methods
	 *************************************************************************/
	// is node x red; false if x is null ?
	private boolean isRed(Node<K,V> x) {
		if (x == null) return false;
		return (x.color == RED);
	}

	// number of node in subtree rooted at x; 0 if x is null
	private int size(Node<K,V> x) {
		if (x == null) return 0;
		return x.N;
	}


	/* ***********************************************************************
	 *  Size methods
	 *************************************************************************/

	// return number of key-value pairs in this symbol table
	public int size() { return size(root); }

	// is this symbol table empty?
	public boolean isEmpty() { return root == null; }

	/* ***********************************************************************
	 *  Standard BST search
	 *************************************************************************/

	// value associated with the given key; null if no such key
	public V get(K key) { return get(root, key); }

	// value associated with the given key in subtree rooted at x; null if no such key
	private V get(Node<K,V> x, K key) {
		while (x != null) {
			int cmp = key.compareTo(x.key);
			if      (cmp < 0) x = x.left;
			else if (cmp > 0) x = x.right;
			else              return x.val;
		}
		return null;
	}

	// is there a key-value pair with the given key?
	public boolean contains(K key) { return (get(key) != null); }

	// is there a key-value pair with the given key in the subtree rooted at x?
	private boolean contains(Node<K,V> x, K key) { return (get(x, key) != null); }

	/* ***********************************************************************
	 *  Red-black insertion
	 *************************************************************************/

	// insert the key-value pair; overwrite the old value with the new value
	// if the key is already present
	public void put(K key, V val) {
		root = put(root, key, val);
		root.color = BLACK;
		assert check();
	}

	// insert the key-value pair in the subtree rooted at h
	private Node<K,V> put(Node<K,V> h, K key, V val) {
		if (h == null) return new Node<>(key, val, RED, 1);

		int cmp = key.compareTo(h.key);
		if      (cmp < 0) h.left  = put(h.left,  key, val);
		else if (cmp > 0) h.right = put(h.right, key, val);
		else              h.val   = val;

		// fix-up any right-leaning links
		if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
		if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
		if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
		h.N = size(h.left) + size(h.right) + 1;

		return h;
	}
	/* ***********************************************************************
	 *  Red-black deletion
	 *************************************************************************/

	// delete the key-value pair with the minimum key
	public void deleteMin() {
		if (isEmpty()) throw new Error("BST underflow");

		// if both children of root are black, set root to red
		if (!isRed(root.left) && !isRed(root.right))
			root.color = RED;

		root = deleteMin(root);
		if (!isEmpty()) root.color = BLACK;
		assert check();
	}

	// delete the key-value pair with the minimum key rooted at h
	private Node<K,V> deleteMin(Node<K,V> h) {
		if (h.left == null)
			return null;

		if (!isRed(h.left) && !isRed(h.left.left))
			h = moveRedLeft(h);

		h.left = deleteMin(h.left);
		return balance(h);
	}


	// delete the key-value pair with the maximum key
	public void deleteMax() {
		if (isEmpty()) throw new Error("BST underflow");

		// if both children of root are black, set root to red
		if (!isRed(root.left) && !isRed(root.right))
			root.color = RED;

		root = deleteMax(root);
		if (!isEmpty()) root.color = BLACK;
		assert check();
	}

	// delete the key-value pair with the maximum key rooted at h
	private Node<K,V> deleteMax(Node<K,V> h) {
		if (isRed(h.left))
			h = rotateRight(h);

		if (h.right == null)
			return null;

		if (!isRed(h.right) && !isRed(h.right.left))
			h = moveRedRight(h);

		h.right = deleteMax(h.right);

		return balance(h);
	}

	// delete the key-value pair with the given key
	public void delete(K key) {
		if (!contains(key)) {
			System.err.println("symbol table does not contain " + key);
			return;
		}

		// if both children of root are black, set root to red
		if (!isRed(root.left) && !isRed(root.right))
			root.color = RED;

		root = delete(root, key);
		if (!isEmpty()) root.color = BLACK;
		assert check();
	}

	// delete the key-value pair with the given key rooted at h
	private Node<K,V> delete(Node<K,V> h, K key) {
		assert contains(h, key);

		if (key.compareTo(h.key) < 0)  {
			if (!isRed(h.left) && !isRed(h.left.left))
				h = moveRedLeft(h);
			h.left = delete(h.left, key);
		}
		else {
			if (isRed(h.left))
				h = rotateRight(h);
			if (key.compareTo(h.key) == 0 && (h.right == null))
				return null;
			if (!isRed(h.right) && !isRed(h.right.left))
				h = moveRedRight(h);
			if (key.compareTo(h.key) == 0) {
				h.val = get(h.right, min(h.right).key);
				h.key = min(h.right).key;
				h.right = deleteMin(h.right);
			}
			else h.right = delete(h.right, key);
		}
		return balance(h);
	}

	/* ***********************************************************************
	 *  red-black tree helper functions
	 *************************************************************************/

	// make a left-leaning link lean to the right
	private Node<K,V> rotateRight(Node<K,V> h) {
		assert (h != null) && isRed(h.left);
		Node<K,V> x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = x.right.color;
		x.right.color = RED;
		x.N = h.N;
		h.N = size(h.left) + size(h.right) + 1;
		return x;
	}

	// make a right-leaning link lean to the left
	private Node<K,V> rotateLeft(Node<K,V> h) {
		assert (h != null) && isRed(h.right);
		Node<K,V> x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = x.left.color;
		x.left.color = RED;
		x.N = h.N;
		h.N = size(h.left) + size(h.right) + 1;
		return x;
	}

	// flip the colors of a node and its two children
	private void flipColors(Node<K,V> h) {
		// h must have opposite color of its two children
		assert (h != null) && (h.left != null) && (h.right != null);
		assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
		|| (isRed(h)  && !isRed(h.left) && !isRed(h.right));
		h.color = !h.color;
		h.left.color = !h.left.color;
		h.right.color = !h.right.color;
	}

	// Assuming that h is red and both h.left and h.left.left
	// are black, make h.left or one of its children red.
	private Node<K,V> moveRedLeft(Node<K,V> h) {
		assert (h != null);
		assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

		flipColors(h);
		if (isRed(h.right.left)) {
			h.right = rotateRight(h.right);
			h = rotateLeft(h);
			// flipColors(h);
		}
		return h;
	}

	// Assuming that h is red and both h.right and h.right.left
	// are black, make h.right or one of its children red.
	private Node<K,V> moveRedRight(Node<K,V> h) {
		assert (h != null);
		assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
		flipColors(h);
		if (isRed(h.left.left)) {
			h = rotateRight(h);
			// flipColors(h);
		}
		return h;
	}

	// restore red-black tree invariant
	private Node<K,V> balance(Node<K,V> h) {
		assert (h != null);

		if (isRed(h.right))                      h = rotateLeft(h);
		if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
		if (isRed(h.left) && isRed(h.right))     flipColors(h);

		h.N = size(h.left) + size(h.right) + 1;
		return h;
	}


	/* ***********************************************************************
	 *  Utility functions
	 *************************************************************************/

	// height of tree; 0 if empty
	public int height() { return height(root); }
	private int height(Node<K,V> x) {
		if (x == null) return 0;
		return 1 + Math.max(height(x.left), height(x.right));
	}

	/* ***********************************************************************
	 *  Ordered symbol table methods.
	 *************************************************************************/

	// the smallest key; null if no such key
	public K min() {
		if (isEmpty()) return null;
		return min(root).key;
	}

	// the smallest key in subtree rooted at x; null if no such key
	private Node<K,V> min(Node<K,V> x) {
		assert x != null;
		if (x.left == null) return x;
		else                return min(x.left);
	}

	// the largest key; null if no such key
	public K max() {
		if (isEmpty()) return null;
		return max(root).key;
	}

	// the largest key in the subtree rooted at x; null if no such key
	private Node<K,V> max(Node<K,V> x) {
		assert x != null;
		if (x.right == null) return x;
		else                 return max(x.right);
	}

	// the largest key less than or equal to the given key
	public K floor(K key) {
		Node<K,V> x = floor(root, key);
		if (x == null) return null;
		else           return x.key;
	}

	// the largest key in the subtree rooted at x less than or equal to the given key
	private Node<K,V> floor(Node<K,V> x, K key) {
		if (x == null) return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0) return x;
		if (cmp < 0)  return floor(x.left, key);
		Node<K,V> t = floor(x.right, key);
		if (t != null) return t;
		else           return x;
	}

	// the smallest key greater than or equal to the given key
	public K ceiling(K key) {
		Node<K,V> x = ceiling(root, key);
		if (x == null) return null;
		else           return x.key;
	}

	// the smallest key in the subtree rooted at x greater than or equal to the given key
	private Node<K,V> ceiling(Node<K,V> x, K key) {
		if (x == null) return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0) return x;
		if (cmp > 0)  return ceiling(x.right, key);
		Node<K,V> t = ceiling(x.left, key);
		if (t != null) return t;
		else           return x;
	}


	// the key of rank k
	public K select(int k) {
		if (k < 0 || k >= size())  return null;
		Node<K,V> x = select(root, k);
		return x.key;
	}

	// the key of rank k in the subtree rooted at x
	private Node<K,V> select(Node<K,V> x, int k) {
		assert x != null;
		assert k >= 0 && k < size(x);
		int t = size(x.left);
		if      (t > k) return select(x.left,  k);
		else if (t < k) return select(x.right, k-t-1);
		else            return x;
	}

	// number of keys less than key
	public int rank(K key) {
		return rank(key, root);
	}

	// number of keys less than key in the subtree rooted at x
	private int rank(K key, Node<K,V> x) {
		if (x == null) return 0;
		int cmp = key.compareTo(x.key);
		if      (cmp < 0) return rank(key, x.left);
		else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
		else              return size(x.left);
	}

	/* *********************************************************************
	 *  Range count and range search.
	 ***********************************************************************/

	// all of the keys, as an Iterable
	public Iterable<K> keys() {
		return keys(min(), max());
	}

	// the keys between lo and hi, as an Iterable
	public Iterable<K> keys(K lo, K hi) {
		Queue<K> queue = new Queue<>();
		// if (isEmpty() || lo.compareTo(hi) > 0) return queue;
		keys(root, queue, lo, hi);
		return queue;
	}

	// add the keys between lo and hi in the subtree rooted at x
	// to the queue
	private void keys(Node<K,V> x, Queue<K> queue, K lo, K hi) {
		if (x == null) return;
		int cmplo = lo.compareTo(x.key);
		int cmphi = hi.compareTo(x.key);
		if (cmplo < 0) keys(x.left, queue, lo, hi);
		if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key);
		if (cmphi > 0) keys(x.right, queue, lo, hi);
	}

	// number keys between lo and hi
	public int size(K lo, K hi) {
		if (lo.compareTo(hi) > 0) return 0;
		if (contains(hi)) return rank(hi) - rank(lo) + 1;
		else              return rank(hi) - rank(lo);
	}

	/* ***********************************************************************
	 *  Check integrity of red-black BST data structure
	 *************************************************************************/
	private boolean check() {
		if (!isBST())            StdOut.format("Not in symmetric order: %s\n", this);
		if (!isSizeConsistent()) StdOut.format("Subtree counts not consistent: %s\n", this);
		if (!isRankConsistent()) StdOut.format("Ranks not consistent: %s\n", this);
		if (!is23())             StdOut.format("Not a 2-3 tree: %s\n", this);
		if (!isBalanced())       StdOut.format("Not balanced: %s\n", this);
		return isBST() && isSizeConsistent() && isRankConsistent() && is23() && isBalanced();
	}

	// does this binary tree satisfy symmetric order?
	// Note: this test also ensures that data structure is a binary tree since order is strict
	private boolean isBST() {
		return isBST(root, null, null);
	}

	// is the tree rooted at x a BST with all keys strictly between min and max
	// (if min or max is null, treat as empty constraint)
	// Credit: Bob Dondero's elegant solution
	private boolean isBST(Node<K,V> x, K min, K max) {
		if (x == null) return true;
		if (min != null && x.key.compareTo(min) <= 0) return false;
		if (max != null && x.key.compareTo(max) >= 0) return false;
		return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
	}

	// are the size fields correct?
	private boolean isSizeConsistent() { return isSizeConsistent(root); }
	private boolean isSizeConsistent(Node<K,V> x) {
		if (x == null) return true;
		if (x.N != size(x.left) + size(x.right) + 1) return false;
		return isSizeConsistent(x.left) && isSizeConsistent(x.right);
	}

	// check that ranks are consistent
	private boolean isRankConsistent() {
		for (int i = 0; i < size(); i++)
			if (i != rank(select(i))) return false;
		for (K key : keys())
			if (key.compareTo(select(rank(key))) != 0) return false;
		return true;
	}

	// Does the tree have no red right links, and at most one (left)
	// red links in a row on any path?
	private boolean is23() { return is23(root); }
	private boolean is23(Node<K,V> x) {
		if (x == null) return true;
		if (isRed(x.right)) return false;
		if (x != root && isRed(x) && isRed(x.left))
			return false;
		return is23(x.left) && is23(x.right);
	}

	// do all paths from root to leaf have same number of black edges?
	private boolean isBalanced() {
		int black = 0;     // number of black links on path from root to min
		Node<K,V> x = root;
		while (x != null) {
			if (!isRed(x)) black++;
			x = x.left;
		}
		return isBalanced(root, black);
	}

	// does every path from the root to a leaf have the given number of black links?
	private boolean isBalanced(Node<K,V> x, int black) {
		if (x == null) return black == 0;
		if (!isRed(x)) black--;
		return isBalanced(x.left, black) && isBalanced(x.right, black);
	}

	/* ***************************************************************************
	 *  Visualization
	 *****************************************************************************/
	private Iterable<Node<K,V>> levelOrderNodes() {
		Queue<Node<K,V>> keys = new Queue<>();
		Queue<Node<K,V>> queue = new Queue<>();
		queue.enqueue(root);
		while (!queue.isEmpty()) {
			Node<K,V> x = queue.dequeue();
			if (x == null) continue;
			keys.enqueue(x);
			queue.enqueue(x.left);
			queue.enqueue(x.right);
		}
		return keys;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Node<K,V> n: levelOrderNodes())
			sb.append (n.key + (n.color ? "* " : " "));
		return sb.toString ();
	}

	public void toGraphviz(String filename) {
		GraphvizBuilder gb = new GraphvizBuilder ();
		toGraphviz (gb, null, root);
		gb.toFileUndirected (filename, "ordering=\"out\"");
	}
	private void toGraphviz (GraphvizBuilder gb, Node<K, V> parent, Node<K, V> n) {
		if (n == null) { gb.addNullEdge (parent); return; }
		String nodeProperties = n.color ? "color=\"red\"" : "";
		String edgeProperties = n.color ? "color=\"red\",style=\"bold\"" : "";
		gb.addLabeledNode (n, n.key.toString (), nodeProperties);
		if (parent != null) gb.addEdge (parent, n, edgeProperties);
		toGraphviz (gb, n, n.left);
		toGraphviz (gb, n, n.right);
	}

	public void drawTree() {
		if (root != null) {
			StdDraw.setCanvasSize(1200,700);
			drawTree(root, .5, 1, .25, 0);
		}
	}
	private void drawTree (Node<K,V> n, double x, double y, double range, int depth) {
		int CUTOFF = 5;
		StdDraw.setPenColor (StdDraw.BLACK);
		StdDraw.text (x, y, n.key.toString ());
		StdDraw.setPenRadius (.005);
		if (n.left != null && depth != CUTOFF) {
			if (n.left.color == RED) {
				StdDraw.setPenRadius (.01);
				StdDraw.setPenColor (StdDraw.RED);
			}
			StdDraw.line (x-range, y-.13, x-.01, y-.01);
			drawTree (n.left, x-range, y-.15, range*.5, depth+1);
		}
		if (n.right != null && depth != CUTOFF) {
			StdDraw.line (x+range, y-.13, x+.01, y-.01);
			drawTree (n.right, x+range, y-.15, range*.5, depth+1);
		}
	}
	/* ***************************************************************************
	 *  Test client
	 *****************************************************************************/
	public static void main(String[] args) {
		StdIn.fromString ("S E A R C H E X A M P L E");
		//StdIn.fromString ("D F B  G E A C");

		RedBlackBST<String, Integer> st = new RedBlackBST<>();
		for (int i = 0; !StdIn.isEmpty(); i++) {
			String key = StdIn.readString();
			st.put(key, i);
		}
		st.toGraphviz ("g.png");
		for (String s : st.keys())
			StdOut.println(s + " " + st.get(s));
		st.drawTree ();
	}
}
