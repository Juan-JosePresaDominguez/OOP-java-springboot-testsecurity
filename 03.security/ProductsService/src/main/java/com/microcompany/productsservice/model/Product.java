package com.microcompany.productsservice.model;

import com.microcompany.productsservice.constraints.SerialNumber;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "products")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(0)
    private Long id;

    @Column
    @NotBlank
    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @Column
    @NotBlank(message = "{serial.not.blank}")
    @NotNull
    @SerialNumber(message = "{serial.format}")
    private String serial;
}
