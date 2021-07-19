
package gameoflife;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.sound.sampled.LineUnavailableException;

/**
 * 
 * @author jacob
 */
public class MousePressedEvent 
{
    
    public boolean mouse1Pressed;
    public boolean justPressedMouse1;
    public boolean mouse2Pressed;
    public boolean justPressedMouse2;
    public boolean mouse3Pressed;
    public boolean justPressedMouse3;
    
    public int zoom = 1;
    
    public MousePressedEvent(Window window) throws LineUnavailableException 
    {
        window.frame.addMouseListener(new MouseAdapter() 
        {

                @Override
                public void mousePressed(MouseEvent e) 
                {
                    switch (e.getButton()) {
                        case MouseEvent.BUTTON1:
                            mouse1Pressed = true;
                            justPressedMouse1 = true;
                            break;
                        case MouseEvent.BUTTON2:
                            mouse2Pressed = true;
                            justPressedMouse2 = true;
                            break;
                        case MouseEvent.BUTTON3:
                            mouse3Pressed = true;
                            justPressedMouse3 = true;
                            break;
                        default:
                            break;
                    }
                }
                @Override
                public void mouseReleased(MouseEvent e) 
                {
                    switch (e.getButton()) {
                        case MouseEvent.BUTTON1:
                            mouse1Pressed = false;
                            justPressedMouse1 = false;
                            break;
                        case MouseEvent.BUTTON2:
                            mouse2Pressed = false;
                            justPressedMouse2 = false;
                            break;
                        case MouseEvent.BUTTON3:
                            mouse3Pressed = false;
                            justPressedMouse3 = false;
                            break;
                        default:
                            break;
                    }
                    
                }
            });
        window.frame.addMouseWheelListener(new MouseWheelListener() 
        {
                @Override
                public void mouseWheelMoved(MouseWheelEvent mwe) {
                    double anchorX = (double) (window.getMouseX() - window.displayX) / (double) zoom;
                    double anchorY = (double) (window.getMouseY() - window.displayY) / (double) zoom;
                    
                    zoom -= mwe.getWheelRotation();
                    if (zoom < 1) {
                        zoom = 1;
                    }
                    
                    double newX = (double) (window.getMouseX() - window.displayX) / (double) zoom;
                    double newY = (double) (window.getMouseY() - window.displayY) / (double) zoom;
                    
                    window.displayX += (newX - anchorX) * (double) zoom;
                    window.displayY += (newY - anchorY) * (double) zoom;
                    
                }

            });
    }
}
