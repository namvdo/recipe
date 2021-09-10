package recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "recipe")
@ToString
@Getter
@Setter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Size(min = 1)
    @NotEmpty
    @ElementCollection(targetClass = String.class)
    private List<String> ingredients;

    @Size(min = 1)
    @NotEmpty
    @ElementCollection(targetClass = String.class)
    private List<String> directions;

    @NotBlank
    private String category;

    private LocalDateTime date;

    @Column(name="user_id")
    @JsonIgnore
    private int userId;
}