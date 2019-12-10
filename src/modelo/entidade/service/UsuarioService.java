package modelo.entidade.service;

import java.util.List;

import modelo.dao.DaoFactory;
import modelo.dao.UsuarioDao;
import modelo.entidade.Usuario;

public class UsuarioService {

	public UsuarioDao dao = DaoFactory.criarUsuario();
	
	public List<Usuario> encontrarTudo(){
		return dao.encontrarTudo();
	}
	
	public List<Usuario> encontrarPorNome(String nome){
		return dao.encontrarPeloNome(nome);
	}
	
	public void salvarOuAtualizar(Usuario usuario) {
		if(usuario.getId() == null) {
			dao.inserir(usuario);
		}
		else {
			dao.update(usuario);
		}
	}
	
	public void remover(Usuario usuario) {
		dao.delete(usuario.getId());
	}
}
