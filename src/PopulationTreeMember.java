import java.util.Random;
import java.util.Vector;

/**
 * User: Kevin
 * Date: 11/27/12
 */
public class PopulationTreeMember {
    private double fitnessValue;
    private Random random;
    private boolean beenParentSelected;
    private boolean beenSurvivalSelected;
    private Node rootNode;
    private int maxDepth;



    /*
      Constructors
     */

    public PopulationTreeMember() {
    }

    public PopulationTreeMember(Random random, int maxDepth) {
        this.maxDepth = maxDepth;
        this.random = random;
        initialize();
    }

    /*
      Functions
     */
    private void insertNode(Node insertNode, Node parentNode,int depthcount){
        insertNode.setOperation(setOperation(depthcount));
        if(insertNode.getOperation().equals("sin")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<1; i++){
                Node newChild = new Node(random);
                insertNode(newChild,insertNode,depthcount + 1);
            }

        }else if(insertNode.getOperation().equals("cos")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<1; i++){
                Node newChild = new Node(random);
                insertNode(newChild,insertNode,depthcount + 1);
            }

        }else if(insertNode.getOperation().equals("/")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                insertNode(newChild,insertNode,depthcount+1);
            }

        }else if(insertNode.getOperation().equals("+")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                insertNode(newChild,insertNode,depthcount+1);
            }

        }else if(insertNode.getOperation().equals("-")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                insertNode(newChild,insertNode,depthcount+1);
            }

        }else if(insertNode.getOperation().equals(".")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                insertNode(newChild,insertNode,depthcount+1);
            }

        }else if(insertNode.getOperation().equals("power(,)")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                insertNode(newChild,insertNode,depthcount+1);
            }

        }else if(insertNode.getOperation().equals("x")){
            insertNode.setChildren(new Vector<Node>());


        }else if(insertNode.getOperation().equals("R")){
            insertNode.setChildren(new Vector<Node>());
            insertNode.setValue((double)random.nextInt(50));
        }
        parentNode.getChildren().add(insertNode);
    }

    private void initialize(){
        Node rootNode = new Node(random);
        rootNode.setParentNode(null);
        int depthcount = 0;
        rootNode.setOperation(setOperation(depthcount));
        if(rootNode.getOperation().equals("sin")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<1; i++){
                Node newChild = new Node(random);
                insertNode(newChild,rootNode,depthcount + 1);
            }

        }else if(rootNode.getOperation().equals("cos")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<1; i++){
                Node newChild = new Node(random);
                insertNode(newChild,rootNode,depthcount + 1);
            }

        }else if(rootNode.getOperation().equals("/")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                insertNode(newChild,rootNode,depthcount+1);
            }

        }else if(rootNode.getOperation().equals("+")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                insertNode(newChild,rootNode,depthcount+1);
            }

        }else if(rootNode.getOperation().equals("-")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                insertNode(newChild,rootNode,depthcount+1);
            }

        }else if(rootNode.getOperation().equals(".")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                insertNode(newChild,rootNode,depthcount+1);
            }

        }else if(rootNode.getOperation().equals("power(,)")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                insertNode(newChild,rootNode,depthcount+1);
            }

        }else if(rootNode.getOperation().equals("x")){
            rootNode.setChildren(new Vector<Node>());
        }else if(rootNode.getOperation().equals("R")){
            rootNode.setValue(random.nextInt(Integer.MAX_VALUE));
        }

        setRootNode(rootNode);

    }

    public String getFormula(Node node){
        if(node.getOperation().equals("sin"))
        {
            return "sin("+ getFormula(node.getChildren().get(0)) + ")";
        }else if(node.getOperation().equals("cos")){
            return "cos("+getFormula(node.getChildren().get(0)) + ")";
        }else if(node.getOperation().equals("x")){
            return "x";
        }else if(node.getOperation().equals("power(,)")){
            return "power(" + getFormula(node.getChildren().get(0)) + "," + getFormula(node.getChildren().get(1)) + ")";
        }else if(node.getOperation().equals("+")){
            return "(" + getFormula(node.getChildren().get(0)) + " + "+ getFormula(node.getChildren().get(1)) + ")";
        }else if(node.getOperation().equals("-")){
            return "(" + getFormula(node.getChildren().get(0)) + " - "+ getFormula(node.getChildren().get(1)) + ")";
        }else if(node.getOperation().equals("/")){
            return "(" + getFormula(node.getChildren().get(0)) + " / "+ getFormula(node.getChildren().get(1)) + ")";
        }else if(node.getOperation().equals(".")){
            return "(" + getFormula(node.getChildren().get(0)) + " * "+ getFormula(node.getChildren().get(1))+ ")";
        }else if(node.getOperation().equals("R")){
            return String.valueOf(node.getValue());
        }

        return "FAIL";

    }

    public Double getValue(Node node,double terminalValue){
        if(node.getOperation().equals("sin")){
            return Math.sin(getValue(node.getChildren().get(0), terminalValue));
        }else if(node.getOperation().equals("cos")){
            return Math.cos(getValue(node.getChildren().get(0),terminalValue));
        }else if(node.getOperation().equals("/")){
            return getValue(node.getChildren().get(0), terminalValue)/getValue(node.getChildren().get(1),terminalValue);
        }else if(node.getOperation().equals("+")){
            return getValue(node.getChildren().get(0),terminalValue) + getValue(node.getChildren().get(1),terminalValue);
        }else if(node.getOperation().equals("-")){
            return getValue(node.getChildren().get(0),terminalValue) - getValue(node.getChildren().get(1),terminalValue);
        }else if(node.getOperation().equals(".")){
            return getValue(node.getChildren().get(0),terminalValue) * getValue(node.getChildren().get(1),terminalValue);
        }else if(node.getOperation().equals("x")){
            return terminalValue;
        }else if(node.getOperation().equals("power(,)")){
            return Math.pow(getValue(node.getChildren().get(0),terminalValue),getValue(node.getChildren().get(1),terminalValue));
        }else if(node.getOperation().equals("R")){
            return node.getValue();
        }
        return null;
    }




    /*
      Getters & Setters
     */

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public boolean isBeenParentSelected() {
        return beenParentSelected;
    }

    public void setBeenParentSelected(boolean beenParentSelected) {
        this.beenParentSelected = beenParentSelected;
    }

    public boolean isBeenSurvivalSelected() {
        return beenSurvivalSelected;
    }

    public void setBeenSurvivalSelected(boolean beenSurvivalSelected) {
        this.beenSurvivalSelected = beenSurvivalSelected;
    }

    private String setOperation(int depthCount){
        String[] choices = {"sin","cos",".","+","-","/","power(,)","R","x"};
        if(depthCount>=maxDepth-1){
            int choice = random.nextInt(2);
            if(choice==0){
                return "x";
            }else if(choice==1){
                return "R";
            }
        }
        int choice = random.nextInt(9);
        return choices[choice];
    }

    private String printFormula(){
        return "";
    }

}
