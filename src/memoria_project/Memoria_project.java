
package memoria_project;


import DAO.SerieDAO;
import Entities.Serie;
import java.util.List;
import java.util.Scanner;
/**
 *
 * @author Bruno
 */
public class Memoria_project {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        int chaveControle = 0;
        
        do{
            String menu = "Insira 1 para realizar o cadastro de um registro\n"
                + "Insira 2 para realizar uma busca por tags\n"
                + "Insira 3 para realizar exclusão de um registro\n"
                + "Insira 4 para atualizar um registro\n"
                + "Insira 5 para buscar todos os registros\n";
            
            System.out.println(menu);
            Scanner sc = new Scanner (System.in);
            chaveControle = sc.nextInt();
        
        
            if(chaveControle == 1){
                Serie serie = criaInstancia();
                SerieDAO serieDAO = new SerieDAO();
                serieDAO.cadastrar(serie);
            }
            else if(chaveControle == 2){
                sc.nextLine();
                String tags = "";
                System.out.println("Digite as tags separadas por vírgula");
                tags = sc.nextLine();
                SerieDAO serieDAO = new SerieDAO();
                serieDAO.buscaPorTags(tags);
            }
            else if(chaveControle == 3){
                System.out.println("Digite o id do registro a ser excluído");
                long id = sc.nextLong();
                SerieDAO serieDAO = new SerieDAO();
                serieDAO.excluir(id);
            }
            else if(chaveControle == 4){
                System.out.println("Digite o id do registro a ser atualizado");
                Long id = sc.nextLong();
                Serie serie = criaInstancia();
                serie.setId(id);
                SerieDAO serieDAO = new SerieDAO();
                serieDAO.atualizar(serie);
            }
            else if(chaveControle == 5){
                SerieDAO serieDAO = new SerieDAO();
                List<Serie> lista = serieDAO.buscarTodos();
                System.out.println(lista);
            }
            System.out.println("");
            
        }while(chaveControle>0&&chaveControle<=5);
    }
    
    public static Serie criaInstancia(){
        Scanner sc = new Scanner (System.in);
        System.out.println("Insira o nome da série: ");
        String nome = sc.nextLine();
        System.out.println("Insira o número de episódios: ");
        Integer numeroEp = sc.nextInt();
        System.out.println("Insira a nota sugerida: ");
        Float nota = sc.nextFloat();
        sc.nextLine();
        System.out.println("Insira o país de origem da obra: ");
        String paisOrigem = sc.nextLine();
        System.out.println("Insira motivos para reassitir obra: ");
        String reassistirObra = sc.nextLine();
        System.out.println("Insira as tags da obra separadas por vírgula: ");
        String tags = sc.nextLine();
        
        Serie serie = new Serie(nome, numeroEp, nota, paisOrigem, reassistirObra, tags);
        
        return serie;
        
    }
}
