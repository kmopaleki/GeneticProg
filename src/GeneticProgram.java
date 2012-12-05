import java.io.File;
import java.io.FileNotFoundException;
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
    private double penaltyScalar;
    private int probabilityOfRecombination;
    private boolean isMutated;
    private Random random;
    private long randomSeed;
    private int maxDepth;
    boolean parentflagged = false;
    private int numParents;
    private int kParents;
    private PopulationTreeMember swapper1;
    private PopulationTreeMember swapper2;
    private Node child1;
    private Node child2;
    private String theLogFile;
    private String theSolutionFile;
    private String theDataFile;
    private int numberOfPairs;
    private double[] xArray;
    private double[] yArray;
    private int mutationProbability;

    public GeneticProgram() {
    }

    public GeneticProgram(String theLogFile,String theSolutionFile,String theDataFile,
                          int numberOfRuns, int numberOfEvals, int populationSize,
                          int probabilityOfRecombination, long randomSeed,
                          int maxDepth,double penaltyScalar,int numParents, int kParents,int mutationDepth,int mutationProbability) {
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
        this.penaltyScalar = penaltyScalar;
        this.numParents = numParents;
        this.kParents = kParents;
        this.mutationDepth = mutationDepth;
        this.mutationProbability = mutationProbability;

    }

    public void GP() throws FileNotFoundException {
        for(int i = 0; i<numberOfRuns; i++){
            //Initialize the Population
            buildXYArrays();
            ArrayList<PopulationTreeMember> populationTreeMembers = new ArrayList<PopulationTreeMember>();
            while(populationTreeMembers.size()<populationSize){
                PopulationTreeMember populationTreeMember = new PopulationTreeMember(random,maxDepth);

                populationTreeMember.setFitnessValue(xArray,yArray);
//                if(!Double.isNaN(populationTreeMember.getFitnessValue())&&!Double.isInfinite(populationTreeMember.getFitnessValue())){
                    populationTreeMembers.add(populationTreeMember);
//                }
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

            //EVALUATE THE NANS & INFINITY
            for(int j = 0; j<numberOfEvals; j++){
                ArrayList<PopulationTreeMember> childPopulation = new ArrayList<PopulationTreeMember>();
                ArrayList<PopulationTreeMember> parentPopulation = new ArrayList<PopulationTreeMember>();
                System.out.println("Run #: " + i + " Eval #: " + j);

                //SELECT PARENTS
                TournamentSelection(populationTreeMembers, parentPopulation);
                while(childPopulation.size()<populationSize){
                    int randomChoice = random.nextInt(100);
                    if(randomChoice<=probabilityOfRecombination){
                        int x = random.nextInt(parentPopulation.size());
                        int y = random.nextInt(parentPopulation.size());
                        treeCrossover(parentPopulation.get(x),parentPopulation.get(y),childPopulation);
                    }else{
                        treeMutation(parentPopulation,childPopulation);
                    }
                }



                //KILL ALL PARENTS, add all children
                populationTreeMembers.clear();
                assert(populationTreeMembers.size()==0);
                populationTreeMembers.addAll(childPopulation);
                for(int k = 0; k<populationTreeMembers.size(); k++){
                    if(Double.isNaN(populationTreeMembers.get(k).getFitnessValue())||Double.isInfinite(populationTreeMembers.get(k).getFitnessValue())){
                        populationTreeMembers.get(k).setFitnessValue(highestValue);
                    }
                }
//                mergeSort(populationTreeMembers,0,populationTreeMembers.size()-1);
//                System.out.println(populationTreeMembers.get(0).getFitnessValue());

            }
        }
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

        child1.setFitnessValue(xArray,yArray);
        child2.setFitnessValue(xArray,yArray);
        children.add(child1);
        if(children.size()<populationSize){
            children.add(child2);
        }
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

    private void treeMutation(ArrayList<PopulationTreeMember> mutatePopulation,ArrayList<PopulationTreeMember> children){
        PopulationTreeMember parent;
        parent = mutatePopulation.get(random.nextInt(mutatePopulation.size()));
        setAllMutateFlagsToFalse(parent.getRootNode());
        isMutated = false;
        PopulationTreeMember child = new PopulationTreeMember(random);
        child.copy(parent);
        setMutateFlag(child.getRootNode());
        mutateNode(child.getRootNode());
        child.setFitnessValue(xArray, yArray);
        if(!Double.isInfinite(child.getFitnessValue())&&!Double.isNaN(child.getFitnessValue())){
            children.add(child);
        }

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

    private void setAllMutateFlagsToFalse(Node node){
        node.setMutateFlag(false);
        for(int i = 0; i<node.getChildren().size();i++){
            setAllMutateFlagsToFalse(node.getChildren().get(i));
        }
    }

    private void mutateNode(Node node){
        if(!node.isMutateFlag()){
            for(int i = 0; i<node.getChildren().size(); i++){
                mutateNode(node.getChildren().get(i));
            }
        }else if(node.isMutateFlag()){
            node.getChildren().clear();
            node.setOperation(setOperation(0));
            if(node.getOperation().equals("sin")){
                for(int i = 0; i<1; i++){
                    Node child = new Node(random);
                    insertNode(child,node,0);
                }
            }else if(node.getOperation().equals("cos")){
                for(int i = 0; i<1; i++){
                    Node child = new Node(random);
                    insertNode(child,node,0);
                }

            }else if(node.getOperation().equals("/")){
                for(int i = 0; i<2; i++){
                    Node child = new Node(random);
                    insertNode(child,node,0);
                }

            }else if(node.getOperation().equals("+")){
                for(int i = 0; i<2; i++){
                    Node child = new Node(random);
                    insertNode(child,node,0);
                }

            }else if(node.getOperation().equals("-")){
                for(int i = 0; i<2; i++){
                    Node child = new Node(random);
                    insertNode(child,node,0);
                }
            }else if(node.getOperation().equals(".")){
                for(int i = 0; i<2; i++){
                    Node child = new Node(random);
                    insertNode(child,node,0);
                }
            }else if(node.getOperation().equals("power(,)")){
                for(int i = 0; i<2; i++){
                    Node child = new Node(random);
                    insertNode(child,node,0);
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

    public void setMutateFlag(Node node){
        int rando = random.nextInt(100);
        if(rando<=mutationProbability){
            node.setMutateFlag(true);
            isMutated = true;
            return;
        }
        for(int i = 0; i<node.getChildren().size(); i++){
            if(!isMutated){
                setMutateFlag(node.getChildren().get(i));
            }
        }
    }

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
            int choice = random.nextInt(2);
            if(choice==1){
                insertNode.setValue((double)random.nextInt(12));
            }else{
                insertNode.setValue((-1)*((double)random.nextInt(12)));
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

    private void mergeSort(ArrayList<PopulationTreeMember> sortingPopulation, int lo, int n){
        int low = lo;
        int high = n;
        if(low >= high){
            return;
        }

        int middle = (low + high)/2;
        mergeSort(sortingPopulation, low,middle);
        mergeSort(sortingPopulation, middle+1,high);
        int end_low = middle;
        int start_high = middle+1;

        while((low <= end_low)&&(start_high<=high)){
            if(sortingPopulation.get(low).getFitnessValue()<sortingPopulation.get(start_high).getFitnessValue()){
                low++;
            }
            else{
                PopulationTreeMember temp = sortingPopulation.get(start_high);
                for(int k = start_high-1; k>=low; k--){
                    sortingPopulation.set(k+1,sortingPopulation.get(k));
                }
                sortingPopulation.set(low,temp);
                low++;
                end_low++;
                start_high++;
            }
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


}
