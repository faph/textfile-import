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
package fap.dssgui.plugins;

import hec.heclib.util.HecTime;
import hec.hecmath.DSS;
import hec.hecmath.DSSFile;
import hec.hecmath.HecMath;
import hec.hecmath.HecMathException;
import hec.io.TimeSeriesContainer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;

public class Importer {

    private String dssFileName;
    private int progress,  progressUpdateInterval;
    private String message;
    private java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);
    private String importFolder,  fileExtension,  fileNamePartsDelimiter = "_";
    private int importFormat; //format taken from xml file, int idicates item in xml, starting at 0
    //formatting parameters
    private int firstDataLine;
    private int formatType; //0=fixed width, 1=delimited
    private int yearStartChar,  monthStartChar,  dayStartChar,  hourStartChar,  minuteStartChar;
    private int valueStartChar,  valueEndChar,  flagStartChar,  flagEndChar;
    private String missingValueFlag;
    private double missingValueValue;
    private String columnDelimiter;
    private int dateColumn,  timeColumn,  dateTimeColumn,  valueColumn,  flagColumn;
    private int dataQualityChecking; //0=dont, 1=check flag, 2=replace specific value, 3=both

    /**
     * Creates a new instance of Importer
     */
    public Importer(String dssFileName, String importFolder, int importFormat) {
        this.importFolder = importFolder;
        this.importFormat = importFormat;
        this.dssFileName = dssFileName;

        //get format parameters from xml file
        getFormatParameters();
    }

    private void getFormatParameters() {
        XmlHandler formatsXmlHandler = new XmlHandler("ImportTimeSeries/filters/ImportFilters.xml");
        int formatIndex = importFormat + 1;
        String xPathPart1 = "/formats/format[" + formatIndex + "]/";
        fileExtension = formatsXmlHandler.getNodeTextValue(xPathPart1 + "fileName/extension");
        firstDataLine = formatsXmlHandler.getNodeIntValue(xPathPart1 + "firstDataLine");
        formatType = formatsXmlHandler.getNodeIntValue(xPathPart1 + "fileFormat/type");
        yearStartChar = formatsXmlHandler.getNodeIntValue(xPathPart1 + "date/yearStartChar");
        monthStartChar = formatsXmlHandler.getNodeIntValue(xPathPart1 + "date/monthStartChar");
        dayStartChar = formatsXmlHandler.getNodeIntValue(xPathPart1 + "date/dayStartChar");
        hourStartChar = formatsXmlHandler.getNodeIntValue(xPathPart1 + "time/hourStartChar");
        minuteStartChar = formatsXmlHandler.getNodeIntValue(xPathPart1 + "time/minuteStartChar");

        if (formatType == 0) {
            valueStartChar = formatsXmlHandler.getNodeIntValue(xPathPart1 + "value/startChar");
            valueEndChar = formatsXmlHandler.getNodeIntValue(xPathPart1 + "value/endChar");
        } else if (formatType == 1) {
            columnDelimiter = formatsXmlHandler.getNodeTextValue(xPathPart1 + "fileFormat/columnDelimiter");
            dateColumn = formatsXmlHandler.getNodeIntValue(xPathPart1 + "date/column");
            timeColumn = formatsXmlHandler.getNodeIntValue(xPathPart1 + "time/column");
            valueColumn = formatsXmlHandler.getNodeIntValue(xPathPart1 + "value/column");
        }

        dataQualityChecking = 0;
        //missing value flag:
        if (formatsXmlHandler.getNodeCount(xPathPart1 + "qualityFlag/missingValueFlag") > 0) {
            missingValueFlag = formatsXmlHandler.getNodeTextValue(xPathPart1 + "qualityFlag/missingValueFlag");
            if (formatType == 0) {
                flagStartChar = formatsXmlHandler.getNodeIntValue(xPathPart1 + "qualityFlag/startChar");
                flagEndChar = formatsXmlHandler.getNodeIntValue(xPathPart1 + "qualityFlag/endChar");
                if ((flagStartChar > 0) && (flagEndChar > 0)) {
                    dataQualityChecking += 1;
                }
            } else if (formatType == 1) {
                flagColumn = formatsXmlHandler.getNodeIntValue(xPathPart1 + "qualityFlag/column");
                if (flagColumn > 0) {
                    dataQualityChecking += 1;
                }
            }
        }
        //value to replace by 'missing value':
        if (formatsXmlHandler.getNodeCount(xPathPart1 + "qualityFlag/missingValueValue") > 0) {
            missingValueValue = formatsXmlHandler.getNodeDoubleValue(xPathPart1 + "qualityFlag/missingValueValue");
            dataQualityChecking += 2;
        }
    }

    public void importAllFiles() {
        File[] importFiles;
        int fileCount, fileIndex;
        int[] recordCounts;
        long allRecordCount = 0;

        //get all files with appropriate extensions (e.g. .txt) from import folder
        File folder = new File(importFolder);
        importFiles = folder.listFiles(getImportFileFilter(fileExtension));
        fileCount = importFiles.length;
        setMessage("Import folder: " + folder.getPath().toUpperCase());
        setMessage("Number of files: " + fileCount);

        //get number of records in each file and total number of records
        recordCounts = new int[fileCount];
        for (fileIndex = 0; fileIndex < fileCount; fileIndex++) {
            recordCounts[fileIndex] = getFileRecordCount(importFiles[fileIndex]);
            allRecordCount += recordCounts[fileIndex];
        }
        progressUpdateInterval = (new Double(Math.ceil(allRecordCount / 100))).intValue();
        if (progressUpdateInterval < 1) {
            progressUpdateInterval = 1;
        }

        setProgress(0);
        //import each file
        for (fileIndex = 0; fileIndex < fileCount; fileIndex++) {
            importFile(importFiles[fileIndex]);
        }

        setProgress(100);
    }

    private void importFile(File file) {
        HecTime timeStep;
        int timeInterval;
        TimeSeriesContainer timeSeriesContainer = new TimeSeriesContainer();
        int recordCount, recordIndex, lineIndex;
        String line;
        String fullName, intervalText, intervalUnit;

        setMessage("Import file: " + file.getName().toUpperCase());

        //add meta data from the file name, assuming file name format "Watershed_Location_P/Q/H.ext"
        timeSeriesContainer = getMetaDataFromFileName(file, timeSeriesContainer);

        //get first data/time value from file
        timeStep = getStartDateFromFile(file, 1);
        timeInterval = getStartDateFromFile(file, 2).value() - timeStep.value();
        setMessage("Start time: " + timeStep.dateAndTime());
        setMessage("Interval: " + timeInterval + "MIN");

        //get total number of records
        recordCount = getFileRecordCount(file);
        setMessage("Number of records: " + recordCount);
        int[] times = new int[recordCount];
        double[] values = new double[recordCount];

        //loop through each line, store in dss, update progress 
        try {
            BufferedReader importFile = new BufferedReader(new FileReader(file));
            for (lineIndex = 1; lineIndex < firstDataLine; lineIndex++) {
                line = importFile.readLine();
            }
            for (recordIndex = 0; recordIndex < recordCount; recordIndex++) {
                line = importFile.readLine();
                if (line != null) {
                    //add entry in times column and increase with time interval
                    times[recordIndex] = timeStep.value();
                    timeStep.add(timeInterval);
                    double value;
                    String flag;

                    //try to get parameter value and flag
                    try {
                        if (formatType == 0) {
                            int valueEndCharCorr;
                            if (valueEndChar <= line.length() + 1) {
                                valueEndCharCorr = valueEndChar;
                            } else {
                                valueEndCharCorr = line.length() + 1;
                            }
                            value = Double.parseDouble(line.substring(valueStartChar - 1, valueEndCharCorr - 1).trim());

                            //quality checking
                            if ((dataQualityChecking == 1) || (dataQualityChecking == 3)) {
                                flag = line.substring(flagStartChar - 1, flagEndChar - 1).trim();
                                if (flag.equalsIgnoreCase(missingValueFlag)) {
                                    value = HecMath.UNDEFINED;
                                }
                            }
                            if ((dataQualityChecking == 2) || (dataQualityChecking == 3)) {
                                if (value == missingValueValue) {
                                    value = HecMath.UNDEFINED;
                                }
                            }
                        } else if (formatType == 1) {
                            value = Double.parseDouble(line.split(columnDelimiter)[valueColumn - 1]);

                            //quality checking
                            if ((dataQualityChecking == 1) || (dataQualityChecking == 3)) {
                                flag = line.split(columnDelimiter)[flagColumn - 1].trim();
                                if (flag.equalsIgnoreCase(missingValueFlag)) {
                                    value = HecMath.UNDEFINED;
                                }
                            }
                            if ((dataQualityChecking == 2) || (dataQualityChecking == 3)) {
                                if (value == missingValueValue) {
                                    value = HecMath.UNDEFINED;
                                }
                            }
                        } else {
                            value = HecMath.UNDEFINED;
                        }
                    } catch (Exception e) {
                        value = HecMath.UNDEFINED;
                    }
                    values[recordIndex] = value;
                }

                //update progress
                if (((recordIndex + 1) % progressUpdateInterval) == 0) {
                    setProgress(progress + 1);
                }

            }

            //add timeseriescontainer to dss
            timeSeriesContainer.times = times;
            timeSeriesContainer.interval = timeInterval;
            timeSeriesContainer.values = values;
            timeSeriesContainer.startTime = times[0];
            timeSeriesContainer.endTime = times[recordCount - 1];
            timeSeriesContainer.numberValues = recordCount;
            if (timeInterval >= 1440) {
                intervalText = (new Integer(new Double(Math.ceil(timeInterval / 1440F)).intValue())).toString();
                intervalUnit = "DAY";
            } else if (timeInterval >= 60) {
                intervalText = (new Integer(new Double(Math.ceil(timeInterval / 60F)).intValue())).toString();
                intervalUnit = "HOUR";
            } else {
                intervalText = (new Integer(timeInterval)).toString();
                intervalUnit = "MIN";
            }
            //full name/path to store data into DSS database
            fullName = "/" + timeSeriesContainer.watershed + "/" + timeSeriesContainer.location + "/" +
                    timeSeriesContainer.parameter + "//" + intervalText + intervalUnit + "/" +
                    timeSeriesContainer.version + "/";
            timeSeriesContainer.fullName = fullName;
            setMessage("DSS location: " + fullName);

            try {
                HecMath hecMath = HecMath.createInstance(timeSeriesContainer);
                timeSeriesContainer = null;
                DSSFile dssFile = DSS.open(dssFileName);
                dssFile.write(hecMath);
                hecMath = null;
            } catch (HecMathException e) {
                setMessage("An error occured while saving the dataset to the database.");
                setMessage(e.getMessage());
            }

            importFile.close();
        } catch (IOException e) {
        }


    }

    private HecTime getStartDateFromFile(File file, int lineNumber) {
        HecTime result = new HecTime();
         String  line = "", dateString = "", timeString = "", hecTimeString = "";
         String 
         
         year, month, day, hour, minute,  second;


         
                 
               int lineIndex;

        try {
            BufferedReader importFile = new BufferedReader(new FileReader(file));
            for (lineIndex = 1; lineIndex <= (firstDataLine + lineNumber - 1); lineIndex++) {
                line = importFile.readLine();
            }
            importFile.close();

            if (formatType == 0) {
                dateString = line;
                timeString = dateString;
            } else if (formatType == 1) {
                dateString = line.split(columnDelimiter)[dateColumn - 1];
                timeString = line.split(columnDelimiter)[timeColumn - 1];
            }

            year = dateString.substring(yearStartChar - 1, yearStartChar - 1 + 4); //assume 4 digits for year
            month = dateString.substring(monthStartChar - 1, monthStartChar - 1 + 2); //assume 2 digits for others
            day = dateString.substring(dayStartChar - 1, dayStartChar - 1 + 2);
            hour = timeString.substring(hourStartChar - 1, hourStartChar - 1 + 2);
            minute = timeString.substring(minuteStartChar - 1, minuteStartChar - 1 + 2);
            second = "00";
            hecTimeString = month + "/" + day + "/" + year + " " +
                    hour + ":" + minute + ":" + second;
            result.set(hecTimeString);

        } catch (IOException e) {
            result.setUndefined();
        }

        return result;
    }

    private TimeSeriesContainer getMetaDataFromFileName(File file, TimeSeriesContainer timeSeriesContainer) {
         String 
         
         watershed, location, parameterFromFile, parameter, units, valueType,version   ;

         TimeSeriesContainer  result   = timeSeriesContainer;

           int stopChar = file.getName().length() - fileExtension.length();
        String fileName = file.getName().substring(0, stopChar);
        String fileNameParts[] = fileName.split(fileNamePartsDelimiter);

        //get parameters from filename, usually in the format "Watershed_Location_P/Q/H/T.ext"
        if (fileNameParts.length > 1) {
            watershed = fileNameParts[0];
            location = fileNameParts[1];
            parameterFromFile = fileNameParts[fileNameParts.length - 1].toUpperCase();
        } else {
            watershed = "";
            location = fileName;
            parameterFromFile = "";
        }
        //set associated parameters
        if (parameterFromFile.startsWith("P") || parameterFromFile.startsWith("R")) {
            parameter = "PRECIP";
            units = "mm";
            valueType = "CUM-PER";
        } else if (parameterFromFile.startsWith("Q") || parameterFromFile.startsWith("F")) {
            parameter = "FLOW";
            units = "m3/s";
            valueType = "INST-VAL";
        } else if (parameterFromFile.startsWith("H") || parameterFromFile.startsWith("S")) {
            parameter = "STAGE";
            units = "m";
            valueType = "INST-VAL";
        } else if (parameterFromFile.startsWith("T")) {
            parameter = "TEMPERATURE";
            units = "°C";
            valueType = "INST-VAL";
        } else {
            parameter = "";
            units = "";
            valueType = "INST-VAL";
        }
        //other parameters
        version = "OBS";

        //add all parameters to timeseriescontainer
        result.watershed = watershed;
        result.location = location;
        result.parameter = parameter;
        result.units = units;
        result.type = valueType;
        result.version = version;
        return result;
    }

    private FileFilter getImportFileFilter(final String fileExtension) {
        FileFilter filter = new  

              FileFilter( ) {

                   
                 
            

            public    
                 boolean accept(File file) {
                String filename = file.getName().toUpperCase();
                return filename.endsWith(fileExtension.toUpperCase());
            }

            public String getDescription() {
                return "Import files (" + fileExtension + ")";
            }
        };
        return filter;
    }

    private int getFileRecordCount(File file) {
        int result = 0;
        try {
            RandomAccessFile randFile = new RandomAccessFile(file, "r");
            long lastRec = randFile.length();
            randFile.close();
            FileReader fileRead = new FileReader(file);
            LineNumberReader lineRead = new LineNumberReader(fileRead);
            lineRead.skip(lastRec);
            int recordCount = lineRead.getLineNumber() - 1;
            result =
                    recordCount - firstDataLine + 2; //not sure why + 2 is needed
            fileRead.close();
            lineRead.close();
        } catch (IOException e) {
            result = 0;
        }

        return result;
    }

    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        int oldProgress = this.progress;
        this.progress = progress;
        propertyChangeSupport.firePropertyChange("progress", new Integer(oldProgress), new Integer(progress));
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        String oldMessage = this.message;
        this.message = message;
        propertyChangeSupport.firePropertyChange("message", oldMessage, message);
    }
}
