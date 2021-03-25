import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.swing.border.Border;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.*;
import java.io.IOException;

public class GUIMain extends yahtzee
{
  static GraphicsConfiguration gc;
  JFrame mainWindow;
  dice dClass;
  file fClass = new file();
  
  playGame playGameClass = new playGame();
  scorecard scClass = new scorecard();
  yahtzee yClass = new yahtzee();

  public static void main(String args[])
  {
    GUI GUIClass = new GUI();
    GUIClass.openGUI();
  }

  void setupDice() 
  {
    dClass = new dice();
  }
}

class GUI extends GUIMain
{
  /**
   * This function opens the GUI
   * 
   * 
   */
  void openGUI()
  {
    createWindow();
    userSettingsGUI();
  }

  
  void createWindow()
  {
    mainWindow = new JFrame(gc);
    mainWindow.setTitle("Yahtzee game");
    mainWindow.setSize(600, 400);
    mainWindow.setLocation(200, 200);
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainWindow.setVisible(true);
    mainWindow.add(handPanel(), BorderLayout.SOUTH);
    mainWindow.add(displayScorecardGUI(), BorderLayout.WEST);
    mainWindow.add(rollDieButton(), BorderLayout.EAST);
  }

  /**
   * This function displays the new dice images according to which dice the user rolled
   * 
   */
  public JPanel handPanel()
  {
    JPanel handP = new JPanel();
    BufferedImage importPicture;

    //set image dice icons to default
    try
    {
      String filename = "media/Dice/D6-01.png";
      importPicture = ImageIO.read(new File(filename));
      Image newImage = importPicture.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
      Icon diceImage = new ImageIcon(newImage);
      JButton dice1B = new JButton (diceImage);
      JButton dice2B = new JButton(diceImage);
      JButton dice3B = new JButton(diceImage);
      JButton dice4B = new JButton(diceImage);
      JButton dice5B = new JButton(diceImage);

      //set dimensions of each label
      dice1B.setPreferredSize(new Dimension(50, 50));
      dice2B.setPreferredSize(new Dimension(50, 50));
      dice3B.setPreferredSize(new Dimension(50, 50));
      dice4B.setPreferredSize(new Dimension(50, 50));
      dice5B.setPreferredSize(new Dimension(50, 50));

      //add the dice to the hand panel
      handP.add(dice1B);
      handP.add(dice2B);
      handP.add(dice3B);
      handP.add(dice4B);
      handP.add(dice5B);

      changeDiceColor(dice1B);
      changeDiceColor(dice2B);
      changeDiceColor(dice3B);
      changeDiceColor(dice4B);
      changeDiceColor(dice5B);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    return handP;
  }

  void changeDiceColor(JButton clickedDice)
  {
    clickedDice.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          System.out.println("Dice button pressed");
          clickedDice.setVisible(false);
        }
      }
    );
  }

  /**
   * This function displays a popup for the user to enter their preferred game settings
   * 
   * 
   */
  void userSettingsGUI()
  {
    JFrame inputSetting = new JFrame(gc);
    inputSetting.setTitle("Settings");
    inputSetting.setSize(300, 300);
    inputSetting.setLocation(200, 200);
    inputSetting.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    inputSetting.setVisible(true);
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
    showScorecard.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          System.out.println("Show Scorecard button pressed");
        }
      }
    );
    scorecardP.add(showScorecard);

    return scorecardP;
  }

  public JPanel rollDieButton()
  {
    JPanel rollDieB = new JPanel();
    JButton rollB = new JButton("Roll Die");
    rollB.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          System.out.println("Roll button pressed");
        }
      }
    );
    rollDieB.add(rollB);
    
    return rollDieB;
  }
}
