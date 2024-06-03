package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class MinesweeperClient extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private JButton[][] buttons;
    

    public MinesweeperClient(int rows, int cols) {
        setTitle("Minesweeper Client");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buttons = new JButton[rows][cols];
        setLayout(new GridLayout(rows, cols));

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton button = new JButton();
                buttons[r][c] = button;
                add(button);
                int finalR = r;
                int finalC = c;
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            revealCell(finalR, finalC);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            flagCell(finalR, finalC);
                        }
                    }
                });
            }
        }

        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(new ServerListener()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void revealCell(int row, int col) {
        out.println("REVEAL " + row + " " + col);
    }

    private void flagCell(int row, int col) {
        out.println("FLAG " + row + " " + col);
    }
    
    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    String[] parts = response.split(" ");
                    String status = parts[0];
                    if (status.equals("SAFE")) {
                        int row = Integer.parseInt(parts[1]);
                        int col = Integer.parseInt(parts[2]);
                        int value = Integer.parseInt(parts[3]);
                        SwingUtilities.invokeLater(() -> {
                            if (buttons[row][col].isEnabled() && buttons[row][col].getText().isEmpty()) {
                                buttons[row][col].setText(String.valueOf(value));
                                buttons[row][col].setEnabled(false);
                            }
                        });
                    } else if (status.equals("MINE_HIT")) {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(MinesweeperClient.this, "Game Over!");
                            dispose();
                        });
                    } else if (status.equals("FLAGGED")) {
                        int row = Integer.parseInt(parts[1]);
                        int col = Integer.parseInt(parts[2]);
                        SwingUtilities.invokeLater(() -> {
                            if (buttons[row][col].isEnabled() && buttons[row][col].getText().isEmpty()) {
                                buttons[row][col].setText("F");
                            }
                        });
                    } else if (status.equals("WON")) {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(MinesweeperClient.this, "You Won!");
                            dispose();
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MinesweeperClient client = new MinesweeperClient(10, 10);
        client.setVisible(true);
    }
}
