package com.Sofka.Reactive.StepVerifier.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@org.springframework.stereotype.Service
public class Service {

    public Mono<String> findOne(){
        return Mono.just("Pedro");
    }

    public Flux<String> findAll(){
        return Flux.just("Pedro", "María", "Jesus", "Carmen");
    }

    public Flux<String> findAllSlow(){
        return Flux.just("Pedro", "María", "Jesus", "Carmen").delaySequence(Duration.ofSeconds(20));
    }

    public Flux<String> findAllFilter() {
        Flux<String> source = Flux.just("John", "Monica", "Mark", "Cloe", "Frank", "Casper", "Olivia", "Emily", "Cate")
                .filter(name -> name.length() == 4)
                .map(String::toUpperCase);
        return source;
    }

    public Flux<Integer> afirmacionesPosteriores_a_la_Ejecucion(){
        Flux<Integer> source = Flux.<Integer>create(emitter -> {
            emitter.next(1);
            emitter.next(2);
            emitter.next(3);
            emitter.complete();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            emitter.next(4);
        }).filter(number -> number % 2 == 0);

        return source;
    }
}
