package aplicacao;

import java.util.List;

import modelo.dao.DaoFactory;
import modelo.dao.UsuarioDao;
import modelo.entidade.Usuario;

public class Teste {

	public static void main(String[] args) {
		
		UsuarioDao dao = DaoFactory.criarUsuario();
		
		List<Usuario> list = dao.encontrarTudo();
		for(Usuario u : list) {
			System.out.println(u);
		}
		
		System.out.println();
		List<Usuario> list2 = dao.encontrarPeloNome("Gabriel");
		for(Usuario u : list2) {
			System.out.println(u);
		}
		
		System.out.println();
		
		/*Usuario u = new Usuario(null,"jo","jo","jo","jo");
		dao.inserir(u);
		System.out.println("usuario inserido");*/
		
		/*Usuario u = new Usuario(null,"po","jo","jo","jo");
		dao.inserir(u);
		System.out.println("usuario inserido");*/
		
		
		
	}

}
