package com.example.akka.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.example.akka.records.RequestMessage;

public class MapperActor extends UntypedActor {

    private final ActorRef[] reducers;

    public MapperActor(ActorRef[] reducers) {
        this.reducers = reducers;
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof String line) {

            String[] words = line.split("\\s+");

            for (String word : words) {
                ActorRef reducer = partition(word, reducers);
                reducer.tell(new RequestMessage(word), getSelf());
            }
        } else {
            unhandled(message);
        }
    }

    private ActorRef partition(String word, ActorRef[] reducers) {
        int index = Math.abs(word.hashCode()) % reducers.length;
        return reducers[index];
    }
}
