package br.com.luciano.ecommerce;

public class Email {

    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Email{" +
                "subject='" + subject + '\'' +
                '}';
    }
}
