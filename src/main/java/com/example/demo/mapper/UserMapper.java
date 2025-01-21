package com.example.demo.mapper;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "password", source = "password")
    public abstract User map(UserRequest request, String password, Set<Role> roles);

    public abstract UserResponse map(User user);

    public abstract List<String> map(Set<Role> roles);

    public abstract String map(Role role);

    public abstract List<UserResponse> map(List<User> list);
}
