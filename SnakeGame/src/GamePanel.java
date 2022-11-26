import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNITE_SIZE = 25;
    static final int GAME_SIZE = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNITE_SIZE;
    static  int DELAY = 150;

    final int x[] = new int[GAME_SIZE];  // X Array holds the x coordinates in game including snake's head.
    final int y[] = new int[GAME_SIZE]; // Y Array holds the y coordinates in game.

    int bodyPart = 6;
    int appleEaten;
    int appleX;                         // X coordinate place where apples appear Randomly , So value is zero .
    int appleY;                         // Y coordinate place where apples appear Randomly , So value is zero .
    char direction = 'R';               // At starting of the game Snake will move in right direction as per instruction given by user.
    boolean running = false;            // Running status of the snake in start of the game , is false.
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(new Color(13, 17, 23));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.startGame();
    }

    public void startGame(){
        newApple();                                 // This method Randomly appear new apple on screen
        running = true;                             // Initially snake is at rest position , After staring the game Running = true.
        timer = new Timer(DELAY,this);       // this method will delay the new apple appearing on screen.
        timer.start();                              // Start the new apple

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){
        if(running) {
//            for (int i = 0; i < SCREEN_HEIGHT / UNITE_SIZE; i++) {
//                g.drawLine(i * UNITE_SIZE, 0, i * UNITE_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNITE_SIZE, SCREEN_WIDTH, i * UNITE_SIZE);
//            }
//            JLabel label = new JLabel();
//            label.setIcon(new ImageIcon("apple.png"));
//            Dimension size = label.getPreferredSize();
//            label.setBounds(appleX, appleY, UNITE_SIZE, UNITE_SIZE);

            g.setColor(new Color(220, 75, 75));                                          // Drawing of apple
            g.fillOval(appleX, appleY, UNITE_SIZE, UNITE_SIZE);

            for (int i = 0; i < bodyPart; i++) {
                if (i == 0) {
                    g.setColor(new Color(191, 127, 63));
                    g.fillRect(x[i], y[i], UNITE_SIZE, UNITE_SIZE  );
                } else {
                    g.setColor(new Color(239, 185, 120));
                    g.fillRect(x[i], y[i], UNITE_SIZE, UNITE_SIZE);
                }
            }
            g.setColor(Color.cyan);
            g.setFont(new Font("Magneto", Font.PLAIN,35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score - " + appleEaten, (SCREEN_WIDTH - metrics.stringWidth("Score" + appleEaten))/2 , g.getFont().getSize()  );
        }
        else {
            gameOver(g);
        }


    }
    public void newApple(){                          // new apple method is to generate coordinates of new apple every time this method is called.
        appleX = random.nextInt((int) (SCREEN_WIDTH/UNITE_SIZE))*UNITE_SIZE;    // Size of the randomly appearing apple = unite size of the screen
        appleY = random.nextInt((int) (SCREEN_HEIGHT/UNITE_SIZE))*UNITE_SIZE;
    }
    public void  move(){                            // Moving of snake in coordinates on Screen
        for (int i = bodyPart; i>0 ; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
            case 'U':
                y[0] = y[0]-UNITE_SIZE;
                break;
            case 'D':
                y[0] = y[0]+UNITE_SIZE;
                break;
            case 'R':
                x[0] = x[0]+UNITE_SIZE;
                break;
            case 'L':
                x[0] = x[0]-UNITE_SIZE;
                break;
        }


    }
    public void checkApple(){
        if((x[0]==appleX && y[0]==appleY)){
            bodyPart++;                                 // snake body will increase after eating an apple
            appleEaten++;                               // check the score of an apple eaten by a snake
            DELAY--;
            newApple();                                // Generates a new Apple
        }

    }
    public void checkCollisions(){
        for (int i = bodyPart; i > 0 ; i--) {                       // Checks the head collided with the body
            if ((x[0] == x[i]) && y[0] == y[i]) {
                running = false;
                break;
            }
        }
        if(x[0] < 0){                                               // Checks the head collided with left side of screen
            running = false;
        }
        if(x[0] > SCREEN_WIDTH){                                    // Checks the head collided with right side of screen
            running = false;
        }
        if(y[0] < 0){                                               // Checks the head collided with top side of screen
            running = false;
        }
        if(y[0] > SCREEN_HEIGHT){                                   // Checks the head collided with bottom side of screen
            running = false;
        }
        if(!running){
            timer.stop();
        }


    }
    public void gameOver(Graphics g){
        // Score Function................................................
        g.setColor(Color.cyan);
        g.setFont(new Font("Magneto", Font.PLAIN,35));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score - " + appleEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score" + appleEaten))/2 , g.getFont().getSize()  );


        // Game Over Functions............................................
        g.setColor(Color.RED);
        g.setFont(new Font("Snap ITC", Font.PLAIN,70));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2 , SCREEN_HEIGHT/2  );

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
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){

            switch (e.getKeyCode()){
                case KeyEvent.VK_A:
                    if(direction!='R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_D:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_W:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_S:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;



            }

        }


    }


}
