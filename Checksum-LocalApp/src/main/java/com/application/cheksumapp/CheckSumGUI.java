package com.application.cheksumapp;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
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

    private static JLabel filePathLabel;
    private static JTextField filePathText;
    private static JButton buttonCheck;

    private static Map<String, JLabel> algorithmLabels;
    private static Map<String, JTextField> algorithmTextFields;
    private static Map<String, JButton> algorithmCopyButtons;

    public CheckSumGUI() 
    {
        frame = new JFrame();
        panel = new JPanel();

        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setTitle("Checksum Checker");
        panel.setLayout(null);

        // File Path location
        filePathLabel = new JLabel("File location: (enter full path including file extension)");
        filePathLabel.setBounds(10, 10, 400, 25);
        panel.add(filePathLabel);

        filePathText = new JTextField();
        filePathText.setBounds(10, 40, 500, 25);
        panel.add(filePathText);

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
        algorithmTextFields.put(algorithm, textField);

        JButton button = new JButton("Copy");
        button.setBounds(520, y + 30, 80, 25);
        button.addActionListener(this);
        panel.add(button);
        algorithmCopyButtons.put(algorithm, button);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource() == buttonCheck && !filePathText.getText().isEmpty()) 
        {
        	
            String filePath = filePathText.getText();
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
        else if(e.getSource() == buttonCheck && filePathText.getText().isEmpty())
        	JOptionPane.showMessageDialog(frame, "Filepath not specified. Please specify the filepath.");
        else
        {
        	// Gets triggered if we press the copy buttons
        	
        	for(String algorithm: algorithmCopyButtons.keySet())
        	{
        		if(e.getSource()==algorithmCopyButtons.get(algorithm) && !filePathText.getText().isEmpty())
        		{
        			String text = algorithmTextFields.get(algorithm).getText();
        			StringSelection selection = new StringSelection(text);
        			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, null);
                    
                    JOptionPane.showMessageDialog(frame, algorithm + " value has been copied to clipboard.");
                    
                    break;
        		}
        		else if(filePathText.getText().isEmpty())
        		{
        			JOptionPane.showMessageDialog(frame, "Filepath not specified. Please specify the filepath.");
        			break;
        		}
        	}
        }
    }
}