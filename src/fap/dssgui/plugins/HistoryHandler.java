package fap.dssgui.plugins;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

public class HistoryHandler {

   private List itemsList;
   private static int shortFileLength;
   private JComboBox comboBox;
   String Name;

   public void PushToCombo() {
      int itemIndex;
      String displayFile;

      comboBox.removeAllItems();
      for (itemIndex = 0; itemIndex < itemsList.size(); itemIndex++) {
         displayFile = (String) itemsList.get(itemIndex);

         if (displayFile.length() > shortFileLength) {
            displayFile = displayFile.substring(0, 3) + "..." +
                    displayFile.substring(displayFile.length() - (shortFileLength - 6));
         }
         comboBox.addItem(displayFile);
      }
   }

   public void addItem(String folder) {
      int itemIndex;

      if (!itemsList.contains(folder)) {
         itemsList.add(0, folder); //add in first row
         if (itemsList.size() == 11) { //if list longer than 10 remove row 11
            itemsList.remove(10);
         }
      } else {
         int existingitemIndex = itemsList.indexOf(folder);
         for (itemIndex = existingitemIndex; itemIndex > 0; itemIndex--) {
            itemsList.set(itemIndex, itemsList.get(itemIndex - 1)); //move all folders above the existing folder one row donw
         }
         itemsList.set(0, folder); //move existing folder to one
      }
      PushToCombo();
      saveAllItems();
   }

   public void moveToTop(int selecteditemIndex) {
      int itemIndex;
      Object selectedFile = itemsList.get(selecteditemIndex);

      for (itemIndex = selecteditemIndex; itemIndex > 0; itemIndex--) {
         itemsList.set(itemIndex, itemsList.get(itemIndex - 1)); //move all folders above the existing folder one row donw
      }
      itemsList.set(0, selectedFile); //move existing folder to one
      PushToCombo();
      saveAllItems();
   }

   public void saveAllItems() {
      int itemIndex;

      for (itemIndex = 0; itemIndex < itemsList.size(); itemIndex++) {
         PreferencesHandler.setPreference(Name + itemIndex, (String) itemsList.get(itemIndex));
      }
   }

   public String getItem(int itemIndex) {
      String result;

      if (itemIndex < itemsList.size()) {
         result = (String) itemsList.get(itemIndex);
      } else {
         result = (String) itemsList.get(0);
      }
      return result;
   }

   /** Creates a new instance of ExportFoldersHandler */
   public HistoryHandler(JComboBox combo, String Name) {
      int itemIndex;

      shortFileLength = 50;
      comboBox = combo;
      this.Name = Name;

      itemsList = new ArrayList();

      itemIndex = 0;
      while ((PreferencesHandler.getPreference(Name + itemIndex)) != "") {
         itemsList.add(PreferencesHandler.getPreference(Name + itemIndex));
         itemIndex++;
      }
      PushToCombo();
   }
}
