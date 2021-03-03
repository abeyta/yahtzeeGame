/** 
 *Name: Adiran Abeyta
 *Class: CPSC 224, Spring 2021
 *Assignment: Homework #2
 *Description: A program that plays and scores one hand of Yahtzee.
 */

import java.util.*;
import java.io.*;

public class yahtzee
{
    static int numberOfSides;
    static int numberOfDice;
    static int numberOfRolls;
    static Scanner consoleInput = new Scanner(System.in); //creates a new scanner to allow input from console

    public static void main(String args[])
    {
        char playAgain = 'y';
        Random rnd = new Random();
        rnd.setSeed(20);
        int[] setting = new int[3];

        //reads the file and sets the the appropriate settings from the previous game before other memory is allocated
        file.readFile(setting);
        //asks if the user wants different settings after each game
        dice.setUpDiceRolls(setting);

        //allocates space for hand once the user has input the number of dice they want to use
        int[] hand = new int[setting[1]];
        
        //executes the main program loop
        play(playAgain, hand, setting[1], setting[2], setting[0]);
        consoleInput.close();
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
    Play function that gives outputs based on whether or not the user would like to keep the dice
    * 
    *@param hand[] uses the numbers generated for each hand
    *@param playAgain user inputed char that determines if the program plays again
    *@param numberOfDice tracks the number of dice from the user
    *@param numberOfRoles tracks the number of roles per hand from the user
    *@param numberOfSides tracks the number of sides on a dice from the user
    */
    static void play(char playAgain, int[] hand, int numberOfDice, int numberOfRolls, int numberOfSides)
    {

        while (playAgain == 'y')
        {
            char[] temp = new char[numberOfDice];
            char[] temp2 = new char[numberOfDice];
            //allocates corect amount of space within keep to acoomodate for various number of die
            for (int i = 0; i < numberOfDice; i++)
            {
                temp[i] = 'n';
                temp2[i] = 'y';
            }
            String keep = String.copyValueOf(temp);
            String keepAll = String.copyValueOf(temp2);
            int turn = 1;
            String answer;

            while (turn < numberOfRolls && keepAll != keep)
            {
                //roll dice not kept
                for (int dieNumber = 0; dieNumber < numberOfDice - 1; dieNumber++)
                {
                    if (keep.charAt(dieNumber) != 'y')
                        hand[dieNumber] = dice.rollDie(numberOfSides);
                }
                //gives the option to display scorecard
                System.out.print("Enter 'S' if you would like to see your scorecard: ");
                answer = consoleInput.nextLine();
                if (answer.charAt(0) == 'S')
                {
                    scorecard.upperScorecard(hand);
                    scorecard.lowerScorecard(hand);
                    System.out.print("\n");
                }
                //output roll
                System.out.print("Your roll was: ");
                for (int dieNumber = 0; dieNumber < numberOfDice; dieNumber++)
                {
                    System.out.print(hand[dieNumber]);
                }
                System.out.print("\n");
                //if not the last roll of the hand prompt the user for dice to keep
                if (turn < numberOfRolls)
                {  
                    System.out.print("enter dice to keep (y or n): ");
                    keep = consoleInput.nextLine();

                    //lets user re-enter dice to keep if wrong number of answers is entered
                    if (check(keep) != true)
                    {
                        System.out.print("re-enter dice to keep (y or n): ");
                        keep = consoleInput.nextLine();
                    }
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
            scorecard.upperScorecard(hand);

            //lower scorecard
            scorecard.lowerScorecard(hand);

            System.out.print("Score " + scorecard.totalAllDice(hand) + " on the ");
            System.out.print("Chance line \n");
            System.out.print("\nEnter 'y' to play again: ");
            playAgain = consoleInput.nextLine().charAt(0);
        }
    }

    /** 
    Check function that check the keep string for the corrent number of inputs
    * 
    *@param checkInput the string to be checked
    */
    static boolean check(String checkInput)
    {
        int count = 0;
        for (int i = 0; i < numberOfDice; i++)
        {
            if (checkInput.charAt(i) == 'y')
            {
                count++;
            }
            else if (checkInput.charAt(i) == 'n')
            {
                count++;
            }
        }
        if (count == numberOfDice)
        {
            return true;
        }
        else
        {
            return false;
        } 
    }
}

class dice extends yahtzee
{
    /** 
    Simulates the rolling of a single die
    * 
    *@param number the number of sides on a dice
    *@return roll returns the number from 1 die roll
    */
    static int rollDie(int number)
    {
        Random rnd = new Random();
        int roll = rnd.nextInt(number - 1) + 1;

        return roll;
    }

    /** 
    This function allows the user tp use previous game settings or create their own
     * 
     * @param setting tracks the user inputted settings
     */
    static void setUpDiceRolls(int[] setting)
    {

        char changeDice = 'n';

        System.out.print("You are playing with " + setting[1] + " " + setting[0] + "-sided dice\n");
        System.out.print("You get " + setting[2] + " rolls per turn\n");
        System.out.print("Enter 'y' if you would like to change the configuration: ");
        changeDice = consoleInput.nextLine().charAt(0);

        if (changeDice == 'y')
        {
            System.out.print("Enter the number of sides on each die: ");
            setting[0] = consoleInput.nextInt();
            System.out.print("Enter the number of dice in play: ");
            setting[1] = consoleInput.nextInt();
            System.out.print("Enter the number of rolls per hand: ");
            setting[2] = consoleInput.nextInt();
            file.writeFile(setting);
        }
    }
}

class file extends yahtzee
{
    /** 
    This function reads the input file and sets the game rules
     * 
     * @param setting tracks the user inputted settings
     */
    static void readFile(int[] setting)
    {  
        try
        {
            File inFile = new File("yahtzeeConfig.txt");
            Scanner scanner = new Scanner(inFile);

            while (scanner.hasNextLine())
            {
                setting[0] = Integer.parseInt(scanner.nextLine());
                setting[1] = Integer.parseInt(scanner.nextLine());
                setting[2] = Integer.parseInt(scanner.nextLine());
            }
            scanner.close();
        }
        catch (FileNotFoundException e) 
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }   
    }

    /** 
     This function uses a scanner to save the game settings in the yahtzeeConfig.txt
     * 
     * @param setting tracks the user inputted settings
     */
    static void writeFile(int[] setting)
    {
        try
        {
            FileWriter writer = new FileWriter("yahtzeeConfig.txt");
            writer.write(setting[0] + "\n" + setting[1] + "\n" + setting[2]);
            writer.close();
        } 
        catch (IOException e) 
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

class scorecard extends yahtzee
{
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
            for (int diePosition = 0; diePosition < numberOfSides - 1; diePosition++)
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
}