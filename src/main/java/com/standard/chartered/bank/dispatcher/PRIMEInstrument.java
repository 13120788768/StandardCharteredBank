package com.standard.chartered.bank.dispatcher;

import java.util.List;

/**
 * @Author: wayyer
 * @Description: LME instrument
 * @Program: HelloWorld
 * @Date: 2019.05.13
 */
public class PRIMEInstrument implements Instrument {

    @Override
    public Object importData(Object list) {
        System.out.println("PRIMEInstrument - importData" + list);
        return null;
    }

    @Override
    public Object publish() {
        System.out.println("PRIMEInstrument - importData");
        return null;
    }
}
