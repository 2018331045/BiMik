package Gui;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
//import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import logic.ViewData;
import logic.events.InputEventListener;
import logic.events.MoveEvent;
import logic.events.EventSource;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import logic.DownData;
import static logic.MatrixOperation.lol;
import logic.events.EventType;
import logic.events.InputEventListener;





public class GuiController implements Initializable
{
    
   public static int ami=0; 
    private static final int BRICK_SIZE = 20;
    Timeline timeLine;
    private Rectangle[][] rectangles;
   private InputEventListener eventLister;
   private Rectangle[][] displayMatrix;
   private BooleanProperty paused = new SimpleBooleanProperty();
    private BooleanProperty isGameOver = new SimpleBooleanProperty();
    
    
    @FXML
	private GridPane gamePanel;
    @FXML
	private GridPane brickPanel;
    @FXML
	private Group groupNotification;
	
	
    
    @FXML
	private Text scoreValue;
    
    @FXML
	private Text hs;
    public Text sv;
    
     @FXML
        private GridPane nextBrick;
    
     @FXML
	private ToggleButton pauseButton;
     
     @FXML
	private GameOverPanel gameOverPanel;
     
     
     
    public void initGameView(int[][] boardMatrix,ViewData viewData) {
		displayMatrix= new Rectangle[boardMatrix.length][boardMatrix[0].length];
		for (int i = 2; i < boardMatrix.length; i++) {
			for (int j = 0; j < boardMatrix[i].length; j++) {
				Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
				rectangle.setFill(Color.TRANSPARENT);
                          
				displayMatrix[i][j] = rectangle;
				gamePanel.add(rectangle, j, i - 2);
                                                        }
                                            }
                       rectangles = new Rectangle[viewData.getBrickData().length][viewData.getBrickData()[0].length];
    for(int i=0;i<viewData.getBrickData().length;i++)
    {
        for(int j=0;j<viewData.getBrickData()[i].length;j++)
        {
            Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
				rectangle.setFill(getFillColor(viewData.getBrickData()[i][j]));
                                
                                rectangles[i][j] = rectangle;
                                brickPanel.add(rectangle, j, i);
                              
                                
                                
        }
        
    }
    brickPanel.setLayoutX(gamePanel.getLayoutX() + viewData.getxPosition() * BRICK_SIZE);
    brickPanel.setLayoutY(gamePanel.getLayoutY()-42 + (viewData.getyPosition() * BRICK_SIZE)+viewData.getyPosition());
    generatePreviewPanel(viewData.getNextBrickData());
    timeLine = new Timeline(
				new KeyFrame(Duration.millis(400),
                                      ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));

    timeLine.setCycleCount(Timeline.INDEFINITE);
    timeLine.play();
    
    
                                }
    
    private void generatePreviewPanel(int[][] nextBrickData) {
		nextBrick.getChildren().clear();
		for (int i = 0; i < nextBrickData.length; i++) {
			for (int j = 0; j < nextBrickData[i].length; j++) {
				Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
				setRectangleData(nextBrickData[i][j], rectangle);
				if (nextBrickData[i][j] != 0) {
					nextBrick.add(rectangle, j, i);
				}
			}
		}
	}
    
    private void moveDown(MoveEvent event) {
		DownData downData = eventLister.onDownEvent(event);
                if(downData.getClearRow()!=null&&downData.getClearRow().getLinesRemoved()>0)
                {
                    ami+= downData.getClearRow().getLinesRemoved();
                    lol(ami);
                  NotificationPanel notificationPanel = new NotificationPanel(" + " + downData.getClearRow().getScoreBonus());
			groupNotification.getChildren().add(notificationPanel);
                        
			notificationPanel.showScore(groupNotification.getChildren());                }
		refreshBrick(downData.getViewData());
	}
    public void refreshGameBackground(int[][] board) {
		for (int i = 2; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				setRectangleData(board[i][j], displayMatrix[i][j]);
			}
		}
                
	}
    private void refreshBrick(ViewData viewData){
         brickPanel.setLayoutX(gamePanel.getLayoutX() + viewData.getxPosition() * BRICK_SIZE);
    brickPanel.setLayoutY(gamePanel.getLayoutY()-42 + (viewData.getyPosition() * BRICK_SIZE)+viewData.getyPosition());
    
        for (int i = 0; i < viewData.getBrickData().length; i++) {
			for (int j = 0; j < viewData.getBrickData()[i].length; j++) {
				setRectangleData(viewData.getBrickData()[i][j], rectangles[i][j]);
			}
		}
    
        generatePreviewPanel(viewData.getNextBrickData());
    }
    public void setEventLister(InputEventListener eventLister) {
		this.eventLister = eventLister;
	}
    public void bindScore(IntegerProperty integerProperty) {
		scoreValue.textProperty().bind(integerProperty.asString());
              // IntegerProperty intProperty = new SimpleIntegerProperty(x);
               //  scoreValue.textProperty().bind(intProperty.asString());
                /*sv= scoreValue;
                System.out.println(sv);
*/
               //  System.out.println(integerProperty);
	}
    
     public void bindScore2(int x) {
		
                IntegerProperty intProperty = new SimpleIntegerProperty(x);
                 hs.textProperty().bind(intProperty.asString());
                /*sv= scoreValue;
                System.out.println(sv);
*/
               //  System.out.println(integerProperty);
	}
    
    public Paint getFillColor(int i) {
		Paint returnPaint;
		switch (i) {
		case 0:
			returnPaint = Color.TRANSPARENT;
			break;
		case 1:
			returnPaint = Color.AQUAMARINE;
			break;
		case 2:
			returnPaint = Color.PALEGREEN;
			break;
		case 3:
			returnPaint = Color.DARKGRAY;
			break;
		case 4:
			returnPaint = Color.YELLOW;
			break;
		case 5:
			returnPaint = Color.RED;
			break;
		case 6:
			returnPaint = Color.VIOLET;
			break;
		case 7:
			returnPaint = Color.CORNSILK;
			break;
		default:
			returnPaint = Color.WHITE;
			break;
		}

		return returnPaint;
	}
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		
		gamePanel.setFocusTraversable(true);
               gamePanel.requestFocus();
               
                gamePanel.setOnKeyPressed((new EventHandler<KeyEvent>(){
                    
    //event handling                
                    @Override
                    public void handle(KeyEvent event) {
                        
                     if (paused.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE)
                       {
                         if(event.getCode()==KeyCode.UP|| event.getCode()==KeyCode.W)
                         {
                             refreshBrick(eventLister.onRotateEvent());
                             event.consume();
                         }
                         
                         
                           
                           if(event.getCode()==KeyCode.DOWN|| event.getCode()==KeyCode.S)
                        
                    {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        event.consume();
                    }
                    if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
						refreshBrick(eventLister.onLeftEvent());
						event.consume();
					}
                    if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
						refreshBrick(eventLister.onRightEvent());
						event.consume();
					}
                       }
                    
                    if (event.getCode() == KeyCode.P) {
					pauseButton.selectedProperty().setValue(!pauseButton.selectedProperty().getValue());
				}
                    }
                    
                    
                    
                    
                }));
                gameOverPanel.setVisible(false);
                
                
                pauseButton.selectedProperty().bindBidirectional(paused);
		pauseButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                       if (newValue) {
					timeLine.pause();
					pauseButton.setText("Resume");
				} else {
					timeLine.play();
					pauseButton.setText("Pause");
				}
			}
                    

			
		});
		Reflection reflection = new Reflection();
		reflection.setFraction(0.9);
		reflection.setTopOpacity(0.7);
		reflection.setTopOffset(-12);
		scoreValue.setEffect(reflection);
	}

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(10);
		rectangle.setArcWidth(10);
        
        
    }
    public void gameOver() {
		timeLine.stop();
		gameOverPanel.setVisible(true);
		isGameOver.setValue(Boolean.TRUE);
		System.out.println("Game Over!");
	}
	
}
