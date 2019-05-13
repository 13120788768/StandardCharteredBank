package com.standard.chartered.bank.dispatcher;

/**
 * @Author: wayyer
 * @Description: the context of instrument type
 * @Program: HelloWorld
 * @Date: 2019.05.13
 */
public class InstrumentContext {
    private Instrument instrument;


    public InstrumentContext(Instrument instrument){
        this.instrument = instrument;
    }

    public Object publish(){
        return instrument.publish();
    }


    public Object importData(){
        return instrument.importData();
    }

}
