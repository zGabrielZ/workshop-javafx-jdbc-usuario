package gui;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.destinatario.ListaDeAlteracaoDeDados;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modelo.entidade.Usuario;
import modelo.entidade.service.UsuarioService;
import modelo.exceptions.Validacao;

public class UsuarioFormController implements Initializable {
	
	private Usuario entidade;
	
	private UsuarioService usuarioService;
	
	private List<ListaDeAlteracaoDeDados> listaDeAlteracaoDeDados = new ArrayList<>();

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setEntidade(Usuario entidade) {
		this.entidade = entidade;
	}
	
	public void addListaDeAlteracaoDeDados(ListaDeAlteracaoDeDados dadosDeAlteracaoDeDados) {
		listaDeAlteracaoDeDados.add(dadosDeAlteracaoDeDados);
	}

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtCPF;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtTelefone;

	@FXML
	private Label lblErroNome;
	
	@FXML
	private Label lblErroCPF;
	
	@FXML
	private Label lblErroEmail;
	
	@FXML
	private Label lblErroTelefone;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) throws ParseException {
		
		if(entidade == null) {
			throw new IllegalStateException("Entidade está nulo");
		}
		
		if(usuarioService == null) {
			throw new IllegalStateException("Service está nulo");
		}
		
		try {
			entidade = formularioDados();
			usuarioService.salvarOuAtualizar(entidade);
			notificarListaDeAlteracaoDeDados();
			Utils.atualStage(event).close();
		}catch (Validacao e) {
			setErrosMensagens(e.getErros());
		}catch (DbException e) {
			Alerts.mostrarAlert("Erro em salvar",null,e.getMessage(),AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.atualStage(event).close();
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarNodes();
		
	}
	
	private void inicializarNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtCPF,12);
		Constraints.setTextFieldMaxLength(txtNome,70);
		Constraints.setTextFieldMaxLength(txtEmail,30);
		Constraints.setTextFieldMaxLength(txtTelefone,30);
	}
	
	private Usuario formularioDados() throws ParseException {
		Usuario usuario = new Usuario();
		
		Validacao validacao = new Validacao("erro");
		
		usuario.setId(Utils.converterParaInt(txtId.getText()));
		
		if(txtCPF.getText() == null || txtCPF.getText().trim().equals("")) {
			validacao.addErro("cpf","campo vázio");
		}
		
		usuario.setCpf(txtCPF.getText());
		
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			validacao.addErro("nome","campo vázio");
		}
		usuario.setNome(txtNome.getText());
		
		if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			validacao.addErro("email","campo vázio");
		}
		usuario.setEmail(txtEmail.getText());
		
		if(txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			validacao.addErro("telefone","campo vázio");
		}
		usuario.setTelefone(txtTelefone.getText());
		
		if(validacao.getErros().size()>0) {
			throw validacao;
		}
		
		
		return usuario;
		
	}
	
	public void atualizarFormularioDados() {
		
		if(entidade == null) {
			throw new IllegalStateException("Entidade está nulo");
		}

		
		txtId.setText(String.valueOf(entidade.getId()));
		txtCPF.setText(entidade.getCpf());
		txtNome.setText(entidade.getNome());
		txtEmail.setText(entidade.getEmail());
		txtTelefone.setText(entidade.getTelefone());
	}
	
	
	
	private void setErrosMensagens(Map<String,String> erros) {
		Set<String> campos = erros.keySet();
		
		if(campos.contains("nome")) {
			lblErroNome.setText(erros.get("nome"));
		}
		else {
			lblErroNome.setText("");
		}
		if(campos.contains("cpf")) {
			lblErroCPF.setText(erros.get("cpf"));
		}
		else {
			lblErroCPF.setText("");
		}
		if(campos.contains("email")) {
			lblErroEmail.setText(erros.get("email"));
		}
		else {
			lblErroEmail.setText("");
		}
		if(campos.contains("telefone")) {
			lblErroTelefone.setText(erros.get("telefone"));
		}
		else {
			lblErroTelefone.setText("");
		}
	}
	
	private void notificarListaDeAlteracaoDeDados() {
		for(ListaDeAlteracaoDeDados l : listaDeAlteracaoDeDados) {
			l.onDadosAlterados();
		}
	}
	
}
