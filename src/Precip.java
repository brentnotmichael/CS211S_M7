import java.text.DecimalFormat;
import java.util.Comparator;

public class Precip implements Comparable<Precip> {
    // instance data
    private int year;
    private int month;
    private double rain;
    private double snow;
    private double tempAvg;
    private double tempMax;
    private double waterLevel;
    DecimalFormat numberFormat = new DecimalFormat("#.00");

    // comparators
    public final static Comparator<Precip> PRECIP_DATE_COMPARATOR = new PrecipDateComparator();
    public final static Comparator<Precip> PRECIP_AMOUNT_COMPARATOR = new PrecipAmountComparator();
    public final static Comparator<Precip> PRECIP_WATERLEVEL_COMPARATOR = new PrecipWaterLevelComparator();

    // constructors
    public Precip(int year, int month, double rain, double snow, double tempAvg, double tempMax) {
        this.year = year;
        this.month = month;
        this.rain = Double.parseDouble(numberFormat.format(rain));
        this.snow = Double.parseDouble(numberFormat.format(snow));
        this.tempAvg = Double.parseDouble(numberFormat.format(tempAvg));
        this.tempMax = Double.parseDouble(numberFormat.format(tempMax));
        this.waterLevel = 0.0;
    }
    public Precip(int year, int month, double rain, double snow, double tempAvg, double tempMax, double waterLevel) {
        this(year, month, rain, snow, tempAvg, tempMax);
        this.waterLevel =  Double.parseDouble(numberFormat.format(waterLevel));
    }

    // getters
    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Double getRain() {
        return rain;
    }

    public Double getSnow() {
        return snow;
    }

    public Double getTempAvg() {
        return tempAvg;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public Double getWaterLevel() {
        return waterLevel;
    }

    // only one setter for waterLevel -- data should not be modified
    public void setWaterLevel(double waterLevel) {
        this.waterLevel = Double.parseDouble(numberFormat.format(waterLevel));
    }

    // toString
    @Override
    public String toString() {
        return year + "-" + month + "\t" + rain + "\t" + snow + "\t" + tempAvg + "\t" + tempMax + "\t" + waterLevel;
    }

    // equals
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Precip) {
            Precip other = (Precip) obj;
            if (this.year != other.year) {
                return false;
            } else if (this.month != other.month) {
                return false;
            } else if (this.rain != other.rain) {
                return false;
            } else if (this.snow != other.snow) {
                return false;
            } else if (this.tempAvg != other.tempAvg) {
                return false;
            } else if (this.tempMax != other.tempMax) {
                return false;
            } else if (this.waterLevel != other.waterLevel) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(Precip other) {
        // compare two Precipitation objects by year, month
        if (this.getYear().compareTo(other.getYear()) == 0) {
            return this.getMonth().compareTo(other.getMonth());
        } else {
            return this.getYear().compareTo(other.getYear());
        }
    }

    private static class PrecipDateComparator implements Comparator<Precip> {
        @Override
        public int compare(Precip o1, Precip o2) {
            if (o1.getYear().compareTo(o2.getYear()) != 0) {
                return o1.getYear().compareTo(o2.getYear());
            } else if (o1.getMonth().compareTo(o2.getMonth()) != 0) {
                return o1.getMonth().compareTo(o2.getMonth());
            } else if (o1.getRain().compareTo(o2.getRain()) != 0) {
                return o1.getRain().compareTo(o2.getRain());
            } else if (o1.getSnow().compareTo(o2.getSnow()) != 0) {
                return o1.getSnow().compareTo(o2.getSnow());
            } else if (o1.getTempAvg().compareTo(o2.getTempAvg()) != 0) {
                return o1.getTempAvg().compareTo(o2.getTempAvg());
            } else if (o1.getTempMax().compareTo(o2.getTempMax()) != 0) {
                return o1.getTempMax().compareTo(o2.getTempMax());
            } else return o1.getWaterLevel().compareTo(o2.getWaterLevel());
        }
    }

    private static class PrecipAmountComparator implements Comparator<Precip> {
        @Override
        public int compare(Precip o1, Precip o2) {
            if (o1.getRain().compareTo(o2.getRain()) != 0) {
                return o1.getRain().compareTo(o2.getRain());
            } else if (o1.getSnow().compareTo(o2.getSnow()) != 0) {
                return o1.getSnow().compareTo(o2.getSnow());
            } else if (o1.getYear().compareTo(o2.getYear()) != 0) {
                return o1.getYear().compareTo(o2.getYear());
            } else if (o1.getMonth().compareTo(o2.getMonth()) != 0) {
                return o1.getMonth().compareTo(o2.getMonth());
            } else if (o1.getTempAvg().compareTo(o2.getTempAvg()) != 0) {
                return o1.getTempAvg().compareTo(o2.getTempAvg());
            } else if (o1.getTempMax().compareTo(o2.getTempMax()) != 0) {
                return o1.getTempMax().compareTo(o2.getTempMax());
            } else return o1.getWaterLevel().compareTo(o2.getWaterLevel());
        }
    }

    private static class PrecipWaterLevelComparator implements Comparator<Precip> {
        @Override
        public int compare(Precip o1, Precip o2) {
            if (o1.getWaterLevel().compareTo(o2.getWaterLevel()) != 0) {
                return o1.getWaterLevel().compareTo(o2.getWaterLevel());
            } else if (o1.getYear().compareTo(o2.getYear()) != 0) {
                return o1.getYear().compareTo(o2.getYear());
            } else if (o1.getMonth().compareTo(o2.getMonth()) != 0) {
                return o1.getMonth().compareTo(o2.getMonth());
            }else if (o1.getRain().compareTo(o2.getRain()) != 0) {
                return o1.getRain().compareTo(o2.getRain());
            } else if (o1.getSnow().compareTo(o2.getSnow()) != 0) {
                return o1.getSnow().compareTo(o2.getSnow());
            } else if (o1.getTempAvg().compareTo(o2.getTempAvg()) != 0) {
                return o1.getTempAvg().compareTo(o2.getTempAvg());
            } else return o1.getTempMax().compareTo(o2.getTempMax());
        }
    }

    /**
     * converts alpha month to integer month
     * @param monthString
     * @return numeric month, -1 if exception is thrown
     * @throws IndexOutOfBoundsException
     */
    public static int monthStringToInt(String monthString) {
        try {
            switch(monthString.toLowerCase().substring(0, 3)) {
                case "jan":
                    return 1;
                case "feb":
                    return 2;
                case "mar":
                    return 3;
                case "apr":
                    return 4;
                case "may":
                    return 5;
                case "jun":
                    return 6;
                case "jul":
                    return 7;
                case "aug":
                    return 8;
                case "sep":
                    return 9;
                case "oct":
                    return 10;
                case "nov":
                    return 11;
                case "dec":
                    return 12;
                default:
                    return -1;
            }
        }
        catch (IndexOutOfBoundsException ex) {
            System.out.println("WARNING: provided monthString must be at least 3 characters long.");
            return -1;
        }
    }

    public static String monthIntToString(int monthInt) {
        switch (monthInt) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return null;
        }
    }

    public static double CtoF(double c) {
        return c * 1.8 + 32;
    }

    public static double FtoC(double f) {
        return (f - 32) / 1.8;
    }

}