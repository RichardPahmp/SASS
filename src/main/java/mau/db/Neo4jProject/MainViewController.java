package mau.db.Neo4jProject;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainViewController extends Controller{

	@FXML
	ListView<Article> articleListView;
	
	@FXML
	ListView<Article> referenceListView;
	
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
	Spinner<Integer> stepsSpinner;

	@FXML
	TextField filterTextField;
	
	Scene editArticleScene;
	EditArticleViewController editArticleViewController;
	
	private boolean outgoingReference = true;
	
	private ObservableList<Article> articleList = FXCollections.observableArrayList();
	private ObservableList<Article> referenceList = FXCollections.observableArrayList();

	private FilteredList<Article> filteredArticleList = new FilteredList<>(articleList, this::articleFilter);
	
	@Override
	public void initialize(App app, Neo4jDatabase database) {
		super.initialize(app, database);
		
		articleListView.setItems(filteredArticleList);
		articleListView.getSelectionModel().clearSelection();
		articleListView.getSelectionModel().selectedItemProperty().addListener(this::articleSelectionChanged);
		
		referenceListView.setItems(referenceList);
		referenceListView.getSelectionModel().selectedItemProperty().addListener(this::referenceSelectionChanged);

		stepsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1));
		stepsSpinner.valueProperty().addListener(this::spinnerChanged);
		
		updateArticleList();
	}

	private boolean articleFilter(Article name){
		String filter = filterTextField.getText();
		return true;
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
			Article article = articleListView.getSelectionModel().getSelectedItem();
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
			editArticleViewController.setCallback(() -> {
				updateArticleList();
				//TODO : update info view after article edit
			});
			
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
			Article article = articleListView.getSelectionModel().getSelectedItem();
			database.deleteArticle(article.name);
			updateArticleList();
		}
	}
	
	private void updateArticleList() {
		ArrayList<Article> articles = database.getAllArticles();
		articleList.setAll(articles);
	}
	
	private void updateReferenceList(Article article, int steps) {
		ArrayList<Article> references;
		if(outgoingReference) {
			references = database.getReferences(article.name, steps);
		} else {
			references = database.getReferencers(article.name);
		}
		referenceList.setAll(references);
	}

	private void updateReferenceList(Article article){
		updateReferenceList(article, 1);
	}
	
	private void updateInfoView(Article article) {
		infoTextArea.setText("Name: " + article.name + "\nYear: " + article.year);
	}

	private void spinnerChanged(ObservableValue<? extends Integer> observable, int oldValue, int newValue){
		if(articleListView.getSelectionModel().getSelectedIndex() >= 0) {
			Article article = articleListView.getSelectionModel().getSelectedItem();
			updateReferenceList(article, newValue);
		}
	}
	
	private void articleSelectionChanged(ObservableValue<? extends Article> observable, Article oldArticle, Article newArticle) {
		if(newArticle != null) {
			referenceListView.getSelectionModel().clearSelection();
			updateInfoView(newArticle);
			
			updateReferenceList(newArticle);
		}
	}
	
	private void referenceSelectionChanged(ObservableValue<? extends Article> observable, Article oldArticle, Article newArticle) {
		if(newArticle != null) {
			articleListView.getSelectionModel().clearSelection();
			updateInfoView(newArticle);
		}
	}
}
