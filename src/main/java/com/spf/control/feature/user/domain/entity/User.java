package com.spf.control.feature.user.domain.entity;

import com.spf.control.feature.token.domain.entity.Token;
import com.spf.control.feature.user.domain.enums.RoleType;
import com.spf.control.feature.userstudio.domain.entity.UserStudio;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS", indexes = {@Index(name = "IDX_USERS_USERNAME", columnList = "user_name")})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_NAME", nullable = false, unique = true, length = 15)
    private String userName;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "LASTNAME", nullable = false, length = 50)
    private String lastName;

    @Column(name = "EMAIL", length = 100, unique = true)
    private String email;

    @Column(name = "TEL", length = 100, unique = true)
    private String tel;

    @Column(name = "PASSWORD", length = 100, nullable = false)
    private String password;

    @Column(name = "ACTIVE",columnDefinition = "boolean default true")
    private Boolean active;

    @Column(name = "BLOCKED", columnDefinition = "boolean default false")
    private Boolean blocked;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Token> tokens;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserStudio> userStudios;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
