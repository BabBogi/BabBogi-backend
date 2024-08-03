package BobBogi.BobBogispring.domain;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "consumption")  // 테이블 이름이 대문자 "Consumption"인지 확인
public class Consumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "food_count")
    private Double foodCount = 1d;

    @Column(name = "weight")
    private Double weight = 0d;

    @Column(name = "kcal")
    private Double kcal = 0d;

    @Column(name = "carbohydrate")
    private Double carbohydrate = 0d;

    @Column(name = "sugar")
    private Double sugar = 0d;

    @Column(name = "protein")
    private Double protein = 0d;

    @Column(name = "fat")
    private Double fat = 0d;

    @Column(name = "transfat")
    private Double transfat = 0d;

    @Column(name = "saturatedfat")
    private Double saturatedfat = 0d;

    @Column(name = "cholesterol")
    private Double cholesterol = 0d;

    @Column(name = "natrium")
    private Double natrium = 0d;

    @Column(name = "date", nullable = false)
    private String date;

    public Consumption(){
    }

    @PrePersist
    protected void onCreate() {
        if (foodCount == null) {
            foodCount = 1d;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Double getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(Double foodCount) {
        this.foodCount = foodCount;
    }

    public Double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(Double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getTransfat() {
        return transfat;
    }

    public void setTransfat(Double transfat) {
        this.transfat = transfat;
    }

    public Double getSaturatedfat() {
        return saturatedfat;
    }

    public void setSaturatedfat(Double saturatedfat) {
        this.saturatedfat = saturatedfat;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Double getNatrium() {
        return natrium;
    }

    public void setNatrium(Double natrium) {
        this.natrium = natrium;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getKcal() {
        return kcal;
    }

    public void setKcal(Double kcal) {
        this.kcal = kcal;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}