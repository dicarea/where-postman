package es.dicarea.postman.whereisthepostman;

public enum StatusEnum {
    NO_DEFINIDO("Ninguno", 0),
    PRE_REGISTRADO("Pre-registrado", 1),
    ADMITIDO("Admitido", 2),
    EN_ENTREGA("En proceso de entrega", 3),
    ENTREGADO("Entregado", 4);

    private String name;
    private int order;

    StatusEnum(String name, int order) {
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return this.name;
    }

    public int getOrder() {
        return this.order;
    }
}
