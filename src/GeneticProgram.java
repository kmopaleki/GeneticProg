import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * User: Kevin
 * Date: 11/27/12
 */
public class GeneticProgram {

    private int numberOfRuns;
    private int numberOfEvals;
    private int kSelection;
    private int kSurvival;
    private int populationSize;
    private int numChildren;
    private int numParents;
    private int probabilityOfRecombination;
    private boolean isMutated;
    private Random random;
    private long randomSeed;
    private int maxDepth;
    boolean parentflagged = false;
    private Node swapper1;
    private Node swapper2;
    private Node child1;
    private Node child2;

    public GeneticProgram(int numberOfRuns, int numberOfEvals,
                          int kSelection, int kSurvival,
                          int populationSize, int numChildren,
                          int numParents, int probabilityOfRecombination,
                          long randomSeed, int maxDepth) {
        this.numberOfRuns = numberOfRuns;
        this.numberOfEvals = numberOfEvals;
        this.kSelection = kSelection;
        this.kSurvival = kSurvival;
        this.populationSize = populationSize;
        this.numChildren = numChildren;
        this.numParents = numParents;
        this.probabilityOfRecombination = probabilityOfRecombination;
        this.randomSeed = randomSeed;
        this.random= new Random(randomSeed);
        this.maxDepth = maxDepth;
    }

    public GeneticProgram() {
    }

    public void GP(){
        random = new Random(12345555567L);
        maxDepth = 5;
        ArrayList<PopulationTreeMember> populationTreeMembers = new ArrayList<PopulationTreeMember>();
        ArrayList<PopulationTreeMember> crapPopulation = new ArrayList<PopulationTreeMember>();
        ArrayList<PopulationTreeMember> goodPopulation = new ArrayList<PopulationTreeMember>();

        for(int i = 0; i<100; i++){
            PopulationTreeMember populationTreeMember = new PopulationTreeMember(random,maxDepth);
            populationTreeMembers.add(populationTreeMember);
        }
        ArrayList<PopulationTreeMember>children = new ArrayList<PopulationTreeMember>();
        treeCrossover(populationTreeMembers,children);
        System.out.println(populationTreeMembers.get(0).getFormula(populationTreeMembers.get(0).getRootNode()));
        System.out.println(children.get(0).getFormula(children.get(0).getRootNode()));
    }

    private void treeCrossover(ArrayList<PopulationTreeMember> parentPopulation,ArrayList<PopulationTreeMember> children){

        PopulationTreeMember parent1 = new PopulationTreeMember();
        parent1.setRootNode(parentPopulation.get(0).getRootNode());

        PopulationTreeMember parent2 = parentPopulation.get(1);
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
        children.add(childNode2);

//        System.out.println(parent1.getFormula(parent1.getRootNode()));
//        System.out.println(children.get(0).getFormula(children.get(0).getRootNode()));

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
            this.swapper1 = node;
        }else{
            for(int i = 0; i<node.getChildren().size(); i++){
                setSwapper1(node.getChildren().get(i));
            }
        }
    }

    private void setSwapper2(Node node){
        if(node.isSwapFlag()){
            this.swapper2 = node;
        }else{
            for(int i = 0; i<node.getChildren().size(); i++){
                setSwapper2(node.getChildren().get(i));
            }
        }
    }

    private void swapNodes1(PopulationTreeMember populationTreeMember) {
        if(populationTreeMember.getRootNode().isSwapFlag()){
            populationTreeMember.setRootNode(swapper2);
        }else{
            exchange1(populationTreeMember.getRootNode());
        }


    }

    private void exchange1(Node node){
        for(int i = 0; i<node.getChildren().size();i++){
            if(node.getChildren().get(i).isSwapFlag()){
                node.getChildren().set(i,swapper2);
                return;
            }
            exchange1(node.getChildren().get(i));
        }

    }

    private void swapNodes2(PopulationTreeMember populationTreeMember){
        if(populationTreeMember.getRootNode().isSwapFlag()){
            populationTreeMember.setRootNode(swapper1);
        }else{
            exchange2(populationTreeMember.getRootNode());
        }

    }
    private void exchange2(Node node){
        for(int i = 0; i<node.getChildren().size();i++){
            if(node.getChildren().get(i).isSwapFlag()){
                node.getChildren().set(i,swapper1);
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






}
