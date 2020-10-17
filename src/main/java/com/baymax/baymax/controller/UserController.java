package com.baymax.baymax.controller;

import com.baymax.baymax.entity.AccessMethod;
import com.baymax.baymax.entity.Department;
import com.baymax.baymax.entity.User;
import com.baymax.baymax.repository.AccessMethodRepository;
import com.baymax.baymax.repository.DepartmentRepository;
import com.baymax.baymax.repository.UserRepository;
import com.baymax.baymax.utils.RoleUtils;
import com.baymax.baymax.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.baymax.baymax.utils.checkPermission.checkPermission;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private AccessMethodRepository accessMethodRepository;


    @PostMapping("/user")
    public String registerUser(@RequestBody User user) {
        if (user.getRole() == null || user.getPassword() == null) {
            return "Missing information";
        }
        if (userRepository.findByUsername(user.getUsername()).size() > 0) {
            return "User already exist";
        }
        List<String> permission = new ArrayList<>();
        if (user.getRole().equals("admin")) {
            permission = RoleUtils.ADMIN_PERMISSIONS;
            for (String per : permission) {
                long id = accessMethodRepository.findByMethod(per).get(0).getId();
                    user.addAccessMethod(accessMethodRepository.getOne(id));
                }
            }
        if (user.getRole().equals("user")) {
            permission = RoleUtils.USER_PERMISSIONS;
            for (String per : permission) {
                long id = accessMethodRepository.findByMethod(per).get(0).getId();
                user.addAccessMethod(accessMethodRepository.getOne(id));
            }
        }
        else if (user.getRole() == null) {
            return "No role declared";
        }
            String hashedPass = SecurityUtils.hashPassword(user.getPassword());
            user.setPassword(hashedPass);
            userRepository.save(user);
            return "Success";
    }

    @PostMapping("{username}/department")
    public String saveDepartment(@RequestBody Department department,
                                 @PathVariable(name = "username") String username) {
        boolean validate = checkPermission(username, "save",
                            userRepository, accessMethodRepository);
        if (validate == Boolean.FALSE) {
            return "failed";
        }
        departmentRepository.save(department);
        return "Success";
    }
    @PostMapping("/user/login")
    public User login(@RequestBody User user) {
        User ret = new User();
        if (user.getUsername() != null && user.getPassword() != null) {
            final String hashed = SecurityUtils.hashPassword(user.getPassword());
            final List<User> users = userRepository.
                                    findByUsernameAndPassword(user.getUsername(), hashed);
            if (users.size() >0) ret = users.get(0);
        }
        return ret;
    }
    @GetMapping("/department/recommender")
    public List<Department> findDepartments(@RequestBody Department department){
        List<Department> recommendedDepartments = new ArrayList<Department>();
        recommendedDepartments = departmentRepository.
                                findBySymptomAndRanking(department.getSymptom(),
                Long.valueOf(5));
        return recommendedDepartments;
    }
    @GetMapping("/{username}/user")
    public List<User> getAllUsers(@PathVariable String username) {
        boolean validate = checkPermission(username, "getUser",
                            userRepository, accessMethodRepository);
        if (validate == Boolean.FALSE) {
            return new ArrayList<>();
        }
        return userRepository.findAll();
    }
    @GetMapping("/department")
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    @GetMapping("/{username}/{userId}")
    public User getUser(@PathVariable(name = "userId") Long userId,
                        @PathVariable(name = "username") String username) {
        if (checkPermission(username, "getUser",
                userRepository, accessMethodRepository)
                == Boolean.FALSE) {
            return new User();
        }
        if (userRepository.findById(userId).isEmpty()) {
            return new User();
        }
        return userRepository.findById(userId).get();
    }
    @DeleteMapping("/{username}/user/{id}")
    void deleteUser(@PathVariable(name = "id") long id,
                    @PathVariable(name = "username") String username) {
        if (checkPermission(username, "deleteUser",
                userRepository, accessMethodRepository) == Boolean.FALSE) {
            System.out.println("No permission to delete user");
            return;
        }
        userRepository.delete(userRepository.getOne(id));
    }
    @PutMapping("/{username}/user/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable(name = "id") long id,
                    @PathVariable(name = "username") String username) {
        if (checkPermission(username, "update",
                userRepository, accessMethodRepository) == Boolean.FALSE) {
            System.out.println("No permission to delete user");
            return new User();
        }
        return userRepository.findById(id).map(user -> {
            user.setUsername(newUser.getUsername());
            user.setSymptom(newUser.getSymptom());
            user.setPassword(newUser.getPassword());
            return userRepository.save(user);
        })
                .orElseGet(()-> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
    }


}
