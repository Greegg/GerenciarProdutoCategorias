/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exemplodao.control;

import exemplodao.interfaces.DaoI;
import exemplodao.model.Categoria;
import exemplodao.model.Produto;
import exemplodao.model.dao.CategoriaDao;
import exemplodao.model.dao.ProdutoDao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ComboBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alunos
 */
public class ProdutoControl {

    private JTextField tfNome;
    private JTextField tfPesquisa;
    private JTextField tfValor;
    private JTable tbProduto;
    private JComboBox comboCategoria;

    private Produto produto = null;
    private List<Produto> listProduto;
    private ProdutoDao produtoDao;
    private CategoriaDao catDao;

    private String msg;

    public ProdutoControl(JTextField tfNome, JTextField tfPesquisa, JTextField tfValor, JTable tbProduto, JComboBox comboCategoria) {
        this.tfNome = tfNome;
        this.tfPesquisa = tfPesquisa;
        this.tfValor = tfValor;
        this.tbProduto = tbProduto;
        this.comboCategoria = comboCategoria;
        
        listProduto = new ArrayList<>();
        produtoDao = new ProdutoDao();
        catDao = new CategoriaDao();
    }

    public void cadastrarAction() {
        produto = new Produto();
        produto.setNome(tfNome.getText());
        produto.setValor(Float.parseFloat(tfValor.getText()));
        produto.setCategoria((Categoria) comboCategoria.getSelectedItem());

        boolean res = produtoDao.salvar(produto);
        if (res == true) {
            JOptionPane.showMessageDialog(null, "Cadastrado com sucesso");
            listarAction();
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao tentar cadastrar");
        }
    }

    public void listarAction() {
        listProduto = produtoDao.listar();
        DefaultTableModel model = (DefaultTableModel) tbProduto.getModel();
        model.setNumRows(0);

        for (Produto c : listProduto) {
            model.addRow(new Object[]{
                c.getId(),
                c.getNome(),
                c.getValor(),
                c.getCategoria().getNome()

            });
        }
    }

    public void pesquisarAction() {
        listProduto = produtoDao.pesquisarPorNome(tfPesquisa.getText());

        DefaultTableModel model = (DefaultTableModel) tbProduto.getModel();
        model.setNumRows(0);
        for (Produto c : listProduto) {
            model.addRow(new Object[]{
                c.getId(),
                c.getNome(),
                c.getValor(),
                c.getCategoria().getNome()
            });

        }

    }

    public void excluirAction() {
        produto = getItemSelecionado();
        if (produto == null) {
            JOptionPane.showMessageDialog(null, "Escolha uma categoria");
        } else {
            boolean res = produtoDao.excluir(produto);
            if (res) {
                JOptionPane.showMessageDialog(null, "Categoria excluida");
                listarAction();
            } else {
                JOptionPane.showMessageDialog(null, " Erro ao excluir");
            }
        }

        produto = null;
    }

    public void alterarAction() {
        produto.setNome(tfNome.getText());
        produto.setValor(Float.parseFloat(tfValor.getText()));

        boolean res = produtoDao.atualizar(produto);
        if (res) {
            JOptionPane.showMessageDialog(null, "Editado com sucesso");
            listarAction();
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao editar");
        }

    }

    private Produto getItemSelecionado() {
        int linha = tbProduto.getSelectedRow();
        if (linha >= 0) {
            return listProduto.get(linha);

        } else {
            return null;
        }
    }

    public void popularFormAction() {
        produto = getItemSelecionado();
        if (produto != null) {
            tfNome.setText(produto.getNome());
            tfValor.setText("" + produto.getValor());
        } else {
            JOptionPane.showMessageDialog(null, "Escolha um produto");
        }

    }

    public void salvarAction() {
        if (produto == null) {
            if (validacaoProduto()) {
                JOptionPane.showMessageDialog(null, msg, "Erro de validação", JOptionPane.ERROR_MESSAGE);
            } else {
                cadastrarAction();
            }
        } else {
            alterarAction();
        }

        listarAction();
        tfNome.setText("");
        tfNome.requestFocus();
        tfValor.setText("");
        tfValor.requestFocus();
        produto = null;
    }

    private boolean validacaoProduto() {
        boolean ok = false;

        if (tfNome.getText().isEmpty()) {
            ok = true;
            msg = "Nome é obrigatório\n";
        }
        if (tfValor.getText().isEmpty()) {
            msg += "Valor é obrigatório\n";
        }

        return ok;

    }

    public void categoriaProduto() {
        try {
            List<Categoria> listCategoria = catDao.listarTodos();
            comboCategoria.removeAllItems();
            for (Categoria categorias : listCategoria) {
                comboCategoria.addItem(categorias);
            }
        } catch (Exception e) {
            System.err.println("Erro ao pesquisar categorias " + e.getMessage());
        }

    }

}
