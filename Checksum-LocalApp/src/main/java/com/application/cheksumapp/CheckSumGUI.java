package com.application.cheksumapp;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.springframework.stereotype.Component;

@Component
public class CheckSumGUI implements ActionListener 
{
    private static JFrame frame;
    private static JPanel panel;

    private static JLabel filePathLabel, verifyLabel;
    private static JTextField filePathTextField, verifyTextField;
    private static JButton buttonBrowse, buttonCheck, buttonVerify;

    private static Map<String, JLabel> algorithmLabels;
    private static Map<String, JTextField> algorithmTextFields;
    private static Map<String, JButton> algorithmCopyButtons;

    public CheckSumGUI() 
    {
        frame = new JFrame();
        panel = new JPanel();

        frame.setSize(640, 540);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setTitle("Checksum Checker");
        panel.setLayout(null);

        // File Path location
        filePathLabel = new JLabel("File location: (enter full path including file extension)");
        filePathLabel.setBounds(10, 10, 400, 25);
        panel.add(filePathLabel);

        filePathTextField = new JTextField();
        filePathTextField.setBounds(10, 40, 400, 25);
        panel.add(filePathTextField);
        
        buttonBrowse = new JButton("Browse");
        buttonBrowse.setBounds(420, 40, 80, 25);
        buttonBrowse.addActionListener(this);
        panel.add(buttonBrowse);

        buttonCheck = new JButton("Check");
        buttonCheck.setBounds(520, 40, 80, 25);
        buttonCheck.addActionListener(this);
        panel.add(buttonCheck);

        algorithmLabels = new HashMap<>();
        algorithmTextFields = new HashMap<>();
        algorithmCopyButtons = new HashMap<>();

        // Add algorithms
        addAlgorithm("MD5", 70);
        addAlgorithm("SHA-1", 130);
        addAlgorithm("SHA-256", 190);
        addAlgorithm("SHA-512", 250);
        addAlgorithm("SHA3-256", 310);
        addAlgorithm("SHA3-512", 370);
        
        // Verify
        verifyLabel = new JLabel("Have a Checksum? Enter and verify: ");
        verifyLabel.setBounds(10, 430, 600, 25);
        panel.add(verifyLabel);
        
        verifyTextField = new JTextField();
        verifyTextField.setBounds(10, 460, 500, 25);
        verifyTextField.setEnabled(false);
        panel.add(verifyTextField);
        
        buttonVerify = new JButton("Verify");
        buttonVerify.setBounds(520, 460, 80, 25);
        buttonVerify.addActionListener(this);
        buttonVerify.setEnabled(false);
        panel.add(buttonVerify);
        
        frame.setVisible(true);
    }

    private void addAlgorithm(String algorithm, int y) 
    {
        JLabel label = new JLabel(algorithm + ":");
        label.setBounds(10, y, 600, 25);
        panel.add(label);
        algorithmLabels.put(algorithm, label);

        JTextField textField = new JTextField();
        textField.setBounds(10, y + 30, 500, 25);
        panel.add(textField);
        textField.setEnabled(false);
        algorithmTextFields.put(algorithm, textField);

        JButton button = new JButton("Copy");
        button.setBounds(520, y + 30, 80, 25);
        button.addActionListener(this);
        panel.add(button);
        button.setEnabled(false);;
        algorithmCopyButtons.put(algorithm, button);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
    	if(e.getSource() == buttonBrowse)
    	{
    		JFileChooser fileChooser = new JFileChooser();
    		int response = fileChooser.showOpenDialog(null);
    		
    		if(response == JFileChooser.APPROVE_OPTION)
    		{
    			File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
    			System.out.println("Entered file path via Browse is: " + file);
    			
    			String filePath = file.toString();
    			filePathTextField.setText(filePath);
    		}
    	}
        if(e.getSource() == buttonCheck && !filePathTextField.getText().isEmpty()) 
        {
        	for(String algorithm: algorithmLabels.keySet())
        	{
        		JTextField textField = algorithmTextFields.get(algorithm);
        		textField.setEnabled(true);
        		
        		JButton copyButton = algorithmCopyButtons.get(algorithm);
        		copyButton.setEnabled(true);
        	}
        	verifyTextField.setEnabled(true);
        	buttonVerify.setEnabled(true);
        	
        	JOptionPane.showMessageDialog(frame, "Please wait, this might take some time.");
        	
            String filePath = filePathTextField.getText();
            System.out.println("Entered file path is: " + filePath);

            try 
            {
                // Perform the checksum calculation for each algorithm
                for(String algorithm : algorithmTextFields.keySet()) 
                {
                    String value = CheckSumLogic.calculateChecksum(filePath, algorithm);
                    JTextField textField = algorithmTextFields.get(algorithm);
                    textField.setText(value);
                }
            } 
            catch(NoSuchAlgorithmException e1) 
            {
                e1.printStackTrace();
            } 
            catch(IOException e1) 
            {
                e1.printStackTrace();
            }
        }
        else
        {
        	// Gets triggered if we press the copy buttons
        	for(String algorithm: algorithmCopyButtons.keySet())
        	{
        		if(e.getSource()==algorithmCopyButtons.get(algorithm))
        		{
        			String text = algorithmTextFields.get(algorithm).getText();
        			StringSelection selection = new StringSelection(text);
        			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, null);
                    
                    JOptionPane.showMessageDialog(frame, algorithm + " value has been copied to clipboard.");
                    
                    break;
        		}
        	}
        	
        	if(e.getSource() == buttonVerify && !verifyTextField.getText().isEmpty())
        	{
        		boolean foundMatch = false;
        		String text = verifyTextField.getText();
        		System.out.println(text);
        		for(String algorithm : algorithmTextFields.keySet())
        		{
        			if(text.equals(algorithmTextFields.get(algorithm).getText()))
        			{
        				System.out.println("Algorithm: " + algorithm + " TextField Value:  " + algorithmTextFields.get(algorithm).getText()) ;
        				foundMatch = true;
        				JOptionPane.showMessageDialog(frame, "Found a match. Algorithm: "+ algorithm);
            			break;
        			}
        		}
        		if(foundMatch == false)
        			JOptionPane.showMessageDialog(frame, "No match found");	
        	}
        	else if((e.getSource() == buttonVerify && verifyTextField.getText().isEmpty()))
        		JOptionPane.showMessageDialog(frame, "Enter checksum.");
        }
    }
}