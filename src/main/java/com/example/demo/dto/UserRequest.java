package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserRequest {

    private String username;

    private String email;

    private String password;

    private List<String> roles;

}
