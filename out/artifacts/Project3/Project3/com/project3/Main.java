package com.project3;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        double confidence=0;
        double support=0;
        if(args.length<2)
        {
            System.out.print("The program requires confidence and support parameters as input");
        }
        else
        {
            confidence = Double.parseDouble(args[0]);
            support=Double.parseDouble(args[1]);
        }


        Scanner scanner = null;
        Data data = new Data("serviceRequest.csv");
        int noOfRows = data.row_item_set.size();
        Iterator itr = data.freq_c1.entrySet().iterator();

        HashMap<TreeSet<String>,Double>  prevL = new HashMap<TreeSet<String>, Double>();
        HashMap<TreeSet<String>,Double>  mainItemsets = new HashMap<TreeSet<String>, Double>();
        while(itr.hasNext())
        {
            Map.Entry pair = (Map.Entry) itr.next();
            double sup=Double.parseDouble(pair.getValue().toString())/noOfRows;
            if(sup>support) {
                TreeSet<String> temp = new TreeSet<String>();
                temp.add(pair.getKey().toString());
                prevL.put(temp, sup);
                mainItemsets.put(temp,sup);
            }
        }
        int k=2;
        while(true)
        {
            HashSet<TreeSet<String>> newC =joinItemsets(prevL,k);
            HashMap<TreeSet<String>,Double> newL = checkMinSup(newC,support,data.row_item_set);
            if(newL.size()==0)
                break;
            mainItemsets.putAll(newL);
            prevL=newL;
            k++;
        }
        HashMap<Rule,Double> rules=calculateConfidence(confidence,mainItemsets);

        printFrequentItemsets(mainItemsets);
        printAssociationRules(rules);

    }

    static void printAssociationRules(HashMap<Rule, Double> rules) {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Association Rules:");

        Iterator itr = rules.entrySet().iterator();
        while(itr.hasNext())
        {
            Map.Entry pair = (Map.Entry) itr.next();
            Rule r = (Rule) pair.getKey();
            Iterator leftItr = r.left.iterator();
            Iterator rightItr = r.right.iterator();
            while(leftItr.hasNext())
            {
                System.out.print(leftItr.next()+ "  ");
            }
            System.out.print("  =>  ");
            while(rightItr.hasNext())
            {
                System.out.print(rightItr.next()+ "  ");
            }
            System.out.println(":  "+pair.getValue());
        }
    }

    static void printFrequentItemsets(HashMap<TreeSet<String>, Double> mainItemsets) {
        System.out.println("Frequent Itemsets:");
        Iterator itr = mainItemsets.entrySet().iterator();
        while(itr.hasNext())
        {
            Map.Entry pair = (Map.Entry) itr.next();
            TreeSet<String> itemset = (TreeSet<String>) pair.getKey();
            Iterator itrItemset = itemset.iterator();
            while(itrItemset.hasNext())
            {
                System.out.print(itrItemset.next()+ "  ");
            }
            System.out.println(":  " +pair.getValue());
        }
    }

    static HashMap<Rule,Double> calculateConfidence(double confidence, HashMap<TreeSet<String>, Double> mainItemsets) {
        Iterator itr = mainItemsets.entrySet().iterator();
        HashMap<Rule,Double> rules = new HashMap<Rule, Double>();
        while(itr.hasNext()) {
            Map.Entry pair = (Map.Entry) itr.next();
            TreeSet set=(TreeSet)pair.getKey();
            List<String> words = new ArrayList<String>(set);

            int length = words.size();
            int pow_set_size = (int)Math.pow(2.0, length);
            int i,j;
            for(i=1;i<pow_set_size-1;i++)
            {
                Rule r = new Rule();
                for(j=0;j<length;j++)
                {
                    int counter = 1 << j;
                    int a = counter &i;
                    if(a == 0)
                    {
                        r.left.add(words.get(j));
                    }
                    else
                        r.right.add(words.get(j));
                }
                double suppLeft=mainItemsets.get(r.left);
                double suppSet=mainItemsets.get(set);
                double conf= suppSet/suppLeft;
                if(conf>confidence)
                    rules.put(r, conf);
            }

    }
        return rules;
    }

    static HashMap<TreeSet<String>,Double> checkMinSup(HashSet<TreeSet<String>> newC, double support, List<TreeSet<String>> row_item_set) {
        HashMap<TreeSet<String>,Double> newL = new HashMap<TreeSet<String>, Double>();
        Iterator newCitr = newC.iterator();
        int noOfRows = row_item_set.size();
        while(newCitr.hasNext())
        {
            TreeSet<String> newCset = (TreeSet)newCitr.next();
            int count=0;
            for(int i=0;i<noOfRows;i++)
            {
                TreeSet intersection = new TreeSet(row_item_set.get(i));
                intersection.retainAll(newCset);
                if(intersection.equals(newCset))
                    count++;
            }
            double sup = ((double)count)/((double)noOfRows);
            if(sup>support)
                newL.put(newCset,sup);
        }
        return newL;
    }

    static HashSet<TreeSet<String>> joinItemsets(HashMap<TreeSet<String>,Double> prevL,int k)
    {
        int length = k-2;
        HashSet<TreeSet<String>> newC = new HashSet<TreeSet<String>>();
        Iterator prevLItr1 = prevL.entrySet().iterator();
        while(prevLItr1.hasNext()) {
            Iterator prevLItr2 = prevL.entrySet().iterator();
            Map.Entry pair1 = (Map.Entry) prevLItr1.next();
            TreeSet s1 = (TreeSet) pair1.getKey();
            while (prevLItr2.hasNext()) {
                Map.Entry pair2 = (Map.Entry) prevLItr2.next();
                TreeSet s2 = (TreeSet)pair2.getKey();
                TreeSet intersection = new TreeSet(s1);
                TreeSet s2Copy = new TreeSet(s2);
               intersection.retainAll(s2);
                if(intersection.size()==length) {
                    s2Copy.addAll(s1);
                    newC.add(s2Copy);
                }
            }
        }
        return newC;
    }
}
