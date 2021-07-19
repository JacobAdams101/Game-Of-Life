
package gameoflife;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author Jadams14
 */
public class Tools {
    
    //Paint tool variables    
    private int selectX;
    private int selectY;
    //Select tool variables
    private int savedX;
    private int savedY;
    
    private int savedX2;
    private int savedY2;
    
    private boolean savedInUse;
    
    private ToolType toolUsing;
    
    private boolean onGrid = false;
    /**
     * 
     */
    public Tools()
    {
        selectX = 0;
        selectY = 0;
        
        savedInUse = false;
        
        toolUsing = ToolType.PAINTTOOL;
    }
    /**
     * 
     */
    enum ToolType
    {
        PAINTTOOL,
        SELECTTOOL,
    }
    /**
     * 
     * @return 
     */
    public int getSelectedX()
    {
        return selectX;
    }
    /**
     * 
     * @return 
     */
    public int getSelectedY()
    {
        return selectY;
    }
    /**
     * 
     * @return 
     */
    public int getSavedX()
    {
        return savedX;
    }
    /**
     * 
     * @return 
     */
    public int getSavedY()
    {
        return savedY;
    }
    /**
     * 
     * @return 
     */
    public int getSavedX2()
    {
        return savedX2;
    }
    /**
     * 
     * @return 
     */
    public int getSavedY2()
    {
        return savedY2;
    }
    /**
     * 
     * @return 
     */
    public boolean savedInUse()
    {
        return savedInUse;
    }
    /**
     * 
     * @return 
     */
    public ToolType getToolUsing()
    {
        return toolUsing;
    }
    /**
     * 
     * @param set 
     */
    public void setTool(ToolType set)
    {
        toolUsing = set;
    }
    /**
     * 
     * @return 
     */
    public boolean onGrid() {
        return onGrid;
    }
    /**
     * 
     * @param game 
     */
    public void run(Game game)
    {
        try 
        {
            selectX = (game.win.getMouseX() - game.win.displayX) / game.win.mouse.zoom;
            selectY = (game.win.getMouseY() - game.win.displayY) / game.win.mouse.zoom;
        } 
        catch (ArithmeticException ex) 
        {
            selectX = 1;
            selectY = 1;
        }
        
        if (toolUsing == ToolType.PAINTTOOL)
        {
            savedInUse = false;
            if (game.win.mouse.mouse1Pressed) 
            {
                game.grid.set(selectX, selectY, true);
                game.refreshAliveCells();
            }
            if (game.win.mouse.mouse3Pressed) 
            {
                game.grid.set(selectX, selectY, false);
                game.refreshAliveCells();
            }
            onGrid = game.tool.getSelectedX() >= 0 && game.tool.getSelectedX() < game.grid.width() && game.tool.getSelectedY() >= 0 && game.tool.getSelectedY() < game.grid.height();
        
        }
        else if (toolUsing == ToolType.SELECTTOOL)
        {
            if (game.win.key.c && game.win.key.control) 
            {
                if (savedInUse)
                {
                    int ix;
                    int iy;
                    int smallX, smallY, largeX, largeY;
                    if (savedX2 > savedX)
                    {
                        smallX = savedX;
                        largeX = savedX2;
                    }
                    else
                    {
                        largeX = savedX;
                        smallX = savedX2;
                    }
                    if (savedY2 > savedY)
                    {
                        smallY = savedY;
                        largeY = savedY2;
                    }
                    else
                    {
                        largeY = savedY;
                        smallY = savedY2;
                    }
                    //Save image to the clipboard
                    BufferedImage region = game.grid.getImage(smallX, smallY, largeX - smallX + 1, largeY - smallY + 1);
                    TransferableImage trans = new TransferableImage( region );
                    Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                    c.setContents( trans, null );
                    
                    game.win.key.c = false;
                }
                
            }
            if (game.win.mouse.justPressedMouse1) 
            {
                savedInUse = true;
                game.win.mouse.justPressedMouse1 = false;
                savedX = selectX;
                savedY = selectY;
            }
            
            if (game.win.mouse.mouse1Pressed) {
                savedX2 = selectX;
                savedY2 = selectY;
            }
            
            if (game.win.key.backspace) {
                game.win.key.backspace = false;
                game.grid.setRect(savedX, savedY, savedX2, savedY2, false);
                game.refreshAliveCells();
            }

            
            onGrid = true;
        }
        if (game.win.key.v && game.win.key.control) 
        {
            //create clipboard object
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            try {
                //Get data from clipboard and assign it to an image.
                //clipboard.getData() returns an object, so we need to cast it to a BufferdImage.
                BufferedImage image = (BufferedImage)clipboard.getData(DataFlavor.imageFlavor);
                game.grid.draw(image, selectX, selectY);
            }
            //getData throws this.
            catch(UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
            }

            catch(IOException ioe) {
                ioe.printStackTrace();
            }

            game.win.key.v = false;
            game.refreshAliveCells();
        }
    }
    /**
     * 
     */
    private class TransferableImage implements Transferable {

        Image i;

        public TransferableImage( Image i ) {
            this.i = i;
        }

        public Object getTransferData( DataFlavor flavor )
        throws UnsupportedFlavorException, IOException {
            if ( flavor.equals( DataFlavor.imageFlavor ) && i != null ) {
                return i;
            }
            else {
                throw new UnsupportedFlavorException( flavor );
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[ 1 ];
            flavors[ 0 ] = DataFlavor.imageFlavor;
            return flavors;
        }

        public boolean isDataFlavorSupported( DataFlavor flavor ) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for ( int i = 0; i < flavors.length; i++ ) {
                if ( flavor.equals( flavors[ i ] ) ) {
                    return true;
                }
            }

            return false;
        }
    }
}
