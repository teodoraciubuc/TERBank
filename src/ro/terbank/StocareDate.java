package ro.terbank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/** Citește și scrie datele aplicației în fișiere text. */
public class StocareDate {

    private final File directorDate = new File("data");
    private final File directorRapoarte = new File("rapoarte");
    private final File fisierConturi = new File(directorDate, "conturi.txt");
    private final File fisierTranzactii = new File(directorDate, "tranzactii.txt");
    private final File fisierRaport = new File(directorRapoarte, "raport.txt");

    /** Creează directoarele și fișierele lipsă. */
    public StocareDate() throws IOException {
        if (!directorDate.exists()) {
            directorDate.mkdirs();
        }
        if (!fisierConturi.exists()) {
            fisierConturi.createNewFile();
        }
        if (!fisierTranzactii.exists()) {
            fisierTranzactii.createNewFile();
        }
    }

    ArrayList<String> citesteConturi() throws IOException {
        return citesteLinii(fisierConturi);
    }

    ArrayList<String> citesteTranzactii() throws IOException {
        return citesteLinii(fisierTranzactii);
    }

    void salveaza(HashMap<String, ContBancar> conturi, ArrayList<Tranzactie> tranzactii)
            throws IOException {
        BufferedWriter writerConturi = new BufferedWriter(
                new FileWriter(fisierConturi, StandardCharsets.UTF_8));

        try {
            for (ContBancar cont : conturi.values()) {
                writerConturi.write(cont.getNumarCont() + "|" + cont.getTitular() + "|" + cont.getSold());
                writerConturi.newLine();
            }
        } finally {
            writerConturi.close();
        }

        BufferedWriter writerTranzactii = new BufferedWriter(
                new FileWriter(fisierTranzactii, StandardCharsets.UTF_8));

        try {
            for (int i = 0; i < tranzactii.size(); i++) {
                Tranzactie tranzactie = tranzactii.get(i);
                String contSursa = "";
                String contDestinatie = "";

                if (tranzactie.getContSursa() != null) {
                    contSursa = tranzactie.getContSursa();
                }
                if (tranzactie.getContDestinatie() != null) {
                    contDestinatie = tranzactie.getContDestinatie();
                }

                writerTranzactii.write(tranzactie.getIdTranzactie() + "|" + tranzactie.getTip() + "|"
                        + tranzactie.getSuma() + "|" + tranzactie.getDataOra() + "|"
                        + contSursa + "|" + contDestinatie);
                writerTranzactii.newLine();
            }
        } finally {
            writerTranzactii.close();
        }
    }

    void salveazaRaport(String raport) throws IOException {
        if (!directorRapoarte.exists()) {
            directorRapoarte.mkdirs();
        }

        BufferedWriter writer = new BufferedWriter(
                new FileWriter(fisierRaport, StandardCharsets.UTF_8));
        try {
            writer.write(raport);
        } finally {
            writer.close();
        }
    }

    private ArrayList<String> citesteLinii(File fisier) throws IOException {
        ArrayList<String> linii = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fisier, StandardCharsets.UTF_8));

        try {
            String linie = reader.readLine();
            while (linie != null) {
                linii.add(linie);
                linie = reader.readLine();
            }
        } finally {
            reader.close();
        }

        return linii;
    }
}
