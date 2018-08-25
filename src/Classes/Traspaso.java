package Classes;

public class Traspaso {
    private String articulo;
    private String descripcion;
    private String value;

    public Traspaso(String articulo, String descripcion, String value) {
        this.articulo = articulo;
        this.descripcion = descripcion;
        this.value = value;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public String toString(){
        String res = "Articulo: "+articulo+" Descripcion: "+descripcion+" Value: "+ value;
        return res;
    }
}
