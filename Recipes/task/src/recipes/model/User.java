package recipes.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import recipes.ExtendedEmailValidator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ExtendedEmailValidator
    private String email;

    @Length(min = 8)
    @NotBlank
    private String password;

    @ElementCollection(targetClass = Recipe.class)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Recipe> recipes;

}