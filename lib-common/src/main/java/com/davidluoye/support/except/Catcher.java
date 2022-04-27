package com.davidluoye.support.except;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Catcher {

    public static <T, R> R execute(T t, Function<T, R> function) {
        try {
            return function.apply(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T, U, R> R execute(T t, U u, BiFunction<T, U, R> function) {
        try {
            return function.apply(t, u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> void execute(T t, Consumer<T> consumer) {
        try {
            consumer.accept(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T, U> void execute(T t, U u, BiConsumer<T, U> consumer) {
        try {
            consumer.accept(t, u);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <R> R execute(Supplier<R> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> boolean execute(T t, Predicate<T> predicate) {
        try {
            return predicate.test(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static <T, U> boolean execute(T t, U u, BiPredicate<T, U> predicate) {
        try {
            return predicate.test(t, u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
