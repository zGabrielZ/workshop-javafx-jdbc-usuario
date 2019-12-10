package modelo.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import db.DB;
import db.DbException;
import modelo.dao.UsuarioDao;
import modelo.entidade.Usuario;

public class UsuarioDaoJDBC implements UsuarioDao {

	private Connection conn = null;
	
	public UsuarioDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void inserir(Usuario usuario) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("INSERT INTO usuario (nome,cpf,email,telefone) VALUES (?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1,usuario.getNome());
			st.setString(2,usuario.getCpf());
			st.setString(3,usuario.getEmail());
			st.setString(4,usuario.getTelefone());
			
			int linhasAfetadas = st.executeUpdate();
			
			if(linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					usuario.setId(id);
				}
			}
			else {
				throw new DbException("erro,nenhuma linha afetada");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
		
	}

	@Override
	public void update(Usuario usuario) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("UPDATE usuario SET nome = ? , cpf = ? , email = ? , telefone = ? WHERE id = ?");
			st.setString(1,usuario.getNome());
			st.setString(2,usuario.getCpf());
			st.setString(3,usuario.getEmail());
			st.setString(4,usuario.getTelefone());
			st.setInt(5,usuario.getId());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
		
		
	}

	@Override
	public void delete(Integer id) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM usuario WHERE id = ?");
			st.setInt(1,id);
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}

	@Override
	public List<Usuario> encontrarTudo() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT * FROM usuario");
			rs = st.executeQuery();
			
			List<Usuario> list = new ArrayList<Usuario>();
			
			while(rs.next()) {
				Usuario usuario = instanciacaoUsuario(rs);
				list.add(usuario);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
		
	}

	@Override
	public List<Usuario> encontrarPeloNome(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT * FROM usuario WHERE nome like ? ");
			st.setString(1,"%"+nome+"%");
			rs = st.executeQuery();
			
			List<Usuario> list = new ArrayList<Usuario>();
			
			while(rs.next()) {
				Usuario usuario = instanciacaoUsuario(rs);
				list.add(usuario);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
		
	}
	
	public Usuario instanciacaoUsuario(ResultSet rs) throws SQLException {
		Usuario usuario = new Usuario();
		usuario.setId(rs.getInt("id"));
		usuario.setNome(rs.getString("nome"));
		usuario.setCpf(rs.getString("cpf"));
		usuario.setEmail(rs.getString("email"));
		usuario.setTelefone(rs.getString("telefone"));
		return usuario;
	}


}
