package eu.unibuc.ro.database.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "challenges",
        foreignKeys = @ForeignKey(entity = User.class,parentColumns = "id",childColumns = "userId", onDelete = CASCADE),
        indices = @Index("userId"))
public class Challenge {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "dateStart")
    private Date dateStart;

    @ColumnInfo(name = "dateEnd")
    private Date dateEnd;

    @ColumnInfo(name = "userId")
    private long userId;

    public Challenge(long id, String name, Date dateStart, Date dateEnd, long userId) {
        this.id = id;
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.userId = userId;
    }

    @Ignore
    public Challenge(String name, Date dateStart, Date dateEnd, long userId) {
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.userId = userId;
    }

    @Ignore
    public Challenge(){

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

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
