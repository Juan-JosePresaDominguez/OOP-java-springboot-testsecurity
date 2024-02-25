package com.microcompany.accountsservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoneyForOwner {
    @Min(1)
    private Long ownerId;
    @Min(1)
    private Integer amount;
}
