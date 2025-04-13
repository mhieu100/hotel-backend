package com.application.vaccine_system.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.vaccine_system.exception.InvalidException;

import com.application.vaccine_system.model.User;
import com.application.vaccine_system.model.User.UserRole;
import com.application.vaccine_system.model.request.ReqRegister;
import com.application.vaccine_system.model.request.ReqUser;
import com.application.vaccine_system.model.response.Pagination;
import com.application.vaccine_system.model.response.ResUser;
import com.application.vaccine_system.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.getUserByEmail(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public ResUser convertToResUser(User user) {
        ResUser resUser = new ResUser();
        resUser.setId(user.getId());
        resUser.setFullname(user.getFullname());
        resUser.setEmail(user.getEmail());
        resUser.setAddress(user.getAddress());
        resUser.setPhone(user.getPhone());
        resUser.setRole(user.getRole());
        return resUser;
    }

    public Pagination getAllUsers(Specification<User> specification, Pageable pageable) {
        Page<User> pageUser = userRepository.findAll(specification, pageable);
        Pagination pagination = new Pagination();
        Pagination.Meta meta = new Pagination.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        pagination.setMeta(meta);

        List<ResUser> listUsers = pageUser.getContent().stream()
                .map(this::convertToResUser).collect(Collectors.toList());

        pagination.setResult(listUsers);

        return pagination;
    }

    public ResUser updateUser(Long id, ReqUser reqUser) throws InvalidException {
        Optional<User> currentUser = userRepository.findById(id);
        if (currentUser.isEmpty()) {
            throw new InvalidException("User not found with id: " + id);
        }
        currentUser.get().setFullname(reqUser.getFullname());
        currentUser.get().setPhone(reqUser.getPhone());
        currentUser.get().setAddress(reqUser.getAddress());
        return convertToResUser(userRepository.save(currentUser.get()));

    }

    public void deleteUser(Long id) throws InvalidException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new InvalidException("User not found with id: " + id);
        }
        user.get().setDeleted(true);
        userRepository.save(user.get());
    }

    public ReqRegister registerUser(ReqRegister reqRegister) throws InvalidException {
        if (userRepository.existsByEmail(reqRegister.getEmail())) {
            throw new InvalidException("Email đã tồn tài : " + reqRegister.getEmail());
        }
        String hashPassword = this.passwordEncoder.encode(reqRegister.getPassword());
        reqRegister.setPassword(hashPassword);
        reqRegister.setRole(UserRole.GUEST);

        User savedUser = new User();
        savedUser.setFullname(reqRegister.getFullname());
        savedUser.setEmail(reqRegister.getEmail());
        savedUser.setPassword(hashPassword);
        savedUser.setRole(reqRegister.getRole());
        this.userRepository.save(savedUser);

        return reqRegister;
    }

    public User getUser(Long userId) throws InvalidException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new InvalidException("User not found"));
    }
}
