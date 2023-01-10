package com.example.berky_konyvtarasztali;

import java.util.*;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class Statisztika {
    static List<Konyv> konyvLista;

    public static void main(String[] args) {
        try {
            Beolvas();
            if (konyvLista.isEmpty()) {
                throw new Exception("Az adatbázis nem tartalmaz könyvet");
            }
            megszamol500nalHosszabbStream();
            vanE1950nelRegebbiStream();
            leghosszabbKonyvStream();
            legtobbKonyvvelSzerzoStream();
        } catch (SQLException e) {
            System.out.println("Hiba a kapcsolat létrehozásakor!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void megszamol500nalHosszabbStream() {
        long db = konyvLista.stream().filter(konyv -> konyv.getPage_count() > 500).count();
        System.out.println("500 oldalnál hosszabb könyvek száma: " + db);
    }

    private static void megszamol500nalHosszabb() {
        int db = 0;
        for (Konyv konyv : konyvLista) {
            if (konyv.getPage_count() > 500) {
                db++;
            }
        }
        System.out.println("500 oldalnál hosszabb könyvek száma: " + db);
    }

    private static void vanE1950nelRegebbiStream() {
        boolean vane = konyvLista.stream().anyMatch(konyv -> konyv.getPublish_year() < 1950);
        System.out.printf("%s 1950-nél régebbi könyv\n", vane ? "Van" : "Nincs");
    }

    private static void vanE1950nelRegebbi() {
        int i = 0;
        while (i < konyvLista.size() && konyvLista.get(i).getPublish_year() >= 1950) {
            i++;
        }
        boolean vane = i < konyvLista.size();
        System.out.printf("%s 1950-nél régebbi könyv\n", vane ? "Van" : "Nincs");
    }

    private static void leghosszabbKonyvStream() {
        Konyv lh = konyvLista.stream().max(Comparator.comparingInt(Konyv::getPage_count)).get();
        System.out.printf("A leghosszabb könyv:\n" +
                        "\tSzerző: %s\n" +
                        "\tCím: %s\n" +
                        "\tKiadás éve: %d\n" +
                        "\tOldalszám: %d\n", lh.getAuthor(), lh.getTitle(),
                lh.getPublish_year(), lh.getPage_count());
    }

    private static void leghosszabbKonyv() {
        Konyv lh = konyvLista.get(0);
        for (int i = 1; i < konyvLista.size(); i++) {
            if (konyvLista.get(i).getPage_count() > lh.getPage_count()) {
                lh = konyvLista.get(i);
            }
        }
        System.out.printf("A leghosszabb könyv:\n" +
                        "\tSzerző: %s\n" +
                        "\tCím: %s\n" +
                        "\tKiadás éve: %d\n" +
                        "\tOldalszám: %d\n", lh.getAuthor(), lh.getTitle(),
                lh.getPublish_year(), lh.getPage_count());
    }

    private static void legtobbKonyvvelSzerzoStream() {
        String legtobbkonyv = konyvLista.stream()
                .collect(Collectors.groupingBy(Konyv::getAuthor, Collectors.counting())).entrySet()
                .stream().max(Comparator.comparingLong(Map.Entry::getValue)).get().getKey();
        System.out.println("A legtöbb könyvvel rendelkező szerző: " + legtobbkonyv);
    }

    private static void legtobbKonyvvelSzerzo() {
        Map<String, Long> szerzok = new HashMap<>();
        for (Konyv konyv : konyvLista) {
            szerzok.putIfAbsent(konyv.getAuthor(), 0L);
            long ujErtek = szerzok.get(konyv.getAuthor()) + 1;
            szerzok.put(konyv.getAuthor(), ujErtek);
        }
        Map.Entry<String, Long> legtobbkonyv = null;
        for (Map.Entry<String, Long> szerzo: szerzok.entrySet()) {
            if (legtobbkonyv == null || legtobbkonyv.getValue() < szerzo.getValue()) {
                legtobbkonyv = szerzo;
            }
        }
        System.out.println("A legtöbb könyvvel rendelkező szerző: " + legtobbkonyv.getKey());
    }

    private static void Beolvas() throws SQLException {
        KonyvtarDB db = new KonyvtarDB();
        konyvLista = db.getKonyvek();
    }
}
