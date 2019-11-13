/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barmagli.dao;

import com.barmagli.model.Fax;
import java.util.List;

public interface FaxDao {

    public boolean add(Fax fax);

    public List<Fax> getAll();

    public Fax getById(long id);

    public int getNum();

    public List<Fax> getLatest(int num);

    public Fax getBySendNumber(long sendNumber);

    public Fax getByFaxOutRef(long faxRef);
}
