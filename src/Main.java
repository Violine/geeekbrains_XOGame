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
        do {
            if (isEmptyCell()) {
                humanTurn();
                aiTurn();
            } else {
                System.out.println("НИЧЬЯ");
                break;
            }
        } while (!isWin());

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

    public static void aiTurn() {
        int x;
        int y;
        int coord;
        int[] aiArray;
        Random random = new Random();
        if (isEmptyCell()) {
            do {
                aiArray = defendCell();
                coord = getMaxElementIndex(aiArray);
                System.out.println(coord);
                if (coord < SIZE){
                    x = coord;
                    y = random.nextInt(SIZE + 1);
                }else if (coord >= SIZE || coord < SIZE*2){
                    y = coord;
                    x = random.nextInt(SIZE + 1);
                }
            }
            while (!isCellFree(x, y));
            map[x - 1][y - 1] = DOT_O;
            printMap();
        } else System.out.println("Поле заполнено!");
    }

    public static int getMaxElementIndex(int[] testedArray) {
        int maxElement = testedArray[0];
        int maxElementIndex = 0;
        for (int i = 0; i < testedArray.length; i++) {
            if (maxElement < testedArray[i]) {
                maxElement = testedArray[i];
                maxElementIndex = i;
            }
        }
        return maxElementIndex;
    }


    public static boolean isCellFree(int x, int y) {
        if (x <= SIZE && x >= 1 && y <= SIZE && y >= 1 && map[x - 1][y - 1] == DOT_EMPTY) { // проверяем попали ли мы в поле
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmptyCell() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == DOT_EMPTY) return true;
            }
        }
        return false;
    }

    public static boolean isWin() {
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
        return false;
    }

    public static int[] defendCell() {
        char[] vertical = new char[SIZE];
        char[] diagonal1 = new char[SIZE];
        char[] diagonal2 = new char[SIZE];
        int[] calculatedArray = new int[SIZE * 2 + 2]; // в массив будем записывать количество "вражеских" символов
        int calculatedArrayCounter = 0;
        for (int i = 0; i < SIZE; i++) {
            calculatedArray[calculatedArrayCounter] = calculateEnemyDot(map[i], DOT_X);
            calculatedArrayCounter++;
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                vertical[j] = map[j][i];
            }
            calculatedArray[calculatedArrayCounter] = calculateEnemyDot(vertical, DOT_X);
            calculatedArrayCounter++;
        }
        int diagSize = SIZE - 1;
        for (int i = 0; i < SIZE; i++) { // заполняем массив диагоналей
            for (int j = 0; j < SIZE; j++) {
                if (i == j) diagonal1[i] = map[i][j];
            }
            diagonal2[i] = map[diagSize][i];
            diagSize--;
        }
        calculatedArray[calculatedArrayCounter] = calculateEnemyDot(diagonal1, DOT_X);
        calculatedArrayCounter++;
        calculatedArray[calculatedArrayCounter] = calculateEnemyDot(diagonal2, DOT_X);
        // System.out.println(Arrays.toString(calculatedArray));
        return calculatedArray;
    }

    public static int calculateEnemyDot(char[] calculatedArray, char enemyDot) {
        int enemyDotCounter = 0;
        for (int i = 0; i < calculatedArray.length; i++) {
            if (calculatedArray[i] == enemyDot) enemyDotCounter++;
        }
        return enemyDotCounter;

    }

    public static boolean compareArray(char comparedArray[]) { // метод сравнивает все значения в массиве и возвращает true, елси они одинаковые
        for (int i = 0; i < comparedArray.length - 1; i++) {
            if (comparedArray[i] == comparedArray[i + 1] && comparedArray[i] != DOT_EMPTY) {
                continue;
            } // сравниваем соседние элементы, если совпадают - продолжнаем сравнивать
            else {
                // System.out.println("НЕ СОВПАЛО");
                return false;
            } // если не совпадают - выходим, так как победы нет
        }
        return true;
    }
}
