package com.xw.project.gracefulmovies.data.ao;

import android.text.TextUtils;

import java.util.List;

public class MovieStills {

    public List<Stills> list;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static class Stills {

        public String imgUrl;

        private String imageTiny;

        public String getImageTiny() {
            if (imageTiny == null && !TextUtils.isEmpty(imgUrl) && imgUrl.contains("_1280X720X2")) {
                imageTiny = imgUrl.replace("_1280X720X2", "");
            }

            return imageTiny;
        }
    }
}