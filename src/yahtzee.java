/** 
 *Name: Adiran Abeyta
 *Class: CPSC 224, Spring 2021
 *Assignment: Homework #4
 *Description: A program that plays and scores one hand of Yahtzee.
 */

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class yahtzee
{
    static int numberOfSides;
    int numberOfDice;
    int numberOfRolls;
    static int[] hand;
    static int[] setting;
    static int[] usedRow;
    static GraphicsConfiguration gc;
    JFrame mainWindow;
    
    static Scanner consoleInput = new Scanner(System.in); // creates a new scanner to allow input from console

    public static void main(String args[])
    {
        //create new objects
        playGame playGame = new playGame();
        dice dice = new dice();
        Random rnd = new Random();
        char playAgain = 'y';
        
        rnd.setSeed(20);
        file file = new file();
        setting = new int[3];
        int totalScore = 0;
        //reads the file and sets the the appropriate settings from the previous game before other memory is allocated
        file.readFile(setting);
        //to keep track of which rows have been already used
        usedRow = new int[setting[0] + 7];
        for (int i = 0; i < usedRow.length; i++)
            usedRow[i] = 0;
        //asks if the user wants different settings after each game
        dice.setUpDiceRolls(setting);
        //allocates space for hand once the user has input the number of dice they want to use
        hand = new int[setting[1]];
        //executes the main program loop
        playGame.play(playAgain, hand, setting[1], setting[2], setting[0], usedRow, totalScore);
        numberOfSides = setting[0];
        consoleInput.close();
    }
}

class playGame extends yahtzee
{
    /**
     * Sorts the array
     * 
     * @param array[] the array to be sorted
     * @param size the size of the array being sorted
     */
    void sortArray(int array[], int size)
    {
        Arrays.sort(array);
    }

    /**
     *
     * Play function that gives outputs based on whether or not the user would like
     * to keep the dice
     *
     * @param hand uses the numbers generated for each hand
     * @param playAgain user inputed char that determines if the program plays again
     * @param numberOfDice tracks the number of dice from the user
     * @param numberOfRoles tracks the number of roles per hand from the user
     * @param numberOfSides tracks the number of sides on a dice from the user
     * @param usedRow tracks which rows have already been used
     * @param totalScore tracks total score
     */
    void play(char playAgain, int[] hand, int numberOfDice, int numberOfRolls, int numberOfSides, int[] usedRow, int totalScore)
    {
        //create new objects
        scorecard scorecard = new scorecard();
        dice dice = new dice();

        int bonusYahtzee = 0;
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

            while (turn < numberOfRolls && keepAll != keep)
            {
                //roll dice not kept
                for (int dieNumber = 0; dieNumber < numberOfDice - 1; dieNumber++)
                {
                    if (dieNumber > 0 && dieNumber < keep.length() && keep.charAt(dieNumber) != 'y')
                        hand[dieNumber] = dice.rollDie(numberOfSides);
                }

                //gives the option to display scorecard
                String answer;
                System.out.print("Enter 'S' if you would like to see your scorecard: ");
                answer = consoleInput.nextLine();
                if (answer.equals("S") || answer.equals("s"))
                    System.out.print("Total score: " + totalScore);
                System.out.print("\n");

                //output roll
                System.out.print("Your roll was: ");
                for (int dieNumber = 0; dieNumber < numberOfDice; dieNumber++)
                {
                    System.out.print(hand[dieNumber] + " ");
                }
                System.out.print("\n");

                //if not the last roll of the hand prompt the user for dice to keep
                if (turn < numberOfRolls)
                {
                    System.out.print("enter dice to keep (y or n): ");
                    keep = consoleInput.nextLine();
                    if (keep.length() != numberOfDice)
                    {
                        System.out.print("enter the correct number of dice to keep (y or n): ");
                        keep = consoleInput.nextLine();
                    } 
                }
                
                scorecard.selectLine(numberOfRolls, hand, usedRow, numberOfSides, totalScore);
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
            scorecard.upperScorecard(hand, numberOfSides, usedRow, numberOfRolls, numberOfSides);
            //lower scorecard
            scorecard.lowerScorecard(hand, numberOfSides, totalScore, bonusYahtzee);
            System.out.print("Score " + scorecard.totalAllDice(hand, numberOfRolls) + " on the ");
            System.out.print("Chance line \n");
            System.out.print("\nEnter 'y' to play again: ");
            String in = consoleInput.nextLine();

            if(in.length() > 0)
                playAgain = in.charAt(0);
        }
    }
}

class scorecard extends yahtzee
{
    /**
     * Allows the user to choose which row in the scorecard the score will go into
     *
     * @param totalRolls the total rolls the user has
     * @param hand[] uses the numbers genereated from each hand
     * @param usedRow tracks the rows and their values that have been used in the scorecard
     * @param numSides the number of sides on 1 dice
     * @param totalScore the total score of the game so far
     */
    void selectLine(int totalRolls, int[] hand, int[] usedRow, int numSides, int totalScore)
    {   
        int[] line = new int[numSides + 7]; 
        for (int i = 0; i < numSides + 7; i++)
            line[i] = 0;

        for (int dieValue = 1; dieValue <= numSides; dieValue++)
        {
            //upper card
            int currentCount = 0;
            for (int diePosition = 0; diePosition < numSides - 1 && diePosition < hand.length; diePosition++)
            {
                if (hand[diePosition] == dieValue)
                    currentCount++;
            }
            if (usedRow[dieValue - 1] == 0)
            {
            System.out.print("Score " + dieValue * currentCount + " on the ");
            System.out.print(dieValue + " line" + "\n");
            }
        }
        int j = numSides + 1;
            //lower card
            if (usedRow[j] == 0)
            {
                if (maxOfAKindFound(hand) >= 3)
                {
                    System.out.print("Score " + totalAllDice(hand, numSides) + " on the 3 of a Kind line " + j + "\n");
                    line[j] = totalAllDice(hand, numSides);
                }
                else
                    System.out.print("Score 0 on the 3 of a Kind line " + j + "\n");
            }
            if (usedRow[j + 1] == 0)
            {
                if (maxOfAKindFound(hand) >= 4)
                {
                    System.out.print("Score " + totalAllDice(hand, numSides) + " on the 4 of a Kind line " + (j + 1) + "\n");
                    line[j] = totalAllDice(hand, numSides);
                }
                    else
                    System.out.print("Score 0 on the 4 of a Kind line " + (j + 1) + "\n");
            }
            if (usedRow[j + 2] == 0)
            {
                if (fullHouseFound(hand, numSides))
                {
                    System.out.print("Score 25 on the Full House line " + (j + 2) + "\n");
                    line[j + 2] = 25;
                }    
                else
                    System.out.print("Score 0 on the Full House line " + (j + 2) + "\n");
            }
            if (usedRow[j + 3] == 0)
            {
                if (maxStraightFound(hand) >= 4)
                {
                    System.out.print("Score 30 on the Small Straight line " + (j + 3) + "\n");
                    line[j + 3] = 30;
                }
                else
                    System.out.print("Score 0 on the Small Straight line " + (j + 3) + "\n");
            }
            if (usedRow[j + 4] == 0)
            {
                if (maxStraightFound(hand) >= 5)
                {
                    System.out.print("Score 40 on the Large Straight line " + (j + 4) + "\n");
                    line[j + 4] = 40;
                }
                else
                    System.out.print("Score 0 on the Large Straight line " + (j + 4) + "\n");
            }
            if (usedRow[j + 5] == 0)
            {
                if (maxOfAKindFound(hand) >= 5)
                {
                    System.out.print("Score 50 on the Yahtzee line " + (j + 5) + "\n");
                    line[j + 5] = 50;
                    int bonusYahtzee = 0;
                    bonusYahtzee = bonusYahtzee + 1;
                }
                else
                    System.out.print("Score 0 on the Yahtzee line " + (j + 5) +"\n");
            }
        
        System.out.print("Select line to save score: ");
        
        //sets user's choice to true in usedRow
        int tmp = Integer.parseInt(consoleInput.nextLine());
        usedRow[tmp - 1] = -1;
        totalScore = totalScore + line[tmp - 1];
        System.out.print("\n");
    }

    /**
     * Produces the outputs for the upper scorecard
     *
     * @param usedRow tracks the rows and their values that have been used in the scorecard
     * @param bonusYahtzee tracks the number of yahtzee per game
     * @return the total score
     */
   int calculateBonus(int totalScore, int bonusYahtzee)
    {
        if (totalScore >= 63)
            totalScore = totalScore + 35;
        if (bonusYahtzee > 1)
            totalScore = totalScore + ((bonusYahtzee - 1) * 100);

        return totalScore;
    }

    /**
     * Produces the outputs for the upper scorecard
     *
     * @param hand uses the numbers generated for each hand
     * @param numberOfSides the number of sides on each dice
     * @param usedRow tracks the rows and their values that have been used in the scorecard
     * @param rolls tracks the number of rolls per game
     * @param numSides tracks the number of sides per dice per game
     */
    void upperScorecard(int[] hand, int numberOfSides, int[] usedRow, int rolls, int numSides)
    {
        for (int dieValue = 1; dieValue <= numSides; dieValue++)
        {
            int currentCount = 0;
            for (int diePosition = 0; diePosition < numberOfSides - 1 && diePosition < hand.length; diePosition++)
            {
                if (hand[diePosition] == dieValue)
                    currentCount++;
            }
        
            System.out.print("Score " + dieValue * currentCount + " on the ");
            System.out.print(dieValue + " line" + "\n");
        }
    }

    /**
     * Produces the outputs for the lower scorecard
     *
     * @param hand[] uses the numbers generated for each hand
     * @param setting the number of dice rolls in one game
     * @param bonusYahtzee tracks the number of yahtzees
     */
    void lowerScorecard(int[] hand, int setting, int totalScore, int bonusYahtzee)
    {
        if (maxOfAKindFound(hand) >= 3)
            System.out.print("Score " + totalAllDice(hand, setting) + " on the 3 of a Kind line \n");
        else
            System.out.print("Score 0 on the 3 of a Kind line \n");
        if (maxOfAKindFound(hand) >= 4)
            System.out.print("Score " + totalAllDice(hand, setting) + " on the 4 of a Kind line \n");
        else
            System.out.print("Score 0 on the 4 of a Kind line \n");
        if (fullHouseFound(hand, setting))
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
        System.out.print("Grand total: " + calculateBonus(totalScore, bonusYahtzee) + "\n");
    }

    /**
     * Returns the length of the longest straight found in a hand
     *
     * @param hand[] uses the numbers generated for each hand
     * @return maxLength maximum length of a straight found
     */
    int maxStraightFound(int hand[])
    {
        int maxLength = 1;
        int curLength = 1;
        for (int counter = 0; counter < 4; counter++)
        {
            if (counter + 1<hand.length && hand[counter] + 1 == hand[counter + 1]) // jump of 1, check index of bound
                curLength++;
            else if (counter + 1<hand.length && hand[counter] + 1 < hand[counter + 1]) // jump of >= 2
                curLength = 1;
            if (curLength > maxLength)
                maxLength = curLength;
        }
        return maxLength;
    }

    /**
     * Returns true if the hand is a full house or false if it does not
     *
     * @param hand[] uses the numbers generated for each hand
     * @param numberOfSides the number of sides on each dice
     * @return foundFH true if a full hand is found
     */

    boolean fullHouseFound(int hand[], int numberOfSides)
    {
        boolean foundFH = false;
        boolean found3K = false;
        boolean found2K = false;
        int currentCount;

        for (int dieValue = 1; dieValue <= numberOfSides; dieValue++)
        {
            currentCount = 0;
            for (int diePosition = 0; diePosition < 5 && diePosition < hand.length; diePosition++)//add index out of check
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
    * Finds the count of the most common number in a hand
    *
    * @param hand[] uses the numbers generated for each hand
    * @return maxCount the count of the die value occuring most in the hand
    */
    int maxOfAKindFound(int hand[])
    {
        int maxCount = 0;
        int currentCount;
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
     * Returns the total value of all dice in a hand
     *
     * @param hand[] uses the numbers generated for each hand
     * @param diceRolls the number of dice rolls in one game
     * @return total total of all 5 dice rolls
     */
    int totalAllDice(int hand[], int diceRolls)
    {
        int total = 0;
        for (int diePosition = 0; diePosition < diceRolls && diePosition < hand.length; diePosition++)
        {
            total += hand[diePosition];
        }
        return total;
    }
}

class file extends yahtzee
{
    /**
     * This function reads the input file and sets the game rules
     *
     * @param setting tracks the user inputted settings
     */
    void readFile(int[] setting)
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
     * This function uses a scanner to save the game settings in the
     * yahtzeeConfig.txt
     *
     * @param setting tracks the user inputted settings
     */
    void writeFile(int[] setting)
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

class dice extends yahtzee
{
    /*
     * Simulates the rolling of a single die
     *
     * @param number the number of sides on a dic
     * @return roll returns the number from 1 die roll
     */
    int rollDie(int number)
    {
        Random rnd = new Random();
        int roll = rnd.nextInt(number - 1 + 1) + 1;
        return roll;
    }

    /**
     * This function allows the user tp use previous game settings or create their
     * own
     *
     * @param setting tracks the user inputted settings
     */
    void setUpDiceRolls(int[] setting)
    {
        file file = new file();
        //initialize
        char changeDice = 'n';
        System.out.print("You are playing with " + setting[1] + " " + setting[0] + "-sided dice\n");
        System.out.print("You get " + setting[2] + " rolls per turn\n");
        System.out.print("Enter 'y' if you would like to change the configuration: ");
        changeDice = consoleInput.nextLine().charAt(0);

        if (changeDice == 'y')
        {
            int tmp;
            System.out.print("Enter the number of sides on each die: ");
            tmp = Integer.parseInt(consoleInput.nextLine());
            setting[0] = tmp;
            System.out.print("Enter the number of dice in play: ");
            tmp = Integer.parseInt(consoleInput.nextLine());
            setting[1] = tmp;
            System.out.print("Enter the number of rolls per hand: ");
            tmp = Integer.parseInt(consoleInput.nextLine());
            setting[2] = tmp;

            //save settings for next game
            file.writeFile(setting);
        }
    }

    int getDiceRolled(int position)
    {
        return hand[position];
    }
}
