package com.sample;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AppController
 */
@WebServlet("/AppController")
public class AppController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public AppController() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("I'm in");
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("I'm in post");
		 // read form fields
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String phoneNo = request.getParameter("phoneNo");
        String emailId = request.getParameter("email");
        String addr = request.getParameter("addr");
        String confirm = request.getParameter("confirm");
       
        UserInfo user =new UserInfo();
        user.setFname(fname);
        user.setLname(lname);
        user.setPhoneNo(phoneNo);
        user.setEmailId(emailId);
        user.setAddr(addr);
        
        if(confirm.equalsIgnoreCase("Yes")){
        	SendMailSSL sendMail = new SendMailSSL();
        	sendMail.sendMail(user);
        	sendMail.sendAcknowledgeMail(user);
        	
        }
        
         
        System.out.println("username: " + fname);
        System.out.println("confirm: " + confirm);
		
	}

}
