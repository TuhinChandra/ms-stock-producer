package com.kingfisher.cufu.realtimestock.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StoreStock {
    private String ean;
    private String storeId;
    private String opco;
    private Long stockLevel;
}
