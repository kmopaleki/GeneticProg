import java.io.*;
import java.util.*;

/**
 * User: Kevin
 * Date: 11/27/12
 */
public class GeneticProgram {

    private int numberOfRuns;
    private int numberOfEvals;
    private int populationSize;
    private int mutationDepth;
    private int probabilityOfRecombination;
    private Random random;
    private long randomSeed;
    private int maxDepth;
    boolean parentflagged = false;
    private int numParents;
    private int numChildren;
    private int kParents;
    private PopulationTreeMember swapper1;
    private PopulationTreeMember swapper2;
    private String theLogFile;
    private String theSolutionFile;
    private String theDataFile;
    private int numberOfPairs;
    private double[] xArray;
    private double[] yArray;

    public GeneticProgram() {
    }

    public GeneticProgram(String theLogFile,String theSolutionFile,String theDataFile,
                          int numberOfRuns, int numberOfEvals, int populationSize,
                          int probabilityOfRecombination, long randomSeed,
                          int maxDepth,int numParents, int kParents,int mutationDepth,
                          int numChildren) {
        this.theLogFile = theLogFile;
        this.theSolutionFile = theSolutionFile;
        this.theDataFile = theDataFile;
        this.numberOfRuns = numberOfRuns;
        this.numberOfEvals = numberOfEvals;
        this.populationSize = populationSize;
        this.probabilityOfRecombination = probabilityOfRecombination;
        this.random = new Random(randomSeed);
        this.maxDepth = maxDepth;
        this.swapper1 = new PopulationTreeMember();
        this.swapper2 = new PopulationTreeMember();
        this.theLogFile = theLogFile;
        this.theDataFile = theDataFile;
        this.theSolutionFile = theSolutionFile;
        this.numParents = numParents;
        this.kParents = kParents;
        this.mutationDepth = mutationDepth;
        this.numChildren = numChildren;

    }

    public void GP() throws IOException {

        FileWriter LogFile = new FileWriter(theLogFile,true);
        FileWriter SolutionFile = new FileWriter(theSolutionFile,true);
        BufferedWriter outLog = new BufferedWriter(LogFile);
        BufferedWriter solutionLog = new BufferedWriter(SolutionFile);

        outLog.write("Result Log" + "\r\n");
        outLog.write("Number of Runs: " + "Number of Evaluations per Run: " + numberOfEvals+ "\r\n");
        outLog.write("Population Size: " + populationSize + "\r\n");
        outLog.write("Number of Parents: " + numParents + "\r\n");
        outLog.write("Parent SelectionALg: " + "Tournament Selection" + "\r\n");
        outLog.write("kParents: " + kParents + "\r\n");
        outLog.write("Probability of Recombination: " + (double)probabilityOfRecombination/(double)100 + "\r\n");
        outLog.write("Result Log" + "\r\n");

        double currentOverAllBest = 0.00;
        String currentOverAllBestString = "";
        for(int i = 0; i<numberOfRuns; i++){
            //Initialize the Population
            int childcounter = numChildren;
            buildXYArrays();
            ArrayList<PopulationTreeMember> populationTreeMembers = new ArrayList<PopulationTreeMember>();
            while(populationTreeMembers.size()<populationSize){
                PopulationTreeMember populationTreeMember = new PopulationTreeMember(random,maxDepth);

                populationTreeMember.setFitnessValue(xArray,yArray);
                    populationTreeMembers.add(populationTreeMember);
            }
            //EVALUATE THE INITIAL POPULATION
            for(int j = 0; j<populationTreeMembers.size(); j++){
                populationTreeMembers.get(j).setFitnessValue(xArray,yArray);

            }

            //SET A VALUE FOR NANS & INFINITY
            double  highestValue = getHighestValue(populationTreeMembers);
            for(int j = 0; j<populationTreeMembers.size(); j++){
                if(Double.isNaN(populationTreeMembers.get(j).getFitnessValue())||Double.isInfinite(populationTreeMembers.get(j).getFitnessValue())){
                    populationTreeMembers.get(j).setFitnessValue(highestValue);
                }
            }
            double currentLocalBest;
            currentLocalBest = populationTreeMembers.get(0).getFitnessValue();
            String currentBestFormula = populationTreeMembers.get(0).getFormula(populationTreeMembers.get(0).getRootNode());
            if(i==0){
                currentOverAllBest = currentLocalBest;
                currentOverAllBestString = currentBestFormula;
            }



            double prevLocalBest = 0.0000;
            outLog.write("Run #: " + (i+1)+"\r\n");
            int evalCounter = 0;
            while(evalCounter<numberOfEvals){
                ArrayList<PopulationTreeMember> childPopulation = new ArrayList<PopulationTreeMember>();
                ArrayList<PopulationTreeMember> parentPopulation = new ArrayList<PopulationTreeMember>();
                System.out.println("Run #: " + i + " Eval #: " + evalCounter);

                //SELECT PARENTS
                TournamentSelection(populationTreeMembers, parentPopulation);

                //Determine CrossOver or Mutation
                while(childPopulation.size()<numChildren){
                    int randomChoice = random.nextInt(100);
                    if(randomChoice<=probabilityOfRecombination){
                        int x = random.nextInt(parentPopulation.size());
                        int y = random.nextInt(parentPopulation.size());
                        treeCrossover(parentPopulation.get(x),parentPopulation.get(y),childPopulation);
                    }else{
                        treeMutation(parentPopulation,childPopulation);
                    }
                }
                //SURVIVAL SELECTION - TOURNAMENT
                highestValue = getHighestValue(populationTreeMembers);
                for(int k = 0; k<childPopulation.size(); k++){
                    if(Double.isNaN(childPopulation.get(k).getFitnessValue())||Double.isInfinite(childPopulation.get(k).getFitnessValue())){
                        childPopulation.get(k).setFitnessValue(highestValue);
                    }
                }
                populationTreeMembers.addAll(childPopulation);
                TournamentSurvivalSelection(populationTreeMembers);
                insertionSort(populationTreeMembers);
                currentLocalBest = populationTreeMembers.get(0).getFitnessValue();
                int diversity = diversity(populationTreeMembers);
                if(diversity<=((0.10)*(populationSize))){
//                    populationSize++;
                    numChildren++;
                }
                else if(diversity>((0.40)*populationSize)){
                    numChildren--;
                }
                currentBestFormula = populationTreeMembers.get(0).getFormula(populationTreeMembers.get(0).getRootNode());

                if(currentLocalBest<currentOverAllBest){
                    currentOverAllBest = currentLocalBest;
                    currentOverAllBestString = currentBestFormula;
                }

                if(evalCounter%numChildren==0){
                    double localAverage = getAverageFitness(populationTreeMembers);
                    outLog.write(childcounter + "\t" + (-1)*localAverage + "\t" + (-1)*currentLocalBest + "\r\n");
                    childcounter = childcounter + numChildren;
                }


                evalCounter++;
            }
        }

        solutionLog.write(currentOverAllBestString+"\r\n");

        outLog.close();
        solutionLog.close();
    }

    private void treeCrossover(PopulationTreeMember parent1,PopulationTreeMember parent2,ArrayList<PopulationTreeMember> children){
        PopulationTreeMember child1 = new PopulationTreeMember(random);
        PopulationTreeMember child2 = new PopulationTreeMember(random);
        child1.copy(parent1);
        child2.copy(parent2);
        setAllToFalse(child1.getRootNode());
        setAllToFalse(child2.getRootNode());
        parentflagged = false;
        while(!parentflagged){
            setPoint(child1.getRootNode());
        }
        parentflagged=false;
        while(!parentflagged){
            setPoint(child2.getRootNode());
        }

        //Grab nodes to be swapped
        swapper1 = new PopulationTreeMember();
        swapper2 = new PopulationTreeMember();
        setSwapper1(child1.getRootNode());
        setSwapper2(child2.getRootNode());


        //ChildNode exchanges with parent 2, ChildNode2 exchanges with parent1
        swapNodes1(child1);
        swapNodes2(child2);

        child1.resetDepth();
        //Repair child 1 if its necessary
        repairTreeFunction(child1);
        child1.setFitnessValue(xArray,yArray);
        children.add(child1);
        if(children.size()<numChildren){
            child2.resetDepth();
            repairTreeFunction(child2);
            child2.setFitnessValue(xArray,yArray);
            children.add(child2);
        }
        String formula1p = parent1.getFormula(parent1.getRootNode());
        String formula1 = child1.getFormula(child1.getRootNode());
        String formula2p = parent2.getFormula(parent2.getRootNode());
        String formula2 = child2.getFormula(child2.getRootNode());
        return;
    }



    private void TournamentSelection(ArrayList<PopulationTreeMember> population,ArrayList<PopulationTreeMember> parentPopulation){
        while(parentPopulation.size()<numParents){
            int current_best = random.nextInt(population.size());
            for(int i = 0; i<kParents-1; i++){
                int index = random.nextInt(population.size());
                if(population.get(index).getFitnessValue()<population.get(current_best).getFitnessValue()){
                    current_best = index;
                }
            }
            parentPopulation.add(population.get(current_best));
        }
    }

    private void TournamentSurvivalSelection(ArrayList<PopulationTreeMember> populationTreeMembers){
        while(populationTreeMembers.size()>populationSize){
            int current_worst = random.nextInt(populationTreeMembers.size());
            for(int i = 0; i<kParents-1; i++){
                int index = random.nextInt(populationTreeMembers.size());
                if(populationTreeMembers.get(index).getFitnessValue()>populationTreeMembers.get(current_worst).getFitnessValue()){
                    current_worst = index;
                }else if(populationTreeMembers.get(index).getFitnessValue()==populationTreeMembers.get(current_worst).getFitnessValue()){
                    if(populationTreeMembers.get(index).getNumOfNodes()>populationTreeMembers.get(current_worst).getNumOfNodes()){
                        current_worst = index;
                    }
                }
            }
            populationTreeMembers.remove(current_worst);
        }
    }

    private void insertionSort(ArrayList<PopulationTreeMember> populationTreeMembers){
        for(int i = 1; i<populationTreeMembers.size(); i++){
            int j = i;
            PopulationTreeMember temp = populationTreeMembers.get(j);

            while((j>0)&&(populationTreeMembers.get(j-1).getFitnessValue()>temp.getFitnessValue())){
                populationTreeMembers.set(j,populationTreeMembers.get(j-1));
                j--;
            }
            populationTreeMembers.set(j,temp);
        }
    }

    private void treeMutation(ArrayList<PopulationTreeMember> mutatePopulation,ArrayList<PopulationTreeMember> children){
        PopulationTreeMember parent;
        parent = mutatePopulation.get(random.nextInt(mutatePopulation.size()));
        int mutationId = random.nextInt(parent.getNumOfNodes());
        PopulationTreeMember child = new PopulationTreeMember(random);
        child.copy(parent);
        mutateNode(child.getRootNode(),mutationId);
        child.setFitnessValue(xArray,yArray);
        children.add(child);
    }

    private void setPoint(Node node){
        int randomValue = random.nextInt(100);
        if(randomValue<=15){
            node.setSwapFlag(true);
            parentflagged=true;
        }
        else{
            for(int i = 0; i<node.getChildren().size(); i++){
                if(!parentflagged){
                    setPoint(node.getChildren().get(i));
                }
            }
        }
    }

    private void setAllToFalse(Node node){
        node.setSwapFlag(false);
        for(int i = 0; i<node.getChildren().size();i++){
            setAllToFalse(node.getChildren().get(i));
        }
    }

    private void mutateNode(Node node,int id){
        if(node.getId()!=id){
            for(int i = 0; i<node.getChildren().size(); i++){
                mutateNode(node.getChildren().get(i),id);
            }
        }else if(node.getId()==id){
            node.getChildren().clear();
            node.setOperation(setOperation(0));
            if(node.getOperation().equals("sin")){
                for(int i = 0; i<1; i++){
                    Node child = new Node(random);
                    child.setDepth(node.getDepth()+1);
                    insertNode(child,node,maxDepth,node.getDepth());
                }
            }else if(node.getOperation().equals("cos")){
                for(int i = 0; i<1; i++){
                    Node child = new Node(random);
                    child.setDepth(node.getDepth()+1);
                    insertNode(child,node,maxDepth,node.getDepth());
                }

            }else if(node.getOperation().equals("/")){
                for(int i = 0; i<2; i++){
                    Node child = new Node(random);
                    child.setDepth(node.getDepth()+1);
                    insertNode(child,node,maxDepth,node.getDepth());
                }

            }else if(node.getOperation().equals("+")){
                for(int i = 0; i<2; i++){
                    Node child = new Node(random);
                    child.setDepth(node.getDepth()+1);
                    insertNode(child,node,maxDepth,node.getDepth());
                }

            }else if(node.getOperation().equals("-")){
                for(int i = 0; i<2; i++){
                    Node child = new Node(random);
                    child.setDepth(node.getDepth()+1);
                    insertNode(child,node,maxDepth,node.getDepth());
                }
            }else if(node.getOperation().equals(".")){
                for(int i = 0; i<2; i++){
                    Node child = new Node(random);
                    child.setDepth(node.getDepth()+1);
                    insertNode(child,node,maxDepth,node.getDepth());
                }
            }else if(node.getOperation().equals("power(,)")){
                for(int i = 0; i<2; i++){
                    Node child = new Node(random);
                    child.setDepth(node.getDepth()+1);
                    insertNode(child,node,maxDepth,node.getDepth());
                }
            }else if(node.getOperation().equals("x")){

            }else if(node.getOperation().equals("R")){
                node.setValue((double)random.nextInt(50));
            }
        }
    }


    private void setSwapper1(Node node){
        if(node.isSwapFlag()){
            Node node1 = new Node(random);
            node1.setChildren(node.getChildren());
            node1.setOperation(node.getOperation());
            swapper1.setRootNode(node1);
        }else{
            for(int i = 0; i<node.getChildren().size(); i++){
                setSwapper1(node.getChildren().get(i));
            }
        }
    }

    private void setSwapper2(Node node){
        if(node.isSwapFlag()){
            swapper2.setRootNode(node);
        }else{
            for(int i = 0; i<node.getChildren().size(); i++){
                setSwapper2(node.getChildren().get(i));
            }
        }
    }

    private void swapNodes1(PopulationTreeMember populationTreeMember) {
        if(populationTreeMember.getRootNode().isSwapFlag()){
            populationTreeMember.setRootNode(swapper2.getRootNode());
        }else{
            exchange1(populationTreeMember.getRootNode());
        }


    }

    private void exchange1(Node node){
        for(int i = 0; i<node.getChildren().size();i++){
            if(node.getChildren().get(i).isSwapFlag()){
                node.getChildren().set(i,swapper2.getRootNode());
                return;
            }
            exchange1(node.getChildren().get(i));
        }

    }

    private void swapNodes2(PopulationTreeMember populationTreeMember){
        if(populationTreeMember.getRootNode().isSwapFlag()){
            populationTreeMember.setRootNode(swapper1.getRootNode());
        }else{
            exchange2(populationTreeMember.getRootNode());
        }

    }
    private void exchange2(Node node){
        for(int i = 0; i<node.getChildren().size();i++){
            if(node.getChildren().get(i).isSwapFlag()){
                node.getChildren().set(i,swapper1.getRootNode());
                return;
            }
            exchange1(node.getChildren().get(i));
        }

    }

    private void checkForPoint(Node node) {
        if(node.isSwapFlag()){
            parentflagged = true;
            return;
        }
        for(int i = 0; i<node.getChildren().size(); i++){
            checkForPoint(node.getChildren().get(i));
        }

    }

    private void buildXYArrays() throws FileNotFoundException {
        File inFile = new File(theDataFile);
        Scanner s = new Scanner(inFile);
        String crap = s.nextLine();
        numberOfPairs=Integer.parseInt(s.nextLine());
        xArray = new double[numberOfPairs];
        yArray = new double[numberOfPairs];
        String crap2 = s.nextLine();
        String[] xyPairs = new String[numberOfPairs];
        int counter = 0;
        while(s.hasNextLine()){
            xyPairs[counter] = s.nextLine();
            String[] tempArray = xyPairs[counter].split(",");
            xArray[counter] = Double.parseDouble(tempArray[0]);
            yArray[counter] = Double.parseDouble(tempArray[1]);
            counter++;
        }
    }
    private void insertNode(Node insertNode, Node parentNode,int depthcount,int current_depth){
        insertNode.setOperation(setOperation(depthcount));
        if(insertNode.getOperation().equals("sin")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<1; i++){
                Node newChild = new Node(random);
                newChild.setDepth(insertNode.getDepth()+1);
                insertNode(newChild,insertNode,depthcount + 1,current_depth+1);
            }

        }else if(insertNode.getOperation().equals("cos")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<1; i++){
                Node newChild = new Node(random);
                newChild.setDepth(insertNode.getDepth()+1);
                insertNode(newChild,insertNode,depthcount + 1,current_depth + 1);
            }

        }else if(insertNode.getOperation().equals("/")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(insertNode.getDepth()+1);
                insertNode(newChild,insertNode,depthcount+1,current_depth+1);
            }

        }else if(insertNode.getOperation().equals("+")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(insertNode.getDepth()+1);
                insertNode(newChild,insertNode,depthcount+1,current_depth+1);
            }

        }else if(insertNode.getOperation().equals("-")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(insertNode.getDepth()+1);
                insertNode(newChild,insertNode,depthcount+1,current_depth+1);
            }

        }else if(insertNode.getOperation().equals(".")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(insertNode.getDepth()+1);
                insertNode(newChild,insertNode,depthcount+1,current_depth+1);
            }

        }else if(insertNode.getOperation().equals("power(,)")){
            insertNode.setChildren(new Vector<Node>());
            for(int i =0; i<2; i++){
                Node newChild = new Node(random);
                newChild.setDepth(insertNode.getDepth()+1);
                insertNode(newChild,insertNode,depthcount+1,current_depth+1);
            }

        }else if(insertNode.getOperation().equals("x")){
            insertNode.setChildren(new Vector<Node>());


        }else if(insertNode.getOperation().equals("R")){
            int choice = random.nextInt(2);
            if(choice==1){
                insertNode.setValue((double)random.nextInt(50));
            }else{
                insertNode.setValue((-1)*((double)random.nextInt(50)));
            }
        }
        parentNode.getChildren().add(insertNode);
    }

    private String setOperation(int depthCount){
        String[] choices = {"sin","cos",".","+","-","/","power(,)","R","x"};
        if(depthCount>=mutationDepth-1){
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

    private Double getAverageFitness(ArrayList<PopulationTreeMember> populationTreeMembers){
        double averageFitness = 0;
        for(int i = 0; i<populationTreeMembers.size(); i++){
           averageFitness = averageFitness + populationTreeMembers.get(i).getFitnessValue();
        }
        return averageFitness/((double)populationTreeMembers.size());
    }



    private void Truncation(ArrayList<PopulationTreeMember> populationTreeMembers){
        //THIS IS ASSUMING MERGE SORT WAS IMPLEMENTED ALREADY
        //AND WE HAVE SORTED FROM MOST TO LEAST FIT
        while (populationTreeMembers.size()>populationSize){
            populationTreeMembers.remove(populationTreeMembers.size()-1);
        }
    }


    private double getHighestValue(ArrayList<PopulationTreeMember> populationTreeMembers){
        double highestvalue = 0.0;
        for(int i = 0; i<populationTreeMembers.size();i++){
            if(!Double.isInfinite(populationTreeMembers.get(i).getFitnessValue())&&!Double.isNaN(populationTreeMembers.get(i).getFitnessValue())){
                if(highestvalue<populationTreeMembers.get(i).getFitnessValue()){
                    highestvalue = populationTreeMembers.get(i).getFitnessValue();
                }
            }
        }
        return highestvalue;
    }

    private void repairTreeFunction(PopulationTreeMember treeMember){
        for(int i = 0; i<treeMember.getRootNode().getChildren().size(); i++){
            recursiveRepair(treeMember.getRootNode().getChildren().get(i));
        }
    }

    private void recursiveRepair(Node node){
        if(node.getDepth()==maxDepth){
            if(node.getOperation()!="R"&&node.getOperation()!="x"){
                //We need to repair the function
                int randomChange = random.nextInt(2);
                node.getChildren().clear();
                if(randomChange==0){
                    node.setOperation("R");
                    node.setValue((double)random.nextInt(12));
                }else if(randomChange==1){
                    node.setOperation("x");
                }
            }
        }else{
            for(int i = 0; i<node.getChildren().size(); i++){
                recursiveRepair(node.getChildren().get(i));
            }
        }
    }

    private int diversity(ArrayList<PopulationTreeMember> treeMembers){
        ArrayList<PopulationTreeMember> foundMembers = new ArrayList<PopulationTreeMember>();
        foundMembers.add(treeMembers.get(0));
        for(int i = 1; i<treeMembers.size(); i++){
            if(isUnique(treeMembers.get(i),foundMembers)){
                foundMembers.add(treeMembers.get(i));
            }
        }
        return foundMembers.size();

    }

    private boolean isUnique(PopulationTreeMember node,ArrayList<PopulationTreeMember> members){
        for(int i = 0; i<members.size(); i++){
           if(node.getFitnessValue()==members.get(i).getFitnessValue()){
               return false;
           }
        }

        return true;

    }

}
