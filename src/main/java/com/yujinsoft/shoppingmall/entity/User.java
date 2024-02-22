package com.yujinsoft.shoppingmall.entity;

import com.yujinsoft.shoppingmall.contract.UserRegister;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 30)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable=false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    private LocalDateTime regDt;

    @CreationTimestamp
    private LocalDateTime updateDt;

    private String oauth;

    public static User createUser(UserRegister userRegister, PasswordEncoder passwordEncoder){
        User user = User.builder()
                .username(userRegister.getUsername())
                .email(userRegister.getEmail())
                .phone(userRegister.getPhone())
                .address(userRegister.getAddress())
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .role(Role.ADMIN)
                .build();

        return user;
    }

}
