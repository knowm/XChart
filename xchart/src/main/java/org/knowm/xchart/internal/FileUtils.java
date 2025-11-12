package org.knowm.xchart.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    /**
     * Only adds the extension of the fileExtension to the filename if the filename doesn't already
     * have it.
     *
     * @param fileName File name
     * @param fileExtension File extension
     * @return filename (if extension already exists), otherwise;: filename + fileExtension
     */
    public static String addFileExtension(String fileName, String fileExtension) {
        String fileNameWithFileExtension = fileName;
        if (fileName.length() <= fileExtension.length()
                || !fileName
                .substring(fileName.length() - fileExtension.length())
                .equalsIgnoreCase(fileExtension)) {
            fileNameWithFileExtension = fileName + fileExtension;
        }
        return fileNameWithFileExtension;
    }

    /**
     * This method returns the files found in the given directory matching the given regular
     * expression.
     *
     * @param dirName - ex. "./path/to/directory/" *make sure you have the '/' on the end
     * @param regex - ex. ".*.csv"
     * @return File[] - an array of files
     */
    public static File[] getAllFiles(String dirName, String regex) {

        File[] allFiles = getAllFiles(dirName);

        List<File> matchingFiles = new ArrayList<File>();

        for (File allFile : allFiles) {

            if (allFile.getName().matches(regex)) {
                matchingFiles.add(allFile);
            }
        }

        return matchingFiles.toArray(new File[matchingFiles.size()]);
    }

    /**
     * This method returns the Files found in the given directory
     *
     * @param dirName - ex. "./path/to/directory/" *make sure you have the '/' on the end
     * @return File[] - an array of files
     */
    public static File[] getAllFiles(String dirName) {

        File dir = new File(dirName);

        File[] files = dir.listFiles(); // returns files and folders

        if (files != null) {
            List<File> filteredFiles = new ArrayList<File>();
            for (File file : files) {

                if (file.isFile()) {
                    filteredFiles.add(file);
                }
            }
            return filteredFiles.toArray(new File[filteredFiles.size()]);
        } else {
            System.out.println(dirName + " does not denote a valid directory!");
            return new File[0];
        }
    }

}
