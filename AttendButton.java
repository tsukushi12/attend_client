import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.xml.bind.DatatypeConverter;

public class AttendButton extends JButton implements ActionListener {
    String text;
    firica thread;
    final String attend = "attend";
    final String create = "create";

    AttendButton() {
        text = attend;


        setText("create");

    }

    public void activate() {
        addActionListener(this);
        thread = new firica("create");
        
        thread.start();
    }

    public void actionPerformed(ActionEvent e) {
        if (text == attend) {
            setT(create);
            thread.seturl("create");
        } else {
            setT(attend);
            thread.seturl("attend");
        }
    }

    private void setT(String str) {
        text = str;
        setText(str);
    }

    public class firica extends Thread {
        private CardTerminal terminal;
        private Card card;
        private byte[] readUID = { (byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x04 }; //IDmを読むための？
        public String method;

        firica(String m) {
            this.method = m;
            try {
                this.terminal = TerminalFactory.getDefault().terminals().list().get(0); //一台目を決め打ち
            } catch (CardException e) {
                System.err.println("読み取り機を取得できませんでした");
                System.exit(-1);
            }
        }

        public void seturl(String url) {
            this.method = url;
        }

        public String readIDm() throws CardException {
            try {
                this.card = terminal.connect("*");
                ResponseAPDU response = card.getBasicChannel().transmit(new CommandAPDU(readUID));
                return DatatypeConverter.printHexBinary(response.getData()); //byte[]を文字列に変換
            } finally {
                this.card.disconnect(false); //例外が発生してもとりあえず接続を切る
            }
        }

        @Override
        public void run() {
            //カードが置かれて、取られるまでの一連をひとまとめにしているが、感度により、一つのカードについて短期間に連続して起こる可能性があることを想定すべき
            while (true) {
                try {
                    this.terminal.waitForCardPresent(0);
                    String res = this.send(readIDm());
                    this.terminal.waitForCardAbsent(0);
                    SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                        setText(res);
                                }
                    });
                } catch (CardException e) {
                    e.printStackTrace();
                }
            }
        }

        public String send(String id) {
            Post p = new Post("localhost", method);
            return p.execute(id);
        }
    }

}