package com.mcx.iris.dao;


import com.mcx.iris.dictionary.refdata.InstrumentModel;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Ext-MayukhG on 12-03-2018.
 */
public interface InstrumentDAO {

    public void setDataSource(DataSource ds);

    public List<InstrumentModel> getAllInstrument();

}
