import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private class Tile{
        int x;
        int y;
        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    int boardHeight;
    int boardWidth;
    int tileSize = 25;

    //Snake
    Tile snakehead;
    ArrayList<Tile> snakeBody;

    //Food
    Tile food;
    Random random;

    //Game Logic
    Timer gameloop;
    int velocityX;
    int velocityY; 
    boolean gameOver = false;
    private boolean gamePaused = false;


    SnakeGame(int boardHeight, int boardWidth){
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakehead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;
        gameloop = new Timer(100, this);
        gameloop.start();
        boolean gamePaused = false;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //For grid
       /* for(int i=0; i < boardWidth/tileSize; i++){
            g.drawLine(i*tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i*tileSize);
        }
        */ 

        //For food
        g.setColor(Color.red);
        //g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize,true);

        //For snake head
        g.setColor(Color.green);
        //g.fillRect(snakehead.x*tileSize, snakehead.y*tileSize, tileSize, tileSize);
        g.fill3DRect(snakehead.x*tileSize, snakehead.y*tileSize, tileSize, tileSize,true);

        //For snake body
        for(int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x * tileSize, snakePart.y *tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y *tileSize, tileSize, tileSize,true);
        }
        //Score
        g.setFont(new Font("Arial",Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            Font gameOverFont = new Font("Arial", Font.BOLD, 24); // Adjust the font size as needed
            g.setFont(gameOverFont);
            String gameOverMessage = "Game Over: " + String.valueOf(snakeBody.size());
            g.drawString(gameOverMessage, boardWidth / 2 - g.getFontMetrics().stringWidth(gameOverMessage) / 2, boardHeight / 2 - 12);
            String restartMessage = "Press 'R' to restart";
            g.drawString(restartMessage, boardWidth / 2 - g.getFontMetrics().stringWidth(restartMessage) / 2, boardHeight / 2 + 12);
        } else {
            g.setColor(Color.white); // Change the color to white for the score
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }

    }
    public void restartGame() {
        // Reset the game state
        snakehead = new Tile(5, 5);
        snakeBody.clear();
        placeFood();
        velocityX = 0;
        velocityY = 0;
        gameOver = false;

        // Restart the game loop
        gameloop.start();
    }
    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }
    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    
    public void move(){
        //eat food
        if(collision(snakehead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        //Snake body
        for(int i = snakeBody.size()-1; i >= 0; i--){
            Tile snakePart = snakeBody.get(i);
            if(i == 0){
                snakePart.x = snakehead.x;
                snakePart.y = snakehead.y;
            }
            else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snake head
        snakehead.x += velocityX;
        snakehead.y += velocityY;

         //Gameover condition
         for(int i = 0; i<snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //collision with snake Head
            if(collision(snakehead, snakePart)){
                gameOver = true;
            }
        }
        if(snakehead.x*tileSize < 0 || snakehead.y*tileSize >boardWidth ||
        snakehead.y*tileSize < 0 || snakehead.x*tileSize >boardHeight){
            gameOver = true;
        }
    }
    
    //After 100 milliseconds we call actionPerformed 
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameloop.stop();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
       if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
        velocityX = 0;
        velocityY = -1;
       }
       else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
        velocityX = 0;
        velocityY = 1;
       }
       else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
        velocityX = -1;
        velocityY = 0;
       }
       else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
        velocityX = 1;
        velocityY = 0;
       }
       if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
        // Restart the game if 'R' is pressed and the game is over
        restartGame();
    }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
   
    @Override
    public void keyReleased(KeyEvent e) {
       
    }
    
}
