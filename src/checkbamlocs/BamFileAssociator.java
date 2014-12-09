/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkbamlocs;

import file.BedMap;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;
import htsjdk.samtools.ValidationStringency;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import utils.SortByChr;

/**
 *
 * @author bickhart
 */
public class BamFileAssociator {
    private final String bamFile;
    private final SamReaderFactory fact;
        
    public BamFileAssociator(String bamFile){
        this.bamFile = bamFile;
        this.fact = SamReaderFactory.makeDefault()
                .enable(SamReaderFactory.Option.CACHE_FILE_BASED_INDEXES)
                .validationStringency(ValidationStringency.LENIENT);
    }
    
    public void associate(final BedMap coords){
        
        try(SamReader reader = fact.open(new File(bamFile))){
        
            // I'll leave this unthreaded for now, but in the future I can make up a
            // bed based on chromosome lengths and use a lambda in order to split up
            // The interrogation of the bam based on subsections in a threaded fashion
            reader.iterator().forEachRemaining(
                    (s) -> {
                        IntersectIncrement.intersect(coords, s.getReferenceName(), s.getAlignmentStart(), s.getAlignmentEnd());
                    }
            );
        
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public void printOutBedMap(final BedMap<BedCounter> coords, String output){
        try(BufferedWriter out = Files.newBufferedWriter(Paths.get(output), Charset.defaultCharset())){
            for(String c : SortByChr.ascendingChr(coords.getListChrs())){
                for(BedCounter b : coords.getSortedBedAbstractList(c)){
                    out.write(b.toString());
                }
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
