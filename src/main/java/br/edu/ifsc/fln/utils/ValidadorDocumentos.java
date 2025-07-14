//package br.edu.ifsc.fln.utils; // ou o pacote de utilidades do seu projeto
//
//import java.util.InputMismatchException;
//
//public class ValidadorDocumentos {
//
//    public static boolean validarCPF(String cpf) {
//        // Remove caracteres não numéricos
//        cpf = cpf.replaceAll("[^0-9]", "");
//
//        // CPFs com todos os números iguais são considerados inválidos
//        if (cpf.matches("(\\d)\\1{10}")) {
//            return false;
//        }
//
//        // Verifica se o CPF tem 11 dígitos
//        if (cpf.length() != 11) {
//            return false;
//        }
//
//        char dig10, dig11;
//        int sm, i, r, num, peso;
//
//        try {
//            // Cálculo do 1º Dígito Verificador
//            sm = 0;
//            peso = 10;
//            for (i = 0; i < 9; i++) {
//                num = (int) (cpf.charAt(i) - 48);
//                sm = sm + (num * peso);
//                peso = peso - 1;
//            }
//
//            r = 11 - (sm % 11);
//            if ((r == 10) || (r == 11))
//                dig10 = '0';
//            else
//                dig10 = (char) (r + 48);
//
//            // Cálculo do 2º Dígito Verificador
//            sm = 0;
//            peso = 11;
//            for (i = 0; i < 10; i++) {
//                num = (int) (cpf.charAt(i) - 48);
//                sm = sm + (num * peso);
//                peso = peso - 1;
//            }
//
//            r = 11 - (sm % 11);
//            if ((r == 10) || (r == 11))
//                dig11 = '0';
//            else
//                dig11 = (char) (r + 48);
//
//            // Verifica se os dígitos calculados conferem com os dígitos informados
//            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
//        } catch (InputMismatchException erro) {
//            return false;
//        }
//    }
//
//    // Método estático para validar CNPJ
//    public static boolean validarCNPJ(String cnpj) {
////        // Remove caracteres não numéricos
////        cnpj = cnpj.replaceAll("[^0-9]", "");
////
////        // CNPJs com todos os números iguais são considerados inválidos
////        if (cnpj.matches("(\\d)\\1{13}")) {
////            return false;
////        }
////
////        // Verifica se o CNPJ tem 14 dígitos
////        if (cnpj.length() != 14) {
////            return false;
////        }
////
////        char dig13, dig14;
////        int sm, i, r, num, peso;
////
////        try {
////            // Cálculo do 1º Dígito Verificador
////            sm = 0;
////            peso = 2;
////            for (i = 11; i >= 0; i--) {
////                num = (int) (cnpj.charAt(i) - 48);
////                sm = sm + (num * peso);
////                peso = peso + 1;
////                if (peso == 10)
////                    peso = 2;
////            }
////
////            r = sm % 11;
////            if ((r == 0) || (r == 1))
////                dig13 = '0';
////            else
////                dig13 = (char) ((11 - r) + 48);
////
////            // Cálculo do 2º Dígito Verificador
////            sm = 0;
////            peso = 2;
////            for (i = 12; i >= 0; i--) {
////                num = (int) (cnpj.charAt(i) - 48);
////                sm = sm + (num * peso);
////                peso = peso + 1;
////                if (peso == 10)
////                    peso = 2;
////            }
////
////            r = sm % 11;
////            if ((r == 0) || (r == 1))
////                dig14 = '0';
////            else
////                dig14 = (char) ((11 - r) + 48);
////
////            // Verifica se os dígitos calculados conferem com os dígitos informados
////            return (dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13));
////        } catch (InputMismatchException erro) {
////            return false;
////        }
//        return true;
//    }
//}