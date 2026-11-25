
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;

import java.awt.Font;

import java.io.File;
import java.io.IOException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.Locale;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class PizzeriaGUI extends JFrame {


    // ======================================
    // 1. INPUT COMPONENTS
    // ======================================

    private final JTextField kundenNameFeld =
            new JTextField(18);


    private final JComboBox<String> pizzaAuswahl =
            new JComboBox<>(
                    new String[]{
                            "Margherita",
                            "Salami",
                            "Funghi"
                    }
            );


    private final JRadioButton kleinRadio =
            new JRadioButton("Small", true);

    private final JRadioButton mittelRadio =
            new JRadioButton("Medium");

    private final JRadioButton grossRadio =
            new JRadioButton("Large");


    private final JCheckBox extraKaeseBox =
            new JCheckBox("Extra cheese (+1 euro)");


    // ======================================
    // 2. OUTPUT COMPONENTS
    // ======================================

    private final JLabel preisAnzeige =
            new JLabel("Total price: not calculated");


    private final JTextArea meldungsBereich =
            new JTextArea(5, 40);


    // ======================================
    // 3. BUTTONS
    // ======================================

    private final JButton berechnenButton =
            new JButton("Calculate");

    private final JButton speichernButton =
            new JButton("Save Order");

    private final JButton leerenButton =
            new JButton("Clear");


    // ======================================
    // 4. OTHER OBJECTS AND VARIABLES
    // ======================================

    private final BestellSpeicher speicher =
            new BestellSpeicher();


    private boolean preisBerechnet = false;

    private int berechneterPreisCent = 0;


    // ======================================
    // 5. CONSTRUCTOR
    // ======================================

    public PizzeriaGUI() {

        setTitle("Luigi's Pizzeria - Order System");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        getRootPane().setBorder(
                new EmptyBorder(12, 12, 12, 12)
        );


        // Build the window sections
        add(createHeader(), BorderLayout.NORTH);

        add(createOrderPanel(), BorderLayout.CENTER);

        add(createMessagePanel(), BorderLayout.SOUTH);


        // Register button and selection events
        addEventListeners();


        // Save is unavailable until a price is calculated
        speichernButton.setEnabled(false);


        setMinimumSize(new Dimension(760, 570));

        setSize(850, 650);

        setLocationRelativeTo(null);


        logMessage("Welcome to Luigi's Pizzeria!");
    }


    // ======================================
    // 6. HEADER WITH LOGO
    // ======================================

    private JPanel createHeader() {

        JPanel header = new JPanel(
                new BorderLayout(15, 5)
        );


        JLabel logoLabel = createImageLabel(
                "images/logo.png",
                180,
                70,
                "LUIGI'S PIZZERIA"
        );


        JLabel title = new JLabel(
                "Luigi's Pizzeria - Order Management"
        );

        title.setFont(
                new Font("Arial", Font.BOLD, 22)
        );


        header.add(logoLabel, BorderLayout.WEST);

        header.add(title, BorderLayout.CENTER);


        return header;
    }


    // ======================================
    // 7. ORDER FORM AND PIZZA IMAGE
    // ======================================

    private JPanel createOrderPanel() {

        JPanel mainPanel = new JPanel(
                new BorderLayout(15, 10)
        );


        // Form containing all input components
        JPanel formPanel = new JPanel(
                new GridLayout(0, 2, 10, 12)
        );

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Order details"
                )
        );


        // Customer name
        formPanel.add(new JLabel("Customer name:"));

        formPanel.add(kundenNameFeld);


        // Pizza selection
        formPanel.add(new JLabel("Choose pizza:"));

        formPanel.add(pizzaAuswahl);


        // Size selection
        ButtonGroup groessenGruppe = new ButtonGroup();

        groessenGruppe.add(kleinRadio);

        groessenGruppe.add(mittelRadio);

        groessenGruppe.add(grossRadio);


        JPanel groessenPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 5, 0)
        );

        groessenPanel.add(kleinRadio);

        groessenPanel.add(mittelRadio);

        groessenPanel.add(grossRadio);


        formPanel.add(new JLabel("Choose size:"));

        formPanel.add(groessenPanel);


        // Extra cheese
        formPanel.add(new JLabel("Extras:"));

        formPanel.add(extraKaeseBox);


        // Pizza image panel
        JPanel imagePanel = new JPanel(
                new BorderLayout()
        );

        imagePanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Pizza preview"
                )
        );


        JLabel pizzaLabel = createImageLabel(
                "images/pizza.png",
                210,
                165,
                "Pizza image unavailable"
        );


        imagePanel.add(
                pizzaLabel,
                BorderLayout.CENTER
        );


        // Put form and image side by side
        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                imagePanel,
                BorderLayout.EAST
        );


        // Buttons and price display
        JPanel actionPanel = new JPanel(
                new BorderLayout(10, 5)
        );


        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );


        buttonPanel.add(berechnenButton);

        buttonPanel.add(speichernButton);

        buttonPanel.add(leerenButton);


        preisAnzeige.setFont(
                new Font("Arial", Font.BOLD, 17)
        );


        actionPanel.add(
                preisAnzeige,
                BorderLayout.NORTH
        );

        actionPanel.add(
                buttonPanel,
                BorderLayout.CENTER
        );


        mainPanel.add(
                actionPanel,
                BorderLayout.SOUTH
        );


        return mainPanel;
    }


    // ======================================
    // 8. IMAGE LOADING
    // ======================================

    private JLabel createImageLabel(
            String imagePath,
            int width,
            int height,
            String fallbackText
    ) {

        File imageFile = new File(imagePath);


        // Check whether image exists
        if (!imageFile.isFile()) {

            JLabel fallback = new JLabel(
                    fallbackText,
                    SwingConstants.CENTER
            );

            fallback.setPreferredSize(
                    new Dimension(width, height)
            );

            return fallback;
        }


        // Load the image
        ImageIcon originalIcon =
                new ImageIcon(imagePath);


        // Check whether the image could be loaded
        if (originalIcon.getIconWidth() <= 0) {

            JLabel fallback = new JLabel(
                    fallbackText,
                    SwingConstants.CENTER
            );

            fallback.setPreferredSize(
                    new Dimension(width, height)
            );

            return fallback;
        }


        // Keep the original aspect ratio
        double scale = Math.min(
                (double) width / originalIcon.getIconWidth(),
                (double) height / originalIcon.getIconHeight()
        );


        int newWidth = Math.max(
                1,
                (int) (originalIcon.getIconWidth() * scale)
        );

        int newHeight = Math.max(
                1,
                (int) (originalIcon.getIconHeight() * scale)
        );


        // Resize image
        Image scaledImage =
                originalIcon.getImage().getScaledInstance(
                        newWidth,
                        newHeight,
                        Image.SCALE_SMOOTH
                );


        // Display resized image using ImageIcon
        JLabel imageLabel =
                new JLabel(new ImageIcon(scaledImage));


        imageLabel.setPreferredSize(
                new Dimension(width, height)
        );


        return imageLabel;
    }


    // ======================================
    // 9. SCROLLABLE MESSAGE AREA
    // ======================================

    private JPanel createMessagePanel() {

        meldungsBereich.setEditable(false);

        meldungsBereich.setLineWrap(true);

        meldungsBereich.setWrapStyleWord(true);


        JScrollPane scrollPane =
                new JScrollPane(meldungsBereich);


        scrollPane.setPreferredSize(
                new Dimension(700, 125)
        );


        JPanel messagePanel = new JPanel(
                new BorderLayout()
        );


        messagePanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Messages"
                )
        );


        messagePanel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        return messagePanel;
    }


    // ======================================
    // 10. EVENT LISTENERS
    // ======================================

    private void addEventListeners() {


        // Calculate button
        berechnenButton.addActionListener(
                event -> berechnePreis()
        );


        // Save button
        speichernButton.addActionListener(
                event -> speichereBestellung()
        );


        // Clear button
        leerenButton.addActionListener(
                event -> leereFormular()
        );


        // Pizza selection changed
        pizzaAuswahl.addActionListener(
                event -> markierePreisAlsUngueltig()
        );


        // Size selection changed
        kleinRadio.addActionListener(
                event -> markierePreisAlsUngueltig()
        );

        mittelRadio.addActionListener(
                event -> markierePreisAlsUngueltig()
        );

        grossRadio.addActionListener(
                event -> markierePreisAlsUngueltig()
        );


        // Extra cheese changed
        extraKaeseBox.addActionListener(
                event -> markierePreisAlsUngueltig()
        );
    }


    // ======================================
    // 11. GET SELECTED SIZE
    // ======================================

    private String getSelectedSize() {

        if (mittelRadio.isSelected()) {

            return "Mittel";
        }

        if (grossRadio.isSelected()) {

            return "Gross";
        }

        return "Klein";
    }


    // ======================================
    // 12. CALCULATE PRICE
    // ======================================

    private void berechnePreis() {


        String pizza =
                (String) pizzaAuswahl.getSelectedItem();


        String groesse = getSelectedSize();


        boolean extraKaese =
                extraKaeseBox.isSelected();


        // Calculate price using Bestellung class
        berechneterPreisCent =
                Bestellung.berechneGesamtpreisCent(
                        pizza,
                        groesse,
                        extraKaese
                );


        // Remember that the current price is valid
        preisBerechnet = true;


        // Enable save button
        speichernButton.setEnabled(true);


        int euro = berechneterPreisCent / 100;

        int cent = berechneterPreisCent % 100;


        String preisText = String.format(
                Locale.GERMANY,
                "%d,%02d €",
                euro,
                cent
        );


        preisAnzeige.setText(
                "Total price: " + preisText
        );


        logMessage(
                "Price calculated: " + preisText
        );
    }


    // ======================================
    // 13. INVALIDATE OLD PRICE
    // ======================================

    private void markierePreisAlsUngueltig() {

        preisBerechnet = false;

        speichernButton.setEnabled(false);


        preisAnzeige.setText(
                "Total price: please calculate again"
        );
    }


    // ======================================
    // 14. SAVE ORDER
    // ======================================

    private void speichereBestellung() {


        // Get customer name
        String kundenName =
                kundenNameFeld.getText().trim();


        // Check empty name
        if (kundenName.isEmpty()) {

            logMessage(
                    "ERROR: Please enter the customer's name."
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the customer's name.",
                    "Missing customer name",
                    JOptionPane.WARNING_MESSAGE
            );

            kundenNameFeld.requestFocus();

            return;
        }


        // Reject line breaks to keep one order per CSV row
        if (kundenName.contains("\n")
                || kundenName.contains("\r")) {

            logMessage(
                    "ERROR: Customer name cannot contain line breaks."
            );

            return;
        }


        // Check whether price is up to date
        if (!preisBerechnet) {

            logMessage(
                    "Please calculate the price again before saving."
            );

            return;
        }


        // Get current form values
        String pizza =
                (String) pizzaAuswahl.getSelectedItem();

        String groesse = getSelectedSize();

        boolean extraKaese =
                extraKaeseBox.isSelected();


        // Create order object
        Bestellung bestellung = new Bestellung(
                kundenName,
                pizza,
                groesse,
                extraKaese
        );


        // Safety check: price must still match
        if (bestellung.getGesamtpreisCent()
                != berechneterPreisCent) {

            markierePreisAlsUngueltig();

            logMessage(
                    "Price changed. Please calculate again."
            );

            return;
        }


        try {


            // Save order in CSV
            speicher.speichereBestellung(bestellung);


            logMessage(
                    "Order saved successfully for "
                            + kundenName
                            + ". Total: "
                            + bestellung.getPreisAnzeige()
            );


            // Prevent saving the same calculated order twice
            preisBerechnet = false;

            speichernButton.setEnabled(false);


            preisAnzeige.setText(
                    "Order saved. Clear for next customer."
            );


        } catch (IOException e) {


            logMessage(
                    "ERROR: Could not save order: "
                            + e.getMessage()
            );


            JOptionPane.showMessageDialog(
                    this,
                    "The order could not be saved.\n"
                            + e.getMessage(),
                    "File error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // ======================================
    // 15. CLEAR FORM
    // ======================================

    private void leereFormular() {


        kundenNameFeld.setText("");


        pizzaAuswahl.setSelectedIndex(0);


        kleinRadio.setSelected(true);


        extraKaeseBox.setSelected(false);


        preisBerechnet = false;

        berechneterPreisCent = 0;


        speichernButton.setEnabled(false);


        preisAnzeige.setText(
                "Total price: not calculated"
        );


        kundenNameFeld.requestFocus();


        logMessage(
                "Form cleared. Ready for the next customer."
        );
    }


    // ======================================
    // 16. WRITE MESSAGE TO MESSAGE AREA
    // ======================================

    private void logMessage(String message) {

        String time = LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss")
        );


        meldungsBereich.append(
                "[" + time + "] " + message
                        + System.lineSeparator()
        );


        // Automatically scroll to the latest message
        meldungsBereich.setCaretPosition(
                meldungsBereich.getDocument().getLength()
        );
    }
}

