package SnakePt2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600; // The width of the screen
    static final int SCREEN_HEIGHT = 600; // the screen height
    static final int UNIT_SIZE = 25; // the size of each block in the game
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; // the number of units/game blocks the snake can traverse
    static final int DELAY = 70; // the speed of the game
    final int x[] = new int[GAME_UNITS]; // the x coordinates of the snake
    final int y[] = new int[GAME_UNITS]; // the y coordinates of the snake
    int bodyParts = 4; // how many initial bodyparts the snake starts with
    int applesEaten = 0; // the number of apples the snake has eaten
    int appleX; // the x coordinate of the new apple
    int appleY; // the y coordinate of the new apple
    char direction = 'R'; // the current direction of the snake
    boolean running = false; // if the game is running or not
    Timer timer; // used to set the speed of the game and how fast it runs
    Random random;
    int randomColor;


    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        randomColor = random.nextInt(3);
        startGame();
    }
    
    //starts the game 
    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }   

    // updates the screen 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // draws the apple, draws the new body parts of the snake including color, score
    public void draw(Graphics g) {

        if (running) {
            /*for(int i=0; i < (SCREEN_HEIGHT/UNIT_SIZE); i++){ // for gridlines
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }*/

            g.setColor(Color.red);// this draws the apple
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);


            switch (randomColor) { // changes the skin of the snake depending on random number
                case 0:
                    for (int i = 0; i < bodyParts; i++) { // normal green snake
                        if (i == 0) {
                            g.setColor(Color.green.darker());
                            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                        } else {
                            g.setColor(new Color(77, 232, 26));
                            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                        }
                    }
                    break;
                case 1:
                    int j = 0; // red black and yellow snake
                    while (j < bodyParts) {
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

                case 2:
                    for (int i = 0; i < bodyParts; i++) { // random pastel color snake
                        if (i == 0) {
                            float hue = random.nextFloat();
                            // Saturation between 0.1 and 0.3
                            float saturation = (random.nextInt(2000) + 1000) / 10000f;
                            float luminance = 0.9f;
                            Color color = Color.getHSBColor(hue, saturation, luminance);
                            g.setColor(color);
                            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                        } else {
                            float hue = random.nextFloat();
                            // Saturation between 0.1 and 0.3
                            float saturation = (random.nextInt(2000) + 1000) / 10000f;
                            float luminance = 0.9f;
                            Color color = Color.getHSBColor(hue, saturation, luminance);
                            g.setColor(color);
                            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                        }
                    }
                    break;
            }
            //Game Over Text
            g.setColor(Color.white);
            g.setFont(new Font("Times New Roman", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

        } else {
            gameOver(g);
        }
    }
    // generates the coordinate for the next apple 
    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        for (int i = 0; i < x.length; i++) { // calls newApple() again if the new apple is inside the body of the snake 
            if (appleX == x[i] && appleY == y[i]) {
                newApple();
            }
        }
    }
    // updates the position of the snakes body 
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
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
    // checks if the head of the snake is touching an apple 
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // checks for body collison with head
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        //checks if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        //checks if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //checks if head touches upper boarder
        if (y[0] < 0) {
            running = false;
        }
        //checks if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }

    }

    public void gameOver(Graphics g) {
        //Game Over Text
        running = false;
        g.setColor(Color.red);
        g.setFont(new Font("Times New Roman", Font.ITALIC + Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        g.setColor(Color.white);
        g.setFont(new Font("Times New Roman", Font.BOLD, 50));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }

    // when the game is running it moves the snake, checks if touches an apple, and checks if it hit anything, and then when the game is over it blanks out the screen 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();

    }

    // to get user input for the game 
    // checks the direction to make sure that 180 degree turns are impossible 
    public class MyKeyAdapter extends KeyAdapter {
        //Overide
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {

                case KeyEvent.VK_LEFT: // changes the direction to left when user presses left arrow key 
                    if (direction != 'R') { 
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:// changes direction to right when right arrow key is pressed 
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:// changes direction to up when up arrow key is pressed 
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:// changes direction to down when down arrow key is pressed 
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }

        }

    }

}
