package ru.fisher.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table(schema = "autopark", name = "manager")
public class Manager {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Обязательное поле")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "Обязательное поле")
    @Column(name = "password")
    private String password;

    @ManyToMany(mappedBy = "managers")
    @JsonIgnore
    private List<Enterprise> enterprises;

    @Column(name = "role")
    private String role; // Роль (например, "MANAGER", "ADMIN", "USER")

    @Column(name = "chat_id")
    private Long chatId;

}
