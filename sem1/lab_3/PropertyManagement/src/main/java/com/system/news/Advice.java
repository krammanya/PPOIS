package com.system.news;

public class Advice extends Publication {
    private String tip1;
    private String tip2;
    private String tip3;

    public Advice(String title, String content, Author author,
                  String tip1, String tip2, String tip3) {
        super(title, content, author);
        this.tip1 = tip1;
        this.tip2 = tip2;
        this.tip3 = tip3;
    }

    @Override
    public String getType() {
        return "Совет";
    }

    public String getAllTips() {
        return String.format("1. %s\n2. %s\n3. %s", tip1, tip2, tip3);
    }
}