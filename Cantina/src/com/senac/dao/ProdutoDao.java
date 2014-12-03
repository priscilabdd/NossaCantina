
package com.senac.dao;

import com.senac.model.Produto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ProdutoDao implements ConectarInterface {

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

    public void salvar(Produto p) throws SQLException {
        iniciarConexao();
        String sql = "insert into tb_produto(cod_produto, nome, tipo, preco)"
                + "values(" + p.getCodProduto() + ",'" + p.getNome() + "','" + p.getTipo() + "'," + p.getPreco() + ")";
        statement.execute(sql);
        encerrarConexao();
    }

    public List<Produto> listarProdutos(String pesquisa) throws SQLException {
        List list = new ArrayList();
        iniciarConexao();
        String sql = "select * from tb_produto where cod_produto like '%" + pesquisa + "%' or nome like '%" + pesquisa + "%'";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Produto p = new Produto();
            p.setCodProduto((Integer) resultSet.getObject("cod_produto"));
            p.setNome((String) resultSet.getObject("nome"));
            p.setPreco((double) resultSet.getObject("preco"));
            p.setTipo((String) resultSet.getObject("tipo"));
            list.add(p);
        }
        encerrarConexao();
        return list;
    }
    
    public void atualizar(Produto p) throws SQLException{
        iniciarConexao();
        String sql = "update tb_produto set nome='"+p.getNome()+"', tipo='"+p.getTipo()+"', preco="+p.getPreco()+" where cod_produto="+p.getCodProduto();
        statement.execute(sql);
        encerrarConexao();
    }
}
