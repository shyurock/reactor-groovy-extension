package org.shyurock.reactor.extension

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import java.time.Duration
import java.util.function.Function

class FluxExtension {

    /**
     * Flattens the results of a Mono<T> returned by the provided function
     * for each element emitted by the source Flux.
     *
     * @param self the source Flux
     * @param action a function that maps each element to a Mono<T>
     * @return a Flux that emits the original elements after applying the action
     */
    static <T> Flux<T> flatNext(Flux<T> self, Function<T, Mono<T>> action) {
        self.flatMap {
            action.apply(it)
                    .thenReturn(it)
        }
    }

    /**
     * Collects all elements emitted by the source Flux into a List,
     * blocking until the collection is complete or the specified duration elapses.
     *
     * @param self the source Flux
     * @param duration an optional duration to wait for the collection
     * @return a List containing all elements emitted by the source Flux
     */
    static <T> List<T> blockAll(Flux<T> self, Duration duration = null) {
        self.collectList()
                .block(duration)
    }
}