package com.standard.chartered.bank.test;

import com.standard.chartered.bank.dispatcher.LMEInstrument;
import com.standard.chartered.bank.handler.Wrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

/**
 * @Author: wayyer
 * @Description: test the wrapper class
 * @Program: test-enterence-project
 * @Date: 2019.05.14
 */
@RunWith(JUnit4.class)
public class WrapperTest {


    @Test
    public void testWrapper() throws Exception{
        Wrapper wrapper = new Wrapper(new LMEInstrument(), "importData", "LME");
        wrapper.doImportService("LME", new ArrayList(){{add("1");}});
    }


}
