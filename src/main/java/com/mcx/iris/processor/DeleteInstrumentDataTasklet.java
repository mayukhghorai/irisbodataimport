package com.mcx.iris.processor;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by Ext-MayukhG on 14-03-2018.
 */
public class DeleteInstrumentDataTasklet implements Tasklet {

    private String sql;

    private DataSource dataSource;

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        new JdbcTemplate(this.dataSource).execute(sql);
        return RepeatStatus.FINISHED;

    }
}
