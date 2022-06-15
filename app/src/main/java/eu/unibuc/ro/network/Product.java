package eu.unibuc.ro.network;

import java.io.Serializable;

public class Product implements Serializable {

    private String name;
    private String quantity;
    private Integer caloriesNumber;
    private Nutrients nutrients;

    public Product() {

    }

    public Product(String name, String quantity, Integer caloriesNumber, Nutrients nutrients) {
        this.name = name;
        this.quantity = quantity;
        this.caloriesNumber = caloriesNumber;
        this.nutrients = nutrients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Integer getCaloriesNumber() {
        return caloriesNumber;
    }

    public void setCaloriesNumber(Integer caloriesNumber) {
        this.caloriesNumber = caloriesNumber;
    }

    public Nutrients getNutrients() {
        return nutrients;
    }

    public void setNutrients(Nutrients nutrients) {
        this.nutrients = nutrients;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", caloriesNumber='" + caloriesNumber + '\'' +
                ", nutrients=" + nutrients +
                '}';
    }
}
