package mnz.creatori.converter.logic;

import android.util.Log;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import mnz.creatori.converter.Entity.Valute;

public class Calculator {

    private String strSumFirst;
    private String strSumSecond;
    private String valStart;
    private String valFinish;
    ArrayList<Valute> valutes;
    private Valute valuteStart;
    private Valute valuteFinish;

    public Calculator(ArrayList<Valute> valutes) {

        this.valutes = valutes;
    }

    public String getResult(String s, String valStart, String valFinish) {

        //метод должен гарантированно возвращать какой-то String

        Log.d("Calculator", "s = " + s + " valStart = " + valStart + " valFinish = " + valFinish);

        for (int i = 0; i < valutes.size(); i++) {

            if(valutes.get(i).getName().equals(valStart)){
                valuteStart = valutes.get(i);
            }

            if(valutes.get(i).getName().equals(valFinish)){
                valuteFinish = valutes.get(i);
            }
        }

        double sum1 = Double.parseDouble(s);
        double value1 = Double.parseDouble(valuteStart.getValue());
        double value2 = Double.parseDouble(valuteFinish.getValue());
        int nominal1 = Integer.parseInt(valuteStart.getNominal());
        int nominal2 = Integer.parseInt(valuteFinish.getNominal());
        double sum2 = sum1 * value1 * nominal2/ (value2 * nominal1);

        sum2 = new BigDecimal(sum2).setScale(4, RoundingMode.UP).doubleValue();

        try {
            return String.valueOf(sum2);
        } catch (Exception e) {
            e.printStackTrace();
            return "Uncorrect result";
        }
    }
}
