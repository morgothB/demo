package com.example.demo.controller;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "User creation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @Operation(summary = "User update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "User with given ID, username or email does not exist",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{userId}")
    public UserResponse updateUser(@PathVariable("userId") String userID, @RequestBody UserRequest request) {
        return userService.updateUser(userID, request);
    }

    @Operation(summary = "Role deletion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/search")
    public List<UserResponse> getUsers(@RequestParam(value = "userName", required = false) String userName,
                                       @RequestParam(value = "roleName", required = false) String roleName,
                                       @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return userService.getUsers(userName, roleName, page, pageSize);
    }
}
