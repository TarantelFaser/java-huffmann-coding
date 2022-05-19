package huffmancoding;

import java.util.LinkedList;
import java.util.List;

public class HuffmanCoding implements huffmann {

    private Node root = null;
    private List<List<String>> codesList = new LinkedList<List<String>>();

    @Override
    public void set_alphabet(char[] alphabet, float[] probabilities) {
        List<Node> liste = new LinkedList<Node>();

        float tempBigProb = 0.0f;
        int tempBigInd = 0;

        //create a sorted list (smallest probabilities at the end)
        for (int j = 0; j < alphabet.length; j++){
            tempBigProb = 0.0f;
            tempBigInd = 0;
            //search biggest probability in the array
            for (int i = 0; i < alphabet.length; i++) {
                if (probabilities[i] > tempBigProb) {
                    tempBigProb = probabilities[i];
                    tempBigInd = i;
                }
            }
            probabilities[tempBigInd] = 0.0f; //to be sure the same index won't be picked twice
            liste.add(new Node(alphabet[tempBigInd], tempBigProb));
        }

        printListToConsole(liste);

        //build the tree
        while (liste.size() > 1) {
            char newChar = ' ';
            //get the last two elements of the sorted list
            Node lastElement = liste.remove(liste.size()-1);
            Node slastElement = liste.remove(liste.size()-1);

            //sort both elements by the smallest probability / the order in which the chars appear in the alphabet
            Node leftChild = null;
            Node rightChild = null;
            if (lastElement.probability < slastElement.probability) {
                leftChild = lastElement;
                rightChild = slastElement;
                newChar = leftChild.character;
            } else if (lastElement.probability > slastElement.probability) {
                leftChild = slastElement;
                rightChild = lastElement;
                newChar = leftChild.character;
            } else {
                //if both probabilities are the same
                //compare the strings of the two nodes (so that the node with the alphabetically smaller one will be leftChild)

                //get index of the char that comes first in the given alphabet
                int indFirstChar = findFirstChar(alphabet, lastElement.character, slastElement.character);

                if (alphabet[indFirstChar] == lastElement.character) {
                    leftChild = lastElement;
                    rightChild = slastElement;
                } else if (alphabet[indFirstChar] == slastElement.character){
                    leftChild = slastElement;
                    rightChild = lastElement;
                }
                newChar = alphabet[indFirstChar];
            }

            //create new node with the two nodes as childs
            Node innerNode = new Node(newChar, lastElement.probability + slastElement.probability, leftChild, rightChild);

            //in the last step the last 2 elements are removed from the list
            //only innerNode has to be part of the list
            if (liste.isEmpty()) {
                root = innerNode;
                break;
            }

            //search for the index where the new node should be added -> list stays sorted
            //add the new node
            for (int i = 0; i < liste.size(); i++) {
                if (liste.get(i).probability <= innerNode.probability) {
                    liste.add(i, innerNode);
                    break;
                }
            }
            System.out.println(liste.size());
            printListToConsole(liste);
        }

        //System.out.println("[ " + root.probability + " " + root.character + " ]");
    }

    //returns the index of the char that comes first in the given alphabet
    private int findFirstChar(char[] alphabet, char c1, char c2) {
        int index = 0;

        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] == c1) {
                index = i;
                break;
            }
            if (alphabet[i] == c2) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void printListToConsole(List<Node> liste) {
        //print the sorted List to console
        for (int i = 0; i < liste.size(); i++) {
            System.out.print("[ " + liste.get(i).probability +" "+ liste.get(i).character + " ]");
        }
        System.out.print("\n");
    }

    @Override
    public List<List<String>> get_codes() {
        getPaths(root, "");
        return codesList;
    }

    private void getPaths(Node n, String path) {
        if (n != null) {
            //if the current node is a leaf
            if (n.leftChild == null && n.rightChild == null) {
                List<String> subList = new LinkedList<String>();
                subList.add(String.valueOf(n.character));
                subList.add(path);
                codesList.add(subList);
                return;
            }
            //if the current node is not a leaf recursively call the function and add the path accordingly
            getPaths(n.leftChild, path + "1");
            getPaths(n.rightChild, path + "0");
        }
    }

    @Override
    public String encode(String plain_text) {
        String encodedMessage = "";
        //iterate through the string
        for (int i = 0; i < plain_text.length(); i++) {
            //for each char in the string, search the list and add the encoded path to the output string
            for (int j = 0; j < codesList.size(); j++) {
                if (codesList.get(j).get(0).charAt(0) == plain_text.charAt(i)) {
                    encodedMessage = encodedMessage.concat(codesList.get(j).get(1));
                }
            }
        }
        return encodedMessage;
    }

    @Override
    public String decode(String huffman_text) {
        String decodedMessage = "";

        while (!huffman_text.isEmpty()) {
            Node iterator = root;

            //as long as iterator is an inner node
            while (!(iterator.leftChild == null && iterator.rightChild == null)) {
                //go left if input strings first char is 1
                if (huffman_text.charAt(0) == '1') {
                    iterator = iterator.leftChild;
                //go right if input strings first char is 0
                } else {
                    iterator = iterator.rightChild;
                }
                //cut off the first char of the input string
                huffman_text = huffman_text.substring(1);
            }
            //when a leaf is found, add the char to the output string
            decodedMessage = decodedMessage.concat(String.valueOf(iterator.character));
        }
        return decodedMessage;
    }
}
