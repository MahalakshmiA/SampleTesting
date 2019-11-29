/**
 * 
 */
package com.api.testing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.google.gson.FieldNamingStrategy;

/**
 * @author 525523
 *
 */
public class StockTesting extends JPanel {
	
	static JTextField[] fields;
	static String text1;
	static String text2;
	static String outputPath;
	
	static JRadioButton rb1,rb2,rb3, rb4, rb5;  
	private static JButton submitButton = new JButton("Submit");
	static JPanel labelPanel = new JPanel(new GridLayout(8,1));
	static JLabel path = new JLabel();
	
	
	public StockTesting() {
		super(new BorderLayout());
		String[] labels = {"Label1", "Label2"};
		
		JPanel fieldPanel = new JPanel(new GridLayout(8,1));
		add(labelPanel,BorderLayout.WEST);
		add(fieldPanel, BorderLayout.CENTER);
		fields = new JTextField[labels.length];
		for(int i= 0; i<labels.length; i++) {
			fields[i]= new JTextField(25);
			JLabel label = new JLabel(labels[i], JLabel.LEFT);
			label.setLabelFor(fields[i]);
		
			labelPanel.add(label);
			JPanel p = new JPanel(new FlowLayout( FlowLayout.LEFT));
			p.add(fields[i]);
			fieldPanel.add(p);
		}
		
		
		 rb1=new JRadioButton("Method 1");    
		rb2=new JRadioButton("Method 2"); 
		rb3=new JRadioButton("Method 3");    
		rb4=new JRadioButton("Method 4");    
		rb5=new JRadioButton("Method 5");    
		JPanel p1 = new JPanel(new FlowLayout( FlowLayout.LEFT));  
		ButtonGroup bg=new ButtonGroup(); 
		bg.add(rb1);
		bg.add(rb2);
		bg.add(rb3);
		bg.add(rb4);
		bg.add(rb5);
		labelPanel.add(rb1);
		labelPanel.add(rb2);
		labelPanel.add(rb3);
		labelPanel.add(rb4);
		labelPanel.add(rb5);
	//	add(p1, BorderLayout.EAST);
		
		labelPanel.add(path);
	
		
	
		

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockTesting form = new StockTesting();
		submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				text1 = form.getText(0);
				text2 = form.getText(1);
				System.out.println("dfdsf"+text1+"--"+text2);
				if(rb1.isSelected()) {
					System.out.println("1 selected");
					outputPath = "1 selected";
				}
				if(rb2.isSelected()) {
					System.out.println("2 selected");
					outputPath = "2 selected";
				}
				if(rb3.isSelected()) {
					System.out.println("3 selected");
					outputPath = "3 selected";
				}
				if(rb4.isSelected()) {
					System.out.println("4 selected");
					outputPath = "4 selected";
				}
				if(rb5.isSelected()) {
					System.out.println("5 selected");
					outputPath = "5 selected";
				}
				path.setLabelFor(path);;
				
			}
		});

		JFrame frame = new JFrame();
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		frame.getContentPane().add(form, BorderLayout.NORTH);

		
		JPanel p = new JPanel();
		p.add(submitButton);
		/*p.add(clearButton);*/
		frame.getContentPane().add(p, BorderLayout.SOUTH);
		frame.getContentPane().add(p, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public String getText(int i) {
	return (fields[i].getText());
	}
}
