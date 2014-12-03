
package com.senac.dao;

import com.senac.exceptions.LoginException;
import com.senac.model.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UsuarioDao implements ConectarInterface {

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

    public void validarLogin(Usuario u) throws SQLException {
        iniciarConexao();
        String sql = "select * from tb_usuario where user_login = '" + u.getUserLogin() + "' and senha = '" + u.getSenha() + "'";
        ResultSet resultSet = statement.executeQuery(sql);
        if (!resultSet.next()) {
            throw new LoginException();
        }
        encerrarConexao();
    }

    public boolean tipoUser(Usuario u) throws SQLException {
        iniciarConexao();
        boolean k = false;
        String sql = "select * from tb_usuario where user_login = '" + u.getUserLogin() + "' and senha = '" + u.getSenha() + "'";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            String acessso = (String) resultSet.getObject("tipo_acesso");
            if(acessso.equals("1")){
                k = true;
            } else if (acessso.equals("0")){
                k = false;
            }
        }
        encerrarConexao();
        return k;
    }
}
