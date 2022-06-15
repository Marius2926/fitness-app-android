package eu.unibuc.ro;

import javax.inject.Singleton;

import dagger.Component;
import eu.unibuc.ro.fragments.DiaryFragment;

@Singleton
@Component(modules = {RoomModule.class})
public interface ApplicationComponent {
    void inject(ChallengeDetailActivity activity);

    void inject(ChallengeReportActivity activity);

    void inject(AlimentsListActivity activity);

    void inject(NutritionActivity activity);

    void inject(HydrationActivity activity);

    void inject(DiaryFragment fragment);

}

