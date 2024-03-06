package com.ppvp.PaymentProject.kakaoLogin.repository;


import com.ppvp.PaymentProject.userModel.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface KakaoLoginRepository extends JpaRepository<User, Long> {

  Optional<User> findByIdAndWithdrawnIsFalse (Long id);
  Optional<User> findByUserIdAndWithdrawnIsFalse(Long userId);

  User save(User user);

  Integer deleteByUserId(Long userId);

}
