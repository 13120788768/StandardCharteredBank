package com.standard.chartered.bank.dispatcher;

import java.util.List;

/**
 * @Author: wayyer
 * @Description: LME instrument
 * @Program: HelloWorld
 * @Date: 2019.05.13
 */
public class LMEInstrument implements Instrument {


    @Override
    public Object importData(Object list) {
        System.out.println("LMEInstrument - importData" + list);
        return list;
    }

    @Override
    public Object publish() {
        System.out.println("LMEInstrument - publish");
        return null;
    }
}
