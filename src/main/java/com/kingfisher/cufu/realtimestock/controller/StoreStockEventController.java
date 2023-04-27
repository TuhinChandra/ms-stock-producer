package com.kingfisher.cufu.realtimestock.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.kingfisher.cufu.realtimestock.domain.StockEvent;
import com.kingfisher.cufu.realtimestock.service.StockEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StoreStockEventController {
    @Autowired
    private StockEventService stockEventService;

    @PostMapping("/v1/stockevent")
    public ResponseEntity<StockEvent> postLibraryEvent(@RequestBody StockEvent stockEvent) throws JsonProcessingException {

        stockEventService.sendStockEvent(stockEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(stockEvent);
    }
    @PostMapping("/v1/stockevent/bulk/{number}")
    public ResponseEntity<List<StockEvent>> generateAndSendStockEvent(@PathVariable long number) throws JsonProcessingException {

        List<StockEvent> listOfStockEvents= stockEventService.generateAndSendStockEvent(number);
        return ResponseEntity.status(HttpStatus.CREATED).body(listOfStockEvents);
    }
}

