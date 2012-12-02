import java.util.ArrayList;

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

    private void treeCrossover(PopulationTreeMember parent1, PopulationTreeMember parent2){

    }

    private void fitnessProportionalSelection(ArrayList<PopulationTreeMember> population){

    }

    private void TruncationSurvivalSelection(ArrayList<PopulationTreeMember> population){

    }

    private void treeMutation(ArrayList<PopulationTreeMember> childPopulation){

    }

    public String getFormula(Node node){
        if(node.getOperation().equals("sin"))
        {
            return "sin("+ getFormula(node.getChildren().get(0)) + ")";
        }else if(node.getOperation().equals("cos")){
            return "cos("+getFormula(node.getChildren().get(1)) + ")";
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
}
