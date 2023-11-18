//My main class
package battleship;

import java.util.Scanner;

public class Main {
    static char[][] playBoard = new char[10][10];
    Scanner scanner = new Scanner(System.in);

    final static char Fog = '~';

    public static void main(String[] args) {
        Main game = new Main();
        game.play();

    }
    public void play() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        player1.placeShips();
        player1.switchTurn();
        player2.placeShips();
        player2.switchTurn();

        System.out.println("\nThe game starts!\n");

        Player player = player1;
        Player opponent = player2;
        while (opponent.theEndOfTheGame()) {
            player.takeTurn(opponent);
            player.switchTurn();

            player = opponent;
            opponent = player == player1 ? player2 : player1;
        }
        System.out.println(opponent.playerName + " wins!");


    }
    public Main() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playBoard[i][j] = Fog;
            }
        }
    }

}


