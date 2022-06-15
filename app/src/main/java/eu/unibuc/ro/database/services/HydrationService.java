package eu.unibuc.ro.database.services;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import eu.unibuc.ro.database.DatabaseManager;
import eu.unibuc.ro.database.dao.IHydrationLevelDao;
import eu.unibuc.ro.database.models.HydrationLevel;

public class HydrationService {
    private static IHydrationLevelDao hydrationDao;

    public static class InsertHydrationLevel extends AsyncTask<HydrationLevel, Void, HydrationLevel> {

        public InsertHydrationLevel(Context context) {
            hydrationDao = DatabaseManager.getInstance(context).getHydrationLevelDao();
        }

        @Override
        protected HydrationLevel doInBackground(HydrationLevel... hydrationLevels) {
            if (hydrationLevels != null && hydrationLevels.length == 1) {

                long hydrationLevelId = hydrationDao.insertHydrationLevel(hydrationLevels[0]);
                if (hydrationLevelId == -1) {
                    return null;
                }
                hydrationLevels[0].setId(hydrationLevelId);
                return hydrationLevels[0];
            }
            return null;
        }
    }

    public static class UpdateHydrationLevel extends AsyncTask<HydrationLevel, Void, Integer> {

        public UpdateHydrationLevel(Context context) {
            hydrationDao = DatabaseManager.getInstance(context).getHydrationLevelDao();
        }

        @Override
        protected Integer doInBackground(HydrationLevel... hydrationLevels) {

            if (hydrationLevels != null && hydrationLevels.length == 1) {
                Integer rowsAffected = hydrationDao.updateHydrationLevel(hydrationLevels[0]);
                return rowsAffected;
            }
            return null;
        }
    }

    public static class GetHydrationAverage extends AsyncTask<Long, Void, Float> {
        public GetHydrationAverage(Context context) {
            hydrationDao = DatabaseManager.getInstance(context).getHydrationLevelDao();
        }

        @Override
        protected Float doInBackground(Long... ids) {
            if (ids != null && ids.length == 1) {
                Float result = hydrationDao.getHydrationAverage(ids[0]);
                if (result == null) {
                    return 0f;
                }
                return result;
            }
            return -1f;
        }
    }

    public static class GetChartData extends AsyncTask<Long, Void, List<HydrationLevel>> {

        public GetChartData(Context context) {
            hydrationDao = DatabaseManager.getInstance(context).getHydrationLevelDao();
        }

        @Override
        protected List<HydrationLevel> doInBackground(Long... ids) {
            if (ids != null && ids.length == 1) {
                return hydrationDao.getChartData(ids[0]);
            }
            return null;
        }
    }
}
