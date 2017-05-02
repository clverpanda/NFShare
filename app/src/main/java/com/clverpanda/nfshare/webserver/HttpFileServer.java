package com.clverpanda.nfshare.webserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.StringTokenizer;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.IStatus;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;
import org.nanohttpd.util.ServerRunner;

import static org.nanohttpd.protocols.http.response.Response.newFixedLengthResponse;

/**
 * Created by clverpanda on 2017/5/2 0002.
 * It's the file for NFShare.
 */

public class HttpFileServer extends NanoHTTPD
{
    private File mSharedFile;

    public HttpFileServer(int port)
    {
        super(port);
    }

    public HttpFileServer(int port, File file)
    {
        super(port);
        this.mSharedFile = file;
    }

    @Override
    public Response serve(IHTTPSession session)
    {
        Map<String, String> header = session.getHeaders();
        Map<String, String> parms = session.getParms();
        String uri = session.getUri();
        return newFixedLengthResponse(Status.OK, "text/html", "HelloWorld");
    }


    //提供文件下载，支持断点续传
    Response serveFile(String uri, Map<String, String> header, File file, String mime) {
        Response res;
        try
        {
            //计算etag
            String etag = Integer.toHexString((file.getAbsolutePath() + file.lastModified() + "" + file.length()).hashCode());

            //获取请求的范围
            long startFrom = 0;
            long endAt = -1;
            String range = header.get("range");
            if (range != null)
            {
                if (range.startsWith("bytes="))
                {
                    range = range.substring("bytes=".length());
                    int minus = range.indexOf('-');
                    try
                    {
                        if (minus > 0)
                        {
                            startFrom = Long.parseLong(range.substring(0, minus));
                            endAt = Long.parseLong(range.substring(minus + 1));
                        }
                    }
                    catch (NumberFormatException ignored) {
                    }
                }
            }

            // 处理if-range头
            String ifRange = header.get("if-range");
            boolean headerIfRangeMissingOrMatching = (ifRange == null || etag.equals(ifRange));

            // 处理if-none-match头
            String ifNoneMatch = header.get("if-none-match");
            boolean headerIfNoneMatchPresentAndMatching = ifNoneMatch != null && ("*".equals(ifNoneMatch) || ifNoneMatch.equals(etag));

            long fileLen = file.length();

            if (headerIfRangeMissingOrMatching && range != null && startFrom >= 0 && startFrom < fileLen)
            {
                if (headerIfNoneMatchPresentAndMatching)
                {
                    // 部分，未改变
                    res = newFixedLengthResponse(Status.NOT_MODIFIED, mime, "");
                    res.addHeader("ETag", etag);
                }
                else
                {
                    // 部分文件请求
                    if (endAt < 0)
                    {
                        endAt = fileLen - 1;
                    }
                    long newLen = endAt - startFrom + 1;
                    if (newLen < 0) {
                        newLen = 0;
                    }

                    FileInputStream fis = new FileInputStream(file);
                    fis.skip(startFrom);

                    res = newFixedLengthResponse(Status.PARTIAL_CONTENT, mime, fis, newLen);
                    res.addHeader("Accept-Ranges", "bytes");
                    res.addHeader("Content-Length", "" + newLen);
                    res.addHeader("Content-Range", "bytes " + startFrom + "-" + endAt + "/" + fileLen);
                    res.addHeader("ETag", etag);
                }
            }
            else
            {
                if (headerIfRangeMissingOrMatching && range != null && startFrom >= fileLen)
                {
                    // 部分文件请求，范围不正确
                    res = newFixedLengthResponse(Status.RANGE_NOT_SATISFIABLE, NanoHTTPD.MIME_PLAINTEXT, "");
                    res.addHeader("Content-Range", "bytes */" + fileLen);
                    res.addHeader("ETag", etag);
                }
                else if (range == null && headerIfNoneMatchPresentAndMatching)
                {
                    // 完整文件请求，未改变
                    res = newFixedLengthResponse(Status.NOT_MODIFIED, mime, "");
                    res.addHeader("ETag", etag);
                }
                else if (!headerIfRangeMissingOrMatching && headerIfNoneMatchPresentAndMatching)
                {
                    res = newFixedLengthResponse(Status.NOT_MODIFIED, mime, "");
                    res.addHeader("ETag", etag);
                }
                else
                {
                    // 提供完整文件
                    res = newFixedFileResponse(file, mime);
                    res.addHeader("Content-Length", "" + fileLen);
                    res.addHeader("ETag", etag);
                }
            }
        } catch (IOException ioe) {
            res = getForbiddenResponse("读取文件失败！");
        }
        return res;
    }

    private Response newFixedFileResponse(File file, String mime) throws FileNotFoundException
    {
        Response res;
        res = Response.newFixedLengthResponse(Status.OK, mime, new FileInputStream(file), file.length());
        res.addHeader("Accept-Ranges", "bytes");
        return res;
    }

    protected Response getForbiddenResponse(String s) {
        return Response.newFixedLengthResponse(Status.FORBIDDEN, NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: " + s);
    }

}
