package com.baymax.baymax.utils;

import com.baymax.baymax.entity.AccessMethod;
import com.baymax.baymax.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.baymax.baymax.repository.UserRepository;
import com.baymax.baymax.repository.AccessMethodRepository;

public class RoleUtils {
    private UserRepository userRepository;
    private AccessMethodRepository accessMethodRepository;
    public static final List<String> ADMIN_PERMISSIONS = Arrays.asList("save", "update", "getUser",
                                                                        "deleteUser", "method");
    public static final List<String> USER_PERMISSIONS = Stream.of("read").collect(Collectors.toList());

    public static List<String> getPermissionFromRole(String role) {
        List<String> permission = new ArrayList<>();

        if (role.equals("admin")) {
            permission = ADMIN_PERMISSIONS;
        }
        else if (role.equals("user")) {
            permission = USER_PERMISSIONS;

        }
        return permission;
    }

}
