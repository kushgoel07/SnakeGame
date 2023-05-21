
package SnakePt2;
import java.awt.*;
import java.awt.event.*;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 70;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 5;
    int applesEaten = 0;
    int appleX; 
    int appleY;
    char direction = 'R';
    boolean running = false; 
    Timer timer;
    Random random; 
    int randomColor;
    

    public GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        randomColor = random.nextInt(3);
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        
        if(running){
            /*for(int i=0; i < (SCREEN_HEIGHT/UNIT_SIZE); i++){ // for gridlines
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }*/

            g.setColor(Color.red);// this draws the apple 
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);


            switch(randomColor){ // changes the skin of the snake depending on random number
                case 0:
                    for(int i=0; i<bodyParts;i++){ // normal green snake 
                        if(i == 0){
                            g.setColor(Color.green.darker());
                            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                        }
                        else{
                            g.setColor(new Color(77, 232, 26));
                            g.fillRect(x[i], y[i], UNIT_SIZE,UNIT_SIZE);
                        }
                    }
                    break;
                case 1: 
                    int j = 0; // red black and yellow snake
                    while (j <bodyParts){
                        g.setColor(Color.red);
                        g.fillRect(x[j], y[j], UNIT_SIZE, UNIT_SIZE);
                        j++;
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(x[j], y[j], UNIT_SIZE, UNIT_SIZE);
                        j++;
                        g.setColor(Color.yellow);
                        g.fillRect(x[j], y[j], UNIT_SIZE, UNIT_SIZE);
                        j++;
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(x[j], y[j], UNIT_SIZE, UNIT_SIZE);
                        j++;
                    }
                    break;
                
                case 2 : 
                    for (int i = 0 ; i < bodyParts; i++){ // random pastel color snake 
                        if(i == 0){
                            float hue = random.nextFloat();
                            // Saturation between 0.1 and 0.3
                            float saturation = (random.nextInt(2000) + 1000) / 10000f;
                            float luminance = 0.9f;
                            Color color = Color.getHSBColor(hue, saturation, luminance);
                            g.setColor(color);
                            g.fillRect(x[i], y[i], UNIT_SIZE,UNIT_SIZE);
                        }else {
                            float hue = random.nextFloat();
                            // Saturation between 0.1 and 0.3
                            float saturation = (random.nextInt(2000) + 1000) / 10000f;
                            float luminance = 0.9f;
                            Color color = Color.getHSBColor(hue, saturation, luminance);
                            g.setColor(color);
                            g.fillRect(x[i], y[i], UNIT_SIZE,UNIT_SIZE);
                        }
                    }
                    break; 
            }
             //Game Over Text 
            g.setColor(Color.white);
            g.setFont(new Font("Times New Roman",Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH-metrics.stringWidth("Score: "+ applesEaten))/2, g.getFont().getSize());
        
        }else {
            gameOver(g);
        }
    }   
    public void newApple(){
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] += UNIT_SIZE;
                break;
            case 'L':
                x[0] -= UNIT_SIZE;
                break;
        }
    }

    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){
        // checks for body collison with head 
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i])&& (y[0] == y[i])){
                running = false;
            }
        }

        //checks if head touches left border
        if(x[0] < 0){
            running = false;
        }
        //checks if head touches right border 
        if (x[0] > SCREEN_WIDTH){
            running = false;
        }
        //checks if head touches upper boarder
        if(y[0] < 0){
            running = false;
        }
        //checks if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }
        if(!running){
            timer.stop();
        }

    }

    public void gameOver(Graphics g){
        //Game Over Text 
        running = false;
        g.setColor(Color.red);
        g.setFont(new Font("Times New Roman",Font.ITALIC + Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH-metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        g.setColor(Color.white);
        g.setFont(new Font("Times New Roman",Font.BOLD, 50));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH-metrics.stringWidth("Score: "+ applesEaten))/2, g.getFont().getSize());
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
        
    }



    public class MyKeyAdapter extends KeyAdapter{
        //Overide 
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }

        }

    }

}