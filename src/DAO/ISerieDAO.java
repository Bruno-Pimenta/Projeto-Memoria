
package DAO;

import Entities.Serie;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author Bruno
 */
public interface ISerieDAO {
    public Long cadastrar(Serie serie) throws Exception;
    public void buscaPorTags(String tags)throws Exception;
    public List buscarTodos() throws SQLException;
    public void excluir(Long id) throws SQLException;
    public void atualizar(Serie serie) throws SQLException;
}
