package com.microcompany.productsservice.model;

import com.microcompany.productsservice.constraints.SerialNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(name = "product", description = "Modelo producto")
@XmlRootElement
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(0)
    @Schema(name = "Product ID", example = "1", required = false)
    private Long id;

    @Column
    @NotBlank(message = "Debe tener valor")
    @NotNull
    @Size(min = 3, max = 50)
    @Schema(name = "Product name", example = "Product 1", required = true)
    private String name;

    @Column
//    @NotNull @NotBlank(message = "Debe tener valor con formato ddd-ddd-dddd")
    @SerialNumber(message = "{serial.format}")
    @Schema(name = "Product serial number", example = "111-222-3333", required = true)
    private String serial;
}
