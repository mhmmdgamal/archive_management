/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barmagli.helper;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author MohamedGamal
 */
public class DatabaseHelper {

    public static void BackupDatabaseToSQL() {
        try {

            String dbName = "archive_management";
            String dbUser = "archive";
            String dbPass = "archive";
            String dbHost = "192.168.1.7";

            String mainFolder = "\\\\" + dbHost + "\\archive_management\\";
            String backupFolder = mainFolder + "backup\\";
            String savePath = backupFolder + "archive_management.sql";
            File f1 = new File(backupFolder);
            f1.mkdir();

            if (new File(savePath).exists()) {
                String lastBackups = new File(savePath).getParent() + "\\last-backups\\";
                new File(lastBackups).mkdirs();
                String lastBackup = lastBackups + Helper.getYear() + "-" + Helper.getMonth() + "-" + Helper.getDay() + "-" + "archive_management.sql";
                Files.deleteIfExists(new File(lastBackup).toPath());
                Files.copy(new File(savePath).toPath(), new File(lastBackup).toPath());
            }

            String executeCmd = mainFolder + "maria\\bin\\mysqldump -u" + dbUser + " -p" + dbPass + " -h" + dbHost + " --databases " + dbName + " -r " + savePath;

            /*NOTE: Executing the command here*/
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
            if (processComplete == 0) {
                JOptionPane.showMessageDialog(null, "تم انشاء نسخة احتياطية");
            } else {
                JOptionPane.showMessageDialog(null, "فشل انشاء نسخة احتياطية");
            }

        } catch (IOException | InterruptedException ex) {
            JOptionPane.showMessageDialog(null, "Error at Backuprestore" + ex.getMessage());
        }
    }

    public static void restoreDatabaseFromSQL(boolean openToChoose) {
        try {

            String dbName = "archive_management";
            String dbUser = "archive";
            String dbPass = "archive";
            String dbHost = "192.168.1.7";

            String mainFolder = "\\\\" + dbHost + "\\archive_management\\";
            String backupFolder = mainFolder + "backup\\";
            String restorePath = "";
            if (!openToChoose) {
                restorePath = backupFolder + "archive_management.sql";
                executeCommand(mainFolder, dbName, dbUser, dbPass, dbHost, restorePath);
            } else {
                JFileChooser file = new JFileChooser();
                file.setCurrentDirectory(new File(System.getProperty("user.home")));

                // هنا قمنا بتحديد نوع الصور التي يمكنك للمستخدم إختيارها من جهازه
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Select a .sql file", "sql");
                file.setFileFilter(filter);

                // هنا قمنا بإظهار النافذة و تخزين قيمة الزر الذي تم النقر عليه و أدى إلى إغلاق النافذة
                int result = file.showOpenDialog(null);

                // سيتم تعديل حجمها ليوافق حجم المربع, ثم ستوضع فيه Open بعد إختيار الصورة, إذا قام المستخدم بالنقر على الزر
                if (result == JFileChooser.APPROVE_OPTION) {
                    restorePath = file.getSelectedFile().getAbsolutePath();
                    executeCommand(mainFolder, dbName, dbUser, dbPass, dbHost, restorePath);
                }
            }

        } catch (IOException | InterruptedException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, "Error at Restoredbfromsql" + ex.getMessage());
        }
    }

    private static void executeCommand(String mainFolder, String dbName, String dbUser, String dbPass, String dbHost, String restorePath) throws InterruptedException, HeadlessException, IOException {
        String[] executeCmd = new String[]{mainFolder + "maria\\bin\\mysql", dbName, "-u" + dbUser, "-p" + dbPass, "-h" + dbHost, "-e", " source " + restorePath};
//            String executeCmd = mainFolder + "maria\\bin\\mysql -u" + dbUser + " -p" + dbPass + " -h" + dbHost + " " + dbName + " < " + restorePath;

        /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
        Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
        int processComplete = runtimeProcess.waitFor();

        /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
        if (processComplete == 0) {
            JOptionPane.showMessageDialog(null, "تم استرجاع بيانات ");
        } else {
            JOptionPane.showMessageDialog(null, "فشل استرجاع بيانات ");
        }
    }
}
