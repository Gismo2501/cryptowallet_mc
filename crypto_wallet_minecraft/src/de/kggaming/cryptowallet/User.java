package de.kggaming.cryptowallet;

public class User {
    private String name;
    private float bitcoin_amount;
    

    public User(String name, float bitcoin_amount) {
        this.name = name;
        this.bitcoin_amount = bitcoin_amount;
    }
    
    public User(String name) {
        this(name, 0);
    }

}
