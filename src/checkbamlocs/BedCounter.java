/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkbamlocs;

import file.BedAbstract;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author bickhart
 */
public class BedCounter extends BedAbstract{
    protected AtomicInteger counter;
    
    public BedCounter(String[] segs) throws Exception{
        if(segs.length < 3)
            throw new Exception("Bed column length less than 3 for: " + StrUtils.StrArray.Join(segs, "\t"));
        
        super.initialVals(segs[0], segs[1], segs[2]);
        // Check if this is a "named" bed file
        if(segs.length > 3)
            this.name = segs[3];
        counter = new AtomicInteger(0);
    }
    
    public BedCounter(String chr, int pos){
        this.chr = chr;
        this.start = pos;
        this.end = pos + 1;
        counter = new AtomicInteger(0);
    }
    
    @Override
    public int compareTo(BedAbstract t) {
        return Start() - t.Start();
    }
    
    public void increment(){
        this.counter.getAndIncrement();
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(chr).append("\t").append(String.valueOf(start)).append("\t");
        str.append(String.valueOf(end));
        if(this.name != null){
            str.append("\t").append(name);
        }
        str.append("\t").append(String.valueOf(this.counter.get()));
        str.append("\n");
        return str.toString();
    }
}
