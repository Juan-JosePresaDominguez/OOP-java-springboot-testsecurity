package com.microcompany.productsservice.controller;

import com.microcompany.productsservice.exception.ProductNotfoundException;
import com.microcompany.productsservice.model.Product;
import com.microcompany.productsservice.model.StatusMessage;
import com.microcompany.productsservice.persistence.ProductsRepository;
import com.microcompany.productsservice.service.ProductsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/products")
@Validated
public class ProductServiceController {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceController.class);

    @Autowired
    private ProductsService service;

    @Autowired
    private ProductsRepository repo;

    // Método GET - getAll()
    //@RequestMapping(value = "", method = RequestMethod.GET)
    //@RequestMapping(value = "", method = {RequestMethod.GET,RequestMethod.DELETE}) // No es correcto.
    /*@GetMapping("")
    public List<Product> getAll() {
        //return service.getProductsByText("");
        return repo.findAll();
    }*/

    // RESPONSE
    /*@GetMapping("")
    public ResponseEntity<List<Product>> getAll() {
        //return repo.findAll();
        return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
    }*/

    // Método GET - getAll() - NO HAPPY PATH - Excepción lanzada desde Controller
    /*@GetMapping("")
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = repo.findAll();
        if (products != null && products.size() > 0) return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
        //else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else throw new ProductNotFoundException("La lista de productos está vacía");
    }*/

    // Método GET - getAll() - NO HAPPY PATH - Excepción lanzada desde Servicio
    @GetMapping("")
    public ResponseEntity<List<Product>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    // Método POST
    /*@RequestMapping(value = "", method = RequestMethod.POST)
    public Product save(@RequestBody Product newProduct) {
        logger.info("newProducto:" + newProduct);
        return repo.save(newProduct);
    }*/
    @PostMapping("")
    public ResponseEntity<Product> save(@RequestBody @Valid Product newProduct) {
        logger.info("newProducto:" + newProduct);
        return new ResponseEntity<>(repo.save(newProduct), HttpStatus.CREATED);
    }

    // Método GET - getOne()
//    @RequestMapping(value = "/1", method = RequestMethod.GET)
//    public Product getOne() {
//        return repo.findById(1L).get();
//    }

    // Método GET - getOne() que escucha una ruta concreta
    @GetMapping("/{pid}")
    public Product getOne(@PathVariable("pid") @Min(1) Long id) {
        return repo.findById(id).get();
    }

    // Método DELETE
    /*@RequestMapping(value = "/{pid}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("pid") Long id) {
        repo.deleteById(id);
    }*/
    @DeleteMapping(value = "/{pid}")
    public ResponseEntity delete(@PathVariable("pid") @Min(1) Long id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Método PUT - cuando queremos actualizar un recurso, hay que indicar el recurso que queremos actualizar
    //@RequestMapping(value = "/{pid}", method = RequestMethod.PUT)
    /*@PutMapping("/{pid}")
    public Product update(@PathVariable("pid") Long id, @RequestBody Product product) {
        if (id == product.getId()) return repo.save(product);
        else throw new RuntimeException();
    }*/
    @PutMapping("/{pid}")
    public ResponseEntity<Object> update(@PathVariable("pid") @Min(1) Long id, @RequestBody Product product) {
        if (id == product.getId())
            return new ResponseEntity<>(repo.save(product), HttpStatus.ACCEPTED);
        else {
            return new ResponseEntity<>(new StatusMessage(HttpStatus.PRECONDITION_FAILED.value(), "Id y product.id deben coincidir"), HttpStatus.PRECONDITION_FAILED);
        }
    }

    // Servicio duplicarProducto (POST)
    @PostMapping(value = "/duplicarProducto/{pid}")
    public ResponseEntity<Product> duplicate(@PathVariable @Min(1) Long pid) {
        Product currProd = repo.findById(pid).get();
        Product newProduct = new Product(null, currProd.getName(), currProd.getSerial());
        return new ResponseEntity<>(repo.save(newProduct), HttpStatus.CREATED);
    }

//    @PostMapping(value = "/duplicarProducto/{pid}")
//    public ResponseEntity<Product> duplicate(@PathVariable Long pid) {
//        return new ResponseEntity<>(service.duplicate(pid), HttpStatus.CREATED);
//    }



//    @RequestMapping("") // Para todas la peticiones que llegan a producto, va a concatenar la ruta del controller + "" ó "/"
//    public String get() {
//        return "Lista productos";
//    }

//    @RequestMapping(value = "", method = RequestMethod.GET)
//    public List<Product> getAll() {
//        return List.of(
//                new Product(1L, "estropajo", "cod-1"),
//                new Product(2L, "bayeta", "cod-2")
//        );
//    }

}