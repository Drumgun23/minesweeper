package minesweeper;

import java.io.*;
import java.net.*;

public class MinesweeperServer {
    private static final int PORT = 12345;
    private MinesweeperGame game;

    public MinesweeperServer(int rows, int cols, int minesCount) {
        game = new MinesweeperGame(rows, cols, minesCount);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started, waiting for clients...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, game).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MinesweeperServer server = new MinesweeperServer(10, 10, 10);
        server.start();
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private MinesweeperGame game;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, MinesweeperGame game) {
        this.clientSocket = socket;
        this.game = game;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] parts = inputLine.split(" ");
                if (parts.length < 3) {
                    // Invalid command format, skip
                    continue;
                }
                String command = parts[0];
                int row = Integer.parseInt(parts[1]);
                int col = Integer.parseInt(parts[2]);

                if (command.equals("REVEAL")) {
                    boolean safe = game.revealCell(row, col);
                    if (safe) {
                        int value = game.getValue(row, col);
                        out.println("SAFE " + row + " " + col + " " + value);
                        if (game.isWon()) {
                            out.println("WON");
                        }
                    } else {
                        out.println("MINE_HIT");
                    }
                } else if (command.equals("FLAG")) {
                    out.println("FLAGGED " + row + " " + col);
                    if (game.isWon()) {
                        out.println("WON");
                    }
                } else {
                    // Unknown command, ignore
                }
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
