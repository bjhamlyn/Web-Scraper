import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class scraper extends JFrame{

    //instance variables
JPanel mainPanel = new JPanel();
JPanel southPanel = new JPanel();
JPanel northPanel = new JPanel();
JTextField tfURL = new JTextField("https://www.dotcom-monitor.com/blog/technical-tools/network-location-ip-addresses/");//web address that contains all of the search variants
JTable jt;
DefaultTableModel tableModel = new DefaultTableModel();
JScrollPane sp;//alowes scroling in center pane
JComboBox cboxRegex;//drop down menue

//buttons for sarting the scrape and for clearing the scramep data from the table data
JButton btRun =  new JButton("Scrape");
JButton btclear = new JButton("Clear");


    //methods
    public void deleteAllRows() {//removes the rows from the data table then Clear is clicked
        for( int i = tableModel.getRowCount() - 1; i >= 0; i-- ) {
            tableModel.removeRow(i);
        }
    }

    public void oneClickClear(ActionEvent f) throws Exception//action event for clear button
    {
        deleteAllRows();
    }
    


// on click actions for the scrape button
public void MyClickEvent(ActionEvent e) throws Exception 
{
  

 URL givenURL = new URL(tfURL.getText());//adds the url in thetfURL box to the givenURL var
 URLConnection connection = givenURL.openConnection();//opens a connection to the givenURL
 InputStream is = connection.getInputStream();//inputStream from the connection is added to var is

 try(BufferedReader br = new BufferedReader( 
    new InputStreamReader(is)//tries to create a bufferedreader from the InputStream
 ))
 {
String line = null;//creates the empty var line
while((line=br.readLine())!=null){
    //matches the pattern of the input from cboxRegex
    Matcher m = Pattern.compile(cboxRegex.getSelectedItem().toString()).matcher(line);
    while (m.find()==true)
    {   //addes the data that matches cboxRegex format into thetableModle
        tableModel.insertRow(tableModel.getRowCount(),new Object[]{tableModel.getRowCount()+1, m.group()});
    }
}
 }
  
  
}
    

    //constructors
    public scraper()
    {
        super("scraping the internet");
        mainPanel.setLayout(new BorderLayout());

/////////////////NORTH////////////
//actionListener for btclear
btclear.addActionListener(f -> {
        try {
            oneClickClear(f);
        } catch (Exception f1) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(rootPane, "Clear table button error");
        }
   
    
});

northPanel.add(tfURL);//adds the textfield tfURL to the north panel
northPanel.add(btclear);//adds the clear button to the north panel
mainPanel.add(northPanel, BorderLayout.NORTH);//adds the northpanel to the mainpanel NORTH borderLayout
////////////////CENTER///////////


tableModel.addColumn("#");// adds the column headder for the first column
tableModel.addColumn("Results");// adds the column headder for the second column
jt = new JTable(tableModel);//loads jt with thetableModle
sp = new JScrollPane(jt);//loads jt in to the scroling pane sp

mainPanel.add(sp, BorderLayout.CENTER);//adds the scroling pane sp to the CENTER borderlayout of the mainPanel

////////////////EAST///////////
//not used
////////////////WEST///////////
//not used
////////////////SOUTH///////////
// create a string array with the search variables for the scraper
String[] options = {
    "[0-9]+",//numbers
    "[0-9]{3}-[0-9]{3}-[0-9]{4}",//phone numbers
    "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9_.+-]+\\.[.com|.edu|.org]",//emails
    "<[a-zA-Z_0-9]+>",//internet links
    "(([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}"+"([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])",//ip

    
    };
    //adds the options array to the dropdown combobox cboxRegex
cboxRegex = new JComboBox(options);
//add actionListener for the scrape button btRun
btRun.addActionListener(e -> {
    try {
        MyClickEvent(e);
    } catch (Exception e1) {
        // TODO Auto-generated catch block
       JOptionPane.showMessageDialog(rootPane, "Bad URL");
    }
});
southPanel.add(cboxRegex);//adds the dropdown list cboxRegex into the south pannel
southPanel.add(btRun);//adds the "scrape" button into the south pannel
mainPanel.add(southPanel, BorderLayout.SOUTH);//names the SOUTH borderLayout southPanel and addes it to the main Panel

    
        //setLayout(new BorderLayout());//sets layout
        add(mainPanel);//adds the mainPanel
        pack();//auto sizes the pannel
        setLocationRelativeTo(null);//sets the panel location to the center of the primary screen
        //setSize(1800,600);
        setVisible(true);//makes the panels visable
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//ends the program when closing out


    }
    
}
