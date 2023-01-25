import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

public class Tetris extends JPanel {
    final int board_x = 10;
    final int board_y = 20;
    public int tetro_x;
    public int tetro_y;
    private JButton[][] Board;
    JButton[][] nextTetro = new JButton[4][4];
    boolean moveFinish = true;
    boolean gameOver = false;
    Tetromino currTetro;
    int point = 0;
    Tetromino nextTetro_;

    //I add these here because it does not compile at ubuntu
    Timer timer;
    JFrame frame;
    JButton pointButton;
    /////////////////////////////////////////////
    /**
     * Cosntructor of Tetris class
     * It create the frame and other panels
     */
    Tetris() {
        frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(board_y, board_x));
        Board = new JButton[board_y][board_x];
        for (int i = 0; i < board_y; i++) {
            for (int j = 0; j < board_x; j++) {
                Board[i][j] = new JButton();
                Board[i][j].setBackground(Color.WHITE);
                leftPanel.add(Board[i][j]);
            }
        }
        frame.setPreferredSize(new Dimension(600, 700));
        frame.add(leftPanel, BorderLayout.CENTER);
        JButton playButton = new JButton("Play");
        JButton restartButton = new JButton("Restart");
        JButton quitButton = new JButton("Quit");
        pointButton = new JButton("Point");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        restartButton.setSize(10, 30);
        quitButton.setSize(10, 30);
        pointButton.setSize(10, 30);

        buttonPanel.add(playButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(quitButton);
        buttonPanel.add(pointButton);
        JPanel container = new JPanel();

        container.add(buttonPanel, BorderLayout.NORTH);

        JPanel nextTetroPanel = new JPanel();
        nextTetroPanel.setLayout(new GridLayout(4, 4));
        nextTetroPanel.setPreferredSize(new Dimension(120, 120));
        container.add(nextTetroPanel, BorderLayout.SOUTH);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                nextTetro[i][j] = new JButton();
                nextTetro[i][j].setBackground(Color.WHITE);
                nextTetroPanel.add(nextTetro[i][j]);
            }
        }
        frame.add(container, BorderLayout.EAST);

        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.addKeyListener(new MyKeyListener());

        currTetro = createTetro();
        timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    if (moveFinish == true) {
                        nextTetro_ = createTetro();
                        if (AddToBoard() == false) {
                            gameOver();
                        }
                        moveFinish = false;
                    } else {
                        changeNextTetroPanel(1);

                        if (move() == 0) {
                            moveFinish = false;
                        } else {
                            moveFinish = true;
                            controlFullLine();
                            currTetro = nextTetro_;
                            changeNextTetroPanel(0);
                        }
                    }
                }
                pointButton.setText("Score: " + Integer.toString(point));
            }
        });
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timer.start();
                frame.requestFocus();
            }
        });
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restart();
                timer.start();
                frame.requestFocus();
            }
        });
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });

    }

    /**
     * It makes point 0 and clear the board then start game again
     */
    public void restart() {
    
            point = 0;
            currTetro = null;
            for (int a = 0; a < board_y; a++) {
                for (int b = 0; b < board_x; b++) {
                    Board[a][b].setBackground(Color.WHITE);
                }
            }
            changeNextTetroPanel(0);
            currTetro = createTetro();
            gameOver = false;
        

    }

    public void quit() {
        System.exit(0);
    }

    /**
     * Pop when the game end
     */
    public void gameOver() {
        /// ekrana pop up fırlat
        int result = JOptionPane.showOptionDialog(null, "Game Over. Your score is: " + point, "Game Over",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] { "OK" }, null);
        quit();
    }

    /**
     * This change next tetromino panel. When the new tetro came it clear the panel
     * for it
     * 
     * @param control if it 0 this function clear the panel for new nexttetromino,
     *                if it 1 write tetromino at the panel
     */
    public void changeNextTetroPanel(int control) {
        Color color;
        if (control == 1) {
            for (int i = 0; i < nextTetro_.getRow(); i++) {
                for (int j = 0; j < nextTetro_.getCol(); j++) {
                    if (nextTetro_.getBody()[i][j] != ' ') {
                        switch (nextTetro_.getType()) {
                            case "I":
                                color = Color.BLUE;
                                break;
                            case "J":
                                color = Color.CYAN;
                                break;
                            case "L":
                                color = Color.ORANGE;
                                break;
                            case "O":
                                color = Color.MAGENTA;
                                break;
                            case "S":
                                color = Color.RED;
                                break;
                            case "T":
                                color = Color.YELLOW;
                                break;
                            case "Z":
                                color = Color.GREEN;
                                break;
                            default:
                                color = Color.WHITE;
                        }
                        nextTetro[i][j].setBackground(color);

                    }
                }
            }
        } else {
            for (int i = 0; i < nextTetro_.getRow(); i++) {
                for (int j = 0; j < nextTetro_.getCol(); j++) {
                    nextTetro[i][j].setBackground(Color.WHITE);
                }
            }
        }

    }

    /**
     * move tetromino one left
     */
    public void moveLeft() {
        int temp_x = tetro_x - 1;
        if (temp_x < 0) {
        } else {
            deleteOld(currTetro, tetro_y, tetro_x);
            if (controlCollide(currTetro, tetro_x - 1, tetro_y)) {
                tetro_x--;
                overwrite(currTetro, tetro_y, tetro_x);
            } else {
                overwrite(currTetro, tetro_y, tetro_x);
            }
        }

    }

    /**
     * move tetromino one right
     */
    public void moveRight() {
        int temp_x = tetro_x + 1;
        if (temp_x + currTetro.getCol() - 1 >= board_x) {

        } else {

            deleteOld(currTetro, tetro_y, tetro_x);
            if (controlCollide(currTetro, tetro_x + 1, tetro_y)) {
                tetro_x++;
                overwrite(currTetro, tetro_y, tetro_x);
            } else {
                overwrite(currTetro, tetro_y, tetro_x);
            }
        }
    }

    /**
     * delete old lcoation of tetromino
     * 
     * @param tetro
     * @param locy
     * @param locx
     */
    public void deleteOld(Tetromino tetro, int locy, int locx) {
        for (int a = 0; a < tetro.getRow(); a++) {
            for (int b = 0; b < tetro.getCol(); b++) {
                if (currTetro.getBody()[a][b] != ' ') {
                    deleteColor(locy, locx + b);
                }
            }
            locy++;
        }
    }

    /**
     * overwrite the tetros body to given location
     * 
     * @param tetro
     * @param locy
     * @param locx
     */
    public void overwrite(Tetromino tetro, int locy, int locx) {
        for (int a = 0; a < tetro.getRow(); a++) {
            for (int b = 0; b < tetro.getCol(); b++) {
                if (currTetro.getBody()[a][b] != ' ') {
                    changeColor(locy, locx + b, currTetro.getType());
                }
            }
            locy++;
        }
    }

    /**
     * change colour of specisif jbutton
     * 
     * @param y
     * @param x
     * @param shape
     */
    public void changeColor(int y, int x, String shape) {
        Color color;
        switch (shape) {
            case "I":
                color = Color.BLUE;
                break;
            case "J":
                color = Color.CYAN;
                break;
            case "L":
                color = Color.ORANGE;
                break;
            case "O":
                color = Color.MAGENTA;
                break;
            case "S":
                color = Color.RED;
                break;
            case "T":
                color = Color.YELLOW;
                break;
            case "Z":
                color = Color.GREEN;
                break;
            default:
                color = Color.WHITE;
        }
        Board[y][x].setBackground(color);
    }

    /**
     * make white specific jbutton
     * 
     * @param y
     * @param x
     */
    public void deleteColor(int y, int x) {
        Board[y][x].setBackground(Color.WHITE);
    }

    /**
     * move tetromino down 1 blok
     * 
     * @return
     */
    public int move() {
        if (tetro_y >= board_y - currTetro.getRow()) {
            return 1;
        } else {
            int[] lookfor = new int[currTetro.getCol()];
            updateLookfor(currTetro, lookfor, 'D');

            for (int c = 0; c < currTetro.getCol(); c++) {
                if (Board[tetro_y + lookfor[c]][tetro_x + c].getBackground() != Color.WHITE) {

                    return 1;
                }
            }
            for (int b = 0; b < currTetro.getRow(); b++) {
                for (int c = 0; c < currTetro.getCol(); c++) {
                    if (currTetro.getBody()[b][c] != ' ') {
                        // changeColor(tetro_y + b, tetro_x + c, currTetro.getType());
                        deleteColor(tetro_y + b, c + tetro_x);
                    }
                }
            }
            tetro_y++;

            for (int b = 0; b < currTetro.getRow(); b++) {
                for (int c = 0; c < currTetro.getCol(); c++) {
                    if (currTetro.getBody()[b][c] != ' ') {
                        changeColor(tetro_y + b, tetro_x + c, currTetro.getType());
                    }
                }
            }
        }
        for (int a = 0; a < currTetro.getCol(); a++) {
            if (currTetro.getBody()[0][a] != ' ') {

                deleteColor(tetro_y - 1, tetro_x + a);
            }
        }
        point += 5;
        return 0;

    }

    /**
     * add tetromino to the board
     * if it cannot add game over
     * 
     * @return
     */
    public boolean AddToBoard() {
        int locx = (board_x / 2) - 1;
        int locy = 0;
        tetro_x = locx;
        tetro_y = 0;
        if (controlCollide(currTetro, locx, locy)) {

            for (int a = 0; a < currTetro.getRow(); a++) {
                for (int b = 0; b < currTetro.getCol(); b++) {
                    if (currTetro.getBody()[a][b] != ' ') {

                        changeColor(locy + a, locx + b, currTetro.getType());
                    }
                }
                locy = 0;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * control every line after every tetromino move down completely
     * if it found fullline call deleteFullLine funciton
     */
    public void controlFullLine() {
        boolean isFull = true;
        for (int a = 0; a < board_y; a++) {
            isFull = true;
            for (int b = 0; b < board_x; b++) {
                if (Board[a][b].getBackground() == Color.WHITE) {
                    isFull = false;
                }
            }
            if (isFull == true) {
                deleteFullLine(a);
            }
        }
    }

    /**
     * delete given line
     * 
     * @param lineNum
     */
    public void deleteFullLine(int lineNum) {
        for (int b = 0; b < board_x; b++) {
            deleteColor(lineNum, b);
        }
        for (int a = lineNum; a > 0; a--) {
            for (int b = 0; b < board_x; b++) {
                Board[a][b].setBackground(Board[a - 1][b].getBackground());
                deleteColor(a - 1, b);
            }
        }
        point += 100;
    }

    /**
     * control collide for the given coordinate
     * 
     * @param x
     * @param y
     * @return
     */
    public boolean controlCollide(Tetromino tetro, int x, int y) {

        for (int a = 0; a < tetro.getRow(); a++) {
            for (int b = 0; b < tetro.getCol(); b++) {

                if (Board[y][x + b].getBackground() != Color.WHITE && tetro.getBody()[a][b] != ' ') {

                    return false;
                }
            }
            y++;
        }
        return true;
    }

    /**
     * with this funciton tetromino does not collide with already placed tetromino
     * 
     * @param tetro
     * @param lookfor
     * @param moveDir
     */
    public void updateLookfor(Tetromino tetro, int[] lookfor, char moveDir) {
        boolean isfound = false;
        for (int a = 0; a < tetro.getCol(); a++) {
            lookfor[a] = tetro.getRow() - 1;
        }
        isfound = false;
        for (int c = 0; c < tetro.getCol(); c++) {
            isfound = false;
            for (int b = tetro.getRow() - 1; b >= 0 && !isfound; b--) {
                if (tetro.getBody()[b][c] != ' ') {
                    isfound = true;
                    lookfor[c] = b + 1;
                }
            }
        }
    }

    /**
     * create new tetromino
     * 
     * @return
     */
    public Tetromino createTetro() {
        Random rand = new Random();
        int randTetro = rand.nextInt(7) + 0;
        Tetromino.Shape shapeEnum = Tetromino.Shape.values()[randTetro];
        Tetromino tetro = new Tetromino(shapeEnum);
        tetro_x = (board_x / 2) - 1;
        tetro_y = 0;
        return tetro;
    }

    /**
     * with this extra class player can use keyboard to control tetromino
     */
    private class MyKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                moveLeft();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                moveRight();
            } else if (e.getKeyCode() == KeyEvent.VK_R) {
                deleteOld(currTetro, tetro_y, tetro_x);
                rotateAble();
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                while (move() == 0) {

                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    /**
     * this function look for tetromino can rotate
     * 
     * @return
     */
    public boolean rotateAble() {
        int y = tetro_y;
        int x = tetro_x;
        currTetro.rotate('L');
        if (x + currTetro.getCol() >= board_x) {
            x = board_x - currTetro.getCol();
        }
        if (controlCollide(currTetro, x, y)) {
            overwrite(currTetro, y, x);
        } else {
            currTetro.rotate('R');
            overwrite(currTetro, y, tetro_x);
        }
        tetro_x = x;
        return true;
    }
}
