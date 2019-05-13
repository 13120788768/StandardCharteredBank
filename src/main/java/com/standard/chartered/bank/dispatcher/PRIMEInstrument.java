package com.standard.chartered.bank.dispatcher;

/**
 * @Author: wayyer
 * @Description: LME instrument
 * @Program: HelloWorld
 * @Date: 2019.05.13
 */
public class PRIMEInstrument implements Instrument {

    @Override
    public Object importData() {
        System.out.println("PRIMEInstrument - importData");
        return null;
    }

    @Override
    public Object publish() {
        System.out.println("PRIMEInstrument - importData");
        return null;
    }
}
