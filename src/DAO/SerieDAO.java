
package DAO;

import Entities.Serie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import memoria_project.conexao.Conexao;

/**
 *
 * @author Bruno
 */
public class SerieDAO implements ISerieDAO{

    public SerieDAO() {
    }
    
    @Override
    public Long cadastrar(Serie serie) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        Long id = null;
                
        try{
            connection = Conexao.getConnection();
            String sql = "insert into serie (nome, numero_episodios, nota, pais_origem, motivo_reassistir)\n" +
                "values (?,?,?,?,?)RETURNING id";
            ps = connection.prepareStatement(sql);
            ps.setString(1, serie.getNome());
            ps.setInt(2, serie.getNumeroEpisodios());
            ps.setDouble(3, serie.getNota());
            ps.setString(4, serie.getPaisOrigem());
            ps.setString(5, serie.getMotivoReassistir());
            ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    id = resultSet.getLong("id");
                }
            associarTags(id, serie.getTags());    
        }    
        catch(SQLException e){
            System.out.println("Não foi possível inserir o registro " + e.getMessage());
        }
        
        finally{
            if(ps != null && !ps.isClosed()){
                ps.close();
            }
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        }
        return id;
    }
    
    private static void associarTags(Long id, String tags) throws SQLException{
        Set<String> setTags = new HashSet<>();
        List<Long> listaTagId = new ArrayList<>();
        String[] tagsSeparadas = null;
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet resultSetTag = null;
        try{
            tagsSeparadas = tags.toLowerCase().replaceAll("\\s", "").split(",");
            for(String tagsAux: tagsSeparadas){
                setTags.add(tagsAux);
            }
            if(setTags.size()>0){
                try{
                    for(String tagsAux: setTags){
                        conexao = Conexao.getConnection();
                        String sqlTags = "select id from tag where tag.nome like ?";
                        pst = conexao.prepareStatement(sqlTags);
                        pst.setString(1, tagsAux);
                        resultSetTag = pst.executeQuery();
                        while (resultSetTag.next()) {
                            listaTagId.add(resultSetTag.getLong("id"));
                        }
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                } 
                
                try{
                    if(!listaTagId.isEmpty()){
                        for(Long idTag: listaTagId){
                            String sqlTags = "insert into serie_tags(serie_id, tag_id) values(?, ?)";
                            pst = conexao.prepareStatement(sqlTags);
                            pst.setLong(1, id);
                            pst.setLong(2, idTag);
                            pst.executeUpdate();
                        }
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                finally{
                    if(conexao != null && !conexao.isClosed()){
                        conexao.close();
                    }
                    if(pst != null && !pst.isClosed()){
                        pst.close();
                    }
                    if(resultSetTag != null && !resultSetTag.isClosed()){
                        resultSetTag.close();
                    }
                }
            }
        }    
        catch(NullPointerException e){
            System.out.println("Não foi encontrada tags para inserção");
            }
    }
    
    public void buscaPorTags(String tags)throws Exception{
        String[] tagsSeparadas = tags.toLowerCase().replaceAll("\\s", "").split(",");
        
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet resultSetTag = null;
        List<Serie> lista = new ArrayList<>();
        
        String parametro = "";
        
        if(tagsSeparadas.length==1){
            parametro = "?"; 
        }
        else if(tagsSeparadas.length>1){
            for(String ts : tagsSeparadas){
                parametro += "," + "?";
            }
            parametro = parametro.substring(1);
            
        }
        
        String sql = "select s.id as id, s.nome as nome, s.numero_episodios as numero_episodios, s.nota as nota, s.pais_origem as pais_origem,\n" +
"		s.motivo_reassistir as motivo_reassistir, STRING_AGG(t.nome, ',') as tag\n" +
"		from serie as s inner join serie_tags as st on s.id = st.serie_id inner join tag as t \n" +
"		on t.id = st.tag_id WHERE t.nome IN (" + parametro + ") GROUP BY s.id, s.nome, s.numero_episodios, \n" +
"		s.nota, s.pais_origem, s.motivo_reassistir";
        
        try{
            conexao = Conexao.getConnection();
            pst = conexao.prepareStatement(sql);
            for(int i=0; i<tagsSeparadas.length;i++){
                pst.setString(i+1, tagsSeparadas[i]);
            }
            resultSetTag = pst.executeQuery();
            
            while(resultSetTag.next()){
                Long id = resultSetTag.getLong("id");
                String nome = resultSetTag.getString("nome");
                int numeroEpisodios = resultSetTag.getInt("numero_episodios");
                Float nota = resultSetTag.getFloat("nota");
                String paisOrigem = resultSetTag.getString("pais_origem");
                String motivoReassistir = resultSetTag.getString("motivo_reassistir");
                String tagsSerie = resultSetTag.getString("tag");
                Serie serie = new Serie(id, nome, numeroEpisodios, nota, paisOrigem, motivoReassistir, tagsSerie);
                lista.add(serie);
            }
        }
        catch (SQLException e){
            System.out.println("Erro na consulta de tags para inserção" + e.getMessage());
        }
        finally{
            if(conexao != null && !conexao.isClosed()){
                conexao.close();
            }
            if(pst != null && !pst.isClosed()){
                pst.close();
            }
            if(resultSetTag != null && !resultSetTag.isClosed()){
                resultSetTag.close();
            }
        }
        System.out.println(lista);
    }
    
    public List buscarTodos() throws SQLException{
        List<Serie> lista = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            conexao = Conexao.getConnection();
            String sql = "Select s.id, s.nome, s.numero_episodios, \n" +
                " s.nota, s.pais_origem, s.motivo_reassistir, STRING_AGG(t.nome, ',')as tags\n" +
                " from serie as s inner join serie_tags as st on s.id = st.serie_id \n" +
                " inner join tag as t on st.tag_id = t.id \n" +
                " GROUP BY s.id, s.nome, s.numero_episodios, \n" +
                " s.nota, s.pais_origem, s.motivo_reassistir order by s.nome, s.id asc";
            ps = conexao.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                Long id = rs.getLong("id");
                String nome = rs.getString("nome");
                Integer numeroEpisodios = rs.getInt("numero_episodios");
                Float nota = rs.getFloat("nota");
                String paisOrigem = rs.getString("pais_origem");
                String motivoReassistir = rs.getString("motivo_reassistir");
                String tags = rs.getString("tags");
                Serie serie = new Serie(id, nome, numeroEpisodios, nota, paisOrigem, motivoReassistir, tags);
                lista.add(serie);
            }
        }
        
        catch(SQLException e){
            System.out.println("Erro no comando SQL referente a busca de registros" + e.getMessage());
        }
        
        finally{
            if(conexao!=null && !conexao.isClosed()){
                conexao.close();
            }
            if(ps!=null && !ps.isClosed()){
                ps.close();
            }
            if(rs!=null && !rs.isClosed()){
                rs.close();
            }
        }
        return lista;
    }
    
    public void excluir(Long id) throws SQLException{
        excluirTags(id);
        Connection conexao = null;
        PreparedStatement ps = null;
        
        try{
            String sql = "delete from serie where id = ?";
            conexao = Conexao.getConnection();
            ps = conexao.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        }
        
        catch(SQLException e){
            System.out.println("Erro ao excluir serie" + e.getMessage());
        }
        
        finally{
            if(conexao!=null && !conexao.isClosed()){
                conexao.close();
            }
            if(ps!=null && !ps.isClosed()){
                ps.close();
            }
        }
    }
            
    private static void excluirTags(Long id) throws SQLException{
        Connection conexao = null;
        PreparedStatement ps = null;
                
        try{
            String sql = "delete from serie_tags where serie_id = ?";
            conexao = Conexao.getConnection();
            ps = conexao.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        }
        
        catch(SQLException e){
            System.out.println("Erro ao excluir as tags" + e.getMessage());
        }
        
        finally{
            if(conexao!=null && !conexao.isClosed()){
                conexao.close();
            }
            if(ps!=null && !ps.isClosed()){
                ps.close();
            }
        }
    }
    
    public void atualizar(Serie serie) throws SQLException{
        Connection conexao = null;
        PreparedStatement ps = null;
                
        try{
            String sql = "update serie set nome = ?, numero_episodios = ?, nota = ?, "
                    + "pais_origem = ?, motivo_reassistir = ? where id = ?";
            conexao = Conexao.getConnection();
            ps = conexao.prepareStatement(sql);
            ps.setString(1, serie.getNome());
            ps.setInt(2, serie.getNumeroEpisodios());
            ps.setFloat(3, serie.getNota());
            ps.setString(4, serie.getPaisOrigem());
            ps.setString(5, serie.getMotivoReassistir());
            ps.setLong(6, serie.getId());
            ps.executeUpdate();
            atualizarTags(serie.getId(), serie.getTags());
        }
        
        catch(SQLException e){
            System.out.println("Erro ao atualizar o registro" + e.getMessage());
        }
        
        finally{
            if(conexao!=null && !conexao.isClosed()){
                conexao.close();
            }
            if(ps!=null && !ps.isClosed()){
                ps.close();
            }
        }
    }
    
    private static void atualizarTags(Long id, String tags) throws SQLException{
        excluirTags(id);
        associarTags(id, tags);
    }
}
