package br.edu.ifsc.fln.exception;

import br.edu.ifsc.fln.model.domain.Servico;

public class garantirServicoUnico extends RuntimeException {
    public garantirServicoUnico(Servico servico) {
        super("Não podem constar serviços repetidos na OS, no caso: "+servico.getDescricao());
    }
}
