package com.leonidshutov.learningpath_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "resources")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillLevel skillLevel;

    @Column(nullable = false)
    private String url;

    private int estimatedTimeMinutes;

    private int baseEffectivenessScore; // From 1 to 10

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "resource_topic_tags", joinColumns = @JoinColumn(name = "resource_id"))
    @Column(name = "tag")
    private List<String> topicTags;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "resource_prerequisites", joinColumns = @JoinColumn(name = "resource_id"))
    @Column(name = "prerequisite_id")
    private List<Long> prerequisites;
}
