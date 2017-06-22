package mnz.creatori.converter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import mnz.creatori.converter.Entity.Valute;
import mnz.creatori.converter.databases.Database;
import mnz.creatori.converter.logic.Calculator;
import mnz.creatori.converter.network.NetworkHelper;

public class MainActivity extends AppCompatActivity {

    private String[] fakeData = {"one", "two", "three", "four", "five"};
    private TextView tvInfo;
    private TextView tvFinishCurrencySum;
    private EditText etStartCurrencySum;
    private Spinner currencyStartType;
    private Spinner currencyFinishType;
    private Button computeBtn;
    private NetworkHelper networkHelper;
    private ArrayList<Valute> valutes;
    private ArrayList<String> valuteNames;
    private Database db;
    private Calculator calculator;
    private String valStart;
    private String valFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkHelper = new NetworkHelper();
        db = new Database();
//        valutes = networkHelper.getValutes();
        valuteNames = networkHelper.getValuteNames();
        if(valuteNames.size() == 1){
            showMessage("Network error!");
            valuteNames = db.getValuteNames();
        }

        db.update(valuteNames);

        Collections.sort(valuteNames);
        String[] names = new String[valuteNames.size()];
        names = valuteNames.toArray(names);

//        String[] names = {"none"};




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tvInfo = (TextView) findViewById(R.id.tv_info);
        tvFinishCurrencySum = (TextView) findViewById(R.id.tv_finish_currency_sum);
        etStartCurrencySum = (EditText) findViewById(R.id.et_start_currency_sum);
        //TODO: сделать поле ввода активным
        //TODO: Если names.lenght = 1, сделать поле неактивным

        currencyStartType = (Spinner) findViewById(R.id.et_start_currency_type);
        currencyFinishType = (Spinner) findViewById(R.id.et_finish_currency_type);
        computeBtn = (Button)findViewById(R.id.btn_compute);
        computeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compute();
            }
        });

        currencyStartType.setAdapter(adapter);
        currencyFinishType.setAdapter(adapter);

        currencyStartType.setPrompt("Start Valute");
        currencyFinishType.setPrompt("Finish Valute");

//        currencyStartType.setSelection(2);
//        currencyFinishType.setSelection(2);
        final String[] finalNames = names;
        currencyStartType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                valStart = finalNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final String[] finalNames1 = names;
        currencyFinishType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                valFinish = finalNames1[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





    }

    private void compute() {


        tvFinishCurrencySum.setText(calculator.getResult(etStartCurrencySum.getText().toString(), valStart, valFinish));


    }

    private void showMessage(String s) {

        //TODO: написать метод showMessage()
    }
}
