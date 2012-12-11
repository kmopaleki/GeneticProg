import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * User: Kevin
 * Date: 11/27/12
 */
public class Main {

    public static void main(String args[]) throws IOException {

        File inFile = new File(args[0]);
        Scanner s = new Scanner(inFile);
        int numberOfRuns = 0;
        int numberOfEvals = 0;
        int populationSize = 0;
        int probabilityOfRecombination = 17;
        int mutationDepth = 0;
        long randomSeed = 0L;
        int maxDepth = 3;
        int numParents=0;
        int kParents = 0;
        int numChildren = 0;
        String solutionFile = "";
        String logFile = "";
        String dataFile = "";
        double penaltyScaler = 0.0;
        int counter = 0;
        int mutationProbability = 0;

        while(s.hasNext()){
            if(counter == 0){
                dataFile = s.next();
            }else if(counter ==1){
                logFile = s.next();
            }else if(counter==2){
                solutionFile = s.next();
            }else if(counter==3){
                populationSize = Integer.parseInt(s.next());
            }else if(counter == 4){
                numberOfRuns = Integer.parseInt(s.next());
            }else if(counter ==5){
                numberOfEvals = Integer.parseInt(s.next());
            }else if(counter == 6){
                randomSeed = Long.parseLong(s.next());
            }else if(counter == 7){
                maxDepth = Integer.parseInt(s.next());

            }else if(counter == 8){
                penaltyScaler = Double.parseDouble(s.next());
            }else if(counter == 9){
                probabilityOfRecombination = Integer.parseInt(s.next());
            }else if(counter == 10){
                numParents = Integer.parseInt(s.next());
            }else if(counter == 11){
                kParents = Integer.parseInt(s.next());
            }else if(counter == 12){
                mutationDepth = Integer.parseInt(s.next());
            }else if(counter == 13){
                mutationProbability = Integer.parseInt(s.next());
            }else if(counter == 14){
                numChildren = Integer.parseInt(s.next());
            }

            counter++;
        }



        GeneticProgram geneticProgram = new GeneticProgram(logFile,solutionFile,dataFile,numberOfRuns,
                numberOfEvals,populationSize,probabilityOfRecombination,randomSeed,maxDepth,penaltyScaler,numParents,
                kParents,mutationDepth,mutationProbability,numChildren);
        geneticProgram.GP();


    }

}
