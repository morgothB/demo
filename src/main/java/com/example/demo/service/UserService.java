package com.example.demo.service;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.DataIntegrityException;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserRequest request) {
        var preExistingUsers = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail());
        if (!preExistingUsers.isEmpty()) {
            throw new DataIntegrityException("User with given username or email already exists");
        }
        Set<Role> roles = roleRepository.findByNameIn(request.getRoles());
        var newRoles = roleMapper.mapRoleNamesToRoles(request.getRoles());
        newRoles.removeAll(roles);
        newRoles = roleRepository.saveAll(newRoles);
        roles.addAll(newRoles);
        var user = userRepository.save(userMapper.map(request, passwordEncoder.encode(request.getPassword()), new HashSet<>(roles)));
        return userMapper.map(user);
    }


    public UserResponse updateUser(String userID, UserRequest request) {
        User user = userRepository.findById(UUID.fromString(userID)).orElseThrow(() -> new DataIntegrityException("User with given ID does not exist"));
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        Set<Role> roles = roleRepository.findByNameIn(request.getRoles());
        user.setRoles(roles);

        user = userRepository.save(user);
        return userMapper.map(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(UUID.fromString(userId));
    }

    public List<UserResponse> getUsers(String userName, String roleName, Integer page, Integer size) {
        Specification<User> spec = Specification.where(null);
        spec = hasUserName(spec, userName);
        spec = hasRoleName(spec, roleName);
        Pageable pageable = PageRequest.of(page, size);
        var users = userRepository.findAll(spec, pageable);
        return userMapper.map(users.get().toList());
    }

    private Specification<User> hasUserName(Specification<User> spec, String userName) {
        if (userName == null) return spec;
        return spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userName"), userName));
    }

    private Specification<User> hasRoleName(Specification<User> spec, String roleName) {
        if (roleName == null) return spec;
        return spec.and((root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.literal(roleName).in(root.get("role").get("name"));
        });
    }

   /* public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
    }

    // Get user authorities
    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return authorities;
    }*/
}
