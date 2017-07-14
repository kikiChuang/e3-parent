import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by xuhongliang on 2017/7/14.
 */
public class Test {

    //上传文件
    @org.junit.Test
    public   void test() {
//客户端配置文件
         String conf_filename = "/Users/xuhongliang/Desktop/E2System/fastdfsClient/src/cn/itcast/fastdfs/cliennt/fdfs_client.conf";
        //本地文件，要上传的文件
         String local_filename = "/Users/xuhongliang/Desktop/壁纸/1463912424378.jpg";
        //for(int i=0;i<100;i++){

        try {
            ClientGlobal.init(conf_filename);

            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;

            StorageClient storageClient = new StorageClient(trackerServer,
                    storageServer);
            NameValuePair nvp [] = new NameValuePair[]{
                    new NameValuePair("item_id", "100010"),
                    new NameValuePair("width", "80"),
                    new NameValuePair("height", "90")
            };
            String fileIds[] = storageClient.upload_file(local_filename, null,
                    nvp);

            System.out.println(fileIds.length);
            System.out.println("组名：" + fileIds[0]);
            System.out.println("路径: " + fileIds[1]);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            //}
        }
    }
}
