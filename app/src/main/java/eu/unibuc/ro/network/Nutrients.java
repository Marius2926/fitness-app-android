package eu.unibuc.ro.network;

import java.io.Serializable;

public class Nutrients implements Serializable {
    private String proteins;
    private String carbs;
    private String fiber;
    private String fats;
    private String sugar;

    public Nutrients(){

    }
    public Nutrients(String proteins, String carbs, String fiber, String fats, String sugar) {
        this.proteins = proteins;
        this.carbs = carbs;
        this.fiber = fiber;
        this.fats = fats;
        this.sugar = sugar;
    }

    public String getProteins() {
        return proteins;
    }

    public void setProteins(String proteins) {
        this.proteins = proteins;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getFiber() {
        return fiber;
    }

    public void setFiber(String fiber) {
        this.fiber = fiber;
    }

    public String getFats() {
        return fats;
    }

    public void setFats(String fats) {
        this.fats = fats;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    @Override
    public String toString() {
        return "Nutrients{" +
                "proteins='" + proteins + '\'' +
                ", carbs='" + carbs + '\'' +
                ", fiber='" + fiber + '\'' +
                ", fats='" + fats + '\'' +
                ", sugar='" + sugar + '\'' +
                '}';
    }
}
