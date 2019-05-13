package com.standard.chartered.bank.dispatcher;

/**
 * @Author: wayyer
 * @Description: LME instrument
 * @Program: HelloWorld
 * @Date: 2019.05.13
 */
public class LMEInstrument implements Instrument {


    @Override
    public Object importData() {
        System.out.println("LMEInstrument - importData");
        return null;
    }

    @Override
    public Object publish() {
        System.out.println("LMEInstrument - publish");
        return null;
    }
}
