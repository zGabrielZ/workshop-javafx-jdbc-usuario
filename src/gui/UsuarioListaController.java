package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import db.DbException;
import gui.destinatario.ListaDeAlteracaoDeDados;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.entidade.Usuario;
import modelo.entidade.service.UsuarioService;

public class UsuarioListaController implements Initializable, ListaDeAlteracaoDeDados {

	@FXML
	private TableView<Usuario> tableViewUsuario;

	@FXML
	private TableColumn<Usuario, Integer> tableColumnId;

	@FXML
	private TableColumn<Usuario, String> tableColumnCpf;

	@FXML
	private TableColumn<Usuario, String> tableColumnNome;

	@FXML
	private TableColumn<Usuario, String> tableColumnEmail;

	@FXML
	private TableColumn<Usuario, String> tableColumnTelefone;
	
	@FXML
	private TableColumn<Usuario, Usuario> tableComlumnEdit;

	@FXML
	private TableColumn<Usuario, Usuario> tableComlumnRemove;

	@FXML
	private Button btNovo;
	
	@FXML
	private Button btProcurarNome;
	
	@FXML
	private Button btListaCompleta;
	
	@FXML 
	private TextField txtProcurarNome;
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.atualStage(event);
		Usuario aluno = new Usuario();
		criarFormulario(aluno, "/gui/UsuarioForm.fxml", parentStage);
	}
	
	@FXML
	public void onBtProcurarNome() {
		buscar();
	}
	
	@FXML
	public void onBtListaCompleta() {
		buscarLista();
	}
	
	
	private UsuarioService usuarioService;

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	private ObservableList<Usuario> obsList;

	private void inicializarTabela() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
	}

	public void atualizarTableView() {
		if (usuarioService == null) {
			throw new IllegalStateException("Service está nulo");
		}

		List<Usuario> list = usuarioService.encontrarTudo();

		obsList = FXCollections.observableArrayList(list);
		tableViewUsuario.setItems(obsList);
		botaoDeEditar();
		botaoDeRemover();

	}

	private void criarFormulario(Usuario aluno, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			UsuarioFormController controller = loader.getController();
			controller.setEntidade(aluno);
			controller.setUsuarioService(new UsuarioService());
			controller.addListaDeAlteracaoDeDados(this);
			controller.atualizarFormularioDados();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite os dados do Usuário");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);

			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.mostrarAlert("Io Exception", "Erro de carregamento da tela", e.getMessage(), AlertType.ERROR);
		}
	}

	private void botaoDeEditar() {
		tableComlumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableComlumnEdit.setCellFactory(param -> new TableCell<Usuario, Usuario>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Usuario obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> criarFormulario(obj, "/gui/UsuarioForm.fxml", Utils.atualStage(event)));
			}
		});
	}

	private void botaoDeRemover() {
		tableComlumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableComlumnRemove.setCellFactory(param -> new TableCell<Usuario, Usuario>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Usuario obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removerEntidade(obj));
			}
		});
	}
	
	private void removerEntidade(Usuario obj) {
		Optional<ButtonType> result = Alerts.mostrarConfirmation("Confirmar","Tem certeza que quer deletar ? ");
		
		if(result.get() == ButtonType.OK) {
			if(usuarioService == null) {
				throw new  IllegalStateException("Service está nulo");
			}
			
			try {
				usuarioService.remover(obj);
				atualizarTableView();
			} catch (DbException e) {
				Alerts.mostrarAlert("Erro em remover",null,e.getMessage(),AlertType.ERROR);
			}
		}
	
	}
	public void buscar() { 
		List<Usuario> usuario = usuarioService.encontrarPorNome(txtProcurarNome.getText());
		obsList = FXCollections.observableArrayList(usuario); 
		tableViewUsuario.setItems(obsList);
	}
	
	public void buscarLista() { 
		List<Usuario> usuario = usuarioService.encontrarTudo();
		obsList = FXCollections.observableArrayList(usuario); 
		tableViewUsuario.setItems(obsList);
	}
	
	@Override
	public void onDadosAlterados() {
		atualizarTableView();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarTabela();
	}

}
