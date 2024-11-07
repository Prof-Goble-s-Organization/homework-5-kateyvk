package hw5;

import java.util.NoSuchElementException;

/**
 * Linked implementation of a binary search tree. The binary search tree
 * inherits the methods from the binary tree. The add and remove methods must
 * must be overridden so that they maintain the BST Property. The contains, get
 * and set methods are overridden to improve their performance by taking
 * advantage of the BST property. The inherited size and traversal methods work
 * well as they are.
 * 
 * @author William Goble
 * @author Dickinson College
 *
 */
public class COMP232LinkedBinarySearchTree<K extends Comparable<K>, V> extends COMP232LinkedBinaryTree<K, V> {

	/*
	 * NOTE: We inherit the size and root fields, and the BTNode class from the
	 * LinkedBinaryTree class because they were declared as protected in that
	 * class.
	 */

	/**
	 * 
	 * Construct an empty binary search tree.
	 */
	public COMP232LinkedBinarySearchTree() {
		super();
	}

	/**
	 * Construct a binary search tree with a single node at the root.
	 * 
	 * @param key
	 *            the key for the root.
	 * @param value
	 *            the value for the root.
	 */
	public COMP232LinkedBinarySearchTree(K key, V value) {
		super(key, value);
	}

	/**
	 * Construct a binary search tree using the provided keys and values. The
	 * key,value pairs are inserted into the tree in level order. If the
	 * resulting tree does not satisfy the BST Property, then an
	 * IllegalArgumentException is thrown.
	 * 
	 * @param keys
	 *            the keys.
	 * @param values
	 *            the values.
	 * @throws IllegalArgumentException
	 *             if the keys and values do not have the same length, the keys
	 *             or the values are empty, or the keys are not specified in an
	 *             order that results in a valid binary search tree.
	 */
	public COMP232LinkedBinarySearchTree(K[] keys, V[] values) {
		super(keys, values);

		if (!checkBSTProperty()) {
			throw new IllegalArgumentException(
					"Key, Value pairs did not satisfy BST property.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean contains(K key) {
		return containsHelper(root, key);
	}

	/*
	 * Recursive helper method that checks if the key can be found in the
	 * subtree rooted at subTreeRoot.
	 */
	private boolean containsHelper(BTNode<K, V> subTreeRoot, K key) {
		if (subTreeRoot == null) {
			return false; // off the tree.
		} else if (subTreeRoot.key.equals(key)) {
			return true; // found it.
		} else if (key.compareTo(subTreeRoot.key) < 0) {
			/*
			 * The key we are looking for is less than the key at the
			 * subTreeRoot so if it is in the tree it will be in the left
			 * subtree.
			 */
			return containsHelper(subTreeRoot.left, key);
		} else {
			/*
			 * The key we are looking for is greater than or equal to the key at
			 * the subTreeRoot so if it is in the tree it will be in the right
			 * subtree.
			 */
			return containsHelper(subTreeRoot.right, key);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	//After the helper method finds the node at the target key we can return the value of the node at that key
	public V get(K key) {
		BTNode<K, V> getNode = getNodeSubTree(root,key);
		if(getNode !=null) {
			return getNode.value;
		}else {
			return null;
		}
	}
	//helper method similar to the LBT helper that gets the node and use the 
	//compare logic and search tree logic to find the node 
	private BTNode<K, V> getNodeSubTree(BTNode<K, V> subTreeRoot, K key){
		if(subTreeRoot ==null) {
			return null;
		}
		
		int compareNum = key.compareTo(subTreeRoot.key);
		
		if(compareNum == 0){
			return subTreeRoot;
		}else if(compareNum <0) {
			//if the key is smaller, than root we traverse left side
			return getNodeSubTree(subTreeRoot.left, key);
		}else {
			return getNodeSubTree(subTreeRoot.right,key);
			//if the key is larger, traverse right side
		}
	}

	/**
	 * {@inheritDoc}
	 */
	//use the helper method to find the node with the matching key and then reseting the new value
	public void set(K key, V value) {
		BTNode<K,V> changeNode = getNodeSubTree(root,key);
		if(changeNode != null) {
			changeNode.value =value;
		}else {
			throw new NoSuchElementException("Key" + key + "not in BST");
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	public void add(K key, V value) {
		BTNode<K, V> nodeToAdd = new BTNode<K, V>(key, value);

		if (size == 0) { // tree is empty!
			root = nodeToAdd;
		} else {
			addNodeToSubTree(root, nodeToAdd);
		}
		size++;
	}

	/*
	 * Add the nodeToAdd to the subtree rooted at subTreeRoot.
	 */
	private void addNodeToSubTree(BTNode<K, V> subTreeRoot,
			BTNode<K, V> nodeToAdd) {
		if (nodeToAdd.key.compareTo(subTreeRoot.key) < 0) {
			/*
			 * Key of the new node is less than the key at subTreeRoot so we are
			 * going to add the new node into the left sub tree.
			 */
			if (subTreeRoot.left == null) {
				/*
				 * The left subtree is empty, so make the new node the left
				 * child of the subtree root.
				 */
				subTreeRoot.left = nodeToAdd;
				nodeToAdd.parent = subTreeRoot;
			} else {
				/*
				 * The left subtree is not empty, so add the new node to that
				 * sub tree.
				 */
				addNodeToSubTree(subTreeRoot.left, nodeToAdd);
			}
		} else {
			/*
			 * The key of the new node is greater than or equal to the key at
			 * the subTreeRoot so we are going to add the new node to the right
			 * sub tree. This is exactly the complement of the approach used
			 * above.
			 */
			if (subTreeRoot.right == null) {
				subTreeRoot.right = nodeToAdd;
				nodeToAdd.parent = subTreeRoot;
			} else {
				addNodeToSubTree(subTreeRoot.right, nodeToAdd);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public V remove(K key) {
		
		if(root==null) {
			return null;
		}
		
		
		BTNode<K,V> removeNode = getNodeSubTree(root,key);
		V removeVal = removeNode.value; //getting and storing value for node at given key
		
		
		//case 1: we are removing a leaf node. So if the left and right of node are null
		if(removeNode.left == null && removeNode.right == null) {
			//if the node we are removing is the root and the root is the only node
			if(removeNode == root) {
				root = null;
			//else the leaf is somewhere else in the tree
			} else {
				//see if the leaf we are targeting is on the left or right of parent node
				if(removeNode.parent.left == removeNode){
					removeNode.parent.left = null; //then set target node's parent's left to null
				}else {//target node is right leaf of it's parent node
					removeNode.parent.right =null;
				}
			}
			size--;//updating the size 
		}
		
		
		//case 2: remove node with 1 child(left or right)
		//if either left or right node exist we run this case
		else if(removeNode.left == null || removeNode.right == null) {
			BTNode<K,V> childNode = null;
			
			//step a check existence of left or right child
			//if the left child exists
			if (removeNode.left !=null) {
				childNode = removeNode.left;//then assign left as child node
			}
			//if the right child exists
			else if(removeNode.right != null) {
				childNode = removeNode.right;//assign right as child
			}
			//swap child and remove node
			if(removeNode == root) {
				root = childNode;
			}else {
				//checking the location of removeNode left or right of parent
				if(removeNode.parent.left == removeNode) {
					//then swapping remove node with it's child
					removeNode.parent.left = childNode;
				}else {
					removeNode.parent.right = childNode;
				}
			}
			//making sure parent of remove's child points to the correct parent, skipping over remove node
			if(childNode != null) {
				childNode.parent =removeNode.parent;
			}
			
			//updating size 
			size--;
		}
		
		//case 3: the removeNode has two children. You swap the target node 
		//with the leftmost node if the right subtree 
		else {
			//get left most node of right subtree
			BTNode<K,V> lrNode = getLeftMost(removeNode.right);
			
			removeVal = removeNode.value;
			
			//swap the values and keys of the removeNode with the leftmost node in subtree
			removeNode.key = lrNode.key;
			removeNode.value = lrNode.value;
			
			
			
			if(lrNode.parent.left == lrNode) {
				lrNode.parent.left = lrNode.right;
			}else {
				lrNode.parent.right = lrNode.right;
			}
			if(lrNode.right != null) {
				lrNode.right.parent = lrNode.parent;
			}
			
			//adjust size
			size--;
		}
		
		return removeVal;
		
	}
	//helper method to find leftmost node 
	private BTNode<K,V> getLeftMost(BTNode<K,V> node){
		//while the given node has a left node
		while(node.left != null) {
			node=node.left;//move down to that left node
		}//this only breaks if their is no more left children, meaning we found the leftmost node
		return node;
	}

	/*
	 * Helper method that verifies the BST property of this tree by traversing
	 * the tree and verifying that at each node the key of the left child is <
	 * the key of the node and that the key of the right child is >= the key of
	 * the node.  This is used by the 
	 */
	boolean checkBSTProperty() {
		return checkBSTPropertyHelper(root);
	}

	private boolean checkBSTPropertyHelper(BTNode<K, V> subTreeRoot) {
		if (subTreeRoot == null) {
			return true; // off tree.
		} else {
			if (leftChildOK(subTreeRoot) && rightChildOK(subTreeRoot)) {
				return checkBSTPropertyHelper(subTreeRoot.left)
						&& checkBSTPropertyHelper(subTreeRoot.right);
			} else {
				return false;
			}
		}
	}

	private boolean leftChildOK(BTNode<K, V> node) {
		if (node.left == null) {
			return true;
		} else {
			// true if key at node is > key at left child.
			return node.key.compareTo(node.left.key) > 0;
		}
	}

	private boolean rightChildOK(BTNode<K, V> node) {
		if (node.right == null) {
			return true;
		} else {
			// true if key at node is <= key at right child.
			return node.key.compareTo(node.right.key) <= 0;
		}
	}
	
	
	
}
