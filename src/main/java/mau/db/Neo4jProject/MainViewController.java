package mau.db.Neo4jProject;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainViewController extends Controller{

	@FXML
	ListView<String> articleListView;
	
	@FXML
	ListView<String> referenceListView;
	
	@FXML
	Button directionButton;
	
	@FXML
	Button newArticleButton;
	
	@FXML
	Button editArticleButton;
	
	@FXML
	Button removeArticleButton;
	
	@FXML
	TextArea infoTextArea;
	
	@FXML
	Spinner stepsSpinner;
	
	Scene editArticleScene;
	EditArticleViewController editArticleViewController;
	
	private boolean outgoingReference = true;
	
	private ObservableList<String> articleList = FXCollections.observableArrayList();
	private ObservableList<String> referenceList = FXCollections.observableArrayList();
	
	@Override
	public void initialize(App app, Neo4jDatabase database) {
		super.initialize(app, database);
		
		articleListView.setItems(articleList);
		articleListView.getSelectionModel().clearSelection();
		articleListView.getSelectionModel().selectedItemProperty().addListener(this::articleSelectionChanged);
		
		referenceListView.setItems(referenceList);
		referenceListView.getSelectionModel().selectedItemProperty().addListener(this::referenceSelectionChanged);
		
		updateArticleList();
	}
	
	@FXML
	private void onChangeDirection() {
		outgoingReference = !outgoingReference;
		
		if(outgoingReference) {
			directionButton.setText("-->");
		} else {
			directionButton.setText("<--");
		}
		
		if(articleListView.getSelectionModel().getSelectedIndex() >= 0) {
			updateReferenceList(articleListView.getSelectionModel().getSelectedItem());
		}
	}
	
	@FXML
	private void onNewArticle() {
		openEditArticleView();
	}
	
	@FXML
	private void onEditArticle() {
		if(articleListView.getSelectionModel().getSelectedIndex() >= 0) {
			String article = articleListView.getSelectionModel().getSelectedItem();
			EditArticleViewController controller = openEditArticleView();
			controller.setArticle(article);
		}
	}
	
	private EditArticleViewController openEditArticleView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/EditArticleView.fxml"));
			editArticleScene = new Scene(loader.load(), 640, 480);
			editArticleViewController = loader.getController();
			editArticleViewController.initialize(app, database);
			editArticleViewController.setCallback(() -> updateArticleList());
			
			Stage stage = new Stage();
			stage.setTitle("New Article");
			stage.setScene(editArticleScene);
			stage.initOwner(this.app.primaryStage);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.show();
			
			return editArticleViewController;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@FXML
	private void onRemoveArticle() {
		if(articleListView.getSelectionModel().getSelectedIndex() >= 0) {
			String article = articleListView.getSelectionModel().getSelectedItem();
			database.deleteArticle(article);
			updateArticleList();
		}
	}
	
	private void updateArticleList() {
		ArrayList<String> articles = database.getAllArticleNames();
		articleList.setAll(articles);
	}
	
	private void updateReferenceList(String articleName) {
		ArrayList<String> references;
		if(outgoingReference) {
			references = database.getReferences(articleName);
		} else {
			references = database.getReferencers(articleName);
		}
		referenceList.setAll(references);
	}
	
	private void updateInfoView(Article article) {
		infoTextArea.setText("Name: " + article.name);
	}
	
	private void articleSelectionChanged(ObservableValue<? extends String> observable, String oldArticle, String newArticle) {
		if(newArticle != null) {
			referenceListView.getSelectionModel().clearSelection();
			Article article = database.getArticle(newArticle);
			updateInfoView(article);
			
			updateReferenceList(newArticle);
		}
	}
	
	private void referenceSelectionChanged(ObservableValue<? extends String> observable, String oldArticle, String newArticle) {
		if(newArticle != null) {
			articleListView.getSelectionModel().clearSelection();
			Article article = database.getArticle(newArticle);
			updateInfoView(article);
		}
	}
}
