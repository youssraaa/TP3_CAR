package com.example.akka.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.springframework.stereotype.Service;
import com.example.akka.actor.MapperActor;
import com.example.akka.actor.ReducerActor;

@Service
public class AkkaService {

    private final ActorSystem system;
    private ActorRef[] mappers;
    private ActorRef[] reducers;

    public AkkaService() {

        this.system = ActorSystem.create("AkkaSystem");
    }

    public void initializeActors() {

        reducers = new ActorRef[2];
        for (int i = 0; i < reducers.length; i++) {
            reducers[i] = system.actorOf(Props.create(ReducerActor.class), "Reducer-" + i);
        }

        mappers = new ActorRef[3];
        for (int i = 0; i < mappers.length; i++) {
            mappers[i] = system.actorOf(Props.create(MapperActor.class), "Mapper-" + i);
        }

        System.out.println("Actors initialized successfully!");
    }

    public ActorSystem getSystem() {
        return system;
    }
}
