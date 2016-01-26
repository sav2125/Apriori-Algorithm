package com.project3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by shivani on 11/28/2015.
 */
public class Data {
    public List<TreeSet<String>> row_item_set = new ArrayList<TreeSet<String>>();
    public TreeMap<String,Integer> freq_c1 = new TreeMap<String, Integer>();

    public Data(String filePath) {
        BufferedReader br=null;
        InputStream is = this.getClass().getResourceAsStream(filePath);
        InputStreamReader isr = new InputStreamReader(is);

        br = new BufferedReader(isr);
        String line = null;

        try {
            while ((line = br.readLine()) != null) {
                TreeSet<String> singleRow = new TreeSet<String>();
                String[] rowData=line.split(",");
                for(int i=0;i<rowData.length;i++){
                    singleRow.add(rowData[i]);
                    if (freq_c1.containsKey(rowData[i])) {
                        int freq = freq_c1.get(rowData[i]);
                        freq++;
                        freq_c1.put(rowData[i], freq);
                    } else {
                        freq_c1.put(rowData[i], 1);
                    }
                }

                row_item_set.add(singleRow);
            }
        } catch (IOException e) {
            System.out.println("Could not read data file.");
        }

    }
}
