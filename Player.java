package battleship;

import java.util.Scanner;

class Player {
    static Scanner scanner = new Scanner(System.in);
    char[][] board;
    final String playerName;
    final static char Fog = '~';
    final static char SHIP = 'O';
    final static char MISSED = 'M';
    final static char HIT = 'X';


    public Player(String playerName) {
        this.board = new char[10][10];
        this.playerName = playerName;

        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.board[i][j] = Fog;
            }
        }
    }

    public void placeShips() {
        // Display game board before placing ships
        System.out.println(playerName + ", place your ships on the game field\n");
        printEmptyBattleField(); // or maybe printBoard("hide");
        for (Ship s : Ship.values()) {
            System.out.printf("\n Enter the coordinates of the %s (%d cells):\n", s.getName(), s.getSize());
            while (true) {
                String input = scanner.nextLine();
                Scanner lineScanner = new Scanner(input);
                String first = lineScanner.next();
                String second = lineScanner.next();

                char firstLetter = first.charAt(0);
                char secondLetter = second.charAt(0);

                int firstNumber = Integer.parseInt(first.substring(1));
                int secondNumber = Integer.parseInt(second.substring(1));

                if (firstLetter < 'A' || firstLetter > 'J' || secondLetter < 'A' || secondLetter > 'J' ||
                        firstNumber < 1 || firstNumber > 10 || secondNumber < 1 || secondNumber > 10) {
                    System.out.println("Error! Coordinates should be between A - J and 1 - 10! Try again:");
                } else if ((firstLetter == secondLetter) == (firstNumber == secondNumber)) {
                    System.out.println("Error! Wrong ship location! Try again:");
                } else if ((Math.abs(firstNumber - secondNumber) + 1 != s.getSize()) && (Math.abs(firstLetter - secondLetter) + 1 != s.getSize())) {
                    System.out.printf("Error! Wrong length of the %s! Try again:\n", s.getName());
                } else if (!isValidPlacement(firstLetter, secondLetter, firstNumber, secondNumber)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                } else {
                    placeShips1(firstLetter, secondLetter, firstNumber, secondNumber);
                    break;

                }
            }
            printBoard("");
        }
    }

    private boolean isValidPlacement(char firstLetter, char secondLetter, int firstNumber, int secondNumber) {
        int rowStart = Math.min(firstLetter - 'A', secondLetter - 'A');
        int rowEnd = Math.max(firstLetter - 'A', secondLetter - 'A');
        int columnStart = Math.min(firstNumber - 1, secondNumber - 1);
        int columnEnd = Math.max(firstNumber - 1, secondNumber - 1);

        if ((rowStart > 0 && columnStart > 0) && (rowEnd < 9)) {
            rowStart -= 1;
            rowEnd += 1;
            columnStart -= 1;
            columnEnd += 1;
        } else if (rowStart == 0 && columnStart == 0) {
            rowEnd += 1;
            columnEnd += 1;
        } else if (rowEnd == 9 && columnStart == 0) {
            rowStart -= 1;
            columnEnd += 1;
        } else if (rowStart == 0 && columnEnd == 9) {
            rowEnd += 1;
            columnStart -= 1;
        } else if (rowEnd == 9 && columnEnd == 9) {
            rowStart -= 1;
            columnStart -= 1;
        }

        for (int i = rowStart; i <= rowEnd; i++) {
            for (int j = columnStart; j <= columnEnd; j++) {
                if (board[i][j] != Fog) {
                    return false;
                }
            }
        }
        return true;
    }

    private void placeShips1(char firstLetter, char secondLetter, int firstNumber, int secondNumber) {
        if (firstLetter == secondLetter) {
            int row = firstLetter - 'A';
            int colStart = Math.min(firstNumber - 1, secondNumber - 1);
            int colEnd = Math.max(firstNumber - 1, secondNumber - 1);
            for (int i = colStart; i <= colEnd; i++) {
                board[row][i] = SHIP;
            }
        } else if (firstNumber == secondNumber) {
            int rowstart = Math.min(firstLetter - 'A', secondLetter - 'A');
            int rowend = Math.max(firstLetter - 'A', secondLetter - 'A');
            int col = firstNumber - 1;
            for (int i = rowstart; i <= rowend; i++) {
                board[i][col] = SHIP;
            }
        }
    }

    public void printBoard(String state) {
        String letters = "ABCDEFGHIJ"; // Labels for rows

        System.out.println("\n  1 2 3 4 5 6 7 8 9 10"); // Labels for columns
        if (state.equals("hide")) {
            for (int row = 0; row < 10; row++) {
                System.out.print(letters.charAt(row) + " "); // Print the label for the current row
                for (int col = 0; col < 10; col++) {
                    char cell = board[row][col];
                    if (board[row][col] == SHIP) {
                        System.out.print(Fog + " ");
                    } else {
                        System.out.print(cell + " ");
                    }

                }
                System.out.println();
            }

        } else {
            for (int row = 0; row < 10; row++) {
                System.out.print(letters.charAt(row) + " "); // Print the label for the current row
                for (int col = 0; col < 10; col++) {
                    char cell = board[row][col];
                    System.out.print(cell + " "); // Space before the marker
                }
                System.out.println(); // Start a new row
            }
        }

    }

    public void hitsAndMisses(Player opponent) {
        int shotRow;
        int shotColumn;
        while (true) {
            String shotCell = scanner.nextLine();
            shotRow = shotCell.charAt(0) - 'A';
            shotColumn = Integer.parseInt(shotCell.substring(1)) - 1;

            if (shotRow < 0 || shotRow > 9 || shotColumn < 0 || shotColumn > 9) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                continue;
            }
            break;
        }
        if (opponent.board[shotRow][shotColumn] == SHIP) {
            opponent.board[shotRow][shotColumn] = HIT;
            System.out.println("You hit a ship!");
            if (opponent.isShipSunk(shotRow, shotColumn)) {
                System.out.println("You sank a ship! Specify a new target:");
            }
        } else if (opponent.board[shotRow][shotColumn] == Fog) {
            opponent.board[shotRow][shotColumn] = MISSED;
            System.out.println("You missed!");
        } else if (opponent.board[shotRow][shotColumn] == HIT) {
            System.out.println("You hit a ship!");

        }

        if (opponent.theEndOfTheGame()) {
            System.out.println("You sank the last ship. You won. Congratulations!");
        }
    }

    public boolean isShipSunk(int shotRow, int shotColumn) {
        // Check up
        for (int i = shotRow - 1; i >= 0; i--) {
            if (board[i][shotColumn] == SHIP) {
                return false;
            } else if (board[i][shotColumn] == Fog) {
                break;
            }
        }

        // Check down
        for (int i = shotRow + 1; i < 10; i++) {
            if (board[i][shotColumn] == SHIP) {
                return false;
            } else if (board[i][shotColumn] == Fog) {
                break;
            }
        }

        // Check right
        for (int i = shotColumn + 1; i < 10; i++) {
            if (board[shotRow][i] == SHIP) {
                return false;
            } else if (board[shotRow][i] == Fog) {
                break;
            }
        }

        // Check left
        for (int i = shotColumn - 1; i >= 0; i--) {
            if (board[shotRow][i] == SHIP) {
                return false;
            } else if (board[shotRow][i] == Fog) {
                break;
            }
        }
        return true;
    }
    private static void printEmptyBattleField() {
        String letters = "ABCDEFGHIJ";
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int row = 0; row < 10; row++) {
            System.out.print(letters.charAt(row));
            for (int col = 0; col < 10; col++) {
                System.out.print(" ~");
            }
            System.out.println();
        }
    }

    public void takeTurn(Player opponent) {
        opponent.printBoard("hide"); //display opponent's board
        System.out.println("---------------------");
        printBoard("not hide"); //show the current player's board
        System.out.println();
        System.out.println(playerName + ", it's your turn\n");
        System.out.println();
        hitsAndMisses(opponent);

    }

    public void switchTurn() {
        System.out.println("Press Enter and pass the move to another player");
        System.out.println("...");
        scanner.nextLine();

    }

    public boolean theEndOfTheGame() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == SHIP) {
                    return false;
                }
            }
        }
        return true;
    }
}


