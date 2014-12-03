
package com.senac.dao;

import com.senac.model.Funcionario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class FuncionarioDao implements ConectarInterface {

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

    public void salvar(Funcionario f) throws SQLException {
        iniciarConexao();
        String sql = "insert into tb_funcionario(cpf, nome) values(" + f.getCpf() + ", '" + f.getNome() + "')";
        statement.execute(sql);
        sql = "insert into tb_usuario(user_login, senha, tipo_acesso, ativo) values (" + f.getCpf() + ",'" + f.getSenha() + "',1 ,1 )";
        statement.execute(sql);
        encerrarConexao();
    }

    public List<Funcionario> listarFuncionarios(String pesquisa) throws SQLException {
        List list = new ArrayList();
        iniciarConexao();
        String sql;
        sql = "select * from tb_funcionario where cpf like '%" + pesquisa + "%' or nome like '%" + pesquisa + "%'";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Funcionario f = new Funcionario();
            f.setCodFuncionario((int)resultSet.getObject("cod_funcionario"));
            f.setNome((String) resultSet.getObject("nome"));
            f.setCpf((String) resultSet.getObject("cpf"));
            list.add(f);
        }
        encerrarConexao();
        return list;
    }

    public void atualizar(Funcionario f) throws SQLException{
        iniciarConexao();
        String sql = "update tb_funcionario set nome='" + f.getNome() + "' where cod_funcionario=" + f.getCodFuncionario();
        statement.execute(sql);
        encerrarConexao();
    }
}
