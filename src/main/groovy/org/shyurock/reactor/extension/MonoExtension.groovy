package org.shyurock.reactor.extension

import groovy.transform.CompileStatic
import reactor.core.publisher.Mono

import java.util.function.Function
import java.util.function.Predicate

@CompileStatic
class MonoExtension {

    /**
     * Flattens the next value produced by the specified action function.
     *
     * @param self the Mono to operate on
     * @param action the function to apply to the value of the Mono, returning another Mono
     * @return a Mono that emits the original value after applying the action
     */
    static <T> Mono<T> flatNext(Mono<T> self, Function<T, Mono<T>> action) {
        self.flatMap {
            action.apply(it)
                    .thenReturn(it)
        }
    }

    /**
     * Flattens the next value if the predicate is true.
     *
     * @param self the Mono to operate on
     * @param predicate the condition to check
     * @param action the function to apply to the value of the Mono if the predicate is true
     * @return a Mono that emits the original value if the predicate is true, or the same Mono otherwise
     */
    static <T> Mono<T> flatNextIf(Mono<T> self, Predicate<T> predicate, Function<T, Mono<T>> action) {
        self.flatMap {
            if (predicate.test(it)) {
                action.apply(it)
                        .thenReturn(it)
            } else {
                self
            }
        }
    }

    /**
     * Flattens the next value if the condition is true.
     *
     * @param self the Mono to operate on
     * @param condition the condition to check
     * @param action the function to apply to the value of the Mono if the condition is true
     * @return a Mono that emits the original value if the condition is true, or the same Mono otherwise
     */
    static <T> Mono<T> flatNextIf(Mono<T> self, Boolean condition, Function<T, Mono<T>> action) {
        self.flatMap {
            if (condition) {
                action.apply(it)
                        .thenReturn(it)
            } else {
                self
            }
        }
    }

    /**
     * Emits an error if the Mono is empty.
     *
     * @param self the Mono to operate on
     * @param ex the exception to emit if the Mono is empty
     * @return a Mono that emits an error if empty, or the original Mono
     */
    static <T> Mono<T> errorIfEmpty(Mono<T> self, Exception ex) {
        self.switchIfEmpty(Mono.error(ex))
    }

    /**
     * Emits an error if the predicate is false.
     *
     * @param self the Mono to operate on
     * @param predicate the condition to check
     * @param ex the exception to emit if the predicate is false
     * @return a Mono that emits an error if the predicate is false, or the original Mono
     */
    static <T> Mono<T> errorIfConditionFalse(Mono<T> self, Predicate<T> predicate, Exception ex) {
        self.flatMap { predicate.test(it) ? self : Mono.error(ex) as Mono<T> }
    }

    /**
     * Emits an error if the predicate is true.
     *
     * @param self the Mono to operate on
     * @param predicate the condition to check
     * @param ex the exception to emit if the predicate is true
     * @return a Mono that emits an error if the predicate is true, or the original Mono
     */
    static <T> Mono<T> errorIfConditionTrue(Mono<T> self, Predicate<T> predicate, Exception ex) {
        self.flatMap { predicate.test(it) ? Mono.error(ex) as Mono<T> : self }
    }
}