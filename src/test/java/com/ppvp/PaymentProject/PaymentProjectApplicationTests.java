package com.ppvp.PaymentProject;

import com.ppvp.PaymentProject.kakaoLogin.repository.KakaoLoginRepository;
import com.ppvp.PaymentProject.order.model.OrderDetails;
import com.ppvp.PaymentProject.order.model.Orders;
import com.ppvp.PaymentProject.order.service.OrderService;
import com.ppvp.PaymentProject.products.model.Product;
import com.ppvp.PaymentProject.products.repository.ProductRepository;
import com.ppvp.PaymentProject.userModel.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PaymentProjectApplicationTests {

	@Mock
	private OrderService orderDetailService;

	@Autowired
	private  ProductRepository productRepository;

	@Autowired
	private KakaoLoginRepository kakaoLoginRepository;

	@Test
	void 상품_페이지_리밋() {
		List<Product> products = productRepository.findAll(PageRequest.of(2, 10)).getContent();
		System.out.println(products);
		Long count = productRepository.count();
		System.out.println(count);

	}

	@Test
	void contextLoads() {
		List<Product> product= productRepository.findAllByProductNameContaining("test");
		Optional<Product> productbyId = productRepository.findProductByProductId(36L);
		System.out.println(product);
		System.out.println(productbyId.get());
	}

	@Test
	void 유저아이디() {
		Optional<User> userOptional = kakaoLoginRepository.findByUserIdAndWithdrawnIsFalse("12");
		System.out.println(userOptional);
	}

}
