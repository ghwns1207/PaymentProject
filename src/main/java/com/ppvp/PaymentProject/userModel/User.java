package com.ppvp.PaymentProject.userModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "user_id", nullable = false )
  private Long userId;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "image")
  private String image;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(name = "is_withdrawn", nullable = false)
  private boolean withdrawn;

  @Column(name = "is_suspended" , nullable = false)
  private boolean suspended;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_role_name", referencedColumnName = "role_name", nullable = false) // UserRole과 연동하는 컬럼
  private UserRole role; // UserRole을 참조하는 필드

  @Column(name = "withdrawn_at")
  private String withdrawnAt;


}
