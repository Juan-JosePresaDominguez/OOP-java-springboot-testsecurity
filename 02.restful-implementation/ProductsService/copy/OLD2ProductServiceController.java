package com.microcompany.productsservice.controller;

import com.microcompany.productsservice.exception.ProductNotFoundException;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/products")
@Validated
@Tag(name = "Products API", description = "Products management APIs")
public class ProductServiceController {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceController.class);

    @Autowired
    private ProductsService service;

    @Autowired
    private ProductsRepository repo;


//    @RequestMapping(value = "", method = RequestMethod.GET)
    /*@GetMapping("")
    public List<Product> getAll() {
        return repo.findAll();
    }*/

    // Método GET - getAll() - NO HAPPY PATH - Excepción lanzada desde Controller
    /*@GetMapping("")
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = repo.findAll();
        if (products != null && products.size() > 0) return new ResponseEntity<>(repo.findAll(), HttpStatus.OK); // HTTP 200
        //else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else throw new ProductNotFoundException("La lista de productos está vacía"); // HTTP 404
    }*/
    // Método GET - getAll() - NO HAPPY PATH - Excepción lanzada desde Servicio
    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = repo.findAll();
        if (products != null && !products.isEmpty()) return ResponseEntity.status(HttpStatus.OK).body(products);
        else throw new ProductNotFoundException("No hay productos");
    }

    /*@RequestMapping(value = "", method = RequestMethod.POST)
    public Product save(@RequestBody Product newProduct) {
        logger.info("newProduct:" + newProduct);
        return repo.save(newProduct);
    }*/

    //    @RequestMapping(value = "", method = RequestMethod.POST)
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Product> save(@RequestBody @Valid Product newProduct) {
        logger.info("newProduct:" + newProduct);
        newProduct.setId(null);
        return new ResponseEntity<>(repo.save(newProduct), HttpStatus.CREATED);
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
        repo.save(newProduct);
        if (newProduct != null && newProduct.getId() > 0) return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(new StatusMessage(HttpStatus.BAD_REQUEST.value(), "No encontrado"), HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Get a product by id", description = "Returns a product as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The product was not found")
    })
    @RequestMapping(value = "/{pid}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Product getOne(
            @Parameter(name = "id", description = "Product id", example = "1", required = true)
            @PathVariable("pid") @Min(1) Long id
    ) {
        return repo.findById(id).get();
    }

    @RequestMapping(value = "/{pid}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("pid") @Min(1) Long id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //    @RequestMapping(value = "/{pid}", method = RequestMethod.PUT)
    @PutMapping(value = "/{pid}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> update(@PathVariable("pid") @Min(1) Long id, @RequestBody Product product) {
        if (id == product.getId()) {
            return new ResponseEntity<>(repo.save(product), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new StatusMessage(HttpStatus.PRECONDITION_FAILED.value(), "Id y produt.id deben coincidir"), HttpStatus.PRECONDITION_FAILED);
        }
    }

    @PostMapping(value = "/duplicarProducto/{pid}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Product> duplicate(@PathVariable @Min(1) Long pid) {
        return new ResponseEntity<>(service.duplicate(pid), HttpStatus.CREATED);
    }

}
