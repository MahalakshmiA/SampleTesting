package com.sample;

import java.util.ArrayList;

class MultithreadingDemo extends Thread 
{ 
	
	private String to;

    public MultithreadingDemo(String to) {
        this.to = to;
    }
	
    public void run() 
    { 
        try
        { 
            // Displaying the thread that is running 
            System.out.println ("Thread " + 
                  Thread.currentThread().getId() + 
                  " is running for " +to); 
           /* Test1 t1 = new Test1();
            t1.testString(Thread.currentThread().getId(), (int) Thread.currentThread().getId());*/
            Client2 t1 = new Client2();
            t1.test2((int) Thread.currentThread().getId(), to);
  
        } 
        catch (Exception e) 
        { 
            // Throwing an exception 
            System.out.println ("Exception is caught"); 
        } 
    } 
} 
  
// Main Class 
public class Multithread 
{ 
    public static void main(String[] args) 
    { 
    	ArrayList<String> niftyList = ReadStocks.readStocksFile("C:/file/niftyStocks.txt");
        int n =10; // Number of threads 
        for (int i=0; i<niftyList.size(); i++) 
        { 
            MultithreadingDemo object = new MultithreadingDemo(niftyList.get(i)); 
            object.start(); 
        } 
    } 
} 