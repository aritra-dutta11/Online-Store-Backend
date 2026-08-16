package com.backend.demoBackend.model.Cart;

import org.springframework.stereotype.Component;

import com.backend.demoBackend.model.Product.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CartProduct extends Product {
    private int maxQuantity;
}
