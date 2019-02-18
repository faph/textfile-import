/* 
 * The MIT License
 *
 * Copyright 2008-2019 Florenz A. P. Hollebrandse.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hec.dssgui.plugins.textfileimport;

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
