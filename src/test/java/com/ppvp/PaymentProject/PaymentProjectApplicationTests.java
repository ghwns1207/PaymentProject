package com.ppvp.PaymentProject;

import com.ppvp.PaymentProject.order.model.OrderDetails;
import com.ppvp.PaymentProject.order.model.Orders;
import com.ppvp.PaymentProject.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PaymentProjectApplicationTests {

	@Mock
	private OrderService orderDetailService;

	@Test
	void testGetOrderDetailList() {

	}

	@Test
	void contextLoads() {


	}

}
