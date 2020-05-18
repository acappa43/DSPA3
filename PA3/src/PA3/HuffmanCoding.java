package PA3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import PA3.hashtable.SimpleHashFunction;
import PA3.list.ArrayList;
import PA3.map.Map;


public class HuffmanCoding<K, V> {

	public static String input;
	public static String output;
	public static Map<String, Integer> map;
	public static BTNode<String, Integer> root;
	public static HashTableSC<String, Integer> tree;
	public static int currentSize;

	public HuffmanCoding(String path) throws IOException {
		HuffmanCoding.input = load_data("./inputData/input2.txt");
		HuffmanCoding.map = HuffmanCoding.compute_fd(input);
		HuffmanCoding.root = huffman_tree(map);
		HuffmanCoding.tree = huffman_code(root);
		HuffmanCoding.output = encode(tree, input);
	}

	public static void main(String[] args) throws IOException{
		input = load_data("./inputData/input2.txt");
		map = HuffmanCoding.compute_fd(input);
		root = huffman_tree(map);
		tree = huffman_code(root);
		output = encode(tree, input);
		process_results(map, tree, input, output);

	}


	// Receives a path to convert text file into 
	public static String load_data(String str) throws IOException {
		String line = new String(Files.readAllBytes(Paths.get(str)),StandardCharsets.UTF_8);
		return line;
	}

	/*
	 * Compute_FD is similar to our second project: receives a string as a 
	 * parameter and finds the frequency of the 
	 * characters in the input
	 */
	public static Map<String, Integer> compute_fd(String str){
		Map<String, Integer> results = new HashTableSC<String, Integer>(11, new SimpleHashFunction<String>());
		for (String key : str.split("")) {
			if(!results.containsKey(key.toString())) {
				results.put(key.toString(), 1);
			}

			else 
				results.put(key.toString(), results.get(key) + 1);
		}
		return results;
	}



	/*
	 * implementation of the tree, which traverses the arrayList, adding the last two values 
	 * and adding up their key and values, ultimately converting them 
	 * into a single node, with their frequencies and individual characters,
	 * returning the tree node. We use this to create the hashtable in the 
	 * next method below.
	 */
	public static BTNode<String ,Integer> huffman_tree(Map<String,Integer> map){
		//sort method added to ArrayList with P2 code
		SortedArrayList<BTNode<String, Integer>> sortedList = 
				new SortedArrayList<BTNode<String, Integer>>(11);
		for(String e : map.getKeys()) {
			sortedList.add(new BTNode<String, Integer>(e, map.get(e)));
		}
		for(int i=0; i<sortedList.size(); i++) {
			System.out.println(sortedList.get(i).getKey()+" "+sortedList.get(i).getValue());
		}
		int size = sortedList.size();
		//from P3 specifications pseudocode:
		for (int j = 1; j < size; j++) {
			BTNode<String, Integer> newNode = new BTNode<String, Integer>();
			BTNode<String, Integer> x = sortedList.get(sortedList.currentSize-1);
			sortedList.removeIndex(sortedList.currentSize-1);
			BTNode<String, Integer> y = sortedList.get(sortedList.currentSize-1);
			sortedList.removeIndex(sortedList.currentSize-1);
			if(x.getValue()==y.getValue() && x.getKey().compareTo(y.getKey()) >0) {
				newNode.setRightChild(x);
				newNode.setLeftChild(y);
			}else {
				newNode.setRightChild(y);
				newNode.setLeftChild(x);
			}
			newNode.setValue(x.getValue()+y.getValue());
			newNode.setKey(newNode.getLeftChild().getKey()+newNode.getRightChild().getKey());
			sortedList.add(newNode);
		}return sortedList.get(0);
	}
	/*
	 * Recursively goes into each node of the root: when traversing the right or left, we will add a value 
	 * string of 0 or 1. We start by going into the left child which is the minimal, all the way into the rightest child,
	 * the last character of the tree. Returns a key-value reference between the letter and the binary code.
	 */

	public static HashTableSC<String, Integer> huffman_code(BTNode<String, Integer> node){
		HashTableSC<String, Integer> result = new HashTableSC<String,Integer>(1, new SimpleHashFunction<String>());
		huffman_aux(node.getLeftChild(), "0", result);
		huffman_aux(node.getRightChild(), "1", result);
		return result;
	}

	//Auxiliary method, goes into each node and adds 0 or 1 until it has reached the end
	public static void huffman_aux(BTNode<String, Integer> node, String num, HashTableSC<String, Integer>result){
		if(node == null) {
			return;
		}
		if(node.getLeft() == null && node.getRight()==null) {
			result.put(node.getText(), Integer.parseInt(num));
		}
		huffman_aux(node.getLeftChild(), num.toString() + 0, result);
		huffman_aux(node.getRightChild(), num.toString() + 1, result);
	}


	/*
	 * Processes the table and deciphers the input String
	 */
	public static String encode(HashTableSC<String,Integer> table, String input) {
		String result = "";
		for(String entry : input.split("")) {
			for(String key: table.getKeys()) {
				if(entry.equals(key.split(":")[0])) {
					result+=table.get(key);
				}
			}
		}
		return result;
	}

	public static void process_results(Map<String, Integer> map, HashTableSC<String,Integer> table, String input, String output) {
		System.out.println("Symbol\t" + "Frequency\t" + "Code");
		System.out.println("-----\t" + "---------\t" + "----");
		for (String entry : table.getKeys()) {
			System.out.println(entry.split(":")[0] + "\t" + entry.split(":")[1] + "\t" + "\t" +  table.get(entry));
		} 		
		System.out.println("Original string:");
		System.out.println(input);
		System.out.println("Encoded String:");
		System.out.println(output);
		System.out.println("The original string requires " +input.getBytes().length+ " bytes.");
		System.out.println("The encoded string requires " +output.getBytes().length+ " bytes.");
		System.out.println("Difference in space required is " +Math.floor(output.length()/input.getBytes().length*100)+ "%");
	}

	//helper method that sorted map into an arraylist with nodes
	public static ArrayList<BTNode<String, Integer>> sort(Map<String, Integer> map){
		ArrayList<BTNode<String ,Integer>> list = new ArrayList<BTNode<String ,Integer>>(1);
		for(String e : map.getKeys()) {
			int i;
			for (i=0; i<list.size() && list.get(i).getValue().compareTo(map.get(e)) > 0; i++);
			list.add(i, (new BTNode<String, Integer>(e, map.get(e))));
		}
		return list;
	}


}
