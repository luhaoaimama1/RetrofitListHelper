package zone.com.okhttplib.java.callwrapper.down;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import zone.com.okhttplib.android.MainHandlerUtils;
import zone.com.okhttplib.java.callwrapper.core.ProgressCallback;

/**
 * Created by Administrator on 2016/6/18.
 */
public class DownLoadUtils {

    public static void saveFile(ProgressCallback listener, ResponseBody response, String urlString, File saveFile) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;

        try {
            long mPreviousTime = System.currentTimeMillis();
            is = response.byteStream();
            final long total = response.contentLength();
            long current = 0;

            if (saveFile.isDirectory()) {
                String fileName = getFileNameByUrl(urlString);
                fos = new FileOutputStream(new File(saveFile, fileName));
            } else
                fos = new FileOutputStream(saveFile);

            while ((len = is.read(buf)) != -1) {
                current += len;
                fos.write(buf, 0, len);

                if (listener != null) {
                    //calculate  networkSpeed
                    long totalTime = (System.currentTimeMillis() - mPreviousTime) / 1000;
                    if (totalTime == 0)
                        totalTime += 1;
                    MainHandlerUtils.onLoading(listener, total, current, current / totalTime, current == total);
                }
            }
            fos.flush();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }

    public static String getFileNameByUrl(String urlString) {
        String[] lin = urlString.split("[/]");
        for (int i = lin.length - 1; i >= 0; i--) {
            if (lin[i].contains("."))
                return lin[i];
        }
        throw new IllegalStateException("not found  file name!");
    }
}
