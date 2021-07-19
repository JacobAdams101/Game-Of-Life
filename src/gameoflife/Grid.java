
package gameoflife;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

/**
 * 
 * @author jacob
 */
public class Grid 
{
    private BufferedImage displayedImage;
    
    private Graphics2D g2;
    /**
     * 
     * @param width
     * @param height
     * @param random 
     */
    public Grid (int width, int height, boolean random)
    {
        regenerateImage(width, height);
           
        if (random) {
            int ix;
            int iy;
            for (ix = 0; ix < width(); ix++)
            {
                for (iy = 0; iy < height(); iy++)
                {
                    boolean cell = Math.random() > 0.5;
                    set(ix, iy, cell);
                }
            }
        }
    }
    /**
     * 
     * @return Returns the width of the grid
     */
    public final int width() 
    {
        return displayedImage.getWidth();
    }
    /**
     * 
     * @return Returns the height of the grid
     */
    public final int height() 
    {
        return displayedImage.getHeight();
    }
    /**
     * 
     * @param image
     * @param x
     * @param y 
     */
    public void draw(BufferedImage image, int x, int y)
    {
        g2.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
    }
    /**
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @return 
     */
    public BufferedImage getImage(int x, int y, int width, int height)
    {
        try
        {
            return displayedImage.getSubimage(x, y, width, height);
        }
        catch (RasterFormatException ex)
        {
        }
        return null;
    }
    /**
     * 
     * @param x
     * @param y
     * @param set 
     */
    public final void set(int x, int y, boolean set) 
    {
        try
        {
            if (get(x, y) != set) {
                setImagePixel(x, y, 1, 1, set);
            }
        } 
        catch (ArrayIndexOutOfBoundsException ex)
        {

        }
    }
    /**
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param fill The colour (alive or dead) of the cell(s) painted
     */
    public final void setRect(int x1, int y1, int x2, int y2, boolean fill) {
        int smallX, smallY, largeX, largeY;
        if (x2 > x1)
        {
            smallX = x1;
            largeX = x2;
        }
        else
        {
            largeX = x1;
            smallX = x2;
        }
        if (y2 > y1)
        {
            smallY = y1;
            largeY = y2;
        }
        else
        {
            largeY = y1;
            smallY = y2;
        }
        
        if (true)
        {
            setImagePixel(smallX, smallY, largeX - smallX + 1, largeY - smallY + 1, fill);
        }
    }
    /**
     * 
     * @param x
     * @param y
     * @return Returns true if the cell selected is alive
     */
    public boolean get(int x, int y) 
    {
        try 
        {
            return displayedImage.getRGB(x, y) == Color.BLACK.getRGB();
        } 
        catch (ArrayIndexOutOfBoundsException ex) 
        {
            
        }
        return false;
    }
    /**
     * 
     * @return Returns the buffered image
     */
    public synchronized BufferedImage display()
    {
        return displayedImage;
    }
    /**
     * Sets a pixel/cell of the grid
     * @param x The x position
     * @param y The y position
     * @param width The width of the drawn area
     * @param height The height of the drawn area
     * @param set The colour (alive or dead) of the pixel
     */
    public synchronized void setImagePixel(int x, int y, int width, int height, boolean set)
    {
        if (set) 
        {
            g2.setPaint(Color.black);
        }
        else
        {
            g2.setPaint(Color.white);
        }
        g2.fillRect(x, y, width, height);
    }
    /**
     * Creates a blank image
     * @param width New image width
     * @param height New image height
     */
    public synchronized void regenerateImage(int width, int height) 
    {
        displayedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        g2 = displayedImage.createGraphics();

        g2.setPaint(Color.white);
        
        g2.fillRect(0, 0, displayedImage.getWidth(), displayedImage.getHeight());
    }
    
    /**
     * A private method used for finding if a cell is a live quickly
     * @param rgbData The much faster rgb data to access
     * @param width The width of the buffered image
     * @param x The x position of the pixel
     * @param y The y position of the pixel
     * @return Returns true if the pixel accessed is black
     */
    private boolean isAlive(int[] rgbData, int width, int x, int y)
    {
        return rgbData[(y*width)+x] == Color.BLACK.getRGB();
    }
    /**
     * Simulates the game of life performing exactly one 'step'
     */
    public synchronized void simulate()
    {
        int[] tempGridrgbData = displayedImage.getRGB(0,0, displayedImage.getWidth(), displayedImage.getHeight(), null, 0, displayedImage.getWidth());    
        int ix;
        int iy;
        
        int aliveSquares;
        
        for (ix = 0; ix < width(); ix++) 
        {
            for (iy = 0; iy < height(); iy++) 
            {
                aliveSquares = getAliveAdjecentSquares(ix, iy, tempGridrgbData, displayedImage.getWidth());
                if (isAlive(tempGridrgbData, displayedImage.getWidth(), ix, iy))
                { //If the cell is alive
                    if (aliveSquares < 2)
                    {
                        setImagePixel(ix, iy, 1, 1, false);
                    }
                    else if (aliveSquares > 3)
                    {
                        setImagePixel(ix, iy, 1, 1, false);
                    }
                }
                else 
                { //If the cell is dead
                    if (aliveSquares == 3)
                    {
                        setImagePixel(ix, iy, 1, 1, true);
                    }
                }
            }
        }
        //regenerateImage();
    }
    /**
     * 
     * @param x
     * @param y
     * @param tempGridrgbData
     * @param width
     * @return Returns the number of horizontally, vertically or diagonally adjacent cells that are alive
     */
    public int getAliveAdjecentSquares(int x, int y, int[] tempGridrgbData, int width)
    {
        int ix;
        int iy;
        
        int aliveCells = 0;
        
        for (ix = -1; ix <= 1; ix++) 
        {
            for (iy = -1; iy <= 1; iy++) 
            {
                if (ix == 0 && iy == 0) 
                {  
                }
                else
                {
                    try 
                    {
                        if (x + ix >= 0 && x + ix < width) {
                            if (isAlive(tempGridrgbData, width, x + ix, y + iy)) 
                            {
                                aliveCells++;
                            }
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException ex)
                    {
                    }
                }
            }
        }
        return aliveCells;
    }
    /**
     * 
     * @return Returns how many cells are alive on the grid
     */
    public int getTotalAlive()
    {
        int count = 0;
        int ix;
        int iy;

        int[] rgbData = displayedImage.getRGB(0,0, displayedImage.getWidth(), displayedImage.getHeight(), null, 0, displayedImage.getWidth());
        for (ix = 0; ix < width(); ix++) 
        {
            for (iy = 0; iy < height(); iy++) 
            {
                if (isAlive(rgbData, displayedImage.getWidth(), ix, iy))
                { //If the cell is alive
                    count++;
                }
                
            }
        }
        return count;
    }
    
}
