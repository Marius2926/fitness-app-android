package eu.unibuc.ro.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import eu.unibuc.ro.database.models.HydrationLevel;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface IHydrationLevelDao {

    @Insert
    long insertHydrationLevel(HydrationLevel hydrationLevel);

    @Update
    int updateHydrationLevel(HydrationLevel hydrationLevel);

    @Delete
    int deleteHydrationLevel(HydrationLevel hydrationLevel);

    @Query("select registrationDay from hydrationLevels where userId=:id")
    List<Date> getRegistrationDays(long id);

    @Query("select * from hydrationLevels where registrationDay >= :day and userId=:id")
    Single<HydrationLevel> getHydrationLevelByRegistrationDay(Date day, long id);

    @Query("select AVG(nr) from (select levelValue as nr from hydrationLevels where userId=:id GROUP BY registrationDay)")
    Float getHydrationAverage(long id);

    @Query("select * from hydrationLevels where userId=:id")
    List<HydrationLevel> getChartData(long id);
}
