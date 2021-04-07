import java.util.ArrayList;

public class PrecipTester {
    public static void main(String[] args) {
        ArrayList<Precip> precipArrayList = new ArrayList<>();
        precipArrayList.add(new Precip(2020, 3, 1.3, 0.0, 62.1, 75.8));
        precipArrayList.get(0).setWaterLevel(3492.1);
        precipArrayList.add(new Precip(2017, 11, 0.41, 4.2, 24.1, 39.8, 3021.72));
        for (Precip p : precipArrayList) {
            System.out.println(p);
        }

    }
}
