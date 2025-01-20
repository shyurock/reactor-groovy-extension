package org.shyurock.reactor.extension

import reactor.core.publisher.Mono

import java.util.function.Function
import java.util.function.Predicate

class MonoExtension {

    /**
     *
     * @param self
     * @param action
     * @return
     */
    static <T, R> Mono<T> flatNext(Mono<T> self, Function<T, Mono<R>> action) {
        self.flatMap {
            action.apply(it)
                    .thenReturn(it)
        }
    }

    /**
     *
     * @param self
     * @param predicate
     * @param action
     * @return
     */
    static <T, R> Mono<T> flatNextIf(Mono<T> self, Predicate<T> predicate, Function<T, Mono<R>> action) {
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
     *
     * @param self
     * @param condition
     * @param action
     * @return
     */
    static <T, R> Mono<T> flatNextIf(Mono<T> self, Boolean condition, Function<T, Mono<R>> action) {
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
     *
     * @param self
     * @param ex
     * @return
     */
    static <T> Mono<T> errorIfEmpty(Mono<T> self, Exception ex) {
        self.switchIfEmpty(Mono.error(ex))
    }

    /**
     *
     * @param self
     * @param predicate
     * @param ex
     * @return
     */
    static <T> Mono<T> errorIfConditionFalse(Mono<T> self, Predicate<T> predicate, Exception ex) {
        self.flatMap {predicate.test(it) ? self : Mono.error(ex) as Mono<T>}
    }

    /**
     *
     * @param self
     * @param predicate
     * @param ex
     * @return
     */
    static <T> Mono<T> errorIfConditionTrue(Mono<T> self, Predicate<T> predicate, Exception ex) {
        self.flatMap {predicate.test(it) ? Mono.error(ex) as Mono<T> : self }
    }
}