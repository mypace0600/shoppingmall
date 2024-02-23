package com.yujinsoft.shoppingmall.entity;

import com.yujinsoft.shoppingmall.contract.UserRegisterRequest;
import com.yujinsoft.shoppingmall.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_user")
public class User extends BaseEntity{

    @Id
    @Column(name = "user_id")
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

    private String oauth;

    public static User createUser(UserRegisterRequest userRegisterRequest, PasswordEncoder passwordEncoder){
        User user = User.builder()
                .username(userRegisterRequest.getUsername())
                .email(userRegisterRequest.getEmail())
                .phone(userRegisterRequest.getPhone())
                .address(userRegisterRequest.getAddress())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .role(Role.ADMIN)
                .build();

        return user;
    }

}
