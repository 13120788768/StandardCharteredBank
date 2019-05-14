package com.standard.chartered.bank.utils;

import com.standard.chartered.bank.constant.InstrumentType;
import org.junit.Assert;

import java.io.*;
import java.util.*;

/**
 * @Author: wayyer
 * @Description:
 * @Program: test-enterence-project
 * @Date: 2019.05.14
 */
public class FileUtils {


    public static void writePublishedToFile(Object list){
        String name = "";
        List<List<String>> tableList = null;
        if(list instanceof HashMap){
            for (Object key: ((HashMap) list).keySet()) {
                name = key.toString();
                break;
            }
            for (Object value: ((HashMap) list).values()) {
                tableList = (List<List<String>>)value;
                break;
            }

        }
        writePublish(name, tableList);
    }

    public static void writeOriginalToFile(Object list){
        String name = "";
        List<List<String>> tableList = null;
        if(list instanceof HashMap){
            for (Object key: ((HashMap) list).keySet()) {
                name = key.toString();
                break;
            }
            for (Object value: ((HashMap) list).values()) {
                tableList = (List<List<String>>)value;
                break;
            }

        }
        write(name, tableList);
    }

    private static void write(String name, List<List<String>> tableList){

        String path = InstrumentType.OUTPUT_PATH+name+".tsv";
        File file = new File(path);
        BufferedWriter writer = null;
        try{
            if (!file.isFile()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(file));
            for (List<String> line: tableList){
                for (String l: line) {
                    writer.write(l+"\t");
                }
                writer.write("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(writer != null){
                try{
                    writer.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

    private static void writePublish(String name, List<List<String>> tableList){

        String path = InstrumentType.OUTPUT_PATH+name+"_TRADABLE.tsv";
        File file = new File(path);
        BufferedWriter writer = null;
        try{
            if (!file.isFile()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(file));
            List<String> columnsList = tableList.get(0);
            for (String l: columnsList) {
                writer.write(l+"\t");
            }
            writer.write("TRADABLE\t\n");

            for (int i = 1; i < tableList.size(); i++) {
                for (String l: tableList.get(i)) {
                    writer.write(l+"\t");
                }
                writer.write("TRUE\t");
                writer.write("\n");
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(writer != null){
                try{
                    writer.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }


    public static void checkFileData(List<List<String>> testDataList, File file) {
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

}
