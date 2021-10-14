package com.voxeldev.wristtheorist.models;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.voxeldev.wristtheorist.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class TheoristStorage {

    private final Context context;
    public List<TheoristBook> bookList;

    public TheoristStorage(Context context){
        this.context = context;

        File file = new File(context.getFilesDir().getPath() + "/books.json");
        if (!file.exists()){
            bookList = new ArrayList<>();
            return;
        }

        try {
            bookList = new Gson().fromJson(new BufferedReader(new FileReader(file)).readLine(), new TypeToken<List<TheoristBook>>(){}.getType());
        } catch (Exception e) {
            Log.e(MainActivity.LOG_TAG, "Error while loading books: " + e.getMessage());
            bookList = new ArrayList<>();
        }
    }

    public boolean addBook(String url){
        if (url == null || url.isEmpty()){
            return false;
        }

        TheoristBook book = getBook(url);
        if (book != null){
            bookList.add(book);
            saveBooks();
            return true;
        }

        return false;
    }

    public boolean refreshBook(int index){
        try{
            TheoristBook book = getBook(bookList.get(index).url);

            if (book != null){
                bookList.set(index, book);
                saveBooks();
                return true;
            }
        }
        catch (Exception e){
            Log.e(MainActivity.LOG_TAG, "Error while refreshing book: " + e.getMessage());
        }

        return false;
    }

    public boolean deleteBook(int index){
        try{
            bookList.remove(index);
            saveBooks();
            return true;
        }
        catch (Exception e){
            Log.e(MainActivity.LOG_TAG, "Error while deleting book: " + e.getMessage());
        }

        return false;
    }

    private TheoristBook getBook(String url){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
            public void onAvailable(Network network) {
                super.onAvailable(network);
                connectivityManager.bindProcessToNetwork(network);
            }
            public void onLost(Network network) {
                super.onLost(network);
            }
        };
        connectivityManager.requestNetwork(
                new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build(),
                callback
        );
    
        try {
            TheoristBook book = new Gson().fromJson(
                    new OkHttpClient().newCall(new Request.Builder().url(url).build())
                            .execute().body().string(), TheoristBook.class);
            book.url = url;
            return book;
        }
        catch (Exception e){
            Log.e(MainActivity.LOG_TAG, "Error while getting book: " + e.getMessage());
        }
        finally {
            connectivityManager.bindProcessToNetwork(null);
            connectivityManager.unregisterNetworkCallback(callback);
        }

        return null;
    }

    private void saveBooks(){
        try{
            File file = new File(context.getFilesDir().getPath() + "/books.json");
            file.getParentFile().mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(new Gson().toJson(bookList));
            writer.close();
        }
        catch (Exception e){
            Log.e(MainActivity.LOG_TAG, "Error while saving bookList: " + e.getMessage());
        }
    }
}
