package ro.terbank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Pornește aplicația TERBank și afișează meniul principal. */
public class Main {

    /** Încarcă datele și rulează aplicația în consolă. */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            GestiuneBancara gestiune = new GestiuneBancara();
            boolean ruleaza = true;
            boolean dateSalvate = false;

            while (ruleaza) {
                afiseazaMeniu();
                System.out.print("Alegeți opțiunea: ");

                if (scanner.hasNextLine()) {
                    try {
                        String optiune = scanner.nextLine().trim();

                        switch (optiune) {
                            case "1":
                                creeazaCont(scanner, gestiune);
                                break;
                            case "2":
                                afiseazaConturi(gestiune);
                                break;
                            case "3":
                                cautaCont(scanner, gestiune);
                                break;
                            case "4":
                                depune(scanner, gestiune);
                                break;
                            case "5":
                                retrage(scanner, gestiune);
                                break;
                            case "6":
                                transfera(scanner, gestiune);
                                break;
                            case "7":
                                afiseazaTranzactii(gestiune.getTranzactii());
                                break;
                            case "8":
                                cautaTranzactii(scanner, gestiune);
                                break;
                            case "9":
                                System.out.println(gestiune.genereazaStatistici());
                                break;
                            case "10":
                                gestiune.genereazaRaport();
                                System.out.println("Raportul a fost salvat în rapoarte/raport.txt.");
                                break;
                            case "0":
                                gestiune.salveaza();
                                System.out.println("Datele au fost salvate. La revedere!");
                                dateSalvate = true;
                                ruleaza = false;
                                break;
                            default:
                                System.out.println("Opțiune invalidă.");
                        }
                    } catch (FonduriInsuficienteException exceptie) {
                        System.out.println("Eroare: " + exceptie.getMessage());
                    } catch (IllegalArgumentException exceptie) {
                        System.out.println("Eroare: " + exceptie.getMessage());
                    } catch (IOException exceptie) {
                        System.out.println("Eroare la accesarea fișierelor: " + exceptie.getMessage());
                    } catch (NoSuchElementException exceptie) {
                        ruleaza = false;
                    }
                } else {
                    ruleaza = false;
                }
            }

            if (!dateSalvate) {
                try {
                    gestiune.salveaza();
                    System.out.println("Datele au fost salvate. La revedere!");
                } catch (IOException exceptie) {
                    System.out.println("Datele nu au putut fi salvate: " + exceptie.getMessage());
                }
            }
        } catch (IOException exceptie) {
            System.out.println("Aplicația nu a putut încărca datele: " + exceptie.getMessage());
        }
    }

    private static void afiseazaMeniu() {
        System.out.println("\n========== TERBank ==========");
        System.out.println("1. Creează cont");
        System.out.println("2. Afișează conturile");
        System.out.println("3. Caută cont");
        System.out.println("4. Depunere");
        System.out.println("5. Retragere");
        System.out.println("6. Transfer");
        System.out.println("7. Istoric tranzacții");
        System.out.println("8. Caută tranzacțiile unui cont");
        System.out.println("9. Statistici");
        System.out.println("10. Generează raport text");
        System.out.println("0. Salvează și ieși");
    }

    private static void creeazaCont(Scanner scanner, GestiuneBancara gestiune) {
        System.out.print("Numărul contului: ");
        String numarCont = scanner.nextLine();
        System.out.print("Numele titularului: ");
        String titular = scanner.nextLine();
        gestiune.creeazaCont(numarCont, titular);
        System.out.println("Contul a fost creat.");
    }

    private static void afiseazaConturi(GestiuneBancara gestiune) {
        ArrayList<ContBancar> conturi = gestiune.getConturi();

        if (conturi.isEmpty()) {
            System.out.println("Nu există conturi.");
        } else {
            for (int i = 0; i < conturi.size(); i++) {
                System.out.println(conturi.get(i));
            }
        }
    }

    private static void cautaCont(Scanner scanner, GestiuneBancara gestiune) {
        System.out.print("Numărul contului: ");
        String numarCont = scanner.nextLine();
        System.out.println(gestiune.cautaCont(numarCont));
    }

    private static void depune(Scanner scanner, GestiuneBancara gestiune) {
        System.out.print("Numărul contului: ");
        String numarCont = scanner.nextLine();
        double suma = citesteSuma(scanner);
        gestiune.depune(numarCont, suma);
        System.out.println("Depunerea a fost înregistrată.");
    }

    private static void retrage(Scanner scanner, GestiuneBancara gestiune)
            throws FonduriInsuficienteException {
        System.out.print("Numărul contului: ");
        String numarCont = scanner.nextLine();
        double suma = citesteSuma(scanner);
        gestiune.retrage(numarCont, suma);
        System.out.println("Retragerea a fost înregistrată.");
    }

    private static void transfera(Scanner scanner, GestiuneBancara gestiune)
            throws FonduriInsuficienteException {
        System.out.print("Contul sursă: ");
        String numarSursa = scanner.nextLine();
        System.out.print("Contul destinație: ");
        String numarDestinatie = scanner.nextLine();
        double suma = citesteSuma(scanner);
        gestiune.transfera(numarSursa, numarDestinatie, suma);
        System.out.println("Transferul a fost înregistrat.");
    }

    private static double citesteSuma(Scanner scanner) {
        System.out.print("Suma: ");

        try {
            String text = scanner.nextLine().trim().replace(',', '.');
            return Double.parseDouble(text);
        } catch (NumberFormatException exceptie) {
            throw new IllegalArgumentException("Introduceți o sumă numerică validă.");
        }
    }

    private static void afiseazaTranzactii(ArrayList<Tranzactie> tranzactii) {
        if (tranzactii.isEmpty()) {
            System.out.println("Nu există tranzacții.");
        } else {
            for (int i = 0; i < tranzactii.size(); i++) {
                System.out.println(tranzactii.get(i));
            }
        }
    }

    private static void cautaTranzactii(Scanner scanner, GestiuneBancara gestiune) {
        System.out.print("Numărul contului: ");
        String numarCont = scanner.nextLine();
        ArrayList<Tranzactie> tranzactii = gestiune.cautaTranzactiiPentruCont(numarCont);
        afiseazaTranzactii(tranzactii);
    }
}
