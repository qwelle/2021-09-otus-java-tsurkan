package atm;

class Money {
    private Integer nominal;
    private Integer qnty;

    Money(Integer nominal, Integer qnty) {
        this.nominal = nominal;
        this.qnty = qnty;
    }

    Integer getQnty() {
        return this.qnty;
    }

    public Integer getNominal() {
        return nominal;
    }
}
