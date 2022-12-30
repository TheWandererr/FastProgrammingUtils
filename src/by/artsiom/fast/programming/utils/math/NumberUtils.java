package by.artsiom.fast.programming.utils.math;

import static by.artsiom.fast.programming.utils.random.Randomizer.randomInt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Artyom Konashchenko
 * @since 05.04.2020
 */
public class NumberUtils {

  private NumberUtils() throws IllegalAccessException {
    throw new IllegalAccessException("This is utility class that cannot be initialized");
  }

  public static Triplet<BigInteger> gcdWide(BigInteger a, BigInteger b) {
    Triplet<BigInteger> triplet = new Triplet<>(a, BigInteger.ONE, BigInteger.ZERO);
    Triplet<BigInteger> triplet2;

    if (b.equals(BigInteger.ZERO)) {
      return triplet;
    }

    triplet2 = gcdWide(b, a.mod(b));

    triplet = new Triplet<>();
    triplet.setD(triplet2.getD());
    triplet.setX(triplet2.getY());
    triplet.setY(triplet2.getX().subtract(a.divide(b).multiply(triplet2.getY())));

    return triplet;
  }

  public static <T extends Number> boolean lessOrEqualsThen(T left, T right) throws IllegalArgumentException {
    Class<?> clazz = left.getClass();
    if (clazz == Integer.class) {
      return left.intValue() <= right.intValue();
    } else if (clazz == Double.class) {
      return left.doubleValue() <= right.doubleValue();
    } else if (clazz == Float.class) {
      return left.floatValue() <= right.floatValue();
    } else if (clazz == BigInteger.class) {
      return ((BigInteger) left).compareTo((BigInteger) right) <= 0;
    } else if (clazz == BigDecimal.class) {
      return ((BigDecimal) left).compareTo((BigDecimal) right) <= 0;
    } else if (clazz == Byte.class) {
      return left.byteValue() <= right.byteValue();
    } else if (clazz == Short.class) {
      return left.shortValue() <= right.shortValue();
    } else {
      throw new IllegalArgumentException("Incompatible types to compare");
    }
  }

  public static <T extends Number> boolean lessThen(T left, T right) throws IllegalArgumentException {
    Class<?> clazz = left.getClass();
    if (clazz == Integer.class) {
      return left.intValue() < right.intValue();
    } else if (clazz == Double.class) {
      return left.doubleValue() < right.doubleValue();
    } else if (clazz == Float.class) {
      return left.floatValue() < right.floatValue();
    } else if (clazz == BigInteger.class) {
      return ((BigInteger) left).compareTo((BigInteger) right) < 0;
    } else if (clazz == BigDecimal.class) {
      return ((BigDecimal) left).compareTo((BigDecimal) right) < 0;
    } else if (clazz == Byte.class) {
      return left.byteValue() < right.byteValue();
    } else if (clazz == Short.class) {
      return left.shortValue() < right.shortValue();
    } else {
      throw new IllegalArgumentException("Incompatible types to compare");
    }
  }

  public static <T extends Number> boolean equals(T left, T right) throws IllegalArgumentException {
    Class<?> clazz = left.getClass();
    if (clazz == Integer.class) {
      return left.intValue() == right.intValue();
    } else if (clazz == Double.class) {
      return left.doubleValue() == right.doubleValue();
    } else if (clazz == Float.class) {
      return left.floatValue() == right.floatValue();
    } else if (clazz == BigInteger.class) {
      return ((BigInteger) left).compareTo((BigInteger) right) == 0;
    } else if (clazz == BigDecimal.class) {
      return ((BigDecimal) left).compareTo((BigDecimal) right) == 0;
    } else if (clazz == Byte.class) {
      return left.byteValue() == right.byteValue();
    } else if (clazz == Short.class) {
      return left.shortValue() == right.shortValue();
    } else {
      throw new IllegalArgumentException("Incompatible types to compare");
    }
  }

  public static boolean isPrime(int number) {
    return isPrime(BigInteger.valueOf(number));
  }

  public static boolean isPrime(long number) {
    return isPrime(BigInteger.valueOf(number));
  }

  public static boolean isPrime(BigInteger number) {
    return number.isProbablePrime(1);
  }

  public static BigInteger modPow(BigInteger num, BigInteger degree, BigInteger module) {
    BigInteger rez = BigInteger.ONE;
    BigInteger two = BigInteger.ONE.add(BigInteger.ONE);
    while (!equals(degree, BigInteger.ZERO)) {
      if (!equals(degree.mod(two), BigInteger.ZERO)) {
        rez = (rez.multiply(num)).mod(module);
        degree = degree.subtract(BigInteger.ONE);
      }
      degree = degree.divide(two);
      num = (num.multiply(num)).mod(module);
    }
    return rez;
  }

  public static BigInteger binPow(BigInteger num, BigInteger degree) {
    BigInteger res = BigInteger.ONE;
    while (!equals(degree, BigInteger.ZERO)) {
      if (!equals(degree.and(BigInteger.ONE), BigInteger.ZERO)) {
        res = res.multiply(num);
      }
      num = num.multiply(num);
      degree = degree.shiftRight(1);
    }
    return res;
  }

  public static int getEvenLessThen(int border) {
    int value = randomInt(border);
    return value % 2 == 0 ? value : value - 1;
  }

  public static long getEvenLessThen(long border) {
    long val = ThreadLocalRandom.current().nextLong(border);
    return val % 2L == 0 ? val : val - 1L;
  }

  public static BigInteger getEvenLessThen(BigInteger border) {
    BigInteger val = getProbablyRandInRange(BigInteger.ZERO, border);
    BigInteger two = BigInteger.ONE.add(BigInteger.ONE);
    BigInteger residue = val.mod(two);
    return equals(residue, BigInteger.ZERO) ? val : val.subtract(BigInteger.ONE);
  }

  public static BigInteger getProbablyRandInRange(BigInteger left, BigInteger right) throws ArithmeticException {
    BigInteger value = right.subtract(left);
    int len = right.bitLength();
    BigInteger res = new BigInteger(len, new Random());
    if (lessThen(res, left)) {
      res = res.add(left);
    }
    if (lessOrEqualsThen(value, res)) {
      res = res.mod(value).add(left);
    }
    return res;
  }

  public static <T extends Number> boolean inRange(T val, T left, T right) {
    return lessOrEqualsThen(val, right) && lessOrEqualsThen(left, val);
  }

  public static class Triplet<T extends Number> {

    private T d;
    private T x;
    private T y;

    Triplet(T one, T two, T three) {
      d = one;
      x = two;
      y = three;
    }

    public Triplet() {
    }

    public T getD() {
      return d;
    }

    public T getX() {
      return x;
    }

    public T getY() {
      return y;
    }

    public void setD(T d) {
      this.d = d;
    }

    public void setX(T x) {
      this.x = x;
    }

    public void setY(T y) {
      this.y = y;
    }
  }
}
