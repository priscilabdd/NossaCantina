
package com.senac.dao;

import com.senac.exceptions.MatriculaInvalidaException;
import com.senac.exceptions.SaldoException;
import com.senac.model.Cliente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ClienteDao implements ConectarInterface {

    Connection connection = null;
    Statement statement = null;

    public void iniciarConexao() {
        try {
            Class.forName(driverJDBC);
            connection = DriverManager.getConnection(host, usuario, senha);
            statement = connection.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Falha na conexao");
        }
    }

    public void encerrarConexao() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void salvar(Cliente c) throws SQLException {
        iniciarConexao();
        String sql = "insert into tb_cliente(matricula, nome, email, saldo)"
                + "values(" + c.getMatricula() + ", '" + c.getNome() + "', '" + c.getEmail() + "', " + c.getSaldo() + ")";
        statement.execute(sql);
        encerrarConexao();
    }

    public List<Cliente> listarClientes(String pesquisa) throws SQLException {
        List list = new ArrayList();
        iniciarConexao();
        String sql = "select * from tb_cliente where matricula like '%" + pesquisa + "%' or nome like '%" + pesquisa + "%'";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Cliente c = new Cliente();
            c.setMatricula((Integer) resultSet.getObject("matricula"));
            c.setNome((String) resultSet.getObject("nome"));
            c.setEmail((String) resultSet.getObject("email"));
            c.setSaldo((double) resultSet.getObject("saldo"));
            list.add(c);
        }
        encerrarConexao();
        return list;
    }

    public void atualizar(Cliente c) throws SQLException {
        iniciarConexao();
        String sql = "update tb_cliente set nome='" + c.getNome() + "', email='" + c.getEmail() + "', saldo=" + c.getSaldo() + " where matricula=" + c.getMatricula();
        statement.execute(sql);
        encerrarConexao();
    }

    public void usuarioCliente(Cliente c) throws SQLException {
        iniciarConexao();
        String sql = "insert into tb_usuario(user_login, senha, tipo_acesso, ativo) values('" + c.getUserLogin() + "', '" + c.getSenha() + "', 0, 1)";
        statement.execute(sql);
        encerrarConexao();
    }

    public void validarCliente(int matricula, double totalVenda) throws SQLException, MatriculaInvalidaException, SaldoException {
        iniciarConexao();
        if (!clienteExiste(matricula)) {
            throw new MatriculaInvalidaException();
        }
        encerrarConexao();
        double saldo = consultarSaldo(matricula);
        iniciarConexao();
        if (saldo < totalVenda) {
            throw new SaldoException();
        } else {
            double novoSaldo = saldo - totalVenda;
            String sql = "update tb_cliente set saldo = " + novoSaldo + " where matricula=" + matricula;
            statement.execute(sql);
        }
        encerrarConexao();
    }

    private boolean clienteExiste(int matricula) throws SQLException {
        String sql = "select '" + matricula + "' from tb_cliente";
        ResultSet r = statement.executeQuery(sql);
        return r.next();
    }

    public double consultarSaldo(int matricula) throws SQLException {
        iniciarConexao();
        String sql = "select saldo from tb_cliente where matricula ='" + matricula + "'";
        ResultSet rs = statement.executeQuery(sql);
        double saldo = 0;
        if (rs.next()) {
            saldo = (Double) rs.getObject("saldo");
        }
        encerrarConexao();
        return saldo;
    }

    public void transferirSaldo(int matriculaDeb, double valorDeb, int matriculaCred, double valorCred) throws SQLException {
        iniciarConexao();
        if (clienteExiste(matriculaCred)) {
            statement.execute("update tb_cliente set saldo = " + valorDeb + " where matricula = " + matriculaDeb);
            encerrarConexao();
            double saldoNovo = valorCred + consultarSaldo(matriculaCred);
            iniciarConexao();
            statement.execute("update tb_cliente set saldo = " + saldoNovo + " where matricula = " + matriculaCred);
        } else {
            throw new MatriculaInvalidaException();
        }
        encerrarConexao();
    }
}
