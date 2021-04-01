import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.swing.border.Border;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.*;
import javax.naming.ldap.ManageReferralControl;

import java.io.IOException;

public class GUIMain extends yahtzee
{
    static GraphicsConfiguration gc;
    static ArrayList<ImageIcon> images;
    static JButton[] diceList;
    static JButton doneChoosing = new JButton("Done Choosing");
    static JButton rollB = new JButton("Roll Die");
    static JPanel scorecardP = new JPanel();
    ArrayList<JButton> chooseRow;
    static JFrame mainWindow;
    static JFrame playAgainWindow;
    dice dClass = new dice();
    file fClass = new file();
    playGame playGameClass = new playGame();
    scorecard scClass = new scorecard();
    yahtzee yClass = new yahtzee();
    static int[] settings = new int[3];
    static int[] hand;
    static JButton[] scorecardArray;
    static int[] scorePerTurn;
    static Boolean[] savedDie;
    static int[] scorePerLine;
    static int totalScore = 0;
    static int bonusYahtzee;
    static int turn;
    public static void main(String args[])
    {
        GUI GUIClass = new GUI();
        int diceSelected = 0;

        GUIClass.userSettingsGUI(diceSelected);

        //roll dice not kept
        GUIClass.rollDieFunction();
        
        //if not the last roll of the hand prompt the user for dice to keep
        GUIClass.doneWithRoll(); 
    }
}

class GUI extends GUIMain
{
    /**
     * function that stores the action listener for the doneChoosing button
     * 
     */
    void doneWithRoll()
    {
        doneChoosing.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    scorecard scorecard = new scorecard();
                    System.out.println("Done Choosing clicked");
                    //disables the dice after done selecting to keep
                    for (int i = 1; i < settings[1] + 1; i++)
                    {
                        diceList[i].setEnabled(false);
                    }
                    rollB.setEnabled(false);
                    
                    //enables all scorecard buttons that havent already been chosen
                    for (int i = 1; i < scorecardArray.length; i++)
                    {
                        if (scorecardArray[i].getBackground() != Color.RED)
                        {
                            scorecardArray[i].setEnabled(true);
                        }
                    }
                    scorePerTurn = scorecard.getScorecardValue(hand, settings[0], settings[2], totalScore, bonusYahtzee, scorePerTurn).clone();
                    updateScores();
                    doneChoosing.setEnabled(false);
                }
            });
    }

    /**
     * function that stores the action listener for the roll dice button
     * 
     */
    void rollDieFunction()
    {
        dice dice = new dice();
        rollB.addActionListener(
            new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                for (int dieNumber = 1; dieNumber < settings[1] + 1; dieNumber++)
                {
                    if (diceList[dieNumber].getBackground() != Color.GREEN)
                    {
                        hand[dieNumber] = dice.rollDie(settings[0]);
                        diceList[dieNumber].setIcon(images.get(hand[dieNumber]));
                        diceList[dieNumber].setEnabled(true);
                    }
                }
                doneChoosing.setEnabled(true);
                updateScores();
            }
        });
    }

    /**
     * opens the main window
     * 
     * @param diceSelected tracks the dice chosen to be kept
     */
    void createWindow(int diceSelected)
    {
        String keep = new String();
        mainWindow = new JFrame(gc);
        mainWindow.setTitle("Yahtzee game");
        mainWindow.setSize(800, 800);
        mainWindow.setLocation(200, 200);
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.add(handPanel(keep, diceSelected), BorderLayout.SOUTH);
        mainWindow.add(displayScorecardGUI(), BorderLayout.WEST);
        mainWindow.add(rollDieButton(), BorderLayout.EAST);
        //ainWindow.add(selectScoreView(), BorderLayout.CENTER);
        doneChoosing.setEnabled(false);
        mainWindow.setVisible(true);
    }

    /**
     * creates the window to allow the user to play again
     *
     */
    void playAgainWindow()
    {
        playAgainWindow = new JFrame();
        JPanel start = new JPanel();
        playAgainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        playAgainWindow.setSize(300, 150);
        playAgainWindow.setLocation(200, 200);
        JLabel grandTotal = new JLabel("Grand Total: " + totalScore + " points.");
        JButton doNotPlayAgain = new JButton("I Do Not Want To Play Again");
        JButton initiateGame = new JButton("Click Me to Play Again");
        start.add(grandTotal, BorderLayout.CENTER);
        start.add(initiateGame, BorderLayout.SOUTH);
        start.add(doNotPlayAgain, BorderLayout.SOUTH);
        initiateGame.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int diceSelected = 0;
                hand = null;
                diceList = null;
                scorecardArray = null;
                scorePerTurn = null;
                scorePerLine = null;
                chooseRow = null;
                scorecardP.removeAll();
                scorecardP.revalidate();
                scorecardP.repaint();
                userSettingsGUI(diceSelected);
                System.out.println("Play Again hidden");
                playAgainWindow.dispose();
            }
        });
        
        doNotPlayAgain.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Play Again hidden");
                playAgainWindow.dispose();
            }
        });
        playAgainWindow.add(start);
        playAgainWindow.setVisible(true);
    }

    /**
    * This function displays the new dice images according to which dice the user rolled
    * 
    * @param keep keeps track of the dice that were selected to be kept
    * @param diceSelected tracks the position of the selected dice to keep
    */
    public JPanel handPanel(String keep, int diceSelected)
    {
        JPanel handP = new JPanel();
        images = new ArrayList<>(13);
        diceList = new JButton[settings[1] + 1];
        loadImages();
        //set image dice icons to default
        //set dimensions of each icon to fit button
        //add the dice to the hand panel
        diceList[0] = (null);
        for (int i = 1; i < settings[1] + 1; i++)
        {
            JButton die = new JButton(getDieImage(1));
            die.setPreferredSize(new Dimension(80, 80));
            diceList[i] = die;
            handP.add(diceList[i]); 
            greenDie(diceList[i]);
            die.setEnabled(false);
        }
        handP.add(doneChoosing);
        return handP;
    }

    /**
     * creates a green border around dice that are kept
     * 
     * @param clickedDice the button that was clicked
     */
    void greenDie(JButton clickedDice)
    {
        clickedDice.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Dice hidden");
                clickedDice.setBackground(Color.GREEN);
                clickedDice.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                clickedDice.setEnabled(false);
            }
        });
    }
    
    /**
     * This function displays a popup with the user's scorecard
     * 
     * 
     */
    public JPanel displayScorecardGUI()
    {
        scorecardP.setLayout(new BoxLayout(scorecardP, BoxLayout.Y_AXIS));
        //JButton showScorecard = new JButton("Show Scorecard");
        JLabel scorecardTitle = new JLabel("Scorecard");
        scorecardArray = new JButton[settings[0] + 7];
        scorecardArray[0] = null;
        scorecardP.setPreferredSize(new Dimension(600, 400));
        String[] name = new String[] {"3 of a Kind", "4 of a Kind", "Full House", "Small Straight", "Large Straight", "Yahtzee"};
        
        
        scorecardP.add(scorecardTitle);
        //add upper scorecard to score
        for (int i = 1; i < settings[0] + 1; i++)
        {
            scorecardArray[i] = new JButton("Score " + scorePerTurn[i] + " on the " + i + " line");
            scorecardArray[i].setPreferredSize(new Dimension(100, 400));
            //hideButton(scorecardArray[i]);
            
            scorecardP.add(scorecardArray[i]);
            scorecardArray[i].setEnabled(false);
            updateScorecard(scorecardArray[i], i);
        }
        //add lower scorecard to score
        for (int i = settings[0] + 1; i < settings[0] + 7; i++)
        {
            scorecardArray[i] = new JButton("Score " + scorePerTurn[i] + " on the " + name[i - (settings[0] + 1)] + " line");
            scorecardArray[i].setPreferredSize(new Dimension(100, 400));

            //hideButton(scorecardArray[i]);
            scorecardP.add(scorecardArray[i]);
            scorecardArray[i].setEnabled(false);
            updateScorecard(scorecardArray[i], i);
        }
        return scorecardP;
    }

    /**
     * function that updates the scores in the scorecardArray
     * 
     */
    void updateScores()
    {
        String[] name = new String[] {"3 of a Kind", "4 of a Kind", "Full House", "Small Straight", "Large Straight", "Yahtzee"};
        for (int i = 1; i < settings[0] + 1; i++)
        {
            if (scorecardArray[i].getBackground() != Color.RED)
            {
                scorecardArray[i].setText("Score " + scorePerTurn[i] + " on the " + i + " of a kind line");
            }
        }
        //add lower scorecard to score
        for (int i = settings[0] + 1; i < settings[0] + 7; i++)
        {
            if (scorecardArray[i].getBackground() != Color.RED)
            {
                scorecardArray[i].setText("Score " + scorePerTurn[i] + " on the " + name[i - (settings[0] + 1)] + " line");
            }
        }
    }

    /**
     * locks the scorecard after each button is clicked
     * 
     * @param clickedDice the button that was clicked
     * @param position the position of the die that was clicked
     */
    void updateScorecard(JButton clickedDice, int position)
    {
        //scorecardArray = new JButton[settings[0] + 20];
        clickedDice.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clickedDice.setBackground(Color.RED);
                clickedDice.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                for (int i = 1; i < scorecardArray.length; i++)
                    scorecardArray[i].setEnabled(false);
                turn++;
                scorePerLine[position] = scorePerTurn[position];
                rollB.setEnabled(true);
                if (turn == settings[2])
                {
                    calculateTotal();
                    mainWindow.dispose();
                    playAgainWindow();
                }
            }
        });
    }

    /**
     * function that calculates the total game score
     * 
     */
    void calculateTotal()
    {
        scorecard scorecard = new scorecard();
        for (int i = 1; i < scorePerLine.length; i++)
            totalScore = totalScore + scorePerLine[i];
        totalScore = scorecard.calculateBonus(totalScore, bonusYahtzee);
    }

    /**
     * a JPanel for the roll die button
     * 
     */
    public JPanel rollDieButton()
    {
        JPanel rollDieB = new JPanel();
        rollDieB.add(rollB);
        
        return rollDieB;
    }

    /**
     * This function displays a popup for the user to enter their preferred game settings
     * 
     * @param diceSelected the dice position of the die that is clicked
     * 
     */
    void userSettingsGUI(int diceSelected)
    {
        
        file file = new file();
        JFrame inputSetting = new JFrame(gc);
        JPanel saveSetting = new JPanel();
        JPanel options = new JPanel();
        JLabel diceSidesLabel = new JLabel("Choose your number of sides on each dice");
        JLabel numberOfDiceLabel = new JLabel("Chose the number of dice you would like to play with");
        JLabel rollsString = new JLabel("Enter the number of rolls per game:");
        JPanel scorecardP = new JPanel();
        scorePerTurn = new int[settings[0] + 20];
        //set all scores equal to 0
        for (int i = 0; i < scorePerTurn.length; i++)
            scorePerTurn[i] = 0;
        
        scorePerLine = new int[20];
        for (int i = 0; i < scorePerLine.length; i++)
            scorePerLine[i] = 0;
        hand = new int[settings[1] + 1];
        turn = 0;

        diceSidesLabel.setVisible(true);
        numberOfDiceLabel.setVisible(true);

        options.setLayout(new BoxLayout (options, BoxLayout.PAGE_AXIS));

        String [] sidesChoices = {"6", "8", "12"};
        String [] numberChoices = {"5", "6", "7"};

        DefaultListCellRenderer centerAlign = new DefaultListCellRenderer();
        centerAlign.setHorizontalAlignment(DefaultListCellRenderer.CENTER);

        final JComboBox<String> sidesString = new JComboBox<String>(sidesChoices);
        final JComboBox<String> numberString = new JComboBox<String>(numberChoices);
        JTextField numberRolls = new JTextField();
        numberRolls.setVisible(true);
        sidesString.setVisible(true);
        numberString.setVisible(true);
        options.add(numberOfDiceLabel);
        options.add(numberString);
        options.add(diceSidesLabel);
        options.add(sidesString);
        options.add(rollsString);
        options.add(numberRolls);
        sidesString.setRenderer(centerAlign);
        numberString.setRenderer(centerAlign);

        inputSetting.setTitle("Settings");
        inputSetting.setSize(400, 200);
        inputSetting.setLocation(200, 200);
        inputSetting.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputSetting.setResizable(false);
        JButton keepDefault = new JButton("Keep Previous");
        JButton saveNew = new JButton("Save New");

        //add the buttons to a panel
        saveSetting.add(keepDefault);
        saveSetting.add(saveNew);

        keepDefault.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Default button pressed");
                file.readFile(settings);
                file.writeFile(settings);
                System.out.print("Settings kept: " + settings[0] + " " + settings[1] + " " + settings[2]);
                hand = new int[settings[1] + 1];
                createWindow(diceSelected);
                inputSetting.dispose();
                
            }
        });

        saveNew.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("New button pressed");
                String tmp = (String) sidesString.getSelectedItem();
                settings[0] = Integer.parseInt(tmp);
                tmp = (String) numberString.getSelectedItem();
                settings[1] = Integer.parseInt(tmp);
                settings[2] = Integer.parseInt(numberRolls.getText());
                hand = new int[settings[1] + 1];
                file.writeFile(settings);
                System.out.print("Settings chosen: " + settings[0] + " " + settings[1] + " " + settings[2]);
                createWindow(diceSelected);
                inputSetting.dispose();
            }
        });
        
        //adds panel to the bottom of frame
        inputSetting.add(saveSetting, BorderLayout.SOUTH);
        inputSetting.add(options);
        inputSetting.setVisible(true);
    }

    /**
     * loads the images from the file folder
     * 
     */
    void loadImages() {
        BufferedImage currPicture;
        images.add(null);
        for( int i = 1; i < 10; i++)
        {
            try 
            {
                String filename = "media/Dice/D6-0" + i + ".png";
                System.out.println(filename);
                currPicture = ImageIO.read(new File(filename));
                Image dimg = currPicture.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                ImageIcon scaledImage = new ImageIcon(dimg);
                images.add(scaledImage);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        for( int i = 10; i < 13; i++)
        {
            try 
            {
                String filename = "media/Dice/D6-" + i + ".png";
                System.out.println(filename);
                currPicture = ImageIO.read(new File(filename));
                Image dimg = currPicture.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                ImageIcon scaledImage = new ImageIcon(dimg);
                images.add(scaledImage);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * gets the image for a particular die value
     * 
     * @param dieValue the value of the die rolled
     */
    public ImageIcon getDieImage(int dieValue) {
        return images.get(dieValue);
    }
}