package huffmancoding;

public class Node {
    char character = ' ';
    float probability = 0.0f;

    Node leftChild = null;
    Node rightChild = null;


    //constructor for leaf node
    public Node(char c, float p) {
        this.character = c;
        this.probability = p;
    }

    //constructor for inner node
    public Node(char c, float p, Node left, Node right) {
        this.character = c;
        this.probability = p;
        this.leftChild = left;
        this.rightChild = right;
    }
}
