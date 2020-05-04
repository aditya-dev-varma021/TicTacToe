/** FIXME: Loaded from an old attempt to make a Chess game,
 * requires updating and modernization.
 */

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class TTTPanel extends JPanel implements MouseListener {

    Game game;
    MachinePlayer enemy;
    Image X, O;
    public TTTPanel(int w, int h){
        super();
        setSize(w, h);
        game = new Game();
        enemy = new MachinePlayer();
        try {
            X = ImageIO.read(new File("src/letterX.png"));
            O = ImageIO.read(new File("src/letterO.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addMouseListener(this);
    }

    public void paint(Graphics g){
        g.setColor(Color.BLACK);

        g.drawLine(200, 0, 200, 600);
        g.drawLine(400, 0, 400, 600);

        g.drawLine(0, 200, 600, 200);
        g.drawLine(0, 400, 600, 400);
        for (int x = 0; x < 3; x += 1) {
            for (int y = 0; y < 3; y += 1) {
                if (game.board[x][y] == 'X') {
                    g.drawImage(X, x * 200, y * 200, null);
                } else if (game.board[x][y] == 'O') {
                    g.drawImage(O, x * 200, y * 200, null);
                }
            }
        }
        g.setFont(new Font("WinFont", Font.BOLD, 30));
        if (game.isWin() != 'E') {
            g.drawString(game.isWin() + " has won the game!", 100, 35);
        } else if (game.validMoves().isEmpty()) {
            g.drawString("Tie game!", 200, 35);
        }
        repaint();
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() / 200;
        int y = e.getY() / 200;
        if (game.isWin() == 'E') {
            if (game.getTurn() == 'X') {
                game.makeMove(x, y, 'X');
            }
            int[] move = enemy.getMove(game);
            if (move != null) {
                game.makeMove(move[0], move[1], 'O');
                repaint();
            }

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    /** A class to manage game dynamics. **/
    private static class Game {

        /** The X win state. **/
        static final String WINX = "XXX";

        /** The Ow in state. **/
        static final String WINY = "OOO";

        /** Stores the game board state. **/
        char[][] board;

        /** Stores whose turn it is. **/
        char turn;

        /** Makes a game. **/
        Game() {
            board = new char[3][3];
            for (int x = 0; x < 3; x += 1) {
                for (int y = 0; y < 3; y += 1) {
                    board[x][y] = 'E';
                }
            }
            turn = 'X';
        }

        /** Switches turn. **/
        void switchTurn() {
           turn =  (turn == 'X') ? 'O' : 'X';
        }

        /** Gets the turn.
         * @return current turn
         */
        char getTurn() {
            return turn;
        }

        /** Makes a move if valid.
         * @param x x coordinate.
         * @param y y coordinate.
         * @param player the player to make a move for.
         * @return if the move was successful. **/
        boolean makeMove(int x, int y, char player) {
            if (board[x][y] == 'E') {
                board[x][y] = player;
                switchTurn();
                return true;
            }
            return false;
        }

        /** Returns a copy of this game. **/
        Game copy() {
            Game r = new Game();
            for (int x = 0; x < 3; x += 1) {
                for (int y = 0; y < 3; y += 1) {
                    Character c = board[x][y];
                    r.board[x][y] = c;
                }
            }
            r.turn = this.turn;
            return r;
        }

        /** Returns if the board state reflects a win.
         * If so, returns the player who has won;
         * otherwise returns the empty piece.
         * @return 'E', 'X', or 'O'.
         */
        char isWin() {
            String[] states = new String[8];
            String a = "", b = "", c = "",
                    d = "", e = "", f = "",
            g = "", h = "";

            for (int x = 0; x < 3; x += 1) {
                a += board[x][0];
                b += board[x][1];
                c += board[x][2];
                d += board[0][x];
                e += board[1][x];
                f += board[2][x];
                g += board[x][x];
                h += board[2-x][x];
            }
            states[0] = a; states[1] = b;
            states[2] = c; states[3] = d;
            states[4] = e; states[5] = f;
            states[6] = g; states[7]= h;
            for (String perm : states) {
                try {
                    if (perm.equals(WINX)) {
                        return 'X';
                    } else if (perm.equals(WINY)) {
                        return 'O';
                    }
                } catch (NullPointerException n) {
                    n.printStackTrace();
                }
            }
            return 'E';
        }

        /** Returns all valid moves.
         * Returns empty if no valid move
         * is possible.
         * @return all possible moves.
         */
        ArrayList<int[]> validMoves() {
            ArrayList<int[]> ret = new ArrayList<int[]>();
            for (int x = 0; x < 3; x += 1) {
                for (int y = 0; y < 3; y += 1) {
                   if (board[x][y] == 'E') {
                       ret.add(new int[]{x, y});
                   }
                }
            }
            return ret;
        }

    }

    /** A machine player to play against.
     * Alpha-Beta pruning is utilized.  **/
    private static class MachinePlayer {

        /** Move to make. **/
        int[] move;


        /** Makes a new MachinePlayer. **/
        MachinePlayer() {
            move = new int[2];
        }


        /** Return a move after searching the game tree to DEPTH>0 moves
         *  from the current position. Assumes the game is not over. */
        private int[] getMove(Game g) {
            Game lookAhead = g.copy();
            double value;
            move = null;

            if (g.getTurn() == 'O') {
                value = minimax(lookAhead, 9, true, 1, -1, 1);
            } else {
                value = minimax(lookAhead, 9, true, -1, -1, 1);
            }
            return move;
        }

        /** Minimax algorithm implementing alpha-beta pruning for moves. **/
        private int minimax(Game game, int depth, boolean saveMove, int maxP, int alpha, int beta) {
            if (depth == 0 || game.validMoves().isEmpty() || game.isWin() != 'E') {
                return heuristicFunc(game);
            }
            if (maxP == 1) {
                int maxEval = -1; Game temp;
                ArrayList<int[]> legalMoves = game.validMoves();
                for (int[] m : legalMoves) {
                    temp = game.copy();
                    temp.makeMove(m[0], m[1], temp.turn);
                    int eval = minimax(temp, depth - 1, false, -1, alpha, beta);
                    if (saveMove) {
                        if (eval > maxEval) {
                            move = m;
                        }
                    }
                    maxEval = (eval > maxEval) ? eval : maxEval;
                    alpha = (eval > alpha) ? (int) eval : alpha;
                    if (beta <= alpha) {
                        break;
                    }
                }
               return maxEval;
            }
            else {
                int minEval = 1; Game temp;
                ArrayList<int[]> legalMoves = game.validMoves();
                for (int[] m : legalMoves) {
                    temp = game.copy();
                    temp.makeMove(m[0], m[1], temp.turn);
                    int eval = minimax(temp, depth - 1, false, 1, alpha, beta);
                    if (saveMove) {
                        if (eval < minEval) {
                            move = m;
                        }
                    }
                    minEval = (eval < minEval) ? eval : minEval;
                    beta = (eval < beta) ?  eval : beta;
                    if (beta <= alpha) {
                        break;
                    }
                }
                return minEval;
            }
        }


        /** Simple static evaluation function. **/
        private int heuristicFunc(Game g) {
            if (g.isWin() == 'X') {
                return -1;
            } else if (g.isWin() == 'O') {
                return 1;
            } else {
                return 0;
            }
        }




    }
}