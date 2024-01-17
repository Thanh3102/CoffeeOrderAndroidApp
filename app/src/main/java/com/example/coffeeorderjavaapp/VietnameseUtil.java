package com.example.coffeeorderjavaapp;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class VietnameseUtil {
    public static String removeAccent(String text) {
        String temp = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}