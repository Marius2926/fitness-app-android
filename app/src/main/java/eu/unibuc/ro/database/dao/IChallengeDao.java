package eu.unibuc.ro.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.Completable;

import java.util.List;

import eu.unibuc.ro.database.models.Challenge;
import io.reactivex.Maybe;

@Dao
public interface IChallengeDao {
    @Insert
    Completable insertChallenge(Challenge challenge);

    @Update
    Completable updateChallenge(Challenge challenge);

    @Delete
    Completable deleteChallenge(Challenge challenge);

    @Query("select * from challenges where name = :challengeName and dateEnd isnull and userId=:id")
    Maybe<Challenge> getChallengeByNameAndDateEnd(String challengeName, long id);

    @Query("select * from challenges where userId=:id and dateEnd is not null")
    Maybe<List<Challenge>> getUserChallenges(long id);
}
