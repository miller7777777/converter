package mnz.creatori.converter.databases;


import java.util.ArrayList;

import mnz.creatori.converter.Entity.Valute;

public class Database {
    public ArrayList<String> getValuteNames() {

        //Метод должен гарантированно возвращать не null!


        ArrayList<String> valNames = new ArrayList<>();
        valNames.add("none2");
        valNames.add("none3");
        return valNames;
    }

    public void update(ArrayList<Valute> valuteNames) {
    }
}
