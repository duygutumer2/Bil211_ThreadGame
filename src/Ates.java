import java.awt.*;
import java.util.ArrayList;

public class Ates implements Runnable {
    int x;
    ArrayList<Ates> atesler;


    public void setColor(Color color) {
        this.color = color;
    }

    int y;
    Color color;
    String direction;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }


    public Ates(int x, int y,Color color,String direction){
        this.x=x;
        this.y=y;
        this.color=color;
        this.direction=direction;
        atesler =new ArrayList<>();
    }


    @Override
    public void run() {


            try {
                if (!Thread.interrupted()) {
                    Thread.sleep(100);
                    if (this.direction.equals("left")) {
                        this.setX(getX() - 10);
                    } else {
                        this.setX(getX() + 10);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }

}
