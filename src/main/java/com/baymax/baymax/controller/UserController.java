package com.baymax.baymax.controller;

import com.baymax.baymax.entity.Department;
import com.baymax.baymax.entity.User;
import com.baymax.baymax.repository.DepartmentRepository;
import com.baymax.baymax.repository.UserRepository;
import com.baymax.baymax.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @PostMapping("/user")
    public String saveUser(@RequestBody User user) {
        String hashedPass = SecurityUtils.hashPassword(user.getPassword());
        user.setPassword(hashedPass);
        userRepository.save(user);
        return "Success";
    }
    @PostMapping("/user/login")
    public User login(@RequestBody User user) {
        User ret = new User();
        if (user.getUsername() != null && user.getPassword() != null) {
            final String hashed = SecurityUtils.hashPassword(user.getPassword());
            final List<User> users = userRepository.findByUsernameAndPassword(user.getUsername(), hashed);
            if (users.size() >0) ret = users.get(0);
        }
        return ret;
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable(name = "id") Long userId) {

        return userRepository.findById(userId).get();
    }

    @DeleteMapping("/user/{id}")
    void deleteUser(@PathVariable long id) {

        userRepository.delete(userRepository.getOne(id));
    }

    @PutMapping("/user/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable long id) {
        return userRepository.findById(id).map(user -> { // update when id found
            user.setUsername(newUser.getUsername());
            user.setRole(newUser.getRole());
            return userRepository.save(user);
        }) // store new record
                .orElseGet(()-> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
    }

    @PostMapping("/department")
    public String saveDepartment(@RequestBody Department department) {
        departmentRepository.save(department);
        return "Success";
    }

    @GetMapping("/department")
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @PutMapping("/department/{id}")
    Department updateDepartment(@RequestBody Department newDepartment, @PathVariable long id) {
        return departmentRepository.findById(id).map(department -> { // update when id found
            department.setDepartment(newDepartment.getDepartment());
            department.setHospital(newDepartment.getHospital());
            department.setSymptom(newDepartment.getSymptom());
            department.setRanking(newDepartment.getRanking());
            department.setInsuranceStatus(newDepartment.getInsuranceStatus());
            department.setPrice(newDepartment.getPrice());
            department.setLocation(newDepartment.getLocation());

            return departmentRepository.save(department);
        }) // store new record
                .orElseGet(()-> {
                    newDepartment.setId(id);
                    return departmentRepository.save(newDepartment);
                });
    }

    @GetMapping("/department/recommender") // recommends the highest-ranking departments
    public List<Department> findDepartmentsBySymptoms(@RequestBody Department department){
        List<Department> recommendedDepartments = new ArrayList<Department>();
        recommendedDepartments = departmentRepository.findBySymptomAndRanking(department.getSymptom(),
                Long.valueOf(5));
        return recommendedDepartments;
    }

    @GetMapping("/department/recommender/insurance") // recommend hospitals filtering by Bao Minh Insurance
    public List<Department> findDepartmentsByInsurance(@RequestBody Department department){
        List<Department> recommendedDepartmentsBySymptoms = findDepartmentsBySymptoms(department);
        List<Department> recommendedDepartmentsByInsurance = new ArrayList<Department>();
        for (int i = 0; i < recommendedDepartmentsBySymptoms.size(); i++){
            if (recommendedDepartmentsBySymptoms.get(i).getInsuranceStatus() == 1){
                recommendedDepartmentsByInsurance.add(recommendedDepartmentsBySymptoms.get(i));
            }
        }
        return recommendedDepartmentsByInsurance;
    }

    @GetMapping("/department/recommender/price/{price}") // recommend hospitals filtering by price
    public List<Department> findDepartmentsByPrice(@RequestBody Department department, @PathVariable long price) {
        List<Department> recommendedDepartmentsBySymptoms = findDepartmentsBySymptoms(department);
        List<Department> recommendedDepartmentsByPrice = new ArrayList<Department>();
        for (int i = 0; i < recommendedDepartmentsBySymptoms.size(); i++) {
            if (recommendedDepartmentsBySymptoms.get(i).getPrice() <= price) {
                recommendedDepartmentsByPrice.add(recommendedDepartmentsBySymptoms.get(i));
            }
        }
        return recommendedDepartmentsByPrice;
    }

    @GetMapping("/department/recommender/location/{location}") // recommend hospitals filtering by location
    public List<Department> findDepartmentsByLocation(@RequestBody Department department, @PathVariable String location) {
        List<Department> recommendedDepartments = new ArrayList<Department>();
        recommendedDepartments = departmentRepository.findBySymptomAndLocation(department.getSymptom(), location);
        return recommendedDepartments;
    }
}
