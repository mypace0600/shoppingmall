package com.yujinsoft.shoppingmall.service;

import com.yujinsoft.shoppingmall.entity.User;
import com.yujinsoft.shoppingmall.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private void validateDuplicateUser(User user){
        Boolean duplicationCheck = userRepository.findByEmail(user.getEmail()).isPresent();
        if(duplicationCheck){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    public User register(User user) {
        validateDuplicateUser(user);
        return userRepository.save(user);
    }
}
