package com.hit.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;



public class CacheUnitView{

    private final JFrame frame;
    private final CacheUnitPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    JTextArea ta = new JTextArea();
    JLabel taLabel = new JLabel();

    public CacheUnitView()
    {
        frame = new JFrame();
        panel = new CacheUnitPanel();
    }
    public void start()
    {
        panel.run();
    }



    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    public <T> void updateUIData(T status){
        // request succeeded
        if (status.toString().equals("true")){
            ta.setForeground(Color.green);
            ta.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            ta.setText("\n\n                   Succeeded :) ");
            taLabel .setIcon(new ImageIcon("images/suc.png"));
        }
        //request failed
        else if (status.toString().equals("false") || status.toString().equals("Empty")){
            ta.setForeground(Color.red);
            ta.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            ta.setText("\n\n                   Failed :( \n"
                    + "Check your Json file and try again ");
//            taLabel .setIcon(new ImageIcon("images/error.png"));
        }
        // statistics request
        else {
            ta.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            ta.setForeground(Color.pink);
            String[] res = ((String) status).split(",");
            ta.setText("Algorithm: " + res[0] + "\n\nCapacity: " + res[1] +
                    "\n\nTotal Numbers Of Requests: " + res[3] +
                    "\n\nTotal Number Of DataModel Swaps(From Cache To Disk): " + res[2] +
                    "\n\nTotal Number Of DataModels(GET/DELETE/UPDATE Requests): " + res[4]);
//            taLabel .setIcon(new ImageIcon("images/analytics.png"));
        }

        ta.validate();
        taLabel.validate();
        panel.revalidate();
        panel.repaint();
    }

    public class CacheUnitPanel extends JPanel implements ActionListener{
        private static final long serialVersionUID = 1L;

        JButton statButton;
        JButton reqButton;
        JLabel wp;
        JLabel label1;

        @Override
        public void actionPerformed(ActionEvent arg0) {}

        public <T> void updateUIData(T t)
        {
            if (t.toString().equals("true")) //load succeeded
            {
                ta.setText("Succeeded :) ");
                ta.setSelectedTextColor(Color.GREEN);
            }
            else if (t.toString().equals("false")) // load failed
            {
                ta.setText("Failed :( ");
                ta.setSelectedTextColor(Color.RED);
            }
            else ta.setText(t.toString());  // stat
            ta.invalidate();
        }


        public void run()
        {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("MMU Project");
            frame.setBounds(900, 900, 900, 900);
            panel.setBorder(new EmptyBorder(5, 5, 5, 5));
            frame.setContentPane(panel);
            panel.setLayout(null);
            ta.setBounds(80, 120, 700, 600);
            taLabel.setBounds(320,190,850,650);
//            taLabel.setIcon(new ImageIcon("images/comp.png"));
            ta.setSelectedTextColor(Color.WHITE);
            ta.setForeground(Color.blue);
            String taStr = "\nWelcome to our MMU Project ! \n"
                    + "Press 'Load a Request' to load your Json File, \n"
                    + "OR Press 'Show Statistics' \n"
                    + "to see the Server's Statistics So far.";
            ta.setText(taStr);
            ta.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            panel.add(taLabel);
            panel.add(ta);
            //mmu-headline
            label1 = new JLabel("MMU");
            label1.setForeground(Color.WHITE);
            label1.setFont(new Font("Comic Sans MS", Font.BOLD, 72));
            label1.setBounds(335, 11, 500, 68);
            panel.add(label1);
            //stats
            statButton = new JButton("Show Statistics");
            statButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
//            statButton.setIcon(new ImageIcon("images/stat.png"));
            statButton.setBackground(Color.WHITE);
            statButton.setBounds(550, 11, 300, 78);
            statButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    PropertyChangeEvent change;
                    change = new PropertyChangeEvent(CacheUnitView.this, "stats", null, "{ \"headers\":{\"action\":\"STATS\"},\"body\":[]}");
                    pcs.firePropertyChange(change);
                }
            });
            panel.add(statButton);

            //request
            reqButton = new JButton("Load a Request");
            reqButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
//            reqButton.setIcon(new ImageIcon("images/upload.png"));
            reqButton.setBackground(Color.WHITE);
            reqButton.setBounds(10,11,300, 78);
            reqButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JFileChooser fc = new JFileChooser();
                    fc.setCurrentDirectory(new File("."));
                    int result = fc.showOpenDialog(new JFrame());
                    if (result == JFileChooser.APPROVE_OPTION)
                    {
                        File selectedFile = fc.getSelectedFile();
                        System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                        if (selectedFile != null)
                        {
                            try
                            {
                                PropertyChangeEvent change;
                                change = new PropertyChangeEvent(CacheUnitView.this,"load",null,ParseFileToString(selectedFile.getPath()));
                                pcs.firePropertyChange(change);

                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                    }
                }

            });
            panel.add(reqButton);
            //background
            wp = new JLabel("");
//            wp.setIcon(new ImageIcon("images/bg.png"));
            wp.setBounds(0, 0, screenSize.width, screenSize.height);
            panel.add(wp);
            //
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

        public String ParseFileToString(String filePath) throws IOException {
            String line= null;
            BufferedReader reader = null;
            StringBuilder str = new StringBuilder();
            File file = new File(filePath);
            reader = new BufferedReader(new FileReader(file));
            line = reader.readLine();
            while (line != null) {
                str.append(line);
                line = reader.readLine();
            }
            reader.close();
            return str.toString();
        }
    }
}
