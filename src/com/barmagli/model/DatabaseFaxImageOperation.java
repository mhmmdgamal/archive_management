/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barmagli.model;

import com.barmagli.dao.FaxImageDaoImpl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MohamedGamal
 */
public class DatabaseFaxImageOperation {

    private static final FaxImageDaoImpl FAX_IMAGE_DAO = new FaxImageDaoImpl();

    public static boolean addFaxImage(FaxImage faxImage, int lastFaxAddedId) {
        try {
            return FAX_IMAGE_DAO.add(faxImage, lastFaxAddedId);
        } catch (IOException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public static boolean updateFaxImage(FaxImage faxImage, int lastFaxAddedId) {
        try {
            return FAX_IMAGE_DAO.update(faxImage, lastFaxAddedId);
        } catch (IOException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public static boolean deleteFaxImage(int id) {
        return FAX_IMAGE_DAO.delete(id);
    }

    public static List<File> getFaxImagePaths(int id) {
        List<File> images = new ArrayList();
        List<FaxImage> allByFaxId = FAX_IMAGE_DAO.getAllByFaxId(id);
        allByFaxId.forEach((faxImage) -> {
            images.add(new File(faxImage.getImagePath()));
        });
        return images;
    }
}
