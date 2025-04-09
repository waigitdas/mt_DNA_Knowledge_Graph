/**
 * Copyright 2022-2024 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.ref_files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import mt_var.neo4jlib.neo4j_qry;
import org.apache.commons.io.FileUtils;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;


public class create_mt_seq_csv_file {
   public static int cfasta_seq[][] = null;
   public static String fasta_seq[] = null;

    @UserFunction
    @Description("Loads FASTA files, adds assigned_hgs and exports csv file.")

    public String read_and_process_fasta(
  
  )
   
         { 
             
        create_fasta_csv();
         return "";
            }

    
    
    public static void main(String args[]) {
        create_fasta_csv();
    }
    
     public static String create_fasta_csv()
    {
        mt_var.neo4jlib.neo4j_info.neo4j_var();
        mt_var.neo4jlib.neo4j_info.neo4j_var_reload();
        
        //location of GenBank FASTA files
        String dir = "E:/DAS_Coded_BU_2020/Genealogy/DNA/mt_haplotree_project/mt_DNA_sequences/entrez/";

        String set_name= "";

       //create Neo4j indices before data populates the database
        try{
            mt_var.neo4jlib.neo4j_qry.CreateIndex("mt_seq", "name");
            mt_var.neo4jlib.neo4j_qry.CreateIndex("mt_seq", "seq_id");
        }
        catch(Exception e){}
        
        //get a list of files from the directory which holds them
        List<File> fasta = new ArrayList<File>();
 

       int i = 0;
       /////////////////////////////////////////////////////////////////
       /////////////////////////////////////////////////////////////////
       //iterate fasta_seqs to create an array of file names
       for (File fasta_file : (List<File>) FileUtils.listFiles(new File(dir), new String[]{"fasta", "FASTA", "fa"}, true)) 
          {

            fasta.add(fasta_file);
        }
       
       //initialize arrays to hold fasta_seq and fasta_seq tile data
       fasta_seq = new String[fasta.size()];
 
       //initialize variables in neo4jLib; they are used during repeated calls to function creating tiles and patterns
       mt_var.neo4jlib.neo4j_info.neo4j_var();
       String seq = "";
       
       //////////////////////////////////////////////////////////////////
       //////////////////////////////////////////////////////////////////
       //iterate through each fasta_seq
       for (i=0; i<fasta.size(); i++)  
       {
        
           //get source of seq
           if (fasta.get(i).getPath().contains("entrez")) {set_name="GenBank";}
//           
           // read fasta_seq file and parse it into an array of the multiple lines in the fasta file
           String s[] = mt_var.neo4jlib.file_lib.readFileByLine(fasta.get(i).getPath()).split("\n");
           fasta_seq[i] =s[0].replace(",", " ").split(" ")[0].replace(">","").split(Pattern.quote("."))[0];
           int n =0;
           seq = "";
           
           // concatenate the rows into a sinle string with the full sequence
          // String header = s[0].replaceAll(" [^a-zA-Z_-]+ ", "");
           for (int j=1; j<s.length; j++)
           {
               seq = seq + s[j];
               }
			
			

           ////////////////////////////////////////////////////////////////////////////
           ////////////////////////////////////////////////////////////////////////////
           ////////////////////////////////////////////////////////////////////////////
           //add mt_seq node and its fullSeq 
           if(seq.length()<17000)
           {
           int seq_id = 1000000 + i + 1;
           String cq = "merge (k:mt_seq{seq_id: " + seq_id + ", name:'" + fasta_seq[i]  + "'}) set k.fullSeq='" + seq + "', k.source='" + set_name + "'";  //, header:'" + header + "'" ;
           mt_var.neo4jlib.neo4j_qry.qry_write(cq);
           
       }   //end if that excludes rogue sequence (too long)
            }  //next seq
    
     
       
       //////////////////////////////////////////////////////////////
       //print csv files for uploading to Neo4
      //fasta_seqs
       String fnfasta_seqs = "fasta_seqs.csv";
        FileWriter fwk = null;
                
       try
        {
            fwk = new FileWriter(mt_var.neo4jlib.neo4j_info.Import_Dir + fnfasta_seqs);
            fwk.write("seq_id|fasta_seq|fullSeq\n");
                }    
        catch(Exception e){
            System.out.println("error #105\t" + e.getMessage());
        }
       
       for (int k=0; k<fasta_seq.length; k++)
       {
           try
           {
               fwk.write(fasta_seq[k] + "\n");
           }
           catch(Exception e){}
       }
       
       try
       {
           fwk.flush();
           fwk.close();
       }
       catch(Exception e){}

               

//flag sequences with anomalies
//hyphen(s) count
//filtering before this should result in none being identified, but will leave this in and a quality check
mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (n:mt_seq) where n.fullSeq<>replace(n.fullSeq,'-','') set n.hyphen = size(n.fullSeq)-size(replace(n.fullSeq,'-','')) ");

//Ns count
//filtering before this should result in none being identified, but will leave this in and a quality check
mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (n:mt_seq) where n.fullSeq<>replace(n.fullSeq,'N','') set n.Ns = size(n.fullSeq)-size(replace(n.fullSeq,'N',''))");
   
//set seq length property
////seqences with exactly 16569 lenght were processed excluding indels
//mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (n:mt_seq) set n.seq_length=size(n.fullSeq)");

//tag seq with frameshifts that preclude their use in discoverying proble
//filtering before this should result in none being identified, but will leave this in and a quality check
mt_var.neo4jlib.neo4j_qry.qry_write("with [25,250,1000,10000, 16000] as ps unwind ps as x call { with x MATCH (k:mt_seq) with k,x match (p:lookup_probe{pos:x}) with k,x, p where k.fullSeq<>replace(k.fullSeq,p.probe,'') with x, k,split(k.fullSeq,p.probe) as kk with x, k, size(kk) as split_ct, size(kk[0])+11 as pos with x,k, split_ct,pos where x-5<pos>x+5 and split_ct=2 return k,split_ct,pos } with k,max(pos-x) as mx set k.seq_mx_frameshift=mx");
        
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
// load mitomap haplogroups provided by CHOP
        mt_var.neo4jlib.file_lib.copyFileToImportDirectory("genbank_haplogroup.tsv");
        mt_var.neo4jlib.neo4j_qry.qry_write("LOAD CSV WITH HEADERS FROM 'file:///genbank_haplogroup.tsv' as line FIELDTERMINATOR '|' with line match (k:mt_seq{name:toString(split(line.genbank_id,'.')[0])}) set k.assigned_hg=toString(line.haplogroup_verbose)");
        
        mt_var.neo4jlib.file_lib.copyFileToImportDirectory("Gene_by_Gene_haplogroup_assignments.csv");
        mt_var.neo4jlib.neo4j_qry.qry_write("load csv with headers from 'file:///Gene_by_Gene_haplogroup_assignments.csv' as line fieldterminator ',' with line, split(line.Accession,'.') as ss MATCH (s:mt_seq{name:ss[0]})  set s.assigned_hg=line.Haplogroup,s.L7=1");
        
        mt_var.neo4jlib.neo4j_qry.qry_write("match(s:mt_seq{name:'AAAA_RSRS'}) set s.source='GenBank', s.usable=1, s.assigned_hg='Eve'");
       
   
        //export reference file
        
        //filter for desired sequences
        mt_var.neo4jlib.neo4j_qry.qry_write("MATCH (s:mt_seq{source:'GenBank'}) where  size(s.fullSeq)>16500 and size(s.fullSeq)-size(replace(s.fullSeq,'N',''))<4 and s.assigned_hg is not null set s.usable=1");

        String fnout = "GenBank_full_sequences_" + mt_var.genlib.current_date_time.getDateTime() + ".csv";
        String fntemp = "GenBank_full_sequences.csv";
        mt_var.neo4jlib.neo4j_qry.qry_to_pipe_delimited("MATCH (s:mt_seq{usable:1}) RETURN s.seq_id as seq_id, s.name as name, s.source as source, s.assigned_hg as assigned_hg, s.fullSeq as fullSeq", fntemp);
        String fin = mt_var.neo4jlib.file_lib.readFileByLine(mt_var.neo4jlib.neo4j_info.Import_Dir + fntemp);
        File f = new File(mt_var.neo4jlib.neo4j_info.Import_Dir + fnout);
        FileWriter fw = null;
        try
        {
             fw = new FileWriter(f);
             fw.write(fin.replace("\"",""));
             fw.flush();
        }
         catch (IOException e){}
                
        try
        {
            fw.flush();
            fw.close();
        }
        catch(IOException e){}
        
        return "";
    }
}
