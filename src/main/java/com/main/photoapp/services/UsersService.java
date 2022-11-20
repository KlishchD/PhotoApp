package com.main.photoapp.services;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.User;
import com.main.photoapp.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.main.photoapp.utils.EmailChecker.isEmailCorrect;
import static com.main.photoapp.utils.UsernameChecker.isUsernameCorrect;
import static com.main.photoapp.utils.PasswordChecker.isPasswordCorrect;

@Component
public class UsersService {
    @Autowired
    private UsersRepository users;

    @Autowired
    private UserDetailsManager manager;

    @Autowired
    private PasswordEncoder encoder;

    private UserDetails getSecurityUserDetails(User user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER").build();
    }

    public int createUser(String username, String email, String password) throws UsernameIsAlreadyTakenException, EmailIsAlreadyTakenException, IncorrectEmailFormat, IncorrectPasswordFormat, IncorrectUsernameFormat {
        if (!isUsernameCorrect(username)) throw new IncorrectUsernameFormat(username);
        if (!isEmailCorrect(email)) throw new IncorrectEmailFormat(email);
        if (!isPasswordCorrect(password)) throw new IncorrectPasswordFormat(password);
        if (users.existsByUsername(username)) throw new UsernameIsAlreadyTakenException(username);
        if (users.existsByEmail(email)) throw new EmailIsAlreadyTakenException(email);
        User user = new User(username, email, encoder.encode(password));
        manager.createUser(getSecurityUserDetails(user));
        user.setId(users.findByUsername(username).get().getId());
        return users.save(user).getId();
    }

    public User getUserByUsername(String username) throws UserNotFoundException, IncorrectUsernameFormat {
        if (!isUsernameCorrect(username)) throw new IncorrectUsernameFormat(username);
        if (!users.existsByUsername(username)) throw new UserNotFoundException(username);
        return users.findByUsername(username).get();
    }

    public User getUserById(int id) throws UserNotFoundException {
        if (!users.existsById(id)) throw new UserNotFoundException(id);
        return users.findById(id).get();
    }

    public void removeUser(int id) throws UserNotFoundException {
        if (!users.existsById(id)) throw new UserNotFoundException(id);
        manager.deleteUser(users.findById(id).get().getUsername());
    }

    public void updateUserEmail(int id, String email) throws IncorrectEmailFormat, UserNotFoundException {
        if (!users.existsById(id)) throw new UserNotFoundException(id);
        if (!isEmailCorrect(email)) throw new IncorrectEmailFormat(email);
        User user = users.findById(id).orElseThrow();
        user.setEmail(email);
        users.save(user);
    }

    public void updateUserPassword(int id, String password) throws IncorrectPasswordFormat, UserNotFoundException {
        if (!users.existsById(id)) throw new UserNotFoundException(id);
        if (!isPasswordCorrect(password)) throw new IncorrectPasswordFormat(password);
        User user = users.findById(id).orElseThrow();
        user.setPassword(encoder.encode(password));
        users.save(user);
    }

    public String getUsername(int userId) {
        return users.findById(userId).get().getUsername();
    }

    public Map<Integer, String> getUsernamesMap(List<Integer> usersIds) {
        Map<Integer, String> usernames = new HashMap<>();
        for (Integer userId: usersIds) {
            usernames.put(userId, getUsername(userId));
        }
        return usernames;
    }

    public List<String> getUsernames(List<Integer> usersIds) {
        List<String> usernames = new ArrayList<>();
        for (Integer userId: usersIds) {
            usernames.add(users.findById(userId).get().getUsername());
        }
        return usernames;
    }

    public List<User> findUsers(String username) {
        return users.findByUsernameLike(username);
    }

    public boolean userExists(int id) {
        return users.existsById(id);
    }

    public boolean userNotExists(int id) {
        return !users.existsById(id);
    }

}
