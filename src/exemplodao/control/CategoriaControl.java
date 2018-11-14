/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exemplodao.control;

import exemplodao.model.Categoria;
import exemplodao.model.Produto;
import exemplodao.model.dao.CategoriaDao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alunos
 */
public class CategoriaControl {

    private JTextField tfNome;
    private JTextField tfPesquisa;
    private JTable tbCategoria;

    private Categoria categoria = null;
    private List<Categoria> listCategoria;
    private CategoriaDao categoriaDao;
    private Produto produto;

    public CategoriaControl() {
    }
    

    public CategoriaControl(JTextField tfnome, JTextField tfPesquisa, JTable tbCategoria) {
        this.tfNome = tfnome;
        this.tfPesquisa = tfPesquisa;
        this.tbCategoria = tbCategoria;
        listCategoria = new ArrayList<>();
        categoriaDao = new CategoriaDao();
    }

    public void cadastrarAction() {
        categoria = new Categoria();
        categoria.setNome(tfNome.getText());

        boolean res = categoriaDao.salvar(categoria);
        if (res == true) {
            JOptionPane.showMessageDialog(null, "Cadastrado com sucesso");
            listarAction();
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao tentar cadastrar");
        }
    }

    public void listarAction() {
        listCategoria = categoriaDao.listar();
        DefaultTableModel model = (DefaultTableModel) tbCategoria.getModel();
        model.setNumRows(0);
        for (Categoria c : listCategoria) {
            model.addRow(new Object[]{
                c.getId(),
                c.getNome()
            });

        }

    }

    public void pesquisarAction() {
        listCategoria = categoriaDao.pesquisarPorNome(tfPesquisa.getText());

        DefaultTableModel model = (DefaultTableModel) tbCategoria.getModel();
        model.setNumRows(0);
        for (Categoria c : listCategoria) {
            model.addRow(new Object[]{
                c.getId(),
                c.getNome()
            });

        }

    }

    public void excluirAction() {
        categoria = getItemSelecionado();
        if (categoria == null) {
            JOptionPane.showMessageDialog(null, "Escolha uma categoria");
        } else {
            boolean res = categoriaDao.excluir(categoria);
            if (res) {
                JOptionPane.showMessageDialog(null, "Categoria excluida");
                listarAction();
            } else {
                JOptionPane.showMessageDialog(null, " Erro ao excluir");
            }
        }

        categoria = null;
    }

    public void alterarAction() {
        categoria.setNome(tfNome.getText());
        boolean res = categoriaDao.atualizar(categoria);
        if (res) {
            JOptionPane.showMessageDialog(null, "Editado com sucesso");
            listarAction();
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao editar");
        }

    }

    private Categoria getItemSelecionado() {
        int linha = tbCategoria.getSelectedRow();
        if (linha >= 0) {
            return listCategoria.get(linha);

        } else {
            return null;
        }
    }

    public void popularFormAction() {
        categoria = getItemSelecionado();
        if (categoria != null) {
            tfNome.setText(categoria.getNome());
        }else{
            JOptionPane.showMessageDialog(null,"Escolha uma categoria");
        }
        tfNome.setText(categoria.getNome());
    }

    public void salvarAction() {
        if (categoria == null) {
            cadastrarAction();
        } else {
            alterarAction();
        }

        listarAction();
        tfNome.setText("");
        tfNome.requestFocus();
        categoria = null;
    }
    
}