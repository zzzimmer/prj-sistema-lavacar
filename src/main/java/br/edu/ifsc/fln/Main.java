/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package br.edu.ifsc.fln;

import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;

/**
 *
 * @author mpisc
 */
public class Main {

    public static void main(String[] args) {
        JavaFXApp.main(args);

        Cliente cliente = new Cliente() {

        };

        if (cliente instanceof PessoaJuridica){
            System.out.println("Fisica sem definir.");
        } else if (cliente instanceof PessoaFisica) {
            System.out.println("");

        }
    }




    
}
