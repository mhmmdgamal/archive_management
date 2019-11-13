/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barmagli.model;

import com.barmagli.dao.FaxDaoImpl;
import com.barmagli.helper.MySQLDatabaseHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author MohamedGamal
 */
public class DatabaseFaxOperation {

    public static final FaxDaoImpl FAX_DAO = new FaxDaoImpl();
    public static MySQLDatabaseHelper db = MySQLDatabaseHelper.getInstance();

    public static int getSendNumberFormFaxRef(int faxRef) {
        return FAX_DAO.getById(faxRef).getSendNumber();
    }

    public static Fax getFaxBySendNumber(int sendNumber) {
        return FAX_DAO.getBySendNumber(sendNumber);
    }

    public static void showFaxes() {
        List<Fax> faxes = FAX_DAO.getAll();
        faxes.forEach(f -> System.out.println(f));
    }

    public static boolean addFax(Fax fax) {
        return FAX_DAO.add(fax);
    }
    
    public static boolean deleteFax(int id) {
        return FAX_DAO.delete(id);
    }

    public static boolean updateFax(Fax fax) {
        return FAX_DAO.update(fax);
    }

    /**
     * get decending sorted list of faxes have the search value depending on
     * rank
     *
     * @param value
     * @param searchFaxes
     * @return
     */
    public static List<Fax> getFaxesByAboutWithRankTakeLargeMemory(String value, List<Fax> searchFaxes) {
        // split search value to array
        String[] splitValue = value.split(" ");
        if (searchFaxes.isEmpty()) {
            searchFaxes = FAX_DAO.getAll();
        }
        // rank faxes: depend on words found on the about
        for (String v : splitValue) {
            for (int i = 0; i < searchFaxes.size(); i++) {
                if (searchFaxes.get(i).getAbout().contains(v)) {
                    searchFaxes.get(i).rank += 1;
                }
            }
        }

        // sort faxes decend
        Collections.sort(searchFaxes, (Fax t, Fax t1) -> {
            if (t.rank == t1.rank) {
                return 0;
            } else if (t.rank > t1.rank) {
                return -1;
            } else {
                return 1;
            }
        });

        // remove faxes if rank == 0
        searchFaxes.removeIf((Fax t) -> t.rank == 0);

        return searchFaxes;
    }

    public static List<Fax> getRelatedFaxes(int sendNumber) {
        Fax firstFax = moveToFirstFax(FAX_DAO.getBySendNumber(sendNumber));
        List<Fax> faxes = new ArrayList();
        faxes.add(firstFax);
        iterateOverNextFaxes(firstFax, faxes);
        List<Fax> finalResults = new ArrayList();
        faxes.stream().filter((f) -> (!finalResults.contains(f))).forEachOrdered((f) -> {
            finalResults.add(f);
        });
        finalResults.forEach(f -> System.out.println(f));
        return finalResults;
    }

    private static Fax moveToFirstFax(Fax fax) {
        if (fax.getFaxRef() != 0) {
            Fax f = fax;
            fax = FAX_DAO.getById(fax.getFaxRef());
            if (fax.getId() != 0) {
                return moveToFirstFax(fax);
            } else {
                return f;
            }
        } else {
            return fax;
        }
    }

    private static void iterateOverNextFaxes(Fax fax, List<Fax> faxes) {
        List<Fax> allByFaxRef = FAX_DAO.getAllByFaxRef(fax.getId());
        if (!allByFaxRef.isEmpty()) {
            if (allByFaxRef.size() == 1) {
                faxes.add(allByFaxRef.get(0));
                iterateOverNextFaxes(allByFaxRef.get(0), faxes);
            } else {
                for (Fax fax1 : allByFaxRef) {
                    faxes.add(fax1);
                    iterateOverNextFaxes(fax1, faxes);
                    getNextFax(fax1, faxes);
                }
            }
        }
        if (fax.getId() != 0) {
            faxes.add(fax);
            getNextFax(fax, faxes);
        }
    }

    private static void getNextFax(Fax fax, List<Fax> faxes) {
        fax = FAX_DAO.getByFaxRef(fax.getId());
        if (fax.getId() != 0) {
            faxes.add(fax);
            getNextFax(fax, faxes);
        }
    }

//    /**
//     * get related faxes with each other
//     *
//     * @param sendNumber
//     * @return
//     */
//    public static List<Fax> getRelatedFaxes(int sendNumber) {
//        Fax fax = FAX_DAO.getBySendNumber(sendNumber);
//        List<Fax> faxes = new ArrayList();
//        faxes.add(fax);
//        getPreviosFax(fax, faxes);
//        getNextFax(fax, faxes);
//        List<Fax> finalResults = new ArrayList();
//        for (Fax f : faxes) {
//            if (!finalResults.contains(f)) {
//                finalResults.add(f);
//            }
//        }
//        finalResults.forEach(f -> System.out.println(f));
//        return finalResults;
//    }
//
//    private static void getPreviosFax(Fax fax, List<Fax> faxes) {
//        Fax f = new Fax();
//        if (fax.getFaxRef() != 0) {
//            f = fax;
//            fax = FAX_DAO.getById(fax.getFaxRef());
//            faxes.add(fax);
//            getPreviosFax(fax, faxes);
//        }
//        if (fax.getFaxRef() == 0) {
//            List<Fax> allByFaxRef = FAX_DAO.getAllByFaxRef(fax.getId());
//            for (Fax fax1 : allByFaxRef) {
//                if (fax1.getId() != f.getId()) {
//                    faxes.add(fax1);
//                    f = fax1;
//                    getNextFax(fax1, faxes);
//                }
//            }
//        }
//    }
//
//    private static void getNextFax(Fax fax, List<Fax> faxes) {
//        fax = FAX_DAO.getByFaxRef(fax.getId());
//        if (fax.getId() != 0) {
//            faxes.add(fax);
//            getNextFax(fax, faxes);
//        }
//    }
    public static List<Fax> getFaxesByAboutWithRankTakeLessMemory(String value) {
        // split search value to array
        String[] splitValue = value.split(" ");
        List<Fax> countResults = new ArrayList();
        for (String v : splitValue) {
            List<Fax> list = FAX_DAO.getAllByAbout(v);
            if (countResults.isEmpty()) {
                list.forEach(f -> f.rank += 1);
                countResults.addAll(list);
            } else {
                for (Fax newFax : list) {
                    for (Fax oldFax : countResults) {
                        if (!countResults.contains(newFax)) {
                            newFax.rank += 1;
                            countResults.add(newFax);
                            break;
                        }
                        if (newFax.getId() == oldFax.getId()) {
                            oldFax.rank += 1;
                            break;
                        }
                    }
                }
            }
        }

        // sort faxes decend
        Collections.sort(countResults, (Fax t, Fax t1) -> {
            if (t.rank == t1.rank) {
                return 0;
            } else if (t.rank > t1.rank) {
                return -1;
            } else {
                return 1;
            }
        });
        System.out.println(countResults.size());
        countResults.forEach(f -> System.out.println(f));
        return countResults;
    }

    public static Fax getFaxById(int id) {
        return FAX_DAO.getById(id);
    }
}
