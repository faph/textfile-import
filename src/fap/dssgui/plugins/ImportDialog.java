package fap.dssgui.plugins;

import hec.dssgui.ListSelection;
import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.help.*;

public class ImportDialog extends javax.swing.JDialog implements PropertyChangeListener {

    private static ListSelection listSelection;
    private HistoryHandler foldersHistoryHandler;
    private HelpBroker hb;

    public ImportDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        listSelection = (ListSelection) parent;
        setUpHelp();
        initComponents();
        populateComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel6 = new javax.swing.JLabel();
        browseButton = new javax.swing.JButton();
        folderCombo = new javax.swing.JComboBox();
        importProgress = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        importTypeCombo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        progressMessageText = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        importFiltersMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpContentsMenuItem = new javax.swing.JMenuItem();
        helpContentsMenuItem.addActionListener(new CSH.DisplayHelpFromSource( hb ));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Time series importing");
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel6.setText("Progress:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        getContentPane().add(jLabel6, gridBagConstraints);

        browseButton.setText("...");
        browseButton.setPreferredSize(new java.awt.Dimension(23, 23));
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 10);
        getContentPane().add(browseButton, gridBagConstraints);

        folderCombo.setEditable(true);
        folderCombo.setMaximumRowCount(10);
        folderCombo.setLightWeightPopupEnabled(false);
        folderCombo.setMaximumSize(new java.awt.Dimension(800, 50));
        folderCombo.setMinimumSize(new java.awt.Dimension(200, 20));
        folderCombo.setOpaque(false);
        folderCombo.setPreferredSize(new java.awt.Dimension(350, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 40);
        getContentPane().add(folderCombo, gridBagConstraints);

        importProgress.setFocusable(false);
        importProgress.setMaximumSize(new java.awt.Dimension(800, 21));
        importProgress.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 10);
        getContentPane().add(importProgress, gridBagConstraints);

        jLabel1.setText("Folder:");
        jLabel1.setMinimumSize(new java.awt.Dimension(20, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        buttonsPanel.setLayout(new java.awt.GridBagLayout());

        okButton.setText("Import");
        okButton.setMinimumSize(new java.awt.Dimension(80, 23));
        okButton.setPreferredSize(new java.awt.Dimension(80, 23));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        buttonsPanel.add(okButton, gridBagConstraints);

        cancelButton.setText("Close");
        cancelButton.setMinimumSize(new java.awt.Dimension(80, 23));
        cancelButton.setPreferredSize(new java.awt.Dimension(80, 23));
        cancelButton.setSelected(true);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        buttonsPanel.add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 10, 10);
        getContentPane().add(buttonsPanel, gridBagConstraints);

        jLabel2.setText("(recommended file name format: Watershed_Location_Q/H/P.ext)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        getContentPane().add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        getContentPane().add(jSeparator1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 0);
        getContentPane().add(importTypeCombo, gridBagConstraints);

        jLabel3.setText("Filter:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 5, 0);
        getContentPane().add(jLabel3, gridBagConstraints);

        progressMessageText.setColumns(20);
        progressMessageText.setEditable(false);
        progressMessageText.setRows(10);
        jScrollPane1.setViewportView(progressMessageText);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jLabel4.setText("Messages:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        getContentPane().add(jLabel4, gridBagConstraints);

        fileMenu.setText("File");

        importFiltersMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        importFiltersMenuItem.setText("Import filters...");
        importFiltersMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importFiltersMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(importFiltersMenuItem);

        jMenuBar1.add(fileMenu);

        helpMenu.setText("Help");

        helpContentsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpContentsMenuItem.setText("Contents");
        helpMenu.add(helpContentsMenuItem);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();

        if ("progress".equals(propertyName)) {
            Integer prog = (Integer) propertyChangeEvent.getNewValue();
            importProgress.setValue(prog);
        } else if ("message".equals(propertyName)) {
            String message = (String) propertyChangeEvent.getNewValue();
            progressMessageText.append(message + "\n");
            progressMessageText.setCaretPosition(progressMessageText.getLineCount() - 1);
        }
    }

    private void populateComponents() {
        foldersHistoryHandler = new HistoryHandler(folderCombo, "importFolders");

        XmlHandler formatsXmlHandler = new XmlHandler("ImportTimeSeries/filters/ImportFilters.xml");
        formatsXmlHandler.pushToCombo("/formats/format[*]/name", importTypeCombo);
        if (importTypeCombo.getItemCount() > 0) {
            importTypeCombo.setSelectedIndex(PreferencesHandler.getPreferenceInt("importTypeDefault"));
        } else {
            progressMessageText.append("No import filters defined." + "\n");
            okButton.setEnabled(false);
        }
    }

    private void setUpHelp() {
        HelpSet hs;
        ClassLoader cl = ImportDialog.class.getClassLoader();
        try {
            URL hsURL = HelpSet.findHelpSet(cl, "javahelp/org/fap/dssgui/plugins/textfileimporter/docs/master.hs");
            hs = new HelpSet(null, hsURL);
        } catch (Exception e) {
            Logger.getLogger(ImportDialog.class.getName()).log(Level.SEVERE, null, e);
            return;
        }
        hb = hs.createHelpBroker();
    }

    private void saveSettings() {
        PreferencesHandler.setPreference("importTypeDefault", importTypeCombo.getSelectedIndex());
        setComboFirstItem();
    }

    private void setComboFirstItem() {
        if (folderCombo.getItemCount() > 0) {
            foldersHistoryHandler.moveToTop(folderCombo.getSelectedIndex());
        }

    }

   private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
       okButton.setEnabled(false);
       setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       importProgress.setValue(0);
       importProgress.setIndeterminate(true);

       saveSettings();
       progressMessageText.setText("");
       final Importer importer = new Importer(listSelection.getDSSFilename(),
               foldersHistoryHandler.getItem(folderCombo.getSelectedIndex()),
               importTypeCombo.getSelectedIndex());

       importer.addPropertyChangeListener(this);

       final SwingWorker worker = new SwingWorker() {

           @Override
           public Object construct() {
               importProgress.setIndeterminate(false);
               importer.importAllFiles();

               listSelection.updateCatalog(true);
               okButton.setEnabled(true);
               setCursor(null);

               return null;
           }
       };
       worker.start();

   }//GEN-LAST:event_okButtonActionPerformed

   private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
       JFileChooser chooser = new JFileChooser();
       try {
           File f = new File(new File(".").getCanonicalPath());
           chooser.setCurrentDirectory(f);
       } catch (IOException e) {
       }
       chooser.setDialogTitle("Select folder with files to import");
       chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
       int result = chooser.showOpenDialog(listSelection);
       switch (result) {
           case JFileChooser.APPROVE_OPTION:
               File file = chooser.getSelectedFile();
               foldersHistoryHandler.addItem(file.getPath());
               break;
           case JFileChooser.CANCEL_OPTION:
               break;
           case JFileChooser.ERROR_OPTION:
               break;
       }

   }//GEN-LAST:event_browseButtonActionPerformed

   private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
       this.dispose();
   }//GEN-LAST:event_cancelButtonActionPerformed

   private void importFiltersMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importFiltersMenuItemActionPerformed
       try {
           File file = new File(this.getClass().getResource("/ImportTimeSeries/filters/ImportFilters.xml").getPath().replaceAll("%20", " "));
           Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
       } catch (Exception e) {
           progressMessageText.append("Cannot open import filters file (./ImportFilters.xml)." + "\n");
       }
   }//GEN-LAST:event_importFiltersMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ImportDialog(listSelection, true).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JComboBox folderCombo;
    private javax.swing.JMenuItem helpContentsMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem importFiltersMenuItem;
    private javax.swing.JProgressBar importProgress;
    private javax.swing.JComboBox importTypeCombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton okButton;
    private javax.swing.JTextArea progressMessageText;
    // End of variables declaration//GEN-END:variables
}
