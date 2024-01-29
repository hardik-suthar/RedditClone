package com.project.redditClone.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUser {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
}
