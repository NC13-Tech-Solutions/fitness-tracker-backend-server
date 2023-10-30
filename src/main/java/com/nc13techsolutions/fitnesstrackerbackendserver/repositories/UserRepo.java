package com.nc13techsolutions.fitnesstrackerbackendserver.repositories;

import java.util.List;

import com.nc13techsolutions.fitnesstrackerbackendserver.models.User;

public interface UserRepo {
    // User Roles
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    /**
     * Adds a new user
     * @param user
     * @return 1 if insert is successful; -1 if not successful; 0 if username already exists
     */
    int insertUser(User user);

    /**
     * Gets all users
     * @return list of all users
     */
    List<User> getUsers();

    /**
     * Finds the User with userId
     * @param userId ID of the user
     * @return User object or Null if user doesn't exist
     */
    User getUserById(int userId);

    /**
     * Finds the User with username and password
     * @param username username of the user
     * @param password password of the user
     * @return User object or Null if user doesn't exist
     */
    User checkUserCredentials(String username, String password);

    /**
     * Updates user's password, if it exists
     * @param user current details of the User
     * @param newPassword new password of the user
     * @return 1 if update is successful; 0 if User is not found; -1 if update is unsuccessful
     */
    int updateUserPassword(User user, String newPassword);

    /**
     * Deletes the User, if it exists
     * @param userId ID of the user
     * @return 1 if delete is successful; -1 if user is not found; 0 if user has dependencies
     */
    int deleteUser(int userId);

    /**
     * Checks if user already exists
     * @param user
     * @return 2 if username and id matched; 1 if only username matches; 0 if only id matches; -1 if there is no match
     */
    int checkIfUserExists(User user);

    /**
     * Finds the heighest user ID
     * @return 0 if there are no users; heighest ID value
     */
    int findHeighestUserId();
}
