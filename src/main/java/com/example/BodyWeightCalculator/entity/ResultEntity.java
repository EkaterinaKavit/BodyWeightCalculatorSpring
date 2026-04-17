package com.example.BodyWeightCalculator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
/* сущность, представляющая результаты замеров
   связана с таблицей results
 */
@Entity
@Table(name="results")
public class ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double weight;
    private double height;
    private double index;
    private String category;
    private LocalDate date;

    /*
    Ссылка на пользователя, которому принадлежат замеры
    Связь многие к одному(@Many-To-One) много замеров у одного пользователя
    @JoinColumn задает имя столбца внешнего ключа в таблице results
    @JsonIgnore исключает поле из сериализации, чтобы избежать циклических ссылок
     */

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    public ResultEntity() {
    }

    public ResultEntity(double weight, double height, double index, String category, LocalDate date, User user) {
        this.weight = weight;
        this.height = height;
        this.index = index;
        this.category = category;
        this.date = date;
        this.user=user;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getIndex() {
        return index;
    }

    public void setIndex(double index) {
        this.index = index;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
