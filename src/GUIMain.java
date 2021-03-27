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
    ArrayList<JButton> diceList;
    ArrayList<JButton> chooseRow;
    JFrame mainWindow;
    dice dClass = new dice();
    file fClass = new file();
    playGame playGameClass = new playGame();
    scorecard scClass = new scorecard();
    yahtzee yClass = new yahtzee();
    int[] settings = new int[3];
    public static void main(String args[])
    {
        GUI GUIClass = new GUI();
        GUIClass.userSettingsGUI(); 
    }
}

class GUI extends GUIMain
{
    ArrayList<ImageIcon> images;

    /**
    * This function opens the GUI
     * 
     * 
    */
    void openGUI()
    {
        createWindow();
        
    }

    void createWindow()
    {
        mainWindow = new JFrame(gc);
        mainWindow.setTitle("Yahtzee game");
        mainWindow.setSize(800, 800);
        mainWindow.setLocation(200, 200);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.add(handPanel(), BorderLayout.SOUTH);
        mainWindow.add(displayScorecardGUI(), BorderLayout.WEST);
        mainWindow.add(rollDieButton(), BorderLayout.EAST);
        mainWindow.add(selectScoreView(), BorderLayout.CENTER);
        mainWindow.setVisible(true);
    }

    /**
    * This function displays the new dice images according to which dice the user rolled
    * 
    */
    public JPanel handPanel()
    {
        JPanel handP = new JPanel();
        images = new ArrayList<>(13);
        diceList = new ArrayList<>(settings[1]);
        loadImages();
        //set image dice icons to default
        for (int i = 0; i < settings[1]; i++)
        {
            JButton die = new JButton(getDieImage(1));
            die.setPreferredSize(new Dimension(80, 80)); //set dimensions of each icon to fit button
            diceList.add(die);
            handP.add(diceList.get(i)); //add the dice to the hand panel
            hideButton(diceList.get(i));
        }
        return handP;
    }

    void hideButton(JButton clickedDice)
    {
        clickedDice.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Dice button pressed");
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
        JButton showScorecard = new JButton("Show Scorecard");
        scorecardP.add(showScorecard);
        showScorecard.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Show Scorecard button pressed");
                openScorecardView();
                //scorecard.selectLine(totalRolls, int[] hand, int[] usedRow, int numSides, int totalScore);
            }
        });
        
        return scorecardP;
    }

    public JPanel selectScoreView()
    {
        chooseRow = new ArrayList<>(settings[0] + 9);
        chooseRow.add(null);
        JButton tmp = new JButton();
        String lineString = new String();
        JPanel selectRow = new JPanel();
        for (int i = 1; i < settings[0] + 8; i++)
        {
            JButton line = new JButton("Line " + i);
            line.setPreferredSize(new Dimension(250, 80)); //set dimensions of each icon to fit button
            chooseRow.add(line);
            selectRow.add(chooseRow.get(i)); //add the dice to the hand panel
            //hideButton(chooseRow.get(i));

            /*chooseRow[i].addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    System.out.println("Default button pressed");
                    file.readFile(settings);
                    file.writeFile(settings);
                    System.out.print("Settings kept: " + settings[0] + " " + settings[1] + " " + settings[2]);
                    inputSetting.dispose();
                    createWindow();
                }
            });*/
        }
        return selectRow;
    }

    void openScorecardView()
    {
        JFrame openScorecard = new JFrame();
        JPanel viewCurrentScore = new JPanel();
        openScorecard.setTitle("Scorecard");
        openScorecard.setSize(600, 400);
        openScorecard.setLocation(200, 200);
        openScorecard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        openScorecard.setVisible(true);

    }

    public JPanel rollDieButton()
    {
        JPanel rollDieB = new JPanel();
        int click = 0;
        String[] blank = new String[10];
        JButton rollB = new JButton("Roll Die");
        rollB.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Roll button pressed");
                if (click == 1)
                {
                    yahtzee.main(blank);
                }
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
    void userSettingsGUI()
    {
        file file = new file();
        JFrame inputSetting = new JFrame(gc);
        JPanel saveSetting = new JPanel();
        JPanel options = new JPanel();
        JLabel diceSidesLabel = new JLabel("Choose your number of sides on each dice");
        JLabel numberOfDiceLabel = new JLabel("Chose the number of dice you would like to play with");
        //String tmp = new String();

        diceSidesLabel.setVisible(true);
        numberOfDiceLabel.setVisible(true);

        options.setLayout(new BoxLayout (options, BoxLayout.PAGE_AXIS));

        String [] sidesChoices = {"6", "8", "12"};
        String [] numberChoices = {"5", "6", "7"};

        final JComboBox<String> sidesString = new JComboBox<String>(sidesChoices);
        final JComboBox<String> numberString = new JComboBox<String>(numberChoices);
        sidesString.setVisible(true);
        numberString.setVisible(true);
        options.add(numberOfDiceLabel);
        options.add(numberString);
        options.add(diceSidesLabel);
        options.add(sidesString);

        inputSetting.setTitle("Settings");
        inputSetting.setSize(600, 400);
        inputSetting.setLocation(200, 200);
        inputSetting.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputSetting.setVisible(true);
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
                inputSetting.dispose();
                createWindow();
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
                inputSetting.dispose();
                createWindow();
            }
        });
        
        //adds panel to the bottom of frame
        inputSetting.add(saveSetting, BorderLayout.SOUTH);
        inputSetting.add(options);

        inputSetting.addWindowFocusListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e) 
            {
                System.exit(0);
            }
        });
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

class propertyChange extends yahtzee
{

    private String message;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String newValue) {
        String oldValue = this.message;
        this.message = newValue;
        // The parameter values of firePropertyChange method
        // constitute the PropertyChangeEvent object
        support.firePropertyChange("message", oldValue, newValue);
    }
}