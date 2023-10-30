package com.nc13techsolutions.fitnesstrackerbackendserver.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.User;

@Repository("redis user")
@EnableAutoConfiguration
public class RedisUserRepo implements UserRepo {

    public static final String USER_HASH_KEY = "Users";
    @Autowired
    private RedisTemplate<String, User> template;

    @Override
    public int insertUser(User user) {
        int result = checkIfUserExists(user);
        if (result == -1) {
            HashOperations<String, Integer, User> ho = template.opsForHash();
            ho.put(USER_HASH_KEY, user.getUserId(), user);

            return 1;
        } else if (result == 1) {
            return 0;
        }

        return -1;
    }

    @Override
    public List<User> getUsers() {
        HashOperations<String, Integer, User> ho = template.opsForHash();
        return ho.values(USER_HASH_KEY);
    }

    @Override
    public User getUserById(int userId) {
        HashOperations<String, Integer, User> ho = template.opsForHash();
        return ho.get(USER_HASH_KEY, userId);
    }

    @Override
    public User checkUserCredentials(String username, String password) {
        HashOperations<String, Integer, User> ho = template.opsForHash();
        Map<Integer, User> m = ho.entries(USER_HASH_KEY);

        for (Map.Entry<Integer, User> me : m.entrySet()) {
            if (username.trim().equals(me.getValue().getUsername()) &&
                    password.trim().equals(me.getValue().getPassword())) {
                return me.getValue();
            }
        }
        return null;
    }

    @Override
    public int updateUserPassword(User user, String newPassword) {
        int result = checkIfUserExists(user);
        if (result == 2) {
            HashOperations<String, Integer, User> ho = template.opsForHash();
            User temp = user;
            temp.setPassword(newPassword);
            ho.put(USER_HASH_KEY, user.getUserId(), temp);

            return 1;
        } else if (result == -1) {
            return 0;
        }
        return -1;
    }

    @Override
    public int deleteUser(int userId) {
        // TODO: Have to check if userId is present in Days
        int result = checkIfUserExists(new User(userId, "", "", USER, "", ""));
        if (result == 0) {
            HashOperations<String, Integer, User> ho = template.opsForHash();
            ho.delete(USER_HASH_KEY, userId);
            return 1;
        }
        return -1;
    }

    @Override
    public int checkIfUserExists(User user) {
        int result = -1;
        HashOperations<String, Integer, User> ho = template.opsForHash();
        Map<Integer, User> m = ho.entries(USER_HASH_KEY);

        for (Map.Entry<Integer, User> me : m.entrySet()) {
            if (me.getValue().getUsername().trim().equalsIgnoreCase(user.getUsername().trim())) {
                if (me.getValue().getUserId() == user.getUserId()) {
                    return 2;
                }
                return 1;
            } else if (me.getValue().getUserId() == user.getUserId()) {
                result = 0;
            }
        }

        return result;
    }

    @Override
    public int findHeighestUserId() {
        int result = 0;
        HashOperations<String, Integer, User> ho = template.opsForHash();
        Map<Integer, User> m = ho.entries(USER_HASH_KEY);

        for (Map.Entry<Integer, User> me : m.entrySet()) {
            if (me.getValue().getUserId() >= result) {
                result = me.getValue().getUserId() + 1;
            }
        }

        return result;
    }

}
