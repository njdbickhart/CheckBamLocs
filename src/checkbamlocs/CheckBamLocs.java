/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkbamlocs;

import GetCmdOpt.SimpleCmdLineParser;
import file.BedMap;

/**
 *
 * @author bickhart
 */
public class CheckBamLocs {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimpleCmdLineParser cmd = setCmdLineOpts();
        cmd.GetAndCheckOpts(args, 
                "i:o:b:", 
                "iob", 
                "iob", 
                "input", "output", "bam");
        
        // initialize and check bam file
        BamFileAssociator assoc = new BamFileAssociator(cmd.GetValue("bam"));
        
        // Generate bed file coordinates
        InputBedFactory bed = new InputBedFactory(cmd.GetValue("input"));
        BedMap<BedCounter> coords = bed.generateBedLocs();
        System.err.println("[MAIN] Loaded test coordinates file!");
        
        // Perform the association
        assoc.associate(coords);
        System.err.println("[MAIN] Performed the association! Printing results to file: " + cmd.GetValue("output"));
        
        // Print out the coordinates
        assoc.printOutBedMap(coords, cmd.GetValue("output"));
    }
    
    public static SimpleCmdLineParser setCmdLineOpts(){
        String nl = System.lineSeparator();
        SimpleCmdLineParser cmd = new SimpleCmdLineParser(
                "CheckBamLocs: a program to intersect bed or vcf files with BAMs" + nl +
                        "Usage: java -jar CheckBamLocs -i <input bed or vcf> -o <output file name> -b <bam file>" + nl +
                        "\tthe program calculates the read depth over each location in the input file" + nl
        );
                        
        return cmd;
    }
}
