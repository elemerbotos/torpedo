package com.epam.training.jjp.display;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserInterface extends Application implements Runnable {

	private class CustomEventHandler<T extends Event> implements EventHandler<T> {
		protected Thread gameThread;
		public CustomEventHandler(Thread gameThread) {
			this.gameThread = gameThread;
		}
		@Override
		public void handle(T arg0) {
	    	if(gameThread==null) System.out.println("GAME THREAD IS NULL2");
	    	else System.out.println("GAME THREAD IS NOT NULL");
	    	notifyAll();
		}

    }

	
	private int size;
	private String[][] map;
	protected Thread gameThread;
	
	public void setSize(final int size) {
		this.size = size;
	}
	
	@Override
	public void run() {
		
		initMap();
		launch();
	}

	private void initMap() {
		map = new String[size][size];
		for(int i = 0 ; i < size ; ++i) {
			for(int j = 0 ; j < size ; ++j) {
				map[i][j] = ".";
			}
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("JavaFX Welcome");
        
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("My torpedo game! ");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);
		
		Button btn = new Button("Start");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.TOP_LEFT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 4);
		
		btn.setOnAction(new CustomEventHandler<ActionEvent>(gameThread));
		
		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);
		
        primaryStage.show();
      
	}

	public void setHit(boolean isThereHit, int x, int y) {
		if(isThereHit) {
			map[x][y] = "O";
		} else {
			map[x][y] = "X";
		}
	}

	public void setGameThread(Thread gameThread) {
		if(gameThread == null) System.out.println("GAME THREAD IS NULL");
		else System.out.println("GAME THREAD IS NOT NULL");
		this.gameThread = gameThread;
		System.out.println("game thread set: " + this.gameThread.getId());
		
	}

}
