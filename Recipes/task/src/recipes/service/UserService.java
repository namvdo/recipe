package recipes.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.model.MyUserDetail;
import recipes.model.Recipe;
import recipes.model.User;
import recipes.repository.RecipeRepository;
import recipes.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final RecipeRepository recipeRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, RecipeRepository recipeRepo) {
        this.userRepo = userRepo;
        this.recipeRepo = recipeRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean doesUserExist(String email) {
        return userRepo.getUserByEmail(email).isPresent();
    }

    public Optional<User> getUserByEmail(String email) {
        if (this.userRepo.getUserByEmail(email).isPresent())  {
            return this.userRepo.getUserByEmail(email);
        }
        return Optional.empty();
    }
    

    public List<User> getAllUser() {
        return this.userRepo.getAllUsers();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = this.userRepo.getUserByEmail(username);
        if (userOptional.isPresent()) {
            return new MyUserDetail(userOptional.get());
        } else {
            throw new UsernameNotFoundException("Not found user");
        }
    }

    public void saveUser(User user) throws Exception {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

    }

    public boolean isOwner(String email, int recipeId) {
        Optional<User> userOptional = this.userRepo.getUserByEmail(email);
        if (userOptional.isPresent()) {
            Optional<Recipe> recipeOptional = this.recipeRepo.findById(recipeId);
            if (recipeOptional.isPresent()) {
                User user = userOptional.get();
                int uid = user.getId();
                Recipe recipe = recipeOptional.get();
                return uid == recipe.getUserId();
            } else {
                return false;
            }
        }
        return false;
    }

}
