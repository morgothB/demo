package com.example.demo.mapper;

import com.example.demo.dto.RoleRequest;
import com.example.demo.dto.RoleResponse;
import com.example.demo.entity.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class RoleMapper {

    public abstract Role map(RoleRequest request);

    protected abstract Role mapRole(String name);

    public abstract RoleResponse map(Role role);

    public abstract List<RoleResponse> map(List<Role> roles);

    public abstract List<Role> mapRoleNamesToRoles(List<String> names);

}
