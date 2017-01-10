
import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.xml.bind.DatatypeConverter;

public class firica implements Runnable {
    private CardTerminal terminal;
    private Card card;
    private static byte[] readUID = { (byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x04 };    //IDmを読むための？
    public String method;
    firica(String m) {
        method = m;
        try {
            terminal = TerminalFactory.getDefault().terminals().list().get(0);    //一台目を決め打ち
        } catch (CardException e) {
            System.err.println("読み取り機を取得できませんでした");
            System.exit(-1);
        }
    }
    public void seturl(String url){
        method = url;
    }
    public String readIDm() throws CardException{
        try {
            card = terminal.connect("*");
            ResponseAPDU response = card.getBasicChannel().transmit(new CommandAPDU(readUID));
            return DatatypeConverter.printHexBinary(response.getData());    //byte[]を文字列に変換
        } finally {
            card.disconnect(false);    //例外が発生してもとりあえず接続を切る
        }
    }
    @Override
    public void run() {
        //カードが置かれて、取られるまでの一連をひとまとめにしているが、感度により、一つのカードについて短期間に連続して起こる可能性があることを想定すべき
        while(true) {
            try {
                terminal.waitForCardPresent(0);

                send(readIDm());

                terminal.waitForCardAbsent(0);
            } catch (CardException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        //無限ループスレッドでカードが置かれた時にIDmを表示するアプリケーション
        Thread thread = new Thread(new firica(args[0]));
        thread.start();
    }
    public void send(String id){
        Post p = new Post("localhost", method);
        p.execute(id);
    }
}