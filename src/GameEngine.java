import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;


class ExtraCharacter extends Character {
    public ExtraCharacter(String fname, int n_frames, int[] ds) {
        super(fname, n_frames, ds);
    }

    protected void updateCurFrameIdx(long elapsedTime) {
        long t = elapsedTime + lastRemainTime;
        while (t>durs[curFrame]) {
            t -= durs[curFrame];
            curFrame++;
            if (curFrame>=durs.length) {
                curFrame = -1;
                valid = false;
                break;
            }
        }
        lastRemainTime = t;
    }
}

public class GameEngine extends Application {
    Character ship;
    Character bkg;
    public AnchorPane pane;
    ArrayList<Character> aliens = new ArrayList<Character>();
    ArrayList<Character> bullets = new ArrayList<Character>();
    ArrayList<Character> flames = new ArrayList<Character>();
    ArrayList<Character> bbb = new ArrayList<Character>();
    ArrayList<Character> life = new ArrayList<Character>();
    ArrayList<Character> temp = new ArrayList<Character>();
    double accSpeed = 0.0;
    final double BASE_SPEED = 50;
    long lastTime, curTime, elapsedTime;
    int count = 0;
    Label score = new Label();
    Label scorename = new Label("Score :");
    Label Life = new Label("Life : ");
    public Label t = new Label("Time : 60");
    public Label Final = new Label("Game Over");
    public Label FinalScore = new Label("FinalScore : ");
    Button restart = new Button("Restart");
    boolean check = false;
    boolean checkfire = false;
    final URL gunresource = getClass().getResource("gun.mp3");
    final Media gunmedia = new Media(gunresource.toString());
    final MediaPlayer gunmediaPlayer = new MediaPlayer(gunmedia);
    final URL boomresource = getClass().getResource("boom.mp3");
    final Media boommedia = new Media(boomresource.toString());
    final MediaPlayer boommediaPlayer = new MediaPlayer(boommedia);
    int timecount = 60000;
    int temptime, tempt = 0;
    int I = 0;
    int lifecount = 2;
    
    
    
    public void updateAll(long elapsedTime) {
        double w = bkg.getFrame().getWidth();
        bkg.update(elapsedTime);
        ship.update(elapsedTime);
        for (Character s: aliens) {
            s.update(elapsedTime);
            double x = s.getX();
            if (x>=(w-s.getFrame().getWidth()) || x<=0)
                s.setVx(-s.getVx());
            if(checkfire == true){
            	Character bullets1 = new Character("bullets", 1, new int[] { 10 });
            	Character bullets2 = new Character("bullets", 1, new int[] { 10 });
            	Character bullets3 = new Character("bullets", 1, new int[] { 10 });
            	bullets1.posProperty().addListener((ev) ->checkShip(bullets1));
            	bullets1.setBnd(0, bkg.getFrame().getWidth(), 0, bkg.getFrame().getHeight());
            	bullets1.setVy(BASE_SPEED * 8);
            	int ttt1;
            	ttt1 = (int)((Math.random()*aliens.size()));
            	double x1 = aliens.get(ttt1).getX();
            	double y1 = aliens.get(ttt1).getY();
            	bullets1.setPos(x1 + aliens.get(ttt1).getWidth() / 2, y1 - bullets1.getHeight());
            	pane.getChildren().add(bullets1.getView());
				bbb.add(bullets1);
				bullets2.posProperty().addListener((ev) ->checkShip(bullets2));
            	bullets2.setBnd(0, bkg.getFrame().getWidth(), 0, bkg.getFrame().getHeight());
            	bullets2.setVy(BASE_SPEED * 8);
            	int ttt2;
            	ttt2 = (int)((Math.random()*aliens.size()));
            	while(ttt2==ttt1)
            		ttt2 = (int)((Math.random()*aliens.size()));
            	double x2 = aliens.get(ttt2).getX();
            	double y2 = aliens.get(ttt2).getY();
            	bullets2.setPos(x2 + aliens.get(ttt2).getWidth() / 2, y2 - bullets2.getHeight());
            	pane.getChildren().add(bullets2.getView());
				bbb.add(bullets2);
				bullets3.posProperty().addListener((ev) ->checkShip(bullets3));
            	bullets3.setBnd(0, bkg.getFrame().getWidth(), 0, bkg.getFrame().getHeight());
            	bullets3.setVy(BASE_SPEED * 8);
            	int ttt3;
            	ttt3 = (int)((Math.random()*aliens.size()));
            	while(ttt3==ttt1||ttt3==ttt2)
            		ttt3 = (int)((Math.random()*aliens.size()));
            	double x3 = aliens.get(ttt3).getX();
            	double y3 = aliens.get(ttt3).getY();
            	bullets3.setPos(x3 + aliens.get(ttt3).getWidth() / 2, y3 - bullets3.getHeight());
            	pane.getChildren().add(bullets3.getView());
				bbb.add(bullets3);
				checkfire = false;
            }
        }
        for (Character s: bullets) {
            s.update(elapsedTime);
        }
        for (Character s: flames) {
            if (s.curFrame>=0)
                s.update(elapsedTime);
        }
        for (Character s: bbb) {
            s.update(elapsedTime);
        }
    }

	AnimationTimer tm = new AnimationTimer(){
        @Override
        public void handle(long arg0) {
            curTime=System.currentTimeMillis();
            elapsedTime=curTime-lastTime;
            updateAll(elapsedTime);
            reclaimCharacters();
            showCharacters();
            lastTime = curTime;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            I++;
            if(check == true)
            {
            	timecount = timecount - 10;
            	if(timecount%1000==0)
            	{
            		temptime = timecount/1000;
            		t.setText("Time : " + temptime);
            		checkfire = true;
            	}
            	if(timecount==0||lifecount==-1)
            	{
            		check=false;
            		checkfire = false;
            		pane.getChildren().add(restart);
            		pane.getChildren().add(Final);
            		pane.getChildren().add(FinalScore);
            		restart.setFont(new Font("Arial", 30));
            		Final.setFont(new Font("Arial", 30));
            		FinalScore.setFont(new Font("Arial", 30));
            		pane.setBottomAnchor(restart, (double) 200);
                    pane.setLeftAnchor(restart, 450.0);
                    pane.setBottomAnchor(FinalScore, (double) 250);
                    pane.setLeftAnchor(FinalScore, 425.0);
                    pane.setBottomAnchor(Final, (double) 300);
                    pane.setLeftAnchor(Final, 440.0);
                    FinalScore.setText("Final Score :" + count);
                    restart.setOnAction(e->{
                    	count = 0;
                    	timecount = 60000;
                    	check = true;
                    	life.get(0).valid=true;
                    	life.get(1).valid=true;
                    	life.get(2).valid=true;
                    	pane.getChildren().add(life.get(0).iv);
                    	pane.getChildren().add(life.get(1).iv);
                    	pane.getChildren().add(life.get(2).iv);
                    	pane.requestFocus();
                    	pane.getChildren().remove(restart);
                    	pane.getChildren().remove(FinalScore);
                    	pane.getChildren().remove(Final);
                    	score.setText("0");
                    	t.setText("Time : 60");
                    	tempt = 0;
                    	I = 0;
                    	temp.clear();
                    	checkfire = false;
                    	lifecount = 2;
                    });
            	}
            }
        }        
    };    

    private void initCharacters() {
        bkg = new Character("bg_image", 1, new int[]{10});
        bkg.setBnd(0, bkg.getFrame().getWidth(), 0, bkg.getFrame().getHeight());
        pane.getChildren().add(bkg.getView());
        ship = new Character("spaceship", 1, new int[]{10});
        ship.setBnd(0, bkg.getFrame().getWidth(), 0, bkg.getFrame().getHeight());
        ship.setVx(BASE_SPEED);
        ship.setPos(ship.getX(), bkg.getFrame().getHeight()-ship.getFrame().getHeight());
        pane.getChildren().add(ship.getView());
        for (int n=0; n<10; n++) {
            int[] durs = new int[15];
            for (int i=0;i<durs.length;i++)
                durs[i] = 50+(int)(50*Math.random()+1);
            Character s = new Character("SkeltonFrame", 15, durs);
            s.setBnd(0, bkg.getFrame().getWidth(), 0, bkg.getFrame().getHeight());
            s.setVx(BASE_SPEED*(Math.random()+1));
            s.setPos(n*80,Math.random()*300+50 );
            aliens.add(s);            
            pane.getChildren().add(s.getView());
        }
        for(int i=0;i<3;i++)
        {
        	Character s = new Character("life", 1, new int[]{10});
        	s.setBnd(0, bkg.getFrame().getWidth(), 0, bkg.getFrame().getHeight());
        	//s.setPos(, );
        	s.iv.setFitHeight(30);
        	s.iv.setFitWidth(30);
        	life.add(s);
        	pane.getChildren().add(s.getView());
        	pane.setLeftAnchor(s.getView(), 270.0+40*i);
        }
    }

    private void checkBullet(Character b) {
        if (b.getY()<0) 
            b.valid = false;
        else if (aliens.size()>0) {
            for (Iterator<Character> it=aliens.iterator(); it.hasNext();) {
                Character a = it.next();
                if (a.collideWith(b)) {
                    double x = a.getX()+a.getWidth()/2;
                    double y = a.getY()+a.getHeight()/2;
                    a.valid = b.valid = false;
                    Character flame = new ExtraCharacter("ExplodeFrame", 9, new int[]{100, 100, 100, 100, 100, 100, 100, 100, 100}); 
                    flame.setBnd(0, bkg.getFrame().getWidth(), 0, bkg.getFrame().getHeight());
                    flame.setPos(x, y);
                    pane.getChildren().add(flame.getView());
                    flames.add(flame);      
                    count++;
                    score.setText(String.valueOf(count));
                    boommediaPlayer.stop();
                	boommediaPlayer.play();
                }                    
            }
        }
    }
    
    private void checkShip(Character b) {
        if (b.getY()>bkg.getFrame().getHeight()) 
            b.valid = false;
        else if (ship != null) {
                if (ship.collideWith(b)) {
                    double x = ship.getX()+ship.getWidth()/2;
                    double y = ship.getY()+ship.getHeight()/2;
                    b.valid = false;
                    Character flame = new ExtraCharacter("ExplodeFrame", 9, new int[]{100, 100, 100, 100, 100, 100, 100, 100, 100}); 
                    flame.setBnd(0, bkg.getFrame().getWidth(), 0, bkg.getFrame().getHeight());
                    flame.setPos(x, y);
                    pane.getChildren().add(flame.getView());
                    flames.add(flame);      
                    boommediaPlayer.stop();
                	boommediaPlayer.play();
                	life.get(lifecount).valid=false;
                	lifecount--;
                }                    
            }
    }

    private void reclaimCharacters() {
    	
        for (Iterator<Character> it=bullets.iterator(); it.hasNext();) {
            Character b = it.next();
            if (!b.valid) {
                pane.getChildren().remove(b.iv);
                it.remove();
            }
        }
        
        for (Iterator<Character> it=bbb.iterator(); it.hasNext();) {
            Character b = it.next();
            if (!b.valid) {
                pane.getChildren().remove(b.iv);
                it.remove();
            }
        }
        
        for (Iterator<Character> it=aliens.iterator(); it.hasNext();) {
            Character a = it.next();
            if (!a.valid) {
            	
                pane.getChildren().remove(a.iv);
                //temp = it;
                it.remove();
                temp.add(a);
                tempt = I;
            }
            
            
        }
        for (Iterator<Character> it=flames.iterator(); it.hasNext();) {
            Character f = it.next();
            if (!f.valid) {
                pane.getChildren().remove(f.iv);
                it.remove();
            }
        }
        for (Iterator<Character> it=life.iterator(); it.hasNext();) {
            Character f = it.next();
            if (!f.valid) {
                pane.getChildren().remove(f.iv);
            }
        }
        
    }
    
    private void showCharacters()
    {
    	if((tempt+100)==I){
    		if(temp.size()!=0) {
    			for(int i=0;i<temp.size();i++)
    			if(!temp.get(i).valid)
    			{
    				temp.get(i).valid = true;
    				aliens.add(temp.get(i));
    				pane.getChildren().add(temp.get(i).iv);
    				
    			}
    		}
    	}
    }

    private void keyStrike(KeyEvent e) {
    	if(check == true){
    		accSpeed = BASE_SPEED*2.0;
    		if (e.getCode() == KeyCode.LEFT && ship.getVx()<0) {
    			ship.setVx(ship.getVx()-accSpeed);
    		}
    		else if (e.getCode()==KeyCode.RIGHT && ship.getVx()>0) {
    			ship.setVx(ship.getVx()+accSpeed);
    		}
    		else if (e.getCode() == KeyCode.LEFT)
    			ship.setVx(-BASE_SPEED);
    		else if (e.getCode() == KeyCode.RIGHT)
    			ship.setVx(BASE_SPEED);
    		if (e.getCode() == KeyCode.SPACE) {
    			
    			if (bullets.size() < 3) {
    				gunmediaPlayer.stop();
        			gunmediaPlayer.play();
    				Character bullet = new Character("bullet", 1, new int[] { 10 });
    				bullet.posProperty().addListener((ev) ->checkBullet(bullet));
    				bullet.setBnd(0, bkg.getFrame().getWidth(), 0, bkg.getFrame().getHeight());
    				bullet.setVy(-BASE_SPEED * 8);
    				double x = ship.getX();
    				double y = ship.getY();
    				bullet.setPos(x + ship.getWidth() / 2, y - bullet.getHeight());
    				pane.getChildren().add(bullet.getView());
    				bullets.add(bullet);
    				
    			}
    			
    		}
    	}
    }    

    @Override
    public void start(Stage primaryStage) {
        pane = new AnchorPane();
        pane.setOnKeyPressed(e->keyStrike(e));
        initCharacters();
        Button btn = new Button("Start");
        pane.getChildren().add(btn);
        pane.setBottomAnchor(btn, (double) 300);
        pane.setLeftAnchor(btn, 450.0);
        btn.setFont(new Font("Arial", 30));
        btn.setOnAction(e->{
        	//pane.setOnKeyPressed(s->keyStrike(s));
        	check = true;
        	pane.requestFocus();
        	pane.getChildren().remove(btn);
        });
        Life.setFont(new Font("Arial", 30));
        scorename.setFont(new Font("Arial", 30));
        score.setFont(new Font("Arial", 30));
        score.setText(String.valueOf(count));
        pane.getChildren().add(scorename);
        pane.getChildren().add(score);
        pane.getChildren().add(Life);
        pane.setLeftAnchor(score, 100.0);
        pane.setRightAnchor(t, 10.0);
        pane.setLeftAnchor(Life, 200.0);
        t.setFont(new Font("Arial", 30));
        t.setText("Time : 60");
        pane.getChildren().add(t);
        Scene scene = new Scene(pane, bkg.getWidth(), bkg.getHeight());
        primaryStage.setScene(scene);
        primaryStage.show();
        //pane.requestFocus();
        lastTime = System.currentTimeMillis();
        tm.start();
    }

    public static void main(String[] args) {
        launch();
    }
}