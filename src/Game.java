import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;


public  class Game extends JFrame  {

    GamePanel gamePanel;

    int width,height;
    static int num_enemy=0;
    static int num_friends=0;
    Random random = new Random();
    ArrayList<Enemy> enemies=new ArrayList<>() ;
    ArrayList<Friend> friends=new ArrayList<>();
   ArrayList<Ates> enemyAtes;
   ArrayList<Ates> friendAtes;
   ArrayList<Ates> aircraftAtes;

    public Game(){

        setSize(500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        width=getWidth();
        height=getHeight();
        friendAtes = new ArrayList<>();
        gamePanel = new GamePanel();
       gamePanel.setFocusable(true);
       gamePanel.addKeyListener(new AirCraft());


       add(gamePanel);


        setVisible(true);
    }





     class Enemy extends JPanel implements Runnable{

        int x= (int) (Math.random()*(500)),y=(int) (Math.random()*(500));
        boolean isMove = true;
        boolean isShoot = true;
         Thread thread;
         ReentrantLock lock = new ReentrantLock();
        public Enemy(){
            enemies.add(this);
            num_enemy++;


            thread = new Thread(() -> {
                enemyAtes=new ArrayList<>();
                while (isShoot) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    enemyAtes.add(new Ates(x, y, Color.BLUE, "right"));
                    enemyAtes.add(new Ates(x, y, Color.BLUE, "left"));

                    moveFire();
                }
            });
            thread.start();


        }
         public int getX() {
             return x;
         }

         public void setX(int x) {
             this.x = x;
         }

         public boolean isShoot() {
             return isShoot;
         }

         public void setShoot(boolean shoot) {
             isShoot = shoot;
         }

         public int getY() {
             return y;
         }

         public void setY(int y) {
             this.y = y;
         }

         @Override
        public  void run() {

             while(isMove){
                 int yon = random.nextInt(4)+1;

                 try {
                     Thread.sleep(500);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }

                 if(yon == 1){
                     setX(getX()-10);
                     if(getX()<0)
                         setX(0);
                 }
                 else if(yon == 2){
                     setX(getX()+10);
                     if(getX()+this.getWidth()> width)
                         setX(width-this.getWidth());
                 }
                 else if(yon==3){
                     setY(getY()-10);
                     if(getY()<0)
                         setY(0);
                 }
                 else if(yon == 4){
                     setY(getY()+10);
                     if(getY()+this.getHeight()>height)
                         setY(height-this.getHeight());
                 }
                 repaint();



             }


         }

         public boolean isShoot(Ates ates){
             if(friends!=null) {
                 for (int j = 0; j < friends.size(); j++) {
                     Friend f = friends.get(j);
                     Thread t=new Thread(f);
                     if (f != null) {
                         if (ates.getX() >= f.x && ates.getX() <= f.x + 10 &&
                                 ates.getY() >= f.y && ates.getY() <= f.y + 10) {
                            f.setShoot(false);
                             friends.remove(f);
                             enemyAtes.remove(ates);
                             num_friends--;

                             return true;
                         }
                     }
                 }
             }
             return false;
         }

         public  void moveFire() {

             thread = new Thread(() -> {

                     if (!enemyAtes.isEmpty()) {
                         lock.lock();
                         for (int i = 0; i < enemyAtes.size(); i++) {
                             Ates ates = enemyAtes.get(i);
                             Thread t = new Thread(ates);
                             t.start();
                             if (ates != null) {

                                 if (ates.direction.equals("right")) {
                                     if (ates.getX() > 500) {
                                         enemyAtes.remove(ates);
                                         i--;
                                     } else {
                                         repaint();
                                     }
                                 } else if (ates.direction.equals("left")) {

                                     if (ates.getX() < 0) {
                                         enemyAtes.remove(ates);
                                         i--;
                                     } else {
                                         repaint();
                                     }
                                 }



                                 if (gamePanel.ac != null && ates != null) {
                                     if (ates.getY() >= gamePanel.ac.y && ates.getY() <= gamePanel.ac.y + 10 &&
                                             ates.getX() >= gamePanel.ac.x && ates.getX() <= gamePanel.ac.x + 10) {
                                         gamePanel.ac.setIsAlive(false);

                                         break;

                                     }
                                 }

                                            if(isShoot(ates)){
                                                isShoot=true;
                                             }

                             }

                         }



                         lock.unlock();

                 }
             });
             thread.start();

             if(!gamePanel.ac.isAlive()){
                 JOptionPane.showMessageDialog(this, "Oyunu Kaybettiniz");
                 System.exit(0);
                 return;
             }
             if(num_enemy==0){
                 JOptionPane.showMessageDialog(this,"Oyunu Kazandınız");
                 System.exit(0);
             }


         }


         public void paint(Graphics g){

             super.paint(g);

             if(!enemyAtes.isEmpty()){
                 for (int i = 0; i < enemyAtes.size(); i++) {
                     if(enemyAtes.get(i)!=null) {
                         enemyAtes.get(i).setColor(Color.BLUE);
                         g.setColor(Color.blue);
                         g.fillRect(enemyAtes.get(i).getX(),enemyAtes.get(i).getY(),5,5);
                     }
                 }
             }

             g.setColor(Color.black);
             g.fillRect(x,y,10,10);

         }


         public void start() {
             thread = new Thread(this);
             thread.start();

         }

         public void join() {

             thread=new Thread();
             try {
                 thread.join();
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
     }
//**********************************************************************************************************************
    public class Friend extends JPanel implements Runnable{

        int x= (int) (Math.random()*(500)),y=(int) (Math.random()*(500));
        boolean isMove = true;
        boolean isShoot = true;
        Thread thread;
        ReentrantLock lock = new ReentrantLock();


    public void setShoot(boolean shoot) {
        isShoot = shoot;
    }

    public Friend() {
             friends.add(this);
             num_friends++;

            Thread thread = new Thread(() ->{
                    friendAtes=new ArrayList<>();
                    while (isShoot) {

                        friendAtes.add(new Ates(x, y, Color.magenta, "right"));
                        friendAtes.add(new Ates(x, y, Color.magenta, "left"));

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        moveFire();

                    }

             });
             thread.start();


    }


        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public  void run() {

            while (isMove) {

                int yon = random.nextInt(4) + 1;
                try {
                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (yon == 1) {
                    setX(getX() - 10);
                    if (getX() < 0)
                        setX(0);
                } else if (yon == 2) {
                    setX(getX() + 10);
                    if (getX() + this.getWidth() > width)
                        setX(width - this.getWidth());
                } else if (yon == 3) {
                    setY(getY() - 10);
                    if (getY() < 0)
                        setY(0);
                } else if (yon == 4) {
                    setY(getY() + 10);
                    if (getY() + this.getHeight() > height)
                        setY(height - this.getHeight());

                }


                gamePanel.repaint();



                }






        }
        public  boolean isShoot(Ates ates){
                if(enemies!=null){
                    for(int j=0;j<enemies.size();j++){
                        Enemy e= enemies.get(j);
                        if(e!=null&&ates!=null) {
                            if (ates.getX() >= e.x && ates.getX() <= e.x + 10 &&
                                    ates.getY() >= e.y && ates.getY() <= e.y + 10) {
                                e.setShoot(false);
                                enemies.remove(e);
                                friendAtes.remove(ates);
                                num_enemy--;
                                return true;
                            }
                        }

                    }


        }
        return false;
        }


            public  void moveFire() {

                 thread = new Thread(() -> {

                     lock.lock();
                        if (!friendAtes.isEmpty()){
                            for(int i=0;i<friendAtes.size();i++){
                                Ates ates = friendAtes.get(i);
                                Thread t = new Thread(ates);
                                if(ates!=null) {

                                    if(ates.direction.equals("right")){
                                        if(ates.getX()>500) {
                                            friendAtes.remove(ates);
                                            i--;
                                        }
                                        else {
                                            t.start();
                                            repaint();
                                        }
                                    }
                                    else if(ates.direction.equals("left")){

                                        if(ates.getX()<0) {
                                            friendAtes.remove(ates);
                                            i--;
                                        }
                                        else {
                                            t.start();
                                            repaint();
                                        }
                                    }
                                    if(isShoot(ates)){
                                        repaint();
                                    }

                                }

                            }

                            lock.unlock();

                        }
                    });
                    thread.start();

                    if(num_enemy==0){
                        JOptionPane.showMessageDialog(this,"Oyunu Kazandınız");
                        System.exit(0);
                    }


            }


        public void paint(Graphics g){

            super.paint(g);

            if(!friendAtes.isEmpty()){
            for (int i = 0; i < friendAtes.size(); i++) {
                if(friendAtes.get(i)!=null) {
                    friendAtes.get(i).setColor(Color.magenta);
                    g.setColor(Color.magenta);
                    g.fillRect(friendAtes.get(i).getX(),friendAtes.get(i).getY(),5,5);
                }
            }
            }

            g.setColor(Color.green);
            g.fillRect(getX(),getY(),10,10);


        }




        public void start() {
            if (thread == null || !thread.isAlive()) {
                thread = new Thread(this);
                thread.start();
            }
        }

        public void join() {
            thread=new Thread();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
//**********************************************************************************************************************
     class AirCraft extends JPanel implements Runnable,KeyListener, MouseListener {

         int x = 250, y = 250;
         boolean isPressed = false;
         boolean isAlive = true;

    public boolean isAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive){
        this.isAlive=isAlive;
    }

    Thread acthread;

         public AirCraft() {
             aircraftAtes = new ArrayList<>();
             addMouseListener(this);
             addKeyListener(this);


             acthread=new Thread(this);

         }

         public void setX(int x) {
             this.x = x;
         }

         public void setY(int y) {
             this.y = y;
         }

         @Override
         public void run() {

             while (isPressed) {

                     try {
                         Thread.sleep(100);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }


                     for (int i = 0; i < aircraftAtes.size() - 1; i += 2) {
                         Ates right = aircraftAtes.get(i);
                         Ates left = aircraftAtes.get(i + 1);

                         right.setX(right.getX() + 10);
                         left.setX(left.getX() - 10);

                         if (left.getX() < 0 && right.getX() > 500) {
                             aircraftAtes.remove(left);
                             aircraftAtes.remove(right);
                             i--;
                         }


                         if(enemies!=null){
                         for (int j = 0; j < enemies.size(); j++) {
                             Enemy e = enemies.get(j);
                             if (right.getX() >= e.getX() && right.getX() <= e.getX() + 10 &&
                                     right.getY() >= e.getY() && right.getY() <= e.getY() + 10) {
                                 aircraftAtes.remove(right);
                                 aircraftAtes.remove(left);
                                 e.setShoot(false);
                                 enemies.remove(e);

                                 num_enemy--;

                             }
                             if (left.getX() >= e.getX() && left.getX() <= e.getX() + 10 &&
                                     left.getY() >= e.getY() && left.getY() <= e.getY() + 10) {
                                 aircraftAtes.remove(left);
                                 aircraftAtes.remove(right);
                                 e.setShoot(false);
                                 enemies.remove(e);

                                 num_enemy--;
                             }
                         }
                         }
                     }
                 }
                     repaint();






         }


         @Override
         public void keyTyped(KeyEvent e) {
         }

         @Override
         public void keyPressed(KeyEvent e) {

             int key = e.getKeyCode();



             if (key == KeyEvent.VK_A) {
                 if (x <= 0)
                     setX(0);
                 setX(x - 10);
             } else if (key == KeyEvent.VK_D) {
                 if (x >= width)
                     setX(width);
                 setX(x + 10);
             } else if (key == KeyEvent.VK_W) {
                 if (y <= 0)
                     setY(0);
                 setY(y - 10);
             } else if (key == KeyEvent.VK_S) {
                 if (y >= height)
                     setY(height);
                 setY(y + 10);
             }
             gamePanel.repaint();

         }

         public void paint(Graphics g) {

             super.paint(g);


             for (int i = 0; i < aircraftAtes.size(); i++) {
                 if (isPressed) {
                     aircraftAtes.get(i).setColor(Color.orange);
                     g.setColor(Color.orange);
                     g.fillRect(aircraftAtes.get(i).getX(),aircraftAtes.get(i).getY(),5,5);

                 }
             }

             g.setColor(Color.red);
             g.fillRect(x, y, 10, 10);
         }

         @Override
         public void keyReleased(KeyEvent e) {

         }


         @Override
         public void mouseClicked(MouseEvent e) {

         }

         @Override
         public void mousePressed(MouseEvent e) {
             isPressed = true;


                 Ates right = new Ates(x, y, Color.orange, "right");
                 aircraftAtes.add(right);
                 Ates left = new Ates(x, y, Color.orange, "left");
                 aircraftAtes.add(left);

                 this.start();

         }

         @Override
         public void mouseReleased(MouseEvent e) {

         }

         @Override
         public void mouseEntered(MouseEvent e) {

         }

         @Override
         public void mouseExited(MouseEvent e) {

         }

         public void start() {
             if (acthread == null || !acthread.isAlive()) {
                 acthread = new Thread(this);
                 acthread.start();
             }

         }

         public void join() throws InterruptedException {
             acthread=new Thread();
                 acthread.join();
         }




}
//**********************************************************************************************************************
 class GamePanel extends JPanel{

     AirCraft ac;
    public GamePanel(){
        setPreferredSize(new Dimension(500,500));
        setFocusable(true);
        setLayout(new BorderLayout());
        setBackground(Color.white);
         ac = new AirCraft();

        addKeyListener(ac);
        addMouseListener(ac);



        setVisible(true);

    }

    public void removeEnemy(int index){
        enemies.get(index).setShoot(false);
       enemies.remove(index);
       num_enemy--;

    }

    public void removeFriend(int index){
        friends.get(index).setShoot(false);
       friends.remove(index);
       num_friends--;
    }


    public void paint(Graphics g){

        super.paint(g);



        if(enemies!=null&&friends!=null){
        for(int k=0;k<enemies.size();k++){
            Enemy e = enemies.get(k);
            Thread t=new Thread(e);
            for(int m=0;m<friends.size();m++) {
                Friend f = friends.get(m);
                if (f != null && e != null) {
                    if (e.x < f.x + 10 && e.x + 10 > f.x && e.y < f.y + 10 && e.y + 10 > f.y) {

                        removeEnemy(k);
                        removeFriend(m);

                        if (num_enemy == 0) {
                            JOptionPane.showMessageDialog(this, "Oyunu Kazandınız");
                            System.exit(0);
                            return;
                        }
                    }
                }
            }
            }
        }

        if(enemies!=null){
        for(int k=0;k<enemies.size();k++) {
            Enemy e = enemies.get(k);
            if (ac != null && e != null) {
                if (e.x < ac.x + 10 && e.x + 10 > ac.x && e.y < ac.y + 10 && e.y + 10 > ac.y) {
                    JOptionPane.showMessageDialog(this, "Oyunu Kaybettiniz");
                    System.exit(0);
                    return;
                }
            }
        }
        }


        if(enemies!=null) {
            for (int i = 0; i < enemies.size(); i++) {
                if(enemies.get(i)!=null)
                enemies.get(i).paint(g);
            }
        }

        if(friends!=null) {
            for (int i = 0; i < friends.size(); i++) {
                if(friends.get(i)!=null)
                friends.get(i).paint(g);
            }
        }

        if(ac!=null)
        ac.paint(g);



     }



 }

 }
