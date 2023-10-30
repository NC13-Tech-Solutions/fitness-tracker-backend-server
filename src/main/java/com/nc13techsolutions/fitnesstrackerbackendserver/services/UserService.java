package com.nc13techsolutions.fitnesstrackerbackendserver.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.User;
import com.nc13techsolutions.fitnesstrackerbackendserver.repositories.UserRepo;

@Service
public class UserService {
    private final UserRepo userRepo;
    InMemoryUserDetailsManager imudm = new InMemoryUserDetailsManager();

    public UserService(@Qualifier("redis user") UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public int addUser(User user) {
        int result = userRepo.insertUser(user);
        if (result == 1) {
            // Since new user is added, we will have to add it to the InMemoryUserDetailsManager
            imudm.createUser(user);
        }
        return result;
    }

    public List<User> getAllUsers() {
        return userRepo.getUsers();
    }

    public User getUserById(int userId) {
        return userRepo.getUserById(userId);
    }

    public User checkUserCredentials(String username, String password) {
        return userRepo.checkUserCredentials(username, password);
    }

    public int updateUserPassword(User user, String newPassword) {
        int result = userRepo.updateUserPassword(user, newPassword);
        if(result == 1){
            // Since the password for user is changed, we need to update it in InMemoryUserDetailsManager
            imudm.updatePassword(user, newPassword);
        }
        return result;
    }

    public int deleteUser(int userId) {
        User user = getUserById(userId);
        int result = userRepo.deleteUser(userId);
        if(result == 1){
            // Since the user is removed, we need to delete it from InMemoryUserDetailsManager
            imudm.deleteUser(user.getUsername());
        }
        return result;
    }

    public int getNewUserId() {
        return userRepo.findHeighestUserId();
    }

    public User copyUser(int userId, User user) {
        return new User(userId,
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getDeviceID(),
                user.getDeviceName());
    }
}
