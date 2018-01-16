/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package format.range;

/**
 * Holding basic method of a range
 * @author feilu
 */
public interface RangesInterface {
    
    /**
     * Return a {@link format.range.Range} object
     * @param rangeIndex
     * @return 
     */
    public Range getRange (int rangeIndex);
    
    /**
     * Sort by range size
     */
    public void sortBySize ();
    
    /**
     * Sort by starting position of a range
     */
    public void sortByStartPosition ();
     
    /**
     * Insert a {@link format.range.Range} into the list
     * @param rangeIndex
     * @param r 
     */
    public void insertRange (int rangeIndex, Range r);
    
    /**
     * Remove a {@link format.range.Range} from the list
     * @param rangeIndex 
     */
    public void removeRange (int rangeIndex);
    
    /**
     * Set a {@link format.range.Range} in the list
     * @param rangeIndex
     * @param r 
     */
    public void setRange (int rangeIndex, Range r);
    
    /**
     * Return total number of {@link format.range.Range} in the list
     * @return 
     */
    public int getRangeNumber ();
    
    /**
     * Return the starting index of range on a chromosome. 
     * @param chr
     * @return negative value if the chromosome does not have a range
     */
    public int getStartIndexOfChromosome (int chr);
    
    /**
     * Return the ending index of range on a chromosome. 
     * @param chr
     * @return negative value if the chromosome does not have a range
     */
    public int getEndIndexOfChromosome (int chr);
    
    /**
     * Return all the chromosomes in an array
     * @return 
     */
    public int[] getChromosomes ();
    
    /**
     * Return the total number of chromosomes
     * @return 
     */
    public int getChromosomeNumber ();
    
    /**
     * Return the starting position of a range
     * @param rangeIndex
     * @return 
     */
    public int getRangeStart (int rangeIndex);
    
    /**
     * Return the ending position of a range
     * @param rangeIndex
     * @return 
     */
    public int getRangeEnd (int rangeIndex);
    
    /**
     * Return the chromosome of a range
     * @param rangeIndex
     * @return 
     */
    public int getRangeChromosome (int rangeIndex);
    
    /**
     * Return a {@link format.range.Ranges} from a chromosome
     * @param chr
     * @return null if the chromosome does not have a range
     */
    public Ranges getRangesByChromosome (int chr);
    
    /**
     * Return a {@link format.range.Ranges}, in which a position is contained
     * @param chr
     * @param pos
     * @return 
     */
    public Ranges getRangesContainsPosition (int chr, int pos);
    
    /**
     * Return the indices of Range, where a position is contained
     * @param chr
     * @param pos
     * @return 
     */
    public int[] getRangesIndicesContainsPosition (int chr, int pos);
    
    /**
     * Return the first index of range, in which a position is contained
     * @param chr
     * @param pos
     * @return negative value if the {@link format.range.Ranges} does not contain the position
     */
    public int getFirstRangeIndex (int chr, int pos);
    
    /**
     * Return a {@link format.range.Ranges} with no overlap between individual range
     * @return 
     */
    public Ranges getNonOverlapRanges ();
    
    /**
     * Return a {@link format.range.Ranges} by adding another Ranges object 
     * @param rs
     * @return 
     */
    public Ranges getMergedRanges(Ranges rs);
    
    /**
     * Add another {@link format.range.Range} to the current list
     * @param r 
     */
    public void addRange(Range r);

    /**
     * Add {@link format.range.Ranges} to the current list
     * @param rs 
     */
    public void addRanges(Ranges rs);
    
}
