package com.project.redditClone.pojo.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String JwtToken;
    private String UserId;
}
