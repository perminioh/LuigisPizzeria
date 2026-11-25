
import java.io.BufferedWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class BestellSpeicher {

    // CSV file in the project folder
    private final Path dateiPfad = Paths.get("orders.csv");


    // Save one order
    public void speichereBestellung(Bestellung bestellung)
            throws IOException {

        // Check whether the CSV file needs a header
        boolean headerNotwendig =
                !Files.exists(dateiPfad) || Files.size(dateiPfad) == 0;


        // Open the file in append mode
        try (BufferedWriter writer = Files.newBufferedWriter(

                dateiPfad,

                StandardCharsets.UTF_8,

                StandardOpenOption.CREATE,

                StandardOpenOption.APPEND
        )) {


            // Add CSV header only once
            if (headerNotwendig) {

                writer.write(
                        "Kundenname;Pizza;Groesse;ExtraKaese;Gesamtpreis"
                );

                writer.newLine();
            }


            // Write order data
            String csvZeile =
                    escapeCsv(bestellung.getKundenName()) + ";" +
                            escapeCsv(bestellung.getPizza()) + ";" +
                            escapeCsv(bestellung.getGroesse()) + ";" +
                            bestellung.getExtraKaeseCsv() + ";" +
                            bestellung.getPreisCsv();


            writer.write(csvZeile);

            writer.newLine();
        }
    }


    // Escape CSV values correctly
    private String escapeCsv(String text) {

        if (text.contains(";") || text.contains("\"")) {

            text = text.replace("\"", "\"\"");

            return "\"" + text + "\"";
        }

        return text;
    }
}
