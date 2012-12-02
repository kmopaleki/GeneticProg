import java.util.Random;

/**
 * User: Kevin
 * Date: 11/27/12
 */
public class Main {

    public static void main(String args[]){
        Random random = new Random(1234567L);
        int maxDepth = 5;
        for(int i = 0; i<100; i++){
            PopulationTreeMember populationTreeMember = new PopulationTreeMember(random,maxDepth);
            System.out.println(populationTreeMember.getFormula(populationTreeMember.getRootNode()));
            System.out.println(populationTreeMember.getError(12.00,7.0));
        }


    }

}
