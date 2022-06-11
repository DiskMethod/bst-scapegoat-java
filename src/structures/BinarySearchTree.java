package structures;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BinarySearchTree<T extends Comparable<T>> implements
		BSTInterface<T> {
	protected BSTNode<T> root;
	
	public BinarySearchTree() {
		root = null;
	}
	
	public boolean isEmpty() {
		return root == null;
	}

	public int size() {
		return subtreeSize(root);
	}

	protected int subtreeSize(BSTNode<T> node) {
		if (node == null) {
			return 0;
		} else {
			return 1 + subtreeSize(node.getLeft())
					+ subtreeSize(node.getRight());
		}
	}

	public boolean contains(T t) throws NullPointerException {
		if(t == null) {
			throw new NullPointerException();
		}
		
		if(get(t) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean remove(T t) throws NullPointerException {
		if (t == null) {
			throw new NullPointerException();
		}
		boolean result = contains(t);
		if (result) {
			root = removeFromSubtree(root, t);
		}
		return result;
	}

	protected BSTNode<T> removeFromSubtree(BSTNode<T> node, T t) {
		// node must not be null
		int result = t.compareTo(node.getData());
		if (result < 0) {
			node.setLeft(removeFromSubtree(node.getLeft(), t));
			return node;
		} else if (result > 0) {
			node.setRight(removeFromSubtree(node.getRight(), t));
			return node;
		} else { // result == 0
			if (node.getLeft() == null) {
				return node.getRight();
			} else if (node.getRight() == null) {
				return node.getLeft();
			} else { // neither child is null
				T predecessorValue = getHighestValue(node.getLeft());
				node.setLeft(removeRightmost(node.getLeft()));
				node.setData(predecessorValue);
				return node;
			}
		}
	}

	private T getHighestValue(BSTNode<T> node) {
		// node must not be null
		if (node.getRight() == null) {
			return node.getData();
		} else {
			return getHighestValue(node.getRight());
		}
	}

	private BSTNode<T> removeRightmost(BSTNode<T> node) {
		// node must not be null
		if (node.getRight() == null) {
			return node.getLeft();
		} else {
			node.setRight(removeRightmost(node.getRight()));
			return node;
		}
	}

	public T get(T t) throws NullPointerException {
		if(t == null) {
			throw new NullPointerException();
		}
		if(isEmpty()) {
			return null;
		}
		return get(t , root);
	}
	
	private T get(T t, BSTNode<T> node) {
		T t1 = null;
		T t2 = null;
		
		if(node.getData().equals(t)) {
			return node.getData();
		} else {
			if(node.getLeft() != null) {
				t1 = get(t, node.getLeft());
			}
			if(node.getRight() != null) {
				t2 = get(t, node.getRight());
			}
		}
		
		if(t1 != null) {
			return t1;
		} else if (t2 != null) {
			return t2;
		} else {
			return null;
		}
	}


	public void add(T t) throws NullPointerException {
		if (t == null) {
			throw new NullPointerException();
		}
		root = addToSubtree(root, new BSTNode<T>(t, null, null));
	}

	protected BSTNode<T> addToSubtree(BSTNode<T> node, BSTNode<T> toAdd) {
		if (node == null) {
			return toAdd;
		}
		int result = toAdd.getData().compareTo(node.getData());
		if (result <= 0) {
			node.setLeft(addToSubtree(node.getLeft(), toAdd));
		} else {
			node.setRight(addToSubtree(node.getRight(), toAdd));
		}
		return node;
	}

	@Override
	public T getMinimum() {
		if(isEmpty()) {
			return null;
		}
		return getMinimum(root);
	}
	
	private T getMinimum(BSTNode<T> node) {
		if (node.getLeft() == null) {
			return node.getData();
		} else {
			return getMinimum(node.getLeft());
		}
	}


	@Override
	public T getMaximum() {
		if(isEmpty()) {
			return null;
		}
		return getHighestValue(root);
	}


	@Override
	public int height() {
		if(isEmpty()) {
			return -1;
		}
		return 1 + height(root, -1);
	}
	
	private int height(BSTNode<T> node, int i) {
		int left = 0;
		int right = 0;
		
		if(node.getLeft() == null && node.getRight() == null) {
			return i;
		} else {
			if(node.getLeft() != null) {
				left = height(node.getLeft(), i + 1);
			}
			if(node.getRight() != null) {
				right = height(node.getRight(), i + 1);
			}
		}
		return maxInt(left, right);
	}


	private int maxInt(int left, int right) {
		if(left > right) {
			return left;
		} else {
			return right;
		}
	}

	public Iterator<T> preorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		preorderTraverse(queue, root);
		return queue.iterator();
	}


	private void preorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			queue.add(node.getData());
			preorderTraverse(queue, node.getLeft());
			preorderTraverse(queue, node.getRight());
		}
	}

	public Iterator<T> inorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		inorderTraverse(queue, root);
		return queue.iterator();
	}
	
	public Iterator<T> inorderIteratorAtNode(BSTNode<T> node) {
		Queue<T> queue = new LinkedList<T>();
		inorderTraverse(queue, node);
		return queue.iterator();
	}


	private void inorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			inorderTraverse(queue, node.getLeft());
			queue.add(node.getData());
			inorderTraverse(queue, node.getRight());
		}
	}

	public Iterator<T> postorderIterator() {
		Queue<T> queue = new LinkedList<T>();
		postorderTraverse(queue, root);
		return queue.iterator();
	}


	private void postorderTraverse(Queue<T> queue, BSTNode<T> node) {
		if (node != null) {
			postorderTraverse(queue, node.getLeft());
			postorderTraverse(queue, node.getRight());
			queue.add(node.getData());
		}
	}

	@Override
	public boolean equals(BSTInterface<T> other) throws NullPointerException {
		if(other == null) {
			throw new NullPointerException();
		}
		
		if(this.size() != other.size() || this.height() != other.height()) {
			return false;
		}
		
		ArrayList<T> thisArrayList = new ArrayList<T>();
		ArrayList<T> otherArrayList = new ArrayList<T>();
		
		for(Iterator<T> itr = this.preorderIterator(); itr.hasNext();) {
			thisArrayList.add(itr.next());
		}
		for(Iterator<T> itr = other.preorderIterator(); itr.hasNext();) {
			otherArrayList.add(itr.next());
		}
		
		for(int i = 0; i < this.size(); i++) {
			if(!thisArrayList.get(i).equals(otherArrayList.get(i))) {
				return false;
			}
		}
		return true;
	}


	@Override
	public boolean sameValues(BSTInterface<T> other) throws NullPointerException {
		if(other == null) {
			throw new NullPointerException();
		}
		
		
		Set<T> thisSet = new HashSet<T>();
		Set<T> otherSet = new HashSet<T>();
		
		for(Iterator<T> itr = this.inorderIterator(); itr.hasNext();) {
			thisSet.add(itr.next());
		}
		for(Iterator<T> itr = other.inorderIterator(); itr.hasNext();) {
			otherSet.add(itr.next());
		}
		
		if(thisSet.size() == 0 && otherSet.size() != 0) {
			return false;
		} else if(thisSet.size() != 0 && otherSet.size() == 0) {
			return false;
		}
		
		thisSet.removeAll(otherSet);
		
		if(thisSet.size() == 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean isBalanced() {
		if(Math.pow(2, height()) <= size() && size() < Math.pow(2, height() + 1)) {
			return true;
		}
		return false;
	}
	
	@Override
    @SuppressWarnings("unchecked")

	
	public void balance() {
		thisList = new ArrayList<T>();
		for(Iterator<T> itr = this.inorderIterator(); itr.hasNext();) {
			thisList.add(itr.next());
		}
		root = sortedArray2BST(0, thisList.size() - 1);
	}
	
	public BSTNode<T> balanceAtNode(BSTNode<T> node) {
		thisList = new ArrayList<T>();
		for(Iterator<T> itr = this.inorderIteratorAtNode(node); itr.hasNext();) {
			thisList.add(itr.next());
		}
		return sortedArray2BST(0, thisList.size() - 1);
	}

	protected ArrayList<T> thisList;
	
	private BSTNode<T> sortedArray2BST(int lower, int upper) {
		if (lower > upper) {
			return null;
		}
		int mid = (lower + upper) / 2;
		BSTNode<T> node = new BSTNode<T>(thisList.get(mid), sortedArray2BST(lower, mid - 1), sortedArray2BST(mid + 1, upper));
		return node;
	}

	@Override
	public BSTNode<T> getRoot() {
        // DO NOT MODIFY
		return root;
	}

	public static <T extends Comparable<T>> String toDotFormat(BSTNode<T> root) {
		// header
		int count = 0;
		String dot = "digraph G { \n";
		dot += "graph [ordering=\"out\"]; \n";
		// iterative traversal
		Queue<BSTNode<T>> queue = new LinkedList<BSTNode<T>>();
		queue.add(root);
		BSTNode<T> cursor;
		while (!queue.isEmpty()) {
			cursor = queue.remove();
			if (cursor.getLeft() != null) {
				// add edge from cursor to left child
				dot += cursor.getData().toString() + " -> "
						+ cursor.getLeft().getData().toString() + ";\n";
				queue.add(cursor.getLeft());
			} else {
				// add dummy node
				dot += "node" + count + " [shape=point];\n";
				dot += cursor.getData().toString() + " -> " + "node" + count
						+ ";\n";
				count++;
			}
			if (cursor.getRight() != null) {
				// add edge from cursor to right child
				dot += cursor.getData().toString() + " -> "
						+ cursor.getRight().getData().toString() + ";\n";
				queue.add(cursor.getRight());
			} else {
				// add dummy node
				dot += "node" + count + " [shape=point];\n";
				dot += cursor.getData().toString() + " -> " + "node" + count
						+ ";\n";
				count++;
			}

		}
		dot += "};";
		return dot;
	}

	public static void main(String[] args) {
		for (String r : new String[] { "a", "b", "c", "d", "e", "f", "g" }) {
			BSTInterface<String> tree = new BinarySearchTree<String>();
			for (String s : new String[] { "d", "b", "a", "c", "f", "e", "g" }) {
				tree.add(s);
			}
			Iterator<String> iterator = tree.inorderIterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next());
			}
			System.out.println();
			iterator = tree.preorderIterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next());
			}
			System.out.println();
			iterator = tree.postorderIterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next());
			}
			System.out.println();

			System.out.println(tree.remove(r));

			iterator = tree.inorderIterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next());
			}
			System.out.println();
		}

		BSTInterface<String> tree = new BinarySearchTree<String>();
		for (String r : new String[] { "a", "b", "c", "d", "e", "f", "g" }) {
			tree.add(r);
		}
		System.out.println(tree.size());
		System.out.println(tree.height());
		System.out.println(tree.isBalanced());
		tree.balance();
		System.out.println(tree.size());
		System.out.println(tree.height());
		System.out.println(tree.isBalanced());
	}
}