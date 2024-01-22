import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//import Main.Task;

public class Main{
    private static ArrayList<Task> tasks = new ArrayList<>();

   //add the reader here.


  
  
    private static DefaultListModel<String> listModel = new DefaultListModel<>();
    private static JList<String> taskList;

    private static void GUI() {
        String file = "tasks.csv";
        BufferedReader reader = null;
        String line = "";
        
        try {
          reader = new BufferedReader(new FileReader(file));
          while((line = reader.readLine()) != null) {
            String[] row = line.split(",");
            String[] date = row[1].split(" ");
            String month = "";
            switch (date[1]){
              case "Jan":
                month = "01";
                break;
              case "Feb":
                month = "02";
                break;
              case "Mar":
                month = "03";
                break;
              case "Apr":
                month = "04";
                break;
              case "May":
                month = "05";
                break;
              case "JUn":
                month = "06";
                break;
              case "Jul":
                month = "07";
                break;
              case "Aug":
                month = "08";
                break;
              case "Sep":
                month = "09";
                break;
              case "Oct":
                month = "10";
                break;
              case "Nov":
                month = "11";
                break;
              case "Dec":
                month = "12";
                break;
            }
            addTask(row[0],date[5]+"-"+month+"-"+date[2]); 
            updateTaskList();
          }
        }catch(Exception e){
          e.printStackTrace();
        }






      
      
        JFrame jFrame = new JFrame("Task Manager");
        jFrame.setLayout(new BorderLayout());
        jFrame.setSize(550, 400);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Task Manager");
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        label.setBorder(border);
        label.setPreferredSize(new Dimension(150, 50));
        label.setHorizontalAlignment(JLabel.CENTER);

        taskList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setPreferredSize(new Dimension(500, 75));

        JTextField taskNameField = new JTextField(15);
        JTextField dateField = new JTextField(10);
        JButton addButton = new JButton("Add Task");
        JButton deleteButton = new JButton("Delete Task");

        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
              try{
                addTask(taskNameField.getText(), dateField.getText());
                updateTaskList();
                taskNameField.setText("");
                dateField.setText("");
                String texta = "";
              for(Task item: tasks){
                texta = texta.concat(item.name.toString() + ","+item.dueDate.toString() +"\n");
              }
                System.out.println(texta);
                FileWriter writer = new FileWriter("tasks.csv");
                writer.write(texta);
                writer.close();
              }catch (IOException ex){
                ex.printStackTrace();
              }
            }
        });
      ActionListener closeButtonAction= new ActionListener() {

          public void actionPerformed(ActionEvent e){
            try{
              int selectedIndex = taskList.getSelectedIndex();
              if (selectedIndex != -1) {
                  tasks.remove(selectedIndex);
                  updateTaskList();
              }
          String text = "";
          for(Task item: tasks){
            text = text.concat(item.name.toString() + ","+item.dueDate.toString() + "\n");
          }
            FileWriter writer = new FileWriter("tasks.csv");
            writer.write(text);
            writer.close();
            } catch(IOException ex){
              ex.printStackTrace();
            }
          }
      };
        deleteButton.addActionListener(closeButtonAction);
       

        inputPanel.add(new JLabel("Task Name: "));
        inputPanel.add(taskNameField);
        inputPanel.add(new JLabel("Due Date (yyyy-MM-dd): "));
        inputPanel.add(dateField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        jFrame.add(mainPanel, BorderLayout.CENTER);
        jFrame.add(inputPanel, BorderLayout.SOUTH);

        jFrame.setVisible(true);
    }

    private static void addTask(String taskName, String dateString) {
        try {
            Date dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            Task task = new Task(taskName, dueDate);
            tasks.add(task);
            Collections.sort(tasks);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    private static void updateTaskList() {
        listModel.clear();
        for (Task task : tasks) {
            listModel.addElement(task.toString());
        }
    }

    public static void main(String[] args) {
        GUI();
    }

    private static class Task implements Comparable<Task> {
        private String name;
        private Date dueDate;

        public Task(String name, Date dueDate) {
            this.name = name;
            this.dueDate = dueDate;
        }


        public int compareTo(Task otherTask) {
            return this.dueDate.compareTo(otherTask.dueDate);
        }


        public String toString() {
            return name + " - Due: " + new SimpleDateFormat("yyyy-MM-dd").format(dueDate);
        }
    }
}