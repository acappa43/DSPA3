package PA3.treeClasses;

import PA3.treeInterfaces.Position;

public class LinkedBinaryTree2<E extends Comparable<E>> extends LinkedBinaryTree<E> {
	
	public void insert(E e) { 
		if (isEmpty())
			addRoot(e); 
		else
			recInsert(root(), e); 
	}

	private void recInsert(Position<E> r, E e) {
		int c = e.compareTo(r.getElement()); 
		if (c < 0) 
			if (!hasLeft(r))
				addLeft(r, e); 
			else 
				recInsert(left(r), e); 
		else 
			if (!hasRight(r))
				addRight(r, e); 
			else 
				recInsert(right(r), e); 		
	}

}