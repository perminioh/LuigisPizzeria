
import java.util.Locale;

public class Bestellung {

    // 1. Private attributes
    private final String kundenName;
    private final String pizza;
    private final String groesse;
    private final boolean extraKaese;
    private final int gesamtpreisCent;


    // 2. Constructor
    public Bestellung(String kundenName, String pizza,
                      String groesse, boolean extraKaese) {

        this.kundenName = kundenName;
        this.pizza = pizza;
        this.groesse = groesse;
        this.extraKaese = extraKaese;

        this.gesamtpreisCent =
                berechneGesamtpreisCent(pizza, groesse, extraKaese);
    }


    // 3. Calculate the price
    public static int berechneGesamtpreisCent(
            String pizza, String groesse, boolean extraKaese) {

        int grundpreisCent;

        switch (pizza) {

            case "Margherita":
                grundpreisCent = 800;
                break;

            case "Salami":
                grundpreisCent = 1000;
                break;

            case "Funghi":
                grundpreisCent = 900;
                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown pizza: " + pizza
                );
        }


        int groessenAufpreisCent;

        switch (groesse) {

            case "Klein":
                groessenAufpreisCent = 0;
                break;

            case "Mittel":
                groessenAufpreisCent = 200;
                break;

            case "Gross":
                groessenAufpreisCent = 400;
                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown size: " + groesse
                );
        }


        int kaesePreisCent = 0;

        if (extraKaese) {
            kaesePreisCent = 100;
        }


        return grundpreisCent
                + groessenAufpreisCent
                + kaesePreisCent;
    }


    // 4. Getters
    public String getKundenName() {
        return kundenName;
    }

    public String getPizza() {
        return pizza;
    }

    public String getGroesse() {
        return groesse;
    }

    public boolean isExtraKaese() {
        return extraKaese;
    }

    public int getGesamtpreisCent() {
        return gesamtpreisCent;
    }


    // 5. Convert price into display format
    public String getPreisAnzeige() {

        int euro = gesamtpreisCent / 100;
        int cent = gesamtpreisCent % 100;

        return String.format(
                Locale.GERMANY,
                "%d,%02d €",
                euro,
                cent
        );
    }


    // 6. Convert price into CSV format
    public String getPreisCsv() {

        int euro = gesamtpreisCent / 100;
        int cent = gesamtpreisCent % 100;

        return String.format(
                Locale.ROOT,
                "%d.%02d",
                euro,
                cent
        );
    }


    // 7. Extra cheese as CSV text
    public String getExtraKaeseCsv() {

        if (extraKaese) {
            return "Ja";
        }

        return "Nein";
    }
}
