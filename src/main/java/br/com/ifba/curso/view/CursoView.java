/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.ifba.curso.view;


import javax.swing.JTable;
import javax.swing.JLabel; // Se ainda não tiver
import javax.swing.JOptionPane; // Se ainda não tiver
import jakarta.persistence.EntityManager;
import br.com.ifba.curso.entity.Curso;
import org.springframework.context.ApplicationContext;
import br.com.ifba.infrastructure.util.SpringContext;




import javax.swing.table.DefaultTableModel; // Para o modelo da tabela

import java.awt.Component; // Para ButtonRenderer/Editor

import java.awt.Dimension;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import javax.swing.table.*;

import java.awt.event.*;
import br.com.ifba.curso.controller.CursoController;
import br.com.ifba.SpringBootCursoApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;


@Slf4j



/**
 *
 * @author jotin
 */
public class CursoView extends javax.swing.JFrame {
    private TableRowSorter<DefaultTableModel> sorter;
      private  EntityManager em;
      private CursoController cursoController;

    


    
    
  
public class IconRenderer extends DefaultTableCellRenderer {
    private final ImageIcon icon;

    public IconRenderer(ImageIcon icon) {
        this.icon = icon;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setIcon(icon);
        return label;
    }
}
    


    /**
     * Creates new form CursoView
     */
    public CursoView() {    
        
     try {
        // Obtem o controller do contexto Spring já inicializado
        this.cursoController = SpringContext.getBean(CursoController.class);

        initComponents();
        carregarCursos();


        // Centraliza a janela na tela
        setLocationRelativeTo(null);

        // Define o modelo da tabela com colunas específicas
        // A coluna ID fica oculta para uso interno apenas
      DefaultTableModel model = new DefaultTableModel(
    new Object[][] {},
    new String[] { "ID", "NOME", "CARGA HORÁRIA", "DESCRIÇÃO", "REMOVER", "EDITAR" }
) {
    boolean[] canEdit = new boolean[] { false, false, false, false, false, false };

    @Override
    public boolean isCellEditable(int row, int column) {
        return canEdit[column];
    }
};
tblCursos.setModel(model);
        
        

        // Configuração visual da tabela
        tblCursos.setModel(model);
        tblCursos.setPreferredScrollableViewportSize(new Dimension(800, 400));
        tblCursos.setRowHeight(40);

        // Carrega os dados do banco na tabela
        for (Curso curso : cursoController.listarCursos()) {
    Integer carga = curso.getCargaHoraria();
    if (carga == null) carga = 0; // valor padrão segur
    model.addRow(new Object[] {
        curso.getId(),
        curso.getNome(),
        carga,
        curso.getDescricao(),
        "REMOVER",
        "EDITAR"
    });
}

        // Habilita ordenação na tabela
        sorter = new TableRowSorter<>(model);
        tblCursos.setRowSorter(sorter);

        // Configura ícones para os botões de ação
        ImageIcon iconRemover = new ImageIcon(getClass().getResource("/imagens/lixeira.png"));
        ImageIcon iconEditar = new ImageIcon(getClass().getResource("/imagens/editar.png"));

        tblCursos.getColumn("REMOVER").setCellRenderer(new IconRenderer(iconRemover));
        tblCursos.getColumn("EDITAR").setCellRenderer(new IconRenderer(iconEditar));

        // Oculta a coluna ID permanentemente
        tblCursos.getColumnModel().getColumn(0).setMinWidth(0);
        tblCursos.getColumnModel().getColumn(0).setMaxWidth(0);
        tblCursos.getColumnModel().getColumn(0).setWidth(0);

        // Listener para tratar eventos de clique na tabela
        tblCursos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = tblCursos.rowAtPoint(evt.getPoint());
                int col = tblCursos.columnAtPoint(evt.getPoint());
                if (row < 0 || col < 0) return;

                int modelRow = tblCursos.convertRowIndexToModel(row); // índice do modelo

                // Ação de remover curso
                if (col == tblCursos.getColumn("REMOVER").getModelIndex()) {
                    int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Tem certeza que deseja remover este curso?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            Long id = Long.parseLong(model.getValueAt(modelRow, 0).toString());
                            cursoController.removerCurso(id);
                            model.removeRow(modelRow);
                            JOptionPane.showMessageDialog(null, "Curso removido com sucesso!");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Erro ao remover curso: " + ex.getMessage());
                        }
                    }
                }

                // Ação de editar curso
                if (col == tblCursos.getColumn("EDITAR").getModelIndex()) {
                    try {
                        Long id = Long.parseLong(model.getValueAt(modelRow, 0).toString());
                        Curso curso = cursoController.buscarCurso(id);

                        if (curso != null) {
                            String novoNome = JOptionPane.showInputDialog(null, "Novo nome:", curso.getNome());
                            if (novoNome == null || novoNome.trim().isEmpty()) return;

                            String novaCarga = JOptionPane.showInputDialog(null, "Nova carga horária:", curso.getCargaHoraria());
                            if (novaCarga == null || novaCarga.trim().isEmpty()) return;

                            String novaDesc = JOptionPane.showInputDialog(null, "Nova descrição:", curso.getDescricao());
                            if (novaDesc == null || novaDesc.trim().isEmpty()) return;

                            curso.setNome(novoNome);
                            curso.setCargaHoraria(Integer.parseInt(novaCarga));
                            curso.setDescricao(novaDesc);

                            cursoController.atualizarCurso(curso);

                            model.setValueAt(novoNome, modelRow, 1);
                            model.setValueAt(Integer.parseInt(novaCarga), modelRow, 2);
                            model.setValueAt(novaDesc, modelRow, 3);

                            JOptionPane.showMessageDialog(null, "Curso atualizado com sucesso!");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erro ao atualizar curso: " + ex.getMessage());
                    }
                }
            }
        });

        // Garantia adicional para ocultar a coluna ID
        tblCursos.getColumnModel().getColumn(0).setMinWidth(0);
        tblCursos.getColumnModel().getColumn(0).setMaxWidth(0);
        tblCursos.getColumnModel().getColumn(0).setWidth(0);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Erro ao iniciar a interface: " + e.getMessage());
    }
        
        }
    
    private void carregarCursos() {
   DefaultTableModel model = (DefaultTableModel) tblCursos.getModel();
    model.setRowCount(0); // Limpa todas as linhas da tabela

    for (Curso curso : cursoController.listarCursos()) {
        Integer carga = curso.getCargaHoraria() != null ? curso.getCargaHoraria() : 0;
        
        model.addRow(new Object[] {
            curso.getId(),
            curso.getNome(),
            carga,
            curso.getDescricao(),
            "REMOVER",
            "EDITAR"
        });
    }
    }
    
    

    
    private void filtrarTabela(String texto) {
    if (texto.trim().length() == 0) {
        sorter.setRowFilter(null); // mostra tudo
    } else {
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto)); // ignora maiúsculas
    }
}
 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topPainel = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        iconLupa = new javax.swing.JButton();
        txtBusca = new javax.swing.JTextField();
        btnRefresh = new javax.swing.JButton();
        rightTopPanel = new javax.swing.JPanel();
        btnHomescreen = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnAdcionar = new javax.swing.JButton();
        CursoManagementFrame = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCursos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        topPainel.setBackground(new java.awt.Color(51, 0, 204));
        topPainel.setPreferredSize(new java.awt.Dimension(1000, 100));
        topPainel.setLayout(new java.awt.BorderLayout());

        searchPanel.setBackground(new java.awt.Color(25, 50, 120));
        searchPanel.setMinimumSize(new java.awt.Dimension(80, 60));
        searchPanel.setPreferredSize(new java.awt.Dimension(450, 60));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        iconLupa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/lupa.png"))); // NOI18N
        iconLupa.setContentAreaFilled(false);
        iconLupa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconLupa.setFocusPainted(false);
        iconLupa.setPreferredSize(new java.awt.Dimension(40, 35));
        iconLupa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iconLupaActionPerformed(evt);
            }
        });
        searchPanel.add(iconLupa, new java.awt.GridBagConstraints());

        txtBusca.setBackground(new java.awt.Color(204, 204, 204));
        txtBusca.setColumns(25);
        txtBusca.setForeground(new java.awt.Color(51, 51, 51));
        txtBusca.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtBusca.setPreferredSize(new java.awt.Dimension(229, 35));
        txtBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscaActionPerformed(evt);
            }
        });
        searchPanel.add(txtBusca, new java.awt.GridBagConstraints());

        btnRefresh.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        searchPanel.add(btnRefresh, new java.awt.GridBagConstraints());

        topPainel.add(searchPanel, java.awt.BorderLayout.WEST);

        rightTopPanel.setBackground(new java.awt.Color(25, 50, 120));
        rightTopPanel.setMaximumSize(new java.awt.Dimension(2147483647, 70));
        rightTopPanel.setMinimumSize(new java.awt.Dimension(50, 70));
        rightTopPanel.setPreferredSize(new java.awt.Dimension(450, 60));
        rightTopPanel.setLayout(new java.awt.GridBagLayout());

        btnHomescreen.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnHomescreen.setText("Homescreen");
        btnHomescreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomescreenActionPerformed(evt);
            }
        });
        rightTopPanel.add(btnHomescreen, new java.awt.GridBagConstraints());

        topPainel.add(rightTopPanel, java.awt.BorderLayout.EAST);

        jPanel2.setBackground(new java.awt.Color(25, 50, 120));

        btnAdcionar.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        btnAdcionar.setText("+");
        btnAdcionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdcionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addComponent(btnAdcionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(180, 180, 180))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(btnAdcionar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        topPainel.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(topPainel, java.awt.BorderLayout.PAGE_START);

        CursoManagementFrame.setBackground(new java.awt.Color(204, 204, 204));
        CursoManagementFrame.setMaximumSize(new java.awt.Dimension(2147483647, 70));
        CursoManagementFrame.setMinimumSize(new java.awt.Dimension(50, 70));
        CursoManagementFrame.setPreferredSize(new java.awt.Dimension(450, 60));
        CursoManagementFrame.setLayout(new java.awt.GridBagLayout());

        tblCursos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NOME", "CARGA HORÁRIA", "DESCRIÇÃO", "REMOVER", "EDITAR"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCursos.setFillsViewportHeight(true);
        tblCursos.setRowHeight(40);
        tblCursos.setSelectionBackground(new java.awt.Color(200, 220, 225));
        tblCursos.setShowGrid(true);
        jScrollPane1.setViewportView(tblCursos);

        CursoManagementFrame.add(jScrollPane1, new java.awt.GridBagConstraints());

        getContentPane().add(CursoManagementFrame, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void iconLupaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iconLupaActionPerformed
String textoBusca = txtBusca.getText();
    filtrarTabela(textoBusca);        // TODO add your handling code here:
    }//GEN-LAST:event_iconLupaActionPerformed

    private void txtBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscaActionPerformed
     String texto = txtBusca.getText();
// faz o que precisar com o texto, tipo:
System.out.println("Texto buscado: " + texto);

    }//GEN-LAST:event_txtBuscaActionPerformed

    private void btnAdcionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdcionarActionPerformed
   try {
        String nome = JOptionPane.showInputDialog(this, "Nome do Curso: ");
        if (nome == null || nome.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome do curso é obrigatório.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cargaHorariaStr = JOptionPane.showInputDialog(this, "Carga Horária:");
        if (cargaHorariaStr == null || cargaHorariaStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "A carga horária é obrigatória.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer cargaHoraria;
        try {
            cargaHoraria = Integer.valueOf(cargaHorariaStr.trim());
            if (cargaHoraria <= 0) {
                JOptionPane.showMessageDialog(this, "A carga horária deve ser maior que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "A carga horária deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String descricao = JOptionPane.showInputDialog(this, "Descrição:");
        if (descricao == null || descricao.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "A descrição é obrigatória.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        

        // Cria e salva o curso
        Curso novoCurso = new Curso();
        novoCurso.setNome(nome.trim());
        novoCurso.setDescricao(descricao.trim());
        novoCurso.setCargaHoraria(cargaHoraria); // nunca vai ser null aqui

        cursoController.salvarCurso(novoCurso);

        // Atualiza a tabela
        DefaultTableModel model = (DefaultTableModel) tblCursos.getModel();
        model.addRow(new Object[]{
            novoCurso.getId(),
            novoCurso.getNome(),
            novoCurso.getCargaHoraria(),
            novoCurso.getDescricao(),
            "REMOVER",
            "EDITAR"
        });
        

        JOptionPane.showMessageDialog(this, "Parabens!! Curso salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro ao salvar o curso: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
   
    }//GEN-LAST:event_btnAdcionarActionPerformed

    private void btnHomescreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomescreenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHomescreenActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
         try {
        txtBusca.setText("");
        sorter.setRowFilter(null);
        carregarCursos();
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro ao atualizar dados: " + ex.getMessage(), 
            "Erro", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnRefreshActionPerformed

    /**
     * 
     * 
     * @param args the command line arguments
     */
    
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CursoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CursoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CursoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CursoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
           try {
              ApplicationContext context = SpringApplication.run(SpringBootCursoApplication.class, args);
SpringContext.init(context);
        java.awt.EventQueue.invokeLater(() -> {
            new CursoView().setVisible(true); 
        });
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Erro ao iniciar aplicação: " + ex.getMessage());
    }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CursoManagementFrame;
    private javax.swing.JButton btnAdcionar;
    private javax.swing.JButton btnHomescreen;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton iconLupa;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel rightTopPanel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTable tblCursos;
    private javax.swing.JPanel topPainel;
    private javax.swing.JTextField txtBusca;
    // End of variables declaration//GEN-END:variables
}
