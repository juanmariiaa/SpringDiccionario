package org.example.dictionaryapp.dto;

public class PalabraDTO {
    private Long id;
    private String termino;
    private String categoriaGramatical;

    public PalabraDTO(Long id, String termino, String categoriaGramatical) {
        this.id = id;
        this.termino = termino;
        this.categoriaGramatical = categoriaGramatical;
    }

    public Long getId() {
        return id;
    }

    public String getTermino() {
        return termino;
    }

    public String getCategoriaGramatical() {
        return categoriaGramatical;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
