package com.jobApplication.token;

import com.jobApplication.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenTYpe tokenTYpe;
    private boolean expired;
    private boolean revoked;
    @ManyToOne()
    @JoinColumn(name = "user_id")//user_id is a foreign key
    private User user;
}
