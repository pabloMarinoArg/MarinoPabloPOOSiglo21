package src.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Invoice {
   private String id;
   private String clientFullName;
   private List<String> detail;
   private String sendAddress;


    public Invoice() {
        this.id = UUID.randomUUID().toString();
        this.detail = new ArrayList<>();
    }

    public Invoice(String clientFullName, List<String> detail, String sendAddress){
        this.id = UUID.randomUUID().toString();
        this.clientFullName = clientFullName;
        this.detail = detail;
        this.sendAddress = sendAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
    }

    public List<String> getDetail() {
        return detail;
    }

    public void setDetail(List<String> detail) {
        this.detail = detail;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    private String getDetailsFull() {
        return concatenarConSaltosDeLinea(this.detail);
    }

    private String concatenarConSaltosDeLinea(List<String> lista) {
        StringBuilder resultado = new StringBuilder();
        for (String elemento : lista) {
            resultado.append(elemento).append(System.lineSeparator());
        }
        return resultado.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id) && Objects.equals(clientFullName, invoice.clientFullName) && Objects.equals(detail, invoice.detail) && Objects.equals(sendAddress, invoice.sendAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientFullName, detail, sendAddress);
    }

    @Override
    public String toString() {
        return "Orden retiro: " + id +
                "\n Nombre cliente: " + clientFullName +
                "\n Direccion envio: " + sendAddress +
                "\n detalle: " + getDetailsFull();
    }
}
