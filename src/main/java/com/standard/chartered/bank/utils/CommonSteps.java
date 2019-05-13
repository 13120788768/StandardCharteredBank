package com.standard.chartered.bank.utils;

import com.standard.chartered.bank.constant.InstrumentType;
import com.standard.chartered.bank.dispatcher.Instrument;
import com.standard.chartered.bank.dispatcher.InstrumentContext;
import com.standard.chartered.bank.dispatcher.LMEInstrument;
import com.standard.chartered.bank.dispatcher.PRIMEInstrument;
import com.standard.chartered.bank.handler.Wrapper;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
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
public class CommonSteps {

    private InstrumentContext context ;
    Wrapper wrapper;

    private ConcurrentHashMap<String, List<List<String>>> importDataMap = new ConcurrentHashMap<>();


    private Instrument instrument;

    public static void main(String[] args) throws Exception {
        Wrapper wrapper = new Wrapper(new LMEInstrument(), "importData", "LME");
        wrapper.doImportService("LME", new ArrayList(){{add("1");}});
    }

    @Given("^The \"([^\"]*)\" instrument \"([^\"]*)\" with details$")
    public void provideDatawithIns(String instrumentType, String tableName, List<List<String>> table) {
        try{
            generateInstruments(instrumentType);
            wrapper = new Wrapper(instrument, "importData", instrumentType);
            Object importServices = wrapper.doImportService(instrumentType, table);
            importDataMap.putIfAbsent(instrumentType+"-"+tableName, (List<List<String>>)importServices);
        } catch (Exception e){
            e.getStackTrace();
        }
    }

    @When("^\"([^\"]*)\" publishes instrument \"([^\"]*)\"$")
    public void publishDatawithIns(String instrument, String tableName) {

    }

    @Then("^Then the application publishes the following instrument internally$")
    public void loadAndCompare(List<List<String>> table) {

    }

    @Then("^A file is found on sink application with name \"([^\"]*)\"$")
    public void thenFileFoundOnSink(String fileName) {

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

    private void checkFileData(List<List<String>> testDataList, File file) {
        if (testDataList.size() < 2 && (file == null || !file.isFile() || !file.exists())) {
            return;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            //updated to remove call to readline() before entering while loop - we were skipping the first line
            List<String> targetList = new ArrayList<>();
            bufferedReader.lines().distinct().forEach(targetList::add);
            if (testDataList.size() < 2 && targetList.size() < 2) {
                return;
            }
            Assert.assertTrue("scenario or tsv is empty.",
                    testDataList.size() > 1 && targetList.size() > 1);
            List<Map<String, String>> scenarioData = putMap(testDataList);
            List<List<String>> tsvLists = new ArrayList<>();
            for (String line : targetList) {
                List<String> strings = Arrays.asList(line.split("\t", -1));
                tsvLists.add(strings);
            }
            List<Map<String, String>> tsvData = putMap(tsvLists);
            Set<String> scenarioHeadSet = scenarioData.get(0).keySet();
            List<String> disjunction = new ArrayList<>(disjunction(scenarioHeadSet, tsvData.get(0).keySet()));
            Assert.assertTrue("Found different field!" + disjunction.toString(), disjunction.isEmpty());
            Assert.assertEquals("The number of data is different. scenarioData : " + scenarioData.size() + " tsvData: " + tsvData.size(),
                    scenarioData.size(), tsvData.size());
            scenarioData.forEach(scenarioLine -> {
                Assert.assertTrue("scenarioLine: " + scenarioLine,
                        tsvData.stream().anyMatch(tsvLine -> scenarioHeadSet.stream().allMatch(
                                s -> tsvLine.get(s).trim().equalsIgnoreCase(scenarioLine.get(s)))));
            });

            tsvData.forEach(tsvLine -> {
                Assert.assertTrue("tsvLine: " + tsvLine,
                        scenarioData.stream().anyMatch(scenarioLine -> scenarioHeadSet.stream().allMatch(
                                s -> tsvLine.get(s).trim().equalsIgnoreCase(scenarioLine.get(s)))));
            });
            System.out.println("tsv file compare completed! ");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue("Not found file or data",
                    testDataList.size() < 2);
        }
    }

    public static Collection disjunction(Collection a, Collection b) {
        ArrayList list = new ArrayList();
        Map mapa = getCardinalityMap(a);
        Map mapb = getCardinalityMap(b);
        Set elts = new HashSet(a);
        elts.addAll(b);
        Iterator it = elts.iterator();

        while(it.hasNext()) {
            Object obj = it.next();
            int i = 0;

            for(int m = Math.max(getFreq(obj, mapa), getFreq(obj, mapb)) - Math.min(getFreq(obj, mapa), getFreq(obj, mapb)); i < m; ++i) {
                list.add(obj);
            }
        }

        return list;
    }

    private static final int getFreq(Object obj, Map freqMap) {
        Integer count = (Integer)freqMap.get(obj);
        return count != null ? count : 0;
    }

    public static Map getCardinalityMap(Collection coll) {
        Map count = new HashMap();
        Iterator it = coll.iterator();

        while(it.hasNext()) {
            Object obj = it.next();
            Integer c = (Integer)((Integer)count.get(obj));
            if (c == null) {
                count.put(obj, 1);
            } else {
                count.put(obj, new Integer(c + 1));
            }
        }

        return count;
    }

    private static List<Map<String, String>> putMap(List<List<String>> target) {
        Assert.assertTrue(target.size() > 1);
        target = new ArrayList<>(target);
        List<String> header = target.remove(0);
        Assert.assertFalse(header.isEmpty());
        Map<String, Integer> convert = new HashMap<>();
        Set<Integer> length = new HashSet<>();
        length.add(header.size());
        for (int i = 0; i < header.size(); i++) {
            String headerString = header.get(i);
        }
        List<Map<String, String>> result = new ArrayList<>();
        target.forEach(strings -> {
            Assert.assertTrue("record size is too short. " + strings,
                    strings.size() >= header.size());
            Map<String, String> map = new HashMap<>();
            convert.forEach((s, i) -> map.put(s, strings.get(i)));
            result.add(map);
            length.add(strings.size());
        });
        Assert.assertEquals(1, length.size());
        return result;
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
