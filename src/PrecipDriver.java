
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class PrecipDriver {
    public static void main(String[] args) {
        int errCount = 0;
        String precipFileName = "hetchhetchyrainfall2008_2020.csv";
        String waterLevelFileName = "hetchhetchywaterlevels2008_2020.csv";
        List<Precip> precipList = new ArrayList<>();
        try (Scanner precipFileScanner = new Scanner(new FileReader(new File (precipFileName)));
             Scanner waterLevelFileScanner = new Scanner(new FileReader(new File(waterLevelFileName)))) {
                precipList = (errCount == 0) ? loadPrecipData(precipFileScanner, precipList) : precipList;
                if (errCount == 0) {
                    loadWaterLevelFile(waterLevelFileScanner, precipList);
                }

        } catch (FileNotFoundException ex) {
            System.out.println("Unable to continue: " + ex.getMessage());
            errCount++;
        }
        LocalDateTime start = LocalDateTime.now();
        System.out.print("Searching " + precipList.size() + " records for most precipitation from 2008 to 2020... ");
        Precip mostRain = null;
        Precip mostSnow = null;
        Precip mostRainAndSnow = null;
        Precip lowestLevel = null;
        Precip highestLevel = null;
        double maxRain = 0.0;
        double maxSnow = 0.0;
        double maxRainAndSnow = 0.0;
        double maxLevel = 0.0;
        double minLevel = 9999999.99;
        for (Precip p : precipList) {
            //System.out.println(p);
            if (p.getRain() > maxRain) {
                maxRain = p.getRain();
                mostRain = p;
            }
            if (p.getSnow() > maxSnow) {
                maxSnow = p.getSnow();
                mostSnow = p;
            }
            double tempRainAndSnow = p.getRain() + p.getSnow();
            if (tempRainAndSnow > maxRainAndSnow) {
                maxRainAndSnow = tempRainAndSnow;
                mostRainAndSnow = p;
            }
            if (p.getWaterLevel() > maxLevel) {
                maxLevel = p.getWaterLevel();
                highestLevel = p;
            }
            if (p.getWaterLevel() < minLevel) {
                minLevel = p.getWaterLevel();
                lowestLevel = p;
            }
        }
        LocalDateTime end = LocalDateTime.now();
        System.out.println("Done. Took " + Duration.between(start,end).toMillis() + " milliseconds.");
        System.out.println("\tMost rain was " + mostRain.getRain() + " inches in " + Precip.monthIntToString(mostRain.getMonth()) + " " + mostRain.getYear());
        System.out.println("\tMost snow was " + mostSnow.getSnow() + " inches in " + Precip.monthIntToString(mostSnow.getMonth()) + " " + mostSnow.getYear());
        System.out.println("\tMost rain and snow was " + (mostRainAndSnow.getRain() + mostRainAndSnow.getSnow()  + " inches in " + Precip.monthIntToString(mostRainAndSnow.getMonth()) + " " + mostRainAndSnow.getYear()));
        System.out.println("\tHighest water level was " + highestLevel.getWaterLevel() + " inches in " + Precip.monthIntToString(highestLevel.getMonth()) + " " + highestLevel.getYear());
        System.out.println("\tLowest water level was " + lowestLevel.getWaterLevel() + " inches in " + Precip.monthIntToString(lowestLevel.getMonth()) + " " + lowestLevel.getYear());

    }

    private static File loadFile (String name) {
        return new File(name);
    }

    private static Scanner initScanner(File file) {
        try {
            return new Scanner(file);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    private static List<Precip> loadPrecipData (Scanner fScan, List<Precip> list) {
        System.out.print("Loading precipitation data... ");
        LocalDateTime start = LocalDateTime.now();
        int records = 0;
        if (fScan.hasNext()) { // if the file is empty bail out now
            fScan.next();   // file has headers, skip them
            while (fScan.hasNext()) {
                Scanner lineScan = new Scanner(fScan.next());
                lineScan.useDelimiter(",");
                // DATE,PRCP,SNOW,TAVG,TMAX
                // 08-Jan,9.98,27.5,35.9,42.8
                while (lineScan.hasNext()) {
                    Scanner dateScan = new Scanner(lineScan.next());
                    // 08-Jan
                    dateScan.useDelimiter("-");
                    // all values are 2 digit years after 1999; add "20" before scanned value
                    String yearString = "20" + dateScan.next();
                    int year = Integer.valueOf(yearString);
                    // need to convert 3 character month from scan to integer month
                    int month = Precip.monthStringToInt(dateScan.next());
                    dateScan.close();
                    double rain = Double.valueOf(lineScan.next());
                    double snow = Double.valueOf(lineScan.next());
                    double tempAvg = Precip.CtoF(Double.valueOf(lineScan.next()));
                    double tempMax = Precip.CtoF(Double.valueOf(lineScan.next()));
                    list.add(new Precip(year, month, rain, snow, tempAvg, tempMax));
                    records++;
                }
            }
        }
        LocalDateTime end = LocalDateTime.now();
        System.out.println("Done. Loaded " + records + " records in " + Duration.between(start, end).toMillis() + " milliseconds.");
        return list;
    }

    public static void loadWaterLevelFile (Scanner fScan, List<Precip> list) {
        System.out.print("Loading water level data... ");
        LocalDateTime start = LocalDateTime.now();
        int records = 0;
        if (fScan.hasNext()) { // if the file is empty bail out now
            Integer currYear = null;
            Integer currMonth = null;
            Integer prevYear = null;
            Integer prevMonth = null;
            Double prevLevel = null;
            Double total = 0.0;
            int readings = 0;
            Double avgLevel = 0.0;
            // averages a month of water level readings and inserts it into the Precip data
            while (fScan.hasNext()) {
                records++;
                Scanner lineScan = new Scanner(fScan.next());   // YYYY-MM, double
                // first get the date
                lineScan.useDelimiter(",");
                while (lineScan.hasNext()) {
                    // get each data field

                    Scanner dateScan = new Scanner(lineScan.next());
                    dateScan.useDelimiter("-");
                    Integer scanMonth = Precip.monthStringToInt(dateScan.next());
                    String yearString = "20" + dateScan.next();
                    Integer scanYear = Integer.valueOf(yearString);
                    dateScan.close();
                    // System.out.println("RECORD " + records + " :: prevLevel = " + prevLevel);
                    Double scanLevel = Double.valueOf(lineScan.next());
                    if (currMonth == null) {
                        prevMonth = scanMonth;
                        currMonth = scanMonth;
                        prevYear = scanYear;
                        currYear = scanYear;
                        total = scanLevel;
                    } else if (!currMonth.equals(scanMonth)) {
                        // new month
                        prevMonth = currMonth;
                        currMonth = scanMonth;
                        prevYear = currYear;
                        currYear = scanYear;

                        // use an Iterator to tabulate the average water level
                        Iterator<Precip> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            Precip p = iterator.next();
                            // !iterator.hasNext() allows setting the water level of the last date range (Dec 2020)
                            if (p.getMonth().equals(prevMonth) && p.getYear().equals((prevYear)) || !iterator.hasNext()) {
                                avgLevel = total/readings;
                               // System.out.println("Setting average water level for " + p.getYear() + "-" + p.getMonth() + " to " + avgLevel + " at record # " + records);
                                p.setWaterLevel(avgLevel);
                                // System.out.println(p);
                                total = 0.0;
                                readings = 0;
                                prevLevel = scanLevel;
                                break;
                            }
                        }
                    }
                    // each record adds to the cumulative total for the current month
                    total += Double.valueOf(scanLevel);
                    readings++;
                   // prevLevel = scanLevel;
                }
            }
        }
        LocalDateTime end = LocalDateTime.now();
        System.out.println("Done. Loaded " + records + " records in " + Duration.between(start, end).toMillis() + " milliseconds.");
    }
}
