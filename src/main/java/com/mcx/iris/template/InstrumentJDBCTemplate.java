package com.mcx.iris.template;

import com.mcx.iris.dao.InstrumentDAO;
import com.mcx.iris.dictionary.refdata.InstrumentModel;
import com.mcx.iris.mapper.InstrumentMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Ext-MayukhG on 12-03-2018.
 */
public class InstrumentJDBCTemplate implements InstrumentDAO {
    private DataSource dataSource;
    private SimpleJdbcCall jdbcCall;

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("USP_GETSCRIP");
    }

    @Override
    public List<InstrumentModel> getAllInstrument() {
        JdbcTemplate jdbcTemplateObject = new JdbcTemplate(dataSource);
        String sql = "Select * from Instrument";
        List<InstrumentModel> instrumentModelList = jdbcTemplateObject.query(sql, new InstrumentMapper());
        return instrumentModelList;
    }
}