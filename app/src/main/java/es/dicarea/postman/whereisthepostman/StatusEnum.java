package es.dicarea.postman.whereisthepostman;

public enum StatusEnum {
    NO_DEFINIDO("Ninguno"),
    PRE_REGISTRADO("Pre-registrado"),
    ADMITIDO("Admitido"),
    EN_ENTREGA("En proceso de entrega"),
    ENTREGADO("Entregado");

    private String name;

    StatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
