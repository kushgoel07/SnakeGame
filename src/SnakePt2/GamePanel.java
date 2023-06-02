package SnakePt2;

import javax.imageio.ImageIO;
import javax.lang.model.type.UnionType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener  { 

    static final int screenWidth = 600; // The width of the screen
    static final int screenHeight = 600; // the screen height
    static final int UNIT_SIZE = 25; // the size of each block in the game
    static final int gameUnits = (screenWidth * screenHeight) / UNIT_SIZE; // the number of units/game blocks the snake can traverse
    static final int DELAY = 80; // the speed of the game

    public int x_cord[] = new int[gameUnits]; // the x coordinates of the snake
    public int y_cord[] = new int[gameUnits]; // the y coordinates of the snake
    int initialSize = 4; // how many initial initialSize the snake starts with
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
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.DARK_GRAY.darker().darker());
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        randomColor = random.nextInt(3);
        startGame();
    }

    //starts the game
    public void startGame() {
        createNewApple();
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

            g.setColor(Color.green.brighter().brighter());// this draws the apple
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < (screenHeight / UNIT_SIZE); i++) { // for gridlines
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, screenHeight);
                g.drawLine(0, i * UNIT_SIZE, screenWidth, i * UNIT_SIZE);
            }


            switch (randomColor) { // changes the skin of the snake depending on random number
                case 0:
                    for (int i = 0; i < initialSize; i++) { // normal green snake
                        if (i == 0) {
                            g.setColor(Color.green.darker());
                            g.fillRect(x_cord[i], y_cord[i], UNIT_SIZE, UNIT_SIZE);
                        } else {
                            g.setColor(new Color(77, 232, 26));
                            g.fillRect(x_cord[i], y_cord[i], UNIT_SIZE, UNIT_SIZE);
                        }
                    }
                    break;
                case 1:
                    int j = 0; // red black and yellow snake
                    while (j < initialSize) {
                        g.setColor(Color.red);
                        g.fillRect(x_cord[j], y_cord[j], UNIT_SIZE, UNIT_SIZE);
                        j++;
                        g.setColor(Color.black);
                        g.fillRect(x_cord[j], y_cord[j], UNIT_SIZE, UNIT_SIZE);
                        j++;
                        g.setColor(Color.yellow);
                        g.fillRect(x_cord[j], y_cord[j], UNIT_SIZE, UNIT_SIZE);
                        j++;
                        g.setColor(Color.black);
                        g.fillRect(x_cord[j], y_cord[j], UNIT_SIZE, UNIT_SIZE);
                        j++;
                    }
                    break;

                case 2: // https://stackoverflow.com/questions/4246351/creating-random-colour-in-java 
                    for (int i = 0; i < initialSize; i++) { // random pastel color snake
                        if (i == 0) {
                            float hue = random.nextFloat();
                            // Saturation between 0.1 and 0.3
                            float saturation = (random.nextInt(2000) + 1000) / 10000f;
                            float luminance = 0.9f;
                            Color color = Color.getHSBColor(hue, saturation, luminance);
                            g.setColor(color);
                            g.fillRect(x_cord[i], y_cord[i], UNIT_SIZE, UNIT_SIZE);
                        } else {
                            float hue = random.nextFloat();
                            // Saturation between 0.1 and 0.3
                            float saturation = (random.nextInt(2000) + 1000) / 10000f;
                            float luminance = 0.9f;
                            Color color = Color.getHSBColor(hue, saturation, luminance);
                            g.setColor(color);
                            g.fillRect(x_cord[i], y_cord[i], UNIT_SIZE, UNIT_SIZE);
                        }
                    }
                    break;
            }
            //Game Over Text
            g.setColor(Color.lightGray);
            g.setFont(new Font("Times New Roman", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (screenWidth - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

        } else {
            endGame(g);
        }
    }

    // generates the coordinate for the next apple
    public void createNewApple() {
        appleX = random.nextInt(screenWidth / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt((int) (screenHeight / UNIT_SIZE)) * UNIT_SIZE;
        for (int i = 0; i < x_cord.length; i++) { // calls newApple() again if the new apple is inside the body of the snake
            if (appleX == x_cord[i] && appleY == y_cord[i]) {
                createNewApple();
            }
        }
    }

    // updates the position of the snakes body
    public void move() {
        for (int i = initialSize; i > 0; i--) {
            x_cord[i] = x_cord[i - 1];
            y_cord[i] = y_cord[i - 1];
        }


        if(direction == 'U'){
            y_cord[0] -= UNIT_SIZE;
        } else if (direction == 'D'){
            y_cord[0] += UNIT_SIZE;
        } else if (direction == 'R'){
            x_cord[0] += UNIT_SIZE;
        } else if (direction == 'L'){
            x_cord[0] -= UNIT_SIZE;
        }
    }

    // checks if the head of the snake is touching an apple
    public void checkApple() {
        if ((x_cord[0] == appleX) && (y_cord[0] == appleY)) {
            initialSize++;
            applesEaten++;
            createNewApple();
        }
    }

    public void checkCollisions() {


        //checks if head touches left border
        if (x_cord[0] < 0) {
            running = false;
        }
        //checks if head touches right border
        if (x_cord[0] > screenWidth) {
            running = false;
        }
        //checks if head touches upper boarder
        if (y_cord[0] < 0) {
            running = false;
        }
        //checks if head touches bottom border
        if (y_cord[0] > screenHeight) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
        // checks for body collison with head
        for (int i = initialSize; i > 0; i--) {
            if ((x_cord[0] == x_cord[i]) && (y_cord[0] == y_cord[i])) {
                running = false;
            }
        }

    }

    public void endGame(Graphics g) {
        //Game Over Text
        running = false;
        g.setColor(Color.red);
        g.setFont(new Font("Times New Roman", Font.ITALIC + Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (screenWidth - metrics.stringWidth("Game Over")) / 2, screenHeight / 2);
        g.setColor(Color.white);
        g.setFont(new Font("Times New Roman", Font.BOLD, 50));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (screenWidth - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
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
