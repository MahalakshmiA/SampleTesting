/**
 * 
 */
package com.api.testing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author 525523
 *
 */
public class StockTesting3 extends JPanel {

	static JTextField[] fields;
	static String text1;
	static String text2;
	static String outputPath;
	static String fileName = "C:\\file\\Functions.txt";

	// static JRadioButton rb1,rb2,rb3, rb4, rb5;
	private static JButton submitButton = new JButton("Submit");
	private static JComboBox cb;
	static JPanel labelPanel = new JPanel(new GridLayout(4, 1));
	static JLabel path = new JLabel();

	public StockTesting3() {
		super(new BorderLayout());
		String[] labels = { "Label1", "Label2", "Label3" };

		JPanel fieldPanel = new JPanel(new GridLayout(4, 1));
		add(labelPanel, BorderLayout.WEST);
		add(fieldPanel, BorderLayout.CENTER);
		fields = new JTextField[labels.length];
		for (int i = 0; i < labels.length; i++) {
			fields[i] = new JTextField(25);
			JLabel label = new JLabel(labels[i], JLabel.LEFT);
			label.setLabelFor(fields[i]);

			labelPanel.add(label);
			JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
			if (i < 2) {

				p.add(fields[i]);
				fieldPanel.add(p);
			} else {
				 ArrayList<String> functionList = readFunctions(fileName);
				 Object[] functions = functionList.toArray();
				
				cb = new JComboBox(functions);

				JPanel wrapper = new JPanel();
				wrapper.add(cb);

				fieldPanel.add(wrapper);

			}
			fieldPanel.add(path);

		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockTesting3 form = new StockTesting3();
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				text1 = form.getText(0);
				text2 = form.getText(1);
				String finalText = "";
				System.out.println("dfdsf" + text1 + "--" + text2);
				String methodSelected = ""   
						   + cb.getItemAt(cb.getSelectedIndex()); 
				System.out.println(methodSelected);
				Class cls = null;
				try {
					cls = Class.forName("com.api.testing."+methodSelected);
					if(null != cls) {
					Object obj = cls.newInstance();
					java.lang.reflect.Method method;
					try {
						method = obj.getClass().getMethod("execute");
					
					
						String text = (String) method.invoke(obj, args);
						System.out.println(text);
						finalText =text;
					 } catch (NoSuchMethodException | SecurityException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}catch (IllegalArgumentException | InvocationTargetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					}
					
				 } catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}catch (InstantiationException | IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("methodSelected - >" + methodSelected + "->"+ finalText);
				
				path.setText(finalText);

			}
		});

		JFrame frame = new JFrame("Stock Testing v2.0");
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		frame.getContentPane().add(form, BorderLayout.NORTH);

		JPanel p = new JPanel();
		p.add(submitButton);
		/* p.add(clearButton); */
		frame.getContentPane().add(p, BorderLayout.SOUTH);
		frame.getContentPane().add(p, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);

	}

	public String getText(int i) {
		return (fields[i].getText());
	}
	
	public static String execute() {
		
		return "I called";
	}
	
	/**
	 * @param fileName
	 * @param megaCapStockList
	 */
	private static ArrayList<String> readFunctions(String fileName) {
		File file = new File(fileName);
		ArrayList<String> functions = new ArrayList<String>();

		try {
			Scanner scanner = new Scanner(file);

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				functions.add(line);
			}

			scanner.close();
			Collections.sort(functions);
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		return functions;
	}
}
