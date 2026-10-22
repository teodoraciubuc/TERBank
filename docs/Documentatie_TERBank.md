# Documentație TERBank

TERBank este o aplicație de consolă pentru simularea gestiunii unor conturi și tranzacții bancare. Este realizată în Java 21 pentru disciplina PPOO. Valorile sunt păstrate ca `double`; această alegere este simplă pentru proiect și nu este destinată unei bănci reale.

## Funcționalități

- Crearea, afișarea și căutarea conturilor.
- Depuneri, retrageri și transferuri între conturi.
- Afișarea istoricului complet sau a tranzacțiilor unui cont.
- Statistici și salvarea unui raport text.
- Citirea datelor la pornire și salvarea lor la ieșire.

## Clase

| Fișier | Rol |
|---|---|
| `Main.java` | Pornește aplicația și gestionează meniul cu `Scanner`. |
| `ContBancar.java` | Păstrează numărul contului, titularul și soldul. |
| `Tranzactie.java` | Păstrează datele unei tranzacții. |
| `TipTranzactie.java` | Definește tipurile DEPUNERE, RETRAGERE și TRANSFER. |
| `GestiuneBancara.java` | Gestionează conturile, operațiunile, istoricul și statisticile. |
| `StocareDate.java` | Citește și salvează fișierele text. |
| `FonduriInsuficienteException.java` | Semnalează o retragere sau un transfer fără sold suficient. |

## Colecții și statistici

`GestiuneBancara` folosește un `HashMap<String, ContBancar>` pentru conturi și un `ArrayList<Tranzactie>` pentru istoricul tranzacțiilor. Statistica folosește un `int[]` pentru numărul tranzacțiilor pe tip și un `double[][]` pentru sumele pe lună și tip.

## Fișiere text

Datele sunt salvate relativ la directorul din care se rulează aplicația:

- `data/conturi.txt`
- `data/tranzactii.txt`
- `rapoarte/raport.txt`

Fiecare linie din fișierele de date conține câmpuri separate prin `|`.

Format cont:

```text
numarCont|titular|sold
C001|Ana Popescu|1500.0
```

Format tranzacție:

```text
id|tip|suma|dataOra|contSursa|contDestinatie
e4a1|DEPUNERE|100.0|2026-10-21T12:30:00||C001
```

La depunere, contul sursă este gol. La retragere, contul destinație este gol. Transferul folosește ambele conturi. Caracterul `|` nu este permis în numerele de cont, numele titularilor și ID-uri. La încărcare, aplicația verifică numărul de câmpuri, valorile numerice, tipul, data, conturile și unicitatea identificatorilor; liniile invalide sunt ignorate și raportate în consolă.

## Meniu

Meniul principal este în `Main` și conține opțiuni numerotate pentru crearea și căutarea conturilor, operațiuni, istoricul tranzacțiilor, statistici, raport și salvare la ieșire.
