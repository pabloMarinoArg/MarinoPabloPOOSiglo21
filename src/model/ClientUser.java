package src.model;

public class ClientUser extends Person{

    private String phoneNumber;
    private String deliveryAddress;
    private String fiscalAddress;
    private Long idNumber;


    public ClientUser(String firstName, String lastName, String email, String phoneNumber, String deliveryAddress, String fiscalAddress, Long idNumber) {
        super(firstName, lastName, email);
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.fiscalAddress = fiscalAddress;
        this.idNumber = idNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getFiscalAddress() {
        return fiscalAddress;
    }

    public void setFiscalAddress(String fiscalAddress) {
        this.fiscalAddress = fiscalAddress;
    }

    public Long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(Long idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    public String toString() {
        return "Cliente: "  + firstName +" "+ lastName +
                " | DNI: " + idNumber +
                " | Telefono: " + phoneNumber +
                " | Direccion de envios: " + deliveryAddress +
                " | Direccion Fiscal: " + fiscalAddress +
                " | email: " + email;
    }
}
