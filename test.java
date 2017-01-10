import java.awt.*;
import javax.swing.*;

public class test extends JFrame{
    final String title = "attendance";

    final int width = 400;
    final int height = 300;
//todo
    final int x = 50;
    final int y = 0;
    test() {
         setVisible(true);
         setTitle(title);
         setBounds(50, 0, 400, 300);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         Container cpane = getContentPane();
         JPanel panel = new JPanel();
         JButton abtn = new JButton();
         abtn.setText("aaaaaaaaaa");
        panel.add(abtn, BorderLayout.CENTER); 

         cpane.add(panel, BorderLayout.CENTER);
   //      abtn.activate();
    }
    public static void main(String args[]) {
            test frame = new test();
    }
}