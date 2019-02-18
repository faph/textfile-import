package fap.dssgui.plugins;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import hec.dssgui.ListSelection;
import rma.awt.FlatPanelButton;

public class ControlImportPlugin {

    private static ListSelection _listSelection;

    public static void main(Object[] args) {
        if (args.length > 0 && args[0] instanceof ListSelection) {
            _listSelection = (ListSelection) args[0];
        }
        UIManager.put("TextField.disabledBackground", UIManager.get("TextField.inactiveBackground"));

        final ListSelection listSelection = (ListSelection) args[0];
        FlatPanelButton pdmButton = new FlatPanelButton("Import...");
        pdmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String currentDirectory = listSelection.getDSSFilename();
                if (currentDirectory == null || currentDirectory.length() == 0) {
                    JOptionPane.showMessageDialog(listSelection, "Please select a DSS file",
                            "No file selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                ImportDialog importDialog = new ImportDialog(_listSelection, false);
                importDialog.setLocationRelativeTo(listSelection);
                importDialog.setVisible(true);

            }
        });
        listSelection.getToolBar().add(pdmButton);
    }
}
