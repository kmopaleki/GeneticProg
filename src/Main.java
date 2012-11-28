import java.util.Random;

/**
 * User: Kevin
 * Date: 11/27/12
 */
public class Main {

    public static void main(String args[]){
        Random random = new Random(12345667L);
        int maxDepth = 10;
        for(int i = 0; i<1000000; i++){
            PopulationTreeMember populationTreeMember = new PopulationTreeMember(random,maxDepth);
            System.out.println(populationTreeMember.getValue(populationTreeMember.getRootNode(),(double)7));
        }


    }
}
