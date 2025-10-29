package com.leonidshutov.learningpath_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    private SkillLevel dataStructuresLevel;

    @Enumerated(EnumType.STRING)
    private SkillLevel algorithmsLevel;

    @Enumerated(EnumType.STRING)
    private SkillLevel systemDesignLevel;
}
