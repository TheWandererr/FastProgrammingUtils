package by.artsiom.fast.programming.utils.math;

import static by.artsiom.fast.programming.utils.arrays.ArrayUtils.copy;
import static by.artsiom.fast.programming.utils.arrays.ArrayUtils.isEmpty;
import static java.math.BigInteger.valueOf;
import static java.util.Objects.isNull;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Artyom Konashchenko
 * @since 05.04.2020
 */
public class MatrixUtils {

  private MatrixUtils() throws IllegalAccessException {
    throw new IllegalAccessException("This is utility class that cannot be initialized");
  }

  @SuppressWarnings("unchecked")
  public static <T> T[][] minor(T[][] matrix, Class<T> of, int rowNumber, int columnNumber) {

    if (isEmpty(matrix)) {
      throw new IllegalArgumentException("Error getting minor from null or empty matrix");
    }

    final int rows = matrix.length;
    final int columns = matrix[0].length;
    if (rowNumber >= rows || columnNumber >= columns) {
      throw new IllegalArgumentException("Error getting minor. Rows or columns out of range");
    }

    final int finalRows = rows - 1;
    final int finalColumns = columns - 1;
    T[][] result = (T[][]) Array.newInstance(of, finalRows, finalColumns);

    for (int i = 0; i < matrix.length; ++i) {
      boolean isRowDeleted = rowNumber < i;
      int resultRowIndex = isRowDeleted ? i - 1 : i;

      for (int j = 0; j < matrix[i].length; ++j) {
        boolean isColDeleted = columnNumber < j;
        int resultColIndex = isColDeleted ? j - 1 : j;

        if (rowNumber != i && columnNumber != j) {
          result[resultRowIndex][resultColIndex] = matrix[i][j];
        }
      }
    }
    return result;
  }

  public static <T extends Number> BigDecimal determinant(final T[][] matrix, Class<T> of) {
    if (isNull(matrix)) {
      throw new IllegalArgumentException("Impossible to find determinant of null matrix");
    }
    int length = matrix.length;
    if (length == 0) {
      return BigDecimal.ZERO;
    }
    if (matrix[0].length != length) {
      throw new IllegalArgumentException("Impossible to find determinant for matrix columns != rows");
    }
    return calculateDeterminant(matrix, of);
  }

  private static <T extends Number> BigDecimal calculateDeterminant(final T[][] matrix, Class<T> of) {
    int length = matrix.length;
    if (length == 2) {
      BigDecimal first = new BigDecimal(matrix[0][0].toString());
      BigDecimal second = new BigDecimal(matrix[0][1].toString());
      BigDecimal third = new BigDecimal(matrix[1][0].toString());
      BigDecimal fourth = new BigDecimal(matrix[1][1].toString());
      return first.multiply(fourth).subtract(third.multiply(second));
    }
    if (length == 1) {
      return new BigDecimal(matrix[0][0].toString());
    }
    T[][] copied = copy(matrix, of);
    BigDecimal res = BigDecimal.ZERO;
    for (int j = 0; j < length; ++j) {
      T[][] minor = minor(copied, of, 1, j);
      res = res.add(BigDecimal.valueOf(Math.pow(-1, 1 + j)).multiply(new BigDecimal(copied[1][j].toString()))
                              .multiply(calculateDeterminant(minor, of)));
    }
    return res;
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] getCopyOfRow(final T[][] source, int index, Class<T> of) {
    if (isEmpty(source)) {
      return (T[]) Array.newInstance(of, 0);
    }
    if (index >= source.length) {
      throw new IllegalArgumentException("Error getting row of matrix. Index out of range");
    }
    int columns = source[0].length;
    T[] arr = (T[]) Array.newInstance(of, columns);
    System.arraycopy(source[index], 0, arr, 0, columns);
    return arr;
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] getCopyOfColumn(final T[][] source, int index, Class<?> of) {
    if (isEmpty(source)) {
      return (T[]) Array.newInstance(of, 0);
    }
    if (index >= source[0].length) {
      throw new IllegalArgumentException("Error getting column of matrix. Index out of range");
    }
    int rows = source.length;
    T[] arr = (T[]) Array.newInstance(of, rows);
    for (int i = 0; i < rows; ++i) {
      arr[i] = source[i][index];
    }
    return arr;
  }

  public static <T> T[][] transpose(final T[][] matrix, Class<T> of) {
    if (isNull(matrix)) {
      throw new IllegalArgumentException("Impossible to transpose NULL");
    }
    T[][] copied = copy(matrix, of);
    for (int i = 0; i < copied.length; i++) {
      for (int j = i + 1; j < copied.length; j++) {
        T temp = copied[i][j];
        copied[i][j] = copied[j][i];
        copied[j][i] = temp;
      }
    }
    return copied;
  }

  public static Integer[][] getMatrixOfAlgebraicSupplements(final Integer[][] matrix) {
    if (isEmpty(matrix)) {
      throw new IllegalArgumentException("Impossible to find matrix of algebraic supplements for NULL or empty matrix");
    }
    final int rows = matrix.length;
    final int columns = matrix[0].length;
    Integer[][] result = new Integer[rows][columns];
    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < columns; ++j) {
        BigInteger supplement = determinant(minor(matrix, Integer.class, i, j), Integer.class)
            .toBigInteger()
            .multiply(valueOf((int) Math.pow(-1, i + j)));
        result[i][j] = supplement.intValue();
      }
    }
    return result;
  }

  public static Double[][] getMatrixOfAlgebraicSupplements(final Double[][] matrix) {
    if (isEmpty(matrix)) {
      throw new IllegalArgumentException("Impossible to find matrix of algebraic supplements for NULL or empty matrix");
    }
    final int rows = matrix.length;
    final int columns = matrix[0].length;
    Double[][] result = new Double[rows][columns];
    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < columns; ++j) {
        BigDecimal supplement = determinant(minor(matrix, Double.class, i, j), Double.class)
            .multiply(BigDecimal.valueOf((int) Math.pow(-1, i + j)));
        result[i][j] = supplement.doubleValue();
      }
    }
    return result;
  }

  public static Integer[][] reverse(final Integer[][] matrix, BigInteger mod, BigInteger determinant) {
    if (isEmpty(matrix)
        || matrix.length != matrix[0].length
        || determinant.mod(mod).equals(BigInteger.ZERO)
        || !determinant.gcd(mod).equals(BigInteger.ONE)) {
      throw new IllegalArgumentException("Impossible to find mod-reverse matrix ");
    }
    BigInteger reverseDet = determinant.modInverse(mod);
    int size = matrix.length;
    Integer[][] matrixOfAlgebraicSupplement = getMatrixOfAlgebraicSupplements(matrix);
    int element;
    int modInt = mod.intValue();
    for (int i = 0; i < size; ++i) {
      for (int j = 0; j < size; ++j) {
        element = matrixOfAlgebraicSupplement[i][j];
        element = BigInteger.valueOf(element).mod(mod).multiply(reverseDet).mod(mod).intValue();
        while (element < 0) {
          element += modInt;
        }
        matrixOfAlgebraicSupplement[i][j] = element;
      }
    }
    return transpose(matrixOfAlgebraicSupplement, Integer.class);
  }

  public static Integer[][] reverse(final Integer[][] matrix) {
    int determinant = determinant(matrix, Integer.class).intValue();
    if (determinant == 0) {
      throw new IllegalArgumentException("Impossible to find reverse matrix: determinant is 0");
    }
    int size = matrix.length;
    Integer[][] matrixOfAlgebraicSupplement = getMatrixOfAlgebraicSupplements(matrix);
    int element;
    for (int i = 0; i < size; ++i) {
      for (int j = 0; j < size; ++j) {
        element = matrixOfAlgebraicSupplement[i][j];
        element /= determinant;
        matrixOfAlgebraicSupplement[i][j] = element;
      }
    }
    return transpose(matrixOfAlgebraicSupplement, Integer.class);
  }

  public static Double[][] reverse(final Double[][] matrix) {
    double determinant = determinant(matrix, Double.class).doubleValue();
    if (determinant == 0D) {
      throw new IllegalArgumentException("Impossible to find reverse matrix: determinant is 0");
    }
    int size = matrix.length;
    Double[][] matrixOfAlgebraicSupplement = getMatrixOfAlgebraicSupplements(matrix);
    double element;
    for (int i = 0; i < size; ++i) {
      for (int j = 0; j < size; ++j) {
        element = matrixOfAlgebraicSupplement[i][j];
        element /= determinant;
        matrixOfAlgebraicSupplement[i][j] = element;
      }
    }
    return transpose(matrixOfAlgebraicSupplement, Double.class);
  }

  public static int[] multiplyVectorOnMatrix(final int[] vector, final int[][] matrix, Integer mod) {
    int[] arr = getEmptyMultiplicationVector(vector, matrix);
    for (int i = 0; i < arr.length; ++i) {
      for (int j = 0; j < matrix.length; ++j) {
        arr[j] += matrix[i][j] * vector[i];
      }
      arr[i] = Math.floorMod(arr[i], mod);
    }
    return arr;
  }

  public static int[] multiplyVectorOnMatrix(final int[] vector, final int[][] matrix) {
    int[] arr = getEmptyMultiplicationVector(vector, matrix);
    for (int i = 0; i < arr.length; ++i) {
      for (int j = 0; j < matrix.length; ++j) {
        arr[j] += matrix[i][j] * vector[i];
      }
    }
    return arr;
  }

  public static Integer[] multiplyVectorOnMatrix(final Integer[] vector, final Integer[][] matrix, Integer mod) {
    Integer[] arr = getEmptyMultiplicationVector(vector, matrix);
    for (int i = 0; i < arr.length; ++i) {
      for (int j = 0; j < matrix.length; ++j) {
        arr[i] += matrix[j][i] * vector[j];
      }
      arr[i] = Math.floorMod(arr[i], mod);
    }
    return arr;
  }

  public static Integer[] multiplyVectorOnMatrix(final Integer[] vector, final Integer[][] matrix) {
    Integer[] arr = getEmptyMultiplicationVector(vector, matrix);
    for (int i = 0; i < arr.length; ++i) {
      for (int j = 0; j < matrix.length; ++j) {
        arr[i] += matrix[j][i] * vector[j];
      }
    }
    return arr;
  }

  public static double[] multiplyVectorOnMatrix(final double[] vector, final double[][] matrix) {
    double[] arr = getEmptyMultiplicationVector(vector, matrix);
    for (int i = 0; i < arr.length; ++i) {
      for (int j = 0; j < matrix.length; ++j) {
        arr[j] += matrix[i][j] * vector[i];
      }
    }
    return arr;
  }


  public static Double[] multiplyVectorOnMatrix(final Double[] vector, final Double[][] matrix) {
    Double[] arr = getEmptyMultiplicationVector(vector, matrix);
    for (int i = 0; i < arr.length; i++) {
      for (int j = 0; j < matrix.length; j++) {
        arr[j] += matrix[i][j] * vector[i];
      }
    }
    return arr;
  }

  private static Double[] getEmptyMultiplicationVector(Double[] vector, Double[][] matrix) {
    if (isNull(vector) || isNull(matrix)) {
      throw new IllegalArgumentException("Impossible to multiply: NULL object(-s)");
    }
    if (vector.length != matrix.length) {
      throw new IllegalArgumentException("Impossible to multiply. Sizes are incompatible");
    }
    int columns = matrix[0].length;
    Double[] arr = new Double[columns];
    Arrays.fill(arr, 0d);
    return arr;
  }

  private static double[] getEmptyMultiplicationVector(double[] vector, double[][] matrix) {
    if (isNull(vector) || isNull(matrix)) {
      throw new IllegalArgumentException("Impossible to multiply: NULL object(-s)");
    }
    if (vector.length != matrix.length) {
      throw new IllegalArgumentException("Impossible to multiply. Sizes are incompatible");
    }
    int columns = matrix[0].length;
    double[] arr = new double[columns];
    Arrays.fill(arr, 0);
    return arr;
  }

  private static Integer[] getEmptyMultiplicationVector(Integer[] vector, Integer[][] matrix) {
    if (isNull(vector) || isNull(matrix)) {
      throw new IllegalArgumentException("Impossible to multiply: NULL object(-s)");
    }
    if (vector.length != matrix.length) {
      throw new IllegalArgumentException("Impossible to multiply. Sizes are incompatible");
    }

    int columns = matrix[0].length;
    Integer[] arr = new Integer[columns];
    Arrays.fill(arr, 0);
    return arr;
  }

  private static int[] getEmptyMultiplicationVector(int[] vector, int[][] matrix) {
    if (isNull(vector) || isNull(matrix)) {
      throw new IllegalArgumentException("Impossible to multiply: NULL object(-s)");
    }
    if (vector.length != matrix.length) {
      throw new IllegalArgumentException("Impossible to multiply. Sizes are incompatible");
    }
    int columns = matrix[0].length;
    return new int[columns];
  }
}
