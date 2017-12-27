/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package format.dna;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import utils.FStringUtils;
import utils.IOFileFormat;
import utils.IOUtils;

/**
 * Providing functions of sorting, searching and collecting statistics
 * Representing full IUPAC codes. (https://www.bioinformatics.org/sms/iupac.html).
 * @author feilu
 */
public abstract class FastaAbstract implements FastaInterface {
    FastaRecordInterface[] records = null;
    protected enum sortType {byName, byID, byLengthAscending, byLengthDescending}
    sortType sType = null;
    
    @Override
    public void writeFasta (String outfileS, boolean[] ifOut, IOFileFormat format) {
        int cnt = 0;
        try {
            BufferedWriter bw = null;
            if (format == IOFileFormat.Text) {
                bw = IOUtils.getTextWriter(outfileS);
            }
            else if (format == IOFileFormat.TextGzip) {
                bw = IOUtils.getTextGzipWriter(outfileS);
            }
            else {
                throw new UnsupportedOperationException("Invalid operation for output");
            }
            for (int i = 0; i < records.length; i++) {
                if (!ifOut[i]) continue;
                bw.write(">"+records[i].getName());
                bw.newLine();
                bw.write(FStringUtils.getMultiplelineString(60, records[i].getSequence()));
                bw.newLine();
                cnt++;
            }
            bw.flush();
            bw.close();
            System.out.println(cnt+ " sequences are written in " + outfileS);
        }
        catch (Exception e) {
            System.out.println("Error while writing "+ outfileS);
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    @Override
    public void writeFasta (String outfileS, int index, IOFileFormat format) {
        try {
            BufferedWriter bw = null;
            if (format == IOFileFormat.Text) {
                bw = IOUtils.getTextWriter(outfileS);
            }
            else if (format == IOFileFormat.TextGzip) {
                bw = IOUtils.getTextGzipWriter(outfileS);
            }
            else {
                throw new UnsupportedOperationException("Invalid operation for output");
            }
            bw.write(">"+records[index].getName());
            bw.newLine();
            bw.write(FStringUtils.getMultiplelineString(60, records[index].getSequence()));
            bw.newLine();
            bw.flush();
            bw.close();
            System.out.println("No." + index +" sequence is written in " + outfileS);
        }
        catch (Exception e) {
            System.out.println("Error while writing "+ outfileS);
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    @Override
    public void writeFasta (String outfileS, IOFileFormat format) {
        try {
            BufferedWriter bw = null;
            if (format == IOFileFormat.Text) {
                bw = IOUtils.getTextWriter(outfileS);
            }
            else if (format == IOFileFormat.TextGzip) {
                bw = IOUtils.getTextGzipWriter(outfileS);
            }
            else {
                throw new UnsupportedOperationException("Invalid operation for output");
            }
            for (int i = 0; i < records.length; i++) {
                bw.write(">"+records[i].getName());
                bw.newLine();
                bw.write(FStringUtils.getMultiplelineString(60, records[i].getSequence()));
                bw.newLine();
            }
            bw.flush();
            bw.close();
            System.out.println(records.length+ " sequences are written in " + outfileS);
        }
        catch (Exception e) {
            System.out.println("Error while writing "+ outfileS);
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Write fast file by chromosome, designed for large genome files
     * @param outfileDirS
     * @param format
     * @param prefix
     * @param postfix 
     */
    public void writeFastaByChr (String outfileDirS, IOFileFormat format, String prefix, String postfix) {
        List<FastaRecordInterface> rList = Arrays.asList(records);
        rList.parallelStream().forEach(r -> {
            try {
                String outfileS = new File (outfileDirS, (prefix+r.getName()+postfix)).getAbsolutePath();
                BufferedWriter bw = null;
                if (format == IOFileFormat.Text) {
                    bw = IOUtils.getTextWriter(outfileS);
                }
                else if (format == IOFileFormat.TextGzip) {
                    bw = IOUtils.getTextGzipWriter(outfileS);
                }
                else {
                    throw new UnsupportedOperationException("Invalid operation for output");
                }
                bw.write(">"+r.getName());
                bw.newLine();
                bw.write(FStringUtils.getMultiplelineString(60, r.getSequence()));
                bw.newLine();
                bw.flush();
                bw.close();
                System.out.println(r.getName() +" sequence is written in " + outfileS);
            }
            catch (Exception e) {
                System.out.println("Error while writing "+ outfileDirS);
                e.printStackTrace();
                System.exit(1);
            }
        });
        
    }
    
    @Override
    public int getN50 () {
        if (sType != sortType.byLengthDescending) this.sortByLengthDescending();
        long sum = this.getTotalSeqLength();
        long halfSum = sum/2;
        int current = 0;
        for (int i = 0; i < this.getSeqNumber(); i++) {
            current+=this.getSeqLength(i);
            if (current > halfSum) return i+1;
        }
        return -1;
    }
    
    @Override
    public int getL50 () {
        if (sType != sortType.byLengthDescending) this.sortByLengthDescending();
        long sum = this.getTotalSeqLength();
        long halfSum = sum/2;
        int current = 0;
        for (int i = 0; i < this.getSeqNumber(); i++) {
            current+=this.getSeqLength(i);
            if (current > halfSum) return this.getSeqLength(i);
        }
        return -1;
    }
    
    @Override
    public long getTotalSeqLength () {
        long sum = 0;
        for (int i = 0; i < this.getSeqNumber(); i++) {
            sum+=this.getSeqLength(i);
        }
        return sum;
    }
    
    @Override
    public int getSeqNumber () {
        return records.length;
    }
    
    @Override
    public int getSeqLength (int index) {
        return records[index].getSequenceLength();
    }
    
    @Override
    public String[] getNames () {
        String[] names = new String[this.getSeqNumber()];
        for (int i = 0; i < names.length; i++) names[i] = this.getName(i);
        return names;
    }
    
    @Override
    public String getName (int index) {
        return records[index].getName();
    }
    
    @Override
    public String getSeq (int index) {
        return records[index].getSequence();
    }
    
    @Override
    public String getSeq (int index, int startIndex, int endIndex) {
        return records[index].getSequence(startIndex, endIndex);
    }
    
    @Override
    public void setName (String newName, int index) {
        records[index].setName(newName);
    }
    
    @Override
    public void sortByName () {
        Arrays.parallelSort(records, new sortByName());
    }
    

    @Override
    public void sortByID () {
        Arrays.parallelSort (records, new sortByID());
    }
    
    @Override
    public void sortByLengthAscending () {
        Arrays.parallelSort (records, new sortByLengthAscending());
    }
    
    @Override
    public void sortByLengthDescending () {
        Arrays.parallelSort (records, new sortByLengthDescending());
    }
    
    @Override
    public boolean isThereN () {
        boolean value;
        for (int i = 0; i < records.length; i++) {
             value = records[i].isThereN();
             if (value) return true;
        }
        return false;
    }
    
    protected interface FastaRecordInterface {
        public String getName ();
        public int getID ();
        public int getSequenceLength ();
        public String getSequence ();
        public String getSequence (int startIndex, int endIndex);
        public void setName (String newName);
        public void setID (int id);
        public boolean isThereN();
        public boolean isThereNonACGTNBase();
    }
    
    
    protected class sortByID implements Comparator <FastaRecordInterface> {
        @Override
        public int compare(FastaRecordInterface o1, FastaRecordInterface o2) {
            return o1.getID() - o2.getID();
        }
    }
    
    protected class sortByName implements Comparator <FastaRecordInterface> {
        @Override
        public int compare (FastaRecordInterface o1, FastaRecordInterface o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
    
    protected class sortByLengthAscending implements Comparator <FastaRecordInterface> {
        @Override
        public int compare (FastaRecordInterface o1, FastaRecordInterface o2) {
            return o1.getSequenceLength() - o2.getSequenceLength();
        }
    }
    
    protected class sortByLengthDescending implements Comparator <FastaRecordInterface> {
        @Override
        public int compare (FastaRecordInterface o1, FastaRecordInterface o2) {
            return o2.getSequenceLength()-o1.getSequenceLength();
        }
    }
}
