package com.sanjogwiz.state8;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateOrderAndReduceStock() throws Exception {
        String createOrderBody = """
                {
                  "customerId": 1,
                  "items": [
                    { "productId": 1, "quantity": 2 }
                  ]
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createOrderBody))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode orderResponse = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(orderResponse.get("orderId").asLong()).isGreaterThan(0L);
        assertThat(orderResponse.get("totalAmount").asText()).isNotBlank();

        MvcResult productResult = mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode productResponse = objectMapper.readTree(productResult.getResponse().getContentAsString());
        assertThat(productResponse.get("stockQuantity").asInt()).isEqualTo(48);
    }
}

