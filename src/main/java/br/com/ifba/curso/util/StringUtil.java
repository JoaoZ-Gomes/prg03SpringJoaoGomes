package br.com.ifba.curso.util;

// Classe utilitária para validações de String
public class StringUtil {

    // Verifica se é nulo ou vazio
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Verifica se é numérico
    public static boolean isNumeric(String s) {
        if (isNullOrEmpty(s)) return false;
        return s.matches("\\d+");
    }

}
