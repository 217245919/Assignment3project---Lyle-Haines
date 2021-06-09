/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.cput.assignment3project;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Lyle Haines - 217245919
 * This java program read and write to a, ser and txt file respectively
 */
public class performOps {

ObjectInputStream read;
FileWriter writer;
BufferedWriter buffWriter;
ArrayList<Customer> arrayCus= new ArrayList<>();
ArrayList<Supplier> arraySup= new ArrayList<>();

    public void openFileRead(){
        try{
            read = new ObjectInputStream( new FileInputStream( "stakeholder.ser" ) ); 
            System.out.println("*** ser file created and opened for reading  ***");               
        }
        catch (IOException ioe){
            System.out.println("error opening ser file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    public void readFile(){
        try{
           while(true){
               Object line = read.readObject();
               String c ="Customer";
               String s = "Supplier";
               String name = line.getClass().getSimpleName();
               if ( name.equals(c)){
                   arrayCus.add((Customer)line);
               } else if ( name.equals(s)){
                   arraySup.add((Supplier)line);
               } else {
                   System.out.println("Error: Not working");
               }
           } 
        }
        catch (EOFException eofe) {
            System.out.println("End of file reached");
        }
        catch (ClassNotFoundException ioe) {
            System.out.println("Class error reading ser file: "+ ioe);
        }
        catch (IOException ioe) {
            System.out.println("Error reading ser file: "+ ioe);
        }
        
        sortCus();
        sortSup();
    }
    public void readCloseFile(){
        try{
            read.close( ); 
        }
        catch (IOException ioe){            
            System.out.println("Error closing ser file: " + ioe.getMessage());
            System.exit(1);
        }
    }
 
    public void sortCus(){
        String[] sortID = new String[arrayCus.size()];
        ArrayList<Customer> sortA= new ArrayList<Customer>();
        int count = arrayCus.size();
        for (int i = 0; i < count; i++) {
            sortID[i] = arrayCus.get(i).getStHolderId();
        }
        Arrays.sort(sortID);
        
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (sortID[i].equals(arrayCus.get(j).getStHolderId())){
                    sortA.add(arrayCus.get(j));
                }
            }
        }
        arrayCus.clear();
        arrayCus = sortA;
    }
    
    public int ageCus(String dob){
        String[] seperation = dob.split("-");
        
        LocalDate birth = LocalDate.of(Integer.parseInt(seperation[0]), Integer.parseInt(seperation[1]), Integer.parseInt(seperation[2]));
        LocalDate current = LocalDate.now();
        Period difference = Period.between(birth, current);
        int age = difference.getYears();
        return age;
    }
    
    public String dobCus(Customer dob){
        LocalDate dateOfBirthToFormat = LocalDate.parse(dob.getDateOfBirth());
        DateTimeFormatter changeFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return dateOfBirthToFormat.format(changeFormat);
    }
   
    public String rentCus(){
        int count = arrayCus.size();
        int canRent = 0;
        int cannotRent = 0;
        for (int i = 0; i < count; i++) {
            if (arrayCus.get(i).getCanRent()){
                canRent++;
            }else {
                cannotRent++;
            }
        }
        String line = "Number of customers who can rent : "+ '\t' + canRent + '\n' + "Number of customers who cannot rent : "+ '\t' + cannotRent;
        return line;
    }
    
    public void displayCustomersText(){
        try{
            writer = new FileWriter("CustomerOutFile.txt");
            buffWriter = new BufferedWriter(writer);
            buffWriter.write(String.format("%s\n","===========================Customers========================================"));
            
            buffWriter.write(String.format("%-15s %-15s %-15s %-15s %-15s\n", "ID","Name","Surname","Date of Birth","Age"));
             buffWriter.write(String.format("%s\n","================================================================================"));
            for (int i = 0; i < arrayCus.size(); i++) {
                buffWriter.write(String.format("%-15s %-15s %-15s %-15s %-15s \n", arrayCus.get(i).getStHolderId(), arrayCus.get(i).getFirstName(), arrayCus.get(i).getSurName(), dobCus(arrayCus.get(i)),ageCus(arrayCus.get(i).getDateOfBirth())));
            }
            buffWriter.write(String.format("%s\n"," "));
            buffWriter.write(String.format("%s\n"," "));
            buffWriter.write(String.format("%s\n",rentCus()));
        }
        catch(IOException fnfe )
        {
            System.out.println(fnfe);
            System.exit(1);
        }
        try{
            buffWriter.close( ); 
        }
        catch (IOException ioe){            
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    
    public void sortSup(){
        String[] sortID = new String[arraySup.size()];
        ArrayList<Supplier> sortA= new ArrayList<Supplier>();
        int count = arraySup.size();
        for (int i = 0; i < count; i++) {
            sortID[i] = arraySup.get(i).getName();
        }
        Arrays.sort(sortID);
        
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (sortID[i].equals(arraySup.get(j).getName())){
                    sortA.add(arraySup.get(j));
                }
            }
        }
        arraySup.clear();
        arraySup = sortA;
    }
   
   public void displaySupplierText(){
        try{
            writer = new FileWriter("SupplierOutFile.txt");
            buffWriter = new BufferedWriter(writer);
            buffWriter.write(String.format("%s\n","===========================SUPPLIERS=========================================="));
           
            buffWriter.write(String.format("%-15s %-15s \t %-15s %-15s \n", "ID","Name","Prod Type","Description"));
            buffWriter.write("==============================================================================\n");
            for (int i = 0; i < arraySup.size(); i++) {
                buffWriter.write(String.format("%-15s %-15s \t %-15s %-15s \n", arraySup.get(i).getStHolderId(), arraySup.get(i).getName(), arraySup.get(i).getProductType(),arraySup.get(i).getProductDescription()));
            }
            System.out.println("Supplier Text file created and information is displayed.");
            
        }
        catch(IOException fnfe )
        {
            System.out.println(fnfe);
            System.exit(1);
        }
        try{
            buffWriter.close( ); 
        }
        catch (IOException ioe){            
            System.out.println("Error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    
public static void main(String args[])  {
    performOps obj=new performOps(); 
    obj.openFileRead();
    obj.readFile();
    obj.readCloseFile();
    obj.displayCustomersText();
    obj.displaySupplierText();

    }  
}
