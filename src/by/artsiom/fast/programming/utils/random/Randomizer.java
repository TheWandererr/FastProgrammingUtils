package by.artsiom.fast.programming.utils.random;

import java.util.Random;
import java.util.UUID;

/**
 * @author Artyom Konashchenko
 * @since 30.12.2022
 */
public class Randomizer {

  private static final Random random = new Random();

  private Randomizer() throws IllegalAccessException {
    throw new IllegalAccessException("This is utility class that cannot be initialized");
  }

  public static int randomInt(int upperBound) {
    return random.nextInt(upperBound);
  }

  public static String sixDigitInt(int upperBound) {
    return String.format("%06d", randomInt(upperBound));
  }

  public static String uuid() {
    return UUID.randomUUID().toString();
  }
}
