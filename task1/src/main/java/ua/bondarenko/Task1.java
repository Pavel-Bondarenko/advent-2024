package ua.bondarenko;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class Task1 {
    private static final String FILE_NAME = "task1.txt";
    private static final String SEPARATOR = "   ";

    private static List<Integer> leftColumn;
    private static List<Integer> rightColumn;

    public static void main(String[] args) {
        new Task1().processTask();
    }

    public void processTask() {

        try {
            readData();
            sortData();

            //part 1
            System.out.println(calculateTotalDistance());

            //part2
            System.out.println(calculateTotalIncluding());
        } catch (IOException e) {
            System.out.println("Some troubles with file - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Program is crashed - " + e.getMessage());
        }
    }

    private void readData() throws IOException, URISyntaxException {
        leftColumn = new ArrayList<>();
        rightColumn = new ArrayList<>();

        URI resource = Thread
                .currentThread()
                .getContextClassLoader()
                .getResource(FILE_NAME)
                .toURI();

        List<String> linesFromFile = Files.readAllLines(Path.of(resource));
        linesFromFile.forEach(l -> {
            String[] values = l.split(SEPARATOR);
            leftColumn.add(Integer.valueOf(values[0]));
            rightColumn.add(Integer.valueOf(values[1]));
        });
    }

    private void sortData() {
        leftColumn.sort(Integer::compare);
        rightColumn.sort(Integer::compare);
    }

    private int calculateTotalDistance() {
        return IntStream
                .range(0, leftColumn.size())
                .map(i -> Math.abs(leftColumn.get(i) - rightColumn.get(i)))
                .sum();
    }

    private long calculateTotalIncluding() {
        return leftColumn
                .stream()
                .map(v -> rightColumn.stream().filter(n -> Objects.equals(n, v)).count() * v)
                .reduce(0L, Long::sum);
    }
}