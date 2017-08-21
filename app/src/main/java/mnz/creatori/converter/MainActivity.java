package mnz.creatori.converter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import java.util.List;

import mnz.creatori.converter.Entity.Valute;
import mnz.creatori.converter.databases.Database;
import mnz.creatori.converter.logic.ExchangeCalculator;
import mnz.creatori.converter.network.NetworkHelper;

public class MainActivity extends AppCompatActivity {

    private TextView tvInfo;
    private TextView tvFinishCurrencySum;
    private EditText etStartCurrencySum;
    private Spinner currencyStartType;
    private Spinner currencyFinishType;
    private Button computeBtn;
    private NetworkHelper networkHelper;
    private List<Valute> valutes;
    private List<String> valuteNames;
    private Database db;
    private ExchangeCalculator exchangeCalculator;
    private String valStart;
    private String valFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkHelper = new NetworkHelper(this);
        db = new Database(this);
        valutes = networkHelper.getValutes();
        valuteNames = networkHelper.getValuteNames();
        if (valuteNames.size() == 1) {
            showMessage("Network error!");
            valutes = db.getValutes();
            valuteNames = db.getValuteNames();
        } else {
            db.update(valutes);
        }

        Collections.sort(valuteNames);
        String[] names = new String[valuteNames.size()];
        names = valuteNames.toArray(names);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tvInfo = (TextView) findViewById(R.id.tv_info);
        tvFinishCurrencySum = (TextView) findViewById(R.id.tv_finish_currency_sum);
        etStartCurrencySum = (EditText) findViewById(R.id.et_start_currency_sum);

        currencyStartType = (Spinner) findViewById(R.id.et_start_currency_type);
        currencyFinishType = (Spinner) findViewById(R.id.et_finish_currency_type);
        computeBtn = (Button) findViewById(R.id.btn_compute);
        computeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compute();
            }
        });
        computeBtn.setEnabled(false);

        currencyStartType.setAdapter(adapter);
        currencyFinishType.setAdapter(adapter);

        currencyStartType.setPrompt("Start Valute");
        currencyFinishType.setPrompt("Finish Valute");

        etStartCurrencySum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etStartCurrencySum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                tvFinishCurrencySum.setText("?");
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (etStartCurrencySum.getText().toString().length() > 0) {
                    computeBtn.setEnabled(true);
                } else {
                    computeBtn.setEnabled(false);
                    tvFinishCurrencySum.setText("?");
                }
            }
        });

        final String[] finalNames = names;
        currencyStartType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
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
                valFinish = finalNames1[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void compute() {

        exchangeCalculator = new ExchangeCalculator(valutes);

        tvFinishCurrencySum.setText(exchangeCalculator.getResult(etStartCurrencySum.getText().toString(), valStart, valFinish));
    }

    private void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        //TODO: сделать SnackBar
    }
}
