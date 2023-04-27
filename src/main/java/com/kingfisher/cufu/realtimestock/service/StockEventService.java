package com.kingfisher.cufu.realtimestock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kingfisher.cufu.realtimestock.domain.StockEvent;
import com.kingfisher.cufu.realtimestock.domain.StoreStock;
import com.kingfisher.cufu.realtimestock.producer.StockEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StockEventService {
    @Autowired
    private StockEventProducer producer;

    public void sendStockEvent(StockEvent stockEvent) throws JsonProcessingException {
        producer.sendStockEvent(stockEvent);
    }

    public List<StockEvent> generateAndSendStockEvent(long number) throws JsonProcessingException {
        List<StockEvent> listOfStockEvents=new ArrayList<>();
        for (long index = 1; index <= number; index++) {
            StockEvent stockEvent = StockEvent.builder().eventId(index).stock(generateStoreStock(index)).build();
            producer.sendStockEvent(stockEvent);
            listOfStockEvents.add(stockEvent);
        }
        return listOfStockEvents;
    }

    private static StoreStock generateStoreStock(long index) {
        StoreStock stock = StoreStock.builder().ean("EAN-" + index).storeId("Store-"+ index).stockLevel(index).opco("BQUK").build();
        return stock;
    }
}


