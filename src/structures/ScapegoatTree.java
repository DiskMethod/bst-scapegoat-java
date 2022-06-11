package structures;

import java.util.Stack;

public class ScapegoatTree<T extends Comparable<T>> extends
		BinarySearchTree<T> {
	private int upperBound = 0;

	@Override
	public void add(T t) {
		upperBound++;
		Stack<BSTNode<T>> visitedNodes = new Stack<BSTNode<T>>();
		
		//Add element according to bst rules
		BSTNode<T> temp = root;
		if(root == null) {
			root = new BSTNode<T>(t, null, null);
		} else {
			BSTNode<T> prev = temp;
			boolean left = false;
			while(temp != null) {
				visitedNodes.push(temp);
				prev = temp;
				left = false;
				if(t.compareTo(temp.getData()) <= 0) {
					temp = temp.getLeft();
					left = true;
				} else {
					temp = temp.getRight();
				}
			}
			if(left) {
				prev.setLeft(new BSTNode<T>(t, null, null));
			} else {
				prev.setRight(new BSTNode<T>(t, null, null));
			}
		}
		
		//condition 1
		if(!(height() <= (Math.log((double) upperBound)/Math.log(3.0/2.0)))) {
			BSTNode<T> scapeGoat = null;
			while(!visitedNodes.isEmpty()) {
				BSTNode<T> child = visitedNodes.pop();
				BSTNode<T> tempScapeGoat = visitedNodes.peek();
				if(((double) subtreeSize(child)/subtreeSize(tempScapeGoat)) > 2.0/3.0) {
					visitedNodes.pop();
					scapeGoat = tempScapeGoat;
					break;
				}
			}
			scapeGoat = balanceAtNode(scapeGoat);
			if(!visitedNodes.isEmpty()) {
				BSTNode<T> parent = visitedNodes.pop();
				if(scapeGoat.getData().compareTo(parent.getData()) <= 0) {
					parent.setLeft(scapeGoat);
				} else {
					parent.setRight(scapeGoat);
				}
			} else {
				root = scapeGoat;
			}
		}
		
		//condition 2
		if(((double) upperBound/2.0 <= size() && size() <= (double) upperBound/2.0)) {
			balance();
		}
	}

	@Override
	public boolean remove(T element) throws NullPointerException {
		if (element == null) {
			throw new NullPointerException();
		}
		boolean result = contains(element);
		if (result) {
			root = removeFromSubtree(root, element);
		}
		
		//condition
		if(upperBound > 2 * size()) {
			balance();
			upperBound = size();
		}
		return result;
	}
}
