package com.project.redditClone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="community")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name="created_on")
    private Date createdOn = new Date();

    @ManyToOne
    @JoinColumn(name="created_by")
    private User createdBy;

    @JsonIgnore
    @ManyToMany(mappedBy = "joinedCommunities", fetch = FetchType.LAZY)
    private Set<User> joinedByUsers = new HashSet<>();
}
