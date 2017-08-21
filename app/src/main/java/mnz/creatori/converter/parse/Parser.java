package mnz.creatori.converter.parse;

import java.util.ArrayList;
import java.util.List;

import mnz.creatori.converter.Entity.Valute;

//Объект класса принимает на вход строку, полученную от NetworkHelper и возвращает ArrayList<Valutes>.
public class Parser {



    ArrayList<Valute> valutes = new ArrayList<>();

    public ArrayList<Valute> parse(String path){

        String result = path;

        while (result.contains("</Valute>")) {

            int t1 = result.indexOf("<Valute");
            int t2 = result.indexOf("</Valute>");
            String temp = result.substring(t1 + 8, t2);

            int t4 = temp.indexOf("ID=");
            int t5 = temp.indexOf(">");
            String id = temp.substring(t4 + 4, t5 - 1);

            t4 = temp.indexOf("<NumCode>");
            t5 = temp.indexOf("</NumCode>");
            String numCode = temp.substring(t4 + 9, t5);

            t4 = temp.indexOf("<CharCode>");
            t5 = temp.indexOf("</CharCode>");
            String charCode = temp.substring(t4 + 10, t5);

            t4 = temp.indexOf("<Nominal>");
            t5 = temp.indexOf("</Nominal>");
            String nominal = temp.substring(t4 + 9, t5);

            t4 = temp.indexOf("<Name>");
            t5 = temp.indexOf("</Name>");
            String name = temp.substring(t4 + 6, t5);

            t4 = temp.indexOf("<Value>");
            t5 = temp.indexOf("</Value>");
            String value = temp.substring(t4 + 7, t5).replace(",", ".");

            valutes.add(new Valute(id, numCode, charCode, nominal, name, value));

            t2 = result.indexOf("</Valute>");
            String temp1 = result.substring(t2 + 9, result.length());
            result = temp1;
        }

        valutes.add(new Valute("36", "643", "RUB", "1", "Российский Рубль", "1.0"));
        return valutes;
    }
}
