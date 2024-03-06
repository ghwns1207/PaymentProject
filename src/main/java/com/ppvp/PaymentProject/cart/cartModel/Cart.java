package com.ppvp.PaymentProject.cart.cartModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ppvp.PaymentProject.userModel.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
public class Cart {

  @Id
  private String id;

  @OneToOne
  @JsonIgnore
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

}
