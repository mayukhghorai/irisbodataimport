package com.mcx.iris.mapper;

import com.mcx.iris.dictionary.refdata.InstrumentModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Ext-MayukhG on 12-03-2018.
 */
public class InstrumentMapper implements RowMapper<InstrumentModel> {
    @Override
    public InstrumentModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        InstrumentModel im = new InstrumentModel();
        im.setUniqueIdentifier(rs.getInt("UniqueIdentifier"));
        im.setInstrumentId(rs.getInt("InstrumentId"));
        im.setInstrumentName(rs.getString("InstrumentName"));
        im.setSymbol(rs.getString("Symbol"));
        im.setStrikePrice(rs.getDouble("StrikePrice"));
        im.setOptionType(rs.getString("OptionType"));
        im.setExpiryDate(rs.getString("ExpiryDate"));
        return im;
    }
}
