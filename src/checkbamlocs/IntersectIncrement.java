/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkbamlocs;

import file.BedAbstract;
import file.BedMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import utils.BinBed;
import utils.LineIntersect;
import static utils.LineIntersect.ovCount;

/**
 *
 * @author bickhart
 */
public class IntersectIncrement extends LineIntersect<BedCounter>{
    public static void intersect(BedMap<BedCounter> a, String chr, int start, int end){
    
     if (a.containsChr(chr)) {
         BinBed.getBins(start, end)
                 .stream()
                 // Select only the bins contained in the BedMap
                 .filter((b) -> (a.containsBin(chr, b)))
                 .map((b) -> {
                     return a.getBedAbstractList(chr, b);
                 })
                    // Process each bed and check for overlap
                    .flatMap(ArrayList::stream)
                    .filter((bed) -> (ovCount(bed.Start(), bed.End(), start, end) > 0))
                        .forEach((bed) -> {
                            bed.increment();
                        });
     }
   }
}
