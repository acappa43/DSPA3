package PA3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import PA3.hashtable.SimpleHashFunction;
import PA3.map.Map;


public class HuffmanCoding {

	public static String input;
	public static String output;
	public static Map<String, Integer> map;
	public static BTNode<String, Integer> root;
	public static HashTableSC<String, String> tree;
	public static int currentSize;
	

	public HuffmanCoding(String path) throws IOException {
		HuffmanCoding.input = load_data("./inputData/input2.txt");
		HuffmanCoding.map = HuffmanCoding.compute_fd(input);
		HuffmanCoding.root = huffman_tree(map);
		HuffmanCoding.tree = huffman_code(root);
		HuffmanCoding.output = encode(tree, input);
	}

	public static void main(String[] args) throws IOException{
		@SuppressWarnings("unused")
		HuffmanCoding code = new HuffmanCoding("./inputData/input2.txt");
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

	public static HashTableSC<String, String> huffman_code(BTNode<String, Integer> node){
		HashTableSC<String, String> result = new HashTableSC<String,String>(1, new SimpleHashFunction<String>());
		huffman_aux(node, new StringBuilder(), result);
		return result;
	}

	//Auxiliary method, goes into each node and adds 0 or 1 until it has reached the end
	public static void huffman_aux(BTNode<String, Integer> node, StringBuilder string, HashTableSC<String, String> result){
		if(node != null) {
			if(node.getLeft() == null && node.getRight()==null) {
				result.put(node.getText(), string.toString());
			}
			
			string.append("0");
			huffman_aux(node.getLeftChild(), string, result);
			string.deleteCharAt(string.length()-1);
			
			string.append("1");
			huffman_aux(node.getRightChild(), string, result);
			string.deleteCharAt(string.length()-1);
		}
		
	}


	/**
	 * 
	 * 
	 * Processes the table and deciphers the input String
	 */
	public static String encode(HashTableSC<String,String> table, String input) {
		String result = "";
		for(String entry : input.split("")) { // splits each input character
			for(String key: table.getKeys()) { 
				if(entry.equals(key.split(":")[0])) { // gets first position in the keys of the table variable: ex. "A:4" returns A
					result+=table.get(key);
				}
			}
		}
		return result;
	}

	
	/**
	 * This method processes all of the given parameters and returns an organized table of values of the given 
	 * 
	 * @param map = mapping of symbols with their frequencies
	 * @param tree = hashtable with each symbol and their byte code
	 * @param input = input string given from text file
	 * @param output = output string from encode method
	 */
	public static void process_results(Map<String, Integer> map, HashTableSC<String, String> tree, String input, String output) {
		System.out.println("Symbol\t" + "Frequency\t" + "Code");
		System.out.println("-----\t" + "---------\t" + "----");
		for (String entry : tree.getKeys()) {
			System.out.println(entry.split(":")[0] + "\t" + entry.split(":")[1] + "\t" + "\t" +  tree.get(entry));
		} 		
	

		System.out.println("Original string:");
		System.out.println(input);
		System.out.println("Encoded String:");
		System.out.println(output);
		double inputBit = input.getBytes().length;
		double outputBit = output.getBytes().length/8 + 1;
		int diff = (int) ((1 - outputBit/inputBit) * 100);
		System.out.println("The original string requires " +inputBit+ " bytes.");
		System.out.println("The encoded string requires " +outputBit+ " bytes.");
		System.out.println("Difference in space required is " + diff+ "%");
	}

}
