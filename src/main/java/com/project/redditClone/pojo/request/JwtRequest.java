package com.project.redditClone.pojo.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtRequest {
    private String userId;
    private String password;
}
