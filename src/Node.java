import java.util.Random;
import java.util.Vector;

/**
 * User: Kevin
 * Date: 11/27/12
 */
public class Node {
    private double value;
    private Vector<Node> children;
    private String operation;
    private Random random;
    private Node parentNode;

    public Node(Random random) {
        this.random = random;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Vector<Node> getChildren() {
        return children;
    }

    public void setChildren(Vector<Node> children) {
        this.children = children;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }
}
