# TERBank

TERBank este o aplicație Java de consolă pentru gestionarea tranzacțiilor e-banking, dezvoltată pentru disciplina Programare și Proiectare Orientată Obiect (PPOO).
Proiectul folosește Java simplu, fără Maven sau Gradle.

## Structura proiectului

```text
src/
└── ro/
    └── terbank/
        └── Main.java
```

## Rulare din linia de comandă

Este necesar un JDK instalat. Din directorul principal al proiectului, executați:

```sh
javac -d out src/ro/terbank/Main.java
java -cp out ro.terbank.Main
```

La pornire, aplicația afișează:

```text
Bun venit la TERBank!
```

## Rulare în IntelliJ IDEA

Deschideți directorul proiectului în IntelliJ IDEA, configurați un JDK și marcați directorul `src` ca **Sources Root** dacă nu este recunoscut automat. Apoi rulați metoda `main` din clasa `ro.terbank.Main`.
