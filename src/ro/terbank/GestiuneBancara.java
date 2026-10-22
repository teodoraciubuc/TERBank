package ro.terbank;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/** Gestionează conturile, tranzacțiile și statisticile aplicației. */
public class GestiuneBancara {

    private final HashMap<String, ContBancar> conturi = new HashMap<>();
    private final ArrayList<Tranzactie> tranzactii = new ArrayList<>();
    private final StocareDate stocareDate;
    private int urmatorulNumarTranzactie = 1;

    /** Creează gestiunea și încarcă datele salvate. */
    public GestiuneBancara() throws IOException {
        stocareDate = new StocareDate();
        incarcaDate();
    }

    /** Creează un cont nou cu soldul zero. */
    public void creeazaCont(String numarCont, String titular) {
        ContBancar cont = new ContBancar(numarCont, titular);

        if (conturi.containsKey(cont.getNumarCont())) {
            throw new IllegalArgumentException("Există deja contul " + cont.getNumarCont() + ".");
        }

        conturi.put(cont.getNumarCont(), cont);
    }

    /** Returnează conturile înregistrate. */
    public ArrayList<ContBancar> getConturi() {
        ArrayList<ContBancar> listaConturi = new ArrayList<>();
        for (ContBancar cont : conturi.values()) {
            listaConturi.add(cont);
        }
        return listaConturi;
    }

    /** Caută un cont după număr. */
    public ContBancar cautaCont(String numarCont) {
        String numarValid = ContBancar.valideazaNumarCont(numarCont);
        ContBancar cont = conturi.get(numarValid);

        if (cont == null) {
            throw new IllegalArgumentException("Contul " + numarValid + " nu există.");
        }

        return cont;
    }

    /** Depune bani și adaugă tranzacția în istoric. */
    public void depune(String numarCont, double suma) {
        valideazaSuma(suma);
        ContBancar cont = cautaCont(numarCont);
        double soldNou = cont.getSold() + suma;
        valideazaSoldNou(soldNou);
        cont.actualizeazaSold(soldNou);
        adaugaTranzactie(TipTranzactie.DEPUNERE, suma, null, cont.getNumarCont());
    }

    /** Retrage bani și adaugă tranzacția în istoric. */
    public void retrage(String numarCont, double suma) throws FonduriInsuficienteException {
        valideazaSuma(suma);
        ContBancar cont = cautaCont(numarCont);

        if (cont.getSold() < suma) {
            throw new FonduriInsuficienteException(cont.getNumarCont());
        }

        double soldNou = cont.getSold() - suma;
        cont.actualizeazaSold(soldNou);
        adaugaTranzactie(TipTranzactie.RETRAGERE, suma, cont.getNumarCont(), null);
    }

    /** Transferă bani între două conturi și adaugă tranzacția în istoric. */
    public void transfera(String numarSursa, String numarDestinatie, double suma)
            throws FonduriInsuficienteException {
        valideazaSuma(suma);
        ContBancar sursa = cautaCont(numarSursa);
        ContBancar destinatie = cautaCont(numarDestinatie);

        if (sursa.getNumarCont().equals(destinatie.getNumarCont())) {
            throw new IllegalArgumentException("Conturile trebuie să fie diferite.");
        }
        if (sursa.getSold() < suma) {
            throw new FonduriInsuficienteException(sursa.getNumarCont());
        }

        double soldSursaNou = sursa.getSold() - suma;
        double soldDestinatieNou = destinatie.getSold() + suma;
        valideazaSoldNou(soldDestinatieNou);
        sursa.actualizeazaSold(soldSursaNou);
        destinatie.actualizeazaSold(soldDestinatieNou);
        adaugaTranzactie(TipTranzactie.TRANSFER, suma,
                sursa.getNumarCont(), destinatie.getNumarCont());
    }

    /** Returnează toate tranzacțiile. */
    public ArrayList<Tranzactie> getTranzactii() {
        return tranzactii;
    }

    /** Caută tranzacțiile care implică numărul de cont indicat. */
    public ArrayList<Tranzactie> cautaTranzactiiPentruCont(String numarCont) {
        String numarValid = ContBancar.valideazaNumarCont(numarCont);

        if (!conturi.containsKey(numarValid)) {
            throw new IllegalArgumentException("Contul " + numarValid + " nu există.");
        }

        ArrayList<Tranzactie> rezultat = new ArrayList<>();
        for (int i = 0; i < tranzactii.size(); i++) {
            Tranzactie tranzactie = tranzactii.get(i);
            boolean contSursa = numarValid.equals(tranzactie.getContSursa());
            boolean contDestinatie = numarValid.equals(tranzactie.getContDestinatie());

            if (contSursa || contDestinatie) {
                rezultat.add(tranzactie);
            }
        }
        return rezultat;
    }

    /** Salvează conturile și tranzacțiile în fișiere. */
    public void salveaza() throws IOException {
        stocareDate.salveaza(conturi, tranzactii);
    }

    /** Calculează statistici cu un vector și o matrice. */
    public String genereazaStatistici() {
        TipTranzactie[] tipuri = TipTranzactie.values();
        int[] numarPeTip = new int[tipuri.length];
        double[][] sumePeLuna = new double[12][tipuri.length];

        for (int i = 0; i < tranzactii.size(); i++) {
            Tranzactie tranzactie = tranzactii.get(i);
            int pozitieTip = tranzactie.getTip().ordinal();
            int pozitieLuna = tranzactie.getDataOra().getMonthValue() - 1;
            numarPeTip[pozitieTip] = numarPeTip[pozitieTip] + 1;
            sumePeLuna[pozitieLuna][pozitieTip] =
                    sumePeLuna[pozitieLuna][pozitieTip] + tranzactie.getSuma();
        }

        StringBuilder raport = new StringBuilder();
        raport.append("Statistici TERBank\n");
        raport.append("Număr conturi: ").append(conturi.size()).append('\n');
        raport.append("Număr tranzacții: ").append(tranzactii.size()).append('\n');

        for (int i = 0; i < tipuri.length; i++) {
            raport.append(tipuri[i]).append(": ");
            raport.append(numarPeTip[i]).append(" tranzacții\n");
        }

        raport.append("Sume pe lună și tip:\n");
        for (int luna = 0; luna < sumePeLuna.length; luna++) {
            int numarLuna = luna + 1;
            if (numarLuna < 10) {
                raport.append("0");
            }
            raport.append(numarLuna);

            for (int tip = 0; tip < tipuri.length; tip++) {
                String sumaText = String.format(Locale.US, "%.2f", sumePeLuna[luna][tip]);
                raport.append(" | ").append(tipuri[tip]).append(": ").append(sumaText);
            }
            raport.append('\n');
        }

        return raport.toString();
    }

    /** Generează și salvează raportul text. */
    public void genereazaRaport() throws IOException {
        String raport = genereazaStatistici();
        stocareDate.salveazaRaport(raport);
    }

    private void incarcaDate() throws IOException {
        ArrayList<String> liniiConturi = stocareDate.citesteConturi();

        for (int i = 0; i < liniiConturi.size(); i++) {
            String linie = liniiConturi.get(i);
            int numarLinie = i + 1;

            if (linie.trim().length() == 0) {
                continue;
            }

            try {
                String[] campuri = linie.split("\\|", -1);
                if (campuri.length != 3) {
                    throw new IllegalArgumentException("format invalid");
                }

                String numarCont = campuri[0];
                String titular = campuri[1];
                double sold = Double.parseDouble(campuri[2]);
                ContBancar cont = new ContBancar(numarCont, titular, sold);

                if (conturi.containsKey(cont.getNumarCont())) {
                    throw new IllegalArgumentException("numărul contului este duplicat");
                }

                conturi.put(cont.getNumarCont(), cont);
            } catch (Exception exceptie) {
                System.out.println("Cont ignorat, linia " + numarLinie + ": " + exceptie.getMessage());
            }
        }

        ArrayList<String> liniiTranzactii = stocareDate.citesteTranzactii();

        for (int i = 0; i < liniiTranzactii.size(); i++) {
            String linie = liniiTranzactii.get(i);
            int numarLinie = i + 1;

            if (linie.trim().length() == 0) {
                continue;
            }

            try {
                String[] campuri = linie.split("\\|", -1);
                if (campuri.length != 6) {
                    throw new IllegalArgumentException("format invalid");
                }

                String id = campuri[0];
                TipTranzactie tip = TipTranzactie.valueOf(campuri[1]);
                double suma = Double.parseDouble(campuri[2]);
                LocalDateTime dataOra = LocalDateTime.parse(campuri[3]);
                String sursa = null;
                String destinatie = null;

                if (campuri[4].length() > 0) {
                    sursa = campuri[4];
                }
                if (campuri[5].length() > 0) {
                    destinatie = campuri[5];
                }

                Tranzactie tranzactie = new Tranzactie(id, tip, suma, dataOra, sursa, destinatie);

                if (contineId(tranzactie.getIdTranzactie())) {
                    throw new IllegalArgumentException("ID-ul tranzacției este duplicat");
                }

                boolean conturiValide = true;
                if (sursa != null && !conturi.containsKey(sursa)) {
                    conturiValide = false;
                }
                if (destinatie != null && !conturi.containsKey(destinatie)) {
                    conturiValide = false;
                }
                if (!conturiValide) {
                    throw new IllegalArgumentException("tranzacția folosește un cont inexistent");
                }

                tranzactii.add(tranzactie);
            } catch (Exception exceptie) {
                System.out.println("Tranzacție ignorată, linia " + numarLinie + ": " + exceptie.getMessage());
            }
        }
    }

    private void adaugaTranzactie(TipTranzactie tip, double suma, String sursa, String destinatie) {
        String id = creeazaIdTranzactie();
        Tranzactie tranzactie = new Tranzactie(id, tip, suma, LocalDateTime.now(), sursa, destinatie);
        tranzactii.add(tranzactie);
    }

    private String creeazaIdTranzactie() {
        String id = "T" + urmatorulNumarTranzactie;

        while (contineId(id)) {
            urmatorulNumarTranzactie++;
            id = "T" + urmatorulNumarTranzactie;
        }

        urmatorulNumarTranzactie++;
        return id;
    }

    private boolean contineId(String id) {
        for (int i = 0; i < tranzactii.size(); i++) {
            if (tranzactii.get(i).getIdTranzactie().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private static void valideazaSuma(double suma) {
        if (Double.isNaN(suma) || Double.isInfinite(suma) || suma <= 0) {
            throw new IllegalArgumentException("Suma trebuie să fie pozitivă și finită.");
        }
    }

    private static void valideazaSoldNou(double sold) {
        if (Double.isNaN(sold) || Double.isInfinite(sold) || sold < 0) {
            throw new IllegalArgumentException("Operațiunea depășește limita numerică permisă.");
        }
    }
}
