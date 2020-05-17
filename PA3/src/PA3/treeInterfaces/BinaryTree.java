package PA3.treeInterfaces;

/**
 * BinaryTree ADT
 * 
 * @author pedroirivera-vega
 *
 * @param <E> Generic type of the elements to be stored in the nodes
 */
public interface BinaryTree<E> extends Tree<E> {
	boolean hasLeft(Position<E> p) throws IllegalArgumentException; 
	boolean hasRight(Position<E> p) throws IllegalArgumentException; 
	Position<E> left(Position<E> p) throws IllegalArgumentException; 
	Position<E> right(Position<E> p) throws IllegalArgumentException; 
	Position<E> sibling(Position<E> p) throws IllegalArgumentException; 
}