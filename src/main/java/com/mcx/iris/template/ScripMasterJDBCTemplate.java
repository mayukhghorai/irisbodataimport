package com.mcx.iris.template;

import com.mcx.iris.dao.ScripMasterDAO;
import com.mcx.iris.dictionary.refdata.InstrumentModel;
import com.mcx.iris.mapper.ScripMasterResultSetExtractor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Ext-MayukhG on 8/14/2018.
 */
public class ScripMasterJDBCTemplate implements ScripMasterDAO {
    private DataSource sqlServerDataSource;
    private SimpleJdbcCall jdbcCall;

    @Override
    public void setDataSource(DataSource ds) {
        this.sqlServerDataSource = ds;
        this.jdbcCall = new SimpleJdbcCall(sqlServerDataSource);
    }

    @Override
    public List<InstrumentModel> getScripMaster(Integer token) {
        JdbcTemplate jdbcTemplateObject = new JdbcTemplate(sqlServerDataSource);
        String sql = "SELECT IssueStartDate,IssueMaturityDate FROM Dim_ScripMaster where Token = "+ token ;
        List<InstrumentModel> scripmaster = jdbcTemplateObject.query(sql, new ScripMasterResultSetExtractor());
        return scripmaster;
    }
}