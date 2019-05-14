package com.standard.chartered.bank;

import com.standard.chartered.bank.constant.InstrumentType;
import com.standard.chartered.bank.dispatcher.Instrument;
import com.standard.chartered.bank.dispatcher.InstrumentContext;
import com.standard.chartered.bank.dispatcher.LMEInstrument;
import com.standard.chartered.bank.dispatcher.PRIMEInstrument;
import com.standard.chartered.bank.handler.Wrapper;
import com.standard.chartered.bank.utils.FileUtils;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wayyer
 * @Description: test steps
 * @Program: HelloWorld
 * @Date: 2019.05.13
 */
public class CucumberSteps {

    private InstrumentContext context ;
    Wrapper wrapper;

    private ConcurrentHashMap<String, List<List<String>>> cacheImportDataMap = new ConcurrentHashMap<>();

    private Instrument instrument;

    @Given("^The \"([^\"]*)\" instrument \"([^\"]*)\" with details$")
    public void provideDatawithIns(String instrumentType, String tableName, List<List<String>> table) {
        try{
            generateInstruments(instrumentType);
            wrapper = new Wrapper(instrument, "importData", instrumentType);
            Object importServices = wrapper.doImportService(instrumentType, table);
            cacheImportDataMap.putIfAbsent(instrumentType+"-"+tableName, (List<List<String>>)importServices);
        } catch (Exception e){
            e.getStackTrace();
        }
    }

    @When("^\"([^\"]*)\" publishes instrument \"([^\"]*)\"$")
    public void publishDatawithIns(String instrumentType, String tableName) throws Exception{
        List<List<String>> tableList = cacheImportDataMap.get(instrumentType+"-"+tableName);
        if(tableList == null){
            tableList = cacheImportDataMap.get(instrumentType+"-PB_03_2018");
        }

        Map<String, List<List<String>>> cachePublishDataMap = new HashMap<>();
        cachePublishDataMap.putIfAbsent(tableName, tableList);
        generateInstruments(instrumentType);
        wrapper = new Wrapper(instrument, "publish", instrumentType);
        wrapper.doPublishService(instrumentType, cachePublishDataMap);
    }

    /**
     * asynchronzied to do the write
     * @param table
     */
    @Then("^Then the application publishes the following instrument internally with \"([^\"]*)\"$")
    public void applicationCompare(String name, List<List<String>> table) {
        FileUtils.checkFileData(table, new File(InstrumentType.OUTPUT_PATH+name+".tsv"));

    }

    @And("^A \"([^\"]*)\" instrument \"([^\"]*)\" with these details$")
    public void provideDatawithPri(String instrumentType, String tableName, List<List<String>> table) {
        try{
            generateInstruments(instrumentType);
            wrapper = new Wrapper(instrument, "importData", instrumentType);
            Object importServices = wrapper.doImportService(instrumentType, table);
            cacheImportDataMap.putIfAbsent(instrumentType+"-"+tableName, (List<List<String>>)importServices);
        } catch (Exception e){
            e.getStackTrace();
        }
    }

    @And("^wait (\\d+) millisecond$")
    public void waitMilliseconds(long n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @And("^I wait Async Queue complete$")
    public void waitAsync(String fullPath) {

    }

    @And("^I will remove the test file on \"([^\"]*)\"$")
    public void andRemoveFileOnSink(String fileName) {
    }

    @And("^log ([^\"]*)")
    public void andLogTime(String t) {

    }

    private void compareDatas(){

    }



    private void generateInstruments(String instrumentType){
        if(InstrumentType.INSTRUMENT_TYPE_LME.equalsIgnoreCase(instrumentType)){
            instrument = new LMEInstrument();
        }else if(InstrumentType.INSTRUMENT_TYPE_PRIME.equalsIgnoreCase(instrumentType)){
            instrument = new PRIMEInstrument();
        }else{
            //extensible for others
            return;
        }

    }
}
