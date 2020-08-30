package TicTacToe;

public class TicTacToeBoard {
    /** Player values, each board position should either be 0 (no player), 1 (player X), 2 (player O)
      Board begins with all 0's and then will write in 1's and 2's as ghe game goes.
      Use TicTacToeBoard.X to get player numbers (don't just write 1) so the code is more readable */
    public static final int X = 1;
    public static final int O = 2;

    // used for checking winning directions: ie. horizontal, vertical, and diagonal
    public static final Pos[] dirs = {new Pos(1, 0), new Pos(1, 1), new Pos(0, 1)};

    private int width, height; //the board will be width x height in dimensions
    private int n; //to win you must get 'n' symbols in a row/column/diagonal
    private int[][] board; //holds the contents of the board
    private int num_pieces;
    private boolean win;
    private int winner;
    private Pos[] winningEndpoints = new Pos[2];

    public boolean isWin() {
        return win;
    }

    public Pos[] getWinningEndpoints() {
        return winningEndpoints;
    }

    public int getWinner() {
        return winner;
    }

    /** checks if there is room for any more moves (needed to detect ties) */
    public boolean isFilled() {
        return width * height == num_pieces;
    }

    /** The Classic Tic Tac Toe board:
     *  *-----------*
     * |2| -  -  -  |
     * |1| X  -  -  |
     * |0| -  -  -  |
     *  *--0--1--2--*   X is at (x = 0, y = 1)
     *  you can specify any dimensions and "n" which is the number of X's or O's in a row needed in order to win
     *  width & height specify the dimensions of the board
     *  Players will alternate turn, but the move order will be enforced by TicTacToeGame.java
     *
     *  the main method to interact with this object is makeMove(int x, int y, int player) which places a "player" piece
     *  at the specified (x,y). This also checks if that player wins and will update variables accordingly if so.
     */
    public TicTacToeBoard(int width, int height, int n) {
        this.width = width;
        this.height = height;
        this.n = n;
        this.board = new int[height][width];
        this.win = false;
        this.winner = 0;
        this.num_pieces = 0;
    }

    /** Get Methods:
     *   returns which player is at the given x and y positions,
     *       if neither player X or O has played at x,y then the value 0 will be returned
     * */
    int get(int x, int y){
        return get(new Pos(x, y));
    }

    int get(Pos pos){
        return this.board[pos.y][pos.x];
    }

    /** Set Methods:
     *   directly changes the player stored at a given x,y position on the board
     *   NOTE: this does NOT change any win values (ie. doesn't update win states)
     * */
    void set(int x, int y, int player){
        set(new Pos(x, y), player);
    }

    void set(Pos pos, int player) {
        this.board[pos.y][pos.x] = player;
    }

    /** valid position: checks the if the following is satisfied
     *   1. is a valid place on the board ie. coordinates are within bounds
     * */
    boolean validPos(Pos pos) {
        boolean validPosition = 0 <= pos.x && pos.x < width && 0 <= pos.y && pos.y < height;
        return validPosition; // also needs space unoccupied
    }

    /** valid move: checks the if the following is satisfied
     *   1. is a valid place on the board ie. coordinates are within bounds
     *   2. isn't already occupied by any player
     * */
    boolean validMove(Pos pos) {
        return validPos(pos) && get(pos) == 0; // also needs space unoccupied
    }

    // check if the latest move (given by "currentPosition") causes a win || assumes win == false
    boolean checkWin(Pos currentPosition) {
        /* TODO: something is up with this function!
                    The "TODOs" in this function give hints about how to debug this function
                           The "TODOs" DON't mean you are supposed to fix/edit the line they are on, just that you are
                           supposed to inspect the code on that line & read the hint
                    Try read & answering the following TODOs in order ie. [1.] then [2.] then [3.]s... etc.
                    You can fix the test by editing just 1 line! (there are multiple ways of doing this too tho)
                    Design Doc: https://docs.google.com/document/d/17cDhZXbFLUugbCaNCjQap4PMKvjLDWDMUUIfgKW-Aqs/edit?usp=sharing
        */


        // get player to check win for
        int player = get(currentPosition);

        // check that the player is either player X or player O
        if (player != TicTacToeBoard.X && player != TicTacToeBoard.O) {
            return false;
        }
        for (Pos dir: dirs) { // TODO: [3.] what is dir and what is dirs?
            // check moves in positive direction
            int streakCount = 0; // TODO: [3.] what is streakCount? What is it used for? When is it updated?
            Pos pointer1 = currentPosition.plus(dir);
            while (validPos(pointer1) && get(pointer1) == player) {
                streakCount += 1;
                pointer1 = pointer1.plus(dir);
            }
            // check moves in negative direction
            Pos pointer2 = currentPosition.plus(dir.times(-1));
            while (validPos(pointer2) && get(pointer2) == player) { /* TODO: [3.] why are there 2 while loops?
                                                                        note: if this is hard to answer see Design Doc*/
                streakCount += 1;
                pointer2 = pointer2.plus(dir.times(-1));
            }
            // check if we have enough of a win streak
            if (streakCount >= this.n) { /* TODO: [4.] currently what setups for the Board will lead to a win?
                                                      why is that the case & how do we fix that?*/
                this.win = true; /* TODO: [2.] our code is failing to set win to true when it should:
                                           what condition must be met for us to set this.win=true?*/
                this.winner = player;
                this.winningEndpoints = new Pos[]{pointer1.plus(dir.times(-1)), pointer2.plus(dir)};
                return win;
            }
        }
        return win; // TODO: [1.] supposed to return True, meaning win should be true at some point
    }

    /** see other "makeMove" */
    public boolean makeMove(int x, int y, int player) {
        return makeMove(new Pos(x, y), player);
    }

    /** makes a move for a given player (player is either TicTacToe.Model.X or TicTacToe.Model.O)
    * returns whether or not that player won */
    public boolean makeMove(Pos move, int player) {
        // check valid move
        if (!validMove(move)) {
            throw new RuntimeException("move is invalid");
        }

        // check valid player
        if (player != TicTacToeBoard.X && player != TicTacToeBoard.O) {
            throw new RuntimeException("player is invalid");
        }

        // make move
        set(move, player);

        if (!win) {
            // check for win
            checkWin(move);
        }

        num_pieces += 1;

        return win;
    }

    /** -----------------------------------      "GUI" Function       --------------------------------------------------
     * prints the winner information*/
    public String displayWinString() {
        if (!win) {
            if (isFilled()) {
                return "it's a Tie!";
            }
            return "ERROR: there is no winner atm";
        }
        String winString = "Player";
        if (winner == TicTacToeBoard.O) {
            winString += " O";
        } else {
            winString += " X";
        }
        winString += " has won!\nThe winning line is from ";
        winString += "(" + winningEndpoints[0].x + ", " + winningEndpoints[0].y + ") to ";
        winString += "(" + winningEndpoints[1].x + ", " + winningEndpoints[1].y + ")";

        return winString;
    }

    /** -----------------------------------      "GUI" Function       --------------------------------------------------
     * THIS IS A HELPER FUNCTION, usually use System.out.println(board.displayBoardString()) to print the board
     * Converts the board into a long string. This returns a list of Strings, each string corresponds to one row of the
     * board*/
    public String[] displayBoardStringList(boolean showNum) {
        // init retlist
        String[] retList = new String[height + 2];

        //construct top and bottom border
        String numBorder = " *-";
        String border = " *-";
        for (int i = 0; i < width; i ++) {
            numBorder += "-" + i + "-";
            border += "---";
        }
        border += "-*";
        numBorder += "-*";
        retList[0] = border;
        retList[height + 1] = showNum ? numBorder : border;

        // visualize board
        for (int y=height - 1; y >= 0; y--) {
            String boardString = showNum ? "|" + y + "|" : " | ";
            for (int x=0; x < width; x++) {
                int val = get(x, y);
                if (val == TicTacToeBoard.X) {
                    boardString += " X ";
                } else if (val == TicTacToeBoard.O) {
                    boardString += " O ";
                } else {
                    boardString += " - ";
                }
            }
//            boardString += " | \n";
            retList[height - y] = boardString + " |";
        }
        return retList;
    }

    /** -----------------------------------      "GUI" Function       --------------------------------------------------
     * Converts the board into a long string.
     * use System.out.println(board.displayBoardString()) to print the board*/
    public String displayBoardString() {
        String displayString = "";
        String[] listStrings = displayBoardStringList(true);
        for (int i = 0; i < listStrings.length; i++) {
            displayString += listStrings[i] + (i == listStrings.length - 1 ? "" : "\n");
        }
        return displayString;
    }

    public static void main(String[] args) {
        TicTacToeBoard m = new TicTacToeBoard(3, 3, 3);
        System.out.println(m.displayBoardString());
//        m.set(0, 0, TicTacToeBoard.X);
//        m.set(1, 0, TicTacToeBoard.X);
//        m.set(2, 0, TicTacToeBoard.X);
//        m.set(3, 0, TicTacToeBoard.X);
//        m.set(4, 0, TicTacToeBoard.X);
//        System.out.println(m.checkWin(new Pos(4,0)));

        System.out.println(m.displayBoardString());
        System.out.println(m.displayWinString());
    }
}
