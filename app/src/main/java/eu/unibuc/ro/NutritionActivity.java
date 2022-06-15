package eu.unibuc.ro;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.unibuc.ro.database.dao.IEatenAlimentsDao;
import eu.unibuc.ro.database.models.EatenAliment;
import eu.unibuc.ro.database.models.User;
import eu.unibuc.ro.network.Product;
import eu.unibuc.ro.network.ProductAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static eu.unibuc.ro.fragments.DiaryFragment.CURRENT_USER;

import javax.inject.Inject;

public class NutritionActivity extends AppCompatActivity {
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private final static int REQUEST_CODE_ADD_ALIMENTS = 200;
    public final static String EATEN_CALORIES_KEY = "eaten_calories_key";
    private ListView lvAlimentsList;
    private int currentCaloriesSum;
    private User user;
    Intent intent;
    @Inject
    IEatenAlimentsDao alimentsDao;
    ActivityResultLauncher<Intent> startActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerApplicationComponent.builder().roomModule(new RoomModule(this)).build().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        intent = getIntent();
        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Toast.makeText(this, "Aliments saved!", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else if (result.getResultCode() != RESULT_CANCELED) {
                        Toast.makeText(this, "Something happened. Please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
        initComponents();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    private void loadData() {
        long time = new Date().getTime();
        Date date = new Date(time - time % (24 * 60 * 60 * 1000));
        mDisposable.add(alimentsDao.getTodayEatenAliments(date, user.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::proccessData));
    }

    private void proccessData(List<EatenAliment> eatenAliments) {
        currentCaloriesSum = 0;
        ArrayList<Product> products = new ArrayList<>();
        for (int i = 0; i < eatenAliments.size(); i++) {
            Product product = new Product();
            product.setName(eatenAliments.get(i).getName());
            product.setQuantity(eatenAliments.get(i).getQuantity());
            product.setCaloriesNumber((int) eatenAliments.get(i).getCaloriesNumber());
            currentCaloriesSum += product.getCaloriesNumber();
            products.add(product);
        }
        ProductAdapter productAdapter = new ProductAdapter(getApplicationContext(), products, getLayoutInflater());
        lvAlimentsList.setAdapter(productAdapter);
    }

    private void initComponents() {
        Button btnBreakfast = findViewById(R.id.nutrition_bt_add_breakfast);
        Button btnLunch = findViewById(R.id.nutrition_bt_add_lunch);
        Button btnDinner = findViewById(R.id.nutrition_bt_add_dinner);
        Button btnSnacks = findViewById(R.id.nutrition_bt_add_snacks);
        lvAlimentsList = findViewById(R.id.nutrition_lv_list_of__today_aliments);
        TextView tvEmptyLv = findViewById(R.id.nutrition_tv_empty_lv);
        lvAlimentsList.setEmptyView(tvEmptyLv);
        user = (User) intent.getSerializableExtra(CURRENT_USER);

        this.loadData();

        btnBreakfast.setOnClickListener(v -> this.onClickListener());

        btnLunch.setOnClickListener(v -> this.onClickListener());

        btnDinner.setOnClickListener(v -> this.onClickListener());

        btnSnacks.setOnClickListener(v -> this.onClickListener());
    }

    private void onClickListener() {
        Intent chooserIntent = new Intent(getApplicationContext(), AlimentsListActivity.class);
        chooserIntent.putExtra(CURRENT_USER, user);
        startActivityIntent.launch(chooserIntent);
    }

    @Override
    public void onBackPressed() {
        intent.putExtra(EATEN_CALORIES_KEY, currentCaloriesSum);
        setResult(RESULT_OK, intent);
        finish();
    }
}
