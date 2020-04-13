package zone.com.sdk.API.file.api;

import java.io.File;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import zone.com.sdk.base.BaseImpl;

/**
 * [2017] by Zone
 */

public class FileImpl extends BaseImpl<FileService> implements FileService {

    @Override
    public Call<String> uploadFile(MultipartBody.Part file) {
        return dialogWrapper(mService.uploadFile(file));
    }

    @Override
    public Call<ResponseBody> downLoad() {
        File file = new File("/Users/fuzhipeng/Downloads");
        return downLoadWrapper(mService.downLoad(), file);
    }

    @Override
    public Call<String> uploadFiles(Map<String, RequestBody> maps) {
        return mService.uploadFiles(maps);
    }
}
