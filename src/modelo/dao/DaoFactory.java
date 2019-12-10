package modelo.dao;

import db.DB;
import modelo.dao.impl.UsuarioDaoJDBC;

public class DaoFactory {

	public static UsuarioDao criarUsuario() {
		return new UsuarioDaoJDBC(DB.getConnection());
	}
}
