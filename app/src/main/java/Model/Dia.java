package Model;

public class Dia {
    boolean funcionamento;
    private String dia_mes;
    private String dia_semana;
    private String arroz;
    private String feijao;
    private String mistura1;
    private String mistura2;
    private String sobremesa;
    private String salada;
    private String suco;
    private boolean especial;

    public Dia() {

    }

    public Dia(boolean funcionamento, String dia_mes, String dia_semana,
               String arroz, String feijao, String mistura1, String mistura2,
               String sobremesa, String salada, String suco, boolean especial) {
        super();
        this.funcionamento = funcionamento;
        this.dia_mes = dia_mes;
        this.dia_semana = dia_semana;
        this.arroz = arroz;
        this.feijao = feijao;
        this.mistura1 = mistura1;
        this.mistura2 = mistura2;
        this.sobremesa = sobremesa;
        this.salada = salada;
        this.suco = suco;
        this.especial = especial;
    }

    public boolean isFuncionamento() {
        return funcionamento;
    }

    public void setFuncionamento(boolean funcionamento) {
        this.funcionamento = funcionamento;
    }

    public String getDia_mes() {
        return dia_mes;
    }

    public void setDia_mes(String dia_mes) {
        this.dia_mes = dia_mes;
    }

    public String getDia_semana() {
        return dia_semana;
    }

    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }

    public String getArroz() {
        return arroz;
    }

    public void setArroz(String arroz) {
        this.arroz = arroz;
    }

    public String getFeijao() {
        return feijao;
    }

    public void setFeijao(String feijao) {
        this.feijao = feijao;
    }

    public String getMistura1() {
        return mistura1;
    }

    public void setMistura1(String mistura1) {
        this.mistura1 = mistura1;
    }

    public String getMistura2() {
        return mistura2;
    }

    public void setMistura2(String mistura2) {
        this.mistura2 = mistura2;
    }

    public String getSobremesa() {
        return sobremesa;
    }

    public void setSobremesa(String sobremesa) {
        this.sobremesa = sobremesa;
    }

    public String getSalada() {
        return salada;
    }

    public void setSalada(String salada) {
        this.salada = salada;
    }

    public String getSuco() {
        return suco;
    }

    public void setSuco(String suco) {
        this.suco = suco;
    }

    public boolean isEspecial() {
        return especial;
    }

    public void setEspecial(boolean especial) {
        this.especial = especial;
    }
}
