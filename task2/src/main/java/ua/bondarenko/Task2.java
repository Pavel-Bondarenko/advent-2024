package ua.bondarenko;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Task2 {
    private static final String FILE_NAME = "task2.txt";
    private static final String SEPARATOR = " ";
    private static final List<Integer> ALLOWED_DIFFERENCES = List.of(1, 2, 3);

    public static void main(String[] args) {
        new Task2().processTask();
    }

    public void processTask() {
        try {
            List<String> reports = readData();

            //part 1
            System.out.println(calculateNumberOfSaveReports(reports));

            //part 2
            System.out.println(calculateNumberOfSaveReportsWithRemoving(reports));
        } catch (Exception e) {
            System.out.println("Something wrong - " + e.getMessage());
        }
    }

    private List<String> readData() throws IOException, URISyntaxException {
        URI resource = Thread
                .currentThread()
                .getContextClassLoader()
                .getResource(FILE_NAME)
                .toURI();

        return Files.readAllLines(Path.of(resource));
    }

    private boolean isReportSave(List<Integer> levels) {
        AtomicInteger previous = new AtomicInteger(levels.get(0));
        boolean isIncreasing = levels.get(1) > levels.get(0);
        return levels
                .stream()
                .skip(1)
                .allMatch(l -> ALLOWED_DIFFERENCES.contains(Math.abs(l - previous.get()))
                        && ((l > previous.getAndSet(l)) == isIncreasing)
                );
    }

    private boolean isReportSaveWithRemove(List<Integer> levels) {
        return IntStream
                .range(0, levels.size())
                .anyMatch(i -> {
                    List<Integer> list = new ArrayList<>(levels);
                    list.remove(i);
                    return isReportSave(list);
                });
    }

    private List<Integer> parseLevels(String report) {
        return Arrays
                .stream(report.split(SEPARATOR))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    private long calculateNumberOfSaveReports(List<String> reports) {
        return reports
                .stream()
                .map(this::parseLevels)
                .filter(this::isReportSave)
                .count();
    }

    private long calculateNumberOfSaveReportsWithRemoving(List<String> reports) {
        return reports
                .stream()
                .map(this::parseLevels)
                .filter(this::isReportSaveWithRemove)
                .count();
    }
}