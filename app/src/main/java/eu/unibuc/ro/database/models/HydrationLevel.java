package eu.unibuc.ro.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "hydrationLevels",
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = CASCADE),
        indices = { @Index("userId")})
public class HydrationLevel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "levelValue")
    private float levelValue;

    @ColumnInfo(name = "registrationDay")
    private Date registrationDay;

    @ColumnInfo(name = "userId")
    private long userId;

    public HydrationLevel(long id, float levelValue, Date registrationDay, long userId) {
        this.id = id;
        this.levelValue = levelValue;
        this.registrationDay = registrationDay;
        this.userId=userId;
    }

    @Ignore
    public HydrationLevel(float levelValue, Date registrationDay, long userId) {
        this.levelValue = levelValue;
        this.registrationDay = registrationDay;
        this.userId=userId;
    }

    @Ignore
    public HydrationLevel(){

    }

    public long getUserId() {
        return userId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public float getLevelValue() {
        return levelValue;
    }

    public void setLevelValue(float levelValue) {
        this.levelValue = levelValue;
    }

    public Date getRegistrationDay() {
        return registrationDay;
    }

    public void setRegistrationDay(Date registrationDay) {
        this.registrationDay = registrationDay;
    }
}
