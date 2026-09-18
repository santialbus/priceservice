package com.santi.priceservice.priceservice.driving.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PriceControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @ParameterizedTest(name = "Test {index}: fecha {0} -> tarifa {3}, precio {4}€")
    @CsvSource({
            "2020-06-14T10:00:00, 35455, 1, 1, 35.50",
            "2020-06-14T16:00:00, 35455, 1, 2, 25.45",
            "2020-06-14T21:00:00, 35455, 1, 1, 35.50",
            "2020-06-15T10:00:00, 35455, 1, 3, 30.50",
            "2020-06-16T21:00:00, 35455, 1, 4, 38.95"
    })
    @DisplayName("Debe retornar la tarifa correcta según la prioridad y fecha de aplicación")
    void shouldReturnCorrectPriceForGivenDates(
            String date, Long productId, Long brandId, Integer expectedPriceList, Double expectedPrice) throws Exception {

        getPrices(date, productId, brandId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.brandId").value(brandId))
                .andExpect(jsonPath("$.priceList").value(expectedPriceList))
                .andExpect(jsonPath("$.price").value(expectedPrice))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @DisplayName("Debe retornar 404 si no se encuentra un precio aplicable")
    void shouldReturn404WhenPriceNotFound() throws Exception {
        getPrices("2020-06-14T10:00:00", 99999L, 1L)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    private ResultActions getPrices(String date, Long productId, Long brandId) throws Exception {
        return mockMvc.perform(get("/prices")
                .param("applicationDate", date)
                .param("productId", String.valueOf(productId))
                .param("brandId", String.valueOf(brandId)));
    }
}