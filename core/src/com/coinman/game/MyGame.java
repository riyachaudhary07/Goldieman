package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class MyGame extends ApplicationAdapter {
	SpriteBatch batch;
    Texture background;
    int manstate;
    Texture[] man;
    int pause=0;
    float gravity=0.2f;
    float velocity=0;
    int manY=0;
    Random random;
    BitmapFont font;
    Rectangle manRectangle;
    int score=0;
    Texture dizzy;
    int Gamestate=0;
    ArrayList<Integer> coinXs=new ArrayList<Integer>();
    ArrayList<Integer> coinYs=new ArrayList<Integer>();
    ArrayList<Rectangle> coinRectangle=new ArrayList<Rectangle>();
    ArrayList<Rectangle> bombRectangle=new ArrayList<Rectangle>();
    Texture coin;
int coinCount;
    ArrayList<Integer> bombXs=new ArrayList<Integer>();
    ArrayList<Integer> bombYs=new ArrayList<Integer>();
    Texture bomb;
    int bombCount;
    private Music bg;
    private Music draw1;
	@Override
	public void create () {
		batch = new SpriteBatch();
        bg= Gdx.audio.newMusic(Gdx.files.internal("mario.mp3"));
        draw1=Gdx.audio.newMusic(Gdx.files.internal("draw.mp3"));
        bg.setLooping(true);
        bg.setVolume(0.2f);
       
		background=new Texture("bg.png");
		man=new Texture[4];
		man[0]=new Texture("frame-1.png");
        man[1]=new Texture("frame-2.png");

        man[2]=new Texture("frame-3.png");
        man[3]=new Texture("frame-4.png");
        manY=Gdx.graphics.getHeight()/2;
        coin=new Texture("coin.png");
        bomb=new Texture("bomb.png");
        random=new Random();
        dizzy=new Texture("dizzy-1.png");
        manRectangle=new Rectangle();
        font=new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
    }
public void makeCoin(){
	    float height=random.nextFloat() * Gdx.graphics.getHeight();
	    coinYs.add((int)height);
	    coinXs.add(Gdx.graphics.getWidth());

}
    public void makeBomb(){
        float height=random.nextFloat() * Gdx.graphics.getHeight();
        bombYs.add((int)height);
        bombXs.add(Gdx.graphics.getWidth());

    }
	@Override
	public void render () {
batch.begin();
batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
if(Gamestate==1){
    if(coinCount<100){
        coinCount++;
    }
    else{
        coinCount=0;
        makeCoin();
    }
    coinRectangle.clear();
    for(int i=0;i<coinXs.size();i++){
        batch.draw(coin,coinXs.get(i),coinYs.get(i));
        coinXs.set(i,coinXs.get(i)-4);


        coinRectangle.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
    }

    if(bombCount<250){
        bombCount++;
    }
    else{
        bombCount=0;
        makeBomb();
    }
    bombRectangle.clear();
    for(int i=0;i<bombXs.size();i++){
        batch.draw(bomb,bombXs.get(i),bombYs.get(i));
        bombXs.set(i,bombXs.get(i)-8);
        bombRectangle.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
        if(Gdx.input.justTouched()){
            velocity=-10;
            bg.play();
        }
        if(pause<8){
            pause++;
        }else {

            pause = 0;
            if (manstate < 3) {
                manstate++;
            } else {
                manstate = 0;
            }
        }
        velocity+=gravity;
        manY-=velocity;
        if(manY<=0){
            manY=0;
        }
    }

}else if(Gamestate==0){
    if(Gdx.input.justTouched()){
        Gamestate=1;
    }
}
else if(Gamestate==2){
    bg.stop();
    draw1.setVolume(0.2f);
    draw1.play();
    if(Gdx.input.justTouched()){
        Gamestate=1;
        manY=Gdx.graphics.getHeight()/2;
        score=0;
        velocity=0;
        coinXs.clear();
        coinYs.clear();
        coinRectangle.clear();
        coinCount=0;
        bombXs.clear();
        bombYs.clear();
        bombRectangle.clear();
        bombCount=0;
    }
}

if(Gamestate==2){
    batch.draw(dizzy,Gdx.graphics.getWidth()/2-man[0].getWidth()/2,manY);
}
else{
batch.draw(man[manstate],Gdx.graphics.getWidth()/2-man[0].getWidth()/2,manY);}
manRectangle.set(Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY,man[manstate].getWidth(),man[manstate].getHeight());
for(int i=0;i<coinRectangle.size();i++){
    if(Intersector.overlaps(manRectangle,coinRectangle.get(i))){
       score++;
       coinRectangle.remove(i);
       coinXs.remove(i);
       coinYs.remove(i);
       break;
    }
}
        for(int i=0;i<bombRectangle.size();i++){
            if(Intersector.overlaps(manRectangle,bombRectangle.get(i))){
                Gdx.app.log("Bomb!","Collision!");
                Gamestate=2;
            }}
        font.draw(batch,String.valueOf(score),100,200);
batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
