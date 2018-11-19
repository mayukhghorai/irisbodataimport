package com.mcx.iris.processor;


import com.mcx.iris.dictionary.refdata.BhavCopyModel;
import com.mcx.iris.dictionary.refdata.InstrumentModel;
import com.mcx.iris.template.InstrumentJDBCTemplate;
import com.mcx.iris.util.DateUtil;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * Created by Ext-MayukhG on 12-03-2018.
 */
public class BhavCopyDataFilterProcessor implements ItemProcessor<BhavCopyModel, BhavCopyModel>, ApplicationContextAware {


    private static ApplicationContext context = null;

    private static List<InstrumentModel> instrumentModelList = null;

    private BhavCopyModel myReturnStatement = null;

    private static void loadBean() {
        InstrumentJDBCTemplate instrumentJDBCTemplate = (InstrumentJDBCTemplate) context.getBean("instrumentJDBCTemplate");
        instrumentModelList = instrumentJDBCTemplate.getAllInstrument();
//        i++;
    }

    @Override
    public BhavCopyModel process(BhavCopyModel item) throws Exception {
        loadBean();
        if (instrumentModelList != null && !instrumentModelList.isEmpty()) {
            for (InstrumentModel instrumentModel : instrumentModelList) {
                if(item.getExpiryDate() != null ) {
                    item.setBhavCopyDateInDate(DateUtil.convertStringToDate(item.getBhavCopyDate()));
                }

                myReturnStatement = null;
                if(item.getInstrumentName().equals("OPTFUT")) {
                        if (instrumentModel.getSymbol().equals(item.getSymbol()) && instrumentModel.getExpiryDate().equals(item.getExpiryDate())
                                && instrumentModel.getInstrumentName().equals(item.getInstrumentName()) && item.getStrikePrice().equals(instrumentModel.getStrikePrice())
                                && instrumentModel.getOptionType().equals(item.getOptionType())) {
                            item.setInstrumentIdentifier(instrumentModel.getUniqueIdentifier());
                            myReturnStatement = item;
                            break;
                        }
                }
                else {
                    if(instrumentModel.getSymbol().equals(item.getSymbol()) && instrumentModel.getExpiryDate().equals(item.getExpiryDate())
                            && instrumentModel.getInstrumentName().equals(item.getInstrumentName())){
                        myReturnStatement = item;
                        item.setInstrumentIdentifier(instrumentModel.getUniqueIdentifier());
                        break;
                    }

                }
            }
        }
        if (myReturnStatement != null) {
            return myReturnStatement;
        } else {
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
