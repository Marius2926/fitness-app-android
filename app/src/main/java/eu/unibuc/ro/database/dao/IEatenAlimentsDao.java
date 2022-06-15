package eu.unibuc.ro.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import eu.unibuc.ro.database.models.EatenAliment;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface IEatenAlimentsDao {

    @Insert
    Completable insertAliment(EatenAliment aliment);

    @Insert
    Completable insertAliment(List<EatenAliment> aliment);

    @Update
    int updateAliment(EatenAliment aliment);

    @Delete
    Completable deleteAliment(EatenAliment aliment);

    @Query("select * from eatenAliments where id = (select MAX(id) from eatenAliments where name= :alimentName and userId=:id)")
    Single<EatenAliment> getLastAlimentByName(String alimentName, long id);

    @Query("select * from eatenAliments where registrationDay >= :day and userId =:id")
    Maybe<List<EatenAliment>> getTodayEatenAliments(Date day, long id);

    @Query("select SUM(caloriesNumber) from eatenAliments where registrationDay >=:day and userId =:id")
    Maybe<Float> getTodayEatenCalories(Date day, long id);

    @Query("select AVG(nr) from (select SUM(caloriesNumber) as nr from eatenAliments where userId=:id GROUP BY registrationDay)")
    Float getCaloriesAverage(long id);
}
