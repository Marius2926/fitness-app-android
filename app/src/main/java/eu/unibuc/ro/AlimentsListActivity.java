package eu.unibuc.ro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import eu.unibuc.ro.database.dao.IEatenAlimentsDao;
import eu.unibuc.ro.database.models.EatenAliment;
import eu.unibuc.ro.database.models.User;
import eu.unibuc.ro.database.utils.AlimentAdapter;
import eu.unibuc.ro.network.HttpManager;
import eu.unibuc.ro.network.JsonParser;
import eu.unibuc.ro.network.Product;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static eu.unibuc.ro.fragments.DiaryFragment.CURRENT_USER;

import javax.inject.Inject;

public class AlimentsListActivity extends AppCompatActivity {
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private static final String URL = "https://mocki.io/v1/83857448-0d11-436e-bfdf-e421b40d1461";
    private RecyclerView recyclerView;
    private ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<Product> selectedProducts = new ArrayList<>();
    private Intent intent;
    private AlimentAdapter alimentAdapter;
    private User user;
    @Inject
    IEatenAlimentsDao alimentsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerApplicationComponent.builder().roomModule(new RoomModule(this)).build().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aliments_list);
        intent = getIntent();
        initComponents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    private void buildRecyclerView() {
        alimentAdapter = new AlimentAdapter(products != null ? products : new ArrayList<>(), (view, product) -> {
            if (((ColorDrawable) view.getBackground()).getColor() == getResources().getColor(R.color.waterProcessBarBackground)) {
                view.setBackgroundColor(Color.WHITE);
                selectedProducts.remove(product);
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.waterProcessBarBackground));
                selectedProducts.add(product);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setAdapter(alimentAdapter);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(manager);
    }

    private void initComponents() {
        recyclerView = findViewById(R.id.food_list_lv_list_of_aliments);
        Button btnDone = findViewById(R.id.food_list_btn_done);

        new HttpManager() {
            @Override
            protected void onPostExecute(String jsonFile) {
                products = JsonParser.buildResponseFromJsonString(jsonFile);
                buildRecyclerView();
            }
        }.execute(URL);

        user = (User) intent.getSerializableExtra(CURRENT_USER);

        btnDone.setOnClickListener(v -> {
            if (selectedProducts.size() > 0) {
                List<EatenAliment> alimentList = new ArrayList<>();
                for (int i = 0; i < selectedProducts.size(); i++) {
                    Product product = selectedProducts.get(i);
                    alimentList.add(new EatenAliment(product.getName(), product.getQuantity(), product.getCaloriesNumber(), new Date(), user.getId()));
                }
                mDisposable.add(alimentsDao.insertAliment(alimentList)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
            }
            setResult(RESULT_OK, intent);
            finish();
        });


    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.aliments_search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        ArrayList<Product> filteredlist = new ArrayList<>();

        for (Product item : products) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        }
        alimentAdapter.filterList(filteredlist);
    }

}
