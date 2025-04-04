package com.spf.control.feature.studio.domain.entity;

import com.spf.control.feature.userstudio.domain.entity.UserStudio;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "STUDIO",indexes ={
        @Index(name = "IDX_studioCode", columnList = "studioCode"),
})
public class Studio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ACTIVE",columnDefinition = "boolean default true")
    private Boolean active;

    @Column(length = 5, nullable = false)
    private String studioCode;

    @Column(length = 100)
    private String studioName;

    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserStudio> userStudios;
}
