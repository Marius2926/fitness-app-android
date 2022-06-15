package eu.unibuc.ro.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import eu.unibuc.ro.database.models.User;

@Dao
public interface IUserDao {

    @Insert
    long insertUser(User user);

    @Update
    int updateUser(User user);

    @Delete
    int deleteUser(User user);

    @Query("select * from users where email  = :userEmail ")
    User getUserByEmail(String userEmail);

    @Query("select * from users where email  = :userId ")
    User getUserById(long userId);

}
