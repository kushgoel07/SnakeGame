import java.awt.*;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{

    //Screen Settings 
    final int originalTileSize = 16; // 16x16 pixel for each tile
    final int scale = 3;// scales the tile size by x

    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreeRow = 12;
    final int screenWidth = tileSize * maxScreenCol; //768 pixels
    final int screenHeight = tileSize * maxScreeRow; //576 pixels

    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player =  new Player(this, keyH);

    // set player's default position 
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        
        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void run() {
        // this is in nanoseconds
        double drawInterval = 1000000000/FPS; // 0.01666 seconds 60FPS
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null){

           // 1 Update: update information such as character position
            update();
           //2 Draw: draw teh screen with the updated information
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if(remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long)remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

    public void update(){
        player.update();

    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics g2 = (Graphics2D)g;

        player.draw((Graphics2D) g2);

        g2.dispose();
    }






    
}
