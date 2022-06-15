package eu.unibuc.ro.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users", indices = @Index(value = "email", unique = true))
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") //aceasta adnotare inseamna ca acest camp va fi o coloana din tabela noastra
    private long id;        //ce e in paranteza reprezinta numele coloanei din baza de date

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "age")
    private int age;

    @ColumnInfo(name = "height")
    private float height;

    @ColumnInfo(name = "weight")
    private float weight;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "goal")
    private String goal;

    @ColumnInfo(name = "activityLevel")
    private String activityLevel;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "imc")
    private float imc;

    @ColumnInfo(name = "caloriesNumber")
    private float caloriesNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getImc() {
        return imc;
    }

    public float getCaloriesNumber() {
        return caloriesNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public User(long id, String name, int age, float height, float weight, String gender,
                String goal, String activityLevel, String email, String password, float imc, float caloriesNumber) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.goal = goal;
        this.activityLevel = activityLevel;
        this.email = email;
        this.password = password;
        this.imc = imc;
        this.caloriesNumber = caloriesNumber;
    }

    @Ignore
    public User(String name, int age, float height, float weight, String gender, String goal,
                String activityLevel, String email, String password) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.goal = goal;
        this.activityLevel = activityLevel;
        this.imc = imcCalculator(weight,height);
        this.caloriesNumber = numberOfCaloriesCalculator(age,height,weight,activityLevel,gender);
        this.email=email;
        this.password=password;
    }

    private float numberOfCaloriesCalculator(int age, float height, float weight, String activityLevel, String gender){

        float metabolicRate;
        if(gender.equals("Female")){
            metabolicRate=655 + (9.6f * weight) + (1.8f * height) - (4.7f * age);
        }else{
            metabolicRate=655 + (13.7f * weight) + (5 * height) - (6.8f * age);
        }

        float numberOfCalories;
        if(activityLevel.equals("Not very active")){
            numberOfCalories = metabolicRate*1.2f;
        } else if(activityLevel.equals("Lightly active")){
            numberOfCalories = metabolicRate*1.375f;
        } else if(activityLevel.equals("Active")){
            numberOfCalories = metabolicRate*1.55f;
        } else {
            numberOfCalories = metabolicRate*1.725f;
        }

        return numberOfCalories;
    }

    private float imcCalculator(float weight, float heigth)
    {
        float heightInMeters = height/100;
        return weight/(heightInMeters * heightInMeters);
    }


}
