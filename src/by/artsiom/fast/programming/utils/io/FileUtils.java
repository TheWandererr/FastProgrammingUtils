package by.artsiom.fast.programming.utils.io;

import static by.artsiom.fast.programming.utils.collections.CollectionUtils.isNotEmpty;
import static by.artsiom.fast.programming.utils.streams.StreamUtils.stream;
import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Artyom Konashchenko
 * @since 05.04.2020
 */
public class FileUtils {

  private FileUtils() throws IllegalAccessException {
    throw new IllegalAccessException("This is utility class that cannot be initialized");
  }

  public static <T extends Number> List<T> readEachLineAsNumber(String path, Function<String, T> constructor) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      return br.lines().map(constructor)
               .collect(toList());
    }
  }

  public static <T extends Number> List<List<T>> readEachLineAsList(String path, String wordDelimiter, Function<String, T> constructor)
      throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      return br.lines()
               .map(line -> stream(line.split(wordDelimiter)).map(constructor).collect(toList()))
               .collect(toList());
    }
  }

  public static List<List<String>> readEachLineAsList(String path, String wordDelimiter) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      return br.lines()
               .map(line -> stream(line.split(wordDelimiter)).collect(toList()))
               .collect(toList());
    }
  }

  public static List<String> readEachLineAsString(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      return br.lines().collect(toList());
    }
  }

  public static void writeLine(String str, String path, boolean append) throws IOException {
    if (!str.isBlank()) {
      try (BufferedWriter br = new BufferedWriter(new FileWriter(path, append))) {
        br.write(str);
        br.newLine();
      }
    }
  }

  public static <T extends CharSequence> void writeAsLineJoining(List<T> words, CharSequence joiner, String path, boolean append)
      throws IOException {
    if (isNotEmpty(words)) {
      String line = String.join(joiner, words);
      writeLine(line, path, append);
    }
  }

  public static <T extends Number> void writeNumbersInline(List<T> numbers, CharSequence joiner, String path, boolean append)
      throws IOException {
    if (isNotEmpty(numbers)) {
      String line = numbers.stream().map(Number::toString).collect(Collectors.joining(joiner));
      writeLine(line, path, append);
    }
  }

  public static <T extends CharSequence> void writeLinesAndJoining(List<List<T>> listOfLines, CharSequence joiner, String path,
                                                                   boolean append) throws IOException {
    if (isNotEmpty(listOfLines)) {
      try (BufferedWriter br = new BufferedWriter(new FileWriter(path, append))) {
        List<String> lines = listOfLines.stream()
                                        .map(innerList -> String.join(joiner, innerList))
                                        .toList();
        for (String line : lines) {
          br.write(line);
          br.newLine();
        }
      }
    }
  }
}
