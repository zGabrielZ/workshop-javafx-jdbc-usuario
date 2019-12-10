package application;
	
import java.io.IOException;

import gui.UsuarioListaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import modelo.entidade.service.UsuarioService;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {

	private static Scene mainScene;
	
	private static Stage stage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/UsuarioListagem.fxml"));
			loader.load();
			
			Parent parent = loader.getRoot();
			mainScene = new Scene(parent);		
			
			UsuarioListaController listaController = loader.getController();
			listaController.setUsuarioService(new UsuarioService());
			listaController.atualizarTableView();
			
			primaryStage.setTitle("Cadastro do usuario");
			primaryStage.setScene(mainScene);
			primaryStage.show();
			setStage(primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Stage getStage() {
		return stage;
	}

	public static void setStage(Stage stage) {
		Main.stage = stage;
	}

	public static Scene getMainScene() {
		return mainScene;
	}

	public static void setMainScene(Scene mainScene) {
		Main.mainScene = mainScene;
	}

	
}
