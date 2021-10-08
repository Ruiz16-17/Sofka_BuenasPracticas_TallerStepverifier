package com.Sofka.Reactive.StepVerifier.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServiceTest {

    @Autowired
    Service service;

    @Test
    void testMono(){
        Mono<String> one = service.findOne();
        StepVerifier.create(one).expectNext("Pedro").verifyComplete();
    }

    @Test
    void testSeveral(){
        Flux<String> one = service.findAll();
        StepVerifier.create(one).expectNext("Pedro").expectNext("María").expectNext("Jesus").expectNext("Carmen").verifyComplete();
    }

    @Test
    void testSeveralSlow(){
        Flux<String> one = service.findAllSlow();
        StepVerifier.create(one)
                .expectNext("Pedro")
                .thenAwait(Duration.ofSeconds(1))
                .expectNext("María")
                .thenAwait(Duration.ofSeconds(1))
                .expectNext("Jesus")
                .thenAwait(Duration.ofSeconds(1))
                .expectNext("Carmen")
                .thenAwait(Duration.ofSeconds(1))
                .verifyComplete();
    }

    @Test
    void testAllFilter() {
        Flux<String> source = service.findAllFilter();
        StepVerifier
                .create(source)
                .expectNext("JOHN")
                .expectNextMatches(name -> name.startsWith("MA"))
                .expectNext("CLOE", "CATE")
                .expectComplete()
                .verify();
    }

    @Test
    void testTodosFilter_Concat() {
        Flux<String> source = service.findAllFilter();
        Flux<String> error = source.concatWith(
                Mono.error(new IllegalArgumentException("Error message"))
        );
        StepVerifier
                .create(error)
                .expectNextCount(4)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Error message"))
                .verify();
    }

    @Test
    void testafirmacionesPosteriores_a_la_Ejecucion(){
        Flux<Integer> source = service.afirmacionesPosteriores_a_la_Ejecucion();

        StepVerifier.create(source)
                .expectNext(2)
                .expectComplete()
                .verifyThenAssertThat()
                .hasDropped(4)
                .tookLessThan(Duration.ofMillis(1050));
    }
}