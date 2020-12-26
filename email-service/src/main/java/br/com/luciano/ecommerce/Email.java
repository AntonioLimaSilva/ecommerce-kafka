package br.com.luciano.ecommerce;

public class Email {

    private String subject;

    public Email() {}

    public Email(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
