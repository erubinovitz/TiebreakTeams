/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Evan Rubinovitz
 */
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Arrays;
import java.lang.Math;

class Team implements Comparable{
    int index;
    int points;
    int gamesPlayed;
    int gamesWon;
    LinkedList<Integer> teamsPlayed;
    double OMP; //opponent match percentage
    public Team (int index){
        this.index=index;
        points=0;
        gamesPlayed=0;
        gamesWon=0;
        teamsPlayed=new LinkedList<>();
    }
    public int compareTo(Object o){
        Team t2 = (Team)o;
        if (this.gamesWon>t2.gamesWon){
            return 1;
        }
        if (t2.gamesWon>this.gamesWon){
            return -1;
        }
        if (OMP>t2.OMP) return 1;
        return -1;
    }
}

public class BotbTieBreakers {

  /*
  1 loses- 7 plays 8
  1 wins- 7 advances, see code
    */
    /*
    4 wins- 2 and 4 play
    4 loses- none needed
    
    1 1 0 0  
1 0 0 1 
 0 0 0 1 
1 1 0 1 
    
    */

    /*
    0.14179
    0.5408
    0.31741
    0 1 0 0
    1 1 0 0
    1 1 0 0
    1 1 0 1

        Team 1: Farmingdalea E-sports
    Team 2: HiGh eLo
    Team 3: Isaac Fan Club
    Team 4: Revert Zac
    Team 5: TSM Team Sea Men
    Team 6: Hey want to have sex later
    Team 7: SBU Light Blue
    Team 8: Andy's big elo

        */
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

    private static int NUM_TEAMS=10; //must be even. If odd # teams, implement a Bye team with 0 wins.
    private static int NUM_ROBIN=4;
    private static int NUM_CUT=4;
    private static boolean robin=true;
    private static boolean swiss = true;
    private static boolean testing=true;
    private static boolean printThings=false;
    private static int formation =0; // 0 = 1 3 5 7   2 4 6 8, 1 = 1 2 3 4 || 5 6 7 8

    public static int computeSwiss(){
        Team[] teams = new Team[NUM_TEAMS];
        for (int i=0; i<teams.length; i++){
            teams[i] = new Team(i);
        }
        for (int i=0; i<NUM_ROBIN; i++){
            if (printThings)System.out.println("ROUND "+(i+1));
            for (int j=0; j <teams.length; j+=2){
                int swapIndex=1;
                if (teams[j].teamsPlayed.contains(teams[j+swapIndex].index)&&false) {
                    while (teams[j].teamsPlayed.contains(teams[j + swapIndex].index)) {
                        swapIndex++;
                    }
                    Team temp = teams[j+1];
                    teams[j+1]= teams[j+swapIndex];
                    teams[j+swapIndex]=temp;
                    j-=2;
                    continue;
                }

                int rand = (int)(10*(Math.random()));
                if (rand<5){ //t1 win
                    teams[j].gamesWon++;
                    if (printThings)System.out.println(teams[j].index + " beats " + teams[j+1].index);
                }
                else{
                    teams[j+1].gamesWon++;
                    if (printThings)System.out.println(teams[j+1].index + " beats " + teams[j].index);
                }
                teams[j].teamsPlayed.add(teams[j+1].index);
                teams[j+1].teamsPlayed.add(teams[j].index);
            }

            for (int j =0; j<teams.length; j++){
                int oppMatchesWon=0;
                for (int k=0; k < teams[j].teamsPlayed.size(); k++){
                    oppMatchesWon+=teams[teams[j].teamsPlayed.get(k)].gamesWon;
                }
                teams[j].OMP=oppMatchesWon;
            }


            Arrays.sort(teams);
        }
        for (int i=0; i < teams.length; i++){
            if (printThings)System.out.println( (i+1) + ": Team #" +(teams[i].index) + " has a score of " +teams[i].gamesWon + " and OMP of " +teams[i].OMP);
            //System.out.print("they played ");
            for (int j=0; j<teams[i].teamsPlayed.size(); j++){
                //System.out.print(teams[i].teamsPlayed.get(j)+" ");
            }
           // System.out.println();

        }
        if (teams[teams.length-NUM_CUT].gamesWon!=teams[teams.length-NUM_CUT-1].gamesWon){
            return 0;
        }
        if (teams[teams.length-NUM_CUT].OMP!=teams[teams.length-NUM_CUT-1].OMP){
            return 1;
        }

        if (teams[teams.length-NUM_CUT-1].OMP!=teams[teams.length-NUM_CUT-2].OMP || teams[teams.length-NUM_CUT-1].gamesWon!=teams[teams.length-NUM_CUT-2].gamesWon){
            return 2;
        }
        return 3;

    }


    public static void main(String[] args){

        int NUM_TRIALS=1000000;

        int[] vals = new int[4];
        if (swiss){
            for (int i=0; i<NUM_TRIALS; i++){
                vals[ computeSwiss()]++; //0=no ties, 1=tiebreakers w/o games, 2= games needed, 3 - monkagiga
                //false for using, true for testing
                if (printThings)System.out.println();
            }
            for (int i=0; i<4; i++){
                System.out.println((double)(1.0*vals[i]/NUM_TRIALS));
            }
            System.exit(0);
        }
        for (int i=0; i<NUM_TRIALS; i++){
            vals[ compute(testing)]++; //0=no ties, 1=tiebreakers w/o games, 2= games needed, 3 - monkagiga
            //false for using, true for testing
            if (printThings)System.out.println();
        }
        if (!printThings)
            for (int i=0; i<4; i++){
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
            if (formation==0)
                for (int i=0; i<NUM_ROBIN; i++){
                    for (int j=0; j<NUM_TEAMS/2; j++){
                        if (sc.nextInt()==0){ // 0 = first team won
                            scores[j][NUM_TEAMS/2+(i+j+NUM_TEAMS/2)%(NUM_TEAMS/2)]=1;
                        }
                        else{ // 1 = second team won
                            scores[NUM_TEAMS/2+(i+j+NUM_TEAMS/2)%(NUM_TEAMS/2)][j]=1;
                        }
                    }
                }
            else{
                for (int i=0; i<2; i++){ // each team
                    for (int j=0; j<3; j++){ //each match
                        //if (i==j)continue;
                        int a = sc.nextInt();
                        if (i==0 && j==0){
                            if (a==0)
                                scores[0][1]=1;
                            else
                                scores[1][0]=1;
                        }
                        else if (i==0 && j==1){
                            if (a==0)
                                scores[0][2]=1;
                            else
                                scores[2][0]=1;
                        }
                        else if (i==0 && j==2){
                            if (a==0)
                                scores[0][3]=1;
                            else
                                scores[3][0]=1;
                        }
                        else if (i==1 && j==0){
                            if (a==0)
                                scores[2][3]=1;
                            else
                                scores[3][2]=1;
                        }
                        else if (i==1 && j==1){
                            if (a==0)
                                scores[1][3]=1;
                            else
                                scores[3][1]=1;
                        }
                        else if (i==1 && j==2){
                            if (a==0)
                                scores[1][2]=1;
                            else
                                scores[2][1]=1;
                        }
                    }
                }
            }
        }
        else{ //randomly generate values

            for(int i=0; i<NUM_TEAMS; i++){
                teamnames[i]=("a"+i);
            }


            if (robin){
                for (int i=0; i<scores.length; i++){
                    for (int j=0; j<scores[0].length; j++){
                        scores[i][j]=-1;
                        if (i==j)scores[i][j]=0;
                    }
                }
                for (int i=0; i<NUM_TEAMS-1; i++){
                    for (int j=0; j<NUM_TEAMS; j++){
                        if (i==j) continue;
                        if (scores[i][j]!=-1) continue;
                        int r = (int)(10*Math.random());
                        int cond=0;

                        //0,1 are good, 2,3 not good
                        if (i<2&&j<2)cond=5;
                        else if (i>=2 && j>=2) cond=5;
                        else cond=5;
                     //   System.out.println("doot");
                        if (r<cond){ // 0 = first team won
                            scores[j][i]=1;
                            scores[i][j]=0;
                        }
                        else{ // 1 = second team won
                            scores[i][j]=1;
                            scores[j][i]=0;
                        }
                    }
                }
            }
            else
            if (formation==0)
                for (int i=0; i<NUM_ROBIN; i++){
                    for (int j=0; j<NUM_TEAMS/2; j++){
                        int r = (int)(10*Math.random());
                        int cond=0;
                        if (i<2&&j<2)cond=5;
                        else if (i>=2 && j>=2) cond=5;
                        else cond=6;
                        if (r<cond){ // 0 = first team won
                            scores[j][NUM_TEAMS/2+(i+j+NUM_TEAMS/2)%(NUM_TEAMS/2)]=1;
                        }
                        else{ // 1 = second team won
                            scores[NUM_TEAMS/2+(i+j+NUM_TEAMS/2)%(NUM_TEAMS/2)][j]=1;
                        }
                    }
                }
            else
                //1 3 5 7  2 4 6 8
                for (int i=0; i<2; i++){ // each team
                    for (int j=0; j<3; j++){ //each match
                        //if (i==j)continue;
                        int a = (int) (10*Math.random());
                        if (i==0 && j==0){
                            if (a<6)
                                scores[0][1]=1;
                            else
                                scores[1][0]=1;
                        }
                        else if (i==0 && j==1){
                            if (a<7)
                                scores[0][2]=1;
                            else
                                scores[2][0]=1;
                        }
                        else if (i==0 && j==2){
                            if (a<8)
                                scores[0][3]=1;
                            else
                                scores[3][0]=1;
                        }
                        else if (i==1 && j==0){
                            if (a<6)
                                scores[2][3]=1;
                            else
                                scores[3][2]=1;
                        }
                        else if (i==1 && j==1){
                            if (a<7)
                                scores[1][3]=1;
                            else
                                scores[3][1]=1;
                        }
                        else if (i==1 && j==2){
                            if (a<6)
                                scores[1][2]=1;
                            else
                                scores[2][1]=1;
                        }
                    }
                }
        }
        test = false;

        // print out adjacancy matrix
        if (printThings)
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
        int finIndex=0;
        boolean tBreak=false;
        //find which teams make the cut without breakers, which teams need breakers, and discard the rest
        for (int i=0; i < NUM_CUT; i++){
            if (winsTotal[i]>winsTotal[NUM_CUT]){

                if (printThings)   System.out.println(teamnames[teamIndexes[i]]+" advances, as they have " +winsTotal[i] + " wins.");
                numOutputted++;
                if (i==NUM_CUT-1)return 0;
            }
            else {numSpaces= NUM_CUT-i;
                finIndex=i-1;
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
        if (wins.length==3 && wins[0]==wins[1]&&wins[1]==wins[2]){
            if (printThings) {
                System.out.println(teamnames[indexes[0]] + " MONKAMONKA plays " + teamnames[indexes[1]] + " and " + teamnames[indexes[2]] + " plays "
                                + "the 1 win team, winners advance "//".\nIf " +teamnames[indexes[2]]+" wins, it advances no matter what; else winner of"
                        // + " the previous match advances."
                );
                for (int i = 0; i < NUM_TEAMS; i++) {
                    for (int j = 0; j < NUM_TEAMS; j++) {
                        System.out.print(scores[i][j]);

                    }
                    System.out.println();
                }
                System.out.println("\n\n\n");
            }
            return 3;
        }
        if (wins.length==5&&wins[0]==wins[1]&&wins[1]==wins[2]&&wins[2]==wins[3]&&wins[3]==wins[4]){
                if (printThings) {
                for (int i = 0; i < NUM_TEAMS; i++) {
                    for (int j = 0; j < NUM_TEAMS; j++) {
                        System.out.print(scores[i][j]);

                    }
                    System.out.println();
                }
                System.out.println("\n\n\n");
                System.out.println("All teams are 2-2: have each team play a team they have not played yet, starting lowest index to highest.");

                }
            return 2;
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
                            if (printThings)  System.out.println(" i = " + i + " j = " + j + " indexes[i]= " +indexes[i]+ " indexes [j] = "
                                    +indexes[j] + " scores = " +scores[indexes[i]][indexes[j]]);
                        }

                    }
                }
                if (beat){
                    if (printThings)System.out.println(teamnames[indexes[i]] +" beat all other tied teams, so they advance");
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





        if (printThings)
            for (int i=0; i<wins.length; i++){
                System.out.println("wins["+i+"] i " +wins[i]);
            }
        int ret=1;
        String[] tieTeams = new String[numTies];
        int marker=0;
        if (printThings)System.out.println("numTies is " + numTies + " ties left is  " + tiesLeft );
            /* For the remaining, deter if tiebreaking games must be played or if
            outcomes can be decided based on the wins array.
            */
        if (wins[tiesLeft]==wins[tiesLeft-1]){
            int lastIndex =wins.length-1;
            startingIndex=0;
            if (printThings) System.out.println("starting idnex is " +startingIndex + " last is " + lastIndex);
            while (wins[startingIndex]==-1) startingIndex++;
            while (wins[lastIndex]==-1) lastIndex--;
            boolean breakersNeeded=false;
            if (lastIndex+1==numTies)breakersNeeded=true;
            while (startingIndex<lastIndex){
                if (wins[startingIndex]==wins[lastIndex]||breakersNeeded)
                    if (printThings) System.out.println("Tie breakers required for " +teamnames[indexes[startingIndex++]]+ " and " + teamnames[indexes[lastIndex--]]);
                    else{
                        startingIndex++;
                        lastIndex--;
                    }
                else{
                    if (printThings)
                        System.out.println(teamnames[indexes[startingIndex++]]+" advances, because it beat teams with a "
                                + "higher W/L than the other(s). (W score: " + wins[startingIndex-1]+")");
                    else startingIndex++;
                }
            }
            //   System.out.println("TEST");
            return 2;
        }
        for (int i=0; i<numTies; i++){
            if (wins[i]==-1)continue;
            if ((i+1<wins.length&&wins[i]>wins[i+1])
                    ||(i+tiesLeft<wins.length&&wins[i]>wins[i+tiesLeft])
            ){
                if (printThings)    System.out.println(teamnames[indexes[i]]+ " advances, because it beat teams"
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
        if (printThings)while (firstItemIndex<lastItemIndex){
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
    
