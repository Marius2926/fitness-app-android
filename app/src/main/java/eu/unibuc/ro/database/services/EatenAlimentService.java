package eu.unibuc.ro.database.services;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import eu.unibuc.ro.database.DatabaseManager;
import eu.unibuc.ro.database.dao.IEatenAlimentsDao;
import eu.unibuc.ro.database.models.EatenAliment;

public class EatenAlimentService {
    private static IEatenAlimentsDao eatenAlimentsDao;

    public static class GetCaloriesAverage extends AsyncTask<Long, Void, Float> {
        public GetCaloriesAverage(Context context) {
            eatenAlimentsDao = DatabaseManager.getInstance(context).getEatenAlimentsDao();
        }

        @Override
        protected Float doInBackground(Long... ids) {
            if (ids != null && ids.length == 1) {
                Float result = eatenAlimentsDao.getCaloriesAverage(ids[0]);
                if (result == null) {
                    return 0f;
                }
                return result;
            }
            return -1f;
        }
    }
}
