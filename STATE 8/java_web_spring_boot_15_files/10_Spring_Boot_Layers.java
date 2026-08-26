/*
 * 10_Spring_Boot_Layers.java
 *
 * Typical Spring Boot layered architecture:
 *
 * Controller -> Service -> Repository -> Database
 *
 * Dependency Injection is preferred over manually constructing dependencies.
 */
package com.example.springboot;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    List<User> findAll() {
        return service.findAll();
    }

    @PostMapping
    User create(@RequestBody User user) {
        return service.create(user);
    }
}

@Service
class UserService {

    private final UserRepository repository;

    UserService(UserRepository repository) {
        this.repository = repository;
    }

    List<User> findAll() {
        return repository.findAll();
    }

    User create(User user) {
        if (user.name() == null || user.name().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        return repository.save(user);
    }
}

@Repository
class UserRepository {

    private final List<User> users = new java.util.ArrayList<>();

    List<User> findAll() {
        return List.copyOf(users);
    }

    User save(User user) {
        users.add(user);
        return user;
    }
}

record User(String name, String email) {}

/*
 * In a real application, UserRepository would commonly be a Spring Data
 * repository connected to a database.
 */
