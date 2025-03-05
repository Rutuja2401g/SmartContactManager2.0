package com.scm.services.Implmentation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.UserRepository;
import com.scm.services.UserService;
@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserRepository userRepository; // Inject the UserRepository

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject the PasswordEncoder

    @SuppressWarnings("unused")
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        // Generate user ID
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);

        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set the user role
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Update user2 fields from user
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

        // Save the user in the database
        User savedUser = userRepository.save(user2);
        return Optional.ofNullable(savedUser);
    }

    @Override
    public void deleteUser(String id) {
        User user2 = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user2);
    }

    @Override
    public boolean isUserExist(String userId) {
        return userRepository.findById(userId).isPresent();
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    
    @Override
public User getUserByEmail(String email) {
    return userRepository.findByEmailIgnoreCase(email);
}

}
