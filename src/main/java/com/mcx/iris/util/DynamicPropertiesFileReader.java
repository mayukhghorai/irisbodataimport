package com.mcx.iris.util;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Ext-MayukhG on 6/4/2018.
 */
public class DynamicPropertiesFileReader implements BeanFactoryAware {

    private static Logger logger = Logger.getLogger(DynamicPropertiesFileReader.class);
    private Properties coreDynamicPropertiesBean;
    public static HashMap<String, String> dynamicPropertiesMap;
    private BeanFactory beanFactory;

    /**

     * Starts reading the dynamic properties
     */
    public void start() {

        setCoreDynamicPropertiesBean(createCoreDynamicPropertiesBeanInstance());
//        logger.info("**** Dynamic Properties File Reader Task is being started... ****");
        readConfiguration();
//        logger.info("**** Dynamic Properties File Reader Task is stopped... ****");
    }

    /**
     * Reads all the dynamic properties
     */
    private void readConfiguration() {
        readMessageContent();
    }

    /**
     * Reads Message_Content dynamic property
     */
    private void readMessageContent() {

        getDynamicPropertiesMap().put(AppConstant.BHAVCOPY_SOURCE, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.BHAVCOPY_SOURCE,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.BHAVCOPY_DEST, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.BHAVCOPY_DEST,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.PRODUCT_MASTER_SOURCE, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.PRODUCT_MASTER_SOURCE,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.PRODUCT_MASTER_DEST, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.PRODUCT_MASTER_DEST,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.INSTRUMENT_HALT_TIME1, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.INSTRUMENT_HALT_TIME1,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.INSTRUMENT_HALT_TIME2, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.INSTRUMENT_HALT_TIME2,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.INSTRUMENT_HALT_TIME3, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.INSTRUMENT_HALT_TIME3,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.FTPS_HOST_NAME, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.FTPS_HOST_NAME,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.FTPS_HOST_PORT, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.FTPS_HOST_PORT,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.PROTOCOL, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.PROTOCOL,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.FTPS_SERVER_USER, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.FTPS_SERVER_USER,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.FTPS_SERVER_PASS, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.FTPS_SERVER_PASS,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.FTPS_SERVER_TIMEOUT, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.FTPS_SERVER_TIMEOUT,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.BHAVCOPY, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.BHAVCOPY,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.INSTRUMENT, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.INSTRUMENT,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.SCHEDULED_TIME, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.SCHEDULED_TIME,
                        AppConstant.DEFAULT_VALUE));
        getDynamicPropertiesMap().put(AppConstant.ZONE, getCoreDynamicPropertiesBean()
                .getProperty(AppConstant.ZONE,
                        AppConstant.DEFAULT_VALUE));

    }

    /**
     * Gets CoreDynamicPropertiesBean
     *
     * @return Properties coreDynamicPropertiesBean
     */
    public Properties getCoreDynamicPropertiesBean() {
        return coreDynamicPropertiesBean;
    }


    public void setCoreDynamicPropertiesBean(Properties coreDynamicPropertiesBean) {
        this.coreDynamicPropertiesBean = coreDynamicPropertiesBean;
    }

    /**
     * Gets DynamicPropertiesMap
     *
     * @return HashMap dynamicPropertiesMap
     */
    public HashMap<String, String> getDynamicPropertiesMap() {
        return dynamicPropertiesMap;
    }


    public void setDynamicPropertiesMap(HashMap<String, String> dynamicPropertiesMap) {
        this.dynamicPropertiesMap = dynamicPropertiesMap;
    }

    /**
     * Gets a new instance of CoreDynamicPropertiesBean
     *
     * @return Properties CoreDynamicPropertiesBean
     */
    public Properties createCoreDynamicPropertiesBeanInstance() {
        return (Properties) this.beanFactory.getBean(AppConstant.BEAN_NAME_CORE_DYNAMIC_PROPERTIES_BEAN);
    }


    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


}
