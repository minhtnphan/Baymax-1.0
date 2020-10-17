package com.baymax.baymax.controller;

import com.baymax.baymax.entity.AccessMethod;
import com.baymax.baymax.entity.User;
import com.baymax.baymax.repository.AccessMethodRepository;
import com.baymax.baymax.utils.RoleUtils;
import com.baymax.baymax.utils.checkPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import com.baymax.baymax.repository.UserRepository;

@RestController
public class MethodController {

    @Autowired
    private AccessMethodRepository accessMethodRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{username}/method/{id}")
    public AccessMethod getAccessMethod(@PathVariable(name = "id") Long methodId,
                                        @PathVariable(name = "username") String username) {
        if (!checkPermission.checkPermission
                (username, "method", userRepository, accessMethodRepository)) {
            System.out.println("No permission to view methods");
            return new AccessMethod();
        }
        if (!accessMethodRepository.existsById(methodId)) {
            System.out.println("no method found");
            return new AccessMethod();
        }
        return accessMethodRepository.findById(methodId).get();

    }

    @GetMapping("/{username}/method")
    public List<AccessMethod> getAllAccessMethod(@RequestBody User user,
                                                 @PathVariable(name = "username") String username) {
        if (!checkPermission.checkPermission
                (username, "method", userRepository, accessMethodRepository)) {
            System.out.println("No permission to view methods");
            return new ArrayList<>();
        }
        return accessMethodRepository.findAll();
    }

    //create access methods before launching
    @PostMapping("/method/create")
    public void saveAccessMethod() {
//        if (!checkPermission.checkPermission
//            (username, "method", userRepository, accessMethodRepository)) {
//            System.out.println("No permission to view methods");
//            return;
//        }
        for (String per: RoleUtils.ADMIN_PERMISSIONS) {
            AccessMethod adminMethod = new AccessMethod(per, new HashSet<>());
            accessMethodRepository.save(adminMethod);
        }
        for (String per: RoleUtils.USER_PERMISSIONS) {
            AccessMethod userMethod = new AccessMethod(per, new HashSet<>());
            accessMethodRepository.save(userMethod);
        }
        System.out.println("Success");
        return;

    }

    @DeleteMapping("/{username}/method/{id}")
    void delete(@PathVariable(name = "id") Long id,
                @PathVariable(name = "username") String username) {
        if (!checkPermission.checkPermission
                (username, "method", userRepository, accessMethodRepository)) {
            System.out.println("No permission to view methods");
            return;
        }
        accessMethodRepository.delete(accessMethodRepository.getOne(id));
    }

    @PutMapping("/{username}/method/{id}")
    AccessMethod updateAccessMethod(@RequestBody AccessMethod newMethod,
                                    @PathVariable(name = "id") Long id,
                                    @PathVariable(name = "username") String username) {
        if (!checkPermission.checkPermission
                (username, "method", userRepository, accessMethodRepository)) {
            System.out.println("No permission to view methods");
            return new AccessMethod();
        }
        return accessMethodRepository.findById(id)
                .map(accessMethod -> {
                    accessMethod.setMethod(newMethod.getMethod());
                    return accessMethodRepository.save(accessMethod);
                })
                .orElseGet(()-> {
                    newMethod.setId(id);
                    return accessMethodRepository.save(newMethod);
                });
    }


}
