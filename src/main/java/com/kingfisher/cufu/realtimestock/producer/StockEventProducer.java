package com.kingfisher.cufu.realtimestock.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingfisher.cufu.realtimestock.domain.StockEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class StockEventProducer {

    @Autowired
    KafkaTemplate<Long,String> kafkaTemplate;

    String topic = "sandpit.cf-oms-poc-store-stock-topic";
    @Autowired
    ObjectMapper objectMapper;

    public void sendStockEvent(StockEvent stockEvent) throws JsonProcessingException {

        Long key = stockEvent.getEventId();
        String value = objectMapper.writeValueAsString(stockEvent);
//        kafkaTemplate.send(topic,key,value);

        ListenableFuture<SendResult<Long,String>> listenableFuture =  kafkaTemplate.sendDefault(key,value);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Long, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Long, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    public void sendStockEventApproach2(StockEvent stockEvent) throws JsonProcessingException {

        Long key = stockEvent.getEventId();
        String value = objectMapper.writeValueAsString(stockEvent);

        ProducerRecord<Long,String> producerRecord = buildProducerRecord(key, value, topic);

        ListenableFuture<SendResult<Long,String>> listenableFuture =  kafkaTemplate.send(producerRecord);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Long, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Long, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    private ProducerRecord<Long, String> buildProducerRecord(Long key, String value, String topic) {


        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));

        return new ProducerRecord(topic, null, System.currentTimeMillis(), key, value, recordHeaders);
    }


    public SendResult<Long, String> sendStockEventSynchronous(StockEvent stockEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {

        Long key = stockEvent.getEventId();
        String value = objectMapper.writeValueAsString(stockEvent);
        SendResult<Long,String> sendResult=null;
        try {
            sendResult = kafkaTemplate.sendDefault(key,value).get(1, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            log.error("ExecutionException/InterruptedException Sending the Message and the exception is {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception Sending the Message and the exception is {}", e.getMessage());
            throw e;
        }

        return sendResult;

    }

    private void handleFailure(Long key, String value, Throwable ex) {
        log.error("Error Sending the Message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }


    }

    private void handleSuccess(Long key, String value, SendResult<Long, String> result) {
        log.info("Message Sent SuccessFully for the key : {} and the value is {} , partition is {}", key, value, result.getRecordMetadata().partition());
    }
}
