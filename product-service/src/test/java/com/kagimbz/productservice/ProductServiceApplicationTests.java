package com.kagimbz.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kagimbz.productservice.dto.AddProductRequest;
import com.kagimbz.productservice.dto.ProductResponse;
import com.kagimbz.productservice.repository.ProductRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepo productRepo;
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongodb/mongodb-community-server:latest");


	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void canAddNewProduct() throws Exception {
		AddProductRequest addProductRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(addProductRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());

		assertThat(productRepo.findAll().size()).isEqualTo(1);
	}

	@Test
	void canGetAllProducts() throws Exception {
		List<ProductResponse> productResponses = getProductResponses();
		String responseString = objectMapper.writeValueAsString(productResponses);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products"))
				.andExpect(status().isOk())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
								.andExpect(content().json(responseString));

		assertThat(productRepo.findAll().size()).isEqualTo(1);
	}

	private AddProductRequest getProductRequest() {
		return AddProductRequest.builder()
				.name("Laptop Stand")
				.description("Chrome metallic adjustable laptop stand")
				.price(BigDecimal.valueOf(960.00))
				.build();
	}

	private List<ProductResponse> getProductResponses() {
		return List.of(ProductResponse.builder()
				.id(productRepo.findAll().get(0).getId())
				.name("Laptop Stand")
				.description("Chrome metallic adjustable laptop stand")
				.price(BigDecimal.valueOf(960.00))
				.build());
	}

}
