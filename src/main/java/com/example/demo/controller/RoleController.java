package com.example.demo.controller;

import com.example.demo.dto.RoleRequest;
import com.example.demo.dto.RoleResponse;
import com.example.demo.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Role creation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public RoleResponse createRole(@RequestBody RoleRequest request) {
        return roleService.createRole(request);
    }

    @Operation(summary = "Role update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Role with given ID does not exist",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{roleId}")
    public RoleResponse updateRole(@PathVariable("roleId") UUID roleId, @RequestBody RoleRequest request) {
        return roleService.updateRole(roleId, request);
    }

    @Operation(summary = "Role deletion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{roleId}")
    public void deleteRole(@PathVariable("roleId") UUID roleId) {
        roleService.deleteRole(roleId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/list")
    public List<RoleResponse> getRoles() {
        return roleService.getRolesList();
    }
}
