
package gameoflife;


import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;


/**
 * 
 * @author jacob
 */
public class KeyPressedEvent {
    KeyboardFocusManager keyManager;
    
    public boolean space = false;
    public boolean c = false;
    public boolean v = false;
    public boolean control = false;
    public boolean r = false;
    public boolean p = false;
    public boolean o = false;
    public boolean q = false;
    public boolean plus = false;
    public boolean minus = false;
    
    public boolean up = false;
    public boolean down = false;
    public boolean left = false;
    public boolean right = false;
    
    public boolean backspace = false;
    
    int lastKeyCode;
    public KeyPressedEvent() {
        
        keyManager=KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyManager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                //Test when keys are pressed
                if(e.getID()==KeyEvent.KEY_PRESSED){
                    
                    lastKeyCode = e.getKeyCode();
                    OnKey(e.getKeyCode());
                    return true;
                }
                //Test when keys are released
                if(e.getID()==KeyEvent.KEY_RELEASED){
                    
                    lastKeyCode = e.getKeyCode();
                    OffKey(e.getKeyCode());
                    return true;
                }
                return false;
              }

            });
    }

    private void OnKey(int key) 
    {
        switch (key) {
            case KeyEvent.VK_SPACE:
                space = true;
                break;
            case KeyEvent.VK_Q:
                q = true;
                break;
            case KeyEvent.VK_C:
                c = true;
                break;
            case KeyEvent.VK_V:
                v = true;
                break;
            case KeyEvent.VK_CONTROL:
                control = true;
                break;
            case KeyEvent.VK_P:
                p = true;
                break;
            case KeyEvent.VK_O:
                o = true;
                break;
            case KeyEvent.VK_R:
                r = true;
                break;
            case KeyEvent.VK_EQUALS:
                plus = true;
                break;
            case KeyEvent.VK_MINUS:
                minus = true;
                break;
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            case KeyEvent.VK_W:
                up = true;
                break;
            case KeyEvent.VK_S:
                down = true;
                break;
            case KeyEvent.VK_A:
                left = true;
                break;
            case KeyEvent.VK_D:
                right = true;
                break;
            case KeyEvent.VK_BACK_SPACE:
                backspace = true;
                break;
            case KeyEvent.VK_DELETE:
                backspace = true;
                break;
            default:
                break;
        }
    }
    private void OffKey(int key) {
        switch (key) {
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
            case KeyEvent.VK_W:
                up = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
            case KeyEvent.VK_C:
                c = false;
                break;
            case KeyEvent.VK_V:
                v = false;
                break;
            case KeyEvent.VK_CONTROL:
                control = false;
                break;
            default:
                break;
        }
    }
}
