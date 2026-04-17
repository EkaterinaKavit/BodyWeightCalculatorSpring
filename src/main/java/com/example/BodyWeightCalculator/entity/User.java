package com.example.BodyWeightCalculator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Сущность, представляющая пользователя системы
 * Связана с таблицей users в базе данных
 * Один пользователь может иметь много замеров (связь @OneToMany)
 */

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)  // данная аннотация используется, чтобы id не участвовал при создании новго клиента при тестировании через Swagger
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Список замеров пользователя
     * Используется @OneToMany для связи с таблицей results
     * Поле user в ResultEntity указывает на этого пользователя
     * CascadeType.ALL все операции касакадируются на все замеры
     * orphanRemoval = true удаление замера приводит к его удалению из БД
     *  @JsonIgnore - исключаем поле из сериализации, чтобы избежать циклических ссылок
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ResultEntity> results = new ArrayList<>();

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ResultEntity> getResults() {
        return results;
    }

    public void setResults(List<ResultEntity> results) {
        this.results = results;
    }
}
