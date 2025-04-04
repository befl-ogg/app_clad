package com.spf.control.feature.token.domain.entity;

import com.spf.control.feature.token.domain.enums.TokenType;
import com.spf.control.feature.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ACCESS_TOKEN")
public class Token implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 300)
    private String token;

    @Column(unique = true, length = 36)
    private String refreshToken;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    private Boolean expiredAccessToken;

    private Boolean expiredRefreshToken;

    private Date createdDate;

    private Date expiredDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
