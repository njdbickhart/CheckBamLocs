/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkbamlocs;

import file.BedMap;
import htsjdk.variant.vcf.VCFFileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

/**
 *
 * @author bickhart
 */
public class InputBedFactory {
    private final Path inputFile;
    private enum fType {VCF, BED, NULL};
    private final fType fFlag;
    
    public InputBedFactory(String input){
        this.inputFile = Paths.get(input);
        this.fFlag = checkFileType(this.inputFile);
        try{
            if(this.fFlag == fType.NULL){
                throw new Exception("Error with file type association! Could not determine file type!");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    private fType checkFileType(Path input){
        // Naive check to see if file conforms to VCF standards
        if(input.endsWith(".gz") || input.endsWith(".vcf")){
            try(VCFFileReader v = new VCFFileReader(input.toFile(), false)){
                return fType.VCF;
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        try(BufferedReader read = Files.newBufferedReader(input, Charset.defaultCharset())){
            String line;
            while((line = read.readLine()) != null){
                if(line.startsWith("#"))
                    continue;
                String[] segs = line.split("\t");
                if(segs.length >= 3)
                    return fType.BED;
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return fType.NULL;
    }
    
    public BedMap<BedCounter> generateBedLocs(){
        BedMap<BedCounter> coords = new BedMap<>();
        switch(this.fFlag){
            case BED:
                bedCreation(coords);
                break;
            case VCF:
                // TODO: add vcf creation tool
                break;
        }
        return coords;
    }
    
    private void bedCreation(BedMap<BedCounter> coords){
        try(BufferedReader read = Files.newBufferedReader(this.inputFile, Charset.defaultCharset())){
            String line;
            while((line = read.readLine()) != null){
                String[] segs = line.split("\t");
                coords.addBedData(new BedCounter(segs));
            }
        }catch(IOException ex){
            ex.printStackTrace();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(InputBedFactory.class.getName()).log(Level.SEVERE, "Issue with bed file interpretation!", ex);
        }
    }
}
