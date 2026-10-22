package ro.terbank;

import java.util.Locale;

/** Reprezintă un cont bancar cu titular și sold. */
public class ContBancar {

    private final String numarCont;
    private String titular;
    private double sold;

    /** Creează un cont cu soldul inițial zero. */
    public ContBancar(String numarCont, String titular) {
        this(numarCont, titular, 0);
    }

    /** Creează un cont cu soldul inițial indicat. */
    public ContBancar(String numarCont, String titular, double sold) {
        this.numarCont = valideazaNumarCont(numarCont);
        this.titular = valideazaText(titular, "Titularul");
        valideazaSold(sold);
        this.sold = sold;
    }

    /** Returnează numărul contului. */
    public String getNumarCont() {
        return numarCont;
    }

    /** Returnează numele titularului. */
    public String getTitular() {
        return titular;
    }

    /** Returnează soldul disponibil. */
    public double getSold() {
        return sold;
    }

    /** Schimbă numele titularului. */
    public void setTitular(String titular) {
        this.titular = valideazaText(titular, "Titularul");
    }

    /** Returnează informațiile contului. */
    @Override
    public String toString() {
        String soldText = String.format(Locale.US, "%.2f", sold);
        return "Cont " + numarCont + " | Titular: " + titular + " | Sold: " + soldText;
    }

    void actualizeazaSold(double soldNou) {
        valideazaSold(soldNou);
        sold = soldNou;
    }

    static String valideazaText(String valoare, String denumire) {
        if (valoare == null) {
            throw new IllegalArgumentException(denumire + " nu poate fi null.");
        }

        String text = valoare.trim();
        if (text.length() == 0) {
            throw new IllegalArgumentException(denumire + " nu poate fi gol.");
        }
        if (text.contains("|")) {
            throw new IllegalArgumentException(denumire + " nu poate conține separatorul |.");
        }
        return text;
    }

    static String valideazaNumarCont(String numarCont) {
        if (numarCont == null || numarCont.length() == 0) {
            throw new IllegalArgumentException("Numărul contului nu poate fi gol.");
        }

        for (int i = 0; i < numarCont.length(); i++) {
            if (numarCont.charAt(i) < '0' || numarCont.charAt(i) > '9') {
                throw new IllegalArgumentException(
                        "Numărul contului trebuie să conțină doar cifre de la 0 la 9.");
            }
        }

        return numarCont;
    }

    private static void valideazaSold(double sold) {
        if (Double.isNaN(sold) || Double.isInfinite(sold)) {
            throw new IllegalArgumentException("Soldul trebuie să fie finit.");
        }
        if (sold < 0) {
            throw new IllegalArgumentException("Soldul nu poate fi negativ.");
        }
    }
}
