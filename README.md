# TERBank

TERBank este o aplicație Java de consolă pentru gestiunea simplificată a conturilor și tranzacțiilor, realizată pentru disciplina PPOO. Proiectul folosește Java 21 și biblioteci standard, fără Maven, Gradle sau bază de date. Sumele sunt de tip `double`, potrivite pentru această simulare educațională.

## Compilare și rulare

Din PowerShell, în directorul principal al proiectului:

```powershell
New-Item -ItemType Directory -Force -Path out | Out-Null
$javaFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out $javaFiles
java '-Dfile.encoding=UTF-8' -cp out ro.terbank.Main
```

În IntelliJ IDEA, deschideți proiectul, configurați JDK 21, marcați `src` ca **Sources Root** și rulați `ro.terbank.Main`.

La pornire, aplicația citește `data/conturi.txt` și `data/tranzactii.txt`. La ieșire, salvează datele în aceste fișiere. Opțiunea de raport creează `rapoarte/raport.txt`. Toate căile sunt relative la directorul proiectului.

Datele sunt text simplu, separate prin `|`. Un cont folosește formatul `C001|Ana Popescu|1500.0`. O tranzacție conține ID-ul, tipul, suma, data și ora, contul sursă și contul destinație; câmpurile de cont nefolosite rămân goale. Caracterul `|` nu este permis în câmpuri.

Aplicația folosește `HashMap<String, ContBancar>` pentru conturi, `ArrayList<Tranzactie>` pentru istoric, un vector și o matrice primitive pentru statistici. Clasele și responsabilitățile lor sunt descrise în [documentație](docs/Documentatie_TERBank.md).
