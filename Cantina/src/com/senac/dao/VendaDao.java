
package com.senac.dao;

import com.senac.model.ItemVenda;
import com.senac.model.Venda;
import com.senac.model.VendaReport;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
 

public class VendaDao implements ConectarInterface {

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

    public void salvar(Venda v, List<ItemVenda> iv, String usuario, int matricula, Double total) throws SQLException {
        iniciarConexao();
        int codUsuario = 0;
        int codVenda = 0;
        String sql = "select cod_usuario from tb_usuario where user_login= '" + usuario + "'";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            codUsuario = (Integer) rs.getObject("cod_usuario");
        }
        sql = "insert into tb_venda values('" + v.getDataVenda() + "'," + v.getTotalVenda() + ", " + matricula + ", " + codUsuario + " )";
        statement.execute(sql);
        rs = statement.executeQuery("select top 1 cod_venda from tb_venda order by cod_venda desc");
        if (rs.next()) {
            codVenda = (Integer) rs.getObject("cod_venda");
        }
        for (ItemVenda i : iv) {
            statement.execute("insert into tb_item_venda values (" + i.getQuantidade() + "," + i.getPreco() + "," + codVenda + "," + i.getCodProduto() + ")");
        }
        encerrarConexao();
    }

    public List<Venda> reportUsuario(int matricula) throws SQLException {
        List list = new ArrayList();
        iniciarConexao();
        ResultSet rs = statement.executeQuery("select cod_venda, data_venda, total_venda from tb_venda where fk_cliente =" + matricula);
        while (rs.next()) {
            Venda v = new Venda();
            v.setCod_venda((Integer) rs.getObject("cod_venda"));
            v.setDataVenda(String.valueOf(rs.getObject("data_venda")));
            v.setTotalVenda((Double) rs.getObject("total_venda"));
            list.add(v);
        }
        encerrarConexao();
        return list;
    }

    public List<VendaReport> reportFuncionario() throws SQLException {
        List list = new ArrayList();
        iniciarConexao();
        String sql = "select v.cod_venda, v.data_venda, v.total_venda, f.nome from tb_venda v, tb_funcionario f, tb_usuario u "
                + "where v.fk_usuario = u.cod_usuario and u.user_login = f.cpf";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            VendaReport v = new VendaReport();
            v.setCodVenda((Integer) rs.getObject("cod_venda"));
            v.setDataVenda(String.valueOf(rs.getObject("data_venda")));
            v.setTotalVenda((Double) rs.getObject("total_venda"));
            v.setNome((String) rs.getObject("nome"));
            list.add(v);
        }
        encerrarConexao();
        return list;
    }
}
