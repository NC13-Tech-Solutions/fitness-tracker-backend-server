package com.nc13techsolutions.fitnesstrackerbackendserver.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.JustUserCredentials;
import com.nc13techsolutions.fitnesstrackerbackendserver.models.TokenValidationRequest;
import com.nc13techsolutions.fitnesstrackerbackendserver.models.User;
import com.nc13techsolutions.fitnesstrackerbackendserver.services.JwtService;
import com.nc13techsolutions.fitnesstrackerbackendserver.services.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    public static final int USER_ID_START = 10000;

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/new")
    public int addUser(@RequestBody User user){
        int id = userService.getNewUserId() > 0 ? userService.getNewUserId() : USER_ID_START;
        return userService.addUser(userService.copyUser(id, user));
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }

    // FIXME: Need to do other user mappings
    @PostMapping("/login")
    public String checkUser(@RequestBody JustUserCredentials user){
        if(userService.checkUserCredentials(user.getUSERNAME(), user.getPASSWORD()) != null){
            return "JwtToken:" + jwtService.createJWTToken(user);
        }
        return "Invalid request!";
    }

    @PostMapping("/validate")
    public boolean validateToken(@RequestBody TokenValidationRequest tokenRequest){
        return jwtService.isJWTTokenValid(tokenRequest.getToken());
    }
}
