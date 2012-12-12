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
    private int numOfNodes;


    /*
      Constructors
     */

    public PopulationTreeMember() {
    }


    public PopulationTreeMember(Random random, int maxDepth) {
        this.maxDepth = maxDepth;
        this.random = random;
        initialize();
        setNumNodes();
        setMaxDepth();
    }

    private void setNumNodes() {
        this.numOfNodes = 1;
        rootNode.setId(0);
        recursiveNodeCounter(rootNode);
    }

    private void recursiveNodeCounter(Node node) {
        for(int i = 0; i< node.getChildren().size(); i++){
            node.getChildren().get(i).setId(numOfNodes);
            numOfNodes++;
            recursiveNodeCounter(node.getChildren().get(i));
        }
    }

    public PopulationTreeMember(Random random) {
        this.random = random;
    }

    public int getNumOfNodes() {
        return numOfNodes;
    }

    public void setNumOfNodes(int numOfNodes) {
        this.numOfNodes = numOfNodes;
    }

    /*
     Functions
    */
    private void insertNode(Node insertNode, Node parentNode,int depthcount,int current_depth){
        insertNode.setOperation(setOperation(current_depth));
        if(insertNode.getOperation().equals("sin")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<1; i++){
                Node newChild = new Node(random);
                newChild.setDepth(current_depth);
                insertNode(newChild,insertNode,depthcount + 1,current_depth+1);
            }

        }else if(insertNode.getOperation().equals("cos")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<1; i++){
                Node newChild = new Node(random);
                newChild.setDepth(current_depth);
                insertNode(newChild,insertNode,depthcount + 1,current_depth + 1);
            }

        }else if(insertNode.getOperation().equals("/")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(current_depth);
                insertNode(newChild,insertNode,depthcount+1,current_depth+1);
            }

        }else if(insertNode.getOperation().equals("+")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(current_depth);
                insertNode(newChild,insertNode,depthcount+1,current_depth+1);
            }

        }else if(insertNode.getOperation().equals("-")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(current_depth);
                insertNode(newChild,insertNode,depthcount+1,current_depth+1);
            }

        }else if(insertNode.getOperation().equals(".")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(current_depth);
                insertNode(newChild,insertNode,depthcount+1,current_depth+1);
            }

        }else if(insertNode.getOperation().equals("power(,)")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(current_depth);
                insertNode(newChild,insertNode,depthcount+1,current_depth+1);
            }

        }else if(insertNode.getOperation().equals("x")){
            insertNode.setChildren(new Vector<Node>());


        }else if(insertNode.getOperation().equals("R")){
            int choice = random.nextInt(2);
            if(choice==1){
                insertNode.setValue((double)random.nextInt(12));
            }else{
                insertNode.setValue((-1)*((double)random.nextInt(12)));
            }
        }
        parentNode.getChildren().add(insertNode);
    }

    private void initialize(){
        Node rootNode = new Node(random);
        rootNode.setParentNode(null);
        rootNode.setDepth(0);
        int depthcount = 0;
        rootNode.setOperation(setOperation(depthcount));
        if(rootNode.getOperation().equals("sin")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<1; i++){
                Node newChild = new Node(random);
                newChild.setDepth(1);
                insertNode(newChild,rootNode,depthcount + 1,1);
            }

        }else if(rootNode.getOperation().equals("cos")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<1; i++){
                Node newChild = new Node(random);
                newChild.setDepth(1);
                insertNode(newChild,rootNode,depthcount + 1,1);
            }

        }else if(rootNode.getOperation().equals("/")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(1);
                insertNode(newChild,rootNode,depthcount+1,1);
            }

        }else if(rootNode.getOperation().equals("+")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(1);
                insertNode(newChild,rootNode,depthcount+1,1);
            }

        }else if(rootNode.getOperation().equals("-")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(1);
                insertNode(newChild,rootNode,depthcount+1,1);
            }

        }else if(rootNode.getOperation().equals(".")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(1);
                insertNode(newChild,rootNode,depthcount+1,1);
            }

        }else if(rootNode.getOperation().equals("power(,)")){
            rootNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(1);
                insertNode(newChild,rootNode,depthcount+1,1);
            }

        }else if(rootNode.getOperation().equals("x")){
            rootNode.setChildren(new Vector<Node>());
        }else if(rootNode.getOperation().equals("R")){
            int choice = random.nextInt(2);
            if(choice==1){
            rootNode.setValue((double)(random.nextInt(Integer.MAX_VALUE)));
            }else{
                rootNode.setValue((-1)*((double)random.nextInt(Integer.MAX_VALUE)));
            }

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


    public double getError(double y, double terminal){
        return Math.abs((getValue(getRootNode(),terminal)-y));
    }

    public void setFitnessValue(double [] xList,double [] yList){
        double fitness = 0.0;
        for(int i =0; i<xList.length; i++ ){
            fitness = (fitness + getError(yList[i],xList[i]));
        }
        this.fitnessValue = fitness;

    }

    public void setFitnessValue(Double fitnessValue){
        this.fitnessValue = fitnessValue;
    }

    public void copy(PopulationTreeMember y){
        rootNode = new Node(random);
        rootNode.setParentNode(null);
        rootNode.setOperation(y.getRootNode().getOperation());
        rootNode.setDepth(y.getRootNode().getDepth());
        if(rootNode.getOperation().equals("sin")){
            rootNode.setChildren(new Vector<Node>());
            for(int i = 0; i<1; i++){
                Node newChild = new Node(random);
                newChild.setDepth(rootNode.getDepth()+1);
                insertCopy(newChild,y.getRootNode().getChildren().get(i),rootNode,y.getRootNode());

            }
        }else if(rootNode.getOperation().equals("cos")){
            rootNode.setChildren(new Vector<Node>());
            for(int i = 0; i<1; i++){
                Node newChild = new Node(random);
                newChild.setDepth(rootNode.getDepth()+1);
                insertCopy(newChild,y.getRootNode().getChildren().get(i),rootNode,y.getRootNode());

            }
        }else if(rootNode.getOperation().equals("/")){
            rootNode.setChildren(new Vector<Node>());
            for(int i = 0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(rootNode.getDepth()+1);
                insertCopy(newChild,y.getRootNode().getChildren().get(i),rootNode,y.getRootNode());

            }
        }else if(rootNode.getOperation().equals("+")){
            rootNode.setChildren(new Vector<Node>());
            for(int i = 0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(rootNode.getDepth()+1);
                insertCopy(newChild,y.getRootNode().getChildren().get(i),rootNode,y.getRootNode());

            }
        }else if(rootNode.getOperation().equals("-")){
            rootNode.setChildren(new Vector<Node>());
            for(int i = 0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(rootNode.getDepth()+1);
                insertCopy(newChild,y.getRootNode().getChildren().get(i),rootNode,y.getRootNode());

            }
        }else if(rootNode.getOperation().equals(".")){
            rootNode.setChildren(new Vector<Node>());
            for(int i = 0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(rootNode.getDepth()+1);
                insertCopy(newChild,y.getRootNode().getChildren().get(i),rootNode,y.getRootNode());

            }
        }else if(rootNode.getOperation().equals("power(,)")){
            rootNode.setChildren(new Vector<Node>());
            for(int i = 0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(rootNode.getDepth()+1);
                insertCopy(newChild,y.getRootNode().getChildren().get(i),rootNode,y.getRootNode());

            }
        }else if(rootNode.getOperation().equals("x")){
            rootNode.setChildren(new Vector<Node>());
        }else if(rootNode.getOperation().equals("R")){
            rootNode.setValue(y.getRootNode().getValue());
        }
        setRootNode(rootNode);
        setNumNodes();
        setMaxDepth();
    }

    private void insertCopy(Node node, Node otherNode,Node parentNode,Node otherParentNode){
        node.setOperation(otherNode.getOperation());
        if(node.getOperation().equals("sin")){
            node.setChildren(new Vector<Node>());
            for(int i= 0; i<1; i++){
                Node newChild = new Node(random);
                newChild.setDepth(otherNode.getDepth()+1);
                insertCopy(newChild,otherNode.getChildren().get(i),node,otherNode);
            }
        }else if(node.getOperation().equals("cos")){
            node.setChildren(new Vector<Node>());
            for(int i= 0; i<1; i++){
                Node newChild = new Node(random);
                newChild.setDepth(otherNode.getDepth()+1);
                insertCopy(newChild,otherNode.getChildren().get(i),node,otherNode);
            }

        }else if(node.getOperation().equals("/")){
            node.setChildren(new Vector<Node>());
            for(int i= 0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(otherNode.getDepth()+1);
                insertCopy(newChild,otherNode.getChildren().get(i),node,otherNode);
            }

        }else if(node.getOperation().equals("+")){
            node.setChildren(new Vector<Node>());
            for(int i= 0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(otherNode.getDepth()+1);
                insertCopy(newChild,otherNode.getChildren().get(i),node,otherNode);
            }

        }else if(node.getOperation().equals("-")){
            node.setChildren(new Vector<Node>());
            for(int i= 0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(otherNode.getDepth()+1);
                insertCopy(newChild,otherNode.getChildren().get(i),node,otherNode);
            }

        }else if(node.getOperation().equals(".")){
            node.setChildren(new Vector<Node>());
            for(int i= 0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(otherNode.getDepth()+1);
                insertCopy(newChild,otherNode.getChildren().get(i),node,otherNode);
            }
        }else if(node.getOperation().equals("power(,)")){
            node.setChildren(new Vector<Node>());
            for(int i= 0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(otherNode.getDepth()+1);
                insertCopy(newChild,otherNode.getChildren().get(i),node,otherNode);
            }
        }else if(node.getOperation().equals("x")){
            node.setChildren(new Vector<Node>());

        }else if(node.getOperation().equals("R")){
            node.setChildren(new Vector<Node>());
            node.setValue(otherNode.getValue());
        }
        parentNode.getChildren().add(node);
    }

    public void setMaxDepth(){
        this.maxDepth = 0;
        recursiveMaxDepth(rootNode);

    }

    private void recursiveMaxDepth(Node node){
        for(int i = 0; i<node.getChildren().size();i++){
            if(node.getChildren().get(i).getDepth()>maxDepth){
                maxDepth = node.getChildren().get(i).getDepth();
            }
            recursiveMaxDepth(node.getChildren().get(i));
        }

    }

    public void resetDepth(){
        rootNode.setDepth(0);
        for(int i = 0; i<rootNode.getChildren().size(); i++){
            rootNode.getChildren().get(i).setDepth(1);
            recursiveResetDepth(rootNode.getChildren().get(i));
        }

    }

    private void recursiveResetDepth(Node node){
        for(int i = 0;i<node.getChildren().size(); i++){
            node.getChildren().get(i).setDepth(node.getDepth()+1);
            recursiveResetDepth(node.getChildren().get(i));
        }
    }


}
