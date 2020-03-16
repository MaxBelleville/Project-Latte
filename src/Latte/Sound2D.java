package Latte;

import java.io.File;
import java.util.ArrayList;

import com.sun.javafx.application.PlatformImpl;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class Sound2D {
	private ArrayList<String>queue = new ArrayList<String>();
	private MediaPlayer player;
	private int item=0;
	private boolean randomize=false;
	private boolean canLoop =false;
	public Sound2D(String path) {
		PlatformImpl.startup(()->{});
		queue.add(path);
		Media hit = new Media(new File(path).toURI().toString());
		player = new MediaPlayer(hit);
	}
	public void randomize() {
		randomize=true;
		item=(int)(Math.random()*queue.size());
		Media hit = new Media(new File(queue.get(item)).toURI().toString());
		player = new MediaPlayer(hit);
	}
	public void addToQueue(String path) {
		queue.add(path);
	}
	public void stop() {
		player.stop();
	}
	public void play() {
		player.play();
	}
	public void setPos(double ms) {
		Duration d = Duration.millis(ms);
		player.seek(d);
	}
	public void setSpeed(double rate) {
		player.setRate(rate);
	}
	public void mute(boolean toggle) {
		player.setMute(toggle);
	}
	public void setStart(double ms) {
		Duration d = Duration.millis(ms);
		player.setStartTime(d);
	}
	public double getStart() {
		return player.getStartTime().toMillis();
	}
	public void setEnd(double ms) {
		Duration d = Duration.millis(ms);
		player.setStopTime(d);
	}
	public void updateQueue() {
		if(hasStopped()) {
			if(!canLoop&&queue.size()>1) {
			queue.remove(item);
			if(randomize)item=(int)(Math.random()*queue.size());
			Media media = new Media(new File(queue.get(item)).toURI().toString());
			player = new MediaPlayer(media);
			player.play();
			}
			else if(canLoop) {
				player.seek(Duration.millis(getStart()));
				canLoop=false;
			}
		}
	}
	public void loop() {
		canLoop=true;
	}
	public boolean queueEmpty() {
		return queue.isEmpty();
	}
	public void pause() {
		player.pause();
	}
	public boolean isPlaying() {
		return player.getStatus()==Status.PLAYING;
	}
	public boolean hasPaused() {
		return player.getStatus()==Status.PAUSED;
	}
	public boolean hasStopped() {
		return player.getStatus()==Status.STOPPED||getLocation()>=getEnd();
	}
	public double getEnd() {
		return player.getStopTime().toMillis();
	}
	public double getLocation() {
		return player.getCurrentTime().toMillis();
	}
	public void setVolume(double volume) {
		player.setVolume(volume);
	}
}