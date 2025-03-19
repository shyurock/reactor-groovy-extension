package org.shyurock.reactor.extension

import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class MonoExtensionSpec extends Specification {

    def "should emit error if predicate is true"() {
        given:
        def mono = Mono.just("test")
        def predicate = { it == "test" }
        def exception = new RuntimeException("Predicate is true")

        when:
        MonoExtension.errorIfConditionTrue(mono, predicate, exception).block()

        then:
        def t = thrown(RuntimeException)
        t.message == "Predicate is true"
    }

    def "should emit error if predicate is true as extension"() {
        expect:
        StepVerifier.create(
                Mono.just(true)
                        .errorIfConditionTrue({ it == true }, new RuntimeException())
        )
                .expectError(RuntimeException.class)
                .verify()
    }

    def "should return original Mono if predicate is false"() {
        given:
        def mono = Mono.just("test")
        def predicate = { it == "not_test" }
        def exception = new RuntimeException("Predicate is true")

        when:
        def result = MonoExtension.errorIfConditionTrue(mono, predicate, exception)

        then:
        result.block() == "test" // Should return the original value
    }

    def "should handle empty Mono correctly"() {
        given:
        def mono = Mono.empty()
        def predicate = { it == "test" }
        def exception = new RuntimeException("Predicate is true")

        when:
        def result = MonoExtension.errorIfConditionTrue(mono, predicate, exception)

        then:
        result.blockOptional().isPresent() == false // Should remain empty
    }

    def "should not throw exception if predicate is false for null value"() {
        given:
        def mono = Mono.justOrEmpty(null)
        def predicate = { it == "not_null" }
        def exception = new RuntimeException("Predicate is true")

        when:
        def result = MonoExtension.errorIfConditionTrue(mono, predicate, exception)

        then:
        result.block() == null // Should return the original value (null)
    }
}

