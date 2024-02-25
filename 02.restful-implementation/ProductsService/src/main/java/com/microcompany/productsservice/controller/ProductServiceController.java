package com.microcompany.productsservice.controller;

import com.microcompany.productsservice.exception.ProductNotfoundException;
import com.microcompany.productsservice.model.Product;
import com.microcompany.productsservice.model.StatusMessage;
import com.microcompany.productsservice.persistence.ProductsRepository;
import com.microcompany.productsservice.service.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
// @CrossOrigin(origins = {"*"}, allowedHeaders = "*")
@Tag(name = "Products API", description = "Products management APIs")
public class ProductServiceController {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceController.class);

    @Autowired
    ProductsRepository productsRepo;

    @Autowired
    ProductsService service;


    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productsRepo.findAll();
        if (products != null && !products.isEmpty()) return ResponseEntity.status(HttpStatus.OK).body(products);
        else throw new ProductNotfoundException("No hay productos");
    }

    @Operation(summary = "Get a product by id", description = "Returns a product as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The product was not found")
    })
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getProduct(
            @Parameter(name = "id", description = "Product id", example = "1") @PathVariable @Min(1) Long id
    ) {
        if (!productsRepo.existsById(id)) throw new ProductNotfoundException();
        Product product = productsRepo.findById(id).get();
        if (product != null) return new ResponseEntity<>(product, HttpStatus.OK);
        else
            return new ResponseEntity<>(new StatusMessage(HttpStatus.NOT_FOUND.value(), "No encontrado"), HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Add a new product", description = "Returns a persisted product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully created"),
            @ApiResponse(responseCode = "4XX", description = "Bad request")
    })
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Product data")
            @RequestBody @Valid Product newProduct
    ) {
        newProduct.setId(null);
        productsRepo.save(newProduct);
        if (newProduct != null && newProduct.getId() > 0) return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(new StatusMessage(HttpStatus.BAD_REQUEST.value(), "No encontrado"), HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateProduct(@PathVariable @Min(1) Long id, @RequestBody @Valid Product aProduct) {
        aProduct.setId(id);
        productsRepo.save(aProduct);
        if (aProduct != null) return new ResponseEntity(aProduct, HttpStatus.ACCEPTED);
        else
            return new ResponseEntity<>(new StatusMessage(HttpStatus.NOT_MODIFIED.value(), "No modificado"), HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteProduct(@PathVariable @Min(1) Long id) {
        productsRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/duplicate/{pid}")
    public ResponseEntity<Product> duplicate(@PathVariable Long pid) {
        return new ResponseEntity<>(service.duplicate(pid), HttpStatus.CREATED);
    }


}