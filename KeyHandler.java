import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

    public boolean upPressed, downPressed, leftPressed, rightPressed;

    @Override
    public void keyPressed(KeyEvent e) {
        
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = true;
            downPressed=false;
            leftPressed=false;
            rightPressed=false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
            upPressed = false;
            leftPressed = false;
            rightPressed = false;
        }   
        if(code == KeyEvent.VK_A){
            leftPressed = true;
            rightPressed =false;
            upPressed = false;
            downPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
            leftPressed = false;
            upPressed=false;
            downPressed=false;
        }
        throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }
    
}
