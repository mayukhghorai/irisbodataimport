package com.mcx.iris.mapper;

import com.mcx.iris.dictionary.refdata.InstrumentModel;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Ext-MayukhG on 8/14/2018.
 */
public class ScripMasterResultSetExtractor implements RowMapper<InstrumentModel> {

    @Override
    public InstrumentModel mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
        InstrumentModel im = new InstrumentModel();
//        Timestamp timestamp1 = rs.getTimestamp("IssueStartDate");
//        if(timestamp1 !=null)
            im.setIssueStartDate(new java.util.Date(rs.getTimestamp("IssueStartDate").getTime()));
//        Timestamp timestamp2 = rs.getTimestamp("IssueMaturityDate");
//        if(timestamp2 !=null)
            im.setIssueMaturityDate(new java.util.Date(rs.getTimestamp("IssueMaturityDate").getTime()));
//        }
        return im;
    }
}