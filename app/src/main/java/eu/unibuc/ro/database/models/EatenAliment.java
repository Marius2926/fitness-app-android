package eu.unibuc.ro.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "eatenAliments",
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = CASCADE),
        indices = @Index("userId"))
public class EatenAliment {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "quantity")
    private String quantity;

    @ColumnInfo(name = "caloriesNumber")
    private float caloriesNumber;

    @ColumnInfo(name = "registrationDay")

    private Date  registrationDay;

    @ColumnInfo(name = "userId")
    private long  userId;

    public EatenAliment(long id, String name,String quantity, float caloriesNumber, Date registrationDay, long userId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.caloriesNumber = caloriesNumber;
        this.registrationDay = registrationDay;
        this.userId = userId;
    }

    @Ignore
    public EatenAliment(String name,String quantity, float caloriesNumber, Date registrationDay, long userId) {
        this.name = name;
        this.quantity=quantity;
        this.caloriesNumber = caloriesNumber;
        this.registrationDay = registrationDay;
        this.userId = userId;
    }

    @Ignore()
    public EatenAliment(){

    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCaloriesNumber() {
        return caloriesNumber;
    }

    public void setCaloriesNumber(float caloriesNumber) {
        this.caloriesNumber = caloriesNumber;
    }

    public Date getRegistrationDay() {
        return registrationDay;
    }

    public void setRegistrationDay(Date registrationDay) {
        this.registrationDay = registrationDay;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}
