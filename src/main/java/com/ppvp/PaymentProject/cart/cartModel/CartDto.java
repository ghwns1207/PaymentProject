package com.ppvp.PaymentProject.cart.cartModel;

import com.ppvp.PaymentProject.userModel.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
public class CartDto {

  @Id
  private String id;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

//  @OneToMany(mappedBy = "cartDto")
//  private List<CartItemDto> cartItemDto = new ArrayList<>();

}
