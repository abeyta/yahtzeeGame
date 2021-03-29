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
import java.io.IOException;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class GUIMain extends yahtzee
{
    static GraphicsConfiguration gc;
    ArrayList<ImageIcon> images;
    JButton[] diceList;
    ArrayList<JButton> chooseRow;
    static JFrame mainWindow;
    dice dClass = new dice();
    file fClass = new file();
    playGame playGameClass = new playGame();
    scorecard scClass = new scorecard();
    yahtzee yClass = new yahtzee();
    static int[] settings = new int[3];
    public static int[] hand;
    static int[] usedRow;
    static int myTurn = 1;
    static char playAgain = 'y';
    static String keep;
    public static void main(String args[])
    {
        dice dice = new dice();
        GUI GUIClass = new GUI();
        int diceSelected = 0;
        String[] keepDice = new String[settings[1]];
        for (int i = 0; i < settings[1]; i++)
        {
            keepDice[i] = "n";
        }
        int[] scorecard = new int[settings[0] + 7];
        
        usedRow = new int[settings[0] + 7];
        for (int i = 0; i < usedRow.length; i++)
            usedRow[i] = 0;
        hand = new int[settings[1]];

        GUIClass.userSettingsGUI(diceSelected, myTurn);
        
        while (playAgain == 'y')
        {
            char[] temp = new char[settings[1]];
            char[] temp2 = new char[settings[1]];
        //allocates corect amount of space within keep to acoomodate for various number of die
            for (int i = 0; i < settings[1]; i++)
            {
                temp[i] = 'n';
                temp2[i] = 'y';
            }
            keep = String.copyValueOf(temp);
            String keepAll = String.copyValueOf(temp2);

            int turn = 1;
            myTurn = 1;

            while (turn < settings[2] && keepAll != keep)
            {
                //roll dice not kept
                for (int dieNumber = 0; dieNumber < settings[1] - 1; dieNumber++)
                {
                    if (dieNumber > 0 && dieNumber < keep.length() && keep.charAt(dieNumber) != 'y')
                        hand[dieNumber] = dice.rollDie(settings[0]);
                }

                //output roll
                for (int dieNumber = 0; dieNumber < settings[1]; dieNumber++)
                {
                    GUIClass.updateDiceImages(dieNumber);
                }

                //if not the last roll of the hand prompt the user for dice to keep
                if (turn < settings[2])
                {
                    System.out.print("enter dice to keep (y or n): ");
                    keep = consoleInput.nextLine();
                    if (keep.length() != settings[1])
                    {
                        System.out.print("enter the correct number of dice to keep (y or n): ");
                        keep = consoleInput.nextLine();
                    } 
                }
                
                //scorecard.selectLine(settings[2], hand, usedRow, settings[0], totalScore);
                turn++;
                myTurn++;
            }
            
        }
    }
}

class GUI extends GUIMain
{
    //ArrayList<ImageIcon> images;

    void createWindow(int diceSelected, int turn)
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
        mainWindow.add(turnCounter(turn), BorderLayout.CENTER);
        mainWindow.add(rollDieButton(), BorderLayout.EAST);
        //ainWindow.add(selectScoreView(), BorderLayout.CENTER);
        mainWindow.setVisible(true);
    }

    public JPanel turnCounter(int turn)
    {
        JLabel myLabel = new JLabel("Turn: " + turn);
        JPanel myPanel = new JPanel();
        myPanel.add(myLabel);
        myPanel.setVisible(true);
        return myPanel;
    }

    void playAgainWindow()
    {
        JFrame playGameAgain = new JFrame();
        JPanel start = new JPanel();
        JButton doNotPlayAgain = new JButton("I Do Not Want To Play Again");
        JButton initiateGame = new JButton("Click Me to Play Again");
        start.add(initiateGame);
        start.add(doNotPlayAgain);
        initiateGame.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Play Again hidden");
                playAgain = 'y';
                playGameAgain.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });
        
        doNotPlayAgain.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Play Again hidden");
                playAgain = 'n';
                playGameAgain.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });
        playGameAgain.add(start);
    }

    /**
    * This function displays the new dice images according to which dice the user rolled
    * 
    */
    public JPanel handPanel(String keep, int diceSelected)
    {
        JPanel handP = new JPanel();
        JButton doneChoosing = new JButton("Done Choosing");
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
            greenDie(diceList[i], keep, diceSelected);
        }
        handP.add(doneChoosing);
        return handP;
    }


    void updateDiceImages(int dieNumber)
    {
        diceList[dieNumber].setIcon(getDieImage(hand[dieNumber]));
    }

    void greenDie(JButton clickedDice, String keep, int diceSelected)
    {
        clickedDice.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String keep1 = new String();
                System.out.println("Dice hidden");
                Border greenline = BorderFactory.createLineBorder(Color.green);
                clickedDice.setBorder(greenline);
                //keep1 = keep.substring(0, diceSelected) + 'y' + keep.substring(diceSelected + 1);
            }
        });
    }



    void hideButton(JButton clickedDice)
    {
        clickedDice.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Dice hidden");
                clickedDice.setVisible(false);
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
        JPanel scorecardP = new JPanel();
        scorecardP.setLayout(new BoxLayout(scorecardP, BoxLayout.Y_AXIS));
        //JButton showScorecard = new JButton("Show Scorecard");
        JLabel scorecardTitle = new JLabel("Scorecard");
        JButton[] scorecardArray = new JButton[settings[0] + 7];
        scorecardArray[0] = null;
        scorecardP.setPreferredSize(new Dimension(600, 400));
        String[] name = new String[] {"3 of a Kind", "4 of a Kind", "Full House", "Small Straight", "Large Straight", "Yahtzee"};
        
        
        scorecardP.add(scorecardTitle);
        //add upper scorecard to score
        for (int i = 1; i < settings[0] + 1; i++)
        {
            int score = 0; //cClass.getScore(i);
            JButton line = new JButton("Score " + score + " on the " + i + " of a kind line");
            scorecardArray[i] = line;
            line.setPreferredSize(new Dimension(100, 400));
            //hideButton(scorecardArray[i]);
            
            scorecardP.add(scorecardArray[i]);
            lockScorecard(scorecardArray[i]);
        }
        //add lower scorecard to score
        for (int i = settings[0] + 1; i < settings[0] + 7; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                int score = 0; //cClass.getScore(i);
                JButton line = new JButton("Score " + score + " on the " + name[j] + " line");
                scorecardArray[i] = line;
                line.setPreferredSize(new Dimension(100, 400));
                //hideButton(scorecardArray[i]);
                scorecardP.add(scorecardArray[i]);
                lockScorecard(scorecardArray[i]);
            }
        }

        JLabel totalScore = new JLabel("Total Score: ");
        scorecardP.add(totalScore);
        
        
        return scorecardP;
    }

    void lockScorecard(JButton clickedDice)
    {
        clickedDice.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                
                Border redline = BorderFactory.createLineBorder(Color.red);
                clickedDice.setBorder(redline);
                clickedDice.setEnabled(false);
                //keep1 = keep.substring(0, diceSelected) + 'y' + keep.substring(diceSelected + 1);
            }
        });
    }

    public JPanel rollDieButton()
    {
        JPanel rollDieB = new JPanel();
        JButton rollB = new JButton("Roll Die");
        rollB.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dice dice = new dice();
                System.out.println("Roll button pressed");
                /*for (int dieNumber = 0; dieNumber < settings[1] - 1; dieNumber++)
                {
                    if (dieNumber > 0 && dieNumber < keep.length() && keep.charAt(dieNumber) != 'y')
                        hand[dieNumber] = dice.rollDie(settings[0]);
                }*/
                for (int i = 0; i < hand.length; i++)
                    System.out.print(hand[i] + " ");
            }
        });
        rollDieB.add(rollB);
        
        return rollDieB;
    }

    /**
     * This function displays a popup for the user to enter their preferred game settings
     * 
     * 
     */
    void userSettingsGUI(int diceSelected, int turn)
    {
        file file = new file();
        JFrame inputSetting = new JFrame(gc);
        JPanel saveSetting = new JPanel();
        JPanel options = new JPanel();
        JLabel diceSidesLabel = new JLabel("Choose your number of sides on each dice");
        JLabel numberOfDiceLabel = new JLabel("Chose the number of dice you would like to play with");
        JLabel rollsString = new JLabel("Enter the number of rolls per game:");
        //String tmp = new String();

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
        inputSetting.setVisible(true);
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
                playAgain = 'y';
                createWindow(diceSelected, turn);
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
                file.writeFile(settings);
                System.out.print("Settings chosen: " + settings[0] + " " + settings[1] + " " + settings[2]);
                playAgain = 'y';
                createWindow(diceSelected, turn);
                inputSetting.dispose();
            }
        });
        
        //adds panel to the bottom of frame
        inputSetting.add(saveSetting, BorderLayout.SOUTH);
        inputSetting.add(options);
    }

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

    public ImageIcon getDieImage(int dieValue) {
        return images.get(dieValue);
    }
}