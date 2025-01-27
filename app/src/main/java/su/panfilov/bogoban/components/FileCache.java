package su.panfilov.bogoban.components;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context){
        //Find the dir to save cached images
//        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
//            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"Savings");
//        else
        cacheDir=context.getCacheDir();
        if(!cacheDir.exists()) {
            if (!cacheDir.mkdirs()) {
                Log.e("FileCache", "Can not create cache dir");
            }
        }
    }

    public File getFile(String url){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename= String.valueOf(Utils.md5(url));
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;

    }

    public void putStringToFile(String url, String data)
    {
        String filename = String.valueOf(Utils.md5(url));

        //Log.e("FileCache", "Put to file " + filename + " string " + data);

        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);

        f.delete();

        try
        {
            try {
                if (!f.createNewFile()) {
                    Log.e("FileCache", "Can not create file " + filename);
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
            FileOutputStream fOut = new FileOutputStream(f);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {
            Log.e("FileCache Exception", "File write failed: " + e.toString());
        }
    }

    public String getStringFromFile(String url) {
        String result = "";

        String filename = String.valueOf(Utils.md5(url));

        File file = new File(cacheDir, filename);
        if ( file.exists() ) {
            //byte[] buffer = new byte[(int) new File(filePath).length()];
            FileInputStream fis = null;
            try {
                //f = new BufferedInputStream(new FileInputStream(filePath));
                //f.read(buffer);

                fis = new FileInputStream(file);
                BufferedReader bReader = new BufferedReader(new InputStreamReader(fis));
                String aDataRow = "";
                StringBuilder buffer = new StringBuilder();
                while ((aDataRow = bReader.readLine()) != null) {
                    buffer.append(aDataRow);
                }
                result = buffer.toString();
                bReader.close();
            } catch (Exception e) {
                Log.d("FileCache Exception", e.toString());
            } finally {
                if (fis != null)
                    try {
                        fis.close();
                    } catch (IOException e) {
                        Log.e("FileCache Exception", "File write failed: " + e.toString());
                    }
            }
            //result = new String(buffer);
        } else {
            Log.e("FileCache", "File not exists " + filename);
        }

        Log.e("FileCache", "File string: " + result);

        return result;
    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

    public void deleteFile(String url){
        String filename = String.valueOf(Utils.md5(url));

        File file = new File(cacheDir, filename);
        if ( file.exists() ) {
            file.delete();
        }
    }

}