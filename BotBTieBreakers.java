/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Evan Rubinovitz
 */
import java.util.Scanner;
import java.util.Arrays;
import java.lang.Math;

public class BotBTieBreakers {

  

/*
INPUT NOTES!!! PLEASE READ
Start by entering each team name followed by a new line (enter).
Then, enter the results of each round seperated by a new line. I have assumed that the matches will be as follows:
First round: 1 plays 5, 2 plays 6, 3 plays 7, 4 plays 8
Second: 1 plays 6, 2 plays 7, 3 plays 8, 4 plays 5
Third: 1 plays 7, 2 plays 8, 3 plays 5, 4 plays 6
Fourth: 1 plays 8, 2 plays 5, 3 plays 6, 4 plays 7
For each round and each match, enter a 0 if the first team won and a 1 if the second team won.
For example, if the winners in the irst round were 6, 2, 3, and 4, the input for the first
round should be: 1 0 0 0.
*/
private static int NUM_TEAMS=8;
private static int NUM_ROBIN=4;
private static int NUM_CUT=NUM_TEAMS/2;
public static void main(String[] args){
    int NUM_TRIALS=100;
    boolean ratios = false; // set to true to see percentage of times certain events happen
    int[] vals = new int[3];
    for (int i=0; i<NUM_TRIALS; i++){
    vals[ compute(false)]++; //0=no ties, 1=tiebreakers w/o games, 2= games needed
    //false for using, true for testing
    System.out.println();
    }
    if (ratios)
    for (int i=0; i<3; i++){
        System.out.println((double)(1.0*vals[i]/NUM_TRIALS));
    }
}
    public static int compute(boolean test) {
        String[] teamnames = new String[NUM_TEAMS];
        int[][] scores = new int[NUM_TEAMS][NUM_TEAMS];
        Scanner sc = new Scanner(System.in);
        if (!test){ // let user input values
                System.out.println("Enter each team named seperated by a new line (enter).");
            for (int i=0; i<NUM_TEAMS; i++){
                teamnames[i]=sc.nextLine();
            }
            System.out.println("enter the results of each round seperated by a new line. I have assumed that the matches will be as follows:\n" +
                "First round: 1 plays 5, 2 plays 6, 3 plays 7, 4 plays 8\n" +
                "Second: 1 plays 6, 2 plays 7, 3 plays 8, 4 plays 5\n" +
                "Third: 1 plays 7, 2 plays 8, 3 plays 5, 4 plays 6\n" +
                "Fourth: 1 plays 8, 2 plays 5, 3 plays 6, 4 plays 7\n" +
                "For each round and each match, enter a 0 if the first team won and a 1 if the second team won.\n" +
                "For example, if the winners in the irst round were 6, 2, 3, and 4, the input for the first\n" +
                "round should be: 1 0 0 0.");

            for (int i=0; i<NUM_TEAMS/2; i++){
                for (int j=0; j<NUM_ROBIN; j++){
                    if (sc.nextInt()==0){ // 0 = first team won
                        scores[j][NUM_TEAMS/2+(i+j+NUM_TEAMS/2)%(NUM_TEAMS/2)]=1;
                    }
                    else{ // 1 = second team won
                        scores[NUM_TEAMS/2+(i+j+NUM_TEAMS/2)%(NUM_TEAMS/2)][j]=1;
                    }
                }
            }
        }
        
        else{ //randomly generate values
            for(int i=0; i<NUM_TEAMS; i++){
                teamnames[i]=("a"+i);
            }
            for (int i=0; i<NUM_TEAMS/2; i++){
                for (int j=0; j<NUM_ROBIN; j++){
                    int r = (int)(2*Math.random());
                    if (r==0){ // 0 = first team won
                    scores[j][NUM_TEAMS/2+(i+j+NUM_TEAMS/2)%(NUM_TEAMS/2)]=1;
                }
                else{ // 1 = second team won
                    scores[NUM_TEAMS/2+(i+j+NUM_TEAMS/2)%(NUM_TEAMS/2)][j]=1;
                }
                }
            }
        }

        
      // print out adjacancy matrix
        for (int i=0; i<NUM_TEAMS; i++){
            for (int j=0; j<NUM_TEAMS; j++){
                System.out.print(scores[i][j]);
                
            }
            System.out.println();
        }
        // calculate number of wins each team got, and record indexes after sorting
            int[] winsTotal = new int[NUM_TEAMS];
            int[] teamIndexes = new int[NUM_TEAMS];
            for (int i=0; i<NUM_TEAMS; i++)
                teamIndexes[i]=i;
          
            
            for (int i=0; i<NUM_TEAMS; i++){
                for (int j=0; j<NUM_TEAMS; j++){
                    
                            winsTotal[i]+=scores[i][j];
                        
               
                }
            }
            bubbleSort(winsTotal,teamIndexes);
            int numTies=0;
            int numSpaces=0;
        int numOutputted=0;
        boolean tBreak=false;
        //find which teams make the cut without breakers, which teams need breakers, and discard the rest
        for (int i=0; i < NUM_CUT; i++){
            if (winsTotal[i]>winsTotal[NUM_CUT]){
                System.out.println(teamnames[teamIndexes[i]]+" advances, as they have " +winsTotal[i] + " wins.");
                numOutputted++;
                if (i==NUM_CUT-1)return 0;
            }
            else {numSpaces= NUM_CUT-i;
                numTies=0;
                for (int j=i; j<NUM_TEAMS; j++){
                    if (winsTotal[i]==winsTotal[j]){

                        numTies++;
                    }
                    else {
                        tBreak=true;
                        break;
                    }
                }
            }
            if (tBreak)break;
        }
        


            int tiesLeft=numSpaces;
            boolean exit = false;
            boolean toBreak=false;
            
           /* wins array represents the number of wins received by the team that a team beat.
            For example, if Team A beat team B, C, and D, and Team B, C, and D have 2, 3, and 0 wins
            respectively, Team A's index in the wins array will equal 5.
            */
            int[] wins = new int[numTies];
            int startingIndex=numOutputted;
            int[] indexes = new int[numTies];
            for (int i=0; i<numTies;i++){
                indexes[i]=teamIndexes[startingIndex++];
            }
            
            for (int i=0; i<numTies; i++){
                for (int j=0; j<NUM_TEAMS; j++){
                    if (scores[indexes[i]][j]==1)
                        for (int k=0; k<NUM_TEAMS; k++){
                            wins[i]+=scores[j][k];
                        }
               
                }
            }
            
            int[] printedNames = new int[NUM_TEAMS];
            /*Check to see if a tiebreaking team has beaten all other teams.
            If so, cut it to the top and keep checking until it is confirmed there are none.
            */
             for (int k=0; k<numSpaces; k++){
                for (int i=0; i<numTies; i++){
                    boolean beat = true;
                    for (int j=0; j<numTies; j++){

                        if (printedNames[indexes[j]]==0&&(scores[indexes[i]][indexes[j]]==0&&i!=j)||( i!=j&&
                                ((indexes[i]<NUM_TEAMS/2&&indexes[j]<NUM_TEAMS/2) || 
                                (indexes[i]>=NUM_TEAMS/2&&indexes[j]>=NUM_TEAMS/2) )   )                             ){
                            beat=false;
                            if (i==0){
                                System.out.println(" i = " + i + " j = " + j + " indexes[i]= " +indexes[i]+ " indexes [j] = " 
                                +indexes[j] + " scores = " +scores[indexes[i]][indexes[j]]);
                            }

                        }
                    }
                    if (beat){
                        if (!test)System.out.println(teamnames[indexes[i]] +" beat all other tied teams, so they advance");
                        printedNames[indexes[i]]=1;
                        wins[i]=-1;
                        toBreak=true;
                        tiesLeft--;
                        if(tiesLeft==0) return 1;
                        break;
                        
                    }
                    if (toBreak )break;
                }
                if (toBreak) break;
            }
            //calculate wins
            if (exit)
               return 1;
            
            
            
            bubbleSort(wins,indexes);

         
        
            
            

            for (int i=0; i<wins.length; i++){
                System.out.println("wins["+i+"] i " +wins[i]);
            }
            int ret=1;
            String[] tieTeams = new String[numTies];
            int marker=0;
            System.out.println("numTies is " + numTies + " ties left is  " + tiesLeft );
            /* For the remaining, deter if tiebreaking games must be played or if
            outcomes can be decided based on the wins array.
            */
            if (wins[tiesLeft]==wins[tiesLeft-1]){
                int lastIndex =wins.length-1;
                startingIndex=0;
                System.out.println("starting idnex is " +startingIndex + " last is " + lastIndex);
                while (wins[startingIndex]==-1) startingIndex++;
                while (wins[lastIndex]==-1) lastIndex--;
                boolean breakersNeeded=false;
                if (lastIndex+1==numTies)breakersNeeded=true;
                while (startingIndex<lastIndex){
                    if (wins[startingIndex]==wins[lastIndex]||breakersNeeded)
                    System.out.println("Tie breakers required for " +teamnames[indexes[startingIndex++]]+ " and " + teamnames[indexes[lastIndex--]]);
                    else{
                        System.out.println(teamnames[indexes[startingIndex++]]+" advances, because it beat teams with a "
                                + "higher W/L than the other(s). (W score: " + wins[startingIndex-1]+")");
                    }
                }
                System.out.println("TEST");
                return 2;
            }
            for (int i=0; i<numTies; i++){
                if (wins[i]==-1)continue;
                if ((i+1<wins.length&&wins[i]>wins[i+1])
                        ||(i+tiesLeft<wins.length&&wins[i]>wins[i+tiesLeft])
                        ){
                    if (!test)    System.out.println(teamnames[indexes[i]]+ " advances, because it beat teams"
                       + " with a higher W/L than the other(s). (W Score: " + wins[i]+")");
                    tiesLeft--;
                    if (tiesLeft==0) return 1;
                   
                }
                else if (i+1<wins.length){
                   // System.out.println("i is " + i);
                   // System.out.println("wins[i]="+wins[i]+"\nwins[i+1]="+wins[i+1]);
                    tieTeams[marker++]=teamnames[indexes[i]];
                    tieTeams[marker++]=teamnames[indexes[i+1]];
              
                   ret=2;
           
                   i++;
                }
            }
            int lastItemIndex=numTies-1;
            for (int i=0; i<numTies; i++){
                if (tieTeams[i]==null){
                    lastItemIndex=i-1;
                    break;
                }
            }
            //Pair tiebreakers from top score to bottom score (common practice in tournament seeding)
            int firstItemIndex=0;
            if (!test)while (firstItemIndex<lastItemIndex){
                System.out.println("Tie breaker required for " + tieTeams[firstItemIndex++] + " and " +tieTeams[lastItemIndex--]);
            }
            return ret;
            
        
                
    }
    public static void bubbleSort(int[] arr, String[] arr2) { 
        int n = arr.length; 
        for (int i = 0; i < n-1; i++) 
            for (int j = 0; j < n-i-1; j++) 
                if (arr[j] > arr[j+1]) 
                { 
                    // swap temp and arr[i] 
                    int temp = arr[j]; 
                    arr[j] = arr[j+1]; 
                    arr[j+1] = temp; 
                    String temp2=arr2[j];
                    arr2[j]=arr2[j+1];
                    arr2[j+1]=temp2;
                } 
    } 
      public static void bubbleSort(int[] arr, int[] arr2) { 
        int n = arr.length; 
        for (int i = 0; i < n-1; i++) 
            for (int j = 0; j < n-i-1; j++) 
                if (arr[j] < arr[j+1]) 
                { 
                    // swap temp and arr[i] 
                    int temp = arr[j]; 
                    arr[j] = arr[j+1]; 
                    arr[j+1] = temp; 
                    int temp2=arr2[j];
                    arr2[j]=arr2[j+1];
                    arr2[j+1]=temp2;
                } 
    } 
      public static boolean contains(int[] arr, int key){
          for (int i=0; i<arr.length; i++)
              if (arr[i]==key)
                  return true;
          return false;
      }
    
}
    
