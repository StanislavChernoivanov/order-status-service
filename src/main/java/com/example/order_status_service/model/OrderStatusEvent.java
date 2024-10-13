package com.example.order_status_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusEvent implements Serializable {

    private String status;
    private Instant date;

}
