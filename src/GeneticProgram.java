import java.awt.image.AreaAveragingScaleFilter;
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
    private double penaltyScalar;
    private int probabilityOfRecombination;
    private boolean isMutated;
    private Random random;
    private long randomSeed;
    private int maxDepth;
    boolean parentflagged = false;
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

    public GeneticProgram() {
    }

    public GeneticProgram(String theLogFile,String theSolutionFile,String theDataFile,
                          int numberOfRuns, int numberOfEvals, int populationSize,
                          int probabilityOfRecombination, long randomSeed,
                          int maxDepth,double penaltyScalar) {
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

    }

    public void GP() throws FileNotFoundException {
        for(int i = 0; i<numberOfRuns; i++){
            //Initialize the Population
            buildXYArrays();
            ArrayList<PopulationTreeMember> populationTreeMembers = new ArrayList<PopulationTreeMember>();
            while(populationTreeMembers.size()<populationSize){
                PopulationTreeMember populationTreeMember = new PopulationTreeMember(random,maxDepth);

                populationTreeMember.setFitnessValue(xArray,yArray);
                if(!Double.isNaN(populationTreeMember.getFitnessValue())&&!Double.isInfinite(populationTreeMember.getFitnessValue())){
                    populationTreeMembers.add(populationTreeMember);
                }
//                populationTreeMembers.add(populationTreeMember);

            }
            //EVALUATE THE INITIAL POPULATION
            for(int j = 0; j<populationTreeMembers.size(); j++){
                populationTreeMembers.get(j).setFitnessValue(xArray,yArray);

            }

            //EVALUATE THE NANS & INFINITY
            for(int j = 0; j<populationTreeMembers.size(); j++){
                if(Double.isNaN(populationTreeMembers.get(j).getFitnessValue())){
                    populationTreeMembers.get(j).setFitnessValue(populationTreeMembers.get(j).getFitnessValue()+penaltyScalar);
                }
            }
            for(int j = 0; j<numberOfEvals; j++){
                ArrayList<PopulationTreeMember> childPopulation = new ArrayList<PopulationTreeMember>();
                ArrayList<PopulationTreeMember> parentPopulation = new ArrayList<PopulationTreeMember>();
                //SORT PARENTS AND PLACE INTO PROPORTIONS

                insertionSort(populationTreeMembers);

//                while(childPopulation.size()<populationSize){
//                    int randomChoice = random.nextInt(100);
//                    if(randomChoice<=probabilityOfRecombination){
////                        treeCrossover();
//                    }else{
//                        treeMutation(populationTreeMembers,childPopulation);
//                    }
//                }

                //EVALUATE CHILDREN

                //KILL ALL PARENTS, add all children


            }
        }
    }

    private void treeCrossover(PopulationTreeMember parent1,PopulationTreeMember parent2,ArrayList<PopulationTreeMember> children){
        setAllToFalse(parent1.getRootNode());
        setAllToFalse(parent2.getRootNode());
        parentflagged = false;
        while(!parentflagged){
            setPoint(parent1.getRootNode());
        }
        parentflagged=false;
        while(!parentflagged){
            setPoint(parent2.getRootNode());
        }

        //Grab nodes to be swapped
        swapper1 = new PopulationTreeMember();
        swapper2 = new PopulationTreeMember();
        setSwapper1(parent1.getRootNode());
        setSwapper2(parent2.getRootNode());

        PopulationTreeMember childNode = new PopulationTreeMember();
        Node node = parent1.getRootNode();
        childNode.setRootNode(node);
        childNode.setRandom(random);
        PopulationTreeMember  childNode2 =  new PopulationTreeMember();
        childNode2.setRootNode(parent2.getRootNode());
        childNode2.setRandom(random);

        //ChildNode exchanges with parent 2, ChildNode2 exchanges with parent1
        swapNodes1(childNode);
        swapNodes2(childNode2);


        children.add(childNode);
        if(children.size()<populationSize){
            children.add(childNode2);
        }
    }



    private void fitnessProportionalSelection(ArrayList<PopulationTreeMember> population){


    }

    private void treeMutation(ArrayList<PopulationTreeMember> mutatePopulation,ArrayList<PopulationTreeMember> children){
        PopulationTreeMember parent = mutatePopulation.get(random.nextInt(mutatePopulation.size()));
        setAllMutateFlagsToFalse(parent.getRootNode());


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

    private void insertionSort(ArrayList<PopulationTreeMember> populationTreeMembers){
        for(int i = 1; i<populationTreeMembers.size(); i++){
            int j = i;
            PopulationTreeMember B = populationTreeMembers.get(i);
            while((j>0)&&(populationTreeMembers.get(j-1).getFitnessValue()>B.getFitnessValue())){
                populationTreeMembers.set(j,populationTreeMembers.get(j-1));
                j--;
            }
            populationTreeMembers.set(j,B);
        }
    }




}
