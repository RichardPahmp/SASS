package mau.db.Neo4jProject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Optional;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditArticleViewController extends Controller{

	@FXML
	TextField nameTextField;
	
	@FXML
	TextField yearTextField;
	
	@FXML
	TextArea topicsTextArea;
	
	@FXML
	ListView<Article> articleListView;
	
	@FXML
	ListView<Article> referenceListView;
	
	@FXML
	ListView<String> authorListView;
	
	@FXML
	ListView<String> authoredListView;

	@FXML
	Button removeAuthorButton;

	@FXML
	Button addAuthorButton;

	@FXML
	Button removeReferenceButton;

	@FXML
	Button addReferenceButton;

	@FXML
	Button createAuthorButton;

	@FXML
	Button deleteAuthorButton;
	
	private ObservableList<Article> articleList = FXCollections.observableArrayList();
	private ObservableList<Article> referenceList = FXCollections.observableArrayList();
	private ObservableList<String> authorList = FXCollections.observableArrayList();
	private ObservableList<String> authoredList = FXCollections.observableArrayList();
	
	private ArrayList<Article> referencesToRemove = new ArrayList<>();
	private ArrayList<String> authoredToRemove = new ArrayList<>();

	private Callback callback;
	
	@Override
	public void initialize(App app, Neo4jDatabase database) {
		super.initialize(app, database);
		
		updateArticleList();
		updateAuthorList();
		
		authorListView.setItems(authorList);
		articleListView.setItems(articleList);
		referenceListView.setItems(referenceList);
		authoredListView.setItems(authoredList);

		authoredListView.getSelectionModel().selectedItemProperty().addListener(this::authoredSelectionChanged);
		authorListView.getSelectionModel().selectedItemProperty().addListener(this::authorSelectionChanged);

		if(App.isAdmin()){
			deleteAuthorButton.setDisable(false);
			createAuthorButton.setDisable(false);
		}

		if(!App.isAdmin()){
			removeAuthorButton.setDisable(true);
			addAuthorButton.setDisable(true);
		}

		if(App.isUser()){
			authoredList.add(App.getUsername());
			database.mergeAuthor(App.getUsername());
		}
	}

	public void setCallback(Callback callback){
		this.callback = callback;
	}
	
	@FXML
	private void onCancel() {
		Stage stage = (Stage) nameTextField.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onSubmit() {
		String name = nameTextField.getText();
		int year;
		try {
			year = Integer.parseInt(yearTextField.getText());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return;
		}
		
		if(!name.isBlank()) {
			String topicsString = topicsTextArea.getText();
			String[] topics;
			if(topicsString.isBlank()){
				topics = new String[0];
			} else {
				topics = topicsString.split(", *");
			}
			
			Article article = new Article(name, year, topics);
			database.mergeArticle(article);
			
			database.mergeReferences(name, Article.toNameList(new ArrayList<Article>(referenceList)));
			
			database.deleteReferences(name, Article.toNameList(referencesToRemove));
			
			database.mergeAuthored(name, new ArrayList<String>(authoredList));
			
			database.deleteAuthored(name, authoredToRemove);
			
			Stage stage = (Stage) nameTextField.getScene().getWindow();
			stage.close();

			callback.call();
		}
	}
	
	@FXML
	private void onAddReference() {
		if(articleListView.getSelectionModel().getSelectedIndex() >= 0) {
			Article article = articleListView.getSelectionModel().getSelectedItem();
			articleList.remove(article);
			referenceList.add(article);
			
			if(referencesToRemove.contains(article)) {
				referencesToRemove.remove(article);
			}
		}
	}
	
	@FXML
	private void onRemoveReference() {
		if(referenceListView.getSelectionModel().getSelectedIndex() >= 0) {
			Article article = referenceListView.getSelectionModel().getSelectedItem();
			referenceList.remove(article);
			articleList.add(article);
			referencesToRemove.add(article);
		}
	}
	
	@FXML
	private void onAddAuthor() {
		if(authorListView.getSelectionModel().getSelectedIndex() >= 0) {
			String author = authorListView.getSelectionModel().getSelectedItem();
			authorList.remove(author);
			authoredList.add(author);
			
			if(authoredToRemove.contains(author)) {
				authoredToRemove.remove(author);
			}
		}
	}
	
	@FXML
	private void onRemoveAuthor() {
		if(authoredListView.getSelectionModel().getSelectedIndex() >= 0) {
			String author = authoredListView.getSelectionModel().getSelectedItem();

			authoredList.remove(author);
			authorList.add(author);
			authoredToRemove.add(author);
		}
	}
	
	@FXML
	private void onCreateAuthor() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("New Author");
		dialog.setHeaderText("Create a new author.");
		dialog.setContentText("Enter new author name:");
		
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> database.mergeAuthor(name));
		updateAuthorList();
	}
	
	@FXML
	private void onDeleteAuthor() {
		if(authorListView.getSelectionModel().getSelectedIndex() >= 0) {
			String name = authorListView.getSelectionModel().getSelectedItem();
			database.deleteAuthor(name);
			updateAuthorList();
		}
	}
	
	private void updateAuthorList() {
		ArrayList<String> authors = database.getAllAuthorNames();
		for(String a : authoredList) {
			authors.remove(a);
		}
		authorList.setAll(authors);
	}
	
	private void updateArticleList() {
		ArrayList<Article> articles = database.getAllArticles();
		for(Article a : referenceList) {
			articles.remove(a);
		}
		articleList.setAll(articles);
	}
	
	public void setArticle(Article article) {
		nameTextField.setText(article.name);
		yearTextField.setText(article.year + "");
		
		String topics = String.join(",", article.topics);
		topicsTextArea.setText(topics);
		
		ArrayList<Article> references = database.getReferences(article.name, 1);
		ArrayList<String> authors = database.getAuthors(article.name);

		articleList.removeAll(references);
		articleList.remove(article);
		referenceList.setAll(references);

		authoredList.setAll(authors);
		authorList.removeAll(authors);

		nameTextField.setDisable(true);

	}

	private void authoredSelectionChanged(ObservableValue<? extends String> observable, String oldAuthor, String newAuthor) {
		if(App.isUser()){
			if(newAuthor.equals(App.getUsername())){
				if(authoredList.size() == 1) {
					removeAuthorButton.setDisable(true);
				} else {
					removeAuthorButton.setDisable(false);
				}
			} else {
				removeAuthorButton.setDisable(true);
			}
		}

	}

	private void authorSelectionChanged(ObservableValue<? extends String> observable, String oldAuthor, String newAuthor) {
		if(App.isUser() && newAuthor.equals(App.getUsername())){
			addAuthorButton.setDisable(false);
		} else if(App.isUser()){
			addAuthorButton.setDisable(true);
		} else {
			addAuthorButton.setDisable(false);
		}
	}
}
