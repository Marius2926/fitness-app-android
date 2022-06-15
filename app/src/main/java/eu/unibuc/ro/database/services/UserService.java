package eu.unibuc.ro.database.services;

import android.content.Context;
import android.os.AsyncTask;

import eu.unibuc.ro.database.DatabaseManager;
import eu.unibuc.ro.database.dao.IUserDao;
import eu.unibuc.ro.database.models.User;

public class UserService {
    private static IUserDao userDao;

    public static class InsertUser extends AsyncTask<User, Void, User> {

        public InsertUser(Context context){
            userDao = DatabaseManager.getInstance(context).getUserDao();
        }
        @Override
        protected User doInBackground(User... users) {
            if(users == null || users.length != 1){
                return null;
            }
            long userId = userDao.insertUser(users[0]);
            if(userId == -1){
                return null;
            }
            users[0].setId(userId);
            return users[0];
        }
    }

    public static class UpdateUser extends AsyncTask<User,Void,Integer>{
        public  UpdateUser(Context context){
            userDao = DatabaseManager.getInstance(context).getUserDao();
        }

        @Override
        protected Integer doInBackground(User... users) {
            if(users == null || users.length != 1){
                return -1;
            }

            Integer rowsAffected = userDao.updateUser(users[0]);
            return rowsAffected;
        }
    }

    public static class DeleteUser extends AsyncTask<User,Void,Integer>{

        public  DeleteUser(Context context){
            userDao = DatabaseManager.getInstance(context).getUserDao();
        }

        @Override
        protected Integer doInBackground(User... users) {
            if(users == null || users.length != 1){
                return -1;
            }
            Integer rowsAffected = userDao.deleteUser(users[0]);
            return rowsAffected;
        }
    }

    public static class GetUserByEmail extends AsyncTask<String, Void, User>{
        public GetUserByEmail(Context context){
            userDao = DatabaseManager.getInstance(context).getUserDao();
        }

        @Override
        protected User doInBackground(String... emails) {
            if(emails == null || emails.length != 1){
                return null;
            }
            return userDao.getUserByEmail(emails[0]);
        }
    }

    public static class getUserById extends AsyncTask<Long, Void, User>{

        public getUserById(Context context){
            userDao = DatabaseManager.getInstance(context).getUserDao();
        }

        @Override
        protected User doInBackground(Long... userIds) {
            if(userIds == null || userIds.length != 1){
                return null;
            }

            return userDao.getUserById(userIds[0]);
        }
    }

}
