package com.standard.chartered.bank.dispatcher;

import com.standard.chartered.bank.utils.FileUtils;

import java.util.List;

/**
 * @Author: wayyer
 * @Description: PRIME instrument
 * @Program: HelloWorld
 * @Date: 2019.05.13
 */
public class PRIMEInstrument implements Instrument {

    @Override
    public Object importData(Object list) {
        System.out.println("PRIMEInstrument - importData" + list);
        return list;
    }

    @Override
    public Object publish(Object list) {
        System.out.println("PRIMEInstrument - publish" + list);


        /**
         * the specific logic of compare the | EXCHANGE_CODE | TRADABLE|
         * leave them
         */
        FileUtils.writeOriginalToFile(list);

        FileUtils.writePublishedToFile(list);
        return null;
    }
}
