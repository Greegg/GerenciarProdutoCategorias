/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exemplodao.model.dao;

import exemplodao.factory.Dao;
import exemplodao.interfaces.DaoI;
import exemplodao.model.Categoria;
import exemplodao.model.Produto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alunos
 */
public class ProdutoDao extends Dao implements DaoI<Produto> {

    private PreparedStatement stmt;
    private Produto produto;

    @Override
    public boolean salvar(Produto obj) {

        try {
            stmt = conexao.prepareStatement("INSERT INTO produto(nome,valor, categoria_id) VALUES(?,?,?)");
            stmt.setString(1, obj.getNome());
            stmt.setFloat(2, obj.getValor());
            stmt.setInt(3,obj.getCategoria().getId());
            int res = stmt.executeUpdate();
            if (res > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean atualizar(Produto obj) {
        try {
            stmt = conexao.prepareStatement("UPDATE produto SET nome =?, valor=?, categoria_id=? WHERE id = ?");
            
            stmt.setString(1, obj.getNome());
            stmt.setFloat(2, obj.getValor());
            stmt.setInt(3,obj.getCategoria().getId());
            stmt.setInt(4, obj.getId());
            
            if (stmt.executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean excluir(Produto obj) {
        try {
            stmt = conexao.prepareStatement("DELETE FROM produto WHERE id = ?");
            stmt.setInt(1, obj.getId());
            return (stmt.executeUpdate() > 0);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Produto> listar() {
        try {
            CategoriaDao cat = new CategoriaDao();
            stmt = conexao.prepareStatement("SELECT id, nome, valor, categoria_id FROM produto ORDER BY id ASC");
            ResultSet result = stmt.executeQuery();
            List<Produto> lista = new ArrayList<>();
            
            while (result.next()) {
                Produto p = new Produto();
                p.setId(result.getInt("id"));
                p.setNome(result.getString("nome"));
                p.setValor(result.getFloat("Valor"));
                p.getCategoria().setNome(result.getString("categoria_id"));

                Categoria cate = cat.lerPorId(result.getInt("categoria_id"));
                p.setCategoria(cate);
                lista.add(p);
            }
            return lista;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public Produto lerPorId(int id) {
        try {
            stmt = conexao.prepareStatement("SELECT id, nome,valor FROM categoria");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            Produto p = new Produto();
            if (res.next()) {
                p.setId(res.getInt("id"));
                p.setNome(res.getString("nome"));
                p.setValor(res.getFloat("Valor"));
            }
            return p;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Produto> pesquisarPorNome(String nome) {
        try {
            CategoriaDao cat = new CategoriaDao();
            stmt = conexao.prepareStatement("SELECT id, nome, valor,categoria_id FROM produto WHERE nome LIKE ?");
            stmt.setString(1, nome + "%");
            ResultSet res = stmt.executeQuery();
            List<Produto> lista = new ArrayList<>();
            while (res.next()) {
                Produto p = new Produto();
                p.setId(res.getInt("id"));
                p.setNome(res.getString("nome"));
                p.setValor(res.getFloat("valor"));
                p.getCategoria().setNome(res.getString("categoria_id"));
                
                
                Categoria cate = cat.lerPorId(res.getInt("categoria_id"));
                p.setCategoria(cate);
                lista.add(p);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List listarTodos() throws SQLException {
        List<Produto> listProduto = new ArrayList<>();
        try {

            stmt = conexao.prepareStatement("SELECT * FROM produto");
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                produto = new Produto();
                produto.setId(res.getInt("id"));
                produto.setNome(res.getString("nome"));
                listProduto.add(produto);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar todos " + e.getMessage());
        } finally {

        }
        return listProduto;
    }

}
