package by.artsiom.fast.programming.utils.async;

import static by.artsiom.fast.programming.utils.streams.StreamUtils.map;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

/**
 * @author Artyom Konashchenko
 * @since 30.12.2022
 */
public class AsyncUtils {

  private AsyncUtils() throws IllegalAccessException {
    throw new IllegalAccessException("This is utility class that cannot be initialized");
  }

  public static <S, R> List<R> supplyAll(List<S> payload, Function<S, R> execution, Executor executor) {
    return map(payload, object -> supplyAsync(() -> execution.apply(object), executor))
        .map(CompletableFuture::join)
        .collect(toList());
  }
}
