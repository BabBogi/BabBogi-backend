package BobBogi.BobBogispring.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recommendationnutrition")
public class RecommendationNutrition {
    @Id
    private Long id;
    private Double kcal;
    private Double carbohydrate;
    private Double sugar;
    private Double protein;
    private Double fat;
    private Double saturatedfat;
    private Double transfat;
    private Double natrium;
    private Double cholesterol;
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

    public Double getSaturatedfat() {
        return saturatedfat;
    }

    public void setSaturatedfat(Double saturatedfat) {
        this.saturatedfat = saturatedfat;
    }

    public Double getTransfat() {
        return transfat;
    }

    public void setTransfat(Double transfat) {
        this.transfat = transfat;
    }

    public Double getNatrium() {
        return natrium;
    }

    public void setNatrium(Double natrium) {
        this.natrium = natrium;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Double getKcal() {
        return kcal;
    }

    public void setKcal(Double kcal) {
        this.kcal = kcal;
    }
}
