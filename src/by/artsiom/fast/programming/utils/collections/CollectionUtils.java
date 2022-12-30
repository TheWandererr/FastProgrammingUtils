package by.artsiom.fast.programming.utils.collections;

import static by.artsiom.fast.programming.utils.streams.StreamUtils.flatStream;
import static by.artsiom.fast.programming.utils.streams.StreamUtils.map;
import static by.artsiom.fast.programming.utils.streams.StreamUtils.nullableStream;
import static by.artsiom.fast.programming.utils.streams.StreamUtils.select;
import static by.artsiom.fast.programming.utils.streams.StreamUtils.stream;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;

/**
 * @author Artyom Konashchenko
 * @since 05.04.2020
 */
public class CollectionUtils {

  private CollectionUtils() throws IllegalAccessException {
    throw new IllegalAccessException("This is utility class that cannot be initialized");
  }

  public static <T> T[] toArray(Collection<T> collection, IntFunction<T[]> generator) {
    return stream(collection).toArray(generator);
  }

  public static <T> T[] selectToArray(Collection<T> collection, Predicate<T> condition, IntFunction<T[]> generator) {
    return select(collection, condition).toArray(generator);
  }

  public static <T, V> V[] mapToArray(Collection<T> collection, Function<T, V> mapper, IntFunction<V[]> generator) {
    return map(collection, mapper).toArray(generator);
  }

  public static <T> boolean isEmpty(Collection<T> collection) {
    return isNull(collection) || collection.size() == 0;
  }

  public static <T> boolean isNotEmpty(Collection<T> collection) {
    return !isEmpty(collection);
  }

  public static <T> boolean isSingleton(Collection<T> collection) {
    return !isEmpty(collection) && collection.size() == 1;
  }

  public static <T> List<T> selectToList(Collection<T> collection, Predicate<? super T> condition) {
    return select(collection, condition).collect(toList());
  }

  public static <S, T> List<T> mapToList(Collection<S> collection, Function<S, T> mapper) {
    return map(collection, mapper).collect(toList());
  }

  public static <S, T> List<T> mapToList(Collection<S> collection, Function<S, T> mapper, Predicate<? super T> condition) {
    return map(collection, mapper).filter(condition).collect(toList());
  }

  public static <S, T> Set<T> mapToSet(Collection<S> collection, Function<S, T> mapper) {
    return map(collection, mapper).collect(toSet());
  }

  public static <S, T> Set<T> mapToSet(Collection<S> collection, Function<S, T> mapper, Predicate<? super T> condition) {
    return map(collection, mapper).filter(condition).collect(toSet());
  }

  public static <T> List<T> list(Collection<T> collection) {
    return nullableStream(collection).collect(toList());
  }

  public static <T> Set<T> set(Collection<T> collection) {
    return nullableStream(collection).collect(toSet());
  }

  @SafeVarargs
  public static <T> List<T> union(Collection<T>... collections) {
    return flatStream(collections).collect(toList());
  }

  public static <T> Optional<T> findFirst(Collection<T> collection, Predicate<? super T> condition) {
    return select(collection, condition).findFirst();
  }

  public static <T> Optional<T> firstOptional(Collection<T> collection) {
    return nullableStream(collection).findFirst();
  }

  public static <T> T first(Collection<T> collection) {
    return firstOptional(collection).orElse(null);
  }

  public static <T> Optional<T> findLast(Collection<T> collection, Predicate<? super T> condition) {
    return select(collection, condition).reduce((prev, next) -> next);
  }

  public static <T> Optional<T> lastOptional(Collection<T> collection) {
    return nullableStream(collection).reduce((prev, next) -> next);
  }

  public static <T> T last(Collection<T> collection) {
    return lastOptional(collection).orElse(null);
  }

  public static <T> boolean isPresent(Collection<T> collection, Predicate<? super T> condition) {
    return stream(collection).anyMatch(condition);
  }

  public static <T> T extract(List<T> list, int index) {
    return list.remove(index);
  }

  public static <T> T pop(List<T> list) {
    return extract(list, 0);
  }

  public static <T> boolean containsAny(Collection<T> source, Collection<T> samples) {
    return isPresent(samples, source::contains);
  }

  public static <R, A, T> R collect(Collection<T> source, Collector<? super T, A, R> collector) {
    return stream(source).collect(collector);
  }

  public static <T> Set<T> newHashSet(Collection<T> collection) {
    return set(collection);
  }

  public static <T> List<T> newArrayList(Collection<T> collection) {
    return list(collection);
  }

  public static <T> boolean allMatch(Collection<T> collection, Predicate<? super T> condition) {
    if (isEmpty(collection)) {
      return false;
    }
    return stream(collection).allMatch(condition);
  }
}
