package com.example.chatping.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignUpRequest {
    private String email;
    private String password;
    private String name;
    private String nickname;
}
