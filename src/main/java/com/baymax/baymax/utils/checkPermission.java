package com.baymax.baymax.utils;

import com.baymax.baymax.entity.AccessMethod;
import com.baymax.baymax.entity.User;
import com.baymax.baymax.repository.AccessMethodRepository;
import com.baymax.baymax.repository.UserRepository;

import java.util.List;

public class checkPermission {
    public static boolean checkPermission(String username, String method, UserRepository userRepository,
                                          AccessMethodRepository accessMethodRepository) {
        List<User> users = userRepository.findByUsername(username);
        if (!users.isEmpty()) {
            User user = users.get(0);
            AccessMethod findMethod = accessMethodRepository.findByMethod(method).get(0);
            if (user.getAccessMethods().contains(findMethod)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
