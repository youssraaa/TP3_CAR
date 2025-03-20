package com.example.akka.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.akka.actor.MapperActor;
import com.example.akka.actor.ReducerActor;
import com.example.akka.records.RequestMessage;
import com.example.akka.records.ResponseMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

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
            mappers[i] = system.actorOf(Props.create(MapperActor.class, (Object) reducers), "Mapper-" + i);
        }
    }

    public void processFile(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int mapperIndex = 0;
            while ((line = reader.readLine()) != null) {
                mappers[mapperIndex].tell(line, ActorRef.noSender());
                mapperIndex = (mapperIndex + 1) % mappers.length; 
            }
        }
    }

    public int getWordOccurrences(String word) {
        int totalOccurrences = 0;

        for (ActorRef reducer : reducers) {
            Future<Object> future = Patterns.ask(reducer, word, 5000);
            try {
                ResponseMessage response = (ResponseMessage) Await.result(future, Duration.create(5, TimeUnit.SECONDS));
                totalOccurrences += response.count();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return totalOccurrences;
    }
}
