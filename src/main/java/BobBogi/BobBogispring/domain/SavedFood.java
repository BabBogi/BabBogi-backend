package BobBogi.BobBogispring.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "savedfood")
public class SavedFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int foodnum;

    @Column(name = "foodcode", nullable = true)
    private String foodcode;

    @Column(name = "foodname", nullable = true)
    private String foodname;

    @Column(name = "foodgroup", nullable = true)
    private String foodgroup;

    @Column(name = "food", nullable = true)
    private String food;

    @Column(name = "nutrientcontentper100", nullable = true)
    private String nutrientcontentper100;

    @Column(name = "kcal", nullable = true)
    private Double kcal;

    @Column(name = "protein", nullable = true)
    private Double protein;

    @Column(name = "fat", nullable = true)
    private Double fat;

    @Column(name = "carbohydrate", nullable = true)
    private Double carbohydrate;

    @Column(name = "sugar", nullable = true)
    private Double sugar;

    @Column(name = "natrium", nullable = true)
    private Double natrium;

    @Column(name = "cholesterol", nullable = true)
    private Double cholesterol;

    @Column(name = "saturatedfat", nullable = true)
    private Double saturatedfat;

    @Column(name = "transfat", nullable = true)
    private Double transfat;

    @Column(name = "companyname", nullable = true)
    private String companyname;

    // Getters and Setters
    public int getFoodnum() {
        return foodnum;
    }

    public void setFoodnum(int foodnum) {
        this.foodnum = foodnum;
    }

    public String getFoodcode() {
        return foodcode;
    }

    public void setFoodcode(String foodcode) {
        this.foodcode = foodcode;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodgroup() {
        return foodgroup;
    }

    public void setFoodgroup(String foodgroup) {
        this.foodgroup = foodgroup;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getNutrientcontentper100() {
        return nutrientcontentper100;
    }

    public void setNutrientcontentper100(String nutrientcontentper100) {
        this.nutrientcontentper100 = nutrientcontentper100;
    }

    public Double getKcal() {
        return kcal;
    }

    public void setKcal(Double kcal) {
        this.kcal = kcal;
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

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }
}
