import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static char[][] map;
    static final int SIZE = 5;
    static final int DOTS_TO_WIN = 3;

    static final char DOT_EMPTY = '•';
    static final char DOT_X = 'X';
    static final char DOT_O = 'O';

    public static void main(String[] args) {
        initMap();
        printMap();

        while (isEmptyCell()) {
            if (!isWin()) {
                humanTurn();
                aiTurn();
            }

        }
        System.out.println("ставить больше некуда");
    }

    public static void initMap() {
        map = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = DOT_EMPTY;
            }
        }
    }

    public static void printMap() {
        System.out.print(" ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(" " + (i + 1));
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void humanTurn() {
        int x;
        int y;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print("Введите координату по горизонтали: ");
            x = scanner.nextInt();
            System.out.print("Введите коориднату по вертикали: ");
            y = scanner.nextInt();
        } while (!isCellFree(x, y));
        map[x - 1][y - 1] = DOT_X;
        printMap();

    }

    private static void aiTurn() {
        int x;
        int y;
        Random random = new Random();
        do {
            x = random.nextInt(SIZE + 1);
            y = random.nextInt(SIZE + 1);
        }
        while (!isCellFree(x, y) && isEmptyCell());
        map[x - 1][y - 1] = DOT_O;
        printMap();

    }

    private static boolean isCellFree(int x, int y) {
        if (x <= SIZE && x >= 1 && y <= SIZE && y >= 1 && map[x - 1][y - 1] == DOT_EMPTY) { // проверяем попали ли мы в поле
            return true;
        } else {
            // System.out.println("Неверные координаты, либо ячейка уже занята");
            return false;
        }
    }

    private static boolean isEmptyCell() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == DOT_EMPTY) return true;
            }
        }
        return false;
    }

    private static boolean isWin() {
        char[] verticalArray = new char[SIZE]; // создаем временный массив для заполнения вертикалями
        char[] diagonalArray1 = new char[SIZE];
        char[] diagonalArray2 = new char[SIZE];
        for (int i = 0; i < SIZE; i++) { // проверяем горизонтали
            if (!compareArray(map[i])) continue;
            else {
                System.out.println("ПОБЕДА! в строке номер " + (i + 1));
                return true;
            }
        }
        for (int i = 0; i < SIZE; i++) {  // формируем массив вертикалей
            for (int j = 0; j < SIZE; j++) {
                verticalArray[j] = map[j][i]; // записываем значение столбца в массив
            }
            // System.out.println(Arrays.toString(verticalArray));
            if (!compareArray(verticalArray)) continue;
            else {
                System.out.println("ПОБЕДА! в столбце номер " + (i + 1));
                return true;
            }
        }
        int diagSize = SIZE - 1;
        for (int i = 0; i < SIZE; i++) { // заполняем массив диагоналей
            for (int j = 0; j < SIZE; j++) {
                if (i == j) diagonalArray1[i] = map[i][j];
            }
            diagonalArray2[i] = map[diagSize][i];
            diagSize--;
        }
        if (compareArray(diagonalArray1) || compareArray(diagonalArray2)) {
            System.out.println("ПОБЕДА! Диагональ ");
            return true;
        }
        System.out.println(Arrays.toString(diagonalArray1));
        return false;
    }

    private static boolean compareArray(char comparedArray[]) { // метод сравнивает все значения в массиве и возвращает true, елси они одинаковые
        for (int i = 0; i < comparedArray.length - 1; i++) {
            if (comparedArray[i] == comparedArray[i + 1] && comparedArray[i] != DOT_EMPTY) {
                continue;
            } // сравниваем соседние элементы, если совпадают - продолжнаем сравнивать
            else {
                // System.out.println("НЕ СОВПАЛО");
                return false;
            } // если не совпадают - выходим, так как победы нет
        }
        System.out.println("СОВПАЛО");
        return true;
    }
}
