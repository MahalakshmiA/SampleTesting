package com.sample;

public class Test1 {
	
	String a = "maha";
	int i =0;
	int z ;
	
	public static void main(String[] args) 
	{
		
	}
	
	public  String testString(String s, int threadId){
		
		s = s + a;
		z= i++;
		
		System.out.println("Thread ID value " + threadId+ "The value of S"+s +" The value of a"+ a 
				+"THe value of i" +i);
		a= s;
		z= i++;
		System.out.println("Thread ID value " + threadId+ "The value of S"+s +" The value of a"+ a 
				+"THe value of i" +i);
		return s;
		
	}

}
