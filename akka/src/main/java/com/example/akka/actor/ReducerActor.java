package com.example.akka.actor;

import akka.actor.UntypedActor;
import com.example.akka.records.RequestMessage;
import com.example.akka.records.ResponseMessage;
import java.util.HashMap;
import java.util.Map;

public class ReducerActor extends UntypedActor {

    private final Map<String, Integer> wordCounts = new HashMap<>();

    @Override
    public void onReceive(Object message) {
        if (message instanceof RequestMessage request) {
        	
            String word = request.word();
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            
        } else if (message instanceof String queryWord) {

            int count = wordCounts.getOrDefault(queryWord, 0);
            getSender().tell(new ResponseMessage(count), getSelf());
            
        } else {
            unhandled(message);
        }
    }
}
