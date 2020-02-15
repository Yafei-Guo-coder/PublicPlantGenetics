/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgl.infra.position;

/**
 *
 * @author feilu
 */
public class ChrPos implements Comparable<ChrPos> {
    short chr;
    int pos;
    
    public ChrPos () {
        
    }
    
    public ChrPos (short chr, int pos) {
        this.chr = chr;
        this.pos = pos;
    }

    public ChrPos getChrPos () {
        return this;
    }
    
    public short getChromosome () {
        return chr;
    }
    
    public int getPosition() {
        return pos;
    }

    public void setChromosome (short chr) {
        this.chr = chr;
    }

    public void setPosition (int position) {
        this.pos = position;
    }

    @Override
    public int compareTo(ChrPos o) {
        if (this.getChromosome() == o.getChromosome()) {
            if (this.getPosition() == o.getPosition()) return 0;
            else if (this.getPosition() < o.getPosition()) return -1;
            return 1;
        }
        else if (this.getChromosome() < o.getChromosome()) return -1;
        return 1;
    }
}
