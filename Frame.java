import java.awt.*;
import javax.swing.*;

public class Frame extends JFrame{
    final String title = "attendance";

    final int width = 400;
    final int height = 300;
//todo
    final int x = 50;
    final int y = 0;
    Frame() {
         setVisible(true);
         setTitle(title);
         setBounds(50, 0, 400, 300);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    public static void main(String args[]) {
            Frame frame = new Frame();
    }
}