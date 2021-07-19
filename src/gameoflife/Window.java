
package gameoflife;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * 
 * @author jacob
 */
public class Window extends JPanel 
{
    
    public JFrame frame;
    
    public MousePressedEvent mouse;
    
    public KeyPressedEvent key;
    
    private int anchorX = 0;
    private int anchorY = 0;
    
    private int originalX = 0;
    private int originalY = 0;
    
    public int displayX = 0;
    public int displayY = 0;
    
    private Game game; //Sotres the current frame of the game object
    
    //Store different font sizes/formats for titles heading etc.
    private Font f1 = new Font("Verdana", Font.BOLD, 24);
    private Font f2 = new Font("Verdana", Font.BOLD, 20);
    private Font f25 = new Font("Verdana", Font.BOLD, 17);
    private Font f3 = new Font("Verdana", Font.BOLD, 12);
    /**
     * 
     * @param width
     * @param height
     * @param gridWidth
     * @param gridHeight
     * @param scale
     * @throws LineUnavailableException 
     */
    public Window(int width, int height, int gridWidth, int gridHeight, int scale) throws LineUnavailableException 
    {
        frame = new JFrame("Game Of Life");
        
        frame.add(this); //Add JPanel to 'JFrame' frame
        
        //Set size and location
	frame.setSize(width, height); //Set frame size
        frame.setLocation(10, 10); //Set frame location
        
        frame.setResizable(true);

	frame.setVisible(true); //Set visible

	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close program when JFrame closed
        
        mouse = new MousePressedEvent(this);

        mouse.zoom = scale;
        
        key = new KeyPressedEvent();
        
    }
    /**
     * 
     * @param grid 
     */
    public void centreView(Grid grid) {
        displayX = - ((grid.width() * mouse.zoom) / 2) + (frame.getWidth() / 2);
        displayY = - ((grid.height() * mouse.zoom) / 2) + (frame.getHeight() / 2); 
    }
    /**
     * 
     * @param g 
     */
    @Override
    public void paint(Graphics g) 
    {
        //Store local versions of the display x and y to aviod tearing
        final int displayX = this.displayX;
        final int displayY = this.displayY;
        
        g.setFont(f1);
        g.setColor(Color.gray);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        if (game != null) 
        {
            g.setColor(Color.black);
            g.fillRect(displayX - 2, displayY - 2, (game.grid.width() * mouse.zoom) + 4, (game.grid.height() * mouse.zoom) + 4);

            g.drawImage(game.grid.display(), displayX, displayY, game.grid.width() * mouse.zoom, game.grid.height() * mouse.zoom, null);
        
        if (game.tool.onGrid()) {
            if (game.tool.savedInUse())
            {
                int smallX, smallY, largeX, largeY;
                if (game.tool.getSavedX2() > game.tool.getSavedX())
                {
                    smallX = game.tool.getSavedX();
                    largeX = game.tool.getSavedX2();
                }
                else
                {
                    largeX = game.tool.getSavedX();
                    smallX = game.tool.getSavedX2();
                }
                if (game.tool.getSavedY2() > game.tool.getSavedY())
                {
                    smallY = game.tool.getSavedY();
                    largeY = game.tool.getSavedY2();
                }
                else
                {
                    largeY = game.tool.getSavedY();
                    smallY = game.tool.getSavedY2();
                }
                smallX = displayX + (smallX * mouse.zoom);
                smallY = displayY + (smallY * mouse.zoom);
                
                largeX = displayX + ((largeX + 1) * mouse.zoom);
                largeY = displayY + ((largeY + 1) * mouse.zoom);
                
                int width = largeX - smallX;
                int height = largeY - smallY;
                g.setColor(new Color(255, 0, 0, 200));
                g.fillRect(smallX, smallY, width, height);    
            }

                g.setColor(new Color(255, 0, 0, 200));
                g.fillRect(displayX + (game.tool.getSelectedX() * mouse.zoom), displayY + (game.tool.getSelectedY() * mouse.zoom), mouse.zoom, mouse.zoom);
            
        }
        
        g.setColor(Color.white);
        g.fillRect(frame.getWidth() - 250, 0, 300, frame.getHeight());
        g.fillRect(0, 0, 10, frame.getHeight());
        g.fillRect(0, 0, frame.getWidth(), 10);
        g.fillRect(0, frame.getHeight() - 50, frame.getWidth(), 50);
        
        
        g.setColor(Color.black);
        String isRunning = "";

            if (game.running) 
            {
                isRunning = "RUNNING";
                g.setFont(f2);
                g.drawString("SPEED: " + game.getPercentSpeed() + "%", frame.getWidth() - 250, 80);
            } 
            else 
            {
                isRunning = "PAUSED";
                g.setFont(f2);
                g.drawString("SPEED: STOPPED", frame.getWidth() - 250, 80);
            }
        
        
        if(!frame.getTitle().equals("Game Of Life - " + isRunning))
        {
            frame.setTitle("Game Of Life - " + isRunning);
        }
        g.setFont(f1);
        g.drawString(isRunning, frame.getWidth() - 250, 50);
        
        g.setFont(f3);
        g.drawString("PRESS SPACE TO PAUSE/UNPAUSE", frame.getWidth() - 250, 100);
        g.drawString("PRESS Q TO RUN A STEP", frame.getWidth() - 250, 120);
        g.drawString("PRESS + TO INCREASE SPEED", frame.getWidth() - 250, 140);
        g.drawString("PRESS - TO DECREASE SPEED", frame.getWidth() - 250, 160);
        g.drawString("PRESS C TO CLEAR", frame.getWidth() - 250, 200);
        g.drawString("PRESS R TO RANDOMISE", frame.getWidth() - 250, 220);

        g.setFont(f2);
        g.drawString("TOOL: " + game.tool.getToolUsing(), frame.getWidth() - 250, 250);
        
        g.setFont(f25);
        g.drawString("PAINT TOOL: ", frame.getWidth() - 250, 275);
        
        g.setFont(f3);
        g.drawString("LMB TO MAKE A CELL ALIVE", frame.getWidth() - 250, 300);
        g.drawString("RMB TO MAKE A CELL DEAD", frame.getWidth() - 250, 320);
        
        g.setFont(f25);
        g.drawString("SELECT TOOL: ", frame.getWidth() - 250, 350);
        
        g.setFont(f3);
        g.drawString("PRESS P TO USE THE PAINT TOOL", frame.getWidth() - 250, 375);
        g.drawString("PRESS O TO USE THE SELECT TOOL", frame.getWidth() - 250, 395);
        g.drawString("USE CONTROL C AND V TO COPY", frame.getWidth() - 250, 415);
        g.drawString("AND PASTE", frame.getWidth() - 250, 430);
        g.drawString("USE BACKSPACE TO DELETE", frame.getWidth() - 250, 450);
        g.drawString("A SELECTION", frame.getWidth() - 250, 465);
        
        g.setFont(f2);
        g.drawString("ALIVE: " + game.aliveCells, frame.getWidth() - 250, 500);
        
        }
    }
    /**
     * 
     * @param game 
     */
    public void update(Game game) 
    {
        this.game = game;
        
        if (mouse.justPressedMouse2) 
        {
            anchorX = getMouseX();
            anchorY = getMouseY();
            originalX = displayX;
            originalY = displayY;
        }
        if (mouse.mouse2Pressed) 
        {
            displayX = originalX - anchorX + getMouseX();
            displayY = originalY - anchorY + getMouseY();
            mouse.justPressedMouse2 = false;
        }
        final int KEYMOVESPEED = 1;
        if (key.up) 
        {
            displayY += KEYMOVESPEED;
        }
        if (key.down) 
        {
            displayY -= KEYMOVESPEED;
        }
        if (key.left) 
        {
            displayX += KEYMOVESPEED;
        }
        if (key.right) 
        {
            displayX -= KEYMOVESPEED;
        }

        this.repaint();
    }
    /**
     * 
     * @return 
     */
    public int getMouseX() 
    {
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int Mousex = (int) b.getX();
        return Mousex - frame.getX() - frame.getInsets().left;
    }
    /**
     * 
     * @return 
     */
    public int getMouseY() 
    {
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int Mousey = (int) b.getY();
        return Mousey - frame.getY() - frame.getInsets().top;
    }
}
