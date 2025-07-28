package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import com.sun.security.jgss.GSSUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir (Cliente cliente) throws DAOException {
        final String sql = "INSERT INTO cliente (nome, email, celular, data_cadastro )VALUES (?,?,?,?)";
        final String sqlPFisica = "INSERT INTO pessoa_fisica (id_cliente, cpf, data_nascimento) VALUES ((SELECT MAX(id) FROM cliente),?,?)";
        final String sqlPJuridica = "INSERT INTO pessoa_juridica (id_cliente, cnpj, inscricao_estadual) VALUES ((SELECT MAX(id) FROM cliente),?,?)";
        try {
            //armazena os dados da superclasse
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCelular());
            stmt.setDate(4, java.sql.Date.valueOf(cliente.getDataCadastro()));
            stmt.execute();
            //armazena os dados da subclasse
            if (cliente instanceof PessoaFisica) {
                stmt = connection.prepareStatement(sqlPFisica);
                stmt.setString(1, ((PessoaFisica)cliente).getCpf());
                stmt.setDate(2, java.sql.Date.valueOf(((PessoaFisica)cliente).getDataNascimento()));
                stmt.execute();
            } else {
                stmt = connection.prepareStatement(sqlPJuridica);
                stmt.setString(1, ((PessoaJuridica)cliente).getCnpj());
                stmt.setString(2, ((PessoaJuridica) cliente).getInscricaoEstadual());
                stmt.execute();
            }
        } catch (java.sql.SQLIntegrityConstraintViolationException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Impossível registrar o cliente no banco de dados!", ex);
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Impossível registrar o cliente banco de dados!", ex);
        }
    }

    public boolean remover (Cliente cliente){
        String sql = "DELETE FROM cliente WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

//    public boolean alterar(Cliente cliente) {
//        String sql = "UPDATE cliente SET nome=?, email=?, celular=? WHERE id=?";
//        String sqlPF = "UPDATE pessoa_fisica SET cpf=? WHERE id_cliente = ?";
//        String sqlPJ = "UPDATE pessoa_juridica SET cnpj=? WHERE id_cliente = ?";
//        try {
//            PreparedStatement stmt = connection.prepareStatement(sql);
//            stmt.setString(1, cliente.getNome());
//            stmt.setString(2, cliente.getEmail());
//            stmt.setString(3, cliente.getCelular());
//            stmt.setInt(4, cliente.getId());
//            stmt.execute();
//            if (cliente instanceof PessoaFisica) {
//                stmt = connection.prepareStatement(sqlPF);
//                stmt.setString(1, ((PessoaFisica)cliente).getCpf());
//                stmt.setInt(2, cliente.getId());
//                stmt.execute();
//            } else {
//                stmt = connection.prepareStatement(sqlPJ);
//                stmt.setString(1, ((PessoaJuridica)cliente).getCnpj());
//                stmt.setInt(2, cliente.getId());
//                stmt.execute();
//            }
//            return true;
//        } catch (SQLException ex) {
//            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
//            return false;
//        }
//    }

    public boolean alterar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome=?, email=?, celular=? WHERE id=?";
        String sqlPF = "UPDATE pessoa_fisica SET cpf=?, data_nascimento=? WHERE id_cliente = ?";
        String sqlPJ = "UPDATE pessoa_juridica SET cnpj=?, inscricao_estadual=? WHERE id_cliente = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCelular());
            stmt.setInt(4, cliente.getId());
            stmt.execute();

            if (cliente instanceof PessoaFisica) {
                stmt = connection.prepareStatement(sqlPF);
                stmt.setString(1, ((PessoaFisica) cliente).getCpf());
                stmt.setDate(2, java.sql.Date.valueOf(((PessoaFisica) cliente).getDataNascimento()));
                stmt.setInt(3, cliente.getId());
                stmt.execute();
            } else {
                stmt = connection.prepareStatement(sqlPJ);
                stmt.setString(1, ((PessoaJuridica) cliente).getCnpj());
                stmt.setString(2, ((PessoaJuridica) cliente).getInscricaoEstadual());
                stmt.setInt(3, cliente.getId());
                stmt.execute();
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Cliente> listar() {
        String sql = "SELECT * FROM cliente c "
                + "LEFT JOIN pessoa_fisica  pf on pf.id_cliente = c.id "
                + "LEFT JOIN pessoa_juridica pj on pj.id_cliente = c.id;";
        List<Cliente> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cliente cliente = populateVO(resultado);
                retorno.add(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    private Cliente populateVO(ResultSet rs) throws SQLException {
        Cliente cliente;
        if (rs.getString("cpf") != null && !rs.getString("cpf").isEmpty()) {
            cliente = new PessoaFisica();
            ((PessoaFisica)cliente).setCpf(rs.getString("cpf"));
            ((PessoaFisica) cliente).setDataNascimento(LocalDate.parse(rs.getString("data_nascimento")));
        } else {
            cliente = new PessoaJuridica();
            ((PessoaJuridica)cliente).setCnpj(rs.getString("cnpj"));
            ((PessoaJuridica) cliente).setInscricaoEstadual(rs.getString("inscricao_estadual"));
        }
        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setEmail(rs.getString("email"));
        cliente.setCelular(rs.getString("celular"));
        return cliente;
    }

    public Cliente buscar (long id){
        String sql = "SELECT * FROM cliente c "
                + "LEFT JOIN pessoa_fisica  pf on pf.id_cliente = c.id "
                + "LEFT JOIN pessoa_juridica pj on pj.id_cliente = c.id WHERE id=?;";
        Cliente retorno = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1,id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()){
                retorno = populateVO(resultado);
            }
        }catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public boolean pontuar(Cliente cliente){
        String sqlPF = "UPDATE pessoa_fisica SET pontuacao=? WHERE id_cliente = ?";
        String sqlPJ = "UPDATE pessoa_juridica SET pontuacao=? WHERE id_cliente = ?";

        try {
            if (cliente instanceof PessoaFisica) {
                PreparedStatement stmt = connection.prepareStatement(sqlPF);
                stmt = connection.prepareStatement(sqlPF);
                stmt.setInt(1, ((PessoaFisica) cliente).getPontuacao().saldo());
                stmt.setInt(2, cliente.getId());
                stmt.execute();
            } else {
                PreparedStatement stmt = connection.prepareStatement(sqlPJ);
                stmt = connection.prepareStatement(sqlPJ);
                stmt.setInt(1, ((PessoaJuridica) cliente).getPontuacao().saldo());
                stmt.setInt(2, cliente.getId());
                stmt.execute();
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}


