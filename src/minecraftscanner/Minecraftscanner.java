/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package minecraftscanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
/**
 *
 * @author TOKELO
 */
public class Minecraftscanner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)   {
        // TODO code application logic here
      
        String path = "C:\\Users\\TOKELO\\Documents\\NetBeansProjects\\Minecraftscanner\\r.0.0.mca";
        Reader(path);   
}
    public static void Reader(String path){
        
       
        //i am using RandomAccessFile as it allows me to read specific bytes instead of fileinputstream that reads sequentionlly
        //the r = read only mode they is also a write mode = w
        try(RandomAccessFile ReadingFile = new RandomAccessFile(path,"r"); ){
            //i am reading from a file
        
       //this array allows me to store data
       //the reason it is 4096 is thats the full entry which is 4*1024
        byte[] buffer = new byte[4096];
        //this allows me to read 
        ReadingFile.read(buffer);
        
        //the total entries for region
        int totalentries= 4096;
        //the i+= 4 means i am incrementing in 4
        for(int i=0;i<totalentries;i += 4){
            
             int CountOffSet = CalculateCountOffSet(buffer,i);
             if(CountOffSet == 0) continue;
       
             //this is chunklength
        int count = CalculateCount(buffer,i);
        
        
        int BytePosition = CountOffSet*4096;
        
        ///this reads my file and ponits to the beginning of chunk
        ReadingFile.seek(BytePosition);
        
        //This creates a a buffer to my chunklength variable count
        byte[] ChunkSize = new byte[4];
        
        byte[] Chunktype = new byte[1];
        

         
         //to now read into file, this gives life of compressed chunk
          ReadingFile.read(ChunkSize);
          
          //this determines type of compression
          ReadingFile.read(Chunktype);
          
            //this converts byte to int if value=1 then its gzip , if value=2 then its zlib
          int value = Chunktype[0]& 0xFF;
          
          //compressed -1 read
           int Chunklength = ByteBuffer.wrap(ChunkSize).getInt();
           
           if(Chunklength<1){
           break;
           }else if(Chunklength>=1){
               byte[] compress = new byte[Chunklength-1];
           
           ReadingFile.read(compress);
           
            //if statement to find out type of compression
          if(value == 1){
              try{
              GZIPInputStream gzipdecom = new GZIPInputStream(new ByteArrayInputStream(compress));
               //outputs the data
            ByteArrayOutputStream out = new ByteArrayOutputStream();
           
           //read and writes to out
           gzipdecom.transferTo(out);
            
           //stores the decompress value of out to btye array
             byte[] DecompressValue = out.toByteArray();
             
             //to print out byte array
             for (byte b : DecompressValue) {
           System.out.printf("%02X ", b);
          }
            System.out.println();
              }catch(BufferUnderflowException ex){
                  System.out.print(ex);
              }
          }else if(value == 2){
             try{
              //this Decompress the data
           InflaterInputStream zlibdecom = new InflaterInputStream(new ByteArrayInputStream(compress));

            //outputs the data
            ByteArrayOutputStream out = new ByteArrayOutputStream();
           
           //read and writes to out
           zlibdecom.transferTo(out);
       
           
           //stores the decompress value of out to btye array
         byte[] DecompressValue = out.toByteArray();
            
         //to print out byte array
         for (byte b : DecompressValue) {
           System.out.printf("%02X ", b);
          }
            System.out.println();
             }catch(BufferUnderflowException ex){
                  System.out.print(ex);
              }
          }
           }
          
        
          
         
           
          

         
        //System.out.println("Entry: "+i/4+" CountOffSet: "+CountOffSet+" Count: "+count+" BytePosition: "+BytePosition);
        
        }
        
        //i want to calculate the countoffset i found the method its how to implement that was the issue 
       
        
       ReadingFile.close();
    }catch(FileNotFoundException ex) {
        //this is to catch filenotfound exception
        ex.printStackTrace();
}catch(IOException ex){
    ex.printStackTrace();
//this is to catch the ioexception error
}
        
 
        
    }
    
    //
    public static int CalculateCount(byte[] buffer,int position){
      //the position value is to follow the forloop   
      // so to convert 1 byte to int fisrt i have to unsign it which is the 0xFF then it will convert
      int unsignedInt = buffer[position+3] & 0xFF; 
        return unsignedInt;

    }
    
    //
    public static int CalculateCountOffSet(byte[] buffer,int position){
        // so to convert 1 byte to int fisrt i have to unsign it which is the 0xFF then it will convert

        int byte1 = buffer[position] & 0xFF;
        int byte2 = buffer[position+1] & 0xFF;
        int byte3 = buffer[position+2] & 0xFF;
        //this is the method to convert 3 byte to int
        int countOffset = (byte1 << 16)|(byte2 << 8)|byte3;
        
        return countOffset;
        
    }
    
    public static String NBTReadParse(){
        
        return null;
    }
}
