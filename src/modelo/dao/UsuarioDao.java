package modelo.dao;

import java.util.List;

import modelo.entidade.Usuario;

public interface UsuarioDao {

	void inserir(Usuario usuario);
	void update(Usuario usuario);
	void delete(Integer id);
	List<Usuario> encontrarTudo();
	List<Usuario> encontrarPeloNome(String nome);
}
