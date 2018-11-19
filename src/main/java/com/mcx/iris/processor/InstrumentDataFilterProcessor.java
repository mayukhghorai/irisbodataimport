package com.mcx.iris.processor;

import com.mcx.iris.dictionary.refdata.InstrumentModel;
import com.mcx.iris.template.ScripMasterJDBCTemplate;
import com.mcx.iris.util.AppConstant;
import com.mcx.iris.util.DateUtil;
import com.mcx.iris.util.DynamicPropertiesFileReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * Created by Ext-MayukhG on 22-03-2018.
 */
public class InstrumentDataFilterProcessor implements ItemProcessor<InstrumentModel, InstrumentModel>, ApplicationContextAware {
    protected static final Log logger = LogFactory.getLog(InstrumentDataFilterProcessor.class);
    private static ApplicationContext context = null;
    DynamicPropertiesFileReader fileReader;

    @Override
    public InstrumentModel process(InstrumentModel item) throws Exception {
        fileReader = (DynamicPropertiesFileReader)context.getBean("DynamicPropertiesFileReader");
        fileReader.start();
//        System.out.println("Inside InstrumentDataFilterProcessor");
        StringBuffer sb = new StringBuffer(item.getSymbol());
        sb.append(" ").append(item.getExpiryDate());
        if(item.getInstrumentName().equalsIgnoreCase("OPTFUT"))
        {
            sb.append("/").append(item.getStrikePrice()).append("/").append(item.getOptionType());
        }
        item.setContract(sb.toString());
        if(item.getExpiryDate() != null ) {
            item.setExpiryDateInDate(DateUtil.convertStringToDate(item.getExpiryDate()));
        }
        item.setDeliveryLot(1);
        if(item.getTradeGroupID() != null && !item.getTradeGroupID().equals(null)) {
            if(item.getTradeGroupID() == 1) {
                item.setTradeClosingTime(getValue(AppConstant.INSTRUMENT_HALT_TIME1));
            }
            else if (item.getTradeGroupID() == 3) {
                item.setTradeClosingTime(getValue(AppConstant.INSTRUMENT_HALT_TIME2));
            }
            else {
                item.setTradeClosingTime(getValue(AppConstant.INSTRUMENT_HALT_TIME3));
            }
        }
        InstrumentModel im = loadBean(item.getUniqueIdentifier());
        if(im != null) {
            item.setIssueStartDate(im.getIssueStartDate());
            item.setIssueMaturityDate(im.getIssueMaturityDate());
        }


//        System.out.println("End of InstrumentDataFilterProcessor");
        return item;
    }

    private static InstrumentModel loadBean(Integer token) {
        InstrumentModel im = new InstrumentModel();
        ScripMasterJDBCTemplate scripMasterJDBCTemplate1 = (ScripMasterJDBCTemplate) context.getBean("scripMasterJDBCTemplate");
        List<InstrumentModel> ims = scripMasterJDBCTemplate1.getScripMaster(token);
//        logger.info("instrument ="+ims.size() + " token=" +token);
        if(ims !=null && ims.size()>0)
            im = ims.get(0);
        return im;
    }

    public String getValue(String constant) {
        return fileReader.getDynamicPropertiesMap().get(constant);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
