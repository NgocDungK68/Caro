package com.mpec.caro;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyWindow extends JFrame implements ActionListener {
    private String text;
    private Cursor cursor = new Cursor();
    private Thread thread;
    private Date currentDate = new Date();
    private boolean runTimer = true;
    private int row = 15;
    private int column = 20;
    private boolean[][] isAvailable = new boolean[row][column];
    private int numOfButtons = row * column;
    private String player = "X";
    
    private JPanel panel;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JLabel timer;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel gameNumberLabel;
    private JButton newGameButton;
    private JButton undoButton;
    private JButton[][] buttons = new JButton[row][column];

    private static int playerOneScore = 0;
    private static int playerTwoScore = 0;
    private static int gameNumber = 1;
    private static String playerGame;
    
    public MyWindow(String text) {
        this.text = text;
        this.setTitle("Caro - " + text);
        this.setSize(1000, 800);
        this.setLayout(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        
        panel = new JPanel();
        panel.setBounds(0, 100, 880, 670);
        panel.setLayout(new GridLayout(row, column));
        this.add(panel);
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 13));
		buttons[i][j].setBackground(Color.white);
                buttons[i][j].setActionCommand(i + "," + j);
                buttons[i][j].addActionListener(this);
                
                isAvailable[i][j] = true;
               
		panel.add(buttons[i][j]);
            }
        }
        
        panel2 = new JPanel();
        panel2.setBounds(880, 200, 100, 100);
        panel2.setLayout(new GridLayout(3, 1));
        panel2.setBackground(Color.BLACK);
        this.add(panel2);
        
        timer = new JLabel();
        timer.setFont(new Font("Arial", Font.BOLD, 20));
        timer.setForeground(Color.WHITE);
        panel2.add(timer);
        
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (runTimer) {
                    try {
                        DateFormat dateFormat = new SimpleDateFormat("mm:ss");
                        Date date = new Date();
                        date.setTime(date.getTime() - currentDate.getTime());
                        timer.setText("    " + dateFormat.format(date));
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MyWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        thread.start();
        
        newGameButton = new JButton("New game");
        newGameButton.addActionListener(this);
        undoButton = new JButton("Undo");
        undoButton.addActionListener(this);
        undoButton.setEnabled(false);
        panel2.add(newGameButton);
        panel2.add(undoButton);
        
        label2 = new JLabel("Player 1: " + playerOneScore);
        label3 = new JLabel("Player 2: " + playerTwoScore);
        label2.setFont(new Font("Arial", Font.BOLD, 25));
        label3.setFont(new Font("Arial", Font.BOLD, 25));
        label4 = new JLabel();
        label4.setBounds(250, 15, 500, 100);
        label4.setFont(new Font("Arial", Font.BOLD, 50));
        label4.setForeground(Color.GREEN);
        this.add(label4);
        
        playerGame = gameNumber % 2 == 1 ? "X" : "O";
        label5 = new JLabel(playerGame);
        label6 = new JLabel(changePlayer(playerGame));
        label5.setFont(new Font("Arial", Font.BOLD, 25));
        label6.setFont(new Font("Arial", Font.BOLD, 25));
        label5.setForeground(getColor(playerGame));
        label6.setForeground(getColor(changePlayer(playerGame)));
        
        panel3 = new JPanel(new FlowLayout());
        panel4 = new JPanel(new FlowLayout());
        panel3.setBounds(600, 15, 300, 35);
        panel4.setBounds(600, 55, 300 ,35);
        this.add(panel3);
        this.add(panel4);
        panel4.add(label6);
        panel4.add(label3);
        panel3.add(label5);
        panel3.add(label2);

        gameNumberLabel = new JLabel("Game: " + gameNumber);
        gameNumberLabel.setBounds(15, 25, 200, 100);
        gameNumberLabel.setFont(new Font("Arial", Font.BOLD, 30));
        this.add(gameNumberLabel);
    }
    
    private Color getColor(String player) {
        return "X".equals(player) ? Color.RED : Color.BLUE;
    }
    
    private String changePlayer(String player) {
        return "X".equals(player) ? "O" : "X";
    }
    
    private void drawPiece(int i, int j) {
        Color color = getColor(player);
        buttons[i][j].setText(player);
        buttons[i][j].setForeground(color);
    }
    
    private boolean checkWin(int i, int j) {
        String pl = buttons[i][j].getText();
        return checkDirection(i, j, 0, 1, pl) ||
                checkDirection(i, j, 1, 0, pl) ||
                checkDirection(i, j, 1, 1, pl) ||
                checkDirection(i, j, 1, -1, pl);
    }
    
    private boolean checkDirection(int i, int j, int yDirection, int xDirection, String player) {
        return countDirection(i, j, yDirection, xDirection, player)
                + countDirection(i, j, -yDirection, -xDirection, player) == 4;
    }
    
    private int countDirection(int i, int j, int yDirection, int xDirection, String player) {
        i += yDirection;
        j += xDirection;
        if (i < 0 || i >= this.row || j < 0 || j >= this.column) {
            return 0;
        } 
        
        if (buttons[i][j].getText().equals(player)) {
            return 1 + countDirection(i, j, yDirection, xDirection, player);
        }
        return 0;
    }
    
    private void deletePiece(int i, int j) {
        buttons[i][j].setText(" ");
        isAvailable[i][j] = true;
        undoButton.setEnabled(false);
    }
    
    private void drawAiMove() {
        
    }
   

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameButton) {
            this.dispose();
            MyWindow mw = new MyWindow(this.text);
        } else if (e.getSource() == undoButton) {
            deletePiece(cursor.getY(), cursor.getX());
            numOfButtons++;
            player = changePlayer(player);
        }
        else {
            String command = e.getActionCommand();
            int i = Integer.parseInt(command.split(",")[0]);
            int j = Integer.parseInt(command.split(",")[1]);

            if (isAvailable[i][j]) {
                drawPiece(i, j);
                numOfButtons--;
                cursor.setXY(j, i);
                undoButton.setEnabled(true);
                player = changePlayer(player);
            }
            
            if (checkWin(i, j)) {
                for (i = 0; i < row; i++) {
                    for (j = 0; j < column; j++) {
                        buttons[i][j].setEnabled(false);
                    }   
                }
                
                if (playerGame.equals(player)) {
                    playerTwoScore++;
                    label3.setText("Player 2: " + playerTwoScore);
                    label4.setText("PLAYER 2 WINS");
                } else {
                    playerOneScore++;
                    label2.setText("Player 1: " + playerOneScore);
                    label4.setText("PLAYER 1 WINS");
                }
                
                runTimer = false;
                undoButton.setEnabled(false);
                gameNumber++;
            }
            
            if (numOfButtons == 0) {
                playerOneScore += 1;
                playerTwoScore += 1;
                label2.setText("Player 1: " + playerOneScore);
                label3.setText("Player 2: " + playerTwoScore);
                label4.setText("DRAW");
                
                runTimer = false;
                undoButton.setEnabled(false);
                gameNumber++;
            }
            
            isAvailable[i][j] = false;
        }
    }
}
