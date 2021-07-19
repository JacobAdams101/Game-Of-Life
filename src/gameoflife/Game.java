
package gameoflife;

import gameoflife.Tools.ToolType;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Jadams14
 */
public class Game 
{
    public boolean running = false;
    
    public Window win;

    public Grid grid;
    
    public Tools tool;
    
    public int aliveCells = 0;

    final long LOOPINTERVAL = 2000000; //Declare and initialize final long variable 'LOOPINTERVAL' to store how long each game refresh should take any excess time will be waited out
    
        
    private int count = 0;
    private int simulateFrames;
    
    private int speed;
    
    public final int MAXSPEED = 10;
    
    public final int BASESCALE = 2;
    /**
     * 
     * @param gridWidth
     * @param gridHeight 
     */
    public Game(int gridWidth, int gridHeight) 
    {
        try 
        {
                    
            tool = new Tools();
            
            win = new Window(1200, 600, gridWidth, gridHeight, BASESCALE);
                    
            grid = new Grid(gridWidth, gridHeight, true);
            
            win.centreView(grid);
        } 
        catch (LineUnavailableException ex) 
        {
        }
        speed = 5;
        simulateFrames = (int) Math.pow(2, MAXSPEED - speed);
        
        refreshAliveCells();
    }
    /**
     * Returns the speed as a percentage of the maximum speed
     * @return Returns the speed as a percentage of the maximum speed
     */
    public int getPercentSpeed() 
    {
        return (int) (((double) speed / (double) MAXSPEED) * 100.0);
    }
    /**
     * Runs the "game"
     */
    public void run() 
    {

        long start; //Declare long variable 'start' to record the starting system nano time at the begging of the loop
        long end; //Declare long variable 'end' to record the ending system nano time at the end of the loop

        while (true) //Loop forever
        {
            start = System.nanoTime(); //record the start frame time
            tool.run(this);
            if (win.key.c && !win.key.control) 
            {
                regenerateGrid(false);
                win.key.c = false;
            }
            if (win.key.r) 
            {
                regenerateGrid(true);
                win.key.r = false;
            }
            if (win.key.p) 
            {
                tool.setTool(ToolType.PAINTTOOL);
                win.key.p = false;
            }
            if (win.key.o) 
            {
                tool.setTool(ToolType.SELECTTOOL);
                win.key.o = false;
            }
            if (win.key.space) 
            {
                running = !running;
                win.key.space = false;
            }
            if (win.key.plus)  //Speed up game speed
            {
                if (speed < MAXSPEED) //Speed cannot be greater than the max speed
                {
                    speed++;
                    simulateFrames = (int) Math.pow(2, MAXSPEED - speed);
                }
                win.key.plus = false;
            }
            if (win.key.minus) //Slow down game speed
            {
                if (speed > 1) 
                {
                    speed--;
                    simulateFrames = (int) Math.pow(2, MAXSPEED - speed);
                }
                else
                {
                    running = false;
                }
                win.key.minus = false;
            }
            if (win.key.q) 
            {
                updateGrid();
                win.key.q = false;
            }
            if (running) 
            {
                count++;
                if (count > simulateFrames)
                {
                    updateGrid();
                }
            }
            win.update(this);
            //Wait until 'LOOPINTERVAL' has passed
            do 
            {
                end = System.nanoTime(); //Record current time after finishing the current game loop
            } 
            while (end <= start + (LOOPINTERVAL));
        }
    }
    /**
     * 
     */
    private void updateGrid()
    {
        count = 0;
        grid.simulate();
        refreshAliveCells();
    }
    /**
     * 
     * @param isRandom 
     */
    private void regenerateGrid(boolean isRandom) 
    {
        getNewGridInput(); //Get the user input
        grid = new Grid(width, height, isRandom);
        win.mouse.zoom = BASESCALE;
        win.centreView(grid);
        refreshAliveCells();
    }
    
    public boolean startupAnswered;
    public int width = 0;
    public int height = 0;
    /**
     * 
     */
    public synchronized void getNewGridInput() 
    {
        startupAnswered = false;
        
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(2048);
        formatter.setAllowsInvalid(false);
        // If you want the value to be committed on each keystroke instead of focus lost
        formatter.setCommitsOnValidEdit(true);
        
        Font font = new Font("Verdana", Font.BOLD, 24);
    //Setup JFrame    
        JFrame f = new JFrame("Setup Dimensions");
        
    //Setup JFrame
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(430, 500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        JPanel mainPanel = new JPanel(null);
        f.add(mainPanel);
        mainPanel.setBackground(Color.WHITE);
    
    //Setup Objects/Buttons/TextFeilds etc.
        JTextArea Title = new JTextArea("Input New Dimesions");
        Title.setEditable(false);
        mainPanel.add(Title);
        Title.setBounds(50, 20, 500, 50);
        Title.setFont(font);
        Title.setBackground(Color.WHITE);
        //Setup the error messgae text box
        JTextArea errorMessage = new JTextArea("");
        errorMessage.setEditable(false);
        mainPanel.add(errorMessage);
        errorMessage.setBounds(50, 50, 500, 50);
        errorMessage.setFont(font);
        errorMessage.setBackground(Color.WHITE);
        //The title for the width entry box
        JTextArea widthTitle = new JTextArea("Width:");
        widthTitle.setEditable(false);
        mainPanel.add(widthTitle);
        widthTitle.setBounds(50, 100, 500, 50);
        widthTitle.setFont(font);
        widthTitle.setBackground(Color.WHITE);
        //The width entry box for the user input
        JFormattedTextField widthAnswer = new JFormattedTextField(formatter);
        widthAnswer.setText("" + grid.width());
        widthAnswer.setEditable(true);
        mainPanel.add(widthAnswer);
        widthAnswer.setBounds(50, 130, 300, 50);
        widthAnswer.setFont(font);
        widthAnswer.setBackground(Color.WHITE);
        //The title for the height entry box
        JTextArea heightTitle = new JTextArea("Height:");
        heightTitle.setEditable(false);
        mainPanel.add(heightTitle);
        heightTitle.setBounds(50, 180, 500, 50);
        heightTitle.setFont(font);
        heightTitle.setBackground(Color.WHITE);
        //The height entry box for the user input
        JFormattedTextField heightAnswer = new JFormattedTextField(formatter);
        heightAnswer.setText("" + grid.height());
        heightAnswer.setEditable(true);
        mainPanel.add(heightAnswer);
        heightAnswer.setBounds(50, 210, 300, 50);
        heightAnswer.setFont(font);
        heightAnswer.setBackground(Color.WHITE);
        //The done/enter/completed/submit button
        JButton doneButton = new JButton("Generate!");
        doneButton.setFont(font);
        mainPanel.add(doneButton);
        doneButton.setBounds(100, 370, 200, 50);
        doneButton.addActionListener(new ActionListener() 
        {
        //Button Action Listener
        @Override
        public void actionPerformed(ActionEvent e)
        {
            
            boolean inputHasWorked = true;
            
            try 
            { //Try to convert inputs to integers
                width = Integer.parseInt(widthAnswer.getText().replace(",", ""));
                height = Integer.parseInt(heightAnswer.getText().replace(",", ""));
            } 
            catch (NumberFormatException ex) 
            { //If conversion to integers failed
                inputHasWorked = false; //The input is not valid and needs to be retaken
            }
            
            startupAnswered = inputHasWorked;

            if (!startupAnswered) 
            { //If the input was incorrectly entered
                errorMessage.setText("Invalid Inputs!"); //Display invalid inputs
            }
        }
        });
        while (!startupAnswered) 
        { //Loop until a valid input has been entered
            System.out.println(startupAnswered);
        } //When the valid input has been entered
        f.dispose(); //Destroy the jframe
    }
    /**
     * 
     */
    public void refreshAliveCells() {
        aliveCells = grid.getTotalAlive();
    }
}
