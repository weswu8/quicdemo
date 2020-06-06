package com.weswu.cloudcdn;

public class Images {
    private static String[] imageUrls = {
            "https://cdn.obwiz.com/01.jpeg",
            "https://cdn.obwiz.com/02.jpg",
            "https://cdn.obwiz.com/03.jpeg",
            "https://cdn.obwiz.com/04.jpeg",
            "https://cdn.obwiz.com/05.jpeg",
            "https://cdn.obwiz.com/06.jpg",
            "https://cdn.obwiz.com/07.jpg",
            "https://cdn.obwiz.com/08.jpg",
            "https://cdn.obwiz.com/09.jpeg"
    };

    public static int count() {
        return imageUrls.length;
    }

    public static String getUrl(int position) {
        return imageUrls[position];
    }
}
