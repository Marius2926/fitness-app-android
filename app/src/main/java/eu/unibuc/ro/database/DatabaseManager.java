package eu.unibuc.ro.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import eu.unibuc.ro.database.dao.IChallengeDao;
import eu.unibuc.ro.database.dao.IEatenAlimentsDao;
import eu.unibuc.ro.database.dao.IHydrationLevelDao;
import eu.unibuc.ro.database.dao.IUserDao;
import eu.unibuc.ro.database.models.Challenge;
import eu.unibuc.ro.database.models.EatenAliment;
import eu.unibuc.ro.database.models.HydrationLevel;
import eu.unibuc.ro.database.models.User;
import eu.unibuc.ro.database.utils.ConvertDate;

@Database(entities = {User.class, EatenAliment.class, HydrationLevel.class, Challenge.class},
        exportSchema = false,
        version = 1)
@TypeConverters({ConvertDate.class})
public abstract class DatabaseManager extends RoomDatabase {

    private static final String DATABASE_NAME = "FitnessAppDatabase";
    private static volatile DatabaseManager databaseManager;

    public static DatabaseManager getInstance(Context context) {
        if (databaseManager == null) {
            synchronized (DatabaseManager.class) {
                if (databaseManager == null) {
                    databaseManager = Room.databaseBuilder(context, DatabaseManager.class, DATABASE_NAME).build();
                    return databaseManager;
                }
            }
        }
        return databaseManager;
    }

    public abstract IUserDao getUserDao();

    public abstract IHydrationLevelDao getHydrationLevelDao();

    public abstract IEatenAlimentsDao getEatenAlimentsDao();

    public abstract IChallengeDao getChallengeDao();
}
