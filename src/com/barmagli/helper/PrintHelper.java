/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barmagli.helper;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.JobAttributes;
import java.awt.PageAttributes;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author MohamedGamal
 */
public class PrintHelper {

    public static void printJob(JFrame frame, File[] selecFiles, int current, int total) {
        JobAttributes ja = new JobAttributes();

        PageAttributes pa = new PageAttributes();
        pa.setMedia(PageAttributes.MediaType.A4);

        PrintJob job = Toolkit.getDefaultToolkit().getPrintJob(frame, "Print Fax From Archive Manager", ja, pa);

        if (job == null) {
            return;
        }

        JobAttributes.DefaultSelectionType defaultSelection = ja.getDefaultSelection();
        int copies = ja.getCopies();

        if (defaultSelection.toString().equals(JobAttributes.DefaultSelectionType.SELECTION.toString())) {
            // print selected page
            for (int i = 0; i < copies; i++) {
                print(job, selecFiles[current - 1]);
            }
        } else if (defaultSelection.toString().equals(JobAttributes.DefaultSelectionType.RANGE.toString())) {
            // print range pages
            int fromPage = ja.getPageRanges()[0][0];
            int toPage = ja.getPageRanges()[0][1];
            if (toPage > total) {
                toPage = total;
            }

            for (int i = 0; i < copies; i++) {
                for (int j = fromPage; j <= toPage; j++) {
                    print(job, selecFiles[j - 1]);
                }
            }
        } else {
            // print all pages
            for (int i = 0; i < copies; i++) {
                for (int j = 0; j < total; j++) {
                    print(job, selecFiles[j]);
                }
            }
        }

        job.end();
    }

    public static void print(PrintJob job, File selectedImage) {

        Graphics page = job.getGraphics();

        Dimension pagesize = job.getPageDimension();

        ImageIcon image = new javax.swing.ImageIcon(selectedImage.getAbsolutePath());

        page.setClip(0, 0, pagesize.width, pagesize.height);
        page.drawImage(image.getImage(), 0, 0, pagesize.width, pagesize.height, null);

        page.dispose();
    }
}
