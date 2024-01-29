package com.project.redditClone.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@Entity
@Table(name="post")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="content")
    private String content;

    @Column(name="url")
    private String url;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="community_id")
    private Community community;

    @Temporal(TemporalType.DATE)
    @Column(name="created_on")
    private Date createdOn = new Date();

    @Column(name="total_votes")
    private int totalVotes;

    @JsonIgnore
    @ManyToMany(mappedBy = "savedPosts", fetch = FetchType.LAZY)
    private Set<User> savedByUsers = new HashSet<>();
}
