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
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainViewController extends Controller{

	@FXML
	ListView<Article> articleListView;
	
	@FXML
	ListView<Article> referenceListView;
	
	@FXML
	Button newArticleButton;

	@FXML
	TextField filterNameTextField;

	@FXML
	TextField filterFromYearTextField;

	@FXML
	TextField filterToYearTextField;

	@FXML
	TextField filterTopicsTextField;

	@FXML
	TextField filterAuthorsTextField;

	@FXML
	TextArea infoTextArea;

	@FXML
	RadioButton neighbourRadioButton;

	@FXML
	RadioButton outgoingRadioButton;

	@FXML
	RadioButton incomingRadioButton;
	
	@FXML
	Spinner<Integer> stepsSpinner;

	@FXML
	Button refreshButton;
	
	Scene editArticleScene;
	EditArticleViewController editArticleViewController;

	ToggleGroup toggleGroup = new ToggleGroup();
	
	private boolean outgoingReference = true;
	
	private ObservableList<Article> articleList = FXCollections.observableArrayList();
	private ObservableList<Article> referenceList = FXCollections.observableArrayList();

	private FilteredList<Article> filteredArticleList = new FilteredList<>(articleList);
	
	@Override
	public void initialize(App app, Neo4jDatabase database) {
		super.initialize(app, database);
		
		articleListView.setItems(filteredArticleList);
		articleListView.getSelectionModel().clearSelection();
		articleListView.getSelectionModel().selectedItemProperty().addListener(this::articleSelectionChanged);
		
		referenceListView.setItems(referenceList);
		referenceListView.getSelectionModel().selectedItemProperty().addListener(this::referenceSelectionChanged);
		referenceListView.setOnMouseClicked(e -> {
			if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2){
				onReferenceDoubleClick();
			}
		});

		neighbourRadioButton.setToggleGroup(toggleGroup);
		outgoingRadioButton.setToggleGroup(toggleGroup);
		incomingRadioButton.setToggleGroup(toggleGroup);
		toggleGroup.selectedToggleProperty().addListener(this::onToggleChange);

		stepsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1));
		stepsSpinner.valueProperty().addListener(this::spinnerChanged);
		
		updateArticleList();
		articleListView.getSelectionModel().select(0);
	}

	private boolean articleFilter(Article article){

		String nameFilter = filterNameTextField.getText();
		String topicsFilter = filterTopicsTextField.getText();
		String authorsFilter = filterAuthorsTextField.getText();
		int fromYearFilter = -1;
		int toYearFilter = -1;
		try {
			fromYearFilter = Integer.parseInt(filterFromYearTextField.getText());
			toYearFilter = Integer.parseInt(filterToYearTextField.getText());
		} catch (NumberFormatException e){

		}

		if(!nameFilter.isBlank() && !article.name.contains(nameFilter)){
			return false;
		}

		if(!topicsFilter.isBlank() && !article.containsTopics(topicsFilter)){
			return false;
		}

		if(!authorsFilter.isBlank() && !article.containsAuthors(authorsFilter)){
			return false;
		}

		if(fromYearFilter > 0 && article.year < fromYearFilter){
			return false;
		}

		if(toYearFilter > 0 && article.year > toYearFilter){
			return false;
		}

		return true;
	}

	private void onToggleChange(ObservableValue<? extends Toggle> ov, Toggle oldValue, Toggle newValue){
		if(neighbourRadioButton.isSelected()){
			stepsSpinner.setDisable(true);
		} else {
			stepsSpinner.setDisable(false);
		}

		if(articleListView.getSelectionModel().getSelectedIndex() >= 0){
			Article article = articleListView.getSelectionModel().getSelectedItem();
			updateReferenceList(article);
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
		if(outgoingRadioButton.isSelected()) {
			references = database.getReferences(article.name, steps);
		} else if (incomingRadioButton.isSelected()){
			references = database.getReferencers(article.name, steps);
		} else {
			references = database.getSharedReferencers(article.name);
		}
		referenceList.setAll(references);
	}

	private void updateReferenceList(Article article){
		updateReferenceList(article, 1);
	}
	
	private void updateInfoView(Article article) {
		String output = "";
		output += "Name: " + article.name;
		output += "\nYear: " + article.year;
		output += "\nTopics: " + String.join(", ", article.topics);
		output += "\nAuthors: " + String.join(", ", article.authors);
		infoTextArea.setText(output);
	}

	private void spinnerChanged(ObservableValue<? extends Integer> observable, int oldValue, int newValue){
		if(articleListView.getSelectionModel().getSelectedIndex() >= 0) {
			Article article = articleListView.getSelectionModel().getSelectedItem();
			updateReferenceList(article, newValue);
		}
	}
	
	private void articleSelectionChanged(ObservableValue<? extends Article> observable, Article oldArticle, Article newArticle) {
		if(newArticle != null) {
			//referenceListView.getSelectionModel().clearSelection();
			updateInfoView(newArticle);
			
			updateReferenceList(newArticle);
		}
	}
	
	private void referenceSelectionChanged(ObservableValue<? extends Article> observable, Article oldArticle, Article newArticle) {
		if(newArticle != null) {
			//articleListView.getSelectionModel().clearSelection();
			updateInfoView(newArticle);
		}
	}

	private void onReferenceDoubleClick(){
		Article article = referenceListView.getSelectionModel().getSelectedItem();
		articleListView.getSelectionModel().select(article);
	}

	@FXML
	private void onRefreshFilter(){
		filteredArticleList.setPredicate(this::articleFilter);
	}
}
