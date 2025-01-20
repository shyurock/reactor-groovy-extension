package org.shyurock.reactor.extension

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import java.time.Duration
import java.util.function.Function

class FluxExtension {

    /**
     *
     * @param self
     * @param action
     * @return
     */
    static <T, R> Flux<T> flatNext(Flux<T> self, Function<T, Mono<R>> action) {
        self.flatMap {
            action.apply(it)
                    .thenReturn(it)
        }
    }

    /**
     *
     * @param self
     * @param duration
     * @return
     */
    static <T> List<T> blockAll(Flux<T> self, Duration duration = null) {
        self.collectList()
                .block(duration)
    }
}
