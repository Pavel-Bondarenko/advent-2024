package ua.bondarenko;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Task3 {
    private static final String FILE_NAME = "task3.txt";
    private static final String MUL_REGEX = "mul[(]\\d{1,3},\\d{1,3}[)]";
    private static final Pattern MUL_PATTERN = Pattern.compile(MUL_REGEX);

    private static final String ENABLING_REGEX = "do[(][)]";
    private static final String DISABLING_REGEX = "don't[(][)]";
    private static final Pattern INSTRUCTION_PATTEN =
            Pattern.compile(MUL_REGEX
                    + "|" + DISABLING_REGEX
                    + "|" + ENABLING_REGEX);

    public static void main(String[] args) {
        new Task3().processTask();
    }

    public void processTask() {

        try {
            String data = readData();

            //part 1
            System.out.println(calculateSumOfCorrectMul(data));

            //part2
            System.out.println(calculateSumOfCorrectInstructionsWithEnabling(data));
        } catch (Exception e) {
            System.out.println("Program is crashed - " + e.getMessage());
        }
    }

    private String readData() throws IOException, URISyntaxException {
        URI resource = Thread
                .currentThread()
                .getContextClassLoader()
                .getResource(FILE_NAME)
                .toURI();

        List<String> linesFromFile = Files.readAllLines(Path.of(resource));
        return String.join("", linesFromFile);
    }

    private List<String> parseAndGetCorrectMul(String input) {
        return parseInputByPattern(input, MUL_PATTERN);
    }

    private List<String> parseAndGetCorrectInstructions(String input) {
        return parseInputByPattern(input, INSTRUCTION_PATTEN);
    }

    private List<String> parseInputByPattern(String input, Pattern pattern) {
        Matcher matcher = pattern.matcher(input);
        List<String> correctData = new ArrayList<>();

        while (matcher.find()) {
            correctData.add(input.substring(matcher.start(), matcher.end()));
        }
        return correctData;
    }

    private List<String> findEnabledMul(List<String> instructions) {
        AtomicBoolean isEnabled = new AtomicBoolean(true);
        return instructions
                .stream()
                .filter(i -> {
                    if (i.matches(ENABLING_REGEX)) {
                        isEnabled.set(true);
                        return false;
                    }

                    if (i.matches(DISABLING_REGEX)) {
                        isEnabled.set(false);
                        return false;
                    }

                    return isEnabled.get();
                })
                .collect(Collectors.toList());
    }

    private int calculateMulExpression(String expression) {
        expression = expression
                .replace("mul(", "")
                .replace(")", "");
        String[] values = expression.split(",");
        return Integer.parseInt(values[0]) * Integer.parseInt(values[1]);
    }

    private long calculateSumOfCorrectMul(String data) {
        return calculateSumOfCorrectMul(parseAndGetCorrectMul(data));
    }

    private long calculateSumOfCorrectInstructionsWithEnabling(String data) {
        List<String> enabledMul = findEnabledMul(parseAndGetCorrectInstructions(data));
        return calculateSumOfCorrectMul(enabledMul);
    }

    private long calculateSumOfCorrectMul(List<String> correctMul) {
        return correctMul
                .stream()
                .map(this::calculateMulExpression)
                .reduce(0, Integer::sum);
    }
}