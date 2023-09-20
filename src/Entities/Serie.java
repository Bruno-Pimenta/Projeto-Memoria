
package Entities;


public class Serie {
    private Long id;
    private String nome;
    private int numeroEpisodios;
    private Float nota;
    private String paisOrigem;
    private String motivoReassistir;
    private String tags;

    public Serie(String nome, int numeroEpisodios, Float nota, String paisOrigem, String motivoReassistir, String tags) {
        this.nome = nome;
        this.numeroEpisodios = numeroEpisodios;
        this.nota = nota;
        this.paisOrigem = paisOrigem;
        this.motivoReassistir = motivoReassistir;
        this.tags = tags;
    }
    
    

    public Serie(Long id, String nome, int numeroEpisodios, Float nota, String paisOrigem, String motivoReassistir, String tags) {
        this.id = id;
        this.nome = nome;
        this.numeroEpisodios = numeroEpisodios;
        this.nota = nota;
        this.paisOrigem = paisOrigem;
        this.motivoReassistir = motivoReassistir;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumeroEpisodios() {
        return numeroEpisodios;
    }

    public void setNumeroEpisodios(int numeroEpisodios) {
        this.numeroEpisodios = numeroEpisodios;
    }

    public Float getNota() {
        return nota;
    }

    public void setNota(Float nota) {
        this.nota = nota;
    }

    public String getPaisOrigem() {
        return paisOrigem;
    }

    public void setPaisOrigem(String paisOrigem) {
        this.paisOrigem = paisOrigem;
    }

    public String getMotivoReassistir() {
        return motivoReassistir;
    }

    public void setMotivoReassistir(String motivoReassistir) {
        this.motivoReassistir = motivoReassistir;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "\nSerie{" + "id: " + id + ", nome: " + nome + ", numero de episodios: " 
                + numeroEpisodios + ", nota: " + nota + ", paisde origem: " + paisOrigem 
                + ", motivo para reassistir: " + motivoReassistir + ", tags: " + tags + '}';
    }
  
}
