/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.pipeline.libgbs;

import cern.colt.GenericSorting;
import cern.colt.Swapper;
import cern.colt.function.IntComparator;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.list.array.TIntArrayList;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import utils.IOUtils;

/**
 *
 * @author feilu
 */
public class TagCount implements Swapper, IntComparator {
    protected int tagLengthInLong = -1;
    protected int groupIndex = -1;
    protected List<long[]> tagList = null;
    protected TByteArrayList r1LenList = null;
    protected TByteArrayList r2LenList = null;
    protected TIntArrayList readCountList = null;

    public TagCount (int tagLengthInLong, int groupIndex) {
        this.tagLengthInLong = tagLengthInLong;
        this.groupIndex = groupIndex;
        tagList = new ArrayList();
        r1LenList = new TByteArrayList();
        r2LenList = new TByteArrayList();
        readCountList = new TIntArrayList();
    }
    
    public TagCount (int tagLengthInLong, int groupIndex, int tagNumber, boolean ifSorted) {
        this.tagLengthInLong = tagLengthInLong;
        this.groupIndex = groupIndex;
        tagList = new ArrayList(tagNumber);
        r1LenList = new TByteArrayList(tagNumber);
        r2LenList = new TByteArrayList(tagNumber);
        readCountList = new TIntArrayList(tagNumber);
    }
    
    public void appendTag (long[] tag, byte r1Len, byte r2Len, int readNumber) {
        tagList.add(tag);
        r1LenList.add(r1Len);
        r2LenList.add(r2Len);
        readCountList.add(readNumber);
    }
    
    public long[] getTag (int tagIndex) {
        return this.tagList.get(tagIndex);
    }
    
    public byte getR1TagLength (int tagIndex) {
        return this.r1LenList.get(tagIndex);
    }
    
    public byte getR2TagLength (int tagIndex) {
        return this.r2LenList.get(tagIndex);
    }
    
    public int getTagLengthInLong () {
        return this.tagLengthInLong;
    }
    
    public int getTotalReadNum () {
        int cnt = 0;
        for (int i = 0; i < this.getTagNumber(); i++) {
            cnt+=this.getReadNumber(i);
        }
        return cnt;
    }
    
    public int getReadNumber (int tagIndex) {
        return this.readCountList.get(tagIndex);
    }
    
    public int getTagNumber () {
        return this.tagList.size();
    }
    
    public int getTagIndex (long[] tag) {
        return Collections.binarySearch(tagList, tag, TagUtils.tagCom);
    }
    
    @Override
    public void swap (int index1, int index2) {
        long[] temp = tagList.get(index1);
        tagList.set(index1, tagList.get(index2));
        tagList.set(index2, temp);
        byte tl = r1LenList.get(index1);
        r1LenList.set(index1, r1LenList.get(index2));
        r1LenList.set(index2, tl);
        tl = r2LenList.get(index1);
        r2LenList.set(index1, r2LenList.get(index2));
        r2LenList.set(index2, tl);
        int tc = readCountList.get(index1);
        readCountList.set(index1, readCountList.get(index2));
        readCountList.set(index2, tc);
    }

    @Override
    public int compare (int index1, int index2) {
        for (int i = 0; i < this.getTagLengthInLong()*2; i++) {
            if (tagList.get(index1)[i] < tagList.get(index2)[i]) {
                return -1;
            }
            if (tagList.get(index1)[i] > tagList.get(index2)[i]) {
                return 1;
            }
        }
        return 0;
    }

    public void sort () {
        //System.out.println("TagCount sort begins");
        GenericSorting.quickSort(0, this.getTagNumber(), this, this);
        //System.out.println("TagCount sort ends");
    }
    
    protected int collapseCounts () {
        int collapsedRows = 0;
        for (int i = 0; i < this.getTagNumber()-1; i++) {
            if (this.readCountList.get(i) == 0) continue;
            for (int j = i + 1; j < this.getTagNumber(); j++) {
                int index = this.compare(i, j);
                if (index < 0) break;
                else {
                    int sum = readCountList.get(i)+readCountList.get(j);
                    readCountList.set(i, sum);
                    collapsedRows++;
                    readCountList.set(j, 0);
                }
            }
        }
        for (int i = 0; i < this.getTagNumber(); i++) {
            if (readCountList.get(i) != 0) continue;
            tagList.remove(i);
            r1LenList.removeAt(i);
            r2LenList.removeAt(i);
            readCountList.removeAt(i);
            i--;
        }
        return collapsedRows;
    }
}
