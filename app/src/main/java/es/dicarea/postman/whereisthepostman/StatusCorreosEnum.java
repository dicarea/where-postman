package es.dicarea.postman.whereisthepostman;

public enum StatusCorreosEnum {
    NO_DEFINIDO("Ninguno", 0),
    PRE_REGISTRADO("Pre-registrado", 1),
    ADMITIDO("Admitido", 2),
    EN_ENTREGA("En proceso de entrega", 3),
    PENDIENTE_RECOGIDA("Envío pendiente de ser recogido en Oficina Postal", 4),
    ENTREGADO("Entregado", 5),
    DESCONOCIDO("Desconocido", 6);

    private String name;
    private int order;

    StatusCorreosEnum(String name, int order) {
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return this.name;
    }

    public int getOrder() {
        return this.order;
    }

    public static StatusCorreosEnum getStatus(String name) {
        for (StatusCorreosEnum c : values()) {
            if (c.name.equals(name)) {
                return c;
            }
        }
        return DESCONOCIDO;
    }

    public static StatusCorreosEnum getStatus(int n) {
        for (StatusCorreosEnum c : values()) {
            if (c.order == n) {
                return c;
            }
        }
        return null;
    }
}
