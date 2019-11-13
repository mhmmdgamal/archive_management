/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barmagli.dao;

import com.barmagli.model.FaxPlace;
import java.util.List;

public interface PlaceDao {

    public boolean add(FaxPlace place);

    public List<FaxPlace> getAll();
    
    public FaxPlace getById(long id);

    public int getNum();

    public List<FaxPlace> getLatest(int num);
}