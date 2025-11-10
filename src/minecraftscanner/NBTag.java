/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraftscanner;

import java.util.List;

/**
 *
 * @author TOKELO
 */
class NBTag {
    byte TAG_End;
    byte TAG_Byte;
    short TAG_Short;
    int TAG_Int;
    long TAG_Long;
    float TAG_Float;
    double TAG_Double;
    byte[] TAG_Byte_Array;
    String TAG_String;
    List<String> TAG_List;
    int[] TAG_Int_Array;
    long[] TAG_Long_Array;
    
    
    
    public NBTag(byte TAG_End,byte TAG_Byte,short TAG_Short,int TAG_Int,long TAG_Long,float TAG_Float,double TAG_Double,byte[] TAG_Byte_Array,String TAG_String,List<String> TAG_List,int[] TAG_Int_Array,long[] TAG_Long_Array) {
    this.TAG_End= TAG_End; 
    this.TAG_Byte= TAG_Byte;
    this.TAG_Short=TAG_Short;
    this.TAG_Int=TAG_Int;
    this.TAG_Long=TAG_Long;
    this.TAG_Float=TAG_Float;
    this.TAG_Double=TAG_Double;
    this.TAG_Byte_Array=TAG_Byte_Array;
    this.TAG_String=TAG_String;
    this.TAG_List=TAG_List;
    this.TAG_Int_Array=TAG_Int_Array;
    this.TAG_Long_Array=TAG_Long_Array;
}
    class ParsedBlock{
        String id;
        String Name;
        int x,y,z;
    }
    
    /*
    Chunk
    BlockID\ItemID
    BlockName&ItemName
    Xcoordinte
    Ycoordinate
    Zcoordinate
    
    
    */
}
