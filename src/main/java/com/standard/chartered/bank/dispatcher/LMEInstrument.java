package com.standard.chartered.bank.dispatcher;

import com.standard.chartered.bank.constant.InstrumentType;
import com.standard.chartered.bank.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
    public Object publish(Object list) {
        System.out.println("LMEInstrument - publish" + list);

        FileUtils.writeOriginalToFile(list);

        FileUtils.writePublishedToFile(list);

        return null;
    }
}
