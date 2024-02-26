package com.ppvp.PaymentProject.kakaoLogin.repository;


import com.ppvp.PaymentProject.userModel.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface UserRoleRepository extends JpaRepository<UserRole , Long> {

  Optional<UserRole> findById(Long id);


}
