package com.ecommerce.project;

import com.ecommerce.project.payload.*;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Cookie jwtCookie;
    private static Long categoryId;
    private static Long productId;
    private static Long addressId;

    @Test
    @Order(1)
    public void testAuthFlow() throws Exception {
        // 1. Signup
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUserName("testuser_full");
        signUpRequest.setEmail("testuser_full@example.com");
        signUpRequest.setPassword("password123");
        
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk());

        // 2. Signin
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("testuser_full");
        loginRequest.setPassword("password123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser_full"))
                .andReturn();

        // Extract JWT cookie
        String setCookie = loginResult.getResponse().getHeader("Set-Cookie");
        if (setCookie != null) {
            String value = setCookie.split(";")[0].split("=")[1];
            jwtCookie = new Cookie("bezkoder-jwt", value);
        }
    }

    @Test
    @Order(2)
    public void testCategoryFlow() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("Mobiles");

        MvcResult result = mockMvc.perform(post("/api/public/categories")
                .cookie(jwtCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDTO savedCategory = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryDTO.class);
        categoryId = savedCategory.getCategoryId();
    }

    @Test
    @Order(3)
    public void testProductFlow() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductName("iPhone 15");
        productDTO.setDescription("Latest iPhone");
        productDTO.setPrice(1000.0);
        productDTO.setDiscount(10.0);
        productDTO.setQuantity(50);

        MvcResult result = mockMvc.perform(post("/api/admin/categories/" + categoryId + "/products")
                .cookie(jwtCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        // Since it returns a String " added", we need to find the product ID by listing
        MvcResult listResult = mockMvc.perform(get("/api/admin/categories/products")
                .cookie(jwtCookie))
                .andExpect(status().isOk())
                .andReturn();
        
        ProductResponse productResponse = objectMapper.readValue(listResult.getResponse().getContentAsString(), ProductResponse.class);
        productId = productResponse.getContent().get(0).getId();
    }

    @Test
    @Order(4)
    public void testAddressFlow() throws Exception {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("MG Road");
        addressDTO.setBuildingName("Empire Heights");
        addressDTO.setCity("Bangalore");
        addressDTO.setState("Karnataka");
        addressDTO.setCountry("India");
        addressDTO.setPincode("560001");

        MvcResult result = mockMvc.perform(post("/api/address")
                .cookie(jwtCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        AddressDTO savedAddress = objectMapper.readValue(result.getResponse().getContentAsString(), AddressDTO.class);
        addressId = savedAddress.getAddressId();
    }

    @Test
    @Order(5)
    public void testCartFlow() throws Exception {
        mockMvc.perform(post("/api/carts/products/" + productId + "/quantity/1")
                .cookie(jwtCookie))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.product[0].productName").value("iPhone 15"));
    }

    @Test
    @Order(6)
    public void testOrderFlow() throws Exception {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setAddressId(addressId);
        orderRequestDTO.setPgName("Razorpay");
        orderRequestDTO.setPgPaymentId("pay_12345");
        orderRequestDTO.setPgStatus("SUCCESS");
        orderRequestDTO.setPgResponseMessage("Payment Successful");

        mockMvc.perform(post("/api/order/user/payment/STRIPE")
                .cookie(jwtCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("Order Accepted!"))
                .andExpect(jsonPath("$.totalAmount").value(900.0)); // 1000 - 10% discount
    }

    @Test
    @Order(7)
    public void testManagementOperations() throws Exception {
        // 1. Update Category
        CategoryDTO updateCategory = new CategoryDTO();
        updateCategory.setCategoryName("Laptops");
        mockMvc.perform(put("/api/admin/categories/" + categoryId)
                .cookie(jwtCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Laptops"));

        // 2. Search Product by Keyword
        mockMvc.perform(get("/api/products/iPhone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].productName").value("iPhone 15"));

        // 3. Update Address
        AddressDTO updateAddress = new AddressDTO();
        updateAddress.setStreet("Indiranagar");
        updateAddress.setBuildingName("Empire Heights Updated");
        updateAddress.setCity("Bangalore");
        updateAddress.setState("Karnataka");
        updateAddress.setCountry("India");
        updateAddress.setPincode("560038");
        mockMvc.perform(put("/api/address/" + addressId)
                .cookie(jwtCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAddress)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Indiranagar"));

        // 4. Delete Product (using a new product to avoid constraint violation with orders)
        ProductDTO tempProduct = new ProductDTO();
        tempProduct.setProductName("Temp Product");
        tempProduct.setDescription("To be deleted");
        tempProduct.setPrice(10.0);
        tempProduct.setDiscount(0.0);
        tempProduct.setQuantity(1);

        MvcResult tempResult = mockMvc.perform(post("/api/admin/categories/" + categoryId + "/products")
                .cookie(jwtCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tempProduct)))
                .andExpect(status().isCreated())
                .andReturn();

        // Get the list again to find the temp product ID (since addProduct returns " added")
        MvcResult listResultAfterTemp = mockMvc.perform(get("/api/admin/categories/products")
                .cookie(jwtCookie))
                .andExpect(status().isOk())
                .andReturn();
        ProductResponse productResponseAfterTemp = objectMapper.readValue(listResultAfterTemp.getResponse().getContentAsString(), ProductResponse.class);
        Long tempProductId = productResponseAfterTemp.getContent().stream()
                .filter(p -> p.getProductName().equals("Temp Product"))
                .findFirst().get().getId();

        mockMvc.perform(delete("/api/admin/products/" + tempProductId)
                .cookie(jwtCookie))
                .andExpect(status().isOk());
    }
}
