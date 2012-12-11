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
    private boolean swapFlag;
    private boolean mutateFlag;
    private int id;
    private int depth;

    public Node(Random random) {
        this.random = random;
        this.swapFlag = false;
        this.mutateFlag = false;
        this.children = new Vector<Node>();
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

    public boolean isSwapFlag() {
        return swapFlag;
    }

    public void setSwapFlag(boolean swapFlag) {
        this.swapFlag = swapFlag;
    }

    public boolean isMutateFlag() {
        return mutateFlag;
    }

    public void setMutateFlag(boolean mutateFlag) {
        this.mutateFlag = mutateFlag;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
