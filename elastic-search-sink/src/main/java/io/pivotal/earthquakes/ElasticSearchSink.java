package io.pivotal.earthquakes;

import io.pivotal.earthquakes.service.ElasticsearchPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.text.ParseException;

@EnableBinding(Sink.class)
public class ElasticSearchSink {

    @Autowired
    private ElasticsearchPersistenceService svc;

    @Autowired
    private ElasticSearchConfiguration config;

    @StreamListener(Sink.INPUT)
    public void onMessage(Message<?> message) {

        //ignore header
        if (message.getPayload().toString().startsWith("DateTime")) return;

        if (!svc.isConfigured()) svc.config(config.getUrl());

        String payload = message.getPayload().toString();
        try {
            svc.insert(svc.parse(payload));
        }
        catch(ParseException pe) {
            System.out.println("Ignoring line with data missing: "+payload);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
