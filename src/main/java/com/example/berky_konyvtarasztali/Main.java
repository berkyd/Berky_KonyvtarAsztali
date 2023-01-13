package com.example.berky_konyvtarasztali;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        //Bár nem magamtól csináltam, mert nem tudnám, legalább foglalkoztam vele addig is

        if (Arrays.asList(args).contains("--stat")){
            Statisztika.main(args);
        } else {
            KonyvtarApp.main(args);
        }
    }
}
