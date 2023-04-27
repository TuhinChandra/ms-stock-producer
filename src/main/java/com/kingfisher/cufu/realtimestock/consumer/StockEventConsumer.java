package com.kingfisher.cufu.realtimestock.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StockEventConsumer {

    @KafkaListener(topics = {"sandpit.cf-oms-poc-store-stock-topic"})
    public void onMessage(ConsumerRecord<Long,String> consumerRecord){

        log.info("ConsumerRecord : {} ", consumerRecord );

    }
}
