package com.kingfisher.cufu.realtimestock.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StockEvent {
    private Long eventId;
    private StoreStock stock;
}
