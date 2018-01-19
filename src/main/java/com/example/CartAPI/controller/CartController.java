package com.example.CartAPI.controller;

import com.example.CartAPI.dto.CartDTO;
import com.example.CartAPI.dto.ProductDTO;
import com.example.CartAPI.entity.Cart;
import com.example.CartAPI.services.CartService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    public static final String URI_FOR_SEARCH = "http://10.177.7.117:8080/search/getList";

    @Autowired
    CartService cartService;
    RestTemplate restTemplate = new RestTemplate();

    //input :
    //output: all the item in the cart
    @RequestMapping(method = RequestMethod.GET, value = "/getAll")
    public ResponseEntity<?> getAll() {
        List<Cart> cart = cartService.findAll();
        ArrayList<CartDTO> cartDTOList = new ArrayList<>();
        for (Cart car : cart) {
            CartDTO cartDTO = new CartDTO();
            BeanUtils.copyProperties(car, cartDTO);
            cartDTOList.add(cartDTO);
        }
        return new ResponseEntity<>(cartDTOList, HttpStatus.OK);
    }

    //input : cartDTO
    //output: remove the product from the particular userId
    @RequestMapping(method= RequestMethod.POST, value="/remove")
    public ResponseEntity<?> delete(@RequestBody CartDTO cartDTO){
        for (Cart element : cartService.findByUserId(cartDTO.getUserId())) {
            if(element.getProductId().equals(cartDTO.getProductId()))
            {
                cartService.deletebycartId(element.getCartID());
            }
        }
        return  new ResponseEntity<>("Succesfully Deleted", HttpStatus.OK);
    }

    //input : cartDDTO
    //output : cart will be cleared for a particualr userID
    @RequestMapping(method= RequestMethod.POST, value="/clearCart")
    public ResponseEntity<?> clearCart(@RequestBody CartDTO cartDTO){
        for (Cart element : cartService.findByUserId(cartDTO.getUserId())) {
            cartService.deletebycartId(element.getCartID());
        }
        return  new ResponseEntity<>("Succesfully Deleted", HttpStatus.OK);
    }

    //input :
    //output : cartCleared
    @RequestMapping(method = RequestMethod.GET, value = "/deleteAll")
    public ResponseEntity<?> deleteAll(){
        cartService.deleteAll();
        CartDTO cartDTO = new CartDTO();
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    //input : CartDTO
    //output : "success" : "success"
    @RequestMapping(method = RequestMethod.POST, value = "/addToCart")
    public ResponseEntity<String> addToCart(@RequestBody CartDTO cartDTO){
        for (Cart element : cartService.findByUserId(cartDTO.getUserId())) {
            if(element.getProductId().equals(cartDTO.getProductId()))
            {
                element.setPurchaseUnit(element.getPurchaseUnit()+cartDTO.getPurchaseUnit());
                Cart cartCreated = cartService.addToCart(element);
                return  new ResponseEntity<>("{\"success\":\"success\"}", HttpStatus.OK);
            }
        }
        if(cartService.countbyuid(cartDTO.getUserId())>= 4)
            return new ResponseEntity<>("Error",HttpStatus.NOT_ACCEPTABLE);
        System.out.println(cartDTO);
        Cart cart = new Cart();
        BeanUtils.copyProperties(cartDTO, cart);
        Cart cartCreated = cartService.addToCart(cart);
        return  new ResponseEntity<>("{\"success\":\"success\"}", HttpStatus.OK);
    }

    //input : userId
    //output : details of product for a particular userId
    @RequestMapping(method = RequestMethod.GET, value = "/getByUid/{userId}")
    public ResponseEntity<?> getByUid(@PathVariable("userId") String userId){
        List<Cart> cartItems = cartService.findByUserId(userId);

        CartDTO cartDTO = new CartDTO();
//        BeanUtils.copyProperties(cartItems.get(0), cartDTO);
        cartDTO.setUserId(userId);

        List<String> listOfPid = new ArrayList<>();
        cartItems.forEach(cartItem ->{
            listOfPid.add(cartItem.getProductId());
        });
        String response = restTemplate.postForEntity(URI_FOR_SEARCH, listOfPid, String.class).getBody();

        List <ProductDTO> productDTOList = null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            productDTOList = mapper.readValue(response, new TypeReference<List<ProductDTO>>(){});
        }catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Integer> unitMap = new HashMap<>();
        cartItems.forEach(cart -> unitMap.put(cart.getProductId(), cart.getPurchaseUnit()));
        productDTOList.forEach(productDTO -> {
            productDTO.setpUnit(unitMap.get(productDTO.getProductId()));
        });
        ArrayList<CartDTO> cartDTOList = new ArrayList<>();
//        for (Cart car : cart) {
//            if(car.getUserId().equals(userId)){
//                for(int i=0; i<productDTOList.size(); i++){
//                    if(car.getProductId().equals(productDTOList.get(i).getProductId())){
//                        productDTOList.get(i).setpUnit(car.getPurchaseUnit());
//                    }
//                }
//            }
//        }

        cartDTO.setProductDTOList(productDTOList);
        System.out.println(cartDTO.getProductDTOList());
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }
}