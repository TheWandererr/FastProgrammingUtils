package by.artsiom.fast.programming.utils.enums;

import static by.artsiom.fast.programming.utils.arrays.ArrayUtils.isEmpty;
import static by.artsiom.fast.programming.utils.collections.CollectionUtils.findFirst;
import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Artyom Konashchenko
 * @since 30.12.2022
 */
public interface Enumerated {

  String getName();

  static <T extends Enumerated> String[] names(Class<T> clazz) {
    final T[] enumConstants = clazz.getEnumConstants();
    if (isEmpty(enumConstants)) {
      return new String[0];
    }
    return Arrays.toString(enumConstants).replaceAll("^.|.$", "").split(", ");
  }

  static <T extends Enumerated> Optional<T> findByName(String name, Class<T> clazz) {
    return findFirst(asList(clazz.getEnumConstants()), enumerated -> enumerated.getName().equalsIgnoreCase(name));
  }

  static <T extends Enumerated> T getByName(String name, Class<T> clazz) {
    return findByName(name, clazz).orElseGet(() -> null);
  }
}
