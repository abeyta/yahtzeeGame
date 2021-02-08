/** 
 *Name: Adiran Abeyta
 *Class: CPSC 224, Spring 2021
 *Assignment: Homework #1
 *Description: A program that plays and scores one hand of Yahtzee.
 */

import java.util.*;

/** 
Completes a full game of Yahtzee by calculating rolls, keeping score, and taking input from the user.
 * 
 * 
*/

class Yahtzee
{
    static int numberOfSides = 6;
    static int numberOfDice = 5;

    /** 
    Main function of the program that calls play to execute 
    * 
    */
    public static void main(String args[])
    {
        int[] hand;
        hand = new int[numberOfDice];
        char playAgain = 'y';
        Random rnd = new Random();
        rnd.setSeed(20);

        //executes the main program loop
        play(playAgain, hand);
    }

    /** 
    Produces the outputs for the upper scorecard 
    * 
    *@param hand[] uses the numbers generated for each hand
    */
    static void upperScorecard(int[] hand)
    {
        for (int dieValue = 1; dieValue <= numberOfSides; dieValue++)
        {
            int currentCount = 0;
            for (int diePosition = 0; diePosition < 5; diePosition++)
            {
                if (hand[diePosition] == dieValue)
                    currentCount++;
            }
            System.out.print("Score " + dieValue * currentCount + " on the ");
            System.out.print(dieValue + " line" + "\n");
        }
    }

    /** 
    Produces the outputs for the lower scorecard
    * 
    *@param hand[] uses the numbers generated for each hand
    */
    static void lowerScorecard(int[] hand)
    {
        if (maxOfAKindFound(hand) >= 3)
            {
                System.out.print("Score " + totalAllDice(hand) + " on the ");
                System.out.print("3 of a Kind line \n");
            }
        else    System.out.print("Score 0 on the 3 of a Kind line \n");

        if (maxOfAKindFound(hand) >= 4)
        {
            System.out.print("Score " + totalAllDice(hand) + " on the ");
            System.out.print("4 of a Kind line \n");
        }
        else    System.out.print("Score 0 on the 4 of a Kind line \n");

        if (fullHouseFound(hand))
            System.out.print("Score 25 on the Full House line \n");
        else
            System.out.print("Score 0 on the Full House line \n");

        if (maxStraightFound(hand) >= 4)
            System.out.print("Score 30 on the Small Straight line \n");
        else
            System.out.print("Score 0 on the Small Straight line \n");

        if (maxStraightFound(hand) >= 5)
            System.out.print("Score 40 on the Large Straight line \n");
        else
            System.out.print("Score 0 on the Large Straight line \n");

        if (maxOfAKindFound(hand) >= 5)
            System.out.print("Score 50 on the Yahtzee line \n");
        else
            System.out.print("Score 0 on the Yahtzee line \n");
    }

    /** 
    Simulates the rolling of a single die
    * 
    *@return roll returns the number from 1 die roll
    */
    static int rollDie()
    {
        Random rnd = new Random();
        int roll = rnd.nextInt(6);

        return roll;
    }

    /** 
    Finds the count of the most common number in a hand
    * 
    *@param hand[] uses the numbers generated for each hand
    *@return maxCount the count of the die value occuring most in the hand
    */
    static int maxOfAKindFound(int hand[])
    {
        int maxCount = 0;
        int currentCount ;

        for (int dieValue = 1; dieValue <= numberOfSides; dieValue++)
        {
            currentCount = 0;
            for (int diePosition = 0; diePosition < 5; diePosition++)
            {
                if (hand[diePosition] == dieValue)
                    currentCount++;
            }
            if (currentCount > maxCount)
                maxCount = currentCount;
        }

        return maxCount;
    }

    /** 
    Returns the total value of all dice in a hand
    * 
    *@param hand[] uses the numbers generated for each hand
    *@return total total of all 5 dice rolls
    */
    static int totalAllDice(int hand[])
    {
        int total = 0;

        for (int diePosition = 0; diePosition < 5; diePosition++)
        {
            total += hand[diePosition];
        }

        return total;
    }

    /** 
    Sorts the array
    * 
    *@param array[] the array to be sorted
    *@param size the size of the array being sorted
    */
    static void sortArray(int array[], int size)
    {
        Arrays.sort(array);
    }

    /** 
    Returns the length of the longest straight found in a hand
    * 
    *@param hand[] uses the numbers generated for each hand
    *@return maxLength maximum length of a straight found
    */
    static int maxStraightFound(int hand[])
    {
        int maxLength = 1;
        int curLength = 1;

        for(int counter = 0; counter < 4; counter++)
        {
            if (hand[counter] + 1 == hand[counter + 1] ) //jump of 1
                curLength++;
            else if (hand[counter] + 1 < hand[counter + 1]) //jump of >= 2
                curLength = 1;
            if (curLength > maxLength)
                maxLength = curLength;
        }

        return maxLength;
    }

    /** 
    Returns true if the hand is a full house or false if it does not
    * 
    *@param hand[] uses the numbers generated for each hand
    *@return foundFH true if a full hand is found
    */
    static boolean fullHouseFound(int hand[])
    {
        boolean foundFH = false;
        boolean found3K = false;
        boolean found2K = false;
        int currentCount ;

        for (int dieValue = 1; dieValue <= numberOfSides; dieValue++)
        {
            currentCount = 0;
            for (int diePosition = 0; diePosition < 5; diePosition++)
            {
                if (hand[diePosition] == dieValue)
                    currentCount++;
            }
            if (currentCount == 2)
                found2K = true;
            if (currentCount == 3)
                found3K = true;
        }
        if (found2K && found3K)
            foundFH = true;

        return foundFH;
    }

    /** 
    Play function that gives outputs based on whether or not the user would like to keep the dice
    * 
    *@param hand[] uses the numbers generated for each hand
    *@param playAgain user inputed char that determines if the program plays again
    */
    static void play(char playAgain, int[] hand)
    {
        Scanner consoleInput = new Scanner(System.in); //creates a new scanner to allow input from console

        while (playAgain == 'y')
        {
            String keep = "nnnnn"; //setup to roll all dice in the first roll
            int turn = 1;
            while (turn < 4 && keep != "yyyyy")
            {
                //roll dice not kept
                for (int dieNumber = 0; dieNumber < numberOfDice; dieNumber++)
                {
                    if (keep.charAt(dieNumber) != 'y')
                        hand[dieNumber] = rollDie();
                }
                //output roll
                System.out.print("Your roll was: ");
                for (int dieNumber = 0; dieNumber < numberOfDice; dieNumber++)
                {
                    System.out.print(hand[dieNumber] );
                }
                System.out.print("\n");
                //if not the last roll of the hand prompt the user for dice to keep
                if (turn < 3)
                {  
                    System.out.print("enter dice to keep (y or n) ");
                    keep = consoleInput.nextLine();
                }
                turn++;
            }
            //start scoring
            //hand need to be sorted to check for straights
            sortArray(hand, numberOfDice);
            System.out.print("Here is your sorted hand : ");
            
            for (int dieNumber = 0; dieNumber < numberOfDice; dieNumber++)
                {
                    System.out.print(hand[dieNumber]);
                }
            System.out.print("\n");

            //upper scorecard
            upperScorecard(hand);

            //lower scorecard
            lowerScorecard(hand);

            System.out.print("Score " + totalAllDice(hand) + " on the ");
            System.out.print("Chance line \n");
            System.out.print("\nEnter 'y' to play again: ");
            playAgain = consoleInput.nextLine().charAt(0);
        } 
        consoleInput.close();
    }
}
