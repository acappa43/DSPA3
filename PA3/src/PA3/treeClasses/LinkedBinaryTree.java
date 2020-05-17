package PA3.treeClasses;

import PA3.treeInterfaces.Position;

public class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {
	// class Node<E> is included at the end of this class

	private Node<E> root;   // the root of the tree
	int size;               // the size of the tree

	public LinkedBinaryTree() { 
		root = null; 
		size = 0; 
	}

	private Node<E> validate(Position<E> p) throws IllegalArgumentException { 
		if (!(p instanceof Node<?>)) 
			throw new IllegalArgumentException("Position is not of right data type."); 

		Node<E> ptn = (Node<E>) p; 
		if (ptn.getParent() == p) 
			throw new IllegalArgumentException("Invalid position --- not a tree position."); 

		return ptn; 
	}

	@Override
	public Position<E> left(Position<E> p) throws IllegalArgumentException {
		Node<E> ptn = validate(p); 
		return ptn.getLeft();
	}

	@Override
	public Position<E> right(Position<E> p) throws IllegalArgumentException {
		Node<E> ptn = validate(p); 
		return ptn.getRight();
	}

	@Override
	public Position<E> root() {
		return root;
	}

	@Override
	public Position<E> parent(Position<E> p) throws IllegalArgumentException {
		Node<E> ptn = validate(p); 
		return ptn.getParent();
	}

	@Override
	public int size() {
		return size;
	}


	////////////////////////////////////////////////////////////////////////////////
	// OTHER methods: addRoot, addLeft, addRight, attach, and remove
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a root for an empty tree, storing the specified element. The tree must
	 * be empty for this operation to be valid. 
	 * @param e reference to the element to be added
	 * @return position where the element is placed, which also becomes the root 
	 * of the tree. 
	 * @throws IllegalStateException throws this exception if the tree is not empty. 
	 * Such operation is not valid on a non empty tree.
	 */
	public Position<E> addRoot(E e) throws IllegalStateException { 
		if (!this.isEmpty()) 
			throw new IllegalStateException("Non-empty tree: Can not add a root to a non-empty tree."); 
		root = createNode(e, null, null, null); 
		size = 1; 
		return root; 
	}

	/**
	 * Adds a new element as the element of the new position to be added as the left 
	 * child of another given position in the tree. 
	 * @param p The position whose left child will be the new position containing the 
	 * specified element. 
	 * @param e The new element to be placed in that new position. 
	 * @return The new position that is created. 
	 * @throws IllegalArgumentException If p is not valid pas per the validate method, 
	 * of if p already has a left child in the tree. 
	 */
	public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException { 
		Node<E> np = validate(p); 
		if (np.getLeft() != null) 
			throw new IllegalArgumentException("Given position already has left child."); 
		Node<E> newNode = createNode(e, np, null, null); 
		np.setLeft(newNode); 
		size++; 
		return newNode;
	}

	/**
	 * Same as the above, but to add a right child of p, which shall containg element e. 
	 * @param p
	 * @param e
	 * @return
	 * @throws IllegalArgumentException
	 */
	public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException { 
		Node<E> np = validate(p); 
		if (np.getRight() != null) 
			throw new IllegalArgumentException("Given position already has right child."); 
		Node<E> newNode = createNode(e, np, null, null); 
		np.setRight(newNode); 
		size++; 
		return newNode;
	}

	/**
	 * Attaches the internal structure of two specified trees as the left and right 
	 * subtrees of a given external node in this binary tree. The two subtrees to be
	 * added are supposed to be independent from the tree where they are to be inserted. 
	 * At the end, both trees to be added will be altered so as to become empty trees.
	 * Note that, if successful, the size of this tree should be incremented by adding
	 * the sizes of the original trees that are attached. 
	 * @param p The external node to which the specified subtrees will be attached. 
	 * @param t1 The binary tree to be inserted as the left subtree of p. 
	 * @param t2 The binary tree to be inserted as the right subtree of p.
	 * @throws IllegalArgumentException if p is not a valid position or if p is not
	 * an external method. 
	 */
	public void Attach(Position<E> p, LinkedBinaryTree<E> t1, LinkedBinaryTree<E> t2) 
			throws IllegalArgumentException
	{ 
		Node<E> np = validate(p); 
		if (isInternal(np))
			throw new IllegalArgumentException("Position is not an external node in the tree."); 
		size += t1.size + t2.size;   // determine new size of this tree
		if (!t1.isEmpty()) { 
			np.setLeft(t1.root);
			t1.root.setParent(np); 

			// make t1 empty
			t1.root = null; 
			t1.size = 0; 
		}
		if (!t2.isEmpty()) { 
			np.setRight(t2.root);
			t2.root.setParent(np); 

			// make t2 empty
			t2.root = null; 
			t2.size = 0; 
		}
	}

	/**
	 * Removes the node at a given position. Replaces it with its current child
	 * (if any). 
	 * @param p The position to be removed (or corresponding to the node to be 
	 * removed. 
	 * @return The element at the removed position. 
	 * @throws IllegalArgumentException if p is not a valid position or if p has
	 * two children. This remove is not valid for nodes having two children. 
	 */
	public E remove(Position<E> p) throws IllegalArgumentException { 
		Node<E> ntd = validate(p); 
		if (numChildren(ntd) == 2)
			throw new IllegalArgumentException("Position to delete has two children.");

		// if execution reaches this point, then p is a valid position for this 
		// remove operation. 
		E etr = ntd.getElement();   // element to return - current value of p

		// Assign to variable child the reference to the only child of p, if any; 
		// otherwise, make it null. 
		Node<E> child = (ntd.getLeft() == null ? ntd.getRight() : ntd.getLeft()); 
		Node<E> parent = ntd.getParent();   // parent of position to remove.
		if (parent == null)   // if the p is the root, then its child becomes the new root.
			root = child; 
		else if (parent.getLeft() == ntd)   // p is the left child of its parent
			parent.setLeft(child);        // then child becomes left child of parent of p
		else 
			parent.setRight(child);       // ow, child becomes right child of parent of p
		if (child != null)
			child.setParent(parent);      // if child exists, set its parent to parent of p
		size--; 

		// discard deleted node
		ntd.discard();          // or clean...

		return etr; 
	}

	/**
	 * SEE ALSO METHOD set in textbook. Work an implementation on your own, but not required
	 * for this lab. 
	 */

	////////////////////////////////////////////////////////
	// Inner class Node<E> and method to create new node  //
	////////////////////////////////////////////////////////	
	/**
	 * Inner class Node<E>
	 * @author pedroirivera-vega
	 *
	 * @param <E> Data type of element in Node
	 */
	@SuppressWarnings("unused")
	private static class Node<E> implements Position<E> { 
		private E element; 
		private Node<E> parent, left, right; 
		public Node() {}
		public Node(E element, Node<E> parent, Node<E> left, Node<E> right) { 
			this.element = element; 
			this.parent = parent; 
			this.left = left; 
			this.right = right; 
		}
		public E getElement() { 
			return element; 
		}
		public Node<E> getParent() {
			return parent;
		}
		public void setParent(Node<E> parent) {
			this.parent = parent;
		}
		public Node<E> getLeft() {
			return left;
		}
		public void setLeft(Node<E> left) {
			this.left = left;
		}
		public Node<E> getRight() {
			return right;
		}
		public void setRight(Node<E> right) {
			this.right = right;
		}
		public void setElement(E element) {
			this.element = element;
		}

		public void discard() { 
			element = null; 
			left = right = null;
			parent = this; 
		}

	} // end Node<E>

	/**
	 * Method to create a new Node
	 * @param e the element in the new node
	 * @param p the parent of the new node
	 * @param l the left child of the new node
	 * @param r the right child of the new node
	 * @return
	 */
	protected Node<E> createNode(E e, Node<E> p, Node<E> l, Node<E> r) { 
		return new Node<E>(e, p, l, r); 
	}
	public LinkedBinaryTree<E> clone() throws CloneNotSupportedException { 
		LinkedBinaryTree<E> other = new LinkedBinaryTree<>(); 
		if (!isEmpty())
			other.addRoot(root().getElement()); 
		cloneSubtree(root(), other, other.root()); 

		return other; 
	}



	private void cloneSubtree(Position<E> rThis, LinkedBinaryTree<E> other, Position<E> rOther) {
		// TODO Auto-generated method stub
		for (Position<E> pThis : children(rThis)) {
			Position<E> top = this.parent(pThis);
			Position<E> pOther = null;
			if(this.hasLeft(top) && this.left(top).equals(pThis))
				pOther = other.addLeft(rOther, pThis.getElement());
			else
				pOther = other.addRight(rOther, pThis.getElement());
			cloneSubtree(pThis, other, pOther); 
		}
	}
}
