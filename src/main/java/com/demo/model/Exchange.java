package com.demo.model;

import lombok.*;

/**
 * Model class to hold exchange data
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Exchange {

    private String currency;
    private double rate;

}
