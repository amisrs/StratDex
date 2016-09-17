package com.amisrs.gavin.stratdex.model;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.amisrs.gavin.stratdex.MainActivity;
import com.amisrs.gavin.stratdex.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Gavin on 15/09/2016.
 */
public class PokemonSpecies {
    private String url = "blank/pokemon-species/0/";
    private String name;

    private String id;
    private Bitmap smallSprite;



    public PokemonSpecies(String url, String name) {

        this.url = url;
        this.name = name;
        Bitmap bmp = BitmapFactory.decodeResource(MainActivity.context.getResources(), R.drawable.default_sprite);
        smallSprite = bmp;


    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Bitmap getSmallSprite() {
        return smallSprite;
    }


    public void setSmallSprite(Bitmap smallSprite) {
        this.smallSprite = smallSprite;
    }

    public void setIdFromUrl() {
        System.out.println("setting id from url");
        Pattern idPattern = Pattern.compile(".+?/pokemon-species/(.+)*/");
        Matcher idMatcher = idPattern.matcher(url);
        System.out.println("trying to match for " + url + "  " + idMatcher.find());
        id = idMatcher.group(1);
        System.out.println(id);
    }

    public Bitmap fillDetailsWithRequests() {
        System.out.println("filling details with requests... for " + name);
        String spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";

        DownloadImageAsync downloadImageAsync = new DownloadImageAsync();
        Bitmap retval = null;

        try {
            retval = downloadImageAsync.execute(spriteUrl).get();
            System.out.println("got the sprite");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return retval;
    }

    public interface getDetailsService {
        @GET("api/v2/pokemon/{id}")
        Call<ResponseBody> getDetails(@Path(value = "id", encoded = true) String id);
    }

    class DownloadImageAsync extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap pic = null;

            try {
                InputStream in = new URL(strings[0]).openStream();
                pic = BitmapFactory.decodeStream(in);
                in.close();
                System.out.println("inside async...got sprite as bitmap for " + name);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            smallSprite = pic;
            System.out.println("picture gotten for " + name + "smallsprite = " + smallSprite.getByteCount());

            return pic;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
        }
    }

}
