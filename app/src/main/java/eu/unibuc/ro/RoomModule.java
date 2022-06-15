package eu.unibuc.ro;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eu.unibuc.ro.database.DatabaseManager;
import eu.unibuc.ro.database.dao.IChallengeDao;
import eu.unibuc.ro.database.dao.IEatenAlimentsDao;
import eu.unibuc.ro.database.dao.IHydrationLevelDao;
import eu.unibuc.ro.database.dao.IUserDao;

@Module
public class RoomModule {
    private static final String DATABASE_NAME = "FitnessAppDatabase";
    private final Context context;

    public RoomModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    DatabaseManager providesRoomDatabase() {
        return Room.databaseBuilder(context, DatabaseManager.class, DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    IChallengeDao providesChallengeDao(DatabaseManager databaseManager) {
        return databaseManager.getChallengeDao();
    }

    @Singleton
    @Provides
    IEatenAlimentsDao providesEatenAlimentsDao(DatabaseManager databaseManager) {
        return databaseManager.getEatenAlimentsDao();
    }

    @Singleton
    @Provides
    IHydrationLevelDao providesHydrationLevelDao(DatabaseManager databaseManager) {
        return databaseManager.getHydrationLevelDao();
    }

    @Singleton
    @Provides
    IUserDao providesUserDao(DatabaseManager databaseManager) {
        return databaseManager.getUserDao();
    }
}
