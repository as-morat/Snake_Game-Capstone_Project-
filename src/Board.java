import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {
    private int dots;

    private Image apple;
    private Image dot;
    private Image head;

    private int apple_x;
    private int apple_y;

    private final int ALL_DOTS = 900;
    private final int DOT_Size = 10;
    private Timer timer;
    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    Board() {
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(300, 300));
        setFocusable(true);

        loadImages();
        initGame();
    }

    private void loadImages() {
        apple = new ImageIcon("D:/Snake_Game/icons/apple.png").getImage();
        dot = new ImageIcon("D:/Snake_Game/icons/dot.png").getImage();
        head = new ImageIcon("D:/Snake_Game/icons/head.png").getImage();
    }

    public void initGame() {
        dots = 3;

        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * DOT_Size;
        }

        locateApple();
        timer = new Timer(140, this);
        timer.start();
    }

    private void locateApple() {
        boolean validPosition;
        do {
            validPosition = true;
            apple_x = (int) (Math.random() * 29) * DOT_Size;
            apple_y = (int) (Math.random() * 29) * DOT_Size;

            for (int i = 0; i < dots; i++) {
                if (x[i] == apple_x && y[i] == apple_y) {
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over !!";
        Font font = new Font("SANS_SERIF", Font.BOLD, 20);
        FontMetrics metrics = getFontMetrics(font);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg, (300 - metrics.stringWidth(msg)) / 2, 300 / 2);
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (leftDirection) {
            x[0] -= DOT_Size;
        }
        if (rightDirection) {
            x[0] += DOT_Size;
        }
        if (upDirection) {
            y[0] -= DOT_Size;
        }
        if (downDirection) {
            y[0] += DOT_Size;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkCollision();
            checkApple();
            move();
        }
        repaint();
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }

        if (y[0] >= 300 || x[0] >= 300 || y[0] < 0 || x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void checkApple() {
        if (x[0] == apple_x && y[0] == apple_y) {
            dots++;
            locateApple();
        }
    }

    public class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            } else if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            } else if (key == KeyEvent.VK_UP && !downDirection) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            } else if (key == KeyEvent.VK_DOWN && !upDirection) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}
